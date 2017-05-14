package space.engine;

import javafx.stage.Stage;
import space.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.*;

// TODO: make it so player can't kill boss by collision

public class Boss extends Enemy {

    public static class BossAttack extends EffectGenerator {

    }

	public static final int FLIP = 1;
	public static final int SIDE_TO_SIDE = 2;
	public static final int CIRCLE = 4;
	public static final int FIGURE_8 = 6;
	public static final int SWEEP = 8;
	public static final int SMART = 10;
	public static final int MOVE_TO_POSITION = 12;

	protected int _movementPattern;
	protected Queue<Integer> _movementPatterns;
	protected float _count;
	protected vect[] _gunPositions;
	protected vect _center;
	protected vect _targetPosition;

	public Boss(int level, int difficulty, int spriteID){
		super();
		this._width = BossSprites.WIDTH;
		this._height = BossSprites.HEIGHT;
		this._p = new vect(Engine.STAGE_WIDTH/2,-180);
		this._center = new vect(this._p.x(), 180);
		this._v = new vect();
		this._spriteID = spriteID;
		this._sprite = BossSprites.getSprite(this._spriteID, this._color);
		this._gunPositions = BossSprites.getGunPositions(this._spriteID);
		this._boundingBox = BossSprites.getBounds(this._spriteID);
		this._boundingBox.center(this._p);
		this._targetPosition = new vect(Engine.STAGE_WIDTH/2,180);
		this._count = 0f;

		this.setStats(level, difficulty);

		this._movementPatterns = new PriorityQueue<>();
		this.changeMovementPattern();
	}

	public void loop(){
		this.move();
		this._boundingBox.center(this._p);
		this._color = _root.player.color();
		this._sprite = BossSprites.getSprite(this._spriteID,this._color);
	}

	public void move(){
		boolean isFlipped = (this._movementPattern & 1) == 1;
		float flip = isFlipped?-1:1;
		int pattern = (isFlipped)?(this._movementPattern-1):(this._movementPattern);
		switch(pattern){
			case SIDE_TO_SIDE: default: {
				this._v = new vect(flip*qmath.cos(this._count),qmath.sin(this._count*2)*0.2f);
				this._count += 0.012f;
				if(this._count > 6.28f) this.changeMovementPattern();  // TODO: change these based off of boss's hp and player stats, change to a random move
			} break;

			case CIRCLE: {
				this._v = new vect(flip*qmath.cos(this._count),qmath.sin(this._count)*0.8f);
				this._count += 0.012f;
				if(this._count > 6.28f) this.changeMovementPattern();
			} break;

			case FIGURE_8: {
				this._v = new vect(flip*qmath.cos(this._count*2)*2,qmath.cos(this._count)*0.8f);
				this._count += 0.012f;
				if(this._count > 6.28f) this.changeMovementPattern();
			} break;

			case SWEEP: {
				this._v = new vect(flip*1.25f,0);
				if((isFlipped && this._p.x() < 80) || (!isFlipped && this._p.x() > Engine.STAGE_WIDTH-80))
					this.changeMovementPattern();
			} break;

			case SMART : {
				vect d = _root.player._p.scale(1f,0.5f+qmath.sin(this._count)*0.05f);
				d = d.sub(this._p);
				if(d.lengthSq() < 4f) d = new vect();
				else d = d.qnormalize().scale(0.05f,0.025f);
				this._v = this._v.scale(0.97f).add(d);
				this._count += 0.012f;
				if(this._count > 6.28f) this.changeMovementPattern();
			} break;

			case MOVE_TO_POSITION: {
				vect d = this._targetPosition.sub(this._p);
				if(d.lengthSq() > 1f){
					d = d.scale(0.05f);
					if(d.lengthSq() > 16)
						d = d.qnormalize().scale(4f);
					d = d.sub(this._v).scale(0.05f);
					this._v = this._v.scale(0.975f).add(d);
				} else {
					this._p = this._targetPosition;
					this._movementPattern = this._movementPatterns.remove();
				}
			} break;

		}

		this._p = this._p.add(this._v);
	}

	public void paint(Graphics2D g){
		if(this._visible){
			int x = this._p.x(vect.ROUND);
			int y = this._p.y(vect.ROUND);
			g.drawImage(this._sprite, x-BossSprites.CENTER_X, y-BossSprites.CENTER_Y, _root);
		}
	}

	public BoundingBox boundingBox(){ return this._boundingBox; }

	public void changeMovementPattern(){
		int max = MOVE_TO_POSITION - 2;
		int pattern = (qmath.randomInt()%max)+2;
		this.changeMovementPattern(pattern);
	}

	public void changeMovementPattern(int movementPattern){
		boolean flip = (movementPattern & 1) == 1;
		int pattern = (flip)?(movementPattern-1):(movementPattern);
		switch(pattern){
			case SIDE_TO_SIDE: case CIRCLE: default: {
				this._targetPosition = new vect(Engine.STAGE_WIDTH/2,180);
			} break;

			case FIGURE_8: {
				this._targetPosition = new vect(Engine.STAGE_WIDTH/2,180);
			} break;

			case SWEEP: {
				float x = (flip)?(Engine.STAGE_WIDTH-64):(64);
				this._targetPosition = new vect(x,240);
			} break;

			case SMART : {
				this._targetPosition = new vect(_root.player._p.x(),180f);
			} break;

			case MOVE_TO_POSITION: {
			} break;

		}
		this._movementPatterns.add(movementPattern);
		this._movementPattern = MOVE_TO_POSITION;
		this._count = 0f;
	}

