package ERS.States;

import static ERS.Game.canvas;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;


import ERS.Game;
import ERS.Objects.GameState;
import ERS.SFX.TextFX;

public class Settings extends State{
	public int selected = 0;
	public int cardYOff = 0;
	public double yD = 1;
	public int max = 2;

	public boolean retToGame = false;
	
	public int yCount = 0, xCount = 0;
	
	public static ArrayList<Option> options = new ArrayList<Option>();
	
	public Settings() {
		selected = 0;
		cardYOff = 0;
		yD = 1;
		max = 2;
		
		yCount = 0;
		xCount = 0;
		
		state = GameState.SETTINGS;
		
		int xOff = 20;
		int leftPadding = 14;
		
		options.add(new Option("Slaps", null, leftPadding, 10));
		
		options.add(new Option("Top/Bottom", Setting.topBottom, xCount + xOff));
		options.add(new Option("King/Queen", Setting.kingQueen));
		options.add(new Option("Add To Ten", Setting.addToTen));

		options.add(new Option("Computer", null, leftPadding, yCount + 50));
		options.add(new Option("Random Burn", Setting.AIBurn, xCount + xOff));
		options.add(new Option("Slaps In", Setting.AISlapIn));
		options.add(new Option("Slaps", Setting.AISlap));

		options.add(new Option("Main Menu (ESC) | UP / DOWN | ENTER / SPACE", null, leftPadding, Game.HEIGHT - 40));

		options.add(new Option("Other", null, Game.WIDTH / 2, 10));
		options.add(new Option("10 Resets", Setting.tenStops, xCount + xOff));
		options.add(new Option("Sound", Setting.sound));
		options.add(new Option("Hints", Setting.hints));

		max = options.size() - 1;
		
		for(Option o: options){
			if(o.setting == null){
				max--;
			}
		}
	}
	
	@Override
	public void tick(){
		if(cardYOff < 0)
			yD = yD * -1;
		
		if(cardYOff > 8)
			yD = yD * -1;
		
		cardYOff += yD;
	}
	
	@Override
	public void render(){
		canvas.setColor(Color.BLACK);
		canvas.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

		canvas.setFont(new Font("Rockwell", 1, 24));

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
		getID(selected).setting.on = !getID(selected).setting.on;
	}
	
	public static int getNextID(){
		int count = 0;
		
		for(Option o: options){
			if(o.setting != null)
				count++;
		}
		
		return count;
	}
	
	public class Option{
		int x, y, id;
		String name;
		Setting setting;
		
		public Option(String n, Setting on, int x, int y){
			name = n;
			this.setting = on;
			this.id = getNextID();
			
			if(setting == null)
				id = -1;
			
			this.x = x;
			this.y = y;
			yCount = y;
			xCount = x;
		}
		
		public Option(String n, Setting on, int x){
			name = n;
			this.setting = on;
			this.id = getNextID();
			
			if(setting == null)
				id = -1;
			
			this.x = x;
			yCount += 40;
			this.y = yCount;
			xCount = x;
		}
		
		public Option(String n, Setting on){
			name = n;
			this.setting = on;
			this.id = getNextID();
			
			if(setting == null)
				id = -1;
			
			this.x = xCount;
			yCount += 40;
			this.y = yCount;
		}
		
		public void render(){
			canvas.setColor(Color.WHITE);

			if(setting == null || id == -1){
				canvas.drawString(name, x, y + 24);
				return;
			}else{
				canvas.drawString(name, x + 40, y + 24);
			}

			canvas.drawRect(x, y, 30, 30);
			
			if(id == selected){
				canvas.setColor(Color.GRAY);
				canvas.drawRect(x - 2, y - 2, 30 + TextFX.getSW(name) + 14, 34);
			}
			
			if(setting.on){
				canvas.setColor(Color.WHITE);
				canvas.drawString("X", x + 6, y + 24);
			}
		}
	}
}
