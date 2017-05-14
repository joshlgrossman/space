package space.engine;

import space.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class TextSprite extends Sprite {
	
	protected Font _font;
	protected String _text;
	protected vect _delta;
	protected vect _origin;
	protected vect _a;
	protected int _delay;
	protected float _deltaScalar;
	protected float _alpha;
	protected Color _color;
	
	public TextSprite(String text, vect position, vect targetPosition, Color color, int delay){
		this._text = text;
		this._origin = this._p = position;
		this._delta = targetPosition.sub(position);
		this._color = color;
		this._deltaScalar = 0f;
		this._alpha = 1f;
		this._font = Engine.getFont(10);
		this._delay = delay;
		this._v = new vect(0f,-1f);
		this._a = new vect(0f,1f/((float)delay+1f));
	}
	
	public void loop(){
		float diff = 1f-this._deltaScalar;
		if(this._delay-- < 0) {	
			this._deltaScalar += (diff)*0.05f;
			vect delta = this._delta.scale(this._deltaScalar);
			this._p = this._origin.add(delta);
			if(diff < 0.05f){
				this._p = this._origin.add(this._delta);
				_root.removeHUD(this);
			}
		} else {
			this._v = this._v.add(this._a);
			this._p = this._p.add(this._v);
		}
		this._alpha = diff;
	}
	
	public void paint(Graphics2D g){
		float x = this._p.x();
		float y = this._p.y();
		g.setFont(this._font);
		g.setColor(color.setAlpha(this._color,this._alpha));
		g.drawString(this._text, x, y);
	}
	
}