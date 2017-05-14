package space.engine;

import java.awt.*;

import space.Engine;
import space.color;

public class Turret extends Enemy {
	
	protected final static BasicStroke STROKE = new BasicStroke(5);
	
	protected vect _target;
	protected vect _delta;
	protected vect _angularV;
	protected Color _colorValue;
	
	public Turret(int level, int difficulty, vect position, boolean color){
		super(color,0,true);
		this._p = position;
		this._width = this._height = 24;
		this._color = color;
		this.setStats(level,difficulty);
		this._attackTimer.start();
		this._colorValue = Engine.getColor(color);
		this._target = new vect(Engine.STAGE_WIDTH>>1,Engine.STAGE_HEIGHT>>1);
		this._angularV = new vect();
		this._delta = new vect();
	}
	
	public void loop(){
		if(this._visible){
			this._angularV = _root.player._p.sub(this._target);
			this._target = this._target.add(this._angularV.scale(0.0125f));
			this._p = this._p.add(new vect(0,1));
			this._delta = this._target.sub(this._p);
			float len = this._delta.qlength();
			if(len > 0)
				this._delta = this._delta.scale(12f/len);
			if(this.isOOOB()){
				this._visible = false;
				_root.remove(this);
			}
		}
	}
	
	public void shoot(){
		if(this._visible){
			vect d = this._delta;
			float dl = this._delta.qlength();
			vect v = d.scale(4f/dl);
			vect p = this._p.add(this._delta);
			_root.add(new Projectile(p,v,this._power,this._color,Projectile.ENEMY));
		}
	}
	
	public void paint(Graphics2D g){
		super.paint(g);	
		int x = this._p.x(vect.ROUND);
		int y = this._p.y(vect.ROUND);
		int dx = this._delta.x(vect.ROUND)+x;
		int dy = this._delta.y(vect.ROUND)+y;
		
		g.setStroke(STROKE);
		g.setColor(color.DROPSHADOW);
		g.fillOval(x-4,y-4,12,12);
		g.drawLine(x+2,y+2,dx+2,dy+2);
		g.setColor(this._colorValue);	
		g.fillOval(x-6,y-6,12,12);
		g.drawLine(x,y,dx,dy);
	}
	
}