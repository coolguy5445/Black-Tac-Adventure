package game.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
	public boolean[] keyStates = new boolean[250];
	public static Keyboard keyboard = new Keyboard();
	
	private Keyboard() {
		for (int i = 0; i < keyStates.length; ++i) keyStates[i] = false;
	}
    
	public void keyTyped(KeyEvent e) {}
    
	public void keyPressed(KeyEvent e) {
		keyStates[e.getKeyCode()] = true;
	}
    
	public void keyReleased(KeyEvent e) {
		keyStates[e.getKeyCode()] = false;
	}
}
