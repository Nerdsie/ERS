package ERS.SFX;

import java.awt.Color;
import java.awt.FontMetrics;

import ERS.Game;

public class TextFX {
	public static int getSW(String s){
		FontMetrics FM = Game.canvas.getFontMetrics(Game.canvas.getFont());
		return FM.stringWidth(s);
	}
	
	public static void drawCString(String m, int y){
		Game.canvas.drawString(m, Game.WIDTH / 2 - getSW(m) / 2, y);
	}
	
	public static void drawCString(String m, int x, int y){
		Game.canvas.drawString(m, Game.WIDTH / 2 - getSW(m) / 2 + x, y);
	}
	
	public static void drawC3DString(String m, int y){
		Game.canvas.setColor(Color.GRAY);
		drawCString(m, 2, y + 2);
		Game.canvas.setColor(Color.WHITE);
		drawCString(m, y);
	}

	public static void drawC3DString(String m, Color f, int y, int sep){
		Game.canvas.setColor(f.darker().darker().darker());
		drawCString(m, sep, y + sep);
		Game.canvas.setColor(f);
		drawCString(m, y);
	}

	public static void drawC3DString(String m, Color f, Color b, int y, int sep){
		Game.canvas.setColor(b);
		drawCString(m, sep, y + sep);
		Game.canvas.setColor(f);
		drawCString(m, y);
	}

	public static void draw3DString(String m, int x, int y, int sep){
		Game.canvas.setColor(Color.GRAY);
		Game.canvas.drawString(m, x + sep, y + sep);
		Game.canvas.setColor(Color.WHITE);
		Game.canvas.drawString(m, x, y);
	}

	public static void draw3DString(String m, Color f, int x, int y, int sep){
		Game.canvas.setColor(f.darker().darker().darker());
		Game.canvas.drawString(m, x + sep, y + sep);
		Game.canvas.setColor(f);
		Game.canvas.drawString(m, x, y);
	}

	public static void draw3DString(String m, Color f, Color b, int x, int y, int sep){
		Game.canvas.setColor(b);
		Game.canvas.drawString(m, x + sep, y + sep);
		Game.canvas.setColor(f);
		Game.canvas.drawString(m, x, y);
	}
}
