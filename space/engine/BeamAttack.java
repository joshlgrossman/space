package space.engine;

import space.Engine;
import space.color;
import space.qmath;

import java.awt.*;
import java.util.*;
import java.awt.image.*;

public class BeamAttack<T extends Sprite & IColor> extends SpecialAttack {

	public static class Beam<U extends Sprite & IColor> extends Sprite implements IBounded {

		protected final static Stroke OUTER_STROKE = new WobbleStroke(1,4,10);
		protected final static Stroke INNER_STROKE = new WobbleStroke(1,2,5);
		protected final static Color WHITE = color.brighten(Engine.WHITE, 0.5f);
		protected final static Color BLACK = color.brighten(Engine.BLACK, 0.5f);

		protected U _source;
		protected vect _offset;
		protected boolean _active;
		protected float _damage;
		protected boolean _color;
		protected Color _colorValue;
		protected vect _previousPos;
		protected int _timer;
		protected float _alpha;
		protected float _alphaChange;
		protected int _endPoint;
		protected BoundingBox _boundingBox;

		public Beam(U source, vect offset, float damage, int timer){
			this._source = source;
			this._offset = offset;
			this._damage = damage;
			this._timer = timer;
			this._endPoint = 0;

			if(this._source != null){
				this._p = this._source._p.add(this._offset);
			    this._color = this._source.color();
			} else {
				this._p = new vect();
				this._color = false;
			}

			this._active = Engine.keyPressed(Engine.KEY_X);
			this._alpha = 0.6f;
			this._alphaChange = 0.4f/(float)timer;

			this._colorValue = this._color?WHITE:BLACK;

			this._boundingBox = new BoundingBox();
		}

		public void loop(){
			this._active = Engine.keyPressed(Engine.KEY_X) && (this._timer-- > 0);
			if(this._active && this._source != null && this._source.exists()){
				this._previousPos = this._p;
				this._p = this._source._p.add(this._offset);
				this._color = this._source.color();
				this._colorValue = this._color?WHITE:BLACK;
				this._alpha -= this._alphaChange;
				this._colorValue = color.setAlpha(this._colorValue,this._alpha);

				float my = this._p.y()/2f;
				this._boundingBox = new BoundingBox(new vect(this._p.x(),my), new vect(10f, this._p.y()));

				ArrayList<Enemy> enemies = _root.enemies;
				int max_y = 0;
				IBounded closest = null;
				Enemy closestEnemy = null;
				for(Enemy e : enemies){
					BoundingBox bb = e.boundingBox().getHitBox(this._boundingBox);
					if(bb != null) {
						int y = bb.bottomRight().y(vect.ROUND)-2;
						if(y > max_y){
							max_y = y;
							closest = e;
							closestEnemy = e;
						}
					}
				}
				ArrayList<Obstacle> obstacles = _root.obstacles;
				for(Obstacle o : obstacles){
					BoundingBox bb = o.boundingBox().getHitBox(this._boundingBox);
					if(bb != null) {
						int y = bb.bottomRight().y(vect.ROUND)-2;
						if(y > max_y){
							max_y = y;
							closest = o;
							closestEnemy = null;
						}
					}
				}

				if(closest != null){
					if(closestEnemy != null) closestEnemy.hit(this._damage);
					this._endPoint = max_y;
					float f = qmath.random()*0.05f+0.93f;
					vect v = this._v.add(new vect(qmath.random()*2f-1f,qmath.random()));
					_root.add(new Particle(new vect(this._p.x(), max_y),v,f,this._colorValue));
				} else
					this._endPoint = 0;

			} else
				_root.remove(this);
		}

		public void paint(Graphics2D g){
			if(this._active){
				int x = this._p.x(vect.ROUND);
				int y = this._p.y(vect.ROUND);
				g.setColor(this._colorValue);
				g.setStroke(OUTER_STROKE);
				g.drawLine(x,y,x,this._endPoint);
				g.setStroke(INNER_STROKE);
				g.drawLine(x,y,x,this._endPoint);
			}
		}

		public void __on_paint(Graphics2D g){
			this._boundingBox.paint(g,true);
		}

		public BoundingBox boundingBox(){ return this._boundingBox; }

	}

	protected T _source;
	protected vect _offset;
	protected int _timer;

	public BeamAttack(T source, vect offset, float damage, boolean color, int timer){
		this._source = source;
		this._offset = offset;
		this._damage = damage;
		this._color = color;
		this._timer = timer;
	}
	
	public void display(){
		if(this._source != null && this._source.exists()){
			Blast blast = new Blast(this._p,new vect(),50f,0.06f,this._color);
			blast.startFollowing(this._source);
			_root.add(blast);
			_root.add(new Beam<>(this._source, this._offset, this._damage, this._timer));
		}
	}

}
