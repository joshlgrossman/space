package space.engine;

import space.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Swarm extends Sprite implements IBounded, ActionListener {
	
	public static final int RESIZE = 1;
	public static final int SMART = 2;
	
	public static final int DUMB_STATIC = 0;
	public static final int DUMB_RESIZE = 1;
	public static final int SMART_STATIC = 2;
	public static final int SMART_RESIZE = 3;
	
	protected static vect experiencePosition = _root.expMeter.position().add(new vect(84f,0f));
	
	public static Stroke STROKE = new BasicStroke(1f);
	public static final float GRID_SIZE = 16;
	
	public static class SwarmEnemy extends Enemy {
		
		Swarm _parent;
		float _random;
		vect _a;
		vect _center;
		vect _offset;
		vect _offsetDelta;
		
		public SwarmEnemy(int level, int difficulty, vect swarm_center, vect relative_position, boolean color, Swarm parent){ this(level,difficulty,swarm_center,relative_position,color,parent,(float)Math.random()); }
		public SwarmEnemy(int level, int difficulty, vect swarm_center, vect relative_position, boolean color, Swarm parent, float random){
			super(color, 4);
			this.setStats(level, difficulty);
			this._center = swarm_center;
			this._offset = relative_position;
			this._offsetDelta = this._offset;
			this._p = swarm_center.add(relative_position);
			this._width = this._height = 16;
			this._parent = parent;
			this._random = random;
		}
		
		public void loop(){
			//this._a = this._parent._v.sub(this._v).scale(this._random*0.25f+0.25f);
			//this._v = this._v.add(this._a);
			this._offsetDelta = this._offsetDelta.add(this._offset.sub(this._offsetDelta).scale(0.05f));
			vect p = this._parent._p.add(this._offsetDelta);
			this._v = this._v.scale(this._random*0.75f+0.125f).add(p.sub(this._p).scale(this._random*0.1f+0.25f));
			this._p = this._p.add(this._v);
		}
		
		public void shoot(){
			vect delta = _root.player._p.sub(this._p);
			vect v = delta.scale(4f/delta.length());
			_root.add(new Projectile(this._p,v,this._power,this._color,Projectile.ENEMY));
		}
		
		public void kill(){ kill(true); }
		public void kill(boolean killedByPlayer){
			if(--this._parent._numMembers <= 0)
				this._parent._center = this._p;
			else
				this._parent.calculateCenter();
			if(killedByPlayer)
				this._parent._killedByPlayer++;
			this._parent._enemies.remove(this);
			
			super.kill(false); // no xp for killing individual member of swarm
		}
		
		
		public void collide(Obstacle that){
			vect v = that.resolveCollision(this);
			this._p = this._p.add(v);
			if(v.x() != 0)
				this._v.x(-this._v.x());
			else
				this._v.y(-this._v.y());
		}
		
		public void offset(vect value){ this._offset = value; }
		public vect offset(){ return this._offset; }
		
		public void random(float value){ this._random = value; }
		public float random(){ return this._random; }
		
	}
	
	public ArrayList<SwarmEnemy> _enemies;
	protected GameTimer _attackTimer;
	protected int _behavior;
	protected int _numMembers;
	protected int _killedByPlayer;
	protected int _initMembers;
	protected int _rows;
	protected int _columns;
	protected boolean _color;
	protected vect _center;
	protected vect _a;
	protected vect _dimensions;
	protected int _experience;
	protected vect _randomization;
	
	public Swarm(int level, int difficulty, vect center, boolean color, int columns, int rows, int behavior){
		int size = rows*columns;
		this._initMembers = this._numMembers = size;
		this._enemies = new ArrayList<SwarmEnemy>();
		this._behavior = behavior;
		this._color = color;
		float grid = GRID_SIZE;
		float hgrid = grid/2;
		this._rows = rows;
		this._columns = columns;
		this._width = columns*grid;
		this._height = rows*grid;
		this._dimensions = new vect(this._width,this._height);
		vect top_left = center.sub(this._dimensions.scale(0.5f));
		int i = 0;
		float random = 0f;
		for(int y = 0; y < rows; y++){
			random = (float)y/(float)rows;
			for(int x = 0; x < columns; x++){
				vect d = new vect(x*grid+hgrid,y*grid+hgrid);
				vect p = top_left.add(d).sub(center);
				SwarmEnemy e = new SwarmEnemy(level,difficulty,center,p,color,this,random);
				this._enemies.add(e);
				_root.add(e);
			}
		}
		this._p = center;
		this._center = center;
		this._v = new vect(3f,0f);
		this._a = new vect();
		float aggro = (((float)level/100)*difficulty);
		int delay = 6000 - (int)(aggro*1000);
		this._attackTimer = new GameTimer(delay,this);
		this._attackTimer.start();
		this._experience = (rows*columns)>>1;
		if((this._behavior&SMART) == 1)
			this._randomization = new vect(qmath.random()*100-50,qmath.random()*20-10);
		else
			this._randomization = new vect();
	}
	
	public void kill(){
		int exp = Math.round(((float)this._killedByPlayer)/((float)this._initMembers)*this._experience);
		if(exp > 0){
			_root.addHUD(new TextSprite("["+exp+"XP]",this._center,experiencePosition,color.LBLUE2,25));
			_root.player.addExperience(exp);
		}
		_root.remove(this);
	}
	
	public void loop(){
		if(this._numMembers > 0){
			switch(this._behavior){
				case SMART_RESIZE:
					this.resize();
				case SMART_STATIC:
					this.smart_loop();
					break;
				case DUMB_RESIZE:
					this.resize();
				default: case DUMB_STATIC:
					this.dumb_loop();
					break;
			}
		} else 
			this.kill();
	}
	
	protected void dumb_loop(){
		float dx = ((Engine.STAGE_WIDTH>>1)-this._p.x());
		float dy = ((Engine.STAGE_HEIGHT>>2)-this._p.y());
		
		this._a = new vect(0f, dy*0.00075f);
		
		this._v = new vect(dx*0.1f,this._v.y()+this._a.y());
		
		float lsq = this._v.lengthSq();
		if(lsq > 9) this._v = this._v.scale(3/(float)Math.sqrt(lsq));
	
		this._p = this._p.add(this._v);
	}
	
	protected void smart_loop(){
		vect player = _root.player._p.add(this._randomization);
		float dx = (player.x()-this._p.x());
		float dy = ((player.y()/Engine.STAGE_HEIGHT)*300-this._p.y());
		
		this._a = new vect(dx*0.001f, dy*0.001f);
		
		this._v = this._v.add(this._a);
		
		float lsq = this._v.lengthSq();
		if(lsq > 9) this._v = this._v.scale(3/(float)Math.sqrt(lsq));
	
		this._p = this._p.add(this._v);
	}
	
	public void resize(){
		if(this._numMembers < this._initMembers){
			if((this._numMembers % this._columns == 0)){
				this._rows--;
				float grid = GRID_SIZE;
				float hgrid = 10;
				this._width = this._columns*grid;
				this._height = this._rows*grid;
				this._dimensions = new vect(this._width,this._height);
				int i = 0;
				float random;
				float f_rows = (float)this._rows;
				int num_enemies = this._enemies.size();
				for(float y = 0; y < f_rows; y++){
					random = y/f_rows;
					for(int x = 0; x < this._columns; x++){
						if(i < num_enemies){
							vect d = new vect(x*grid+hgrid,y*grid+hgrid).sub(this._dimensions.scale(0.5f));
							SwarmEnemy e = this._enemies.get(i++);
							e.offset(d);
							e.random(random);
						}
					}
				}
				this._initMembers = this._rows*this._columns;
			}
		}
	}
	
	public void actionPerformed(ActionEvent ae){
		if(this._numMembers > 1){
			int rand = qmath.randomInt() % this._numMembers;
			this._enemies.get(rand).shoot();
		} else if(this._numMembers == 1){
			this._enemies.get(0).shoot();
		}
	}
	
	public BoundingBox boundingBox(){
		return new BoundingBox(this._p, this._dimensions);
	}
	
	public void collide(Obstacle that){
		vect v = that.resolveCollision(this);
		this._p = this._p.add(v);
		if(v.x() != 0)
			this._v.x(-this._v.x()*0.75f);
		else
			this._v.y(-this._v.y()*0.75f);
	}
	
	public void calculateCenter(){
		if(this._numMembers > 0){
			vect sum = new vect();
			for(SwarmEnemy member : this._enemies)
				sum = sum.add(member._p);
			float k = 1f/((float)this._numMembers);
			this._center = sum.scale(k);
		}
	}
	
	public void __on_paint(Graphics2D g){
		g.setColor(color.DEBUGCOLOR);
		g.setStroke(STROKE);
		vect tl = this._p.sub(this._dimensions.scale(0.5f));
		int x = tl.x(vect.ROUND);
		int y = tl.y(vect.ROUND);
		int w = this._dimensions.x(vect.ROUND);
		int h = this._dimensions.y(vect.ROUND);
		g.drawRect(x, y, w, h);
		x = this._p.x(vect.ROUND);
		y = this._p.y(vect.ROUND);
		g.fillRect(x-1, y-1, 2, 2);
		x = this._center.x(vect.ROUND);
		y = this._p.y(vect.ROUND);
		g.fillRect(x-1,y-1,2,2);
		if((this._behavior&SMART)==1){
			vect p = new vect(_root.player.x(),(_root.player.y()/Engine.STAGE_HEIGHT)*300).add(this._randomization);
			int x1 = this._p.x(vect.ROUND);
			int y1 = this._p.y(vect.ROUND);
			int x2 = p.x(vect.ROUND);
			int y2 = p.y(vect.ROUND);
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
}