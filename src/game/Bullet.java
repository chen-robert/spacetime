package game;

import java.awt.Image;

import config.BulletData;
import ui.Renderable;

public class Bullet implements Renderable{
	//HARDCODED VARIABLES
	
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
	}
	
	//VARIABLES
	private double bulletX;
	private double bulletY;
	private double velocityX;
	private double velocityY;
	
	private double damageMultiplier;
	
	private BulletData bulletdata;
	
	//METHODS
	
	public Image getImg() {
		// TODO Auto-generated method stub
		return null;
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

	public void linearMove(double delta) {
		bulletX += velocityX * delta;
		bulletY += velocityY * delta;
	}
}
