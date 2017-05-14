package space.engine;

import space.engine.BoundingBox;
import java.awt.Graphics2D;

public abstract class GameObject extends Sprite implements IBounded {
	
	// STATS //////////////////////////
	// health - total amount of damage this object can take
	protected float _HP;
	protected float _maxHP;
	// shield - shield that recharges over time, while charged the shield absorbs some fraction of damage taken
	protected EnergyShield _shield;
	// power - damage this object deals
	protected float _power;
	// speed - affects max velocity and acceleration
	protected float _speed;
	
	protected boolean _visible;
	
	protected BoundingBox _boundingBox;
	
	public GameObject(){ this(100,100, 1, 1); }
	public GameObject(float hp, float shield, float power, float speed){
		super();
		this._HP = this._maxHP = hp;
		this._power = power;
		this._speed = speed;
		this._shield = new EnergyShield(this);	
	}
	
	// class functions
	public void display(){ _root.add(this._shield); }
	public void remove(){ _root.remove(this._shield); }
	
	public abstract void shoot();
	public abstract void hit(float damage);
	public abstract void hit(float damage, boolean color);
	public abstract void kill();
	public abstract void blink();
	
	// getters/setters
	public BoundingBox boundingBox(){ return new BoundingBox(this._p,new vect(this._width,this._height)); }
	public boolean visible(){ return this._visible; }
	public void visible(boolean value){ this._visible = value; }
	
	public float HP(){ return this._HP; }
	public void HP(float value){ this._HP = (value>this._maxHP?this._maxHP:value); }
	public float maxHP(){ return this._maxHP; }
	public void maxHP(float value){ this._maxHP = value; }
	
	public EnergyShield shield(){ return this._shield; }
	public float maxShield(){ return this._shield.maxEnergy(); }
	public void maxShield(float value){ this._shield.maxEnergy(value); }
	
	public float power(){ return this._power; }
	public void power(float value){ this._power = value; }
	
	public float speed(){ return this._speed; }
	public void speed(float value){ this._speed = value; }
	
	// Debug functions
	public void __on_paint(Graphics2D g){
		this.boundingBox().paint(g);
	}
	
}