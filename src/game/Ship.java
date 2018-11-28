package game;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import config.CraftData;
import main.Main;
import ui.Renderable;

public class Ship implements Renderable {
	public Ship() {}
	public Ship(CraftData cd) {
		craftdata = cd;
		shipX = 0;
		shipY = 0;
	}
	
	private double shipX;
	private double shipY;
	//these values are the X and Y components of the ship's current velocity vector
	private double velocityX;
	private double velocityY;
	//just something to make my life easier
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
	public int getX() {
		return (int)shipX;
	}
	@Override
	public int getY() {
		return (int)shipY;
	}
	@Override
	public int getRenderPriority() {
		return 3;//can be changed later
	}
	@Override
	public double getDirectionDegrees() {
		return direction;
	}
	@Override
	public double getDirectionRadians() {
		return Math.toRadians(direction);
	}
	
	/**
	 * Uses the inputs from KeyAdapter to update its data
	 */
	public void move() {
		//DOES NOT HAVE SLOWDOWN LOL
		if (Main.KEY_ADAPTER.isKeyPressed(KeyEvent.VK_LEFT))direction += craftdata.getTurnSpeed();
		if (Main.KEY_ADAPTER.isKeyPressed(KeyEvent.VK_RIGHT))direction -= craftdata.getTurnSpeed();
		
		if (Main.KEY_ADAPTER.isKeyPressed(KeyEvent.VK_UP)) {
			velocityX += craftdata.getAcceleration() * Math.cos(getDirectionRadians());
			//- not + as rendering coordinates are annoying >:(
			velocityY -= craftdata.getAcceleration() * Math.sin(getDirectionRadians());
		}
		else if (Main.KEY_ADAPTER.isKeyPressed(KeyEvent.VK_DOWN)) {
			velocityX -= 0.25 * craftdata.getAcceleration() * Math.cos(getDirectionRadians());
			velocityY += 0.25 * craftdata.getAcceleration() * Math.sin(getDirectionRadians());
		}
		
		netvelocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
		
		if ((!Main.KEY_ADAPTER.isKeyPressed(KeyEvent.VK_UP)) && 
			(!Main.KEY_ADAPTER.isKeyPressed(KeyEvent.VK_DOWN)) &&
			netvelocity > 0) {
			double newvelocity = netvelocity - craftdata.getDeceleration();
			if (newvelocity < 0)newvelocity = 0;
			double slowscaling = newvelocity/netvelocity;
			velocityX *= slowscaling;
			velocityY *= slowscaling;
		}
		
		if (netvelocity > craftdata.getMaxSpeed()) {
			double speedscaling = craftdata.getMaxSpeed()/netvelocity;
			velocityX *= speedscaling;
			velocityY *= speedscaling;
		}
		
		shipX += velocityX;
		shipY += velocityY;
	}
}
