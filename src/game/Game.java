package game;

import java.util.ArrayList;
import java.util.Collection;

import ui.Renderable;

public class Game {
	/**
	 * Used to update the game state. Game logic goes here.
	 */
	
	ArrayList<Renderable> renderables;
	
	public Game() {
		renderables = new ArrayList<Renderable>();
	}
	
	public void update() {
		// lets do something! ~Alex
		
		//extend things into renderables... god damn it Robert
		//note that the order of drawing matters
	}

	public Collection<Renderable> getRenderables() {
		return new ArrayList<>();
	}
}
