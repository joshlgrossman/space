package space.logic;

import java.util.ArrayList;
import java.awt.event.*;
import space.engine.*;
import space.*;

public class EnemyGenerator implements ActionListener {
	
	protected Engine _root;
	protected Player _player;
	protected int _difficulty;
	protected boolean _running;
	protected GameTimer _timer;
	
	protected float _aggro;
	
	public EnemyGenerator(int difficulty){
		this._root = Engine.ROOT;
		this._player = Engine.ROOT.player;
		this._difficulty = difficulty;
		this._running = false;
		this._timer = new GameTimer(750,this);
		this._aggro = 0;
	}
	
	public void actionPerformed(ActionEvent ae){
		if(this._running && !_root.paused)
			this.generateEnemy();
	}
	
	public void generateEnemy(){
		int difficulty = this._difficulty = this._root.difficulty;
		int lvl = this._player.level();
		int num_enemies = this._root.enemies.size();
		int max_enemies = maxEnemyFunction(lvl,difficulty);
		if(num_enemies < max_enemies){
			int rand = qmath.randomInt();
			int freq = difficultyFunction(lvl, difficulty);
			if(rand < freq){
				int typeDiff = freq/10;
				int diff = Math.abs(freq - rand);
				vect pos = new vect(qmath.random()*(Engine.STAGE_WIDTH-20)+10, -10);
				if(diff < typeDiff){			// rare enemy type 1
					pos = pos.add(new vect(Engine.STAGE_WIDTH/2f, 10)).scale(0.5f);
					int columns = qmath.randomInt()%6+3;
					int rows = qmath.randomInt()%4+2;
					pos.y(rows*-8);
					Swarm s = new Swarm(lvl, difficulty, pos, qmath.randomBoolean(), columns, rows, qmath.randomInt()%5);
					this._root.add(s);
				} else if(diff < typeDiff*2){ 	// rare enemy type 2
					Enemy s = new SmartEnemy(lvl, difficulty, pos, qmath.randomBoolean(), 0);
					this._root.add(s);
				} else {						// common enemy type 3
					int pattern = ((qmath.randomInt()%4)+1)*4;
					if(qmath.randomBoolean()) pattern += 2;
					Enemy e = new PatternEnemy(lvl, difficulty, pos, qmath.randomBoolean(), pattern);
					this._root.add(e);
				}
			}
		}
	}
	
	public void start(){
		this._running = true;
		this._timer.start();
	}
	
	public void stop(){
		this._running = false;
		this._timer.stop();
	}
	
	public boolean isRunning(){ return this._running; }
	
	// return integer [0-255] used for enemy spawning frequency based on current level and difficulty
	public int difficultyFunction(int lvl, int difficulty){
		double x = (double)lvl;
		switch(difficulty){
			case 0:		return (int)Math.round(75.0+105.0*Math.log10(1.0+(x/50.0))); 	// easy		50		(75 - 125)
			case 1: 	return (int)Math.round(100.0+0.75*x);							// medium	75		(100 - 175)
			case 2: 
			default:	return (int)Math.round(150.0+Math.pow(x/150.0,2.0)+x);			// hard		100		(150 - 250)
		}
	}
	
	// return integer representing how many enemies can be on screen at once based on current level and difficulty
	public int maxEnemyFunction(int lvl, int difficulty){
		double x = (double)lvl;
		switch(difficulty){
		case 0:		return (int)Math.round(3.0+73.0*Math.log10(1.0+x/100.0)); 			// easy		(3 - 25)
		case 1: 	return (int)Math.round(5.0+x/3.3);									// medium	(5 - 35)
		case 2: 
		default:	return (int)Math.round(7.0+Math.pow(x/23.5, 2)+x/4.0);				// hard		(7 - 50)
	}
	}
	
}