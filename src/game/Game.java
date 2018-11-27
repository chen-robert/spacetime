package game;

import java.util.ArrayList;
import java.util.Collection;

import config.CraftDataImpl;
import config.ImageLoader;
import ui.Renderable;

public class Game {
	/**
	 * Used to update the game state. Game logic goes here.
	 */
	
	ImageLoader imageloader;
	
	ArrayList<Renderable> renderables;
	Ship playerShip;
	
	public Game() {
		imageloader = new ImageLoader();
		renderables = new ArrayList<Renderable>();
		playerShip = new Ship(new CraftDataImpl());
		renderables.add(playerShip);
	}
	
	public void update() {
		/*
		 * We begin to process movement
		 */
		playerShip.move();
	}

	public Collection<Renderable> getRenderables() {
		return renderables;
	}
}
