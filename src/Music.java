package game.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

public class Music {
    
    private Clip clip;
    
    public Music(String path) {
        load(path);
    }
    
    private void load(String path) {
        try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(SoundEffect.class.getResource(path));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
    }
    
    public void start() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    public void stop() {
        clip.stop();
        clip.setFramePosition(0);
    }
}
