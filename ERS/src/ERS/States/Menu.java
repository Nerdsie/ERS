package ERS.States;

import static ERS.Game.canvas;

import java.awt.Color;
import java.awt.Font;


import ERS.Game;
import ERS.Objects.GameState;
import ERS.SFX.Images;

public class Menu extends State{
	public int selected = 0;
	public int cardYOff = 0;
	public double yD = 1;
	public int max = 1;
	
	public Menu() {
		state = GameState.MENU;
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
		
		canvas.drawImage(Images.TOP, 410, 8, null);
		canvas.setColor(Color.WHITE);
		
		int xOff = 00;
		int yOff = 238;
		
		int x = 450 + xOff;
		canvas.drawString("Play.", x, 140 + yOff);
		if(selected == 0)
			canvas.drawImage(Images.STAR, x - 28 + cardYOff, 140 - 17 + yOff, 18, 18, null);
		
		yOff+=26;
		
		canvas.drawString("Settings.", x, 140 + yOff);
		if(selected == 1)
			canvas.drawImage(Images.STAR, x - 28 + cardYOff, 140 - 17 + yOff, 18, 18, null);

		canvas.drawImage(Images.INST, 8, 8, 400, 460, null);
	}
	
	public void select(){
		if(selected == 0){
			Game.currentState = State.inGame;
			((InGame) State.inGame).start();
		}
		if(selected == 1){
			((Settings) State.settings).retToGame = false;
			Game.currentState = State.settings;
		}
	}
}
