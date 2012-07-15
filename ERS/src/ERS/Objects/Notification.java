package ERS.Objects;

import java.awt.Color;

import ERS.Objects.GamePlay.Player;

public class Notification {
	public int xOff = 0, yOff = 0;
	public int alpha = 255;
	public Color color = Color.WHITE;
	public Player owner;
	public String message;
	
	public Notification(Player owner, String m){
		this.owner = owner;
		message = m;
	}
	
	public Notification(Player owner, String m, Color c){
		this.owner = owner;
		message = m;
		color = c;
	}
	
	public void update(){
		xOff+=owner.xD;
		yOff+=owner.yD;
		
		alpha-=5;
		if(alpha<=0){
			owner.notifications.remove(this);
		}
	}
}
