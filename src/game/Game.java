package game;

import java.util.ArrayList;
import java.util.Collection;

import config.BackgroundParser;
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
		renderables.add(BackgroundParser.getBackgroundSprite(480, 360));
	}
	
	public void update() {
		//move everything
		playerShip.move();
		
		//process collisions for everything
		playerShip.collide();
	}

	public Collection<Renderable> getRenderables() {
		return renderables;
	}
}
