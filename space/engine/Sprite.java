package space.engine;

import java.awt.*;

import space.Engine;
import space.color;

public class Sprite {
	
	public static Engine _root = Engine.ROOT;
	
	private boolean _isBounded;
	
	private boolean _exists;
	
	//protected SpriteList _children;
	
	protected float _width;
	protected float _height;
	
	protected vect _p;
	protected vect _v;
	
	public Sprite(){
		this._p = new vect();
		this._v = new vect();
		//this._children = new SpriteList();
		this._exists = false;
		this.initialize();
		this._isBounded = this instanceof IBounded;
	}
	
	// Methods
	public boolean isOOB(){ return !this._p.isInside(Engine.TOP_LEFT, Engine.BOTTOM_RIGHT); }				// is Out Of Bounds
	public boolean isOOOB(){ return !this._p.isInside(Engine.OUTER_TOP_LEFT, Engine.OUTER_BOTTOM_RIGHT); }	// is Out Of Outer Bounds
	
	public void checkCollisions(){
		if(this._isBounded){
			IBounded bb = (IBounded)this;
			for(Obstacle o : _root.obstacles) 
				if(o.collision(bb))
					this.collide(o);
		} else 
			for(Obstacle o : _root.obstacles) 
				if(o.collision(this))
					this.collide(o);
	}
	
	// Getters/setters
	public void x(float value){ this._p.x(value); }
	public void y(float value){ this._p.y(value); }
	public float x(){ return this._p.x(); }
	public float y(){ return this._p.y(); }
	
	public vect position(){ return this._p; }
	
	public void vx(float value){ this._v.x(value); }
	public void vy(float value){ this._v.y(value); }
	public float vx(){ return this._v.x(); }
	public float vy(){ return this._v.y(); }
	
	public vect velocity(){ return this._v; }
	
	public float width(){ return this._width; }
	public float height(){ return this._height; }
	
	public final boolean exists(){ return this._exists; }
	
	public final void onDisplay(){
		this._exists = true;
		this.display();
	}
	
	public final void onRemove(){
		this._exists = false;
		this.remove();
	}
	
	// Override these functions
	public void initialize(){}
	public void display(){}
	public void loop(){}
	public void remove(){}
	public void paint(Graphics2D g){}
	public void onKeyEvent(byte keyCode, boolean isPressed){}
	
	public void collide(Obstacle that){ this._p = this._p.add(that.resolveCollision(this)); }
	
	// Debug functions
	public void __on_paint(Graphics2D g){
		int x = this._p.x(vect.ROUND);
		int y = this._p.y(vect.ROUND);
		int vx = this._v.x(vect.ROUND);
		int vy = this._v.y(vect.ROUND); 
		g.setColor(color.DEBUGCOLOR);
		g.setStroke(new BasicStroke());
		g.drawLine(x,y,x+vx,y+vy);
	}
	
}