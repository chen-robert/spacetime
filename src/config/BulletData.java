package config;

import java.awt.Image;

public interface BulletData {
	String getName();

	/**
	 * @return the image of said bullet
	 */
	default Image getImage() {
		return ImageLoader.getImg(getName());
	}

	/**
	 * @return Spawn X value
	 */
	double getInitialX();
	
	/**
	 * @return Spawn Y value
	 */
	double getInitialY();
	
	/**
	 * @return the speed at which to start the bullet
	 */
	double getInitialSpeed();
	
	/**
	 * @return the direction in which to start the bullet
	 */
	double getInitialDirection();
	
	/**
	 * @return radius of the circular hitbox
	 * if 0, bullet only hits a ship if it enters one
	 */
	double getHitboxRadius();
	
	/**
	 * @return radius of the circular explosion left after expiration
	 * Note: the explosion ignores walls and such. Plan accordingly.
	 */
	double getExplosionRadius();

	/**
	 * @return amount of damage dealt per bullet
	 */
	double getDamage();
	

	/**
	 * @return a double between 0 and 1, fraction of energy when rebounding
	 * If it does not bounce off walls, return -1
	 */
	double getRebound();
	
	/**
	 * @return a double, the amount of speed lost per TICK passing through walls
	 * If it does not pass through walls, return -1
	 * TODO:  Probably want a better name for this btw :|
	 */
	double getWallDrag();
}
