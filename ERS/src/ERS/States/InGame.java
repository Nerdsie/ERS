package ERS.States;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import ERS.Game;
import ERS.Objects.Card;
import ERS.Objects.GameState;
import ERS.Objects.Notification;
import ERS.Objects.Player;
import ERS.Objects.SlapHand;
import ERS.Objects.Status;
import ERS.SFX.Images;

public class InGame extends State{

	public InGame(Game g) {
		super(g);
		
		state = GameState.GAME;
		game.restartGame();
	}
	
	@Override
	public void tick(){
		for(Player p: Player.values()){
			if(p!=null && p.notifications!=null){
				try{
					for(Notification n: p.notifications){
						n.update();
					}
				}catch(Exception e){}
			}
		}
			
		try{
			for(SlapHand h: Game.hands){
				h.tick();
			}
		}catch(Exception e){}
			
		game.counttwo++;
		
		int speed = new Random().nextInt(12);
		speed += game.level;
		
		for(Player p: Player.values()){
			if(p.deck.size()==0){
				p.isOut=true;
				if(p==Game.current){
					Game.current = Player.getNext();
				}
			}else{
				p.isOut=false;
			}
			
			if(p.deck.size()==52 && game.currentState.state==GameState.GAME){
				game.winner = p;
				game.currentState.state = GameState.FINISHED;
			}
		}
		
		if(Game.current!=null && game.currentState != null && game.currentState.state!=null && !Game.current.isHuman && ( game.currentState.state==GameState.GAME || game.currentState.state==GameState.PICKUPWAITING )){
			game.count++;
			
			speed = new Random().nextInt(12);
			speed+= game.level;
			
			if(game.count>15 + speed){
				game.playCard();
				game.counttwo = 0;
				game.count = 0;
				return;
			}
		}
		
		if(game.counttwo>20 +speed){
			Player p = Player.getPlayer(new Random().nextInt(4) + 1);
			
			if(!p.isHuman){
				if(game.isSlap()){
					if(new Random().nextInt(3)==0){
						game.trySlap(p);
						
						game.counttwo = 0;
						game.count = -10;
						
						return;
					}
				}
				
				if(new Random().nextInt(100)==0){
					game.trySlap(p);
					
					game.counttwo = 0;
					game.count = -10;
					
					return;
				}
			}
			
			game.counttwo = 0;
		}
	}
	
