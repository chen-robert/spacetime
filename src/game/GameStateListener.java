package game;

import networking.Serializable;

public interface GameStateListener {
	/**
	 * Adds a object.
	 * 
	 * @param obj
	 */
	public void addObject(Serializable obj);

	/**
	 * Binds a {@link Game} object to listen to updates from the listener.
	 * 
	 * @param game
	 */
	public void bind(Game game);
}
