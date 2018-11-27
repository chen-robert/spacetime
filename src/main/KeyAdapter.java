package main;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;

public class KeyAdapter extends java.awt.event.KeyAdapter {
	/**
	 * Collection of all listeners to fire events to
	 */
	private final Collection<KeyListener> listeners = new ArrayList<>();

	@Override
	public void keyPressed(KeyEvent e) {
		synchronized (listeners) {
			for (KeyListener listener : listeners) {
				listener.keyPressed(e.getKeyChar());
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		synchronized (listeners) {
			for (KeyListener listener : listeners) {
				listener.keyReleased(e.getKeyChar());
			}
		}
	}

	public void removeListener(KeyListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	public void addListener(KeyListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
}
