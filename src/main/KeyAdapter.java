package main;

import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class KeyAdapter extends java.awt.event.KeyAdapter {
	private final Set<Integer> keys = Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());

	@Override
	public void keyPressed(KeyEvent e) {
		keys.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys.remove(e.getKeyCode());
	}

	/**
	 * Returns whether or not a key is pressed. Note: Use {@link KeyEvent} codes.
	 * For example, to detect if 'a' is pressed, use {@link KeyEvent#VK_A}.
	 * 
	 * @param code an integer code.
	 * @return if the key is pressed down
	 */
	public boolean isKeyPressed(int code) {
		return keys.contains(code);
	}

}
