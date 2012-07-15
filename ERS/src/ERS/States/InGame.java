package ERS.States;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import ERS.Game;
import ERS.Objects.GameState;
import ERS.Objects.Notification;
import ERS.Objects.SlapHand;
import ERS.Objects.Status;
import ERS.Objects.GamePlay.Card;
import ERS.Objects.GamePlay.Player;
import ERS.SFX.Images;
import ERS.SFX.TextFX;

public class InGame extends State{

	public InGame() {
		state = GameState.GAME;
	}
	
	public void start(){
		Game.restartGame();
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
			
		Game.counttwo++;
		
		int speed = new Random().nextInt(12);
		speed += Game.level;
		
		for(Player p: Player.values()){
			if(p.deck.size()==0){
				p.isOut=true;
				if(p==Game.current){
					Game.current = Player.getNext();
				}
			}else{
				p.isOut=false;
			}
			
			if(p.deck.size()==52 && Game.currentState.state==GameState.GAME){
				Game.winner = p;
				Game.currentState.state = GameState.FINISHED;
			}
		}
		
		if(Game.current!=null && Game.currentState != null && Game.currentState.state!=null && !Game.current.isHuman && ( Game.currentState.state==GameState.GAME || Game.currentState.state==GameState.PICKUPWAITING )){
			Game.count++;
			
			speed = new Random().nextInt(12);
			speed+= Game.level;
			
			if(Game.count>15 + speed){
				Game.playCard();
				Game.counttwo = 0;
				Game.count = 0;
				return;
			}
		}
		
		if(Game.counttwo>8 +speed){
			Player p = Player.getPlayer(new Random().nextInt(4) + 1);
			
			if(!p.isHuman){
				if(Game.isSlap()){
					if(new Random().nextInt(8)<3 || (p.isOut && new Random().nextInt(12)<10)){
						Game.trySlap(p);
						
						return;
					}
				}
				
				if(new Random().nextInt(100)==0 && Setting.AIBurn.on){
					if(Game.isSlap())
						Game.trySlap(p);
					else
						Game.burnCard(p);
					
					return;
				}
			}
			
			Game.counttwo = 0;
		}
	}
	
