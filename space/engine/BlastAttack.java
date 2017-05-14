package space.engine;

import space.Engine;
import space.color;
import space.qmath;

import java.awt.*;
import java.util.*;

public class BlastAttack extends SpecialAttack {
	
	public static class FXParticle extends SpecialAttack.FXParticle {
		
		float _angle;
		float _angularVel;
		
		public FXParticle(vect position, vect velocity, Color color_val, float damage, boolean color){
			super(position, velocity, 1f, damage, color, color_val);
			this._angle = qmath.randomAngle();
			this._angularVel = 3.1416f/(qmath.random()*75+25);
		}
		
		public void fx_loop(){
			if(this._p.y() >= 0){
				
				this._angle += this._angularVel; //~pi/100
				
				float ax = (0f - this._v.x())*0.1f + qmath.sin(this._angle)*0.025f;
				float ay = (ax*ax)/-4000f;
				this._v = this._v.add(new vect(ax,ay));
				
				ArrayList<Enemy> enemies = _root.enemies;
				for(Enemy e : enemies){
					if(e.boundingBox().hitTest(this._p)){
						e.hit(this._damage,this._color);
						this._active = false;
						break;
					}
				}
				
			} else
				this._active = false;
		}
		
		public void paint(Graphics2D g){
			super.paint(g);
			if(this._active){
				int x = this._p.x(vect.ROUND);
				int y = this._p.y(vect.ROUND);
				g.setColor(((Particle)this)._color);
				g.fillRect(x-3,y-3,6,6);
			}
		}
		
	}	
	
	protected final static int PARTICLE_COUNT = 60;
	
	public BlastAttack(vect position, float damage, boolean color){
		super(position,damage,color);
	}
	
	public void display(){
		_root.add(new Blast(this._p,this._v.add(new vect(0f,-4f)),100f,0.06f,this._color));
		_root.add(new Blast(this._p,this._v.add(new vect(0f,-4f)),60f,0.095f,this._color));
		
		float init_vx = -9f;
		float diff_vx = Math.abs(init_vx*2)/((float)PARTICLE_COUNT-1);
		
		int middle = PARTICLE_COUNT>>1;
		for(int i = 0; i < PARTICLE_COUNT; i++){
			int di = Math.abs(i - middle);
			int color_index = 9-(di*9)/middle;
			Color c = this._color?WHITES[color_index]:BLACKS[color_index];
			vect v = new vect(init_vx,-5.5f);
			vect pos = this._p;
			if(i < PARTICLE_COUNT-1) pos = pos.add(v);
			else pos = pos.add(new vect(init_vx,0f));
			float vxsq = init_vx*init_vx;
			pos = pos.add(new vect(0f,vxsq/4f));
			_root.add(new FXParticle(pos ,v, c, this._damage, this._color));
			init_vx += diff_vx;
		}
		
	}
	
}