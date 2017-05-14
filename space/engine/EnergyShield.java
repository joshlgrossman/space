package space.engine;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import space.*;

public class EnergyShield extends Sprite implements ActionListener {
	
	public static final int DEFAULT = 0;
	public static final int SHIELDING = 1;
	public static final int FAILING = 2;
	public static final int RECHARGING = 3;
	public static final int FADING = 4;
	
	protected static final float MAX_ALPHA = 0.75f;
	protected static final float FAIL_ALPHA = 0.5f;
	
	protected int _state;
	protected int _counter; // counter for recurring actions by frame
	protected float _alpha;
	protected boolean _visible;
	protected float _radius;
	protected float _growthRate; // radius = radius + growthRate
	protected float _maxRadius;
	protected float _absorbRate; // percentage of damage that this shield absorbs
	protected int _rechargeDelay; // milliseconds to begin recharging
	protected int _longRechargeDelay; // milliseconds to begin recharging after shield breaks
	protected float _rechargeRate; // percentage of maxSP recovered each frame
	protected float _SP; // shield points (equivalent to HP)
	protected float _maxSP;
	
	protected GameTimer _rechargeTimer;
	protected Color _color;
	protected Color _failColor;
	protected GameObject _target;
	
	public EnergyShield(GameObject target){
		this._absorbRate = 1f;
		this._rechargeRate = 1;
		this._rechargeDelay = 1000;
		this._maxSP = this._SP = 1f; // maxSP of 0 could lead to divide by 0 error
		this._radius = this._maxRadius = 1f;
		this._target = target;
	}
	
	public EnergyShield(GameObject target, float radius, float shieldPoints, float absorbRate, float rechargeRate, int rechargeDelay){
		this._absorbRate = absorbRate;
		this._rechargeRate = rechargeRate;
		this._rechargeDelay = rechargeDelay;
		this._maxSP = this._SP = shieldPoints;
		this._radius = this._maxRadius = radius;
		this._target = target;
	}
	
	public void checkCollisions(){}
	
	public void initialize(){
		this._alpha = 0f;
		this._visible = false;
		this._state = DEFAULT;
		this._color = color.SHIELDCOLOR;
		this._failColor = color.SHIELDFAILCOLOR;
		this._rechargeTimer = new GameTimer(this._rechargeDelay,this);
		this._longRechargeDelay = (this._rechargeDelay << 1) + (this._rechargeDelay >> 1); // longdelay = delay*2.5
		this._counter = 0;
	}
	
	public void actionPerformed(ActionEvent ae){
		this._rechargeTimer.stop();
		this._visible = true;
		this._state = RECHARGING;
	}
	
	public void setProperties(float radius, float shieldPoints, float absorbRate, float rechargeRate, int rechargeDelay){
		this._maxRadius = this._radius = radius;
		this._absorbRate = absorbRate;
		this._rechargeRate = rechargeRate;
		this._rechargeDelay = rechargeDelay;
		this._longRechargeDelay = (this._rechargeDelay << 1) + (this._rechargeDelay >> 1); // longdelay = delay*2.5
		this._maxSP = this._SP = shieldPoints;
		this._rechargeTimer = new GameTimer(this._rechargeDelay,this);
	}

	public float absorbDamage(float damage){
		float dmg = Math.min(damage*this._absorbRate,this._SP);
		float prev_sp = this._SP;
		if((this._SP -= dmg) > 0){
			this.flash();
			this._rechargeTimer.setDelay(this._rechargeDelay);
			this._rechargeTimer.restart();
		} else if(prev_sp > 0){
			this.fail();
			this._rechargeTimer.setDelay(this._longRechargeDelay);
			this._rechargeTimer.restart();
		}
		return damage - dmg;
	}
	
	protected void flash(){
		this._alpha = this._SP/this._maxSP * MAX_ALPHA;
		this._visible = true;
		this._state = SHIELDING;
	}
	
	protected void fail(){
		if(this._state != FAILING){
			this._alpha = FAIL_ALPHA;
			this._visible = true;
			this._state = FAILING;
			this._radius = this._maxRadius;
			this._growthRate = 0.5f;
		}
	}
	
	public void loop(){
		if(this._visible){
			switch(this._state){
				case SHIELDING:
					this._alpha *= 0.85f;
					if(this._alpha < 0.01f){
						this._alpha = 0f;
						this._visible = false;
						this._state = DEFAULT;
					}
					break;
				case FAILING:
					this._radius += (this._growthRate -= 0.05); 
					if(this._radius <= 1f){
						this._alpha = 0f;
						this._visible = false;
						this._state = DEFAULT;
						this._target.blink();
					}
					break;
				case RECHARGING:
					this._SP += this._maxSP*this._rechargeRate;
					if(this._SP >= this._maxSP){
						this._SP = this._maxSP;
						this._alpha = MAX_ALPHA;
						this._radius = this._maxRadius;
						this._state = FADING;
					} else {
						float r = this._SP/this._maxSP;
						this._alpha = r*MAX_ALPHA;
						this._radius = r*this._maxRadius;
					}
					break;
				case FADING:
					this._alpha *= 0.95f;
					if(this._alpha < 0.01f){
						this._alpha = 0f;
						this._visible = false;
						this._state = DEFAULT;
					}
					break;
				default:
			}
		}
	}
	
	public void paint(Graphics2D g){
		if(this._visible){
			this._p = this._target._p;
			int x = this._p.x(vect.ROUND);
			int y = this._p.y(vect.ROUND);
			int radius = Math.round(this._radius);
			int diameter = (int)Math.round(this._radius*2);
			switch(this._state){
				case RECHARGING:
					g.setColor(color.setAlpha(this._color,this._alpha));
					g.drawOval(x-radius,y-radius,diameter,diameter);
					break;
				case FAILING:
					g.setColor(color.setAlpha(this._failColor,this._alpha));
					g.drawOval(x-radius,y-radius, diameter, diameter);
					break;
				default:
					g.setColor(color.setAlpha(this._color,this._alpha));
					g.fillOval(x-radius,y-radius,diameter,diameter);
			}
		}
	}
	
	// getters/setters
	public float radius(){ return this._maxRadius; }
	public void radius(float value){ this._maxRadius = value; }
	
	public float absorbRate(){ return this._absorbRate; }
	public void absorbRate(float value){ this._absorbRate = value; }
	public float rechargeRate(){ return this._rechargeRate; }
	public void rechargeRate(float value){ this._rechargeRate = value; }
	public int rechargeDelay(){ return this._rechargeDelay; }
	public void rechargeDelay(int value){ this._rechargeDelay = value; }
	
	public float energy(){ return this._SP; }
	public void energy(float value){ this._SP = value; this._rechargeTimer.stop(); this.flash(); }
	public float maxEnergy(){ return this._maxSP; }
	public void maxEnergy(float value){ this._maxSP = value; }
	
	public int state(){ return this._state; }
	
	// public methods
	public boolean isBroken(){ return this._SP <= 0; }
	
	public void instantFailure(){if(this._SP > 0){ this._SP = 0; this._rechargeTimer.setDelay(this._longRechargeDelay); this._rechargeTimer.restart(); this.fail(); }}
	public void instantRecharge(){if(this._SP < this._maxSP){ this._state = FADING; this._SP = this._maxSP; this._rechargeTimer.stop(); this._visible = true; }}
	public void recharge(){if(this._state != RECHARGING){ this._state = RECHARGING; this._rechargeTimer.stop(); this._visible = true; }}
	
	
}