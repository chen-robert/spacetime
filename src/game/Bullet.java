package game;

import static io.Util.concat;

import java.awt.Image;
import java.util.ArrayList;

import config.BackgroundParser;
import config.BulletData;
import config.DataLoader;
import io.Util;
import main.Main;
import networking.SerializableObject;
import ui.Renderable;
import ui.UI;

public class Bullet extends SerializableObject implements Renderable {
	// HARDCODED VARIABLES
	/**
	 * how far at max each bullet goes
	 */
	private final double COLLISION_STEP = 1.0;
	/**
	 * the minimum speed a bullet can go to rebound
	 */
	private final double MIN_REBOUND = 0.2;

	public Bullet(String name, double initialX, double initialY, double initDirDeg, double dm) {
		construct(name, initialX, initialY, initDirDeg, dm);
	}

	private void construct(String name, double initialX, double initialY, double initDirDeg, double dm) {
		this.name = name;
		directionDegrees = initDirDeg;
		wbtick = -20;

		BulletData bd = DataLoader.getBulletData(name);
		bulletdata = bd;
		bulletX = initialX;
		bulletY = initialY;
		velocityX = bd.getInitialSpeed() * Math.cos(Math.toRadians(initDirDeg));
		velocityY = -1 * bd.getInitialSpeed() * Math.sin(Math.toRadians(initDirDeg));
		maxSpeed = bd.getMaxSpeed();
		damageMultiplier = dm;
		lifetime = bd.getMaxLifetime();
	}

	// VARIABLES
	private String name;
	private double bulletX;
	private double bulletY;
	private double velocityX;
	private double velocityY;
	private double maxSpeed;
	private double directionDegrees;

	/**
	 * Counts down from full. Delete at 0. In ticks.
	 */
	private double lifetime;
	/**
	 * Counts up from 0. Resets upon wallbounce
	 */
	private double wbtick;

	private double damageMultiplier;

	private BulletData bulletdata;

	// METHODS

	@Override
	public Image getImg() {
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
		else
			return 6;
	}

	public void update() {
		lifetime--;
		wbtick++;
		if (lifetime <= 0) {
			Main.GAME.removeBullet(this);
			return;
		}

		double distToCover = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
		while (distToCover > COLLISION_STEP) {
			move(COLLISION_STEP);
			wallCollide(COLLISION_STEP);
			distToCover -= COLLISION_STEP;
		}
		if (distToCover > 0) {
			move(distToCover);
			wallCollide(distToCover);
		}

		objectCollide();
	}

