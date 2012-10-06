package ERS;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import ERS.Files.Stream;
import ERS.Listener.GameFocusListener;
import ERS.Listener.GameKeyListener;
import ERS.Objects.GameState;
import ERS.Objects.SlapHand;
import ERS.Objects.Status;
import ERS.Objects.GamePlay.Card;
import ERS.Objects.GamePlay.Player;
import ERS.SFX.Sound;
import ERS.States.Setting;
import ERS.States.State;

public class Game extends Applet implements Runnable {
	private static final long serialVersionUID = 1L;
	public boolean running = false;
	
	int x = 10, y = 10;
	int w = 50, h = 50;
	public static final int WIDTH = 640, HEIGHT = 480;
	int ticks = 0;
	
	public Image backBuffer;
	public static Graphics canvas;
	
	public static ArrayList<Card> pile = new ArrayList<Card>();
	public static Player current = Player.FIRST;
	public static int tries = -1;
	public static Player winner = null;
	public static ArrayList<SlapHand> hands = new ArrayList<SlapHand>();
	
	public static State currentState = State.mainMenu;
	public static int level = 20;
	public static int players = 0;
	public static int count = 0;
	public static int counttwo = 0;
	
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
			canvas.fillRect(0, 0, WIDTH, HEIGHT);

			int xOff = -135, yOff = yFocOff - 10;
			
			canvas.setFont(new Font("Arial", 1, 30));
			canvas.setColor(Color.GRAY);
			canvas.drawString("Click Here to Play!", WIDTH / 2 + 2 + xOff, HEIGHT / 2 + 2 + yOff);
			canvas.setColor(Color.WHITE);
			canvas.drawString("Click Here to Play!", WIDTH / 2 + xOff, HEIGHT / 2 + yOff);
			
