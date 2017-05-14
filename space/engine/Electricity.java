package space.engine;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;

import space.*;

public class Electricity extends Sprite implements IBounded, ActionListener {
	
	// ORIGIN types
	public static final byte PLAYER = 0;
	public static final byte ENEMY = 1;
	public static final byte OTHER = 2;

	protected static final Color COLOR1 = color.setAlpha(color.WHITE,0.15f);
	protected static final Color COLOR2 = color.setAlpha(color.ELECTRICITY,0.9f);
	protected static final Stroke STROKE2 = new BasicStroke(1);
	protected static final Stroke STROKE1 = new BasicStroke(5);
	
	protected vect _start;
	protected vect _end;
	protected vect[] _points;
	protected int _numPoints;
	protected int _counter;
	protected int _maxCount;
	protected float _damage;
	protected byte _origin;
	protected BoundingBox _boundingBox;
	protected GameTimer _timer;
	
	public Electricity(vect start, vect end, float damage, byte origin){
		this(start,end,new vect(0,0),damage,origin,0);
	}
	
	public Electricity(vect start, vect end, vect velocity, float damage, byte origin){
		this(start,end,velocity,damage,origin,0);
	}
	
	public Electricity(vect start, vect end, vect velocity, float damage, byte origin, int time){
		this._start = start;
		this._end = end;
		this._damage = damage;
		this._origin = origin;
		this._v = velocity;
		
		switch(Engine.QUALITY){
		case 0:
			this._maxCount = 3;
			this._numPoints = 8;
			break;
		case 1:
			this._maxCount = 2;
			this._numPoints = 10;
			break;
		case 2:
			this._maxCount = 1;
			this._numPoints = 12;
			break;
		}
		
		this._points = new vect[this._numPoints];
		this._counter = 0;
		this.calculateBoundingBox();
		
		if(time > 0){
			this._timer = new GameTimer(time,this);
			this._timer.start();
		}
	}
	
	public void calculateBoundingBox(){
		vect center = this._start.add(this._end).scale(0.5f);
		vect size = this._end.sub(this._start).abs();
		if(size.x() < 8) size.x(8);
		if(size.y() < 8) size.y(8);
		this._boundingBox = new BoundingBox(center,size);
	}
	
	public BoundingBox boundingBox(){ return this._boundingBox; }
	
	public boolean hitTest(vect p){
		if(!this._boundingBox.hitTest(p)) return false;
		vect axis = this._end.sub(this._start);
		if(axis.lengthSq() < 1f) return false;
		vect v = p.sub(this._start);
		vect d = v.sub(v.project(axis));
		return (d.lengthSq() <= 64f);
	}
	
	public void loop(){
		this._start = this._start.add(this._v);
		this._end = this._end.add(this._v);
		this._boundingBox._center = this._boundingBox._center.add(this._v);
		
		if((this._counter++)%this._maxCount == 0){
			float n = (float)this._numPoints;
			int n1 = this._numPoints-1;
			vect diff = this._end.sub(this._start).scale(1f/n);
			float l = diff.qlength();
			float dl = l/2f;
			float ql = dl/2f;
			this._points[0] = this._start;
			this._points[n1] = this._end;
			for(int i = n1-1; i >= 1; i--){
				vect p = this._start.add(diff.scale(i));
				p = p.add(new vect(qmath.random()*dl-ql,qmath.random()*dl-ql));
				this._points[i] = p;
			}
		} else {
			this._points[0] = this._start;
			this._points[this._numPoints-1] = this._end;
		}
		
		switch(this._origin){
			case PLAYER:
				ArrayList<Enemy> enemies = _root.enemies;
				for(Enemy enemy : enemies){
					if(this.hitTest(enemy.position())){
						enemy.hit(this._damage);
						break;
					}
				}
				break;
			case ENEMY:
				if(this.hitTest(_root.player.position())){
					_root.player.hit(this._damage);
				}
				break;
			default:
				break;
		}
	}
	
	public void paint(Graphics2D g){
		if(this._counter%3 != 0){
			int x,y,px,py;
			px = this._start.x(vect.ROUND);
			py = this._start.y(vect.ROUND);
			g.setColor(color.WHITE);
			for(int i = 0; i < this._numPoints; i++){
				vect p = this._points[i];
				x = p.x(vect.ROUND);
				y = p.y(vect.ROUND);
				g.setStroke(STROKE1);
				g.setColor(COLOR1);
				g.drawLine(px, py, x, y);
				g.setColor(COLOR2);
				g.setStroke(STROKE2);
				g.drawLine(px, py, x, y);
				px = x;
				py = y;
			}
		}
		
	}
	
	public void actionPerformed(ActionEvent ae){
		this._timer.stop();
		_root.remove(this);
	}
	
	public void __on_paint(Graphics2D g){
		this._boundingBox.paint(g);
	}
	
	public void start(vect value){
		this._start = value;
		this.calculateBoundingBox();
	}
	
	public vect start(){ return this._start; }
	
	public void end(vect value){
		this._end = value; 
		this.calculateBoundingBox();
	}
	
	public vect end(){ return this._end; }
	
}