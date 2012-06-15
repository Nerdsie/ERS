package ERS.SFX;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class Images {
	public static Images HAND1 = new Images("hand1.png");
	public static Images HAND2 = new Images("hand2.png");
	public static Images HAND3 = new Images("hand3.png");
	public static Images HAND4 = new Images("hand4.png");
	public static Images BACKGROUND = new Images("bg.png");
	public static Images BACK = new Images("back.png");
	public static Images CHECK = new Images("check.png");
	public static Images X = new Images("x.png");
	public static Images TOP = new Images("top.png");
	public static Images INST = new Images("instructions.png");
	public static Images NAMES = new Images("names.png");
	public static Images STAR = new Images("star.png");
	
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
		}catch(Exception e){ return null;}
	}
}
