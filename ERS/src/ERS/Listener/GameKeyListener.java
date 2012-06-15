package ERS.Listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ERS.Game;
import ERS.Objects.GameState;
import ERS.Objects.Player;
import ERS.States.Menu;

public class GameKeyListener implements KeyListener{
	public Game game;
	
	public GameKeyListener(Game g){
		game = g;
	}
		
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_R && (game.currentState.state == GameState.MENU || game.currentState.state == GameState.FINISHED)){
			game.restartGame();
			game.currentState.state = GameState.GAME;
			game.winner = null;
		}

		if(e.getKeyCode() == KeyEvent.VK_UP && game.currentState instanceof Menu){
			Menu menu = (Menu) game.currentState;
			
			menu.selected--;
			if(menu.selected>menu.max)
				menu.selected = 0;
			if(menu.selected<0)
				menu.selected = menu.max;
		}

		if(e.getKeyCode() == KeyEvent.VK_ENTER && game.currentState instanceof Menu){
			Menu menu = (Menu) game.currentState;

			menu.select();
		}

		if(e.getKeyCode() == KeyEvent.VK_DOWN && game.currentState instanceof Menu){
			Menu menu = (Menu) game.currentState;
			
			menu.selected++;
			if(menu.selected>menu.max)
				menu.selected = 0;
			if(menu.selected<0)
				menu.selected = menu.max;
		}
		
		if(Player.FIRST.isHuman){
			if(e.getKeyCode() == KeyEvent.VK_NUMPAD1){
				if(game.currentState.state == GameState.GAME){
					if(Game.current==Player.FIRST && !Player.FIRST.isOut)
						game.playCard();
					else
						game.burnCard(Player.FIRST);
				}
				if(game.currentState.state == GameState.PICKUPWAITING && game.pickupOwner() == Player.FIRST){
					game.tryPickup(Player.FIRST);
				}
			}
	
			if(e.getKeyCode() == KeyEvent.VK_NUMPAD3){
				game.trySlap(Player.FIRST);
			}
		}
		
		if(Player.SECOND.isHuman){
			if(e.getKeyCode() == KeyEvent.VK_UNDERSCORE){
				if(game.currentState.state == GameState.GAME){
					if(Game.current==Player.SECOND && !Player.SECOND.isOut)
						game.playCard();
					else
						game.burnCard(Player.SECOND);
				}
				if(game.currentState.state == GameState.PICKUPWAITING && game.pickupOwner() == Player.SECOND){
					game.tryPickup(Player.SECOND);
				}
			}
	
			if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
				game.trySlap(Player.SECOND);
			}
		}
		
		if(Player.THIRD.isHuman){
			if(e.getKeyCode() == KeyEvent.VK_G){
				if(game.currentState.state == GameState.GAME){
					if(Game.current==Player.THIRD && !Player.THIRD.isOut)
						game.playCard();
					else
						game.burnCard(Player.THIRD);
				}
				if(game.currentState.state == GameState.PICKUPWAITING && game.pickupOwner() == Player.THIRD){
					game.tryPickup(Player.THIRD);
				}
			}
	
			if(e.getKeyCode() == KeyEvent.VK_J){
				game.trySlap(Player.THIRD);
			}
		}
		
		if(Player.FOURTH.isHuman){
			if(e.getKeyCode() == KeyEvent.VK_Q){
				if(game.currentState.state == GameState.GAME){
					if(Game.current==Player.FOURTH && !Player.FOURTH.isOut)
						game.playCard();
					else
						game.burnCard(Player.FOURTH);
				}
				if(game.currentState.state == GameState.PICKUPWAITING && game.pickupOwner() == Player.FOURTH){
					game.tryPickup(Player.FOURTH);
				}
			}
	
			if(e.getKeyCode() == KeyEvent.VK_E){
				game.trySlap(Player.FOURTH);
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
