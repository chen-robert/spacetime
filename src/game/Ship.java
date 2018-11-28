package game;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import config.BackgroundParser;
import config.CraftData;
import main.Main;
import ui.Renderable;
import ui.UI;

public class Ship implements Renderable {
	public Ship() {}
	public Ship(CraftData cd) {
		craftdata = cd;
		shipX = 10;
		shipY = 10;
		hitArray = BackgroundParser.getBackgroundCollisions(480, 360);
	}
	private boolean[][] hitArray;
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
	public int getRenderX() {
		return (int)shipX;
	}
	@Override
	public int getRenderY() {
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
	
	//TODO: Once we've detected collisions, actually do something with them
	public void collide() {
		//TOP
		if (shipY - craftdata.getHitboxRadius() < 0) {
			shipY = 2 * craftdata.getHitboxRadius() - shipY;
			velocityX *= craftdata.getRebound();
			velocityY *= -1 * craftdata.getRebound();
		}
		
		//BOTTOM
		if (shipY + craftdata.getHitboxRadius() > UI.FIELD_HEIGHT) {
			
			shipY = 2 * UI.FIELD_HEIGHT - 2 * craftdata.getHitboxRadius() - shipY;
			velocityX *= craftdata.getRebound();
			velocityY *= -1 * craftdata.getRebound();
		}
		
		//LEFT
		if (shipX - craftdata.getHitboxRadius() < 0) {
			shipX = 2 * craftdata.getHitboxRadius() - shipX;
			velocityX *= -1 * craftdata.getRebound();
			velocityY *= craftdata.getRebound();
		}
		
		//RIGHT
		if (shipX + craftdata.getHitboxRadius() > UI.FIELD_WIDTH) {
			shipX = 2 * UI.FIELD_WIDTH - 2 * craftdata.getHitboxRadius() - shipX;
			velocityX *= -1 * craftdata.getRebound();
			velocityY *= craftdata.getRebound();
		}
		
		
		
		boolean foundCollision = false;
		for (int angleIterator = 0; angleIterator < 30; angleIterator++) {
			double testX = shipX + craftdata.getHitboxRadius() * Math.cos(12 * angleIterator);
			double testY = shipY - craftdata.getHitboxRadius() * Math.sin(12 * angleIterator);
			if (hitArray[(int)testX][(int)testY]) {
				foundCollision = true;
				break;
			}
		}
		
		if (foundCollision) {
			//we know there is a collision; now we put algorithm into action!
			int count = 0;//there had better be at least one considering we found one -_-
			int anglesum = 0;
			for (int angleIterator = 0; angleIterator < 60; angleIterator++) {
				for (int radiusShift = 0; radiusShift < 5; radiusShift++) {
					if (hitArray
							[(int)(shipX + (craftdata.getHitboxRadius() + 0.5 * radiusShift) * 
							Math.cos(6 * angleIterator))]
							[(int)(shipY + (craftdata.getHitboxRadius() - 0.5 * radiusShift) * 
							Math.sin(6 * angleIterator))]) {//holy crap that was hard
						count++;
						//System.out.println(angleIterator + " " + radiusShift);
						anglesum += 6 * angleIterator;
					}
				}
			}
			double reflectEstimate = anglesum/count;
		}
	}
}
