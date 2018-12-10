package game;

import java.awt.Image;

import config.ImageLoader;
import ui.Renderable;

public class OtherShip implements Renderable {
	private Image selfImage;
	private double renderX, renderY;
	private double direction, speed;
	
	public OtherShip(double x, double y, double dir, double spd, String name) {
		selfImage = ImageLoader.getImg(name + "-e");
		renderX = x;
		renderY = y;
		direction = dir;
		speed = spd;
	}

	@Override
	public int getRenderX() {
		// TODO Auto-generated method stub
		return (int)renderX;
	}

	@Override
	public int getRenderY() {
		// TODO Auto-generated method stub
		return (int)renderY;
	}
	
	@Override
	public double getDirectionDegrees() {
		return direction;
	}
	
	public double getSpeed() {
		return speed;
	}

	@Override
	public int getRenderPriority() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Image getImg() {
		// TODO Auto-generated method stub
		return selfImage;
	}

}
