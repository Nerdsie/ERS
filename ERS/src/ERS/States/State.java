package ERS.States;

import ERS.Objects.GameState;

public class State{
	public static State inGame = new InGame();
	public static State mainMenu = new Menu();
	public static State pauseScreen = new Pause();
	public static State settings = new Settings();
	
	public GameState state = GameState.MENU;
	
	public State(){
		
	}
	
	public void tick(){
		
	}
	
	public void render(){
		
	}
}
