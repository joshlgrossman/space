package space.hud;

import java.awt.*;

import space.color;
import space.engine.*;
import space.Engine;

public class Button extends Element implements IMouseClickListener, IFocusable {
	
	protected final static BasicStroke STROKE = new BasicStroke();
	protected final static Font FONT = Engine.getFont(12);
	
	protected int _width,_height;
	
	protected boolean _hasFocus;
	protected boolean _mouseOver;
	protected boolean _isPressed;
	
	protected Color _color;
	protected Color _focusColor;
	protected Color _currentColor;
	protected Color _activeColor;
	protected String _text;
	
	protected ButtonFunction _function;
	
	protected Menu _menu;
	
	public Button(int x, int y, int width, int height, Menu menu){
		this._x = x;
		this._y = y;
		this._width = width;
		this._height = height;
		
		this._hasFocus = false;
		this._isPressed = false;
		this._mouseOver = false;
		
		this._color = color.GREY;
		this._focusColor = color.MORANGE1;
		this._activeColor = color.LBLUE1;
		this._currentColor = this._color;
		
		this._menu = menu;
		
		this._function = (button) -> {};

	}
	
	public void loop(){
		vect m = Engine.ROOT.mouse;
		int mx = m.x(vect.ROUND);
		int my = m.y(vect.ROUND);
		this._mouseOver = (mx >= this._x && mx <= this._x + this._width && my >= this._y && my <= this._y + this._height);
		if(this._mouseOver)
			this._menu.requestFocus(this);
	}
	
	public void paint(Graphics2D g){
		Color c = this._hasFocus?this._focusColor:this._color;
		this._currentColor = color.mix(this._currentColor, c, 0.8f);
		g.setStroke(STROKE);
		int xw = this._x + this._width;
		int yh = this._y + this._height;
		g.setColor(color.HIGHLIGHT);
		g.drawLine(this._x, yh, this._x, this._y);
		g.drawLine(this._x, this._y, xw, this._y);
		g.setColor(color.DROPSHADOW);
		g.drawLine(xw, yh, xw, this._y);
		g.drawLine(this._x,yh, xw, yh);
		g.fillRect(this._x+4, this._y+4, this._width, this._height);
		g.setColor(this._currentColor);
		g.fillRect(this._x, this._y, this._width, this._height);
		
		int blur_x = 10;
		g.setColor(this._focusColor);
		g.fillRect(this._x,this._y,blur_x,this._height);
		float blur_quality = 4;
		int blur_width = 2;
		if(Engine.LOW_QUALITY){
			blur_quality = 2;
			blur_width = 3;
		} else if(Engine.HIGH_QUALITY){
			blur_quality = 10;
			blur_width = 1;
		}
		float blur_val = 1f;
		float blur_diff = 1f/(blur_quality+1);
		
		for(int i = 0; i < blur_quality; i++){
			blur_val -= blur_diff;
			g.setColor(color.mix(this._focusColor, this._currentColor, blur_val));
			g.fillRect(this._x+blur_x, this._y, blur_width, this._height);
			blur_x += blur_width;
		}
		
		int str_len = (this._text.length()-1) * 8;
		int diff = this._width - str_len;
		int x = this._x + (diff>>1);
		int y = this._y + (this._height>>1) + 3;
		
		g.setFont(FONT);
		g.setColor(color.DROPSHADOW);
		g.drawString(this._text, x+2, y+2);
		g.setColor(color.WHITE);
		g.drawString(this._text, x, y);
	}
	
	public Color color(){ return this._color; }
	public void color(Color value){ this._color = value; }
	
	public Color focusColor(){ return this._focusColor; }
	public void focusColor(Color value){ this._focusColor = value; }
	
	public void resetColor(){ this._currentColor = this._color; }
	
	public String text(){ return this._text; }
	public void text(String value){ this._text = value; }
	
	public ButtonFunction function(){ return this._function; }
	public void function(ButtonFunction function){ this._function = function; }
	
	public void activate(){
		this._currentColor = this._activeColor;
		this._function.accept(this);
	}
	
	public void setFocus(boolean value){ this._hasFocus = value; }
	
	public boolean hasFocus(){ return this._hasFocus; }
	
	public Menu menu(){ return this._menu; }
	
	public void mousePressed(){
		if(this._mouseOver)
			this.activate();
	}
	
	public void mouseClicked(){}
	
	public void mouseReleased(){}
	
	
	
}