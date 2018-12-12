package game;

import java.awt.Image;

import config.ImageLoader;
import io.Util;
import networking.SerializableObject;
import ui.Renderable;

public class OtherShip extends SerializableObject implements Renderable {
	private Image selfImage;
	private double renderX, renderY;
	private double direction, speed;
	private String name;
	private String id;

	public OtherShip(double x, double y, double dir, double spd, String id, String name) {
		this.name = name;

		construct(x, y, dir, spd, id, name);
	}

	private void construct(double x, double y, double dir, double spd, String id, String name) {
		this.id = id;

		selfImage = ImageLoader.getImg(name + "-e");
		renderX = x;
		renderY = y;
		direction = dir;
		speed = spd;
	}

	public double getX() {
		return renderX;
	}

	public double getY() {
		return renderY;
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
				Util.toBytes(renderX, renderY, direction, speed), //
				Util.toBytes(name.length(), id.length()), //
				name.getBytes(), //
				id.getBytes());
	}

	public OtherShip(byte[] b) {
		double x = Util.toDouble(b, 0);
		double y = Util.toDouble(b, 1 * 8);
		double dir = Util.toDouble(b, 2 * 8);
		double spd = Util.toDouble(b, 3 * 8);

		int strLen = Util.toInt(b, 4 * 8 + 0 * 4);
		int idLen = Util.toInt(b, 4 * 8 + 1 * 4);
		String name = new String(b, 6 * 4, strLen);
		String id = new String(b, 6 * 4 + strLen, idLen);

		construct(x, y, dir, spd, id, name);
	}

	@Override
	public String getId() {
		return id;
	}

}
