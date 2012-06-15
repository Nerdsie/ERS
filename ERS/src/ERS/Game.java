package ERS;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import ERS.Listener.GameFocusListener;
import ERS.Listener.GameKeyListener;
import ERS.Objects.Card;
import ERS.Objects.GameState;
import ERS.Objects.Player;
import ERS.Objects.SlapHand;
import ERS.Objects.Status;
import ERS.States.Menu;
import ERS.States.State;


public class Game extends Applet implements Runnable {
	private static final long serialVersionUID = 1L;
	public boolean running = false;
	
	int x = 10, y = 10;
	int w = 50, h = 50;
	public int width = 640;
	public int height = 480;
	int ticks = 0;
	
	public Image backBuffer;
	public static Graphics canvas;
	
	public static ArrayList<Card> pile = new ArrayList<Card>();
	public static Player current = Player.FIRST;
	public int tries = -1;
	public Player winner = null;
	public static ArrayList<SlapHand> hands = new ArrayList<SlapHand>();
	
	public State currentState = new Menu(this);
	public int level = 20;
	public int players = 0;
	public int count = 0;
	public int counttwo = 0;
	
	public boolean focus = false;
	public int yFocOff = 0, yFocDir = 1;
	
	public void tick(){
		if(!focus){
			yFocOff+=yFocDir;
			
			if(yFocOff > 20){
				yFocDir = -yFocDir;
			}
			if(yFocOff < 0){
				yFocDir = -yFocDir;
			}
			
			return;
		}
		
		currentState.tick();
	}	

	public void render(){
		currentState.render();

		if(!focus){
			canvas.setColor(new Color(0, 0, 0, 210));
			canvas.fillRect(0, 0, width, height);

			int xOff = -135, yOff = yFocOff - 10;
			
			canvas.setFont(new Font("Arial", 1, 30));
			canvas.setColor(Color.GRAY);
			canvas.drawString("Click Here to Play!", width / 2 + 2 + xOff, height / 2 + 2 + yOff);
			canvas.setColor(Color.WHITE);
			canvas.drawString("Click Here to Play!", width / 2 + xOff, height / 2 + yOff);
			
			repaint();
		}
	}
	
	public void init(){
		
	}
	
	public void playCard(){
		int value = 0;

		if(currentState.state == GameState.PICKUPWAITING){
			tryPickup(pickupOwner());
			
			return;
		}
		
		if(isSlap() && !current.isHuman){
			trySlap(current);
		}
		
		if(current.deck.size()==0){
			current.isOut = true;
			current = Player.getNext();
		}

		value = current.deck.get(0).value;
		
		current.deck.get(0).owner=current;
		current.deck.get(0).status=Status.INGAME;
		pile.add(current.deck.get(0));
		current.deck.remove(current.deck.get(0));
		
		tries--;
		
		if(value>10){
			tries = value - 10;
			current = Player.getNext();
			currentState.state = GameState.GAME;
		}
		
		if(tries==0){
			currentState.state = GameState.PICKUPWAITING;
			current = pickupOwner();
		}
			
		if(tries<0){
			current = Player.getNext();
			currentState.state = GameState.GAME;
		}

		if(current !=null && current.deck !=null && current.deck.size()==0){
			current.isOut = true;
			current = Player.getNext();
		}
	}
	
	public void tryPickup(Player p){
		currentState.state = GameState.GAME;
		int max = Math.min(5, pile.size());
			
		for(int i = 1; i < max + 1; i++){
			int lookup = pile.size() - i;
			
			if(lookup<0)
				break;
			
			if(pile.get(lookup).value>10 && p==pile.get(lookup).owner){
				pile.get(lookup).owner.getPile();
				tries = -1;
				return;
			}
		}

		burnCard(p);
	}
	
	public Player pickupOwner(){
		int max = Math.min(5, pile.size());
			
		for(int i = 1; i < max + 1; i++){
			int lookup = pile.size() - i;
			
			if(lookup<0)
				break;
			
			if(pile.get(lookup).value>10){
				return pile.get(lookup).owner;
			}
		}

		return null;
	}
	
	public void burnCard(Player p){
		if(p == null || p.deck == null || pile == null || p.deck.isEmpty() || pile.isEmpty())
			return;
		
		ArrayList<Card> holder = new ArrayList<Card>();
		holder.add(p.deck.get(0));
		p.addNotification("-1", Color.RED);
		holder.get(0).status=Status.BURNT;
		p.deck.remove(p.deck.get(0));
		
		for(Card c: pile){
			holder.add(c);
		}
		
		pile = holder;
	}
	
	public void trySlap(Player p){
		counttwo = 0;
		count = -10;
		
		if(isSlap()){
			currentState.state = GameState.GAME;
			current = p;
			p.getPile();
			tries = -1;
			hands.add(p.getSlapHand());
		}else{
			tryPickup(p);
			
			if(p.deck.size()==0){
				current = Player.getNext();
			}
		}
	}
	
	public void run(){
		while(running){
			tick();
			render();
			repaint();
			
			try{
				Thread.sleep(17);
			}catch(Exception e){}
		}
	}
	
	public boolean isSlap(){
		if((pile.size()>1 && pile.get(pile.size() - 1).value == pile.get(pile.size() - 2).value && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 2).status==Status.INGAME) || (pile.size()>2 && pile.get(pile.size() - 1).value == pile.get(pile.size() - 3).value && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 3).status==Status.INGAME)){
			return true;
		}
		
		return false;
	}
	
	public void restartGame(){
		for(Player p: Player.values()){
			pile.clear();
			p.deck.clear();
			p.isOut = false;
		}
	
		try{
			
			players = Integer.parseInt(JOptionPane.showInputDialog("How many players?"));
			if(players>4)
				players = 4;
			if(players<0)
				players = 0;
			
		}catch(Exception e){players = 0;}

		this.requestFocus();
		
		if(players < 4){
			try{
				level = (Integer.parseInt(JOptionPane.showInputDialog("What skill level? 1 (Evil) - 5 (Easy)?"))) * 5 - 10;
			}catch(Exception e){level = 20;}
		}
		
		this.requestFocus();
		
		for(int i = 0; i < players; i++){
			Player.getPlayer(i + 1).isHuman = true;
		}
		
		for(int i = players; i < 4; i++){
			Player.getPlayer(i + 1).isHuman = false;
		}
		
		int[] diff = new int[13];
		
		for(int i = 0; i < 13; i++){
			diff[i]=4;
		}
		
		for(Player p: Player.values()){
			p.isOut = false;
			p.notifications.clear();
			p.deck.clear();
			
			for(int i = 0; i < 13; i++){
				int add = new Random().nextInt(13);
				
				while(diff[add]==0){
					add = new Random().nextInt(13);
				}
				
				p.deck.add(new Card(add + 2));
				diff[add]--;
			}
		}
		
		currentState.state = GameState.GAME;
	}
	
	public void start(){
		this.setSize( width, height );
		
		running = true;
		
		backBuffer = createImage(this.width, this.height);
		canvas = backBuffer.getGraphics();

		this.addKeyListener(new GameKeyListener(this));
		this.addFocusListener(new GameFocusListener(this));
		
		new Thread(this).start();

		this.requestFocus();
	}
	
	public void stop(){
		running = false;
	}
	
	public void paint(Graphics g){
		g.drawImage(backBuffer, 0, 0, null);
	}
	
	public void update(Graphics g){	
		paint(g);
	}
}
