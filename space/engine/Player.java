package space.engine;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import space.color;
import space.Engine;
import javax.swing.Timer;

public class Player extends GameObject implements ActionListener, IColor {
	
	public final static int HOMING_ATTACK = 0;
	public final static int BLAST_ATTACK = 1;
	public final static int BOMB_ATTACK = 2;
	public final static int BEAM_ATTACK = 3;
	
	protected final static float INIT_HP = 100;
	protected final static float INIT_SHIELD = 100;
	
	protected vect _min;
	protected vect _max;
	
	protected final vect _gunPosition = new vect(0, -4);
	protected final vect _exhaustPosition = new vect(0,8);
	protected final vect _centerPosition = new vect(0, 0);
	
	protected boolean _shooting;
	protected boolean _thrusters;
	protected boolean _colorToggle;
	
	protected GameTimer _shotTimer;
	protected GameTimer _exhaustTimer;
	protected GameTimer _blinkTimer;
	
	protected float _maxSpeed;
	protected float _friction;
	protected boolean _color;
	protected vect _a;
	
	protected int _specialAttack;
	protected int _special;
	protected int _maxSpecial;
	
	protected int _experience;
	protected int _maxExperience;
	
	protected int _level;
	
	public Player(){
		super(INIT_HP,INIT_SHIELD, 1, 1);
		this._shield.setProperties(16f, 10f, 0.2f, 0.01f, 2000);
	}
	
	public void initialize(){
		int exhaustDelay;
		switch(Engine.QUALITY){
		case 0: exhaustDelay = 25; break;
		case 2: exhaustDelay = 5; break;
		default: exhaustDelay = 10; break;
		}
		
		this._shotTimer = new GameTimer(100,this);
		this._exhaustTimer = new GameTimer(exhaustDelay,this);
		this._blinkTimer = new GameTimer(50,this);
		
		this._width = this._height = 16;
		
		this._level = 1;

		this._special = 0;
		this._maxSpecial = 10;
		this._experience = 0;
		this._maxExperience = Engine.experienceFunction(1);
			
		this._maxSpeed = 5.0f;
		this._friction = 0.925f;
		
		this._color = true;
		this._visible = true;
		
		this._min = new vect(this._width/2f,Engine.STAGE_HEIGHT*0.25f);
		this._max = new vect(Engine.STAGE_WIDTH-(this._width/2f)-10, Engine.STAGE_HEIGHT-98);
		this._a = new vect();
		
		this._blinkTimer.start();

		this.specialAttackType(HOMING_ATTACK);
		
	}
	
	public void loop(){
		this._a = new vect(0f,0f);
		this._thrusters = false;
		this._width += (16f-this._width)*0.3f;
		if(Engine.keyPressed(Engine.KEY_LEFT)){
			this._a.x(-0.75f);
			this._thrusters = true;
		} else if(Engine.keyPressed(Engine.KEY_RIGHT)){
			this._a.x(0.75f);
			this._thrusters = true;
		}
		
		if(Engine.keyPressed(Engine.KEY_UP)){
			this._a.y(-0.5f);
			this._thrusters = true;
		} else if(Engine.keyPressed(Engine.KEY_DOWN)){
			this._a.y(0.5f);
		}
		
		this.move();
		
		boolean key_a = Engine.keyPressed(Engine.KEY_A);
		
		if(key_a && !this._shooting)
			this.shoot();
		else if(!key_a && this._shooting)
			this._shotTimer.stop();
		this._shooting = key_a;
			
		if(Engine.keyToggled(Engine.KEY_B)){
			this._color = !this._color;
			this._width = 4f;
		}
		
		if(Engine.keyToggled(Engine.KEY_X) && this._special == this._maxSpecial){
			this.useSpecial();
		}
		
		boolean exhaust = this._exhaustTimer.isRunning();
		
		if(this._thrusters && !exhaust)
			this._exhaustTimer.restart();
		else if(!this._thrusters && exhaust)
			this._exhaustTimer.stop();
	}
	
