package ERS.Objects;

import java.awt.Image;

import ERS.Game;

public class SlapHand {
	public Image img;
	int ticks = 0;
	public Player owner;
	
	public SlapHand(Player p, Image i){
		img = i;
		owner = p;
	}

	public void tick(){
		ticks++;
		
		if(ticks>=20)
			Game.hands.remove(this);
	}
}
