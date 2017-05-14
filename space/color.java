package space;

import java.awt.Color;

public class color {
	
	public static final Color DEBUGCOLOR = Color.red;

	// specific use colors
	public static final Color PANELCOLOR = 		new Color(0x664D4857,true);
	public static final Color FADE =			new Color(0x90000000,true);
	public static final Color DROPSHADOW = 		new Color(0x55000000,true);
	public static final Color HIGHLIGHT  = 		new Color(0x22FFFFFF,true);
	public static final Color LVLUPCOLOR1 =		new Color(0xBBAED4F5,true);
	public static final Color LVLUPCOLOR2 = 	new Color(0x001C3BC7,true);
	public static final Color TRANSPARENT =		new Color(0x00888888,true);
	public static final Color SHIELDCOLOR = 	new Color(0xFFABD1DE,true);
	public static final Color SHIELDFAILCOLOR = new Color(0xFFFC7C89,true);
	public static final Color ELECTRICITY =		new Color(0xFFECF5FD,true);
	public static final Color LMETAL =			new Color(0xFFA795BA,true);
	public static final Color MMETAL =			new Color(0xFF786B87,true);
	public static final Color DMETAL = 			new Color(0xFF453952,true);
	
	// general purpose colors
	public static final Color WHITE = new Color(0xFFFFFFFF, true);
	public static final Color LGREY = new Color(0xFFCCCCCC, true);
	public static final Color GREY =  new Color(0xFF888888, true);
	public static final Color DGREY = new Color(0xFF444444, true);
	public static final Color BLACK = new Color(0xFF000000, true);
	
	public static final Color LORANGE1 = new Color(0xFFE0B15A,true);
	public static final Color MORANGE1 = new Color(0xFFD49F3D,true);
	public static final Color DORANGE1 = new Color(0xFFC98E20,true);
	
	public static final Color LRED1 = new Color(0xFFE83F52,true);
	public static final Color MRED1 = new Color(0xFFE0263B,true);
	public static final Color DRED1 = new Color(0xFFC71E31,true);
	
	public static final Color LRED2 = new Color(0xFFE8533F,true);
	public static final Color MRED2 = new Color(0xFFE0402B,true);
	public static final Color DRED2 = new Color(0xFFC9301C,true);
	
	public static final Color LGREEN1 = new Color(0xFF89EB50,true);
	public static final Color MGREEN1 = new Color(0xFF66D128,true);
	public static final Color DGREEN1 = new Color(0xFF50B019,true);
	
	public static final Color LGREEN2 = new Color(0xFF5CDE4E,true);
	public static final Color	MGREEN2 = new Color(0xFF39D128,true);
	public static final Color DGREEN2 = new Color(0xFF31AD23,true);
	
	public static final Color LBLUE1 = new Color(0xFF46A2F2,true);
	public static final Color MBLUE1 = new Color(0xFF2890EB,true);
	public static final Color DBLUE1 = new Color(0xFF1875C7,true);
	
	public static final Color LBLUE2 = new Color(0xFF5A76F2,true);
	public static final Color MBLUE2 = new Color(0xFF3051E3,true);
	public static final Color DBLUE2 = new Color(0xFF1C3BC7,true);
	
	public static final Color LPURPLE1 = new Color(0xFFCC56DB,true);
	public static final Color MPURPLE1 = new Color(0xFFB728C9,true);
	public static final Color DPURPLE1 = new Color(0xFF8E189E,true);
	
	public static final Color BROWN = new Color(0xFF473318,true);
	
	public static final Color[] FIRE = {new Color(0xFFE5ED4C,true), new Color(0xFFEDD328,true), new Color(0xFFEDAF28,true), new Color(0xFFED8128, true), new Color(0xFFF5CEA2,true)};
	
	public static Color fire(){ return FIRE[(int)(Math.random()*FIRE.length)]; }
	public static Color setAlpha(Color color, float alpha){
		int a = (int)(alpha * 255);
		int rgb = color.getRGB() & 0x00FFFFFF;
		return new Color(a<<24 | rgb,true);
	}
	public static Color brighten(Color color, float amount){ return mix(Color.white, color,amount); }
	public static Color darken(Color color, float amount){ return mix(Color.black,color,amount); }
	// mix = a * ratio + b * (1-ratio)
	public static Color mix(Color A, Color B, float ratio){
		float ka = ratio;
		float kb = 1f-ratio;
		int r = (int)(A.getRed()*ka + B.getRed()*kb);
		int g = (int)(A.getGreen()*ka + B.getGreen()*kb);
		int b = (int)(A.getBlue()*ka + B.getBlue()*kb);
		int a = (int)(A.getAlpha()*ka + B.getAlpha()*kb);
		return new Color(r,g,b,a);
	}
	
	/*private final int _r;
	private final int _g;
	private final int _b;
	private final int _a;

	// 0xRRGGBBAA
	public color(int value){
		this._r = value & 0xFF000000;
		this._g = value & 0x00FF0000;
		this._b = value & 0x0000FF00;
		this._a = value & 0x000000FF;
	}
	
	public int rgb(){ return ((this._r << 24) | (this._g << 16) | (this._b << 8)); }
	public Color value(){ return new Color(this._r,this._g,this._b,this._a); }
	
	public color alpha(int value){ return new color(this.rgb()<<8 | value); }
	public int alpha(){ return this._a; }*/
	
	
}