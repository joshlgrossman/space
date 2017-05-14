package space.engine;

import javax.swing.Timer;
import space.Engine;
import java.awt.event.*;

public class GameTimer extends Timer {
	
	Engine _root = Engine.ROOT;

	public GameTimer(int delay, ActionListener listener){
		super(delay,listener);
	}
	
	@Override
	protected void fireActionPerformed(ActionEvent ae){
		if(!_root.paused)
			super.fireActionPerformed(ae);
	}
	
}