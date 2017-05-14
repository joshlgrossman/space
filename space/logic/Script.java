package space.logic;

import space.*;
import space.engine.*;
import java.awt.event.*;
import java.util.*;

public class Script implements ActionListener {
	
	protected final float[] VARIABLES = new float[32];
	protected final Stack<Float> STACK = new Stack<Float>();
	protected final int ENEMY_COUNT_INDEX = 27;
	protected final int DIFFICULTY_INDEX = 28;
	protected final int DELAY_INDEX = 29;
	protected final int PC_INDEX = 30;
	protected final int RA_INDEX = 31;
	protected final HashMap<String,Integer> VARIABLE_NAMES = new HashMap<String,Integer>();
	
	protected String[] _instructions;
	protected String _currentInstruction;
	protected int _index;
	protected boolean _waiting;
	protected boolean _running;
	protected boolean _paused;
	protected boolean _synchronized;
	protected int _updateDelay;
	protected GameTimer _updateTimer;
	protected Engine _root;
	
	public Script(int updateDelay, String[] instructions){
		this._root = Engine.ROOT;
		this._updateDelay = updateDelay;
		this._updateTimer = new GameTimer(updateDelay, this);
		this._index = 0;
		this._instructions = instructions;
		this._currentInstruction = "";
		this._waiting = false;
		this._running = false;
		this._paused = false;
		this._synchronized = false;
		this.VARIABLES[DELAY_INDEX] = this._updateDelay;
		this.VARIABLES[DIFFICULTY_INDEX] = _root.difficulty;
	}
	
	public boolean isRunning(){ return this._running; }
	
	public void start(){
		this._running = true;
		this._updateTimer.start(); 
	}
	
	public void stop(){
		this._running = false;
		this._updateTimer.stop();
	}
	
	public void actionPerformed(ActionEvent ae){
		if(!this._paused){
			if(this._waiting){
				this._updateTimer.setDelay(this._updateDelay);
				this._waiting = false;
			}
			this.VARIABLES[ENEMY_COUNT_INDEX] = _root.enemies.size();
			this.nextInstruction();
		}
	}
	
	public String getCurrentInstruction(){ return this._currentInstruction; }
	
	public void nextInstruction(){
		// get next instruction
		this._currentInstruction = this._instructions[this._index++];
		if(this._index >= this._instructions.length) this._index = this._instructions.length-1;
		// parse instruction str
		String[] arr = this._currentInstruction.split(" ");
		String opcode = arr[0];
		if(opcode.charAt(0) == '$'){
			String value = evaluate(opcode);
		} else if(opcode.equals("ret")){
			this._index = (int)this.VARIABLES[RA_INDEX];
		} else if(opcode.equals("goto")){
			String value = arr[1];
			if(value.charAt(0) == '$')
				value = evaluate(value);
			int op1 = (int)Float.parseFloat(value);
			this._index = op1;
		} else if(opcode.equals("goto_if")){
			String value = arr[1];
			String index = arr[2];
			if(value.charAt(0) == '$')
				value = evaluate(value);
			if(index.charAt(0) == '$')
				index = evaluate(index);
			if(value.equals("true"))
				this._index = (int)Float.parseFloat(index);
		} else if(opcode.equals("branch")){
			String value = arr[1];
			if(value.charAt(0) == '$')
				value = evaluate(value);
			int op1 = (int)Float.parseFloat(value);
			this._index += op1;
		} else if(opcode.equals("branch_if")){
			String value = arr[1];
			String index = arr[2];
			if(value.charAt(0) == '$')
				value = evaluate(value);
			if(index.charAt(0) == '$')
				index = evaluate(index);
			if(value.equals("true"))
				this._index += (int)Float.parseFloat(index);
		} else if(opcode.equals("wait")){
			if(arr.length > 1){
				String value = arr[1];
				if(value.charAt(0) == '$')
					value = evaluate(value);
				int op1 = (int)Float.parseFloat(value);
				this._updateTimer.setDelay(op1);
				this._waiting = true;
			}
		} else if(opcode.equals("pause")){
			this._paused = true;
		} else if(opcode.equals("resume")){
			this._paused = false;
		} else if(opcode.equals("new")){
			String type = arr[1];
			ArrayList<String> args_list = new ArrayList<String>();
			int l = arr.length;
			int indx = 0;
			for(int i = 2; i<l; i++){
				String value = arr[i];
				if(value.charAt(0) == '$')
					value = evaluate(value);
				args_list.add(value);
			}
			this.createNew(type, args_list);
		} else if(opcode.equals("define")){
			int n = arr.length;
			for(int i = 1; i < n; i++){
				String value = arr[i];
				value = value.substring(1, value.length()-1);
				String[] array = value.split(",");
				if(array.length == 2){
					String key = array[1];
					int val = Integer.parseInt(array[0]);
					if(key.indexOf("=") > 0){
						String[] keyArr = key.split("=");
						key = keyArr[0];
						String init = keyArr[1];
						if(init.equalsIgnoreCase("true"))
							this.VARIABLES[val] = 1f;
						else if(init.equalsIgnoreCase("false"))
							this.VARIABLES[val] = 0f;
						else
							this.VARIABLES[val] = Float.parseFloat(keyArr[1]);
					}
					this.VARIABLE_NAMES.put(key,val);
				}
			}
		} else if(opcode.indexOf("sync") >= 0){
			this._synchronized = !this._synchronized;
		} else if(opcode.equals("}")){
			this._synchronized = false;
		}
		if(this._synchronized)
			this.nextInstruction();
		this.VARIABLES[this.PC_INDEX] = this._index+1;
	}
	
