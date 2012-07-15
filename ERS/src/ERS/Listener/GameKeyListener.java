package ERS.Listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ERS.Game;
import ERS.Objects.GameState;
import ERS.Objects.GamePlay.Player;
import ERS.States.InGame;
import ERS.States.Menu;
import ERS.States.Pause;
import ERS.States.Setting;
import ERS.States.Settings;
import ERS.States.State;

public class GameKeyListener implements KeyListener{
	public Game game;
	
	public GameKeyListener(Game g){
		game = g;
	}
		
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_M){
			Setting.sound.on = !Setting.sound.on;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_R && (Game.currentState.state == GameState.FINISHED)){
			Game.winner = null;
			Game.currentState = State.mainMenu;
		}
		
		if(Game.currentState == State.pauseScreen){
			if(e.getKeyCode() == KeyEvent.VK_A){
				Game.getPlayers();
			}
			if(e.getKeyCode() == KeyEvent.VK_L){
				Game.getLevel();
			}
			if(e.getKeyCode() == KeyEvent.VK_E){
				Game.currentState = State.mainMenu;
			}
			if(e.getKeyCode() == KeyEvent.VK_S){
				Game.currentState = State.settings;
				((Settings) State.settings).retToGame = true;
			}
		}

		if((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && Game.currentState instanceof Menu){
			Menu menu = (Menu) Game.currentState;
			
			menu.selected--;
			if(menu.selected>menu.max)
				menu.selected = 0;
			if(menu.selected<0)
				menu.selected = menu.max;
			
			return;
		}

		if((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && Game.currentState instanceof Settings){
			Settings menu = (Settings) Game.currentState;
			
			menu.selected--;
			if(menu.selected>menu.max)
				menu.selected = 0;
			if(menu.selected<0)
				menu.selected = menu.max;
			
			return;
		}

		if((e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) && Game.currentState instanceof Menu){
			Menu menu = (Menu) Game.currentState;

			menu.select();
			
			return;
		}

		if((e.getKeyCode() == KeyEvent.VK_ESCAPE )){
			if(Game.currentState instanceof Settings){
				if(!((Settings) State.settings).retToGame)
					Game.currentState = new Menu();
				else
					Game.currentState = State.pauseScreen;
				
				game.save();
				
				return;
			}
			
			if(Game.currentState instanceof Pause){
				Game.currentState = State.inGame;
			
				return;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_P){
			if(Game.currentState instanceof InGame){
				Game.currentState = State.pauseScreen;
				
				return;
			}
		}

		if((e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) && Game.currentState instanceof Settings){
			Settings menu = (Settings) Game.currentState;

			menu.select();
			
			return;
		}

		if((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)  && Game.currentState instanceof Menu){
			Menu menu = (Menu) Game.currentState;
			
			menu.selected++;
			if(menu.selected>menu.max)
				menu.selected = 0;
			if(menu.selected<0)
				menu.selected = menu.max;
			
			return;
		}

		if((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)  && Game.currentState instanceof Settings){
			Settings menu = (Settings) Game.currentState;
			
			menu.selected++;
			if(menu.selected>menu.max)
				menu.selected = 0;
			if(menu.selected<0)
				menu.selected = menu.max;
			
			return;
		}
		
		if(Player.FIRST.isHuman){
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				if(Game.currentState.state == GameState.GAME){
					if(Game.current==Player.FIRST && !Player.FIRST.isOut)
						Game.playCard();
					else
						Game.burnCard(Player.FIRST);
				}
				if(Game.currentState.state == GameState.PICKUPWAITING && Game.pickupOwner() == Player.FIRST){
					Game.tryPickup(Player.FIRST);
				}
			}
	
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				Game.trySlap(Player.FIRST);
			}
		}
		
		if(Player.SECOND.isHuman){
			if(e.getKeyCode() == KeyEvent.VK_COMMA){
				if(Game.currentState.state == GameState.GAME){
					if(Game.current==Player.SECOND && !Player.SECOND.isOut)
						Game.playCard();
					else
						Game.burnCard(Player.SECOND);
				}
				if(Game.currentState.state == GameState.PICKUPWAITING && Game.pickupOwner() == Player.SECOND){
					Game.tryPickup(Player.SECOND);
				}
			}
	
		if(e.getKeyCode() == KeyEvent.VK_SLASH){
				Game.trySlap(Player.SECOND);
			}
		}
		
		if(Player.THIRD.isHuman){
			if(e.getKeyCode() == KeyEvent.VK_G){
				if(Game.currentState.state == GameState.GAME){
					if(Game.current==Player.THIRD && !Player.THIRD.isOut)
						Game.playCard();
					else
						Game.burnCard(Player.THIRD);
				}
				if(Game.currentState.state == GameState.PICKUPWAITING && Game.pickupOwner() == Player.THIRD){
					Game.tryPickup(Player.THIRD);
				}
			}
	
			if(e.getKeyCode() == KeyEvent.VK_J){
				Game.trySlap(Player.THIRD);
			}
		}
		
		if(Player.FOURTH.isHuman){
			if(e.getKeyCode() == KeyEvent.VK_Q){
				if(Game.currentState.state == GameState.GAME){
					if(Game.current==Player.FOURTH && !Player.FOURTH.isOut)
						Game.playCard();
					else
						Game.burnCard(Player.FOURTH);
				}
				if(Game.currentState.state == GameState.PICKUPWAITING && Game.pickupOwner() == Player.FOURTH){
					Game.tryPickup(Player.FOURTH);
				}
			}
	
			if(e.getKeyCode() == KeyEvent.VK_E){
				Game.trySlap(Player.FOURTH);
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}

}
