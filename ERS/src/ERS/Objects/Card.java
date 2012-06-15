package ERS.Objects;

import java.awt.Image;

import ERS.SFX.Images;

public class Card {
	public Player owner;
	public int value = 0;
	public Status status = Status.INHAND;
	public Image image = null;
	
	public Card(int v, Status s){
		value = v;
		status = s;
		image = new Images().getImage(v + ".png");
	}
	
	public Card(int v){
		value = v;
		image = new Images().getImage(v + ".png");
	}
}
