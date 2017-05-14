package space.engine;

import java.util.*;
import java.awt.event.*;
import java.awt.*;

import space.Engine;
import space.color;
import space.qmath;

public class ElectricObstacle extends Sprite implements IBounded, ActionListener {
	
	public static final int[] RANDOM = {12,15,6,5,4,0,10,6,4,6,5,14,5,13,8,0,1,4,10,7,5,11,12,5,3,6,15,13,1,2,15,14,8,1,15,9,6,0,5,11,10,4,9,1,10,5,15,2,11,15,2,3,9,6,2,0,14,10,6,8,2,3,1,8};
	
	// bits 0-3 = pattern
	public static final int PATTERN_BITS = 0x00F;
	public static final int RANDOM_PATTERN = 0;
	public static final int SEQUENTIAL_PATTERN = 1;
	public static final int CUSTOM_PATTERN = 2;
	// bits 4-7 = type
	public static final int TYPE_BITS = 0x0F0;
	public static final int TOGGLE_TYPE = 0;
	public static final int CONTINUOUS_TYPE = 1;
	// bits 8-12 = delay
	public static final int DELAY_BITS = 0xF00;
	public static final int STANDARD_DELAY = 0;
	public static final int LONG_DELAY = 1;
	public static final int MEDIUM_DELAY = 2;
	public static final int SHORT_DELAY = 3;
	
	public static class ElectricNode extends WallObstacle {
		
		protected static final BasicStroke DEFAULT_STROKE = new BasicStroke();
		protected static final BasicStroke STROKE = new BasicStroke(3);
		protected static final Color LINE = color.setAlpha(color.MMETAL,0.75f);
		protected static final Color FILL = color.DMETAL;
		
		protected float _scale;
		protected boolean _charging;
		protected ElectricObstacle _parent;
		
		public ElectricNode(vect position, vect velocity, ElectricObstacle parent){
			super(position.sub(new vect(8f,8f)),position.add(new vect(8f,8f)),velocity);		
			this._width = 16;
			this._height = 16;
			this._scale = 0f;
			this._charging = false;
			this._parent = parent;
		}
		
		public void charge(){
			this._charging = true;
			this._scale = 0f;
		}
		
		public void loop(){
			if(this._charging){
				this._scale+=0.02f;
				if(this._scale >= 1f){
					this._scale = 1f;
					this._charging = false;
					this._parent.electrify();
				}
			} else {
				this._scale *= 0.75f;
				if(this._scale < 0.01f) this._scale = 0f;
			}
			this._p = this._p.add(this._v);
			if(this._p.y() > Engine.STAGE_HEIGHT)
				this._parent.removeNode(this);
			this._boundingBox._center = this._p;
		}
		
		public void paint(Graphics2D g){
			int x = this._p.x(vect.ROUND)-8;
			int y = this._p.y(vect.ROUND)-8;
			
			g.setColor(color.DROPSHADOW);
			g.fillOval(x+2, y+2, 16, 16);
			
			if(this._scale != 0f)
				g.setColor(color.mix(color.ELECTRICITY, FILL, this._scale));
			else
				g.setColor(FILL);
			g.fillOval(x, y, 16, 16);
			g.setStroke(STROKE);
			g.setColor(LINE);
			g.drawOval(x, y, 16, 16);
			
			g.setColor(color.HIGHLIGHT);
			g.fillOval(x+2, y+2, 6, 6);
			
			if(this._charging){
				g.setColor(color.setAlpha(color.ELECTRICITY,this._scale*0.25f));
				g.fillOval(x-4,y-4,24,24);
			}
		}
		
		public void __on_paint(Graphics2D g){
			this._boundingBox.paint(g);
		}
		
	}
	
	protected int[] _pattern;
	protected int _behaviorPattern;
	protected int _behaviorType;
	protected int _behaviorDelay;
	protected int _index;
	
	protected ElectricNode _currentNode;
	protected ElectricNode _nextNode;
	protected int _prevIndex;
	protected int _currentIndex;
	protected int _nextIndex;
	
	public ArrayList<ElectricNode> _nodes;
	protected BoundingBox _boundingBox;
	protected GameTimer _cycleTimer;
	protected int _cycleDelay;
	protected boolean _active;
	
	public ElectricObstacle(vect[] positions, vect velocity){ this(positions,velocity,0); }
	public ElectricObstacle(vect[] positions, vect velocity, int behavior){
		this._nodes = new ArrayList<ElectricNode>();
		float tlx = Engine.STAGE_WIDTH;
		float tly = Engine.STAGE_HEIGHT;
		float brx = 0;
		float bry = 0;
		for(int i = 0; i < positions.length; i++){
			vect p = positions[i];
			this._nodes.add(new ElectricNode(p, velocity, this));
			float px = p.x();
			float py = p.y();
			if(px < tlx) tlx = px;
			else if(px > brx) brx = px;
			if(py < tly) tly = py;
			else if(py > bry) bry = py;
		}
		vect tl = new vect(tlx,tly);
		vect br = new vect(brx,bry);
		vect center = tl.add(br).scale(0.5f);
		vect size = br.sub(tl);
		this._boundingBox = new BoundingBox(center,size);
		this._v = velocity;
		this._p = center;
		this._cycleTimer = new GameTimer(2000,this);
		this.behavior(behavior);
		this._cycleTimer.start();
		this._active = false;
	}
	
