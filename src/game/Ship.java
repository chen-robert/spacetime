package game;

import java.awt.Image;
import java.awt.event.KeyEvent;

import config.BackgroundParser;
import config.CraftData;
import main.Main;
import ui.Renderable;
import ui.UI;

public class Ship implements Renderable {
	private static final int COLLISION_ACCURACY = 20;
	private static final double MIN_REBOUND = 0.1;//if traveling slower, no rebound

	public Ship() {
	}

	public Ship(CraftData cd) {
		craftdata = cd;
		shipX = 10;
		shipY = 10;
		hitArray = BackgroundParser.getBackgroundCollisions(480, 360);
	}

	private boolean[][] hitArray;
	private double shipX;
	private double shipY;
	// these values are the X and Y components of the ship's current velocity
	// vector
	private double velocityX;
	private double velocityY;
	// just something to make my life easier
	private double netvelocity;
	/**
	 * 0 direction faces to the right, 90 faces up, goes counterclockwise
	 */
	private double direction;
	private CraftData craftdata;

	@Override
	public Image getImg() {
		return craftdata.getImage();
	}

	@Override
	public int getRenderX() {
		return (int) shipX;
	}

	@Override
	public int getRenderY() {
		return (int) shipY;
	}

	@Override
	public int getRenderPriority() {
		return 3;// can be changed later
	}

	@Override
	public double getDirectionDegrees() {
		return direction;
	}

	@Override
	public double getDirectionRadians() {
		return Math.toRadians(direction);
	}

	public void update() {
		processKeys();
		for (int i = 0; i < COLLISION_ACCURACY; i++) {
			move(1.0 / COLLISION_ACCURACY);
			collide();
		}
	}

	public void move(double delta) {
		shipX += velocityX * delta;
		shipY += velocityY * delta;
	}

	/**
	 * Uses the inputs from KeyAdapter to update its data
	 */
	public void processKeys() {
		if (Main.KEY_ADAPTER.isKeyPressed(KeyEvent.VK_LEFT)) direction += craftdata.getTurnSpeed();
		if (Main.KEY_ADAPTER.isKeyPressed(KeyEvent.VK_RIGHT)) direction -= craftdata.getTurnSpeed();

		if (Main.KEY_ADAPTER.isKeyPressed(KeyEvent.VK_UP)) {
			velocityX += craftdata.getAcceleration() * Math.cos(getDirectionRadians());
			// - not + as rendering coordinates are annoying >:(
			velocityY -= craftdata.getAcceleration() * Math.sin(getDirectionRadians());
		} else if (Main.KEY_ADAPTER.isKeyPressed(KeyEvent.VK_DOWN)) {
			velocityX -= 0.25 * craftdata.getAcceleration() * Math.cos(getDirectionRadians());
			velocityY += 0.25 * craftdata.getAcceleration() * Math.sin(getDirectionRadians());
		}

		netvelocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
		
		//slowdown if no movement
		if ((!Main.KEY_ADAPTER.isKeyPressed(KeyEvent.VK_UP)) && (!Main.KEY_ADAPTER.isKeyPressed(KeyEvent.VK_DOWN))
				&& netvelocity > 0) {
			double newvelocity = netvelocity - craftdata.getDeceleration();
			if (newvelocity < 0) newvelocity = 0;
			double slowscaling = newvelocity / netvelocity;
			velocityX *= slowscaling;
			velocityY *= slowscaling;
		}

		if (netvelocity > craftdata.getMaxSpeed()) {
			double speedscaling = craftdata.getMaxSpeed() / netvelocity;
			velocityX *= speedscaling;
			velocityY *= speedscaling;
		}

	}

	public void collide() {
		// TOP
		if (shipY - craftdata.getHitboxRadius() < 0) {
			shipY = 2 * craftdata.getHitboxRadius() - shipY;
			velocityX *= craftdata.getRebound();
			velocityY *= -1 * craftdata.getRebound();
		}

		// BOTTOM
		if (shipY + craftdata.getHitboxRadius() > UI.FIELD_HEIGHT) {
			shipY = 2 * UI.FIELD_HEIGHT - 2 * craftdata.getHitboxRadius() - shipY;
			velocityX *= craftdata.getRebound();
			velocityY *= -1 * craftdata.getRebound();
		}

		// LEFT
		if (shipX - craftdata.getHitboxRadius() < 0) {
			shipX = 2 * craftdata.getHitboxRadius() - shipX;
			velocityX *= -1 * craftdata.getRebound();
			velocityY *= craftdata.getRebound();
		}

		// RIGHT
		if (shipX + craftdata.getHitboxRadius() > UI.FIELD_WIDTH) {
			shipX = 2 * UI.FIELD_WIDTH - 2 * craftdata.getHitboxRadius() - shipX;
			velocityX *= -1 * craftdata.getRebound();
			velocityY *= craftdata.getRebound();
		}

		boolean foundCollision = false;
		for (int angleIterator = 0; angleIterator < 8; angleIterator++) {
			double testX = shipX + craftdata.getHitboxRadius() * Math.cos(Math.PI / 4 * angleIterator);
			double testY = shipY - craftdata.getHitboxRadius() * Math.sin(Math.PI / 4 * angleIterator);

			if (hitArray[(int) testX][(int) testY]) {
				foundCollision = true;
			}
		}

		// if this screws up, fix the heuristic accordingly
		if (foundCollision) {
			int numCollides = 0;
			boolean[] hasCollides = new boolean[8];
			for (int angleIterator = 0; angleIterator < 8; angleIterator++) {
				double testX = shipX + craftdata.getHitboxRadius() * Math.cos(Math.PI / 4 * angleIterator);
				double testY = shipY - craftdata.getHitboxRadius() * Math.sin(Math.PI / 4 * angleIterator);
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
				for (int i = 0; i < 4; i++) {
					if (hasCollides[2 * i + 1]) reflectEstimateD = 45 + 90 * i;
				}
				if (reflectEstimateD == -1) {
				*/
				double suma = 0;
				for (int j = 0; j < 8; j++)
					if (hasCollides[j])suma += j;
				suma /= 2;
				reflectEstimateD = 45 * suma;
				//}
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
						if (hasCollides[j])sum += j;
					sum /= 3;
					reflectEstimateD = 45 * sum;
				}
				break;
			default:
				double sum = 0;
				for (int j = 0; j < 8; j++)
					if (hasCollides[j])sum += j;
				sum /= numCollides;
				reflectEstimateD = 45 * sum;
				break;
			}
			double reflectEstimateR = Math.toRadians(reflectEstimateD);
			
			move(0.0 - 1.0/COLLISION_ACCURACY);

			double prevVelocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
			if (Math.abs(velocityX) < 0.2) velocityX = 0;
			if (Math.abs(velocityY) < 0.2) velocityY = 0;
			double currentAngle = Math.atan2(0 - velocityY, velocityX);
			if (currentAngle < 0) currentAngle += 2 * Math.PI;
			currentAngle = 2 * reflectEstimateR + Math.PI - currentAngle;
			
			
			velocityX = prevVelocity * craftdata.getRebound() * Math.cos(currentAngle);
			velocityY = -1 * prevVelocity * craftdata.getRebound() * Math.sin(currentAngle);
			//if the ship is too slow to rebound, stop it
			if (prevVelocity * craftdata.getRebound() < MIN_REBOUND) {
				velocityX = 0;
				velocityY = 0;
			}
		}
	}
}
