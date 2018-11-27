package main;

public interface KeyListener {
	/**
	 * Fired when a key char is pressed
	 * 
	 * @param c char pressed
	 */
	public void keyPressed(char c);

	/**
	 * Fired when a key char is released
	 * 
	 * @param c char released
	 */
	public void keyReleased(char c);
}
