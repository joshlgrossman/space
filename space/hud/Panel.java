package space.hud;

import space.color;
import space.hud.*;
import java.awt.*;

public class Panel extends Element {
	
	protected static Stroke STROKE = new BasicStroke();
	
	protected int _width;
	protected int _height;
	
	protected Color _color;
	
	protected boolean _visible;
	
	public Panel(int x, int y, int width, int height){
		this._x = x;
		this._y = y;
		this._width = width;
		this._height = height;
		this._visible = true;
	}
	
	
	public void loop(){}
	public void paint(Graphics2D g){
		if(this._visible){
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
			g.setColor(this._color);
			g.fillRect(this._x, this._y, this._width, this._height);
		}
	}
	
	public Color color(){ return this._color; }
	public void color(Color value){ this._color = value; }
	
	
}