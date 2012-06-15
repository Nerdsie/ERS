package ERS.Listener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import ERS.Game;

public class GameFocusListener implements FocusListener{
	public Game game;
	
	public GameFocusListener(Game g){
		game = g;
	}
	
	public void focusGained(FocusEvent e) {
		game.focus = true;
	}

	public void focusLost(FocusEvent e) {
		game.focus = false;
	}

}
