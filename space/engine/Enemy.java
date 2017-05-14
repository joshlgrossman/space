package space.engine;

import space.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public abstract class Enemy extends GameObject implements IColor, ActionListener {
	
	protected static vect experiencePosition = _root.expMeter.position().add(new vect(84f,0f));
	protected boolean _color;
	protected int _experience;
	protected int _spriteID;
	
	protected BufferedImage _sprite;
	
	protected GameTimer _blinkTimer;
	protected GameTimer _attackTimer;
	
	public Enemy(){
		this._blinkTimer = new GameTimer(50,this); 
		this._visible = true;
		this._color = true;
		this._spriteID = 0;
		this._sprite = EnemySprites.getSprite(this._spriteID, this._color); 
	}
	
	public Enemy(boolean color){ 
		this._blinkTimer = new GameTimer(50,this); 
		this._visible = true;
		this._color = color;
		this._spriteID = 0;
		this._sprite = EnemySprites.getSprite(this._spriteID, this._color); 
	}
	public Enemy(boolean color, int spriteID){
		this._blinkTimer = new GameTimer(50,this);
		this._visible = true; 
		this._color = color;
		this._spriteID = spriteID;
		this._sprite = EnemySprites.getSprite(this._spriteID, this._color);
	} 
	public Enemy(boolean color, int spriteID, boolean specialType){
		this._blinkTimer = new GameTimer(50,this);
		this._visible = true; 
		this._color = color;
		this._spriteID = spriteID;
		if(specialType)
			this._sprite = EnemySprites.getSpecialSprite(this._spriteID, this._color);
		else
			this._sprite = EnemySprites.getSprite(this._spriteID, this._color);
	}
	
	public void actionPerformed(ActionEvent ae){
		Object src = ae.getSource();
		if(src.equals(this._attackTimer)) this.shoot();
		else if(src.equals(this._blinkTimer)) this.blink();
	}
	
	public void kill(){ this.kill(true); }
	
	public void kill(boolean killedByPlayer){
		if(this.exists()){
			if(this._blinkTimer != null)
				this._blinkTimer.stop();
			if(this._attackTimer != null)
				this._attackTimer.stop();
			this._attackTimer = null;
			this._blinkTimer = null;
			_root.remove(this);
			_root.add(new Blast(this._p,this._v,25f,0.07f,this._color));
			if(Engine.QUALITY > 0){
			Color c = Engine.getColor(this._color);
				for(int i = Engine.QUALITY*16; i >= 0; i--){
					float f = qmath.random()*0.05f+0.93f;
					vect v = this._v.add(new vect(qmath.random()*2f-1f,qmath.random()*2f-1f));
					_root.add(new Particle(this._p,v,f,c));
				}
			}
			if(killedByPlayer){
				_root.addHUD(new TextSprite("["+this._experience+"XP]",this._p,experiencePosition,color.LBLUE2,25));
				_root.player.addExperience(this._experience);
			}
		}
	}
	
	public void shoot(){}
	
	public void hit(float damage){
		this._HP -= this._shield.absorbDamage(damage);
		if(this._HP <= 0) 
			this.kill();
		else
			this.blink();
	}
	
	public void hit(float damage, boolean color){
		this._HP -= this._shield.absorbDamage(damage) * ((this._color^color)?2:1);
		if(this._HP <= 0) 
			this.kill();
		else
			this.blink();
	}
	
	public boolean color(){return this._color;}
	
	public void updateSprite(){ this._sprite = EnemySprites.getSprite(this._spriteID, this._color); }
	
	public void paint(Graphics2D g){
		if(this._visible){
			int x = this._p.x(vect.ROUND);
			int y = this._p.y(vect.ROUND);
			g.drawImage(this._sprite, x-EnemySprites.CENTER_X, y-EnemySprites.CENTER_Y, _root);
		}
		/*if(Engine.DEBUG_MODE){
			int x = this._p.x(vect.ROUND);
			int y = this._p.y(vect.ROUND);
			g.setColor(Color.RED);
			g.fillRect(x-1, y-1, 2, 2);
			g.drawRect(x-12,y-12,24,24);
		}*/
		
	}
	
	@Override
	public void checkCollisions(){
		if(_root.player.boundingBox().hitTest(this)){
			_root.player.hit(this._power*4);
			this.kill(false);
		} else 
			super.checkCollisions();
	}
	
	@Override
	public void collide(Obstacle that){
		vect v = that.resolveCollision(this);
		this._p = this._p.add(v);
		if(v.x() != 0) this._v.x(-this._v.x());
		else if(v.y() != 0) this._v.y(-this._v.y());
	}
	
	protected void setStats(int level, int difficulty){
		// 1<=difficulty<=3
		difficulty = difficulty<1?1:(difficulty>3?3:difficulty);
		this._experience = 1;
		int exp = Engine.experienceFunction(level);
		// HP = exp
		this._maxHP = this._HP = exp;
		// SHIELD = (difficulty-1)*5
		this._shield.setProperties(16, (difficulty)*5, difficulty*0.25f, 0.01f, 5000 - difficulty*1000);
		// POWER = ceil(level/10) * difficulty
		this._power = (float)(Math.ceil((double)level/10.0) * difficulty);
		// SPEED
		this._speed = 1;
		// aggressiveness:
		// lvl 1 easy = 5990 lvl 50 easy = 5500 lvl 100 easy = 5000
		// lvl 1 med  = 5980 lvl 50 med  = 5000 lvl 100 med  = 4000
		// lvl 3 hard = 5970 lvl 50 hard = 4500 lvl 100 hard = 3000
		float aggro = (((float)level/100)*difficulty);
		int delay = 6000 - (int)(aggro*1000);
		this._attackTimer = new GameTimer(delay,this);
	}
	
	public void blink(){
		if(this._blinkTimer != null){
			if(this._blinkTimer.isRunning()){
				this._visible = !this._visible;
				int delay = this._blinkTimer.getDelay();
				if(delay > 10) this._blinkTimer.setDelay(delay-10);
				else {
					this._visible = true;
					this._blinkTimer.stop();
				}
			} else {
				this._blinkTimer.setDelay(50);
				this._blinkTimer.restart();
			}
		}
	}
	
}

