package ERS.Objects;

import java.util.ArrayList;

public class Deck {
	public ArrayList<Integer> cards = new ArrayList<Integer>();
	
	public void addPile(ArrayList<Integer> pile){
		for(int i: pile){
			cards.add(i);
		}
	}
	
	public int nextCard(){
		return cards.get(0);
	}
	
	public void play(){
		cards.remove(cards.get(0));
	}
	
	public boolean isFace(){
		if(cards.get(0)>=11)
			return true;
		
		return false;
	}
	
	public boolean isOut(){
		if(cards.isEmpty())
			return true;
		
		return false;
	}
}
