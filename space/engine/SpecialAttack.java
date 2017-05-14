package space.engine;

import space.Engine;
import space.color;
import space.qmath;

import java.awt.*;
import java.util.*;

public abstract class SpecialAttack extends EffectGenerator implements IColor {
	
	public static abstract class FXParticle extends Particle {
		
		protected boolean _color;
		protected float _damage;
		protected float _decay;
		protected vect[] _trail;
		protected int _trailIndex;
		protected int _trailLength;
		protected boolean _active;
		protected int _inactivityCounter;
		
		public FXParticle(vect position, vect velocity, float friction, float damage, boolean color){
			this(position,velocity,friction,damage,color,color?(WHITES[qmath.randomInt()%10]):(BLACKS[qmath.randomInt()%10]));
		}
		
		public FXParticle(vect position, vect velocity, float friction, float damage, boolean color, Color colorValue){
			super(position, velocity, friction, colorValue);
			this._color = color;
			this._damage = damage;
			switch(Engine.QUALITY){
				case Engine.LOW: this._trailLength = 6; break;
				case Engine.DEFAULT: default: this._trailLength = 12; break;
				case Engine.HIGH: this._trailLength = 24; break;
			}
			this._decay = 1f;
			this._trail = new vect[this._trailLength];
			this._trailIndex = 0;
			for(int i = 0; i < this._trailLength; i++) this._trail[i] = this._p;
			this._active = true;
			this._inactivityCounter = 0;
		}
		
		public void loop(){
			super.loop();
			if(this._active){
				
				this.fx_loop();
				
				if(this._trailIndex >= this._trailLength) this._trailIndex = 0; 
				this._trail[this._trailIndex++] = this._p;
			} else {
				this._v = this._v.scale(0.7f);
				if(this._trailIndex >= this._trailLength) this._trailIndex = 0;
				this._trail[this._trailIndex++]= this._p; 
				if(this._inactivityCounter++ >= this._trailLength)
					_root.remove(this);
			}
		}
		
		public void paint(Graphics2D g){
			int size = 5;
			float da = 1f/(1.5f*(float)this._trailLength*(float)(this._inactivityCounter+1));
			float a = 0;
			
			for(int i = 0; i < this._trailLength; i++){
				int j = (this._trailIndex + i) % this._trailLength;
				a += da;
				vect v = this._trail[j];
				int x = v.x(vect.ROUND);
				int y = v.y(vect.ROUND);
				g.setColor(color.setAlpha(super._color,a*this._decay));
				g.fillRect(x-(size>>1),y-(size>>1),size,size);
			}
		}
		
		public void fx_loop(){}
		
	}
	
	protected final static Color[] WHITES = new Color[10];
	protected final static Color[] BLACKS = new Color[10];
	{
		for(int i = 0; i < 10; i++){
			float r = (float)i/10f;
			Color w = color.brighten(Engine.WHITE, r);
			Color b = color.brighten(Engine.BLACK, r);
			WHITES[i] = w;
			BLACKS[i] = b;
		}
	}
	
	protected boolean _color;
	protected float _damage;
	
	public boolean color(){ return this._color; }
	public float damage(){ return this._damage; }
	
	public SpecialAttack(){ this(new vect(), 0f, false); }
	public SpecialAttack(vect position, float damage, boolean color){
		this._p = position;
		this._v = new vect();
		this._damage = damage;
		this._color = color;
	}
	
}