package space.engine;

import space.Engine;
import space.color;
import space.qmath;

public class FireParticle extends Particle {
	
	protected boolean _spark;
	
	public FireParticle(vect position, vect velocity, float friction){
		this._p = position;
		this._v = velocity;
		this._friction = friction;
		this._scale = 1.0f;
		this._color = color.fire();
		this._spark = Math.random()*friction<0.1;
		if(this._spark) this._color = color.brighten(this._color, 0.5f);
	}
	
	public void loop(){
		this.move();
		if(Engine.LOW_QUALITY){
			if(this.isOOB() || this._v.lengthSq() < 0.0225) _root.remove(this);
			else this._scale *= this._friction;
		} else {
			if(this._spark && qmath.random()<0.5)
				this._v = this._v.qrotate((float)(Math.random()*1.570796f-0.78539816f));  // pi/2 - pi/4
			this.checkBounds();
		}
	}
	
}