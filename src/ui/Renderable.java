package ui;

import java.awt.Image;

public interface Renderable {
	public Image getImg();

	public int getX();

	public int getY();

	/**
	 * Returns priority for rendering. Higher priority items get rendered on top
	 * (get rendered later). For example, background is probably low priority.
	 * 
	 * @return
	 */
	public int getRenderPriority();
}
