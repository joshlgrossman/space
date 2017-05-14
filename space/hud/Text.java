package space.hud;

import java.awt.*;
import space.*;

public class Text extends Element {
	
	protected Font _font;
	protected int _size;
	protected int _styleFlags;
	protected String _text;
	protected int _targetX;
	protected int _targetY;
	protected boolean _move;
	protected boolean _visible;
	protected Color _color;
	
	public Text(){ this("",0,0,0,0); }
	public Text(int x, int y){ this("",x,y,x,y); }
	public Text(int x, int y, int targetX, int targetY){ this("",x,y,targetX, targetY); }
	public Text(String text, int x, int y){ this(text,x,y,x,y); }
	public Text(String text, int x, int y, int targetX, int targetY){
		super(x,y);
		this._text = text;
		this._x = x;
		this._y = y;
		this._targetX = targetX;
		this._targetY = targetY;
		this._move = x == targetX && y == targetY;
		this._visible = true;
		this._color = color.WHITE;
		this._size = 12;
		this._styleFlags = Font.PLAIN;
		this._font = Engine.getFont(this._size,this._styleFlags);
	}
	
	public void loop(){
		if(this._move){
			int dx = this._targetX - this._x;
			int dy = this._targetY - this._y;
			if(dx <= 1) this._x = this._targetX;
			else this._x += this._targetX >> 2;
			if(dy <= 1) this._y = this._targetY;
			else this._y += this._targetY >> 2;
			this._move = this._x == this._targetX && this._y == this._targetY;
		}
	}
	
	public void paint(Graphics2D g){
		if(this._visible){
			g.setFont(this._font);
			g.setColor(color.DROPSHADOW);
			g.drawString(this._text,this._x+2,this._y+2);
			g.setColor(this._color);
			g.drawString(this._text, this._x,this._y);
		}
	}
	
	public void x(int value){ this._targetX = value; this._move = true; }
	public void y(int value){ this._targetY = value; this._move = true; }
	
	public Color color(){ return this._color; }
	public void color(Color value){ this._color = value; }
	
	public int size(){ return this._font.getSize(); }
	public void size(int value){ this._font = Engine.getFont((this._size = value),this._styleFlags); }
	
	public int style(){ return this._styleFlags; }
	public void style(int flags){ this._font = Engine.getFont(this._size, (this._styleFlags = flags)); }
	
	public String text(){ return this._text; }
	public void text(String value){ this._text = value; }
	
	public boolean visible(){ return this._visible; }
	public void visble(boolean value){ this._visible = value; }
	
	
}