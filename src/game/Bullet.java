package game;

import java.awt.Image;

import config.BulletData;
import ui.Renderable;

public class Bullet implements Renderable{
	//HARDCODED VARIABLES
	
	//CONSTRUCTORS
	public Bullet() {
	}
	
	public Bullet(BulletData bd) {
		bulletdata = bd;
		bulletX = bd.getInitialX();
		bulletY = bd.getInitialY();
		velocityX = bd.getInitialSpeed() * Math.cos(bd.getInitialDirection());
		velocityY = -1 * bd.getInitialSpeed() * Math.sin(bd.getInitialDirection());
	}
	
	//VARIABLES
	private double bulletX;
	private double bulletY;
	private double velocityX;
	private double velocityY;
	
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

}
