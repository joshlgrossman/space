package space.engine;

import space.*;

import java.awt.*;
import java.util.*;


public class BombAttack extends SpecialAttack {
	
	public static class FXParticle extends SpecialAttack.FXParticle {
		
		public FXParticle(vect position, vect velocity, Color color_val, float damage, boolean color){
			super(position, velocity, 0.975f, damage, color, color_val);
		}
		
		public void fx_loop(){
			if(this._p.y() >= 0){
				
				this._decay *= 0.925f;
				
				if(this._v.lengthSq() >= 36f) this._v = this._v.scale(0.5f);
				else if(this._decay <= 0.01f) this._active = false;
				
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
		
	}	
	
	public static class Bomb extends Particle {
		
		float _damage;
		boolean _color;
        Color _glow;
        float _glowSize;
		int _timer;
		int _initTimer;
		int _detonationTime;
		
		public Bomb(vect position, vect velocity, float damage, boolean color, int timer_init){
			super(position,velocity,0.975f,Engine.getColor(color));
			this._damage = damage;
			this._color = color;
			this._initTimer = this._timer = timer_init;
			this._detonationTime = (timer_init*3) >> 2;
            this._glow = space.color.setAlpha(super._color, 0.3f);
            this._glowSize = 20f;
		}
		
		public void loop(){
			if(this._timer-- < this._detonationTime){
				if(this._timer <= 0 || Engine.keyToggled(Engine.KEY_X) || this._v.lengthSq() < 0.01f)
					this.explode();
            }
            this._glowSize *= 0.95f;
            super.loop();
	    }

		public void explode(){
			float num_particles = 125;
			float da = qmath.TAU/num_particles;
			_root.add(new Blast(this._p, 75f, 0.3f, this._color));
			_root.add(new Blast(this._p, 150f, 0.15f, this._color));
			for(float a = 0; a <= qmath.TAU; a+=da){
				vect v = vect.qcreatePolar(qmath.random()*5f+10f, a).add(this._v);
				vect p = this._p.add(v);
				Color c = (this._color)?WHITES[qmath.randomInt()%10]:BLACKS[qmath.randomInt()%10];
				_root.add(new FXParticle(p,v,c,this._damage,this._color));
			}
			_root.remove(this);
		}
		
		public void paint(Graphics2D g){
			int x = this._p.x(vect.ROUND);
			int y = this._p.y(vect.ROUND);
            int s = Math.round(20-this._glowSize);
            int hs = s>>1;
            g.setColor(this._glow);
            g.fillOval(x-hs, y-hs, s, s);
            g.fillOval(x-this._v.x(vect.ROUND)-4,y-this._v.y(vect.ROUND)-4,8,8);
			g.setColor(super._color);
			g.fillOval(x-3, y-3, 6, 6);
		}
		
	}
	
	
	public BombAttack(vect position, vect velocity, float damage, boolean color){
        super(position, damage, color);
        this._v = velocity;

    }
	
	public void display(){
		Bomb bomb = new Bomb(this._p, this._v.add(new vect(0f,-7f)), this._damage, this._color, 60);
		_root.add(bomb);
	}
	
}