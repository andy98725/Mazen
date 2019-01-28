package util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.App;

public class KeyInput implements KeyListener {



	@Override
	public void keyPressed(KeyEvent e) {
		App.disp.keyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Unused
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Unused
	}

}
