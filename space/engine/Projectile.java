package space.engine;

import space.Engine;
import space.color;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

public class Projectile extends Sprite implements IColor {
	
	// ORIGIN types
	public static final byte PLAYER = 0;
	public static final byte ENEMY = 1;
	public static final byte OTHER = 2;
	
	// prerendered projectile graphic
	protected static final BasicStroke STROKE = new BasicStroke(4f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
	protected static BufferedImage IMAGE_W = new BufferedImage(8,8,BufferedImage.TYPE_INT_ARGB);
	protected static BufferedImage IMAGE_B = new BufferedImage(8,8,BufferedImage.TYPE_INT_ARGB);
	static {
		Graphics2D img_gfx = (Graphics2D)IMAGE_W.getGraphics();
		img_gfx.setColor(Engine.getColor(true));
		img_gfx.setStroke(STROKE);
		img_gfx.drawLine(4, 6, 4, 2);
		img_gfx.dispose();
		img_gfx = (Graphics2D)IMAGE_B.getGraphics();
		img_gfx.setColor(Engine.getColor(false));
		img_gfx.setStroke(STROKE);
		img_gfx.drawLine(4, 6, 4, 2);
		img_gfx.dispose();
	}
	
	protected byte _origin;
	protected float _damage;
	protected boolean _color;
	
	protected BufferedImage _image = new BufferedImage(8,16,BufferedImage.TYPE_INT_ARGB);
	
	protected vect _a;
	protected float _maxSpeed;

	public Projectile(vect position, vect velocity, float damage, boolean color, byte origin){
		this._p = position;
		this._v = velocity;
		this._a = new vect();
		this._damage = damage;
		this._origin = origin;
		this._image = color?IMAGE_W:IMAGE_B;
		this._width = 1;
		this._height = 2;
		//this._v = velocity.scale(0.1f);
		//this._a = velocity.scale(0.375f);
		this._maxSpeed = velocity.length();
		this._color = color;
		/*float cos = velocity.cos();
		float sin = velocity.sin();
		Graphics2D g = (Graphics2D)this._image.getGraphics();
		Engine.trace(velocity.angle()+Math.PI/2);
		g.rotate(velocity.angle()+Math.PI/2);
		g.setColor(Color.GREEN);
		g.setStroke(STROKE);	
		g.drawLine(4,0,4,12);
		g.dispose();*/
		//image_gfx.transform(new AffineTransform(cos, sin, -cos, sin, 0f,0f));
	}
	
	// getters
	public float damage(){ return this._damage; }
	public byte origin(){ return this._origin; }
	public boolean color(){ return this._color; }
	
	public void hit(GameObject obj){
		obj.hit(this._damage, this._color);
		_root.remove(this);
		_root.add(new Blast(this._p,10f,0.15f,this._color));
	}
	
	// methods
	public void update(){
		/*vect v = this._v.add(this._a);
		float v_len = v.length();
		if(v_len > this._maxSpeed) v = v.scale(this._maxSpeed/v_len);
		this._v = v;*/
		this._p = this._p.add(this._v);
		if(this.isOOB())
			_root.remove(this);
		else switch(this._origin) {
			// player projectile
			case PLAYER:
				ArrayList<Enemy> enemies = _root.enemies;
				for(Enemy enemy : enemies){
					if(enemy.boundingBox().hitTest(this._p)){
						this.hit(enemy);
						break;
					}
				}
			break;
			// enemy projectile
			case ENEMY:
				if(_root.player.boundingBox().hitTest(this._p))
					this.hit(_root.player);
			break;
			// other
			default:
			break;		
		}
	}
	
	// Overridden Sprite methods
	public void loop(){ this.update(); }
	public void paint(Graphics2D g){
		int x = this._p.x(vect.ROUND);
		int y = this._p.y(vect.ROUND);
		//GradientPaint gradient = new GradientPaint();
		//g.setColor(Engine.getColor(this._color));
		//g.setStroke(Projectile.STROKE);
		//g.drawLine(x,y,x,y-3);
		g.drawImage(this._image,x-4,y-8,_root);
	}
	
	public void collide(Obstacle that){
		this._p = new vect(-1f,-1f);
		this._v = new vect();
	}
	
}