package space;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Canvas;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;
import space.engine.*;
import space.hud.*;
import space.logic.*;
import java.util.function.*;

public class Engine extends Canvas implements KeyListener, IMouseClickListener {
	
	
	///////////////////  DEBUG STUFF //////////////////////////////
	// Debug stuff
	public static final boolean DEBUG_MODE = true;
	public static boolean DEBUG_MODE_DRAW = false;
	public static final boolean DEBUG_DEMO = false;
	public ActionListener debug_listener = new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			float ms = (float)(debug_c_ms - debug_p_ms);
			debug_fps = 1000f/ms;
			debug_fps = Math.round(debug_fps*10f)/10f;
		}
	};
	public Timer debug_timer;
	public long debug_p_ms = 0;
	public long debug_c_ms = 0;
	public float debug_fps = 0;
	public MultilineText debug_text = new MultilineText(10,16);
	public Enemy debug_enemy;
	//public Electricity elec;
	// Debug mode functions
	public void __on_init(){
		debug_timer = new Timer(1000,debug_listener);
		debug_timer.start();
		player.addSpecial(player.maxSpecial());
		//this.add(new Item(new vect(200,0),0));
		//debug_enemy = this.add(new PatternEnemy(50,3,new vect(200f,0f),BLACK_VAL, 3,PatternEnemy.CIRCLE|PatternEnemy.LOOP,-1));
		//this.add(new SmartEnemy(2,3,new vect(100f,0f),WHITE_VAL,4));
		//String[] instructions = {"new enemy $rndi[10,400] -10 $rndb[1,2] 1 $rndi[4,18] $rndi[-1,8]","new wall 0 -1300 50 10 0 2","wait","new wall 50 -200 250 -180 0 2", "wait", "new wall 350 -100 400 -50 0 2", "goto 0"};
		//this.add(new Swarm(2,3, new vect(200f,0f), false, 10,10,2));
		//this.add(new Turret(50,3,new vect(200f,0f),true));
		currentScript = new Script(1000,(DEBUG_DEMO)?(Space.DEMO_SCRIPT):(Space.nextScript()));
		currentScript.start();	
		//elec = new Electricity(new vect(100,100),new vect(200,150),1f,Electricity.ENEMY);
		//this.add(elec);
		//vect[] pos = {new vect(50,0), new vect(50,-200), new vect(350,-200), new vect(350,0)};
		//ElectricObstacle eo = new ElectricObstacle(pos, new vect(0,1));
		//this.add(eo);
		pauseMenu.__debug_buttons();
		//currentScript.stop();
		//Boss b = new Boss(1,1,0);
		//add(b);
	}
	
	
	public void __on_pause(boolean p){
		/*if(!p){
			if(debug_enemy.HP() <= 0)
				debug_enemy = this.add(new PatternEnemy(50,3,new vect(200f,0f),WHITE_VAL, 2,PatternEnemy.CIRCLE|PatternEnemy.LOOP,-1));
		}
		if(!p){
			Engine.DEBUG_MODE_DRAW = !Engine.DEBUG_MODE_DRAW;
			player.addSpecial(player.maxSpecial());
		}*/
	}
	public void __on_loop(){
		//elec.end(new vect(200,200));
		//elec.start(new vect(20,10f));
		String[] txt = new String[10];
		String inst = currentScript.getCurrentInstruction();
		if(inst.length() > 28)
			inst = inst.substring(0,25)+"...";
		//double en = debug_enemy.shield().energy();
		//double hp = debug_enemy.HP();
		//en = Math.round(en*100)/100;
		//hp = Math.round(hp*100)/100;
		txt[0] = "::::[debug_mode]::::::::::::::::";
		txt[1] = "::- framerate = "+debug_fps+" fps";
		txt[2] = "::- num_sprts = "+sprites.length();
		//txt[3] = "::- enemy_hp  = "+hp;
		//txt[4] = "::- enemy_sp  = "+en;
		txt[3] = "::- "+inst;
		//txt[4] = "::- "+elec.hitTest(mouse);
		txt[4] = "::- "+mouse;
		txt[5] = "::- gen_rning = "+enemyGenerator.isRunning();
		txt[6] = "::- mx_enmies = "+enemyGenerator.maxEnemyFunction(player.level(), difficulty);
		txt[7] = "::::::::::::::::::::::::::::::::";
		debug_text.text(txt);
		debug_p_ms = debug_c_ms;
		debug_c_ms = System.currentTimeMillis();
	}
	public void __on_paint(Graphics2D g){
		debug_text.paint(g);
	}
	///////////////// END DEBUG STUFF //////////////////////////////
	
	
	// Graphics stuff
	private BufferStrategy _bufferStrategy;
	private Color _clearColor = new Color(0.0f,0.0f,0.0f,0.85f);
	public float getTrails(){ return _clearColor.getAlpha(); }
	public void setTrails(float alpha){ this._clearColor = new Color(0f,0f,0f,alpha); }
	
	// Game stuff
	public static Space STAGE;
	public static Engine ROOT;
	
	public final static int STAGE_WIDTH = 400;
	public final static int STAGE_HEIGHT = 640;//720;
	
	public static int QUALITY = 1; // 2 = high, 1 = default, 0 = low
	public final static int LOW = 0;
	public final static int DEFAULT = 1;
	public final static int HIGH = 2;
	public static boolean LOW_QUALITY = (QUALITY == 0);
	public static boolean DEFAULT_QUALITY = (QUALITY == 1);
	public static boolean HIGH_QUALITY = (QUALITY == 2);
	public static void setQuality(int value){
		QUALITY = value;
		if(LOW_QUALITY = (QUALITY == 0)) ROOT._clearColor = new Color(0f,0f,0f,1f);
		else ROOT._clearColor = new Color(0f,0f,0f,0.85f);
		DEFAULT_QUALITY = (QUALITY == 1);
		HIGH_QUALITY = (QUALITY == 2);
	}
	
	public final static vect TOP_LEFT = new vect(0.0f,0.0f);
	public final static vect OUTER_TOP_LEFT = new vect(-16f, -16f);
	public final static vect BOTTOM_RIGHT = new vect(STAGE_WIDTH,STAGE_HEIGHT);
	public final static vect OUTER_BOTTOM_RIGHT = new vect(STAGE_WIDTH+16f,STAGE_HEIGHT+16f);
	
	public final static boolean[] KEY_PRESSED = new boolean[16];
	public static boolean[] KEY_TOGGLED = new boolean[16];
	public final static int[] KEY_VALUE = {KeyEvent.VK_A,KeyEvent.VK_W,KeyEvent.VK_D,KeyEvent.VK_S,KeyEvent.VK_L,KeyEvent.VK_K,KeyEvent.VK_J,KeyEvent.VK_P,KeyEvent.VK_ENTER};
	public final static byte KEY_LEFT = 0;
	public final static byte KEY_UP = 1;
	public final static byte KEY_RIGHT = 2;
	public final static byte KEY_DOWN = 3;
	public final static byte KEY_A = 4;
	public final static byte KEY_B = 5;
	public final static byte KEY_X = 6;
	public final static byte KEY_Y = 7;
	public final static byte KEY_START = 8;
	
	public final static boolean WHITE_VAL = true;
	public final static boolean BLACK_VAL = false;
	public final static Color BLACK = color.LPURPLE1;
	public final static Color WHITE = color.MGREEN1;
	
	public final static ConvolveOp BLUR_FILTER = new ConvolveOp(new Kernel(3,3,new float[]{
																		0.0625f, 0.0625f, 0.0625f,
																		0.0625f, 0.5f,	  0.0625f,
																		0.0625f, 0.0625f, 0.0625f
																}));
	
	// Game convenience functions
	public static Color getColor(boolean value){ return value?WHITE:BLACK; }
	public static Font getFont(int size){ return new Font(Font.MONOSPACED,Font.PLAIN,size); }
	public static Font getFont(int size, int flags){ return new Font(Font.MONOSPACED,flags,size); }
	 
	//0<x<=25 y = round(3*sqrt(x)+2)
	//25<x<=75 y = round(0.025*(x-25)^2+17)
	//75<x<=100 y = round(3*ln(x-74)+80)
	public static int experienceFunction(int lvl){
		double x = (double)lvl;
		if(lvl <= 25) return (int)Math.round(3*Math.sqrt(x)+2.0);
		else if(lvl <= 75) return (int)Math.round(0.025*Math.pow(x-25.0,2.0)+17.0);
		else return (int)Math.round(3*Math.log(x-74.0)+80.0);
	}
	
	public static int difficulty(){ return ROOT.difficulty; }
	public static void difficulty(int value){ ROOT.difficulty = value; }
	
	
	// Engine stuff
	//public LinkedList<Sprite> sprites;
	public Meter hpMeter;
	public Meter expMeter;
	public Meter specialMeter;
	public Text levelText;
	public Text specialText;
	public PauseMenu pauseMenu;
	public Panel bottomPanel;
	public Player player;
	public StarField background;
	public EnemyGenerator enemyGenerator;
	public ArrayList<Enemy> enemies;
	public ArrayList<Obstacle> obstacles;
	public ArrayList<IMouseClickListener> mouseListeners;
	public SpriteList sprites;
	public SpriteList hud;
	public Script currentScript;
	public int difficulty;
	public boolean paused;
	public vect mouse;
	
	// Constructor
	public Engine(){
		this.bottomPanel = new Panel(0,STAGE_HEIGHT-88,STAGE_WIDTH,88);
		this.pauseMenu = new PauseMenu();
		this.hpMeter =		 new Meter("[  H P  ]",8,STAGE_HEIGHT-72);
		this.specialMeter =	 new Meter("[SPECIAL]",8,STAGE_HEIGHT-56);
		this.expMeter =		 new Meter("[ E X P ]",8,STAGE_HEIGHT-40,false);
		this.levelText =	 new Text("LVL. 1", 84, this.expMeter.y());
		this.specialText =	 new Text("",84,this.specialMeter.y());
		this.sprites = new SpriteList();
		this.hud = new SpriteList();
		this.enemies = new ArrayList<Enemy>();
		this.obstacles = new ArrayList<Obstacle>();
		this.mouseListeners = new ArrayList<IMouseClickListener>();
		this.mouse = new vect();
	}
	
	// Game Routines /////////////////////////////
	public void initialize(){
		// setup buffers
		this.createBufferStrategy(2);
		this._bufferStrategy = this.getBufferStrategy();
		
		
		this.add(this.background = new StarField());
		// add player sprite
		this.add(this.player = new Player());
		this.player.x(STAGE_WIDTH/2f);
		this.player.y(STAGE_HEIGHT*0.75f);
		
		this.mouseListeners.add(this.pauseMenu);
		
		// setup meters
		this.bottomPanel.color(color.PANELCOLOR);
		this.levelText.color(color.setAlpha(color.brighten(color.LBLUE1,0.6f),0.9f));
		this.levelText.style(Font.BOLD);
		this.levelText.size(11);
		this.specialText.color(color.setAlpha(color.brighten(color.LGREEN1,0.6f),0.9f));
		this.specialText.style(Font.BOLD);
		this.specialText.size(11);
		this.hpMeter.setColors(color.setAlpha(color.MRED1, 0.75f),color.setAlpha(color.MRED2, 0.2f),color.setAlpha(color.LRED1, 1f));
		this.expMeter.setColors(color.setAlpha(color.MBLUE1, 0.75f),color.setAlpha(color.MBLUE2, 0.2f),color.setAlpha(color.LBLUE1, 1f));
		this.specialMeter.setColors(color.setAlpha(color.MGREEN1, 0.75f),color.setAlpha(color.MGREEN2, 0.2f),color.setAlpha(color.LGREEN1, 1f));
		
		this.hpMeter.fill();
		
		this.paused = false;
		
		this.difficulty = 1;
		
		this.enemyGenerator = new EnemyGenerator(this.difficulty);
		
		if(DEBUG_MODE) this.__on_init();
	}
	
	
	public void loop(){
		//if(Engine.keyPressed(KEY_B)) Engine.trace("# Sprites:",sprites.length(),"\t# Listeners:",keyListeners.length());
		boolean start_pressed = KEY_PRESSED[KEY_START];
		boolean start_toggled = start_pressed && !KEY_TOGGLED[KEY_START];
		if(!paused){
			if(start_toggled) 
				this.pause(true);
			else {
				sprites.loop();
				sprites.checkCollisions();
				hpMeter.loop();
				expMeter.loop();
				specialMeter.loop();
				hud.loop();
			}
		} else 
			pauseMenu.loop();
		System.arraycopy(KEY_PRESSED, 0, KEY_TOGGLED, 0, 16);
		if(DEBUG_MODE) __on_loop();
	}
	
	public void pause(final boolean isPaused){
		if(DEBUG_MODE) this.__on_pause(isPaused);
		this.paused = isPaused;
		this.pauseMenu.visible(isPaused);
	}
	
	public void exit(){
		STAGE.remove(this);
		STAGE.setVisible(false);
		STAGE.dispose();
		System.exit(0);
	}
	
	// Game Functions ////////////////////////////
	public Sprite add(Sprite sprite){
		this.sprites.push(sprite);
		sprite.onDisplay();
		return sprite;
	}
	
	public Enemy add(Enemy enemy){
		this.enemies.add(enemy);
		this.sprites.push(enemy);
		enemy.onDisplay();
		return enemy;
	}
	
	public Obstacle add(Obstacle obstacle){
		this.obstacles.add(obstacle);
		this.sprites.push(obstacle);
		return obstacle;
	}
	
	public EffectGenerator add(EffectGenerator effect){
		effect.onDisplay();
		return effect;
	}
	
	public Sprite addHUD(Sprite sprite){
		this.hud.push(sprite);
		sprite.onDisplay();
		return sprite;
	}
	
	public Sprite removeHUD(Sprite sprite){
		sprite.onRemove();
		this.hud.remove(sprite);
		return sprite;
	}
	
	public Sprite remove(Sprite sprite){
		sprite.onRemove();
		boolean success = this.sprites.remove(sprite);
		return success?sprite:null;
	}
	
	public Enemy remove(Enemy enemy){
		enemy.onRemove();
		boolean success = this.sprites.remove(enemy) && this.enemies.remove(enemy);
		return success?enemy:null;
	}
	
	public Obstacle remove(Obstacle obstacle){
		obstacle.onRemove();
		boolean success = this.sprites.remove(obstacle) && this.obstacles.remove(obstacle);
		return success?obstacle:null;
	}
	
	public EffectGenerator remove(EffectGenerator effect){ return effect; }  // can't remove what wasn't added to begin with, function exists for symmetry
	
	public boolean contains(Sprite sprite){
		return this.sprites.indexOf(sprite) >= 0;
	}
		
	public static void clearInput(){ for(int i = KEY_PRESSED.length - 1; i >= 0; i--) KEY_PRESSED[i] = false; }
	public static boolean keyPressed(byte keyCode){ return KEY_PRESSED[keyCode]; }
	public static boolean keyToggled(byte keyCode){ return KEY_PRESSED[keyCode] && !KEY_TOGGLED[keyCode]; }
	// Graphics Functions ////////////////////////
	
	public void paint(Graphics g){
		if(this._bufferStrategy != null){
			do {
				BufferedImage bufferedImage = new BufferedImage(STAGE_WIDTH,STAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
				Graphics2D bufferGraphics;
				if(this.paused)
					bufferGraphics = (Graphics2D)bufferedImage.getGraphics();
				else
					bufferGraphics = (Graphics2D)this._bufferStrategy.getDrawGraphics();
				// Clear screen
				bufferGraphics.setColor(this._clearColor);
				bufferGraphics.fillRect(0, 0, STAGE_WIDTH, STAGE_HEIGHT);
				// Turn on anti-aliasing
				if(!Engine.LOW_QUALITY){
					bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					bufferGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
					bufferGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				}
				// Draw sprites
				sprites.paint(bufferGraphics);
				
				
				bottomPanel.paint(bufferGraphics);
				hpMeter.paint(bufferGraphics);
				expMeter.paint(bufferGraphics);
				specialMeter.paint(bufferGraphics);
				
				levelText.paint(bufferGraphics);
				specialText.paint(bufferGraphics);
				
				hud.paint(bufferGraphics);
				
				if(DEBUG_MODE_DRAW && DEBUG_MODE){
					sprites.__on_paint(bufferGraphics);
					__on_paint(bufferGraphics);
				}
				
				if(this.paused){
					bufferedImage = BLUR_FILTER.filter(bufferedImage, null);
					pauseMenu.paint((Graphics2D)bufferedImage.getGraphics());
					this._bufferStrategy.getDrawGraphics().drawImage(bufferedImage, 0, 0, ROOT);
				}
				
				bufferGraphics.dispose();
				// Draw buffer to screen
				this._bufferStrategy.show();
			} while(this._bufferStrategy.contentsRestored());
			
			Toolkit.getDefaultToolkit().sync();
		}
	}
	
	public void update(Graphics g){ this.paint(g); }

	// Listener Functions ///////////////////////
	public void keyPressed(KeyEvent ke){
		int index;
		for(index = 0; index < KEY_VALUE.length; index++)
			if(ke.getKeyCode() == KEY_VALUE[index]) break;
		KEY_PRESSED[index] = true;
	}
	
	public void keyReleased(KeyEvent ke){
		int index;
		for(index = 0; index < KEY_VALUE.length; index++)
			if(ke.getKeyCode() == KEY_VALUE[index]) break;
		KEY_PRESSED[index] = false;
	}
	
	public void keyTyped(KeyEvent ke){}
	
	public void mousePressed(){
		for(IMouseClickListener listener : this.mouseListeners)
			listener.mousePressed();
	}
	
	public void mouseReleased(){
		for(IMouseClickListener listener : this.mouseListeners)
			listener.mouseReleased();
	}
	
	public void mouseClicked(){
		for(IMouseClickListener listener : this.mouseListeners)
			listener.mouseClicked();
	}
	
	// trace function for debug output
	public static void trace(Object... output){
		if(DEBUG_MODE){
			String str = "";
			for(Object obj : output)
				str += obj.toString() + " ";
			System.out.println(str);
		}
	}

}