class EnemySprites {

	static int WIDTH = 24;
	static int HEIGHT = 24;
	static int CENTER_X = 12;
	static int CENTER_Y = 12;
	
	static BufferedImage SPRITE1W = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage SPRITE1B = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage[] SPRITE1 = {SPRITE1B,SPRITE1W};
	static BufferedImage SPRITE2W = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage SPRITE2B = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage[] SPRITE2 = {SPRITE2B,SPRITE2W};
	static BufferedImage SPRITE3W = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage SPRITE3B = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage[] SPRITE3 = {SPRITE3B,SPRITE3W};
	static BufferedImage SPRITE4W = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage SPRITE4B = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage[] SPRITE4 = {SPRITE4B, SPRITE4W};
	static BufferedImage SPRITE5W = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage SPRITE5B = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage[] SPRITE5 = {SPRITE5B,SPRITE5W};
	
	static BufferedImage TURRETBASEW = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage TURRETBASEB = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage[] TURRETBASE = {TURRETBASEB,TURRETBASEW};
	
	
	static BufferedImage[][] SPRITES = {SPRITE1,SPRITE2,SPRITE3,SPRITE4,SPRITE5};
	static BufferedImage[][] SPECIAL_SPRITES = {TURRETBASE};
	
	static BufferedImage getSprite(int spriteNumber, boolean color){ return SPRITES[spriteNumber][color?1:0]; }
	
	static BufferedImage getSpecialSprite(int spriteID, boolean color){ return SPECIAL_SPRITES[spriteID][color?1:0]; }
	
