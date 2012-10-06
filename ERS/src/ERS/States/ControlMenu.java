package ERS.States;

import static ERS.Game.canvas;

import java.awt.Color;
import java.awt.Font;

import ERS.Game;
import ERS.Objects.GameState;

public class ControlMenu extends State{
	
	public ControlMenu() {
		state = GameState.CONTROL_MENU;
	}
	
	@Override
	public void tick(){

	}
	
	@Override
	public void render(){
		canvas.setColor(Color.BLACK);
		canvas.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

		canvas.setFont(new Font("Rockwell", 1, 24));
		canvas.setColor(Color.WHITE);

		int y = 30;
		canvas.drawString("Player Controls:", 10, y); y+=30;
		canvas.drawString("Player 1:", 30, y); y+=30;
		canvas.drawString("Left = Play/Collect", 40, y); y+=30;
		canvas.drawString("Right = Slap", 40, y); y+=40;
		canvas.drawString("Player 2:", 30, y); y+=30;
		canvas.drawString(", = Play/Collect", 40, y); y+=30;
		canvas.drawString("/ = Slap", 40, y); y+=30;
		canvas.drawString("Player 3:", 30, y); y+=30;
		canvas.drawString("G = Play/Collect", 40, y); y+=30;
		canvas.drawString("J = Slap", 40, y); y+=40;
		canvas.drawString("Player 4:", 30, y); y+=30;
		canvas.drawString("Q = Play/Collect", 40, y); y+=30;
		canvas.drawString("E = Slap", 40, y); y+=30;
		canvas.drawString("(ESC) Back", 10, y + 25);
		
		int xOff = -40;
		
		y = 30;
		canvas.drawString("More Controls:", 360 + xOff, y); y+=30;
		canvas.drawString("ESC = Pause", 380 + xOff, y); y+=30;
		canvas.drawString("M = Mute Toggle", 380 + xOff, y); y+=30;
		
		y += 10;
		canvas.drawString("Information:", 360 + xOff, y); y+=30;
		canvas.drawString("If you find a bug please", 380 + xOff, y); y+=30;
		canvas.drawString("email details to me at", 380 + xOff, y); y+=30;
		canvas.drawString("richcoll97@gmail.com", 380 + xOff, y); y+=30;
		canvas.drawString("or tweet at me.", 380 + xOff, y); y+=30;
		canvas.drawString("@NerdsWBNerds", 380 + xOff, y); y+=30;
	}
	
	public void select(){

	}
}
