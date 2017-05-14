package space.engine;

import space.Engine;
import java.awt.*;

public class GravityParticle extends Particle {
	
	protected Sprite _target;
	protected int _counter;
	
	public GravityParticle(vect position, vect velocity, float friction, Color color, Sprite target){
		super(position,velocity,friction,color);
		this._target = target;
		this._counter = 0;
		this._scale = 1;
	}
	
	public void loop(){
		if(this._target != null){
			vect delta = this._target._p.sub(this._p);
			this._v = this._v.scale(this._friction).add(delta.scale(0.005f));
			this._scale *= Math.max(this._friction,0.99);
			if(this._scale < 0.25f || delta.length() < 1) _root.remove(this);
		} else {
			this._v = this._v.scale(this._friction);
			this._scale *= this._friction;
			if(this._v.length() < 0.1) _root.remove(this);
		}
		this.move();
	}
	
}