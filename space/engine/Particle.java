package space.engine;

import space.Engine;
import space.color;
import java.awt.*;

public class Particle extends Sprite {
	
	protected float _friction;
	protected float _scale;
	protected Color _color;
	protected boolean _visible;
	
	
	public Particle(){ this(new vect(), new vect(), 0f, new Color(0,0,0,0)); }
	public Particle(vect position, vect velocity, float friction, Color col){
		this._p = position;
		this._v = velocity;
		this._friction = friction;
		this._scale = 1.0f;
		this._color = col;
		this._visible = true;
	}
	
	public void loop(){
		this.move();
		if(Engine.LOW_QUALITY){
			if(this.isOOB() || this._v.lengthSq() < 0.0225) _root.remove(this);
			else this._scale *= this._friction;
		} else {
			this.checkBounds();
		}
	}
	
	public void paint(Graphics2D g){
		int size = (int)Math.round(5*this._scale);
		int x = this._p.x(vect.ROUND);
		int y = this._p.y(vect.ROUND);
		g.setColor(color.mix(this._color,color.BROWN,this._scale));
		g.fillRect(x-(size>>1),y-(size>>1),size,size);
	}
	
	public void remove(){
		this._visible = false;
	}
	
	protected void checkBounds(){
		if(this._p.x() < 0) {
			this._p.x(0);
			this._v.x(-this._v.x());
		} else if (this._p.x() > Engine.STAGE_WIDTH-10){
			this._p.x(Engine.STAGE_WIDTH-10);
			this._v.x(-this._v.x());
		}
		if(this._v.lengthSq() < 0.0225 || this._p.y() < Engine.OUTER_TOP_LEFT.y() || this._p.y() > Engine.STAGE_HEIGHT)
			_root.remove(this);
		this._scale *= this._friction;
	}
	
	protected void move(){
		this._p = this._p.add(this._v);
		this._v = this._v.scale(this._friction);
	}
	
	public void collide(Obstacle that){
		vect v = that.resolveCollision(this);
		this._p = this._p.add(v);
		if(v.x() != 0)
			this._v.x(-this._v.x());
		else
			this._v.y(-this._v.y());
	}
	
}