	public void loop(){
		vect c = this._boundingBox._center.add(this._v);
		this._boundingBox._center = c;
	}
	
	public void display(){
		for(ElectricNode node : this._nodes)
			_root.add(node);
	}
	
	public int nextNode(){
		int n = this._nodes.size();
		if(n > 1){
			this._index++;
			if(this._index >= (this._pattern.length-1))
				this._index = 0;
			int node = this._pattern[this._index]%n;
			return node;
		} else
			return 0;
	}
	
	public void removeNode(ElectricNode node){
		this._nodes.remove(node);
		_root.remove(node);
		int n = this._nodes.size();
		if(n < 1){
			_root.remove(this);
		} else {
			if(n < 2)
				this._cycleTimer.stop();
			this.calculateBounds();
		}
	}
	
	public void electrify(){
		//TODO prevent both nodes from calling this function, add some kind of timer to the electricity
		Electricity e;
		if(this._behaviorType == TOGGLE_TYPE)
			e = new Electricity(this._currentNode._p,this._nextNode._p,this._v, 1,Electricity.ENEMY,this._cycleDelay);
		else
			e = new Electricity(this._currentNode._p,this._nextNode._p,this._v, 1,Electricity.ENEMY,this._cycleDelay+250);
		_root.add(e);
	}
	
	public void actionPerformed(ActionEvent e){
		if(this._behaviorType != TOGGLE_TYPE || !this._active){
			int n = this._nodes.size();
			if(n > 2){
				this._prevIndex = this._currentIndex;
				this._currentIndex = this._nextIndex;
				if(this._behaviorPattern == SEQUENTIAL_PATTERN)
					this._nextIndex = (this._currentIndex+1)%this._nodes.size();
				else
					this._nextIndex = this.nextNode();
				if(this._behaviorPattern == RANDOM_PATTERN){
					while(this._nextIndex == this._currentIndex || this._nextIndex == this._prevIndex){
						this._nextIndex = (this._nextIndex+1)%n;
					}
				}
			} else {
				this._currentIndex = this._prevIndex = 0;
				this._nextIndex = 1;
			}
			this._currentNode = this._nodes.get(this._currentIndex);
			this._nextNode = this._nodes.get(this._nextIndex);
			this._currentNode.charge();
			this._nextNode.charge();
			this._active = true;
		} else
			this._active = false;
	}
	
	public void calculateBounds(){
		float tlx = Engine.STAGE_WIDTH;
		float tly = Engine.STAGE_HEIGHT;
		float brx = 0;
		float bry = 0;
		for(ElectricNode node : this._nodes){
			float nx = node.x();
			float ny = node.y();
			if(nx < tlx) tlx = nx;
			else if(nx > brx) brx = nx;
			if(ny < tly) tly = ny;
			else if(ny > bry) bry = ny;
		}
		vect tl = new vect(tlx,tly);
		vect br = new vect(brx,bry);
		vect center = tl.add(br).scale(0.5f);
		vect size = br.sub(tl);
		this._boundingBox = new BoundingBox(center,size);
	}
	
	public BoundingBox boundingBox(){ return this._boundingBox; }
	
	public int[] pattern(){ return this._pattern; }
	public void pattern(int[] value){
		if(value == null){
			this._pattern = RANDOM;
			this._index = qmath.randomInt()%RANDOM.length;
			this._prevIndex = this._nextIndex = this._currentIndex = RANDOM[this._index]%this._nodes.size();
		} else {
			this._pattern = value;
		}
	}
	
	public int behavior(){ return this._behaviorType | this._behaviorPattern; }
	public void behavior(int value){
		this._behaviorPattern = (value & PATTERN_BITS);
		this._behaviorType = (value & TYPE_BITS)>>4;
		this._behaviorDelay = (value & DELAY_BITS)>>8;
		if(this._behaviorPattern == RANDOM_PATTERN){
			this._pattern = RANDOM;
			this._index = qmath.randomInt()%RANDOM.length;
			this._prevIndex = this._nextIndex = this._currentIndex = RANDOM[this._index]%this._nodes.size();
		}
		switch(this._behaviorDelay){
			case LONG_DELAY:
				this._cycleDelay = 2500;
				break;
			case MEDIUM_DELAY:
				this._cycleDelay = 1500;
				break;
			case SHORT_DELAY:
				this._cycleDelay = 750;
				break;
			default:
				this._cycleDelay = 2000;
		}
		this._cycleTimer.setDelay(this._cycleDelay);
	}
	
	public void __on_paint(Graphics2D g){
		this._boundingBox.paint(g);
	}
	
}