package space.engine;

import java.awt.Graphics2D;
import space.*;

public abstract class Obstacle extends Sprite implements IBounded {
	
	public abstract boolean collision(Sprite that);
	public abstract boolean collision(IBounded that);
	public abstract vect resolveCollision(Sprite that);

	// Debug functions
	public void __on_paint(Graphics2D g){
		this.boundingBox().paint(g);
	}
	
	public void checkCollisions(){}
	/*public void checkCollisions(){
		for(Enemy e : _root.enemies)
			if(this.collision(e))
				e._p.add(this.resolveCollision(e));
		
		if(this.collision(_root.player)){
			Player p = _root.player;
			vect v = this.resolveCollision(p);
			p._p = p._p.add(v);
		}
	}*/
	
}