	public void move(){
		this._v = this._v.add(this._a).scale(this._friction);
		float length = this._v.qlength();
		if(length > this._maxSpeed) this._v = this._v.scale(this._maxSpeed/length);
		this._p = this._p.add(this._v);
		float px = this._p.x();
		float py = this._p.y();
		if(px < this._min.x()) px = this._min.x();
		else if(px > this._max.x()) px = this._max.x();
		if(py < this._min.y()) py = this._min.y();
		else if (py > this._max.y()) py = this._max.y();
		this._p = new vect(px,py);
	}
	
	public void collide(Obstacle that){
		this._p = this._p.add(that.resolveCollision(this));
		float px = this._p.x();
		float py = this._p.y();
		if(py < this._min.y()) {
			this._p.y(this._min.y());
		} else if(px < this._min.x() || px > this._max.x() || py > this._max.y()){
			this._p = new vect(Engine.STAGE_WIDTH>>1,(Engine.STAGE_HEIGHT>>2)*3);
			this.blink();
			this.hit(INIT_HP/10);
		}
	}
	
	public void paint(Graphics2D g){
		if(this._visible){
			int x = this._p.x(vect.ROUND);
			int y = this._p.y(vect.ROUND);
			double scale = 1.0-(Math.abs(this._v.x())/12.0);
			double dw = this._width*scale;
			int w = (int)(dw)>>1;
			int h = (int)(Math.round(this._height))>>1;
			Polygon shape = new Polygon(new int[]{-w,0,w},new int[]{10,-10,10},3);
			shape.translate(x, y);
			g.setColor(Engine.getColor(this._color));
			g.fill(shape);
		}
	}
	
	public boolean color(){ return this._color; }
	public void kill(){}

	public void hit(float damage){ this.hit(damage,!this._color); }
	public void hit(float damage, boolean color){
		if(this._color != color){
			if(!this._blinkTimer.isRunning()){
				this.blink();
				this._HP -= this._shield.absorbDamage(damage);
				if(this._HP < 0) this._HP = 0;
				this.updateHPMeter();
			}
		} else {
			this._special += damage;
			if(this._special > this._maxSpecial) this._special = this._maxSpecial;
			this.updateSpecialMeter();
		}
	}
	
	public void shoot(){
			vect p = this._p.add(this._v.add(this._gunPosition));
			vect v = new vect(this._v.x()*0.2f,-7.5f);
			_root.add(new Projectile(p,v,this._power,this._color, Projectile.PLAYER));
		if(!this._shotTimer.isRunning())
			this._shotTimer.restart();
	}
	
	public void useSpecial(){
		switch(this._specialAttack){
			case HOMING_ATTACK:
				_root.add(new HomingAttack(this._p,this._power*10,this._color));
				break;
			case BLAST_ATTACK:
				_root.add(new BlastAttack(this._p,this._power*10,this._color));
				break;
			case BOMB_ATTACK:
				_root.add(new BombAttack(this._p, new vect(this._v.x()*0.5f, this._v.y()*0.1f), this._power*10, this._color));
				break;
			case BEAM_ATTACK:
				_root.add(new BeamAttack<>(this,new vect(0,-16),this._power*5,this._color,240));
				break;
		}
		this._special = 0;
		this.updateSpecialMeter();
	}
	
	public void exhaust(){
		float amount = (Math.abs(this._v.y())/this._maxSpeed)*0.2f + 0.75f;
		vect p = this._p.add(this._exhaustPosition);
		float vx = this._v.x()*0.8f+(float)Math.random()-0.5f;
		float vy = this._v.y()*0.5f+3f*(float)Math.random()+3f;
		_root.add(new FireParticle(p,new vect(vx,vy), amount));
	}
	
	public void blink(){
		if(this._blinkTimer.isRunning()){
			this._visible = !this._visible;
			int delay = this._blinkTimer.getDelay();
			if(delay > 5) this._blinkTimer.setDelay(delay-5);
			else {
				this._visible = true;
				this._blinkTimer.stop();
			}
		} else {
			this._blinkTimer.setDelay(50);
			this._blinkTimer.restart();
		}
	}
	
	public void actionPerformed(ActionEvent ae){
		Object source = ae.getSource();
		if(source.equals(this._shotTimer)) this.shoot();
		else if(source.equals(this._exhaustTimer)) this.exhaust();
		else if(source.equals(this._blinkTimer)) this.blink(); 
	}
	
