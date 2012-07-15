package ERS.States;

import static ERS.Game.canvas;

import java.awt.Color;
import java.util.ArrayList;

import ERS.Game;
import ERS.Objects.GameState;
import ERS.SFX.TextFX;

public class Pause extends State{
	public int selected = 0;
	public int cardYOff = 0;
	public double yD = 1;
	public int max = 2;

	public int yCount = 0, xCount = 0;
	
	public static ArrayList<Option> options = new ArrayList<Option>();
	
	public Pause(){
		state = GameState.PAUSE;
		
		options.add(new Option("Pause Menu", 10, true));
		
		options.add(new Option("Settings (S)", false));
		options.add(new Option("Toggle Mute (M)", false));
		options.add(new Option("Change Players (A)", false));
		options.add(new Option("Change Level (L)", false));
		options.add(new Option("Back To Game. (ESC)", false));
		options.add(new Option("Back To Menu. (E)", false));
	}
	
	public void tick(){
		
	}
	
	public void render(){
		State.inGame.render();
		
		Game.canvas.setColor(new Color(0, 0, 0, 200));
		Game.canvas.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		for(Option o: options){
			o.render();
		}
	}

	public Option getID(int i){
		for(Option o: options){
			if(o.id == i)
				return o;
		}
		
		return null;
	}
	
	public void select(){
	}
	
	public static int getNextID(){
		int count = 0;
		
		for(Option o: options){
			if(o.id != -1)
				count++;
		}
		
		return count;
	}
	
	public class Option{
		int x, y, id;
		String name;
		boolean isLabel = true;
		
		public Option(String n, int x, int y, boolean l){
			name = n;
			this.id = getNextID();
			
			if(l)
				id = -1;
			
			isLabel = l;
			
			this.x = x;
			this.y = y;
			yCount = y;
			xCount = x;
		}
		
		public Option(String n, int x, boolean l){
			name = n;
			this.id = getNextID();

			if(l)
				id = -1;
			
			isLabel = l;
			
			this.x = x;
			yCount += 40;
			this.y = yCount;
			xCount = x;
		}
		
		public Option(String n, boolean l){
			name = n;
			this.id = getNextID();
			
			if(l)
				id = -1;
			
			isLabel = l;
			
			this.x = xCount;
			yCount += 40;
			this.y = yCount;
		}
		
		public void render(){
			canvas.setColor(Color.WHITE);

			if(id == -1){
				Game.canvas.setFont(Game.canvas.getFont().deriveFont(1));
				TextFX.drawCString(name, y + 24);
				return;
			}else{
				Game.canvas.setFont(Game.canvas.getFont().deriveFont(0));
				TextFX.drawCString(name, y + 24);
			}
		}
	}
}
