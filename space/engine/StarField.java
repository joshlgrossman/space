// Scrolling star field for the background
package space.engine;

import space.Engine;
import java.awt.*;
import java.awt.image.*;

public class StarField extends Sprite {
	
	protected float _speed = 1.0f;
	
	private Image bg;
	private Star[] stars;
	
	public void initialize(){
		int n;
		switch(Engine.QUALITY){
			case 0: n = 100; break;
			case 2: n = 300; break;
			default: n = 200;
		}
		this.stars = new Star[n];
		for(int i = 0; i < n; i++)
			this.stars[i] = new Star(Math.random()*0.5+0.05);
		bg = new BufferedImage(Engine.STAGE_WIDTH,Engine.STAGE_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		Graphics bg_gfx = bg.getGraphics();
		for(int i = n*5; i > 0; i--){
			int x = (int)(Math.random()*Engine.STAGE_WIDTH);
			int y = (int)(Math.random()*Engine.STAGE_HEIGHT);
			double scale = Math.random()*0.1+0.05;
			bg_gfx.setColor(new Color(1.0f, 1.0f, 1.0f, (float)scale));
			bg_gfx.drawLine(x, y, x, y-1);
		}
	}
	
	public void checkCollisions(){}
	
	public void loop(){
		for(Star star : this.stars)
			star.move(this._speed);
	}
	
	public void paint(Graphics2D g){
		g.drawImage(bg, 0, 0, _root);
		for(Star star : this.stars)
			star.paint(g);
	}
	
	public float speed(){ return this._speed; }
	public void speed(float value){ this._speed = value; }
	
	
}

class Star {
	vect p;
	double scale;
	Color color_1;
	Color color_2;
	Color color_3;
	
	Star(double scale){
		this.scale = scale;
		this.p = new vect((float)Math.random()*Engine.STAGE_WIDTH,(float)Math.random()*Engine.STAGE_HEIGHT);
		color_1 = new Color(1.0f,1.0f,1.0f,(float)(0.25*scale));
		color_2 = new Color(1.0f,1.0f,1.0f,(float)(0.5*scale));
		color_3 = new Color(1.0f,1.0f,1.0f,(float)(scale));
	}
	
	void move(double speed){
		float py = p.y();
		float px = p.x();
		py += speed * scale;
		if(py > Engine.STAGE_HEIGHT){
			py = -10;
			px = (float)Math.random()*Engine.STAGE_WIDTH;
		}
		this.p = new vect(px,py);
	}
	
	void paint(Graphics2D g){
		int px = p.x(vect.ROUND);
		int py = p.y(vect.ROUND);
		int dy = (int)(scale*2)+1;
		g.setColor(color_1);
		g.drawLine(px,py,px,py-(dy*3));
		g.setColor(color_2);
		g.drawLine(px,py,px,py-(dy*2));
		g.setColor(color_3);
		g.drawLine(px,py,px,py-dy);
	}
	
}