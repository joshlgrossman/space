package space.engine;

import java.awt.*;

import space.Engine;

public class SpriteList {
	
	protected Sprite _first;
	protected SpriteList _next;
	
	public SpriteList(){ this(null, null); }
	public SpriteList(Sprite first){ this(first,null); }
	public SpriteList(Sprite first, SpriteList next){ this._first = first; this._next = next; }
	
	// methods
	public void add(Sprite sprite){
		if(this._next != null) this._next.add(sprite);
		else if(this._first != null) this._next = new SpriteList(sprite);
		else this._first = sprite;
	}
	
	public void add(SpriteList list){
		if(this._next != null) this._next.add(list);
		else this._next = list;
	}
	
	public boolean remove(Sprite sprite){
		if(this._first.equals(sprite)){
			if(this._next != null){
				this._first = this._next._first;
				this._next = this._next._next;
			} else 
				this._first = null;
			return true;
		} else if(this._next != null)
			return this._next.remove(sprite);
		else
			return false;
	}
	
	public void push(Sprite sprite){
		this._next = new SpriteList(this._first,this._next);
		this._first = sprite;
	}
	
	public void push(SpriteList list){
		list.add(this);
		this._first = list._first;
		this._next = list._next;
	}
	
	public Sprite pop(){
		Sprite sprite = this._first;
		if(this._next != null){
			this._first = this._next._first;
			this._next = this._next._next;
		}
		return sprite;
	}
	
	public int indexOf(Sprite sprite){
		if(sprite.equals(this._first))
			return 0;
		else if(this._next != null){		
			int index = this._next.indexOf(sprite);
			return (index<0)?(-1):(1 + index);
		} else 
			return -1;
	}
	
	public Sprite spriteAt(int index){
		if(index <= 0) return this._first;
		else if(this._next != null) return this._next.spriteAt(index-1);
		else return null;
	}
	
	public Sprite[] toArray(){
		int length = this.length();
		Sprite[] array = new Sprite[length];
		SpriteList current = this;
		for(int i = 0; i < length; i++){
			array[i] = current._first;
			current = current._next;
		}
		return array;
	}
	
	// Sprite methods
	public void loop(){
		if(this._next != null) this._next.loop();
		if(this._first != null)	this._first.loop();
	}
	
	public void paint(Graphics2D g){
		if(this._next != null) this._next.paint(g);
		if(this._first != null) this._first.paint(g);
	}
	
	public void checkCollisions(){
		if(this._next != null) this._next.checkCollisions();
		if(this._first != null) this._first.checkCollisions();
	}
	
	public void onKeyEvent(byte keyCode, boolean isPressed){
		if(this._next != null) this._next.onKeyEvent(keyCode,isPressed);
		if(this._first != null) this._first.onKeyEvent(keyCode,isPressed);
	}
	// getters
	public Sprite first(){ return this._first; }
	public SpriteList next(){ return this._next; }
	
	public boolean isEmpty(){ return this._first == null && this._next == null; }
	public boolean hasFirst(){ return this._first != null; }
	public boolean hasNext(){ return this._next != null; }
	
	public int length(){
		if(this._first != null)
			return (this._next == null)?(1):(1 + this._next.length());
		else
			return 0;
	}
	
	// Debug functions
	public void __on_paint(Graphics2D g){
		if(this._first != null) this._first.__on_paint(g);
		if(this._next != null) this._next.__on_paint(g);
	}
	
}