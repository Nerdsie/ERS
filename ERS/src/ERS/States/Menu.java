package ERS.States;

import static ERS.Game.canvas;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JOptionPane;

import ERS.Game;
import ERS.Objects.GameState;
import ERS.SFX.Images;

public class Menu extends State{
	public int selected = 0;
	public int cardYOff = 0;
	public double yD = 1;
	public int max = 1;
	
	public Menu(Game g) {
		super(g);
		
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
		canvas.fillRect(0, 0, game.width, game.height);

		canvas.setFont(new Font("Rockwell", 1, 24));
		
		canvas.drawImage(Images.TOP.get(), 410, 8, null);
		canvas.setColor(Color.WHITE);
		
		int xOff = 100;
		int yOff = 238;
		
		int x = 450 + xOff;
		int y = 140 + yOff;
		canvas.drawString("Play.", x, 140 + yOff);
		if(selected == 0)
			canvas.drawImage(Images.STAR.get(), x - 28 + cardYOff, y - 17, 18, 18, null);

		canvas.drawImage(Images.INST.get(), 8, 8, 400, 460, null);
	}
	
	public void select(){
		if(selected == 0){
			game.currentState = new InGame(game);
		}
	}
}
