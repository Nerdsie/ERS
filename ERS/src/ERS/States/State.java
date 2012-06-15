package ERS.States;

import ERS.Game;
import ERS.Objects.GameState;

public class State{
	public Game game;
	public GameState state = GameState.MENU;
	
	public State(Game g){
		game = g;
	}
	
	public void tick(){
		
	}
	
	public void render(){
		
	}
}
