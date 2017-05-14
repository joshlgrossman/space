package space.engine;

import space.*;
import java.awt.*;
import java.util.*;

public class BoundingBoxGroup extends BoundingBox {

	protected ArrayList<BoundingBox> _boundingBoxes = new ArrayList<>();
	protected vect _origin;

	public BoundingBoxGroup(BoundingBox bounds, BoundingBox... boundingBoxes){
		vect center = new vect();
		float min_x = Float.MAX_VALUE;
		float min_y = Float.MAX_VALUE;
		float max_x = Float.MIN_VALUE;
		float max_y = Float.MIN_VALUE;
		for(BoundingBox bb : boundingBoxes){
			vect c = bb._center;
			vect tl = bb.topLeft();
			vect br = bb.bottomRight();
			this._boundingBoxes.add(bb);
			center = center.add(c);
			if(tl.x() < min_x) min_x = tl.x();
			if(br.x() > max_x) max_x = br.x();
			if(tl.y() < min_y) min_y = tl.y();
			if(br.y() > max_y) max_y = br.y();
		}
		this._center = bounds._center;
		this._size = bounds._size;
		this._origin = center.scale(1f/(float)boundingBoxes.length);
	}

	public boolean hitTest(IBounded that){ return this.hitTest(that.boundingBox()); }

	public boolean hitTest(BoundingBox that){
		if(super.hitTest(that)){
			that = that.localize(this._center);
			for(BoundingBox bb : this._boundingBoxes)
				if(bb.hitTest(that)) return true;
			return false;
		}
		return false;
	}

	public boolean hitTest(vect point){
		if(super.hitTest(point)){
			point = point.sub(this._center);
			for(BoundingBox bb : this._boundingBoxes)
				if(bb.hitTest(point)) return true;
			return false;
		}
		return false;
	}

	public BoundingBox getHitBox(IBounded that){ return this.getHitBox(that.boundingBox()); }

	public BoundingBox getHitBox(BoundingBox that){
		if(super.hitTest(that)){
			that = that.localize(this._center);
			for(BoundingBox bb : this._boundingBoxes)
				if(bb.hitTest(that)) return bb.globalize(this._center);
			return null;
		}
		return null;
	}

	public BoundingBox getHitBox(vect point){
		if(super.hitTest(point)){
			point = point.sub(this._center);
			for(BoundingBox bb : this._boundingBoxes)
				if(bb.hitTest(point)) return bb.globalize(this._center);
			return null;
		}
		return null;
	}

	public void paint(Graphics2D g, boolean dot){
		super.paint(g, dot);
		for(BoundingBox bb : this._boundingBoxes)
			bb.paint(g, dot, this._center);
	}


}
