package space.engine;

import java.awt.*;
import java.awt.image.BufferedImage;
import space.*;

public class Item extends Sprite implements IBounded {
	
	public static final int HEALTH = 0;
	public static final int SPECIAL = 1;
	public static final int STATUP = 2;
	public static final int POWERUP = 3;      // limited time powerup, like mario's star or something
	public static final int HOMING = 4;       // alternate spec weapons
	public static final int BLAST = 5;
	public static final int BOMB = 6;
	public static final int BEAM = 7;
	
	protected static final int NUM_TYPES = 8;
	
	protected int _id;
	protected BufferedImage _sprite;
	protected Color _glowColor;
	protected float _glowAlpha;
	protected int _glowRadius;
	protected BoundingBox _boundingBox;
	
	public Item(){ this(qmath.randomInt()%NUM_TYPES); }
	public Item(int id){ this(new vect(), id); }
	public Item(vect position, int id){
		this._id = id;
		this._v = new vect(0,1);
		this._sprite = ItemSprites.getSprite(id);
		this._p = position;
		this._glowRadius = 0;
		this._glowAlpha = 0.5f;
		this._glowColor = color.setAlpha(ItemSprites.getColor(id),this._glowAlpha);
		this._boundingBox = new BoundingBox(this._p,new vect(24,24));
	}
	
	public BoundingBox boundingBox(){
		return this._boundingBox;
	}
	
	public int type(){ return this._id; }
	
	public void loop(){
		this._p = this._p.add(this._v);
		this._boundingBox.center(this._p);
		if(this.isOOOB())
			_root.remove(this);
		if(this._glowRadius++ > 32){
			this._glowRadius = 0;
			this._glowAlpha = 0.5f;
		} else 
			this._glowAlpha -= 0.01f;

		vect player = _root.player.position();
		vect diff = player.sub(this._p);
		if(diff.lengthSq() < 2500) {
			diff = diff.scale(0.05f);
			this._v = this._v.scale(0.95f).add(diff);
			if (this.boundingBox().hitTest(player)) {
				_root.player.pickUp(this);
				if (!Engine.LOW_QUALITY)
					_root.add(new Blast(this._p, 25, 0.15f, this._glowColor));
				_root.remove(this);
			}
		}
	}
	
	public void paint(Graphics2D g){
		int x = this._p.x(vect.ROUND);
		int y = this._p.y(vect.ROUND);
		
		int r = this._glowRadius;
		int hr = r >> 1;
		g.setColor(color.setAlpha(this._glowColor,this._glowAlpha));
		g.fillOval(x-hr, y-hr, r, r);
		
		g.drawImage(this._sprite, x-12, y-12, _root);
	}
	
	public void __on_paint(Graphics2D g){
		this._boundingBox.paint(g, false);
	}
	
	
}

class ItemSprites {
	