	@Override
	public void render(){
		Game.canvas.clearRect(0, 0, game.width, game.height);
		Game.canvas.drawImage(Images.BACKGROUND.get(), 0, 0, game.width, game.height, null);
		
		int ww = 20, hh = 25, ws = 3, hs = 3;
		
		//Player 1's deck
		int max = Math.min(3, Player.FIRST.deck.size());
		for(int i = 0; i < max; i++){
			Game.canvas.drawImage(Images.BACK.get(), game.width / 2 - 60 + (16*i), game.height - 84, ww * ws, hh * hs, null);
		}
		
		//Player 2's deck
		max = Math.min(3, Player.SECOND.deck.size());
		for(int i = 0; i < max; i++){
			Game.canvas.drawImage(Images.BACK.get(), 5 + (16*i), game.height / 2 - 36, ww * ws, hh * hs, null);
		}
		
		//Player 3's deck
		max = Math.min(3, Player.THIRD.deck.size());
		for(int i = 0; i < max; i++){
			Game.canvas.drawImage(Images.BACK.get(), game.width / 2 - 60 + (16*i), 8, ww * ws, hh * hs, null);
		}
		
		//Player 4's deck
		max = Math.min(3, Player.FOURTH.deck.size());
		for(int i = 0; i < max; i++){
			Game.canvas.drawImage(Images.BACK.get(), game.width - 115 + (16*i), game.height / 2 - 40, ww * ws, hh * hs, null);
		}
		
		//Middle pile!
	
		int mps = 4;
		
		Card topBurnt = null;
		for(int i = Game.pile.size(); i > 0; i--){
			if(Game.pile.get(i - 1).status==Status.BURNT)
				topBurnt = Game.pile.get(i - 1);
		}
		
		try{
			if(topBurnt !=null && topBurnt.status==Status.BURNT){
				Game.canvas.setColor(new Color(255, 0, 0, 75));
				Game.canvas.drawImage(topBurnt.image, game.width / 2 - 16 - (16*4), game.height / 2 - 40 + 8, ww * mps, hh * mps, null);
				Game.canvas.fillRect(game.width / 2 - 16 - 16 * 4, game.height / 2 - 40 + 8, ww * mps, hh * mps);
			}
		}catch(Exception e){}
		
		max = Math.min(3, Game.pile.size());
		for(int i = max; i >= 1; i--){
			if(Game.pile.size() - i >= 0){
				if(Game.pile.get(Game.pile.size() - i).status!=Status.BURNT)
					Game.canvas.drawImage(Game.pile.get(Game.pile.size() - i).image, game.width / 2 - 16 - (16*i), game.height / 2 - 50, ww * mps, hh * mps, null);
			}
		}
		
		//Player 1 Check
		Game.canvas.setColor(Color.BLACK);
		Game.canvas.setFont(new Font("Arial", 1, 16));
		Game.canvas.fillRect( game.width / 2 + 42, game.height - 58, 50, 50);
		Game.canvas.drawString(Player.FIRST.deck.size() + "", game.width / 2 + 42, game.height - 64);
		Game.canvas.setColor(Color.WHITE);
		Game.canvas.fillRect( game.width / 2 + 40, game.height - 60, 50, 50);
		Game.canvas.drawString(Player.FIRST.deck.size() + "", game.width / 2 + 40, game.height - 66);
		if(Game.current==Player.FIRST){
			Game.canvas.drawImage(Images.CHECK.get(), game.width / 2 + 43, game.height - 56, 45, 45, null);
		}
	
		//Player 2 Check
		Game.canvas.setColor(Color.BLACK);
		Game.canvas.fillRect( 8, game.height / 2 + 48, 50, 50);
		Game.canvas.drawString(Player.SECOND.deck.size() + "", 62, game.height / 2 + 60);
		Game.canvas.setColor(Color.WHITE);
		Game.canvas.drawString(Player.SECOND.deck.size() + "", 60, game.height / 2 + 58);
		Game.canvas.fillRect( 6, game.height / 2 + 46, 50, 50);
		if(Game.current==Player.SECOND){
			Game.canvas.drawImage(Images.CHECK.get(), 9, game.height / 2 + 49, 45, 45, null);
		}
		
		//Player 3 Check
		Game.canvas.setColor(Color.BLACK);
		Game.canvas.fillRect( game.width / 2 - 116, 10, 50, 50);
		Game.canvas.drawString(Player.THIRD.deck.size() + "", game.width / 2 - 84, 78);
		Game.canvas.setColor(Color.WHITE);
		Game.canvas.drawString(Player.THIRD.deck.size() + "", game.width / 2 - 86, 76);
		Game.canvas.fillRect( game.width / 2 - 118, 8, 50, 50);
		if(Game.current==Player.THIRD){
			Game.canvas.drawImage(Images.CHECK.get(), game.width / 2 - 115, 11, 45, 45, null);
		}
	
		//Player 4 Check
		Game.canvas.setColor(Color.BLACK);
		Game.canvas.fillRect( game.width - 71, game.height / 2  - 94, 50, 50);
		Game.canvas.drawString(Player.FOURTH.deck.size() + "",  game.width - 94, game.height / 2  - 46);
		Game.canvas.setColor(Color.WHITE);
		Game.canvas.drawString(Player.FOURTH.deck.size() + "",  game.width - 96, game.height / 2  - 48);
		Game.canvas.fillRect( game.width - 73, game.height / 2  - 96, 50, 50);
		if(Game.current==Player.FOURTH){
			Game.canvas.drawImage(Images.CHECK.get(), game.width - 70, game.height / 2 - 93, 45, 45, null);
		}
		
		//Player 1 notification
		for(Notification n: Player.FIRST.notifications){
			Game.canvas.setColor(new Color(50, 50, 50, n.alpha));
			Game.canvas.drawString(n.message, game.width / 2 + 64 + n.xOff, game.height - 64 + n.yOff);
			Game.canvas.setColor(new Color(n.color.getRed(), n.color.getGreen(), n.color.getBlue(), n.alpha));
			Game.canvas.drawString(n.message, game.width / 2 + 62 + n.xOff, game.height - 66 + n.yOff);
		}
			
		//Player 2 notification
		for(Notification n: Player.SECOND.notifications){
			Game.canvas.setColor(new Color(50, 50, 50, n.alpha));
			Game.canvas.drawString(n.message, 62 + n.xOff, game.height / 2  + 78 + n.yOff);
			Game.canvas.setColor(new Color(n.color.getRed(), n.color.getGreen(), n.color.getBlue(), n.alpha));
			Game.canvas.drawString(n.message, 60 + n.xOff, game.height / 2 + 76 + n.yOff);
		}
		
		//Player 3 notification
		for(Notification n: Player.THIRD.notifications){
			Game.canvas.setColor(new Color(50, 50, 50, n.alpha));
			Game.canvas.drawString(n.message, game.width / 2 - 114 + n.xOff, 78 + n.yOff);
			Game.canvas.setColor(new Color(n.color.getRed(), n.color.getGreen(), n.color.getBlue(), n.alpha));
			Game.canvas.drawString(n.message, game.width / 2 - 116 + n.xOff, 76 + n.yOff);
		}
		
		//Player 4 notification
		for(Notification n: Player.FOURTH.notifications){
			Game.canvas.setColor(new Color(50, 50, 50, n.alpha));
			Game.canvas.drawString(n.message,  game.width - 103 + n.xOff, game.height / 2  - 64 + n.yOff);
			Game.canvas.setColor(new Color(n.color.getRed(), n.color.getGreen(), n.color.getBlue(), n.alpha));
			Game.canvas.drawString(n.message,  game.width - 105 + n.xOff, game.height / 2  - 66 + n.yOff);
		}
			
		//Render winner message!!!!
		if(game.winner!=null && game.currentState.state==GameState.FINISHED){
			Game.canvas.setFont(new Font("Arial", 1, 40));
			Game.canvas.setColor(Color.BLACK);
			int xOff = 38;
			Game.canvas.drawString("PLAYER " + game.winner.id + " WON!", game.width / 2 - 156, game.height / 2 - 16 - 10);
			Game.canvas.drawString("Press [R] to restart.", game.width / 2 - 226 + xOff, game.height / 2 + 24  - 10);
			Game.canvas.setColor(Color.RED);
			Game.canvas.drawString("PLAYER " + game.winner.id + " WON!", game.width / 2 - 160, game.height / 2 - 20 - 10);
			Game.canvas.drawString("Press [R] to restart.", game.width / 2 - 230 + xOff, game.height / 2 + 20 - 10);
		}
		
		/*
		for(Player p: Player.values()){
			if(p.isColl){
				int multi = 2;
				mps = 4;
				max = Math.min(3, p.deck.size());
				for(int i = max; i >= 1; i--){
					game.canvas.drawImage(p.deck.get(p.deck.size() - i).image, game.width / 2 - 16 - (16*i) + (p.collX * multi), game.height / 2 - 70 + (p.collY * multi), ww * mps, hh * mps, null);
					
					try{
						if(p.deck.get(p.deck.size() - i).status==Status.BURNT){
							game.canvas.setColor(new Color(255, 0, 0, 75));
							game.canvas.fillRect(game.width / 2 - 16 - (16*i) + (p.collX * multi), game.height / 2 - 70 + (p.collY * multi), ww * mps, hh * mps);
						}
					}catch(Exception e){}
				}	
			}
		} */
		
		for(SlapHand h: Game.hands){
			if(h.owner == Player.FIRST)
				Game.canvas.drawImage(h.img, game.width / 2 - 44, game.height - 190, 60, 60, null);
			if(h.owner == Player.SECOND)
				Game.canvas.drawImage(h.img, 120, game.height / 2 - 50 , 60, 60, null);
			if(h.owner == Player.THIRD)
				Game.canvas.drawImage(h.img, game.width / 2 - 44, 110 , 60, 60, null);
			if(h.owner == Player.FOURTH)
				Game.canvas.drawImage(h.img, game.width - 200, game.height / 2 - 50 , 60, 60, null);
		}
	}
}
