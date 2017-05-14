package space.engine;

import space.Engine;
import space.color;
import space.qmath;
import java.awt.*;

public class SmartEnemy extends Enemy {
	
	protected static final BasicStroke STROKE = new BasicStroke();
	
	protected vect _a;
	
	protected vect _randomization;
	
	public SmartEnemy(int level, int difficulty, vect position, boolean color, int enemySprite){
		super(color,enemySprite);
		this._p = position;
		this._a = new vect();
		this._v = new vect();
		this._width = this._height = 16;
		this.setStats(level,difficulty);
		this._attackTimer.start();
		this._randomization = new vect(qmath.random()*100-50,qmath.random()*20-10);
	}
	
	public void loop(){
		Player plyr = _root.player;
		vect p = new vect(plyr._p.x(),plyr._p.y()/4f).add(this._randomization);
		vect delta = p.sub(this._p);
		this._a = delta.normalize().scale(0.125f);
		this._v = this._v.add(this._a);
		float lsq = this._v.lengthSq();
		if(lsq > 9f){
			float l = (float)Math.sqrt(lsq);
			this._v = this._v.scale(3f/l);
		}
		this._p = this._p.add(this._v);
		if(this.isOOOB())
			_root.remove(this);
	}
	
	public void collide(Obstacle that){
		vect v = that.resolveCollision(this);
		this._p = this._p.add(v);
		if(v.x() != 0)
			this._v.x(-this._v.x());
		else
			this._v.y(-this._v.y());
	}
	
	public void shoot(){
		vect delta = _root.player._p.sub(this._p);
		vect v = delta.scale(4f/delta.length());
		_root.add(new Projectile(this._p,v,this._power,this._color,Projectile.ENEMY));
	}
	
	public void __on_paint(Graphics2D g){
		super.__on_paint(g);
		vect p = new vect(_root.player.x(),_root.player.y()/4f).add(this._randomization);
		int x1 = this._p.x(vect.ROUND);
		int y1 = this._p.y(vect.ROUND);
		int x2 = p.x(vect.ROUND);
		int y2 = p.y(vect.ROUND);
		g.setColor(color.DEBUGCOLOR);
		g.setStroke(STROKE);
		g.drawLine(x1, y1, x2, y2);
	}
	
}