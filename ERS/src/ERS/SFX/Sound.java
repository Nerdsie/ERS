package ERS.SFX;

import java.io.File;
import java.net.URISyntaxException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Sound {
	public static Sound SLAP = new Sound("slap.wav");
	public static Sound CARD_PLAY = new Sound("card_play.wav");
	
	Clip clip;
	
	public Sound(String audioFile){
		try {
			init(new File(getClass().getResource("/res/sound/" + audioFile).toURI()));
		} catch (URISyntaxException e) {}
	}
	
	private void init(File audioFile){
		try {
			AudioInputStream soundStream = AudioSystem.getAudioInputStream( audioFile );
			AudioFormat audioFormat = soundStream.getFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(
					Clip.class, AudioSystem.getTargetFormats(
							AudioFormat.Encoding.PCM_SIGNED, audioFormat ),
							audioFormat.getFrameSize(),
							audioFormat.getFrameSize() * 2 );
			if ( !AudioSystem.isLineSupported( dataLineInfo ) ) {
				System.err.println( "Unsupported Clip File!" );
				return;
			}
			clip = ( Clip ) AudioSystem.getLine( dataLineInfo );
			clip.open( soundStream );
		}
		catch ( Exception e) {
		}
	}
	
	public void play(){
		clip.start();
	}
}