/*
private void createBuffer(){
		GraphicsConfiguration gc = this.getGraphicsConfiguration();
		this._bufferImage = gc.createCompatibleVolatileImage(STAGE_WIDTH, STAGE_HEIGHT);
	}
 
 
 
 public synchronized void paint(Graphics g){
if(this._bufferStrategy != null){
	Image previousFrame = this._bufferImage;
	this.createBuffer();
	do {
		do {
			GraphicsConfiguration gc = this.getGraphicsConfiguration();
			int flag = this._bufferImage.validate(gc);
			if(flag == VolatileImage.IMAGE_INCOMPATIBLE)
				this.createBuffer();
			Graphics2D bufferGraphics = (Graphics2D)this._bufferImage.getGraphics();
			bufferGraphics.drawImage(previousFrame,0,0,Color.BLACK,this);
			// Clear screen
			//Color c = new Color(0f,0f,0f,0.9f);
			bufferGraphics.setColor(new Color(0.0f,0.0f,0.0f,1f));
			bufferGraphics.fillRect(0, 0, STAGE_WIDTH, STAGE_HEIGHT);
			// Turn on anti-aliasing
			bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			bufferGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			bufferGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			// Draw sprites
			sprites.paint(bufferGraphics);
			bufferGraphics.dispose();
		} while(this._bufferImage.contentsLost());
		
		// Draw buffer to screen
		Graphics vsyncGraphics = this._bufferStrategy.getDrawGraphics();	
		vsyncGraphics.drawImage(this._bufferImage,0,0,this);
		vsyncGraphics.dispose();
		this._bufferStrategy.show();
		
	} while(this._bufferStrategy.contentsRestored());
	
	Toolkit.getDefaultToolkit().sync();
}
}*/

/*// EXP needed to level up @ current level
// lvl 1 exp = 100, lvl 100 exp = 10000
// floor(11900*ln(1+x/50) - 0.3*x*x -135+62*(x)/100)
public static int playerExperienceFunction(int lvl){
	double x = (double)lvl;
	int xp = (int)(11900*Math.log(1.0+x/50.0) - 0.3*x*x + 0.62*x - 135);
	return xp;
}

// EXP gained for defeating an enemy @ specified level
// current formula: playerXP/(lvl + 2)
// previous formulas: 
//     floor((200*ln(1+x/25) + 27)* (100-x)/100 + 6.25*x)
//     playerXPFunc(x)/round(10*ln(x)*(0.25+0.75*x/100)+3+x/100)
public static int enemyExperienceFunction(int lvl){
	double x = (double)lvl;
	double ttl = (double)(playerExperienceFunction(lvl));
	int xp = (int)(Math.round(ttl/(lvl+2)));
	return xp;
}*/