	private void move(double delta) {
		switch (bulletdata.getMovementType()) {
		case "Linear":
			linearMove(delta);
			break;
		case "Homing":
			homingMove(delta);
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

	private void homingMove(double delta) {
		if (wbtick < 5) {
			linearMove(delta);
			return;
		}

		ArrayList<OtherShip> targets = new ArrayList<OtherShip>();
		targets.addAll(Main.GAME.getOtherShips());
		targets.add(Main.GAME.getShip().getOtherShip());

		int bestgot = -1;
		double deviation = 999;
		for (int i = 0; i < targets.size(); i++) {
			// in rads
			double currentAngle = Math.atan2(bulletY - targets.get(i).getY(), targets.get(i).getX() - bulletX);
			if (currentAngle < 0) currentAngle += 2 * Math.PI;
			while (directionDegrees < 0)
				directionDegrees += 360;
			while (directionDegrees >= 360)
				directionDegrees -= 360;
			double directionRadians = Math.toRadians(directionDegrees);
			double currentDev = Math.abs(directionRadians - currentAngle);
			if (currentDev < deviation) {
				bestgot = i;
				deviation = currentDev;
			}
			currentDev = Math.abs(2 * Math.PI + directionRadians - currentAngle);
			if (currentDev < deviation) {
				bestgot = i;
				deviation = currentDev;
			}
		}

		double optimalAngle = Math.atan2(bulletY - targets.get(bestgot).getY(), targets.get(bestgot).getX() - bulletX);
		if (optimalAngle < 0) optimalAngle += 2 * Math.PI;
		double directionRadians = Math.toRadians(directionDegrees);
		double distToTarget = Math.hypot(bulletX - targets.get(bestgot).getX(), bulletY - targets.get(bestgot).getY());
		double turnamount = 3.0 + 20.0 / (distToTarget / maxSpeed);
		if (optimalAngle - directionRadians > Math.PI
				|| (optimalAngle < directionRadians && directionRadians - optimalAngle < Math.PI)) {
			// optimal is to turn to the right
			if (Math.toDegrees(deviation) < turnamount)
				directionDegrees = Math.toDegrees(optimalAngle);
			else
				directionDegrees -= turnamount;
		} else {
			if (Math.toDegrees(deviation) < turnamount)
				directionDegrees = Math.toDegrees(optimalAngle);
			else
				directionDegrees += turnamount;
		}
		directionRadians = Math.toRadians(directionDegrees);
		velocityX = Math.cos(directionRadians) * maxSpeed * (1.0 - 0.4 * deviation / Math.PI);
		velocityY = -1 * Math.sin(directionRadians) * maxSpeed * (1.0 - 0.4 * deviation / Math.PI);
		// if (deviation > Math.PI/10 && distToTarget < 20 * maxSpeed) {
		// velocityX *= (1.0 - 0.4 * deviation/Math.PI);
		// velocityY *= (1.0 - 0.4 * deviation/Math.PI);
		// }
		linearMove(delta);
	}

	private void velocityUpdatesDirDeg() {
		if (Math.abs(velocityX) < 0.001) velocityX = 0;
		if (Math.abs(velocityY) < 0.001) velocityY = 0;
		directionDegrees = Math.toDegrees(Math.atan2(0 - velocityY, velocityX));
	}

	private void wallCollide(double adjustment) {
		// everyone bounces on the border, so let's handle that first
		// TOP
		if (bulletY - bulletdata.getHitboxRadius() < 0) {
			bulletY = 2 * bulletdata.getHitboxRadius() - bulletY;
			velocityY *= -1;
			velocityUpdatesDirDeg();
			wbtick = 0;
		}

		// BOTTOM
		if (bulletY + bulletdata.getHitboxRadius() > (UI.FIELD_HEIGHT - 1)) {
			bulletY = 2 * (UI.FIELD_HEIGHT - 1) - 2 * bulletdata.getHitboxRadius() - bulletY;
			velocityY *= -1;
			velocityUpdatesDirDeg();
			wbtick = 0;
		}

		// LEFT
		if (bulletX - bulletdata.getHitboxRadius() < 0) {
			bulletX = 2 * bulletdata.getHitboxRadius() - bulletX;
			velocityX *= -1;
			velocityUpdatesDirDeg();
			wbtick = 0;
		}

		// RIGHT
		if (bulletX + bulletdata.getHitboxRadius() > (UI.FIELD_WIDTH - 1)) {
			bulletX = 2 * (UI.FIELD_WIDTH - 1) - 2 * bulletdata.getHitboxRadius() - bulletX;
			velocityX *= -1;
			velocityUpdatesDirDeg();
			wbtick = 0;
		}

		// colliding with the walls
		boolean[][] hitArray = BackgroundParser.getBackgroundCollisions(480, 360);
		boolean foundCollision = false;
		for (int angleIterator = 0; angleIterator < 8; angleIterator++) {
			double testX = bulletX + bulletdata.getHitboxRadius() * Math.cos(Math.PI / 4 * angleIterator);
			double testY = bulletY - bulletdata.getHitboxRadius() * Math.sin(Math.PI / 4 * angleIterator);

			if (hitArray[(int) testX][(int) testY]) {
				foundCollision = true;
			}
		}

		if (foundCollision) {
			wbtick = 0;
			if (bulletdata.getRebound() != -1) {// does not pass but bounces
				// if this screws up, fix the heuristic accordingly
				int numCollides = 0;
				boolean[] hasCollides = new boolean[8];
				for (int angleIterator = 0; angleIterator < 8; angleIterator++) {
					double testX = bulletX + bulletdata.getHitboxRadius() * Math.cos(Math.PI / 4 * angleIterator);
					double testY = bulletY - bulletdata.getHitboxRadius() * Math.sin(Math.PI / 4 * angleIterator);
					if (hitArray[(int) testX][(int) testY]) {
						hasCollides[angleIterator] = true;
						numCollides++;
					}
				}

				double reflectEstimateD = -1;
				switch (numCollides) {
				case 0:
					System.exit(-69);
					break;
				case 1:
					for (int i = 0; i < 8; i++) {
						if (hasCollides[i]) reflectEstimateD = 45 * i;
					}
					if (reflectEstimateD == -1) {
						System.out.println("what the actual heccc");
						System.exit(-6969);
					}
					break;
				case 2:
					/*
					 * for (int i = 0; i < 4; i++) { if (hasCollides[2 * i + 1]) reflectEstimateD =
					 * 45 + 90 * i; } if (reflectEstimateD == -1) {
					 */
					double suma = 0;
					for (int j = 0; j < 8; j++)
						if (hasCollides[j]) suma += j;
					suma /= 2;
					reflectEstimateD = 45 * suma;
					// }
					break;
				case 3:
					for (int i = 0; i < 8; i++) {
						if (hasCollides[(i + 7) % 8] && hasCollides[i] && hasCollides[(i + 1) % 8]) {
							reflectEstimateD = 45 * i;
						}
					}
					if (reflectEstimateD == -1) {
						double sum = 0;
						for (int j = 0; j < 8; j++)
							if (hasCollides[j]) sum += j;
						sum /= 3;
						reflectEstimateD = 45 * sum;
					}
					break;
				default:
					double sum = 0;
					for (int j = 0; j < 8; j++)
						if (hasCollides[j]) sum += j;
					sum /= numCollides;
					reflectEstimateD = 45 * sum;
					break;
				}
				double reflectEstimateR = Math.toRadians(reflectEstimateD);

				move(-1.0 * adjustment);

				double prevVelocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
				if (Math.abs(velocityX) < 0.05) velocityX = 0;
				if (Math.abs(velocityY) < 0.05) velocityY = 0;
				double currentAngle = Math.atan2(0 - velocityY, velocityX);
				if (currentAngle < 0) currentAngle += 2 * Math.PI;
				currentAngle = 2 * reflectEstimateR + Math.PI - currentAngle;
				directionDegrees = Math.toDegrees(currentAngle);

				velocityX = prevVelocity * bulletdata.getRebound() * Math.cos(currentAngle);
				velocityY = -1 * prevVelocity * bulletdata.getRebound() * Math.sin(currentAngle);

				// if the BULLET is too slow to rebound, KILL it
				if (prevVelocity * bulletdata.getRebound() < MIN_REBOUND) {
					Main.GAME.removeBullet(this);
					return;
				}
			} else {
				if (bulletdata.getWallDrag() == -1) {// pops on contact with walls
					Main.GAME.removeBullet(this);
					return;
				} else {// passes through walls; calculate speed loss from walls,
						// and if <= 0 kill itself
					double prevVelocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
					if (prevVelocity < bulletdata.getWallDrag()) {// too slow to continue
						Main.GAME.removeBullet(this);
						return;
					} else {
						double adjuster = (prevVelocity - bulletdata.getWallDrag()) / prevVelocity;
						velocityX *= adjuster;
						velocityY *= adjuster;
					}
				}
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
