package space.hud;

import java.awt.Font;
import java.awt.Graphics2D;
import space.engine.*;

public abstract class Element {
	
	protected int _x;
	protected int _y;
	
	public Element(){this(0,0);}
	
	public Element(int x, int y){
		this._x = x;
		this._y = y;
	}
	
	public final void update(Graphics2D g){
		this.loop();
		this.paint(g);
	}
	
	public int x(){ return this._x; }
	public int y(){ return this._y; }
	public void x(int value){ this._x = value; }
	public void y(int value){ this._y = value; }
	
	public vect position(){ return new vect(this._x, this._y); }
	
	public abstract void loop();
	public abstract void paint(Graphics2D g);
	
}