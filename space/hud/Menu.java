package space.hud;

import java.util.*;

public abstract class Menu extends Element implements IMouseClickListener {
	
	protected ArrayList<IFocusable> elements;
	protected ArrayList<Button> buttons;
	protected int focalIndex;
	protected IFocusable focalElement;
	protected boolean _visible;
	
	public abstract boolean requestFocus(IFocusable element);
	
	public void visible(boolean value){ this._visible = value; }
	public boolean visible(){ return this._visible; }
	
	public void add(IFocusable element){ this.elements.add(element); }
	public void add(Button button){ this.buttons.add(button); this.elements.add(button); }
	
}