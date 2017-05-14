package space.engine;

import java.awt.*;
import space.*;

public class Blast extends Sprite {
	
	// for following
	protected boolean _following;
	protected Sprite _target;
	protected float _elasticity;
	
	protected float _growthRate;
	protected float _radius;
	protected float _scale;
	protected Color _color1;
	protected Color _color2;
	protected Color _color;
	
	public Blast(vect position, float radius, float growthRate, boolean color1){ this(position,radius,growthRate,Engine.getColor(color1), color.DROPSHADOW); }
	public Blast(vect position, float radius, float growthRate, Color init_color){ this(position,radius,growthRate,init_color,color.setAlpha(init_color,0f)); }
	public Blast(vect position, float radius, float growthRate, Color color1, Color color2){
		this._p = position;
		this._radius = radius;
		this._growthRate = growthRate;
		this._color = this._color1 = color1;
		this._color2 = color2;
		this._scale = 0f;
		this._following = false;
	}
	public Blast(vect position, vect velocity, float radius, float growthRate, boolean color1){
		this(position, radius, growthRate,Engine.getColor(color1), color.TRANSPARENT);
		this._v = velocity;
	}
	
	public void loop(){
		float delta = 1f - this._scale;
		if(delta < 0.01) _root.remove(this);
		this._scale += delta * this._growthRate;
		this._color = color.mix(this._color2, this._color1, this._scale);
		if(this._following && this._target != null && this._target.exists()){
			this._v = this._target._p.sub(this._p).scale(this._elasticity);
			this._p = this._p.add(this._v);
		} else {
			this._v = this._v.scale(0.9f);
			this._p = this._p.add(this._v);
		}
	}
	
	public void paint(Graphics2D g){
		int r = (int)(Math.round(this._radius * this._scale));
		int d = r << 1;
		int cx = this._p.x(vect.ROUND) - r;
		int cy = this._p.y(vect.ROUND) - r;
		g.setColor(this._color);
		g.fillOval(cx,cy,d,d);
	}
	
	public void startFollowing(Sprite target){ this.startFollowing(target,1f); }
	public void startFollowing(Sprite target, float elasticity){
		this._elasticity = (elasticity < 0f ? 0f : (elasticity > 1f ? 1f : elasticity));
		this._target = target;
		this._following = true;
	}
	
	public void stopFollowing(){ this._target = null; this._elasticity = 0f; this._following = false; }
	
}