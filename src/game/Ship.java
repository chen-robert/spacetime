package game;

import java.awt.Image;
import java.awt.image.BufferedImage;

import ui.Renderable;

public class Ship implements Renderable {
	private double shipX;
	private double shipY;
	/**
	 * 0 direction faces to the right, 90 faces up, goes counterclockwise
	 */
	private double direction;
	private BufferedImage shipImage;
	@Override
	public Image getImg() {
		// TODO Auto-generated method stub
		return null;
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
		return 0;
	}
}
