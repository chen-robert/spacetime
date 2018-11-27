package config;

import java.awt.image.BufferedImage;

public interface CraftData {
	String getName();
	
	/**
	 * @return the image of said craft
	 */
	BufferedImage getImage();
	
	/**
	 * @return radius of the circular hitbox
	 */
	double getHitboxRadius();
	/**
	 * @return a double between 0 and 1, the fraction of energy returned when colliding
	 */
	double getRebound();
	/**
	 * @return acceleration speed
	 */
	double getAcceleration();
	/**
	 * @return maximum speed for the ship; on each tick, acceleration vectors are added
	 * and THEN if they surpass the maximum speed, are scaled down accordingly
	 */
	double getMaxSpeed();
	/**
	 * @return the number of DEGREES that the ship can turn in either direction each tick
	 */
	double getTurnSpeed();
	
	/**
	 * @return number of hitpoints the ship spawns with and can have at max
	 */
	int getMaxHP();
	/**
	 * @return amount of base damage reduction the ship has (dubious, change/delete as necessary)
	 */
	double getDef();
	/**
	 * @return coefficient by which damage is scaled by from base weapon damage
	 */
	double getDamageMultiplier();
}
