package space.engine;

import space.qmath;
import java.util.Comparator;

public final class vect {
	// rounding functions
	public static final byte FLOOR = 0;
	public static final byte ROUND = 1;
	public static final byte CEIL = 2;
	
	private float _x;
	private float _y;
	
	public vect(){ this(0.0f,0.0f); }
	public vect(float x, float y){
		this._x = x;
		this._y = y;
	}
	
	// static functions
	public static vect createCartesian(float x, float y){ return new vect(x,y); }
	public static vect createPolar(float r, float theta){ return new vect((float)(Math.cos(theta)*r), (float)(Math.sin(theta)*r)); }
	public static vect qcreatePolar(float r, float theta){ return new vect(qmath.cos(theta)*r,qmath.sin(theta)*r); }
	public static vect toVect(Object obj){ return (obj != null && obj instanceof vect)?((vect)obj):(new vect(0f,0f)); }
	public static vect toVect(int i){ return new vect(i,0f); }
	public static vect toVect(float f){ return new vect(f,0f); }
	public static vect toVect(double d){ return new vect((float)d,0f); }

	// methods
	public vect copy(){ return new vect(this._x,this._y); }
	
	public float length(){ return (float)Math.sqrt(this._x*this._x+this._y*this._y); }
	public float qlength(){ return qmath.sqrt(this._x*this._x+this._y*this._y); }
	public float lengthSq(){ return this._x*this._x+this._y*this._y; }
	public float angle(){ return (float)Math.atan2(this._y, this._x); }
	public float cos(){ return this._x/this.length(); }
	public float sin(){ return this._y/this.length(); }
	
	public float dot(vect that){ return this._x * that._x + this._y * that._y; }
	public float dot(Object that){ return this.dot(vect.toVect(that)); }
	public float cross(vect that){ return this._x * that._y - this._y * that._x; }
	public float cross(Object that){ return this.cross(vect.toVect(that)); }
	
	public vect normalize(){
		float length = this.length();
		return new vect(this._x/length, this._y/length);
	}
	public vect qnormalize(){
		float l = qmath.sqrt(this._x*this._x+this._y*this._y);
		return new vect(this._x/l,this._y/l);
	}
	
	public vect project(vect axis){
		float k = this.dot(axis)/axis.dot(axis);
		return axis.scale(k);
	}
	public vect project(Object axis){ return this.project(vect.toVect(axis)); }

	public vect negate(){ return new vect(-this._x, -this._y); }
	
	public vect abs(){ return new vect(Math.abs(this._x),Math.abs(this._y)); }
	
	public vect scale(float k){ return new vect(this._x * k, this._y * k); }
	public vect scale(float kx, float ky){ return new vect(this._x*kx,this._y*ky); }
	public vect rotate(float theta){
		float l = (float)Math.sqrt(this._x*this._x+this._y*this._y);
		float a = (float)Math.atan2(this._y, this._x) + theta;
		return new vect((float)(Math.cos(a)*l), (float)(Math.sin(a)*l));
	}
	public vect qrotate(float theta){
		float l = qmath.sqrt(this._x*this._x+this._y*this._y);
		float a = (float)Math.atan2(this._y,this._x)+ theta;
		return new vect(qmath.cos(a)*l,qmath.sin(a)*l);
	}
	
	public vect add(vect that){ return new vect(this._x + that._x, this._y + that._y); }
	public vect add(Object that){ return this.add(vect.toVect(that)); }
	
	public vect sub(vect that){ return new vect(this._x - that._x, this._y - that._y); }
	public vect sub(Object that){ return this.sub(vect.toVect(that)); }
	
	public boolean isInside(vect top_left, vect bottom_right){ return (this._x>top_left._x) && (this._x<bottom_right._x) && (this._y>top_left._y) && (this._y<bottom_right._y); }	
	public boolean isInside(vect center, float radius){ return (this.sub(center).length() <= radius); }
	
	public String toString(){ return "<"+this._x+","+this._y+">"; }
	public String toString(byte roundingFunction, int precision){
		float exp = (float)Math.pow(10,precision);
		float x = this._x * exp;
		float y = this._y * exp;
		switch(roundingFunction){
			case ROUND:
				x = (int)(Math.round(x));
				y = (int)(Math.round(y));
				break;
			case CEIL:
				x = (int)(Math.ceil(x));
				y = (int)(Math.ceil(y));
				break;
			default: case FLOOR:
				x = (int)x;
				y = (int)y;
				break;
		}
		return "<"+(x/exp)+","+(y/exp)+">";
	}
	
	// getters/setters
	public float x(){ return this._x; }
	public void x(float value){ this._x = value; }
	public float y(){ return this._y; }
	public void y(float value){ this._y = value; }
	
	public int x(byte roundingFunction){
		switch(roundingFunction){
			case ROUND:	return (int)(Math.round(this._x));
			case CEIL:  return (int)(Math.ceil(this._x));
			default: case FLOOR: return (int)(this._x);
		}
	}	
	public int y(byte roundingFunction){
		switch(roundingFunction){
			case ROUND:	return (int)(Math.round(this._y));
			case CEIL:  return (int)(Math.ceil(this._y));
			default: case FLOOR: return (int)(this._y);
		}
	}

}