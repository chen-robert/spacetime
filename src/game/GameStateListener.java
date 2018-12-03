package game;

import networking.Serializable;

public interface GameStateListener {
	public void addObject(Serializable obj);

	public void bind(Game g);
}
