package space.hud;

import java.awt.*;
import java.util.*;
import space.Engine;
import space.color;
import space.engine.Player;
import space.qmath;
import space.engine.Enemy;
import space.engine.SmartEnemy;
import space.engine.vect;

public class PauseMenu extends Menu {

	protected final static BasicStroke STROKE = new BasicStroke();

	protected Panel panel;
	protected Panel textPanel;
	protected Text  text;
	protected Button resumeGame;
	protected Button quality;
	protected Button difficulty;
	
	public PauseMenu(){	
		this.elements = new ArrayList<IFocusable>();
		this.buttons = new ArrayList<Button>();
		
		this.panel = new Panel(50,(Engine.STAGE_HEIGHT>>2)-70,Engine.STAGE_WIDTH-100,(Engine.STAGE_HEIGHT>>1)+140);
		this.panel.color(color.PANELCOLOR);
		
		int element_x = this.panel._x+20;
		int element_width = this.panel._width-40;
		
		this.textPanel = new Panel(element_x-10, this.panel._y+10, element_width+20, 30);
		this.textPanel.color(color.PANELCOLOR);
		
		this.text = new Text("[PAUSED]", this.panel.x()+110,this.panel.y()+30);
		this.text.color(color.GREY);
		this.text.size(16);
		
		this.resumeGame = new Button(element_x,this.panel.y()+60,element_width,20, this);
		this.resumeGame.text("RESUME GAME");
		this.resumeGame.function(
            (button) -> {
                for(Button b : button.menu().buttons) b.resetColor();
                Engine.ROOT.pause(false);
            }
        );
		
		String quality_txt = "QUALITY: MEDIUM";
		if(Engine.LOW_QUALITY) quality_txt = "QUALITY: LOW";
		else if(Engine.HIGH_QUALITY) quality_txt = "QUALITY: HIGH";
		this.quality = new Button(element_x, this.panel.y()+100, element_width, 20, this);
		this.quality.text(quality_txt);
		this.quality.function(
            (button) -> {
                switch(Engine.QUALITY){
                case 0:
                    Engine.setQuality(1);
                    button.text("QUALITY: MEDIUM");
                    break;
                case 1:
                    Engine.setQuality(2);
                    button.text("QUALITY: HIGH");
                    break;
                case 2:
                    Engine.setQuality(0);
                    button.text("QUALITY: LOW");
                    break;
                }
		    }
        );

		this.difficulty = new Button(element_x, this.panel.y()+140, element_width, 20, this);
		this.difficulty.text("DIFFICULTY: MEDIUM");
		this.difficulty.function(
            (button) -> {
                switch(Engine.difficulty()){
                case 0:
                    Engine.difficulty(1);
                    button.text("DIFFICULTY: MEDIUM");
                    break;
                case 1:
                    Engine.difficulty(2);
                    button.text("DIFFICULTY: HARD");
                    break;
                case 2:
                    Engine.difficulty(0);
                    button.text("DIFFICULTY: EASY");
                    break;
                }
		    }
        );
		
		this.add(this.resumeGame);
		this.add(this.quality);
		this.add(this.difficulty);
		
		this._visible = false;
	}
	
	public void loop(){
		if(Engine.keyToggled(Engine.KEY_DOWN)){
			if(++this.focalIndex >= this.elements.size()) this.focalIndex = 0;
			this.requestFocus(this.focalIndex);
		} else if(Engine.keyToggled(Engine.KEY_UP)){
			if(--this.focalIndex < 0) this.focalIndex = this.elements.size() - 1;
			this.requestFocus(this.focalIndex);
		} else if(Engine.keyToggled(Engine.KEY_START)){
			this.focalElement.activate();
		}
			
		
		panel.loop();
		textPanel.loop();
		text.loop();
		
		for(Button b : this.buttons) b.loop();
	}
	
	public void paint(Graphics2D g){
		g.setColor(color.FADE);
		g.fillRect(0,0,Engine.STAGE_WIDTH,Engine.STAGE_HEIGHT);
		panel.paint(g);
		textPanel.paint(g);
		text.paint(g);
		
		for(Button b : this.buttons) b.paint(g);
	}
	
