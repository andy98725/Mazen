package util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import main.App;

public class KeyInput implements KeyListener {

	ArrayList<Integer> activeKeycodes = new ArrayList<Integer>();

	// Do any keys held down
	public void forceInput() {
		// Press each keycode
		synchronized (activeKeycodes) {
			for (int i = 0; i < activeKeycodes.size(); i++) {
				App.disp.keyPressed(activeKeycodes.get(i));
			}
		}
	}

	// Clear buffer
	public void clearBuffer() {
		activeKeycodes.clear();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		synchronized (activeKeycodes) {
			// Add to active codes
			activeKeycodes.add(e.getKeyCode());
			// Press
			App.disp.keyPressed(e.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		synchronized (activeKeycodes) {
			// Check if keycode
			int index = activeKeycodes.indexOf(e.getKeyCode());
			// If found, remove
			if (index != -1) {
				activeKeycodes.remove(index);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Unused
	}

}
