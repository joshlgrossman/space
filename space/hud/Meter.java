package space.hud;

import java.awt.*;
import space.color;
import space.Engine;

public class Meter extends Element {
	
	protected static Font FONT = Engine.getFont(12);
	protected static BasicStroke STROKE = new BasicStroke(1.25f);
	protected static BasicStroke NORMAL_STROKE = new BasicStroke();
	
	protected String _text;
	protected float _value;
	protected float _currentValue;
	protected float _maxValue;
	protected int _width;
	protected int _height;
	protected Color _barColor;
	protected Color _bgColor;
	protected Color _fontColor;
	protected boolean _visible;
	protected boolean _isFillable;
	protected int _blinkCounter;
	protected int _barX;
	protected int _barY;

	public Meter(String text, int x, int y){ this(text,x,y,true); }
	public Meter(String text, int x, int y, boolean fillable){
		super(x,y);
		this._text = text;
		this._maxValue = 100;
		this._value = 0;
		this._currentValue = 0;
		this._width = 150;
		this._height = 8;
		this._fontColor = Color.white;
		this._barColor = Color.gray;
		this._bgColor = Color.darkGray;
		this._visible = true;
		this._barX = (text.length()-1)*10;
		this._barY = this._y - this._height;
		this._isFillable = fillable;
	}
	
	public void loop(){
		boolean full;
		if(!this._isFillable) full = this._value >= this._maxValue;
		else full = this._value > this._maxValue; 
		
		float d_val;
		if(full)	// if value is greater than max value, fill to max value
			d_val = (this._maxValue-this._currentValue);
		else
			d_val = (this._value - this._currentValue);
		
		if(d_val < 0.1f) { // if current value is almost equal to the expected value
			if(full){ // if meter is greater than max value then wrap value back to 0 and continue to fill to actual value
				this._value = this._value % this._maxValue;
				this._currentValue = 0f;
			} else // otherwise set current value to the actual value
				this._currentValue = this._value;
		} else {
			if(full)
				this._currentValue += d_val * 0.2; // fill twice as fast if the bar needs to wrap back to 0
			else
				this._currentValue += d_val * 0.1;
		}
	}
	
	public void paint(Graphics2D g){
		float scale = this._currentValue/this._maxValue;
		int fill_width = (int)Math.round(scale*this._width);
		int rest_width = this._width - fill_width;
		g.setStroke(STROKE);
		g.setFont(FONT);
		g.setColor(color.darken(this._fontColor,0.75f));
		g.drawString(this._text, this._x+1, this._y+2);
		g.setColor(this._fontColor);
		g.drawString(this._text, this._x, this._y);
		
		if(this._visible){
			Color col = color.mix(this._barColor,this._bgColor,scale*0.5f+0.5f);
			g.setColor(col);
			g.fillRect(this._barX, this._y-this._height, fill_width, this._height);
			g.setColor(this._bgColor);
			g.fillRect(this._barX+fill_width, this._barY, rest_width, this._height);
			if(this._currentValue == this._maxValue)
				g.setColor(color.setAlpha(color.brighten(this._bgColor, 0.5f),1f));	
			else 
				g.setColor(color.setAlpha(color.darken(this._bgColor,0.25f),1f));
			g.drawRect(this._barX, this._barY, this._width, this._height);
		}
		
		g.setStroke(NORMAL_STROKE);
	}
	
	public void setColors(Color barColor, Color bgColor, Color fontColor){
		this._barColor = barColor;
		this._bgColor = bgColor;
		this._fontColor = fontColor;
	}
	
	public void fill(){ this._value = this._maxValue; }
	
	public float value(){ return this._value; }
	public void value(float value){ this._value = value*this._maxValue; }
	
	public int width(){ return this._width; }
	public void width(int value){ this._width = value; }
	
	public Color barColor(){ return this._barColor; }
	public void barColor(Color value){this._barColor = value; }
	
	public Color bgColor(){ return this._bgColor; }
	public void bgColor(Color value){this._bgColor = value; }
	
	public Color fontColor(){ return this._fontColor; }
	public void fontColor(Color value){ this._fontColor = value; }
	
	public boolean isFillable(){ return this._isFillable; }
	public void isFillable(boolean value){ this._isFillable = value; }
	
}