			repaint();
		}
	}
	
	public void init(){
		
	}
	
	public static void playCard(){
		int value = 0;

		if(currentState.state == GameState.PICKUPWAITING){
			tryPickup(pickupOwner());
			
			return;
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

		Sound.playRandomFlip();
		
		tries--;
		
		if(value>10){
			tries = value - 10;
			current = Player.getNext();
			currentState.state = GameState.GAME;
		}
		
		if(value == 10 && Setting.tenStops.on){
			currentState.state = GameState.GAME;
			tries = -1;
		}
		
		if(tries==0){
			currentState.state = GameState.PICKUPWAITING;
			current = pickupOwner();
			
			if(current.isHuman)
				Sound.playRandomCollect();
			
			return;
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
	
	public static void tryPickup(Player p){
		if(p.lives < 1){
			if(p.isHuman)
				p.addNotification("No Lives.");
			
			return;
		}
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
	
	public static Player pickupOwner(){
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
	
	public static void burnCard(Player p){
		if(p == null || p.deck == null || pile == null || pile.isEmpty()){
			return;
		}

		if(p.lives < 1)
			return;
		
		if(p.deck.isEmpty() || pile.isEmpty()){
			p.lives--;
			if(currentState == State.inGame){
				p.addNotification("-1 Life", Color.RED);
			}
			
			return;
		}

		Sound.playRandomBurn();
		
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
	
	public static void trySlap(Player p){
		if(p.lives < 1)
			return;
		
		if(!p.isHuman){
			if(!Setting.AISlap.on)
				return;
			
			if(!Setting.AISlapIn.on && p.deck.isEmpty())
				return;
		}
		
		if(isSlap()){
			currentState.state = GameState.GAME;
			current = p;
			p.getPile();
			tries = -1;
			hands.add(p.getSlapHand());
			counttwo = 0;
			count = -10;
			Sound.playRandomSlap();
		}else{
			if(currentState.state == GameState.PICKUPWAITING && pickupOwner() == p){
				tryPickup(p);
			}else{
				burnCard(p);
			}
			
			if(p.deck.size()==0 && current == p){
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
	
	public static boolean isSlap(){
		if(pile.size()>1 && pile.get(pile.size() - 1).value == pile.get(pile.size() - 2).value && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 2).status==Status.INGAME){			
			return true;
		}
		
		if(pile.size()>2 && pile.get(pile.size() - 1).value == pile.get(pile.size() - 3).value && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 3).status==Status.INGAME){
			return true;
		}
		
		if(Setting.addToTen.on){
			if(pile.size()>1 && pile.get(pile.size() - 1).value + pile.get(pile.size() - 2).value == 10 && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 2).status==Status.INGAME){	
				return true;
			}
			
			if(pile.size()>2 && pile.get(pile.size() - 1).value + pile.get(pile.size() - 3).value == 10 && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 3).status==Status.INGAME){
				return true;
			}
		}
		
		if(Setting.kingQueen.on){
			if(pile.size()>1 && pile.get(pile.size() - 1).value == 12 && pile.get(pile.size() - 2).value == 13 && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 2).status==Status.INGAME){
				return true;
			}
			
			if(pile.size()>1 && pile.get(pile.size() - 1).value == 13 && pile.get(pile.size() - 2).value == 12 && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 2).status==Status.INGAME){
				return true;
			}
			
			if(pile.size()>2 && pile.get(pile.size() - 1).value == 13 && pile.get(pile.size() - 3).value == 12 && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 3).status==Status.INGAME){
				return true;
			}
			
			if(pile.size()>2 && pile.get(pile.size() - 1).value == 12 && pile.get(pile.size() - 3).value == 13 && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 3).status==Status.INGAME){
				return true;
			}
		}

		
		if(Setting.sixNine.on){
			if(pile.size()>1 && pile.get(pile.size() - 1).value == 6 && pile.get(pile.size() - 2).value == 9 && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 2).status==Status.INGAME){
				return true;
			}
			
			if(pile.size()>1 && pile.get(pile.size() - 1).value == 9 && pile.get(pile.size() - 2).value == 6 && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 2).status==Status.INGAME){
				return true;
			}
			
			if(pile.size()>2 && pile.get(pile.size() - 1).value == 6 && pile.get(pile.size() - 3).value == 9 && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 3).status==Status.INGAME){
				return true;
			}
			
			if(pile.size()>2 && pile.get(pile.size() - 1).value == 9 && pile.get(pile.size() - 3).value == 6 && pile.get(pile.size() - 1).status==Status.INGAME && pile.get(pile.size() - 3).status==Status.INGAME){
				return true;
			}
		}

		if(Setting.topBottom.on && pile.size() > 3){
			if(pile.size()>1 && pile.get(pile.size() - 1).value == getBottom().value && pile.get(pile.size() - 1).status==Status.INGAME && getBottom().status==Status.INGAME){		
				return true;
			}
		}
		
		return false;
	}
	
	public static Card getBottom(){
		for(int i = 0; i < pile.size(); i++){
			if(pile.get(i).status == Status.INGAME)
				return pile.get(i);
		}
		
		return null;
	}
	
	public static void restartGame(){
		for(Player p: Player.values()){
			pile.clear();
			p.deck.clear();
			p.isOut = false;
		}
		
		newCards();

		getPlayers();
		getLevel();
		
		currentState.state = GameState.GAME;

		current = Player.FIRST;
		
	}
	
	public static void getLevel(){
		if(players < 4){
			try{
				level = (Integer.parseInt(JOptionPane.showInputDialog("What skill level? 1 (Evil) - 4 (Easy)?"))) * 10 - 10;
			}catch(Exception e){level = 3;}
		}
	}
	
	public static void getPlayers(){
		try{
			players = Integer.parseInt(JOptionPane.showInputDialog("How many HUMAN players 1-4? (AI will take any non-human spots)"));
			if(players>4)
				players = 4;
			if(players<0)
				players = 0;
			
		}catch(Exception e){ players = 1; }

		for(int i = 0; i < players; i++){
			Player.getPlayer(i + 1).isHuman = true;
		}
		
		for(int i = players; i < 4; i++){
			Player.getPlayer(i + 1).isHuman = false;
		}
	}
	
	public static void newCards(){
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
	}
	
	public void start(){
		newCards();
		
		this.setSize( WIDTH, HEIGHT );
		
		load();		
		
		running = true;
		
		backBuffer = createImage(Game.WIDTH, Game.HEIGHT);
		canvas = backBuffer.getGraphics();

		this.addKeyListener(new GameKeyListener(this));
		this.addFocusListener(new GameFocusListener(this));
		
		new Thread(this).start();

		this.requestFocus();
	}
	
	public void save(){
		try {
			new Stream().save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void load(){
		try {
			for(String s: new Stream().load()){
				Setting.load(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
