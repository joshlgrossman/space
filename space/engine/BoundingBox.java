package space.engine;

import java.awt.*;
import space.*;

public class BoundingBox {
	
	protected vect _center;
	protected vect _size;

	protected BoundingBox(){
		this._center = new vect();
		this._size = new vect();
	}

	public BoundingBox(vect center, vect size){
		this._center = center;
		this._size = size;
	}
	
	public boolean hitTest(IBounded that){ return this.hitTest(that.boundingBox()); }
	
	public boolean hitTest(BoundingBox that){
		vect delta = that._center.sub(this._center).abs();
		vect bounds = this._size.add(that._size).scale(0.5f);
		return (delta.x() < bounds.x() && delta.y() < bounds.y());
	}
	
	public boolean hitTest(vect point){
		vect delta = point.sub(this._center).abs();
		vect bounds = this._size.scale(0.5f);
		return (delta.x() < bounds.x() && delta.y() < bounds.y());
	}

	public BoundingBox getHitBox(IBounded that){ return this.getHitBox(that.boundingBox()); }
	public BoundingBox getHitBox(BoundingBox that){ return (this.hitTest(that)?(this):(null)); }
	public BoundingBox getHitBox(vect point){ return (this.hitTest(point)?(this):(null)); }

	public BoundingBox localize(vect center){ return new BoundingBox(this._center.sub(center), this._size); }
	public BoundingBox globalize(vect center) { return new BoundingBox(this._center.add(center), this._size); }

	public vect center(){ return this._center; }
	public vect size(){ return this._size; }
	public void center(vect value){ this._center = value; }
	public void size(vect value){ this._size = value; }

	protected vect topLeft(){ return this._center.sub(this._size.scale(0.5f)); }
	protected vect bottomRight(){ return this._center.add(this._size.scale(0.5f)); }
	
	public void paint(Graphics2D g){ this.paint(g,true); }
	
	public void paint(Graphics2D g, boolean dot){
		vect tl = this._center.sub(this._size.scale(0.5f));
		int cx = this._center.x(vect.ROUND);
		int cy = this._center.y(vect.ROUND);
		int w = this._size.x(vect.ROUND);
		int h = this._size.y(vect.ROUND);
		int x = tl.x(vect.ROUND);
		int y = tl.y(vect.ROUND);
		
		g.setColor(color.DEBUGCOLOR);
		g.drawRect(x, y, w,h);
		if(dot) g.fillRect(cx-1, cy-1, 2,2);
	}

	public void paint(Graphics2D g, boolean dot, vect translate){
		vect center = this._center.add(translate);
		vect tl = center.sub(this._size.scale(0.5f));
		int cx = center.x(vect.ROUND);
		int cy = center.y(vect.ROUND);
		int w = this._size.x(vect.ROUND);
		int h = this._size.y(vect.ROUND);
		int x = tl.x(vect.ROUND);
		int y = tl.y(vect.ROUND);

		g.setColor(color.DEBUGCOLOR);
		g.drawRect(x, y, w,h);
		if(dot) g.fillRect(cx-1, cy-1, 2,2);
	}
	
}