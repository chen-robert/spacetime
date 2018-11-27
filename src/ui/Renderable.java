package ui;

import java.awt.Image;

public interface Renderable {
	/**
	 * Returns the image to be used for the sprite.
	 * 
	 * @return sprite's image
	 */
	public Image getImg();

	/**
	 * Gets the x coordinate of the sprite. Note that the image is drawn centered on
	 * this value.
	 * 
	 * @return x coord
	 */
	public int getX();

	/**
	 * Gets the y coordinate of the sprite. Note that the image is drawn centered on
	 * this value.
	 * 
	 * @return y coord
	 */
	public int getY();

	/**
	 * gets the direction of the sprite. The default 0 angle faces to the right and 
	 * further rotation moves it counterclockwise. 
	 * 
	 * @return direction rotated towards (0 to the right)
	 */
	public double getDirectionDegrees();
	/**
	 * Derp turns it into radians just in case
	 * 
	 * @return direction rotated towards (0 to the right) now in radians
	 */
	public double getDirectionRadians();
	
	/**
	 * Returns priority for rendering. Higher priority items get rendered on top
	 * (get rendered later). For example, background is probably low priority.
	 * 
	 * @return render priority as an integer
	 */
	public int getRenderPriority();
}