	@Override
	public void render(){
		Game.canvas.clearRect(0, 0, Game.WIDTH, Game.HEIGHT);
		Game.canvas.drawImage(Images.BACKGROUND, 0, 0, Game.WIDTH, Game.HEIGHT, null);
		
		int ww = 20, hh = 25, ws = 3, hs = 3;
		
		//Player 1's deck
		int max = Math.min(3, Player.FIRST.deck.size());
		for(int i = 0; i < max; i++){
			Game.canvas.drawImage(Images.BACK, Game.WIDTH / 2 - 60 + (16*i), Game.HEIGHT - 84, ww * ws, hh * hs, null);
		}
		
		//Player 2's deck
		max = Math.min(3, Player.SECOND.deck.size());
		for(int i = 0; i < max; i++){
			Game.canvas.drawImage(Images.BACK, 5 + (16*i), Game.HEIGHT / 2 - 36, ww * ws, hh * hs, null);
		}
		
		//Player 3's deck
		max = Math.min(3, Player.THIRD.deck.size());
		for(int i = 0; i < max; i++){
			Game.canvas.drawImage(Images.BACK, Game.WIDTH / 2 - 60 + (16*i), 8, ww * ws, hh * hs, null);
		}
		
		//Player 4's deck
		max = Math.min(3, Player.FOURTH.deck.size());
		for(int i = 0; i < max; i++){
			Game.canvas.drawImage(Images.BACK, Game.WIDTH - 115 + (16*i), Game.HEIGHT / 2 - 40, ww * ws, hh * hs, null);
		}
		
		//Middle pile!
	
		
		//Burn cards
		int mps = 4;
		
		Card topBurnt = null;
		for(int i = Game.pile.size(); i > 0; i--){
			if(Game.pile.get(i - 1).status==Status.BURNT)
				topBurnt = Game.pile.get(i - 1);
		}
		
		try{
			if(topBurnt !=null && topBurnt.status==Status.BURNT){
				Game.canvas.setFont(new Font("Times New Roman", 1, 17));
				TextFX.draw3DString("Latest Burn.", Color.WHITE, Color.BLACK, Game.WIDTH / 2 - 16 - (16*4), Game.HEIGHT / 2 + 90, 2);
				Game.canvas.setColor(new Color(255, 0, 0, 75));
				Game.canvas.drawImage(topBurnt.image, Game.WIDTH / 2 - 16 - (16*4), Game.HEIGHT / 2 - 40 + 16, ww * mps, hh * mps, null);
				Game.canvas.fillRect(Game.WIDTH / 2 - 16 - 16 * 4, Game.HEIGHT / 2 - 40 + 16, ww * mps, hh * mps);
			}
		}catch(Exception e){}

		
		//Bottom card
		Card bottom = null;
		int nBC = 0;
		for(int i = Game.pile.size(); i > 0; i--){
			if(Game.pile.get(i - 1).status==Status.INGAME){
				bottom = Game.pile.get(i - 1);
				nBC++;
			}
		}
		
		try{
			if(bottom !=null && bottom.status==Status.INGAME && nBC > 3 && Setting.topBottom.on){
				Game.canvas.setFont(new Font("Times New Roman", 1, 17));
				TextFX.draw3DString("Bottom Card.", Color.WHITE, Color.BLACK, Game.WIDTH / 2 - 16 - (16*4), Game.HEIGHT / 2 - 76, 2);
				Game.canvas.setColor(new Color(0, 0, 255, 75));
				Game.canvas.drawImage(bottom.image, Game.WIDTH / 2 - 16 - (16*4), Game.HEIGHT / 2 - 80 + 8, ww * mps, hh * mps, null);
				Game.canvas.fillRect(Game.WIDTH / 2 - 16 - 16 * 4, Game.HEIGHT / 2 - 80 + 8, ww * mps, hh * mps);
			}
		}catch(Exception e){}
		
		
		//Deck
		max = Math.min(3, Game.pile.size());
		for(int i = max; i >= 1; i--){
			if(Game.pile.size() - i >= 0){
				if(Game.pile.get(Game.pile.size() - i).status!=Status.BURNT)
					Game.canvas.drawImage(Game.pile.get(Game.pile.size() - i).image, Game.WIDTH / 2 - 16 - (16*i), Game.HEIGHT / 2 - 50, ww * mps, hh * mps, null);
			}
		}
		
		//Player 1 Check
		Game.canvas.setColor(Color.BLACK);
		Game.canvas.setFont(new Font("Arial", 1, 16));
		Game.canvas.fillRect( Game.WIDTH / 2 + 42, Game.HEIGHT - 58, 50, 50);
		Game.canvas.drawString(Player.FIRST.deck.size() + "", Game.WIDTH / 2 + 42, Game.HEIGHT - 64);
		Game.canvas.setColor(Color.WHITE);
		Game.canvas.fillRect( Game.WIDTH / 2 + 40, Game.HEIGHT - 60, 50, 50);
		Game.canvas.drawString(Player.FIRST.deck.size() + "", Game.WIDTH / 2 + 40, Game.HEIGHT - 66);
		if(Game.current==Player.FIRST){
			Game.canvas.drawImage(Images.CHECK, Game.WIDTH / 2 + 43, Game.HEIGHT - 56, 45, 45, null);
		}
	
		//Player 2 Check
		Game.canvas.setColor(Color.BLACK);
		Game.canvas.fillRect( 8, Game.HEIGHT / 2 + 48, 50, 50);
		Game.canvas.drawString(Player.SECOND.deck.size() + "", 62, Game.HEIGHT / 2 + 60);
		Game.canvas.setColor(Color.WHITE);
		Game.canvas.drawString(Player.SECOND.deck.size() + "", 60, Game.HEIGHT / 2 + 58);
		Game.canvas.fillRect( 6, Game.HEIGHT / 2 + 46, 50, 50);
		if(Game.current==Player.SECOND){
			Game.canvas.drawImage(Images.CHECK, 9, Game.HEIGHT / 2 + 49, 45, 45, null);
		}
		
		//Player 3 Check
		Game.canvas.setColor(Color.BLACK);
		Game.canvas.fillRect( Game.WIDTH / 2 - 116, 10, 50, 50);
		Game.canvas.drawString(Player.THIRD.deck.size() + "", Game.WIDTH / 2 - 84, 78);
		Game.canvas.setColor(Color.WHITE);
		Game.canvas.drawString(Player.THIRD.deck.size() + "", Game.WIDTH / 2 - 86, 76);
		Game.canvas.fillRect( Game.WIDTH / 2 - 118, 8, 50, 50);
		if(Game.current==Player.THIRD){
			Game.canvas.drawImage(Images.CHECK, Game.WIDTH / 2 - 115, 11, 45, 45, null);
		}
	
		//Player 4 Check
		Game.canvas.setColor(Color.BLACK);
		Game.canvas.fillRect( Game.WIDTH - 71, Game.HEIGHT / 2  - 94, 50, 50);
		Game.canvas.drawString(Player.FOURTH.deck.size() + "",  Game.WIDTH - 94, Game.HEIGHT / 2  - 46);
		Game.canvas.setColor(Color.WHITE);
		Game.canvas.drawString(Player.FOURTH.deck.size() + "",  Game.WIDTH - 96, Game.HEIGHT / 2  - 48);
		Game.canvas.fillRect( Game.WIDTH - 73, Game.HEIGHT / 2  - 96, 50, 50);
		if(Game.current==Player.FOURTH){
			Game.canvas.drawImage(Images.CHECK, Game.WIDTH - 70, Game.HEIGHT / 2 - 93, 45, 45, null);
		}
		
		//Player 1 notification
		for(Notification n: Player.FIRST.notifications){
			Game.canvas.setColor(new Color(50, 50, 50, n.alpha));
			Game.canvas.drawString(n.message, Game.WIDTH / 2 + 64 + n.xOff, Game.HEIGHT - 64 + n.yOff);
			Game.canvas.setColor(new Color(n.color.getRed(), n.color.getGreen(), n.color.getBlue(), n.alpha));
			Game.canvas.drawString(n.message, Game.WIDTH / 2 + 62 + n.xOff, Game.HEIGHT - 66 + n.yOff);
		}
			
		//Player 2 notification
		for(Notification n: Player.SECOND.notifications){
			Game.canvas.setColor(new Color(50, 50, 50, n.alpha));
			Game.canvas.drawString(n.message, 62 + n.xOff, Game.HEIGHT / 2  + 78 + n.yOff);
			Game.canvas.setColor(new Color(n.color.getRed(), n.color.getGreen(), n.color.getBlue(), n.alpha));
			Game.canvas.drawString(n.message, 60 + n.xOff, Game.HEIGHT / 2 + 76 + n.yOff);
		}
		
		//Player 3 notification
		for(Notification n: Player.THIRD.notifications){
			Game.canvas.setColor(new Color(50, 50, 50, n.alpha));
			Game.canvas.drawString(n.message, Game.WIDTH / 2 - 114 + n.xOff, 78 + n.yOff);
			Game.canvas.setColor(new Color(n.color.getRed(), n.color.getGreen(), n.color.getBlue(), n.alpha));
			Game.canvas.drawString(n.message, Game.WIDTH / 2 - 116 + n.xOff, 76 + n.yOff);
		}
		
		//Player 4 notification
		for(Notification n: Player.FOURTH.notifications){
			Game.canvas.setColor(new Color(50, 50, 50, n.alpha));
			Game.canvas.drawString(n.message,  Game.WIDTH - 103 + n.xOff, Game.HEIGHT / 2  - 64 + n.yOff);
			Game.canvas.setColor(new Color(n.color.getRed(), n.color.getGreen(), n.color.getBlue(), n.alpha));
			Game.canvas.drawString(n.message,  Game.WIDTH - 105 + n.xOff, Game.HEIGHT / 2  - 66 + n.yOff);
		}
			
		//Render winner message!!!!
		if(Game.winner!=null && Game.currentState.state==GameState.FINISHED){
			Game.canvas.setColor(new Color(0, 0, 0, 210));
			Game.canvas.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			
			Game.canvas.setFont(new Font("Arial", 1, 40));
			Game.canvas.setColor(Color.BLACK);

			TextFX.drawC3DString("Player " + Game.winner.id + " has Won!", Color.RED, Game.HEIGHT / 2 - 24, 3);
			TextFX.drawC3DString("Press [R] to restart!", Color.RED, Game.HEIGHT / 2 + 24, 3);
		}
		
		Game.canvas.setFont(new Font("Arial", 0, 24));
		
		if(Setting.hints.on && Game.isSlap()){
			TextFX.draw3DString("SLAP!", Game.WIDTH - 10 - TextFX.getSW("SLAP!"), Game.HEIGHT - 14, 2);
		}
		
		/*
		for(Player p: Player.values()){
			if(p.isColl){
				int multi = 2;
				mps = 4;
				max = Math.min(3, p.deck.size());
				for(int i = max; i >= 1; i--){
					Game.canvas.drawImage(p.deck.get(p.deck.size() - i).image, Game.WIDTH / 2 - 16 - (16*i) + (p.collX * multi), Game.HEIGHT / 2 - 70 + (p.collY * multi), ww * mps, hh * mps, null);
					
					try{
						if(p.deck.get(p.deck.size() - i).status==Status.BURNT){
							Game.canvas.setColor(new Color(255, 0, 0, 75));
							Game.canvas.fillRect(Game.WIDTH / 2 - 16 - (16*i) + (p.collX * multi), Game.HEIGHT / 2 - 70 + (p.collY * multi), ww * mps, hh * mps);
						}
					}catch(Exception e){}
				}	
			}
		} */
		
		for(SlapHand h: Game.hands){
			if(h.owner == Player.FIRST)
				Game.canvas.drawImage(h.img, Game.WIDTH / 2 - 44, Game.HEIGHT - 150, 60, 60, null);
			if(h.owner == Player.SECOND)
				Game.canvas.drawImage(h.img, 110, Game.HEIGHT / 2 - 30 , 60, 60, null);
			if(h.owner == Player.THIRD)
				Game.canvas.drawImage(h.img, Game.WIDTH / 2 - 44, 90 , 60, 60, null);
			if(h.owner == Player.FOURTH)
				Game.canvas.drawImage(h.img, Game.WIDTH - 190, Game.HEIGHT / 2 - 30 , 60, 60, null);
		}
		
		for(Player p: Player.values()){
			Game.canvas.setFont(new Font("Arial", 1, 17));
			
			if(p == Player.FIRST)
				TextFX.draw3DString(p.lives + " lives.", new Color(200, 255, 200), Color.BLACK, Game.WIDTH / 2 + 98, Game.HEIGHT - 10, 2);
			if(p == Player.SECOND)
				TextFX.draw3DString(p.lives + " lives.", new Color(200, 255, 200), Color.BLACK, 8, Game.HEIGHT / 2 + 116, 2);
			if(p == Player.THIRD)
				TextFX.draw3DString(p.lives + " lives.", new Color(200, 255, 200), Color.BLACK, Game.WIDTH / 2 - 122 - TextFX.getSW(p.lives + " lives."), 22, 2);
			if(p == Player.FOURTH)
				TextFX.draw3DString(p.lives + " lives.", new Color(200, 255, 200), Color.BLACK, Game.WIDTH - 22 - TextFX.getSW(p.lives + " lives."), Game.HEIGHT / 2  - 102, 2);
		}
	}
}
