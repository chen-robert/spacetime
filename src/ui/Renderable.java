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
	 * Returns priority for rendering. Higher priority items get rendered on top
	 * (get rendered later). For example, background is probably low priority.
	 * 
	 * @return render priority as an integer
	 */
	public int getRenderPriority();
}