	public void addSpecial(int sp){
		int spec = this._special + sp;
		this._special = (spec > this._maxSpecial)?(this._maxSpecial):(spec);
		this.updateSpecialMeter();
	}
	
	public int maxSpecial(){ return this._maxSpecial; }
	
	public int specialAttackType(){ return this._specialAttack; }
	public void specialAttackType(int value){ this._specialAttack = value; this.updateSpecialText(); }

	// TODO: allow leveling up more than one level at a time
	public void addExperience(int xp){
		int exp = this._experience + xp;
		if(exp >= this._maxExperience){
			if(this._level < 100){
				this._experience = exp;
				this.levelUp();
				this.updateExpMeter();
				this._maxExperience = Engine.experienceFunction(this._level);
				this._experience = this._experience % this._maxExperience;
			} else {
				this._experience = this._maxExperience;
				this._level = 101;
				this.updateLevelText();
				_root.expMeter.isFillable(true);
			}
		} else {
			this._experience = exp;
			this.updateExpMeter();
		}
		
	}
	
	public int experienceToNextLevel(){ return this._maxExperience - this._experience; }
	public int level(){ return this._level; }
	
	public void levelUp(){
		this._level++;
		Blast blast = new Blast(this._p, 40f,0.07f,color.LVLUPCOLOR1, color.LVLUPCOLOR2);
		blast.startFollowing(this,0.5f);
		_root.add(blast);
		this.blink();
		this.updateLevelText();
	}
	
	public void pickUp(Item item){
		switch(item.type()){
			case Item.HEALTH:
				this.HP(this._HP + INIT_HP/4);
				this.updateHPMeter();
				break;
			case Item.SPECIAL:
				this._special = this._maxSpecial;
				this.updateSpecialMeter();
				break;
			case Item.STATUP:
				/** TODO implement stat up item */
				break;
			case Item.POWERUP:
				/** TODO implement power up item */
				break;
			case Item.HOMING:
				this._specialAttack = HOMING_ATTACK;
				this.updateSpecialText();
				break;
			case Item.BLAST:
				this._specialAttack = BLAST_ATTACK;
				this.updateSpecialText();
				break;
			case Item.BOMB:
				this._specialAttack = BOMB_ATTACK;
				this.updateSpecialText();
				break;
			case Item.BEAM:
				this._specialAttack = BEAM_ATTACK;
				this.updateSpecialText();
				break;
		}
	}
	
	public void updateHPMeter(){ _root.hpMeter.value(((float)this._HP)/((float)this._maxHP)); }
	public void updateExpMeter(){ _root.expMeter.value((float)(this._experience)/((float)this._maxExperience)); }
	public void updateSpecialMeter(){
		_root.specialMeter.value(((float)this._special)/((float)this._maxSpecial));
	}
	public void updateSpecialText(){
		switch(this._specialAttack){
			case HOMING_ATTACK: _root.specialText.text("HOMING");	break;
			case BLAST_ATTACK:	_root.specialText.text("BLAST");  	break;
			case BOMB_ATTACK:	_root.specialText.text("BOMB");		break;
			case BEAM_ATTACK:	_root.specialText.text("BEAM");		break;
		}
	}
	public void updateLevelText(){ 
		if(this._level <= 100)
			_root.levelText.text("LVL. "+this._level);
		else
			_root.levelText.text("MAX");
	}
	
}

