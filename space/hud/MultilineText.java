package space.hud;

import java.awt.*;
import space.*;

public class MultilineText extends Text {
	
	protected String[] _text;
	
	public MultilineText(){ this(new String[0],0,0,0,0); }
	public MultilineText(int x, int y){ this(new String[0],x,y,x,y); }
	public MultilineText(int x, int y, int targetX, int targetY){ this(new String[0],x,y,targetX, targetY); }
	public MultilineText(String[] text, int x, int y){ this(text,x,y,x,y); }
	public MultilineText(String[] text, int x, int y, int targetX, int targetY){
		super(x,y,targetX,targetY);
		this._text = text;
	}
	
	public void paint(Graphics2D g){
		if(this._visible){
			g.setFont(this._font);
			for(int i = 0; i < this._text.length; i++){
				int y = this._y + (this._size+2) * i;
				String t = this._text[i];
				if(t != null){
					g.setColor(color.DROPSHADOW);
					g.drawString(t,this._x+2,y+2);
					g.setColor(this._color);
					g.drawString(t,this._x,y);
				}
			}
		}
	}
	
	public void text(String[] value){ this._text = value; }
	
}