	public int getVarIndex(String var){
		switch(var){
		case "enemyCount":
			return ENEMY_COUNT_INDEX;
		case "difficulty":
			return DIFFICULTY_INDEX;
		case "delay":	// update delay
			return DELAY_INDEX;
		case "pc":		// current instruction index
			return PC_INDEX;
		case "ra":		// return address index
			return RA_INDEX;
		case "zero":	// zero
			return 0;
		default:
			if(this.VARIABLE_NAMES.containsKey(var))
				return this.VARIABLE_NAMES.get(var);
			else
				return Integer.parseInt(var);
		}
	}
	
	public float setVarIndex(int index, float value){
		this.VARIABLES[index] = value;
		if(index == this.DELAY_INDEX){
			this._updateDelay = (int)value;
			this._updateTimer.setDelay(this._updateDelay);
			return this._updateDelay;
		} else if(index == this.DIFFICULTY_INDEX){
			_root.difficulty = (int)value;
			return value;
		} else
			return value;
	}
	
	public String evaluate(String str){
		String func = str.substring(1, 5);
		String argstr = str.substring(6,str.length()-1);
		String[] args = argstr.split(",");
		int num_args;
		if(argstr.equals(""))
			num_args = 0;
		else
			num_args = args.length;
		
		String value = "0";
		switch(func){
			// RANDOM FUNCTIONS //////////////////////////////////////////////
			case "rndi":	// random integer between [arg0 - arg1]
				if(num_args == 2){
					int min = Integer.parseInt(args[0]);
					int max = Integer.parseInt(args[1]);
					int d = max-min;
					if(d < 256) value = Integer.toString(qmath.randomInt()%d+min);
					else value = Integer.toString((int)Math.round(qmath.random()*d+min));
				} else if(num_args == 3){
					int v = this.getVarIndex(args[0]);
					int min = Integer.parseInt(args[1]);
					int max = Integer.parseInt(args[2]);
					int d = max-min;
					int val;
					if(d < 256) val = (qmath.randomInt()%d+min);
					else val = ((int)Math.round(qmath.random()*d+min));
					this.VARIABLES[v] = val;
					value = Integer.toString(val);
				}
				break;
			case "rndf":	// random float between [arg0 - arg1]
				if(num_args == 2){
					double min = Double.parseDouble(args[0]);
					double max = Double.parseDouble(args[1]);
					double d = max-min;
					value = Double.toString(Math.round((qmath.random()*d+min)*1000.0)/1000.0);
				} else if(num_args == 3){
					int v = this.getVarIndex(args[0]);
					double min = Double.parseDouble(args[1]);
					double max = Double.parseDouble(args[2]);
					double d = max-min;
					float val = (float)(Math.round((qmath.random()*d+min)*1000.0)/1000.0);
					this.VARIABLES[v] = val;
					value = Float.toString(val);
				}
				break;
			case "rndb":	// random boolean w/ [true:false] ratio [arg0:arg1], if no args, assume ratio is 1:1
				if(num_args == 2){
					double a = Double.parseDouble(args[0]);
					double b = Double.parseDouble(args[1]);
					double c = a+b;
					double r = a/c;
					if(qmath.random()<=r)
						value = "true";
					else
						value = "false";
				} else if(num_args == 3){
					int v = this.getVarIndex(args[0]);
					double a = Double.parseDouble(args[1]);
					double b = Double.parseDouble(args[2]);
					double c = a+b;
					double r = a/c;
					if(qmath.random()<=r) {
						value = "true";
						this.VARIABLES[v] = 1;
					} else {
						value = "false";
						this.VARIABLES[v] = 0;
					}
				} else if(num_args == 0){
					value = qmath.randomBoolean()?"true":"false";
				} else if(num_args == 1){
					int v = this.getVarIndex(args[0]);
					boolean b = qmath.randomBoolean();
					this.VARIABLES[v] = b?1:0;
					value = b?"true":"false";
				}
				break;
			// VARIABLE FUNCTIONS /////////////////////////////////////////////
			case "seti":	// set variable value to immediate
				if(num_args == 2){
					int var = this.getVarIndex(args[0]);
					value = args[1];
					float val = Float.parseFloat(value);
					this.setVarIndex(var, val);
				}
				break;
			case "setv":	// set variable value to variable
				if(num_args == 2){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					value = Float.toString(this.setVarIndex(var1,this.VARIABLES[var2]));
				}
				break;
			case "getv":	// get variable value
				if(num_args == 1){
					int var = this.getVarIndex(args[0]);
					value = Float.toString(this.VARIABLES[var]);
				}
				break;
			case "addi":	// add immediate value to variable value and store in variable
				if(num_args == 3){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					float val = Float.parseFloat(args[2]) + this.VARIABLES[var2];
					this.setVarIndex(var1,val);
					value = Float.toString(val);
				} else if(num_args == 2){ // return sum but do not store
					int var = this.getVarIndex(args[0]);
					float val = this.VARIABLES[var] + Float.parseFloat(args[1]);
					value = Float.toString(val);
				}
				break;
			case "addv":	// add value of variable 2 to value of variable 3 and store in variable 1
				if(num_args == 3){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					int var3 = this.getVarIndex(args[2]);
					float val = this.VARIABLES[var2] + this.VARIABLES[var3];
					this.setVarIndex(var1,val);
					value = Float.toString(val);
				} else if(num_args == 2){ // return sum but do not store
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					float val = this.VARIABLES[var1] + this.VARIABLES[var2];
					value = Float.toString(val);
				}
				break;
			case "modi":	// mod immediate value and variable
				if(num_args == 3){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					float val = this.VARIABLES[var2] % Float.parseFloat(args[2]);
					this.setVarIndex(var1,val);
					value = Float.toString(val);
				} else if(num_args == 2){ // return remainder but do not store
					int var = this.getVarIndex(args[0]);
					float val = this.VARIABLES[var] % Float.parseFloat(args[1]);
					value = Float.toString(val);
				}
				break;
			case "modv":	// mod variable value and variable
				if(num_args == 3){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					int var3 = this.getVarIndex(args[2]);
					float val = this.VARIABLES[var2] % this.VARIABLES[var3];
					this.setVarIndex(var1,val);
					value = Float.toString(val);
				} else if(num_args == 2){ // return remainder but do not store
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					float val = this.VARIABLES[var1] % this.VARIABLES[var2];
					value = Float.toString(val);
				}
				break;
			case "muli":
				if(num_args == 3){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					float val = this.VARIABLES[var2] * Float.parseFloat(args[2]);
					this.setVarIndex(var1, val);
					value = Float.toString(val);
				} else if(num_args == 2){ // return product but do not store
					int var = this.getVarIndex(args[0]);
					float val = this.VARIABLES[var] * Float.parseFloat(args[1]);
					value = Float.toString(val);
				}
				break;
			case "mulv":
				if(num_args == 3){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					int var3 = this.getVarIndex(args[2]);
					float val = this.VARIABLES[var2] * this.VARIABLES[var3];
					this.setVarIndex(var1,val);
					value = Float.toString(val);
				} else if(num_args == 2){ // return product but do not store
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					float val = this.VARIABLES[var1] * this.VARIABLES[var2];
					value = Float.toString(val);
				}
				break;
			case "indx":	// set current index to variable
				if(num_args == 1){
					int var = this.getVarIndex(args[0]);
					this.setVarIndex(var,this._index);
				}
				break;
			case "prnt":
				if(Engine.DEBUG_MODE && num_args == 1){
					try {
						int var = this.getVarIndex(args[0]);
						Engine.trace(this.VARIABLES[var]);
					} catch(Exception e){
						Engine.trace(args[0]);
					}
				}
				break;
			case "vars":
				String arrstr = "{ ";
				for(int i = 0; i<this.VARIABLES.length; i++)
					arrstr += "("+i+":"+this.VARIABLES[i]+") ";
				Engine.trace(arrstr+"}");
				break;
			case "name":
				if(num_args == 2){
					int indx = this.getVarIndex(args[0]);
					String name = args[1];
					this.VARIABLE_NAMES.put(name, indx);
				}
				break;
			case "pshv": // push variable value to stack
				if(num_args == 1){
					int var = this.getVarIndex(args[0]);
					this.STACK.push(this.VARIABLES[var]);
				}
				break;
			case "pshi": // push value to stack
				if(num_args == 1){
					this.STACK.push(Float.parseFloat(args[0]));
				}
				break;
			case "popv": // pop value from stack
				if(num_args == 0){
					value = Float.toString(this.STACK.pop());
				} else if(num_args == 1){
					int var = this.getVarIndex(args[0]);
					this.setVarIndex(var, this.STACK.pop());
				}
				break;
			case "bool": // return boolean string value of variable
				if(num_args == 1){
					int var = this.getVarIndex(args[0]);
					float val = this.VARIABLES[var];
					value = (val!=0f)?("true"):("false");
				}
				break;
			case "_and": case "__&&": case "___&":
				if(num_args == 2){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					value = (this.VARIABLES[var1] != 0f && this.VARIABLES[var2] != 0f)?("true"):("false");
				} else if(num_args == 3){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					int var3 = this.getVarIndex(args[2]);
					boolean b = (this.VARIABLES[var2] != 0f && this.VARIABLES[var3] != 0f);
					this.VARIABLES[var1] = (b)?(1f):(0f);
					value = b?"true":"false";
				}
				break;
			case "__or": case "__||": case "___|":
				if(num_args == 2){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					value = (this.VARIABLES[var1] != 0f || this.VARIABLES[var2] != 0f)?("true"):("false");
				} else if(num_args == 3){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					int var3 = this.getVarIndex(args[2]);
					boolean b = (this.VARIABLES[var2] != 0f || this.VARIABLES[var3] != 0f);
					this.VARIABLES[var1] = (b)?(1f):(0f);
					value = b?"true":"false";
				}
				break;
			case "_not": case "___!":
				if(num_args == 1){
					int var1 = this.getVarIndex(args[0]);
					value = (this.VARIABLES[var1] == 0f)?("true"):("false");
				} else if(num_args == 2){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					boolean b = (this.VARIABLES[var2] == 0f);
					this.VARIABLES[var1] = (b)?(1f):(0f);
					value = b?"true":"false";
				}
				break;
			// COMPARISONS ////////////////////////////////////////////
			case "__gt":
				if(num_args == 2){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					value = (this.VARIABLES[var1] > this.VARIABLES[var2])?"true":"false";
				}
				break;
			case "_gte":
				if(num_args == 2){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					value = (this.VARIABLES[var1] >= this.VARIABLES[var2])?"true":"false";
				}
				break;
			case "__lt":
				if(num_args == 2){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					value = (this.VARIABLES[var1] < this.VARIABLES[var2])?"true":"false";
				}
				break;
			case "_lte":
				if(num_args == 2){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					value = (this.VARIABLES[var1] <= this.VARIABLES[var2])?"true":"false";
				}
				break;
			case "__eq":
				if(num_args == 2){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					value = (this.VARIABLES[var1] == this.VARIABLES[var2])?"true":"false";
				}
				break;
			case "_neq":
				if(num_args == 2){
					int var1 = this.getVarIndex(args[0]);
					int var2 = this.getVarIndex(args[1]);
					value = (this.VARIABLES[var1] != this.VARIABLES[var2])?"true":"false";
				}
				break;
		}
		return value;
	}
	
