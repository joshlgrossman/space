package space.engine;

import space.Engine;
import space.color;
import space.qmath;

import java.awt.*;
import java.util.*;

public class HomingAttack extends SpecialAttack {
	
	public static class FXParticle extends SpecialAttack.FXParticle {
		
		GameObject _target;
		
		public FXParticle(vect position, vect velocity, GameObject target, float damage, boolean color){
			super(position, velocity, 0.98f, damage,  color);
			this._target = target;
			this._damage = damage;
		}
		
		public void fx_loop(){
			if(this._target != null && this._target.exists()){
				if(this._target.boundingBox().hitTest(this._p)){
					this._target.hit(this._damage,this._color);
					this._target = null;
				} else {
					vect d = this._target._p.sub(this._p);
					if(this._visible && d.lengthSq() < 4) {
						if((this._decay *= 0.8f) < 0.05f)
							this._active = false;
					} else {
						d = d.qnormalize().scale(0.3f);
						this._v = this._v.scale(0.995f).add(d);
						float lsq = this._v.lengthSq();
						if(lsq > 25)
							this._v = this._v.scale(0.7f);
					}
				}
			} else
				this._active = false;
		}
		
	}
	
	protected final static int PARTICLE_COUNT = Engine.QUALITY*25+75;
	
	public HomingAttack(vect position, float damage, boolean color){
		super(position,damage,color);
	}
	
	public void display(){
		ArrayList<Enemy> enemies = _root.enemies;
		int num_enemies = enemies.size();
		if(num_enemies > 0){
			_root.add(new Blast(this._p,this._v,125f,0.05f,this._color));
			_root.add(new Blast(this._p,this._v,100f,0.09f,this._color));
			int num_particles = PARTICLE_COUNT/num_enemies;
			float dmg_per = this._damage/(float)num_particles;
			for(Enemy e : enemies){
				for(int i = 0; i < num_particles; i++){
					vect v = vect.qcreatePolar(qmath.random()*3.5f+3.5f, qmath.randomAngle());
					FXParticle p = new FXParticle(this._p,v,e, dmg_per,this._color);
					_root.add(p);
				}
			}
		} else {
			_root.add(new Blast(this._p,this._v,100f,0.05f,this._color));
			_root.add(new Blast(this._p,this._v,90f,0.09f,this._color));
			_root.add(new Blast(this._p,this._v,50f,0.1f,this._color));
			for(int i = 0; i < 50; i++){
				vect v = vect.qcreatePolar(qmath.random()*3.5f+3.5f, qmath.randomAngle());
				int color_index = qmath.randomInt()%10;
				Color c = (this._color)?(WHITES[color_index]):(BLACKS[color_index]);
				Particle p = new Particle(this._p,v,0.96f,c);
				_root.add(p);
			}
		}
	}
	
}
/*
public static class FXParticle extends Particle {

GameObject _target;
int _counter;
vect[] _previous;
int _index;
int num_prev;
float _decay;
float _damage;
boolean _color;
Color _colorVal;

public FXParticle(vect position, vect velocity, GameObject target, Color color_val, float damage, boolean color){
	super(position, velocity, 0.98f, color_val);
	this._target = target;
	this._damage = damage;
	if(Engine.LOW_QUALITY)
		this.num_prev = 6;
	else if(Engine.DEFAULT_QUALITY)
		this.num_prev = 12;
	else
		this.num_prev = 24;
	this._previous = new vect[num_prev];
	this._index = 0;
	this._counter = 0;
	this._decay = 1f;
	this._color = color;
	this._colorVal = color_val;
	for(int i = 0; i < num_prev; i++)
		this._previous[i] = position;
}

public void loop(){
	super.loop();
	if(this._target != null && this._target.exists()){
		if(this._target.boundingBox().hitTest(this._p)){
			this._target.hit(this._damage,this._color);
			this._target = null;
		} else {
			vect d = this._target._p.sub(this._p);
			if(this._visible && d.lengthSq() < 4) {
				if((this._decay *= 0.8f) < 0.05f)
					_root.remove(this);
			} else {
				d = d.qnormalize().scale(0.3f);
				this._v = this._v.scale(0.995f).add(d);
				float lsq = this._v.lengthSq();
				if(lsq > 25)
					this._v = this._v.scale(0.7f);
			}
			
			if(this._index >= this.num_prev) this._index = 0;
			this._previous[this._index++] = this._p; 
		}
	} else {
		this._v = this._v.scale(0.7f);
		if(this._index >= this.num_prev) this._index = 0;
		this._previous[this._index++] = this._p; 
		if(this._counter++ >= this.num_prev)
			_root.remove(this);
	}
}

public void paint(Graphics2D g){
	int size = 5;
	float da = 1f/(1.5f*(float)num_prev*(float)(this._counter+1));
	float a = 0;
	
	for(int i = 0; i < num_prev; i++){
		int j = (this._index + i) % num_prev;
		a += da;
		vect v = this._previous[j];
		int x = v.x(vect.ROUND);
		int y = v.y(vect.ROUND);
		g.setColor(color.setAlpha(this._colorVal,a*this._decay));
		g.fillRect(x-(size>>1),y-(size>>1),size,size);
	}
}

}*/