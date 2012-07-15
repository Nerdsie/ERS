package ERS.SFX;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.Random;

import ERS.States.Setting;

public class Sound {
	public static Sound SLAP1 = new Sound("slap0.wav");
	public static Sound SLAP2 = new Sound("slap1.wav");
	public static Sound SLAP3 = new Sound("slap2.wav");
	public static Sound FLIP1 = new Sound("flip0.wav");
	public static Sound FLIP2 = new Sound("flip1.wav");
	public static Sound FLIP3 = new Sound("flip2.wav");
	public static Sound COLLECT1 = new Sound("collect0.wav");
	public static Sound COLLECT2 = new Sound("collect1.wav");
	public static Sound COLLECT3 = new Sound("collect2.wav");
	public static Sound BURN1 = new Sound("burn0.wav");
	public static Sound BURN2 = new Sound("burn1.wav");
	public static Sound BURN3 = new Sound("burn2.wav");
	
	public AudioClip clip;

	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource("/sound/" + name));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void play() {
		if(!Setting.sound.on)
			return;
			
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static Sound randomFlip(){
		int x = new Random().nextInt(3);
		
		if(x==0)
			return FLIP1;
		
		if(x==1)
			return FLIP2;
		
		return FLIP3;
	}
	
	public static Sound randomSlap(){
		int x = new Random().nextInt(3);
		
		if(x==0)
			return SLAP1;
		
		if(x==1)
			return SLAP2;
		
		return SLAP3;
		
	}
	
	public static Sound randomCollect(){
		int x = new Random().nextInt(3);
		
		if(x==0)
			return COLLECT1;
		
		if(x==1)
			return COLLECT2;
		
		return COLLECT3;
		
	}
	
	public static Sound randomBurn(){
		int x = new Random().nextInt(3);
		
		if(x==0)
			return BURN1;
		
		if(x==1)
			return BURN2;
		
		return BURN3;
		
	}

	public static void playRandomSlap(){
		randomSlap().play();
	}
	
	public static void playRandomFlip(){
		randomFlip().play();
	}
	
	public static void playRandomCollect(){
		randomCollect().play();
	}
	
	public static void playRandomBurn(){
		randomBurn().play();
	}
}
