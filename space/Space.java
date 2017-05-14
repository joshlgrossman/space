package space;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.JFrame;
import space.logic.*;
import space.engine.*;
import java.util.function.*;

public class Space extends JFrame implements WindowListener, MouseListener, MouseMotionListener {
	
	public static final String[] DEMO_SCRIPT = new String[]{
		"define (1,counter) (2,temp) (3,size)",
		"$seti[4,2]",
		"$seti[counter,0]",
		"$setv[ra,pc]",
		"new walls 40 700 0 1",
		"new enemy pattern $rndi[10,400] -10 $rndb[1,1] 1 $rndi[4,18] $rndi[-1,8]",
		"$addi[counter,counter,1]",
		"$modi[temp,counter,4]",
		"branch_if $__eq[temp,0] 4",
		"branch_if $_neq[temp,4] 1",
		"new item $rndf[100,300] -10 $rndi[0,2]",
		"wait 4000",
		"ret",
		"$rndi[size,2,5]",
		"new enemy swarm $rndf[150,350] $muli[size,-8] $rndb[1,1] $rndi[3,7] $getv[size] 3 0",
		"ret"
	};

	public static final String[] DEMO_BOSS = new String[]{
		"FULL_HP {",
		"MOVEMENT, CIRCLE",
		"ATTACK, BEAM",
		"}",
		"VERY_HIGH_HP {",
		"MOVEMENT, BACK_AND_FORTH",
		"ATTACK, SWEEP",
		"}"
	};
	
	public static String[][] SCRIPTS;
	public static boolean SCRIPTS_LOADED = false;
	private static int CURRENT_SCRIPT;
	private static int NUM_SCRIPTS;
	
	public static void main(String[] args){
		
		Engine.trace("Enable hardware acceleration:");
		    // System.setProperty("sun.java2d.trace", "timestamp,log,count");
			//System.setProperty("sun.java2d.opengl", "true");
		System.setProperty("sun.java2d.accthreshold","0");
		System.setProperty("sun.java2d.transaccel", "True");
		System.setProperty("sun.java2d.d3d", "True");
		System.setProperty("sun.java2d.ddforcevram", "True");
		System.setProperty("sun.java2d.ddscale","true");
		
		Engine.trace("Loading scripts:");
		try { 
			SCRIPTS = loadScripts("bin/scripts"); // "src/scripts"
			NUM_SCRIPTS = SCRIPTS.length;
			SCRIPTS_LOADED = true;
			Engine.trace("load completed successfully");
		} catch(IOException e){
			SCRIPTS = new String[][]{DEMO_SCRIPT};
			NUM_SCRIPTS = 1;
			SCRIPTS_LOADED = false;
			Engine.trace("load failed, using demo script ("+e.toString()+")");
		} finally {
			CURRENT_SCRIPT = 0;
		}
		Engine root = new Engine();
		Space stage = new Space();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		stage.setAutoRequestFocus(true);
		stage.setSize(Engine.STAGE_WIDTH,Engine.STAGE_HEIGHT);
		stage.setLocation(screenSize.width/2-Engine.STAGE_WIDTH/2,screenSize.height/2-Engine.STAGE_HEIGHT/2 - 40);
        stage.setResizable(false);
		stage.setVisible(true);
		stage.add(root);
		stage.addWindowListener(stage);
		root.addMouseMotionListener(stage);
		root.addMouseListener(stage);
		stage.addKeyListener(root);
		stage.requestFocus();
		stage.addKeyListener(Engine.ROOT);
		
		Engine.ROOT = root;
		Engine.STAGE = stage;
		
		root.initialize();
		
		int delay = 17;
		long cycle = System.currentTimeMillis();
		
		while(true){
			root.loop();
			root.repaint();
			cycle += delay;
			long difference = cycle - System.currentTimeMillis();
			stage.requestFocus();
			try {
				//Thread.sleep(16, 666666); // 60fps
				Thread.sleep(Math.max(difference,0));
			} catch(Exception e){
				System.exit(0);
				break;
			}
		}
	}
	
	public static boolean hasNextScript(){ return (CURRENT_SCRIPT < (NUM_SCRIPTS-1)); }
	public static String[] nextScript(){ return SCRIPTS[CURRENT_SCRIPT++]; }
	
	public static String[][] loadScripts(String location) throws IOException {
		File config = new File(location + "/config.txt");
		Scanner scanner = new Scanner(config);
		
		ArrayList<String> fileNames = new ArrayList<>(1);

		int numFiles = 0;

		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			fileNames.add(line);
			numFiles++;
		}

		String[][] scripts = new String[numFiles][];
		for(int index = 0; index < numFiles; index++){
			ArrayList<String> script = new ArrayList<String>(1);
			File file = new File(location+"/"+fileNames.get(index));
			Scanner fileScanner = new Scanner(file);
			
			int numLines = 0;
			while(fileScanner.hasNextLine()){
				String nextLine = fileScanner.nextLine().trim();
				if(nextLine.length() > 0 && nextLine.charAt(0) != '#'){
					script.add(nextLine);
					numLines++;
				}
			}
			
			scripts[index] = new String[numLines];
			script.toArray(scripts[index]);
		}
	
		return scripts;
	}
	
	public void windowClosing(WindowEvent e){ Engine.ROOT.exit(); }
	
	public void windowActivated(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowOpened(WindowEvent e){}
	
	public void mouseDragged(MouseEvent me){
		Engine.ROOT.mouse = new vect(me.getX(),me.getY());
	}
	
	public void mouseMoved(MouseEvent me){
		Engine.ROOT.mouse = new vect(me.getX(),me.getY());
	}
	
	public void mousePressed(MouseEvent me){
		Space stage = Engine.STAGE;
		stage.requestFocus();
		stage.addKeyListener(Engine.ROOT);
		Engine.clearInput();
		Engine.ROOT.mousePressed();
	}
	

	public void mouseReleased(MouseEvent me){
		Engine.ROOT.mouseReleased();
	}
	
	public void mouseClicked(MouseEvent me){
		Engine.ROOT.mouseClicked();
	}
	
	public void mouseEntered(MouseEvent me){}
	public void mouseExited(MouseEvent me){}
	
}