	static BufferedImage HEALTH = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage SPECIAL = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage STATUP = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage POWERUP = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);

	static BufferedImage HOMING = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage BLAST = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage BOMB = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	static BufferedImage BEAM = new BufferedImage(24,24,BufferedImage.TYPE_INT_ARGB);
	
	static BufferedImage[] SPRITES = {HEALTH,SPECIAL,STATUP,POWERUP,HOMING,BLAST,BOMB,BEAM};
	
	static BufferedImage getSprite(int spriteNumber){ return SPRITES[spriteNumber]; }
	static Color getColor(int spriteNumber){ return getColor(spriteNumber, 0); }
	static Color getColor(int spriteNumber, int darkness){
		if(darkness == 0) {
			switch(spriteNumber){
				case 0: return color.LRED2;
				case 1: return color.LGREEN1;
				case 2: return color.LBLUE1;
				case 3: return color.LORANGE1;
				default: return color.LPURPLE1;
			}
		} else if(darkness == 1){
			switch(spriteNumber){
				case 0: return color.MRED2;
				case 1: return color.MGREEN1;
				case 2: return color.MBLUE1;
				case 3: return color.MORANGE1;
				default: return color.MPURPLE1;
			}
		} else {
			switch(spriteNumber){
				case 0: return color.DRED2;
				case 1: return color.DGREEN1;
				case 2: return color.DBLUE1;
				case 3: return color.DORANGE1;
				default: return color.DPURPLE1;
			}
		}
	}
	
	static {
		BasicStroke stroke = new BasicStroke(2);
		
		Graphics2D g = (Graphics2D)HEALTH.getGraphics();
		Polygon poly = new Polygon(new int[]{12,24,12,0},new int[]{0,12,24,12},4);
		Font font = Engine.getFont(11);
		
		// HEALTH item
		g.setColor(getColor(0,0));
		g.fillPolygon(poly);
		g.setStroke(stroke);
		g.setColor(color.DROPSHADOW);
		g.drawPolygon(poly);
		g.drawLine(10, 14, 18, 14);
		g.drawLine(14, 10, 14, 18);
		g.setColor(color.WHITE);
		g.drawLine(8, 12, 16, 12);
		g.drawLine(12, 8, 12, 16);
		
		// SPECIAL item
		g = (Graphics2D)SPECIAL.getGraphics();
		poly = new Polygon(new int[]{23,17,19,12,5,7,1,9,12,15}, new int[]{8,14,22,18,22,14,8,8,0,8}, 10);
		g.setColor(getColor(1,2));
		g.fillPolygon(poly);
		poly = new Polygon(new int[]{23,18,19,12,5,6,1,8,12,16},new int[]{8,14,22,19,22,14,8,7,0,7},10);
		g.setColor(color.DROPSHADOW);
		g.drawPolygon(poly);
		poly = new Polygon(new int[]{22,16,18,12,6,8,2,10,12,14},new int[]{9,13,20,16,20,13,9,9,2,9},10);
		g.setColor(color.setAlpha(color.WHITE,0.35f));
		g.fillPolygon(poly);
		g.setFont(font);
		g.setColor(color.DROPSHADOW);
		g.drawString("S", 11, 18);
		g.setColor(color.WHITE);
		g.drawString("S", 9, 16);
		
		// STATUP item
		g = (Graphics2D)STATUP.getGraphics();
		g.setColor(getColor(2,0));
		g.fillOval(2, 2, 20, 20);
		g.setColor(color.DROPSHADOW);
		g.drawOval(2, 2, 20, 20);
		poly = new Polygon(new int[]{14,18,16,16,13,13,10},new int[]{8,14,14,18,18,14,14},7);
		g.fillPolygon(poly);
		g.setColor(color.WHITE);
		poly = new Polygon(new int[]{11,12,16,14,14,11,11,8},new int[]{8,7,13,13,17,17,13,13},8);
		g.fillPolygon(poly);
		/*poly = new Polygon(new int[]{12,24,16,16,8,8,0},new int[]{0,12,12,16,16,12,12},7);
		g.setColor(getColor(2,0));
		g.fillPolygon(poly);
		g.fillRect(8, 18, 8, 2);*/
		
		// POWERUP item
		g = (Graphics2D)POWERUP.getGraphics();
		g.setColor(getColor(3,0));
		poly = new Polygon(new int[]{24,18,21,15,12,9,3,6,0,7,7,12,17,17},new int[]{9,13,19,17,24,17,19,13,9,8,1,6,1,8},14);
		g.fillPolygon(poly);
		g.setColor(color.DROPSHADOW);
		g.drawPolygon(poly);
		poly = new Polygon(new int[]{17,14,16,13,12,11,8,10,7,10,10,12,14,14},new int[]{11,12,15,14,17,14,15,12,11,11,7,10,7,11},14);
		g.drawPolygon(poly);
		g.setColor(color.WHITE);
		g.fillPolygon(poly);

		// HOMING item
		g = (Graphics2D)HOMING.getGraphics();
		g.setColor(getColor(4,0));
		poly = new Polygon(new int[]{3,21,21,3},new int[]{3,3,21,21},4);
		g.fillPolygon(poly);
		g.setColor(color.DROPSHADOW);
		g.drawPolygon(poly);
		g.drawOval(7,7,12,12);
		g.drawLine(6,13,9,13);
		g.drawLine(13,6,13,9);
		g.drawLine(17,13,20,13);
		g.drawLine(13,17,13,20);
		g.setColor(color.WHITE);
		g.drawOval(6,6,12,12);
		g.drawLine(5,12,8,12);
		g.drawLine(12,5,12,8);
		g.drawLine(16,12,19,12);
		g.drawLine(12,16,12,19);

		// BLAST item
		g = (Graphics2D)BLAST.getGraphics();
		g.setColor(getColor(4,0));
		poly = new Polygon(new int[]{3,21,21,3},new int[]{3,3,21,21},4);
		g.fillPolygon(poly);
		g.setColor(color.DROPSHADOW);
		g.drawPolygon(poly);
		g.fillRect(6,12,14,4);
		g.setColor(color.WHITE);
		g.fillRect(5,10,14,4);

		// BOMB item
		g = (Graphics2D)BOMB.getGraphics();
		g.setColor(getColor(4,0));
		poly = new Polygon(new int[]{3,21,21,3},new int[]{3,3,21,21},4);
		g.fillPolygon(poly);
		g.setColor(color.DROPSHADOW);
		g.drawPolygon(poly);
		g.fillOval(10,10,8,8);
		g.setColor(color.WHITE);
		g.fillOval(8,8,8,8);

		// BEAM item
		g = (Graphics2D)BEAM.getGraphics();
		g.setColor(getColor(4,0));
		poly = new Polygon(new int[]{3,21,21,3},new int[]{3,3,21,21},4);
		g.fillPolygon(poly);
		g.setColor(color.DROPSHADOW);
		g.drawPolygon(poly);
		g.fillRect(12,6,4,14);
		g.setColor(color.WHITE);
		g.fillRect(10,5,4,14);

	}
	
	private static void __createAndPrintStar(int n, double radius1, double radius2){
		int[] points_x = new int[n*2];
		int[] points_y = new int[n*2];
		double d = (double)n;
		double theta = -Math.PI/(d*2);
		double dtheta = Math.PI/d;
		String x_str = "{";
		String y_str = "{";
		for(int i = 0; i < n; i++){
			double x = Math.cos(theta)*radius1;
			double y = Math.sin(theta)*radius1;
			int px = (int)(Math.round(x))+12;
			int py = (int)(Math.round(y))+12;
			points_x[i*2] = px;
			points_y[i*2] = py;
			x_str += px+",";
			y_str += py+",";
			theta+=dtheta;
			x = Math.cos(theta)*radius2;
			y = Math.sin(theta)*radius2;
			px = (int)(Math.round(x))+12;
			py = (int)(Math.round(y))+12;
			points_x[(i*2)+1] = px;
			points_y[(i*2)+1] = py;
			x_str += px;
			y_str += py;
			if(i != (n-1)){
				x_str += ",";
				y_str += ",";
				theta += dtheta;
			} else {
				x_str += "}";
				y_str += "}";
			}
		}
		
		Engine.trace("poly = new Polygon(new int[]"+x_str+",new int[]"+y_str+","+2*n+");");
	}
	
	
	
}