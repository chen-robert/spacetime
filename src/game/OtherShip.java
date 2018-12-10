package game;

import java.awt.Image;

import config.ImageLoader;
import io.Util;
import networking.Serializable;
import ui.Renderable;

public class OtherShip implements Renderable, Serializable {
	private Image selfImage;
	private double renderX, renderY;
	private double direction, speed;
	private String name;

	public OtherShip(double x, double y, double dir, double spd, String name) {
		this.name = name;

		construct(x, y, dir, spd, name);
	}

	private void construct(double x, double y, double dir, double spd, String name) {
		selfImage = ImageLoader.getImg(name + "-e");
		renderX = x;
		renderY = y;
		direction = dir;
		speed = spd;
	}

	@Override
	public int getRenderX() {
		// TODO Auto-generated method stub
		return (int) renderX;
	}

	@Override
	public int getRenderY() {
		// TODO Auto-generated method stub
		return (int) renderY;
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
		return 1;
	}

	@Override
	public Image getImg() {
		// TODO Auto-generated method stub
		return selfImage;
	}

	@Override
	public byte[] toBytes() {
		return Util.concat(//
				Util.toBytes((int) renderX, (int) renderY, (int) direction, (int) speed), //
				Util.toBytes(name.length()), //
				name.getBytes());
	}

	public OtherShip(byte[] b) {
		int x = Util.toInt(b, 0);
		int y = Util.toInt(b, 1 * 4);
		int dir = Util.toInt(b, 2 * 4);
		int spd = Util.toInt(b, 3 * 4);

		int strLen = Util.toInt(b, 4 * 4);
		String name = new String(b, 5 * 4, strLen);

		construct(x, y, dir, spd, name);
	}

}
