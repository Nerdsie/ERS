package ERS.Objects;

import java.awt.Color;
import java.util.ArrayList;

import ERS.Game;
import ERS.SFX.Images;

public enum Player {
	FIRST(1, 0, -2), SECOND(2, 2, 0), THIRD(3, 0, 2), FOURTH(4, -2, 0);
	
	public boolean isOut;
	public ArrayList<Card> deck = new ArrayList<Card>();
	public int id;
	public int xD = 0, yD = 0;
	public boolean isHuman = false;
	public ArrayList<Notification> notifications = new ArrayList<Notification>();
	
	Player(int i, int xx, int yy){
		id = i;
		xD = xx;
		yD = yy;
	}
	
	public ArrayList<Card> getDeck(){
		return deck;
	}
	
	public static Player getNext(){
		boolean run = true;
		
		int check = Game.current.id ;
		
		while(run){			
			check++;
			
			if(check > 4)
				check = 1;
			
			Player checking = getPlayer(check);
			
			if(!checking.isOut){
				run = false;
				break;
			}

		}

		Player checking = getPlayer(check);
		return checking;
	}
	
	
	public static Player getPlayer(int i){
		for(Player p: Player.values()){
			if(p.id==i)
				return p;
		}
		
		return null;
	}
	
	public void getPile(){
		int gained = Game.pile.size();

		addNotification("+" + gained);
		
		for(Card c: Game.pile){
			deck.add(c);
			c.status=Status.INHAND;
		}
		
		//Game.hands.add(getSlapHand());
		Game.pile.clear();
	}
	
	public SlapHand getSlapHand(){
		if(id==1)
			return new SlapHand(this, Images.HAND1.get());
		if(id==2)
			return new SlapHand(this, Images.HAND2.get());
		if(id==3)
			return new SlapHand(this, Images.HAND3.get());
		if(id==4)
			return new SlapHand(this, Images.HAND4.get());
		
		return null;
	}
	
	public void addNotification(String m){
		notifications.add(new Notification(this, m));
	}
	
	public void addNotification(String m, Color c){
		notifications.add(new Notification(this, m, c));
	}
}
