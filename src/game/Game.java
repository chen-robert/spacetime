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

	private ArrayList<Renderable> miscRenderables;

	private Ship playerShip;
	private ArrayList<Bullet> bullets;
	private ArrayList<Bullet> toRemove = new ArrayList<>();

	private GameStateListener gsl;

	public Game() {
		imageloader = new ImageLoader();
		miscRenderables = new ArrayList<Renderable>();
		playerShip = new Ship(new CraftDataImpl());
		bullets = new ArrayList<Bullet>();
		miscRenderables.add(playerShip);
		miscRenderables.add(BackgroundParser.getBackgroundSprite(480, 360));
	}

	// ugh
	public void addBullet(Bullet b) {
		bullets.add(b);
	}

	// UGH
	public void removeBullet(Bullet b) {
		toRemove.add(b);
	}

	public void update() {
		playerShip.update();
		for (Bullet bullet : bullets)
			bullet.update();

		cleanup();
	}

	private void cleanup() {
		for (Bullet bullet : toRemove)
			bullets.remove(bullet);
		toRemove.clear();
	}

	public Collection<Renderable> getRenderables() {
		ArrayList<Renderable> ret = new ArrayList<Renderable>();
		ret.addAll(miscRenderables);
		ret.addAll(bullets);
		return ret;
	}
}