/*boolean key_b = Engine.keyPressed(Engine.KEY_B);

if(key_b && !this._colorToggle){
	this._colorToggle = true;
	this._color = !this._color;
} else if(!key_b) this._colorToggle = false;
*/
/*
public class Player extends GameObject implements ActionListener, IColor {
	
	protected boolean _shooting;
	protected Timer _shotTimer;
	protected Timer _exhaustTimer;
	protected vect _gunPosition = new vect(0,-16);
	protected float _maxSpeed;
	protected boolean _color;
	
	public Player(){ super(100,100); }
	
	@Override
	public void initialize(){
		this._shotTimer = new Timer(100, this);
		this._exhaustTimer = new Timer(5,this);
		this._width = 16;
		this._height = 15;
		this._exhaustTimer.start();
		this._maxSpeed = 5.0f;
	}
	
	@Override
	public void loop(){
		this._v = this._v.scale(0.925f);
		float vx = this._v.x();
		float vy = this._v.y();
		boolean k_l = Engine.keyPressed(Engine.KEY_LEFT);
		boolean k_r = Engine.keyPressed(Engine.KEY_RIGHT);
		boolean k_u = Engine.keyPressed(Engine.KEY_UP);
		if(k_l)
			vx -= 0.75;
		else if(k_r)
			vx += 0.75;
		if(k_u)
			vy -= 0.5;
		else if(Engine.keyPressed(Engine.KEY_DOWN))
			vy += 0.5;
		vect v = new vect(vx,vy);
		float length = v.length();
		if(length > this._maxSpeed) this._v = v.scale(this._maxSpeed/length);
		else this._v = v;
		this._p = this._p.add(this._v);
		float px = this._p.x();
		float py = this._p.y();
		float w = this._width/2;
		float h = this._height/2;
		float vx2 = this._v.x();
		float vy2 = this._v.y();
		if(Math.abs(vx2) < 0.125) this._v.x(vx2 = 0);
		if(Math.abs(vy2) < 0.075) this._v.y(vy2 = 0);
		vx2 = Math.abs(vx2)<1?0:vx2;
		vy2 = Math.abs(vy2)<1?0:vy2;
		if(px < w) {
			px = w;
			this._v = new vect(-vx2*0.1f,vy2);
		} else if(px > Engine.STAGE_WIDTH-w-10) {
			px = Engine.STAGE_WIDTH-w-10;
			this._v = new vect(-vx2*0.1f,vy2);
		} 
		if(py < Engine.STAGE_HEIGHT>>2) {
			py = Engine.STAGE_HEIGHT>>2;
			this._v = new vect(vx2,0);
		} else if(py > Engine.STAGE_HEIGHT-88) {
			py = Engine.STAGE_HEIGHT-88;
			this._v = new vect(vx2,0);
		}
		this._p = new vect(px,py);
		
		boolean shoot_pressed = Engine.keyPressed(Engine.KEY_A);
		if(this._shooting && !shoot_pressed) {
			this._shooting = false;
			this._shotTimer.stop();
		} else if(!this._shooting && shoot_pressed){
			this.shoot();
			this._shooting = true;
			this._shotTimer.restart();
		}
				
		if(!this._exhaustTimer.isRunning() && vy2 < 0 || (vy2 <= 0 && vx2 != 0))
			this._exhaustTimer.restart();
		 else if(this._exhaustTimer.isRunning() && vy2 >= 0)
			this._exhaustTimer.stop();
	}
	
	
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource() == this._shotTimer) {
			if(this._shooting) this.shoot();
		} else if(ae.getSource() == this._exhaustTimer){
			this.generateExhaust();
		}
	}
	
	public void generateExhaust(){
		float amount = (Math.abs(this._v.y())/this._maxSpeed)*0.2f + 0.75f;
		float vx = this._v.x()*0.8f+(float)Math.random()-0.5f;
		float vy = this._v.y()*0.5f+3f*(float)Math.random()+3f;
		Engine.ROOT.add(new FireParticle(this._p,new vect(vx,vy), amount));
	}
	
	// IColor methods
	public boolean color(){ return this._color; }
	
	// GameObject methods
	public void shoot(){
		vect p = this._p.add(this._v.add(this._gunPosition));
		vect v = new vect(this._v.x()*0.2f,-7.5f);
		Engine.ROOT.add(new Projectile(p,v,5.0,Projectile.PLAYER));
		//Engine.ROOT.add(new Projectile(this._p.add(this._gunPosition), new vect(0f,0f),5.0,Projectile.PLAYER));
	}
	
	public void hit(){}
	
	public void kill(){}
	
	@Override
	public void paint(Graphics2D g){
		int x = this._p.x(vect.ROUND);
		int y = this._p.y(vect.ROUND);
		double scale = 1.0-(Math.abs(this._v.x())/12.0);
		double dw = this._width*scale;
		int w = (int)(dw)>>1;
		int h = (int)(Math.round(this._height))>>1;
		Polygon shape = new Polygon(new int[]{-w,0,w},new int[]{0,-20,0},3);
		shape.translate(x, y);
		g.setColor(color.DGREEN1);
		g.fill(shape);
	}
	
}*/