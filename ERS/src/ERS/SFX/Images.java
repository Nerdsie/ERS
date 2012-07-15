package ERS.SFX;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class Images {
	public static Image HAND1 = new Images("hand1.png").get();
	public static Image HAND2 = new Images("hand2.png").get();
	public static Image HAND3 = new Images("hand3.png").get();
	public static Image HAND4 = new Images("hand4.png").get();
	public static Image BACKGROUND = new Images("bg.png").get();
	public static Image BACK = new Images("back.png").get();
	public static Image CHECK = new Images("check.png").get();
	public static Image X = new Images("x.png").get();
	public static Image TOP = new Images("top.png").get();
	public static Image INST = new Images("instructions.png").get();
	public static Image NAMES = new Images("names.png").get();
	public static Image STAR = new Images("star.png").get();
	
	String name;
	Image img;
	
	public Images(String n){
		name = n;
		img = getImage(name);
	}
	
	public Images(){
		
	}
	
	public Image get(){
		img = getImage(name);
		return img;
	}
	
	public Image getImage(String name){
		try{
			String loc = "/img/" + name;
			URL url = getClass().getResource(loc);
			
			return Toolkit.getDefaultToolkit().getImage(url);
		}catch(Exception e){ e.printStackTrace(); return null;}
	}
}
