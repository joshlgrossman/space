package space.engine;

import space.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

public class PatternEnemy extends Enemy implements ActionListener {
	
	public static final int LOOP = Pattern.LOOP;
	public static final int FLIP = Pattern.FLIP;
	public static final int L_SHAPE = 4;
	public static final int S_SHAPE = 8;
	public static final int U_SHAPE = 12;
	public static final int CIRCLE = 16;
	public static final int LINE = 20;
	
	protected Pattern _pattern;
	protected int _patternID;
	protected GameTimer _patternTimer;
	
	// numLoops < 0 = loop indefinitely
	public PatternEnemy(int level, int difficulty, vect position, boolean color, int patternID){ this(level,difficulty,position,color,0,patternID,0); }
	public PatternEnemy(int level, int difficulty, vect position, boolean color, int patternID, int numLoops){ this(level,difficulty,position,color,0,patternID,0); }
	public PatternEnemy(int level, int difficulty, vect position, boolean color, int enemySprite, int patternID, int numLoops){
		super(color,enemySprite);
		this._p = position;
		this._width = this._height = 16;
		this._patternID = patternID;
		this._patternTimer = new GameTimer(150,this);
		this._patternTimer.start();
		this._pattern = new Pattern(patternID, numLoops);
		this.setStats(level, difficulty);
		this._attackTimer.start();
	}
	
	public void loop(){
		this._p = this._p.add(this._v);
		if(this.isOOOB())
			_root.remove(this);
	}
	
	public void shoot(){
		vect delta = _root.player._p.sub(this._p);
		vect v = delta.scale(4f/delta.length());
		_root.add(new Projectile(this._p,v,this._power,this._color,Projectile.ENEMY));
	}
	
	public void collide(Obstacle that){
		vect v = that.resolveCollision(this);
		this._p = this._p.add(v);
		this.kill(false);
	}
	
	
	public void updateVelocity(){
		this._v = this._pattern.next();
	}
	
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource().equals(this._patternTimer)) this.updateVelocity();
		else super.actionPerformed(ae);
	}
	
}

class Pattern {
	
	static final int LOOP = 1;
	static final int FLIP = 2;
	
	static final Pattern L_SHAPE = new Pattern(0,-1,new vect(0, 4), new vect(0, 4), new vect(0, 4), new vect(0, 4), new vect(0, 4), new vect(0, 4), new vect(0, 4), new vect(0, 4), new vect(0, 3), new vect(0, 2), new vect( 0.25f, 1.9f), new vect( 1, 1.7f), new vect( 1.414f, 1.414f), new vect( 1.7f, 1f), new vect( 2,0), new vect( 3,0), new vect( 4,0));
	static final Pattern S_SHAPE = new Pattern(0,-1,new vect(-2.77f,0.77f),new vect(-2.12f,1.41f),new vect(-1.15f,1.85f),new vect(0.0f,2.0f),new vect(1.15f,1.85f),new vect(2.12f,1.41f),new vect(2.77f,0.77f),new vect(3.0f,0.0f),new vect(2.77f,0.77f),new vect(2.12f,1.41f),new vect(1.15f,1.85f),new vect(0.0f,2.0f),new vect(-1.15f,1.85f),new vect(-2.12f,1.41f),new vect(-2.77f,0.77f),new vect(-3.0f,0.0f));
	static final Pattern U_SHAPE = new Pattern(0,-1,new vect(0,4), new vect(0,4), new vect(0,4), new vect(0,4), new vect(0,3), new vect(0,2.5f), new vect(0,2), new vect(0.25f,1.9f), new vect(1, 1.7f), new vect(1.414f, 1.414f), new vect(1.7f,1), new vect(1.9f, 0.25f), new vect(2,0), new vect(2,0), new vect(1.9f,-0.25f), new vect(1.7f,-1f), new vect(1.414f,-1.414f), new vect(1f, -1.7f), new vect(0.25f,-1.9f), new vect(0,-2), new vect(0,-3), new vect(0,-4));
	static final Pattern CIRCLE = new Pattern(5,24,new vect(0,3),new vect(0,3),new vect(0,3),new vect(0,3),new vect(0,3),new vect(0.0f,3.0f),new vect(0.93f,2.85f),new vect(1.76f,2.43f),new vect(2.43f,1.76f),new vect(2.85f,0.93f),new vect(3.0f,0.0f),new vect(2.85f,-0.93f),new vect(2.43f,-1.76f),new vect(1.76f,-2.43f),new vect(0.93f,-2.85f),new vect(0.0f,-3.0f),new vect(-0.93f,-2.85f),new vect(-1.76f,-2.43f),new vect(-2.43f,-1.76f),new vect(-2.85f,-0.93f),new vect(-3.0f,0.0f),new vect(-2.85f,0.93f),new vect(-2.43f,1.76f),new vect(-1.76f,2.43f),new vect(-0.93f,2.85f), new vect(0,3),new vect(0.93f,2.85f),new vect(1.76f,2.43f),new vect(2.43f,1.76f),new vect(2.85f,0.93f),new vect(3.0f,0.0f),new vect(2.85f,-0.93f),new vect(2.43f,-1.76f),new vect(1.76f,-2.43f),new vect(0.93f,-2.85f),new vect(0.0f,-3.0f));
	static final Pattern LINE = new Pattern(0,-1,new vect(0,3));
	
	static final Pattern[] PATTERNS = {L_SHAPE,S_SHAPE,U_SHAPE,CIRCLE,LINE};
	
	private vect[] _pattern;
	private int _index;
	private int _loopEndIndex;
	private int _loopStartIndex;
	private int _numLoops;
	private boolean _loops;
		
	public Pattern(int patternID, int numLoops){
		this._numLoops = numLoops;
		this._loops = (patternID & LOOP) == LOOP;
		boolean flip = (patternID & FLIP) == FLIP;
		Pattern id = PATTERNS[(patternID >> 2)-1];
		this._loopStartIndex = id._loopStartIndex;
		this._loopEndIndex = id._loopEndIndex;
		vect[] pattern = id._pattern;
		this._pattern = new vect[pattern.length];
		for(int i = pattern.length - 1; i>=0; i--){
			vect v = pattern[i];
			this._pattern[i] = new vect(flip?-v.x():v.x(),v.y());
		}
	}
	
	private Pattern(int loopStartIndex, int loopEndIndex, vect... pattern){
		this._pattern = pattern;
		this._loops = false;
		this._loopStartIndex = (loopStartIndex < 0)?this._pattern.length-1:loopStartIndex;
		this._loopEndIndex = (loopEndIndex < 0)?this._pattern.length-1:loopEndIndex;
		this._index = 0;
	}
	
	public vect next(){
		if(this._loops && this._index >= this._loopEndIndex && this._numLoops != 0){
			this._index = this._loopStartIndex;
			this._numLoops--;
		} else if (this._index < this._pattern.length - 1)
			this._index++;
		return this._pattern[this._index];
	}
	
}