	@Override
	protected void setStats(int level, int difficulty){
		// 1<=difficulty<=3
		difficulty = difficulty<1?1:(difficulty>3?3:difficulty);
		this._experience = level*100;
		int exp = Engine.experienceFunction(level);
		// HP = exp
		this._maxHP = this._HP = exp*100;
		// SHIELD = (difficulty-1)*5
		this._shield.setProperties(64, (difficulty)*5, difficulty*0.25f, 0.01f, 5000 - difficulty*1000);
		// POWER = ceil(level/10) * difficulty
		this._power = (float)(Math.ceil((double)level/10.0) * difficulty);
		// SPEED
		this._speed = 1;
		// aggressiveness:
		// lvl 1 easy = 5990 lvl 50 easy = 5500 lvl 100 easy = 5000
		// lvl 1 med  = 5980 lvl 50 med  = 5000 lvl 100 med  = 4000
		// lvl 3 hard = 5970 lvl 50 hard = 4500 lvl 100 hard = 3000
		float aggro = (((float)level/100)*difficulty);
		int delay = 6000 - (int)(aggro*1000);
		this._attackTimer = new GameTimer(delay,this);
	}

	@Override
	public void checkCollisions(){
		vect d = _root.player._p.sub(this._p);
		if(d.lengthSq() < 4096){
			d = d.qnormalize().scale(64f);
			_root.player._p = this._p.add(d);
		}
	}

	public void __on_paint(Graphics2D g){
		int x = this._p.x(vect.ROUND);
		int y = this._p.y(vect.ROUND);
		g.setColor(color.DEBUGCOLOR);
		g.setStroke(new BasicStroke());
		this._boundingBox.paint(g,true);
		for(vect v : this._gunPositions){
			int vx = v.x(vect.ROUND)+x-BossSprites.CENTER_X;
			int vy = v.y(vect.ROUND)+y-BossSprites.CENTER_Y;
			g.drawRect(vx-1,vy-1,2,2);
		}
	}
	
}

class BossScript implements ActionListener {

	protected Engine _root;
	protected Boss _boss;
	protected GameTimer _timer;
	protected int _timerCount;

	protected String[] _instructions;
	protected String _currentInstruction;
	protected int _index;

	public BossScript(Boss boss, String[] instructions){
		this._boss = boss;
		this._instructions = instructions;
		this._currentInstruction = "";
		this._index = 0;
		this._timer = new GameTimer(1000,this);
	}

	public void actionPerformed(ActionEvent ae){
		this._timerCount++;
	}

}

class BossSprites {

	static int WIDTH = 128;
	static int HEIGHT = 128;
	static int CENTER_X = 64;
	static int CENTER_Y = 64;

	static BufferedImage SPRITE1W = new BufferedImage(128,128,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage SPRITE1B = new BufferedImage(128,128,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage[] SPRITE1 = {SPRITE1B,SPRITE1W};
	static vect[] GUNS1 = {new vect(37,122), new vect(64,82), new vect(91,122)};
	static BoundingBoxGroup BOUNDS1 = new BoundingBoxGroup(
			new BoundingBox(new vect(0,0), new vect(128,128)),
			new BoundingBox(new vect(0,-24), new vect(64,80)),
			new BoundingBox(new vect(-48,-56), new vect(32,16)),
			new BoundingBox(new vect(48,-56), new vect(32,16))
	);

	static BufferedImage[][] SPRITES = {SPRITE1};
	static vect[][] GUNS = {GUNS1};
	static BoundingBoxGroup[] BOUNDS = {BOUNDS1};

	static BufferedImage getSprite(int spriteNumber, boolean color){ return SPRITES[spriteNumber][color?1:0]; }
	static vect[] getGunPositions(int spriteNumber){ return GUNS[spriteNumber]; }
	static BoundingBoxGroup getBounds(int spriteNumber){ return BOUNDS[spriteNumber]; }

	static {
		Color shadow = color.setAlpha(color.DGREY,0.7f);
		Color highlight = color.setAlpha(color.LGREY,0.5f);
		BasicStroke stroke = new BasicStroke(2);
		BasicStroke stroke2 = new BasicStroke(1f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
		Color white = Engine.getColor(true);
		Color black = Engine.getColor(false);
		Color mwhite = color.darken(white,0.1f);
		Color dwhite = color.darken(white,0.2f);
		Color mblack = color.darken(black,0.1f);
		Color dblack = color.darken(black,0.2f);
		int width = 128, height = 128;
		int cx = 64, cy = 64;
		Polygon poly;
		Graphics2D g;

		// image1 - white
		g = (Graphics2D)SPRITE1W.getGraphics();
		g.setColor(white);
		g.fillRect(32,16,64,64);
		g.fillRect(48,0,32,16);
		g.setColor(mwhite);
		g.fillRect(0,0,32,16);
		g.fillRect(96,0,32,16);
		g.setColor(dwhite);
		g.fillRect(16,16,16,16);
		g.fillRect(96,16,16,16);
		g.fillRect(24,32,8,80);
		g.fillRect(96,32,8,80);
		g.fillRect(32,104,10,16);
		g.fillRect(86,104,10,16);
		// image1 - black
		g = (Graphics2D)SPRITE1B.getGraphics();
		g.setColor(black);
		g.fillRect(32,16,64,64);
		g.fillRect(48,0,32,16);
		g.setColor(mblack);
		g.fillRect(0,0,32,16);
		g.fillRect(96,0,32,16);
		g.setColor(dblack);
		g.fillRect(16,16,16,16);
		g.fillRect(96,16,16,16);
		g.fillRect(24,32,8,80);
		g.fillRect(96,32,8,80);
		g.fillRect(32,104,10,16);
		g.fillRect(86,104,10,16);
	}

}