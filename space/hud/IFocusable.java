package space.hud;

public interface IFocusable {
	
	public void setFocus(boolean value);
	public boolean hasFocus();
	
	public void activate();
	
	public Menu menu();
	
}