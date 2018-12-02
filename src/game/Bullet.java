package game;

import java.awt.Image;

import config.BulletData;
import main.Main;
import ui.Renderable;
import ui.UI;

public class Bullet implements Renderable{
	//HARDCODED VARIABLES
	/**
	 * how far at max each bullet goes
	 */
	private final double COLLISION_STEP = 1.0;
	
	//CONSTRUCTORS
	public Bullet() {
	}
	
	public Bullet(BulletData bd, double initialX, double initialY, double directionDegrees, double dm) {
		bulletdata = bd;
		bulletX = initialX;
		bulletY = initialY;
		velocityX = bd.getInitialSpeed() * Math.cos(Math.toRadians(directionDegrees));
		velocityY = -1 * bd.getInitialSpeed() * Math.sin(Math.toRadians(directionDegrees));
		damageMultiplier = dm;
		lifetime = bd.getMaxLifetime();
	}
	
	//VARIABLES
	private double bulletX;
	private double bulletY;
	private double velocityX;
	private double velocityY;
	
	/**
	 * Counts down from full. Delete at 0. In ticks.
	 */
	private double lifetime;
	
	private double damageMultiplier;
	
	private BulletData bulletdata;
	
	//METHODS
	
	public Image getImg() {
		// TODO Auto-generated method stub
		return bulletdata.getImage();
	}

	@Override
	public int getRenderX() {
		return (int)bulletX;
	}

	@Override
	public int getRenderY() {
		return (int)bulletY;
	}

	@Override
	public int getRenderPriority() {
		// TODO Auto-generated method stub
		return 4;
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
		switch(bulletdata.getMovementType()) {
		case "Linear":
			linearMove(delta);
			break;
		case "Homing":
			//TODO
			break;
		case "Sinusoid":
			//TODO
			break;
		case "Controlled":
			//TODO
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
		//everyone bounces on the border, so let's handle that first
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
		
		if (bulletdata.getRebound() != -1) {//does not pass but bounces
			
		}
		else {
			if (bulletdata.getWallDrag() == -1) {//pops on contact with walls
				//TODO
			}
			else {//passes through walls; calculate speed loss from walls, and if <= 0 kill itself
				//TODO
			}
		}
	}
	
	private void objectCollide() {
		//TODO
	}
}