	public void createNew(String type, ArrayList<String> args){
		/*if(Engine.DEBUG_MODE){
			String arg_str = "";
			int n = args.size();
			for(int i = 0; i<n; i++){
				String stri = args.get(i);
				if(stri == null) break;
				arg_str += stri + " ";
			}
			Engine.trace("new "+type+"( "+arg_str+")");
		}*/
		switch(type){
			case "enemy": {
				String enemy_type = args.get(0);
				if(enemy_type.equals("pattern")){
					float px = Float.parseFloat(args.get(1));
					float py = Float.parseFloat(args.get(2));
					vect p = new vect(px,py);
					boolean c = args.get(3).equals("true");
					int s =(int)Float.parseFloat(args.get(4));
					int ptn = (int)Float.parseFloat(args.get(5));
					int nl = (int)Float.parseFloat(args.get(6));
					_root.add(new PatternEnemy(_root.player.level(),_root.difficulty,p,c,s,ptn,nl));
				} else if(enemy_type.equals("smart")){
					float px = Float.parseFloat(args.get(1));
					float py = Float.parseFloat(args.get(2));
					vect p = new vect(px,py);
					boolean c = args.get(3).equals("true");
					int s = (int)Float.parseFloat(args.get(4));
					_root.add(new SmartEnemy(_root.player.level(),_root.difficulty,p,c,s));
				} else if(enemy_type.equals("swarm")){
					float px = Float.parseFloat(args.get(1));
					float py = Float.parseFloat(args.get(2));
					vect p = new vect(px,py);
					boolean c = args.get(3).equals("true");
					int cols = (int)Float.parseFloat(args.get(4));
					int rows = (int)Float.parseFloat(args.get(5));
					int b = (int)Float.parseFloat(args.get(6));
					_root.add(new Swarm(_root.player.level(),_root.difficulty,p,c,cols,rows,b));
				} else if(enemy_type.equals("turret")){
					float px = Float.parseFloat(args.get(1));
					float py = Float.parseFloat(args.get(2));
					vect p = new vect(px,py);
					boolean c = args.get(3).equals("true");
					_root.add(new Turret(_root.player.level(),_root.difficulty,p,c));
				} else if(enemy_type.equals("boss")){
					int s = (int)Float.parseFloat(args.get(1));
					_root.add(new Boss(_root.player.level(),_root.difficulty,s));
				}
			} break;
			
			case "item": {
				float x = Float.parseFloat(args.get(0));
				float y = Float.parseFloat(args.get(1));
				int id = (int)Float.parseFloat(args.get(2));
				_root.add(new Item(new vect(x,y), id));
			} break;
			
			case "wall": {
				float tlx = Float.parseFloat(args.get(0));
				float tly = Float.parseFloat(args.get(1));
				float brx = Float.parseFloat(args.get(2));
				float bry = Float.parseFloat(args.get(3));
				float vx = Float.parseFloat(args.get(4));
				float vy = Float.parseFloat(args.get(5));
				_root.add(new WallObstacle(new vect(tlx,tly),new vect(brx,bry),new vect(vx,vy)));
			} break;
			
			case "walls": {
				float w = Float.parseFloat(args.get(0));
				float h = Float.parseFloat(args.get(1));
				float vx = Float.parseFloat(args.get(2));
				float vy = Float.parseFloat(args.get(3));
				_root.add(new WallObstacle(new vect(0f,-h),new vect(w,0),new vect(vx,vy)));
				_root.add(new WallObstacle(new vect(Engine.STAGE_WIDTH-w-7,-h),new vect(Engine.STAGE_WIDTH-7,0),new vect(vx,vy)));
			} break;
			
			case "fence": { // vect[] pts, vect vel, int behavior
				int n = args.size() - 3;
				vect[] p = new vect[n];
				for(int i = 0; i < n; i++){
					String str = args.get(i);
					str = str.substring(1,str.length()-1);
					String[] arr = str.split(",");
					float x = Float.parseFloat(arr[0]);
					float y = Float.parseFloat(arr[1]);
					p[i] = new vect(x,y);
				}
				float vx = Float.parseFloat(args.get(n));
				float vy = Float.parseFloat(args.get(n+1));
				String bstr = args.get(n+2);
				int b;
				if(bstr.indexOf(".") >= 0)
					b = (int)Float.parseFloat(bstr);
				else	
					b = Integer.decode(args.get(n+2)); // accepts decimal or hex
				_root.add(new ElectricObstacle(p,new vect(vx,vy),b));
			} break;
		}
	}
	
}