	public void mousePressed(){
		if(this._visible)
			for(Button b : this.buttons) b.mousePressed();
	}
	
	public void mouseClicked(){}
	
	public void mouseReleased(){}
	
	public boolean requestFocus(int index){
		if(index >= this.elements.size() || index < 0) return false;
		IFocusable e = this.elements.get(index);
		if(this.focalElement != null) this.focalElement.setFocus(false);
		this.focalElement = this.elements.get(index);
		this.focalIndex = index;
		this.focalElement.setFocus(true);
		return true;
	}
	
	public boolean requestFocus(IFocusable element){
		int size = this.elements.size();
		for(int i = 0; i < size; i++){
			if(this.elements.get(i).equals(element)){
				if(this.focalElement != null) this.focalElement.setFocus(false);
				this.focalElement = element;
				this.focalIndex = i;
				this.focalElement.setFocus(true);
				return true;
			}
		}
		return false;
	}
	
	public void visible(boolean value){
		if(!this._visible && value)
			this.requestFocus(0);
		this._visible = value;
	}
	
	
	public void __debug_buttons(){
		int button_x = this.panel._x+20;
		int button_y = this.difficulty._y;
		int button_width = this.panel._width-40;
		
		// debug mode buttons
		Button fillHP = new Button(button_x, button_y+40,button_width,20,this);
		fillHP.text("FILL HP");
		fillHP.function(
            (button) -> {
				Engine.ROOT.player.HP(Engine.ROOT.player.maxHP());
				Engine.ROOT.player.updateHPMeter();
			}
		);
		
		Button fillSpec = new Button(button_x, button_y+80,button_width,20,this);
		fillSpec.text("FILL SPECIAL");
		fillSpec.function(
            (button) ->
				Engine.ROOT.player.addSpecial(Engine.ROOT.player.maxSpecial())
		);
			
		Button lvlUp = new Button(button_x, button_y+120,button_width,20,this);
		lvlUp.text("FORCE LEVEL UP");
		lvlUp.function(
            (button) ->
				Engine.ROOT.player.addExperience(Engine.ROOT.player.experienceToNextLevel())
		);
		
		Button debugDraw = new Button(button_x, button_y+160, button_width, 20, this);
		debugDraw.text("TOGGLE DEBUG DRAW");
		debugDraw.function(
            (button) ->
				Engine.DEBUG_MODE_DRAW = !Engine.DEBUG_MODE_DRAW
        );
		
		Button enemyGen = new Button(button_x, button_y+200, button_width, 20, this);
		enemyGen.text("TOGGLE ENEMY GENERATOR");
		enemyGen.function(
            (button) -> {
				if(Engine.ROOT.currentScript.isRunning()){
					Engine.ROOT.currentScript.stop();
					Engine.ROOT.enemyGenerator.start();
				} else {
					Engine.ROOT.currentScript.start();
					Engine.ROOT.enemyGenerator.stop();
				}
		});
		
		Button spawn = new Button(button_x, button_y+240, button_width, 20, this);
		spawn.text("SPAWN ENEMY");
		spawn.function(
            (button) -> {
				Engine.ROOT.currentScript.stop();
				Engine.ROOT.enemyGenerator.stop();
				Enemy s = new SmartEnemy(Engine.ROOT.player.level(), Engine.difficulty(), new vect(qmath.random()*(Engine.STAGE_WIDTH-20)+10, -10), qmath.randomBoolean(), 0);
				Engine.ROOT.add(s);
			}
        );
		
		Button changeSpec = new Button(button_x, button_y+280, button_width, 20,this);
		changeSpec.text("CHANGE SPECIAL");
		changeSpec.function(
            (button) -> {
				int s = Engine.ROOT.player.specialAttackType();
				s = (s+1)%4;
				Engine.ROOT.player.specialAttackType(s);
	        }
        );

		this.add(fillHP);
		this.add(fillSpec);
		this.add(lvlUp);
		this.add(debugDraw);
		this.add(enemyGen);
		this.add(spawn);
		this.add(changeSpec);
	}

}