	static {
		Color shadow = color.setAlpha(color.DGREY,0.7f);
		Color highlight = color.setAlpha(color.LGREY,0.5f);
		BasicStroke stroke = new BasicStroke(2);
		BasicStroke stroke2 = new BasicStroke(1f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
		Color white = Engine.getColor(true);
		Color black = Engine.getColor(false);
		Polygon poly;
		
		// image1 - white
		Graphics2D g = (Graphics2D)SPRITE1W.getGraphics();
		g.setColor(white);
		g.fillRect(6, 6, 12, 12);
		g.fillRect(4, 9, 16, 6);
		g.fillRect(8, 12, 8, 10);
		// image1 - black
		g = (Graphics2D)SPRITE1B.getGraphics();
		g.setColor(black);
		g.fillRect(6, 6, 12, 12);
		g.fillRect(4, 9, 16, 6);
		g.fillRect(8, 12, 8, 10);
		
		// image2 - white
		g = (Graphics2D)SPRITE2W.getGraphics();
		g.setColor(white);
		g.fillRect(9, 3, 6, 16);
		poly = new Polygon(new int[]{0,9,9}, new int[]{12,3,12},3);
		g.fillPolygon(poly);
		poly = new Polygon(new int[]{15,15,24}, new int[]{12,3,12},3);
		g.fillPolygon(poly);
		g.fillRect(0, 12, 3, 4);
		g.fillRect(21, 12,3, 4);
		// image2 - black
		g = (Graphics2D)SPRITE2B.getGraphics();
		g.setColor(black);
		g.fillRect(9, 3, 6, 16);
		poly = new Polygon(new int[]{0,9,9}, new int[]{12,3,12},3);
		g.fillPolygon(poly);
		poly = new Polygon(new int[]{15,15,24}, new int[]{12,3,12},3);
		g.fillPolygon(poly);
		g.fillRect(0, 12, 3, 4);
		g.fillRect(21, 12,3, 4);
		
		// image3 - white
		g = (Graphics2D)SPRITE3W.getGraphics();
		g.setColor(white);
		g.fillRect(6, 6, 12, 12);
		g.fillRect(4, 8, 16, 8);
		g.fillRect(0, 10, 24, 4);
		// image3 - black
		g = (Graphics2D)SPRITE3B.getGraphics();
		g.setColor(black);
		g.fillRect(6, 6, 12, 12);
		g.fillRect(4, 8, 16, 8);
		g.fillRect(0, 10, 24, 4);
		
		// image4 - white
		g = (Graphics2D)SPRITE4W.getGraphics();
		g.setColor(white);
		g.fillRect(4, 4, 16, 12);
		g.fillRect(4, 16, 6, 6);
		g.fillRect(14,16, 6, 6);
		g.fillRect(2, 2, 8, 8);
		g.fillRect(14, 2, 8, 8);
		// image4 - black
		g = (Graphics2D)SPRITE4B.getGraphics();
		g.setColor(black);
		g.fillRect(4, 4, 16, 12);
		g.fillRect(4, 16, 6, 6);
		g.fillRect(14,16, 6, 6);
		g.fillRect(2, 2, 8, 8);
		g.fillRect(14, 2, 8, 8);
		
		// image5 - white
		g = (Graphics2D)SPRITE5W.getGraphics();
		g.setColor(white);
		g.fillRect(8,8,8,8);
		g.fillRect(10,14,4,4);
		// image5 - black
		g = (Graphics2D)SPRITE5B.getGraphics();
		g.setColor(black);
		g.fillRect(8,8,8,8);
		g.fillRect(10,14,4,4);
		
		// turret base - white
		g = (Graphics2D)TURRETBASEW.getGraphics();
		g.setColor(color.GREY);
		g.fillRect(0, 0, 24, 24);
		g.setColor(color.DGREY);
		g.fillRect(2, 2, 3, 3);
		g.fillRect(19,2,3,3);
		g.fillRect(2, 19, 3,3);
		g.fillRect(19,19,3,3);
		g.setColor(shadow);
		g.setStroke(stroke2);
		g.drawRect(2, 2, 19, 19);
		g.setStroke(stroke);
		g.drawLine(23,1,23,23);
		g.drawLine(1, 23, 23, 23);
		g.setColor(highlight);
		g.drawLine(1, 1, 23, 1);
		g.drawLine(1, 1, 1, 23);
		// turret base - black
		g = (Graphics2D)TURRETBASEB.getGraphics();
		g.setColor(color.GREY);
		g.fillRect(0, 0, 24, 24);
		g.setColor(color.DGREY);
		g.fillRect(2, 2, 3, 3);
		g.fillRect(19,2,3,3);
		g.fillRect(2, 19, 3,3);
		g.fillRect(19,19,3,3);
		g.setColor(shadow);
		g.setStroke(stroke2);
		g.drawRect(2, 2, 19, 19);
		g.setStroke(stroke);
		g.drawLine(23,1,23,23);
		g.drawLine(1,23,23,23);
		g.setColor(highlight);
		g.drawLine(1,1,23,1);
		g.drawLine(1,1,1,23);
	}
	
}