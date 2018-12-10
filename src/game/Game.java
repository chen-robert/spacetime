package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import config.BackgroundParser;
import config.CraftDataImpl;
import config.ImageLoader;
import networking.Serializable;
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

	public Game(GameStateListener gsl) {
		imageloader = new ImageLoader();
		miscRenderables = new ArrayList<Renderable>();
		playerShip = new Ship(new CraftDataImpl());
		bullets = new ArrayList<Bullet>();
		miscRenderables.add(playerShip);
		miscRenderables.add(BackgroundParser.getBackgroundSprite(480, 360));

		this.gsl = gsl;
		gsl.bind(this);
	}

	/**
	 * Generic "fake add" method where we call
	 * {@link GameStateListener#addObject(networking.Serializable)}.
	 * 
	 * @param obj any serializable object
	 */
	public void add(Serializable obj) {
		gsl.addObject(obj);
	}

	/**
	 * Add a bullet. This will be called by {@link GameStateListener} through
	 * reflection.
	 * 
	 * Note, ensure the method name is strictly "add" + className. For example, if
	 * we wanted to make a Powerup class, our new method would be called addPowerup.
	 * 
	 * @param b
	 */
	public void addBullet(Bullet b) {
		synchronized (bullets) {
			bullets.add(b);
		}
	}

	public void addOtherShip(OtherShip s) {
		if (s.getId().equals(Ship.ID)) return;

		synchronized (miscRenderables) {
			Collection<Renderable> dupes = miscRenderables.stream()
					.filter(r -> r instanceof OtherShip && ((OtherShip) r).getId().equals(s.getId()))
					.collect(Collectors.toList());
			dupes.forEach(dupe -> miscRenderables.remove(dupe));
			miscRenderables.add(s);
		}
	}

	// UGH
	public void removeBullet(Bullet b) {
		toRemove.add(b);
	}

	public void update() {
		playerShip.update();
		add(playerShip.getOtherShip());

		synchronized (bullets) {
			for (Bullet bullet : bullets)
				bullet.update();
		}

		cleanup();
	}

	private void cleanup() {
		synchronized (bullets) {
			for (Bullet bullet : toRemove)
				bullets.remove(bullet);
		}
		toRemove.clear();
	}

	public Collection<Renderable> getRenderables() {
		ArrayList<Renderable> ret = new ArrayList<Renderable>();
		ret.addAll(miscRenderables);
		ret.addAll(bullets);
		return ret;
	}
}
