package game;

import static io.Util.concat;

import java.awt.Image;

import config.BulletData;
import config.DataLoader;
import io.Util;
import main.Main;
import networking.Serializable;
import ui.Renderable;
import ui.UI;

public class Bullet implements Renderable, Serializable {
	// HARDCODED VARIABLES
	/**
	 * how far at max each bullet goes
	 */
	private final double COLLISION_STEP = 1.0;

	public Bullet(String name, double initialX, double initialY, double directionDegrees, double dm) {
		this.name = name;
		this.directionDegrees = directionDegrees;

		construct(name, initialX, initialY, directionDegrees, dm);
	}

	private void construct(String name, double initialX, double initialY, double directionDegrees, double dm) {
		BulletData bd = DataLoader.getBulletData(name);
		bulletdata = bd;
		bulletX = initialX;
		bulletY = initialY;
		velocityX = bd.getInitialSpeed() * Math.cos(Math.toRadians(directionDegrees));
		velocityY = -1 * bd.getInitialSpeed() * Math.sin(Math.toRadians(directionDegrees));
		damageMultiplier = dm;
		lifetime = bd.getMaxLifetime();
	}

	// VARIABLES
	private String name;
	private double bulletX;
	private double bulletY;
	private double velocityX;
	private double velocityY;
	private double directionDegrees;

	/**
	 * Counts down from full. Delete at 0. In ticks.
	 */
	private double lifetime;

	private double damageMultiplier;

	private BulletData bulletdata;

	// METHODS

	@Override
	public Image getImg() {
		// TODO Auto-generated method stub
		return bulletdata.getImage();
	}

	@Override
	public int getRenderX() {
		return (int) bulletX;
	}

	@Override
	public int getRenderY() {
		return (int) bulletY;
	}

	@Override
	public int getRenderPriority() {
		if (bulletdata.getWallDrag() == -1)
			return 3;
		else return 6;
	}

	public void update() {
		lifetime--;
		if (lifetime <= 0) {
			Main.GAME.removeBullet(this);
			return;
		}

		double distToCover = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
		while (distToCover > COLLISION_STEP) {
			move(COLLISION_STEP);
			wallCollide();
			distToCover -= COLLISION_STEP;
		}
		if (distToCover > 0) {
			move(distToCover);
			wallCollide();
		}

		objectCollide();
	}

	private void move(double delta) {
		switch (bulletdata.getMovementType()) {
		case "Linear":
			linearMove(delta);
			break;
		case "Homing":
			// TODO
			break;
		case "Sinusoid":
			// TODO
			break;
		case "Controlled":
			// TODO
			break;
		default:
			System.err.println("String error in movement");
			System.exit(-847);
			break;
		}
	}

	private void linearMove(double delta) {
		bulletX += velocityX * delta;
		bulletY += velocityY * delta;
	}

	private void wallCollide() {
		// everyone bounces on the border, so let's handle that first
		// TOP
		if (bulletY - bulletdata.getHitboxRadius() < 0) {
			bulletY = 2 * bulletdata.getHitboxRadius() - bulletY;
			velocityY *= -1;
		}

		// BOTTOM
		if (bulletY + bulletdata.getHitboxRadius() > UI.FIELD_HEIGHT) {
			bulletY = 2 * UI.FIELD_HEIGHT - 2 * bulletdata.getHitboxRadius() - bulletY;
			velocityY *= -1;
		}

		// LEFT
		if (bulletX - bulletdata.getHitboxRadius() < 0) {
			bulletX = 2 * bulletdata.getHitboxRadius() - bulletX;
			velocityX *= -1;
		}

		// RIGHT
		if (bulletX + bulletdata.getHitboxRadius() > UI.FIELD_WIDTH) {
			bulletX = 2 * UI.FIELD_WIDTH - 2 * bulletdata.getHitboxRadius() - bulletX;
			velocityX *= -1;
		}

		if (bulletdata.getRebound() != -1) {// does not pass but bounces

		} else {
			if (bulletdata.getWallDrag() == -1) {// pops on contact with walls
				// TODO
			} else {// passes through walls; calculate speed loss from walls, and if <= 0 kill
					// itself
					// TODO
			}
		}
	}

	private void objectCollide() {
		// TODO
	}

	// Serialization

	@Override
	public byte[] toBytes() {
		return concat(Util.toBytes(name.length()), name.getBytes(), Util.toBytes((int) bulletX),
				Util.toBytes((int) bulletY), Util.toBytes((int) directionDegrees),
				Util.toBytes((int) damageMultiplier));
	}

	public Bullet(byte[] data) {
		int nameLen = Util.toInt(data, 0);
		String name = new String(data, 4, nameLen);

		int bulletX = Util.toInt(data, nameLen + 4);
		int bulletY = Util.toInt(data, nameLen + 8);
		int directionDegrees = Util.toInt(data, nameLen + 12);
		int dm = Util.toInt(data, nameLen + 16);

		construct(name, bulletX, bulletY, directionDegrees, dm);
	}
}
