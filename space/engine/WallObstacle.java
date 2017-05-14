package space.engine;

import space.*;
import java.awt.*;

public class WallObstacle extends Obstacle {
	
	protected static final BasicStroke STROKE1 = new BasicStroke(2f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
	protected static final BasicStroke STROKE2 = new BasicStroke(1f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
	protected static final Color SHADOW = color.setAlpha(color.DGREY,0.7f);
	protected static final Color HIGHLIGHT = color.setAlpha(color.LGREY,0.5f);
	
	protected BoundingBox _boundingBox;
	
	public WallObstacle(vect top_left, vect bottom_right, vect velocity){
		vect size = bottom_right.sub(top_left);
		vect center = (bottom_right.add(top_left)).scale(0.5f);
		this._boundingBox = new BoundingBox(center,size);
		this._p = center;
		this._v = velocity;
		this._width = size.x();
		this._height = size.y();
	}
	
	public void loop(){
		this._p = this._p.add(this._v);
		this._boundingBox.center(this._p);
		if(this._p.y() > Engine.STAGE_HEIGHT+this._height/2){
			_root.remove(this);
		}
	}
	
	public void paint(Graphics2D g){
		int x = this._p.x(vect.ROUND);
		int y = this._p.y(vect.ROUND);
		int w = (int)Math.round(this._width);
		int hw = w>>1;
		int h = (int)Math.round(this._height);
		int hh = h>>1;
		int lt = x-hw;
		int tp = y-hh;
		int rt = x+hw;
		int bm = y+hh;
		g.setColor(color.DROPSHADOW);
		g.fillRect(lt+4, tp+4, w, h);
		
		g.setColor(color.GREY);
		g.fillRect(lt, tp, w, h);
		
		g.setStroke(STROKE2);
		g.setColor(SHADOW);
		g.drawRect(lt+4, tp+4, w-8,h-8);
		
		g.setStroke(STROKE1);
		g.setColor(SHADOW);
		g.drawLine(rt,tp, rt,bm);
		g.drawLine(lt, bm, rt, bm);
		g.setColor(HIGHLIGHT);
		g.drawLine(lt, tp, lt, bm);
		g.drawLine(lt, tp, rt,tp);
		
		g.setColor(color.DGREY);
		g.fillRect(lt+4,tp+4,3,3);
		g.fillRect(rt-7,tp+4,3,3);
		g.fillRect(lt+4,bm-7,3,3);
		g.fillRect(rt-7,bm-7,3,3);
		
		
		
		
	}
	
	public BoundingBox boundingBox(){
		return this._boundingBox;
	}
	
	public boolean collision(Sprite that){ return this._boundingBox.hitTest(that._p);}
	public boolean collision(IBounded that){ return this._boundingBox.hitTest(that.boundingBox()); }
	
	
	public vect resolveCollision(Sprite that){
		vect diff = that._p.sub(this._p);
		vect diff_abs = diff.abs();
		vect size = new vect((this._width+that._width)/2f,(this._height+that._height)/2f);
		if(diff_abs.x() < size.x() && diff_abs.y() < size.y()){
			vect d = size.sub(diff_abs);
			if(diff.x() < 0) d.x(-d.x());
			if(diff.y() < 0) d.y(-d.y());
			if(Math.abs(d.x()) < Math.abs(d.y())) return new vect(d.x(),0f);
			else return new vect(0f,d.y());
		}
		return new vect();
	}
	
	
}