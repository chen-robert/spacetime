package game;

import java.awt.Image;

import config.ImageLoader;
import io.Util;
import networking.SerializableObject;
import networking.Serialize;
import ui.Renderable;

@Serialize(fields = { "name", "renderX", "renderY", "direction", "speed", "id" })
public class OtherShip extends SerializableObject implements Renderable {
	private Image selfImage;
	private double renderX, renderY;
	private double direction, speed;
	private String name;
	private String id;

	public OtherShip(double x, double y, double dir, double spd, String id, String name) {
		this.name = name;
		this.id = id;

		selfImage = ImageLoader.getImg(name + "-e");
		renderX = x;
		renderY = y;
		direction = dir;
		speed = spd;
	}

	public OtherShip() {
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
		if (selfImage == null) selfImage = ImageLoader.getImg(name + "-e");
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
		int offset = 0;

		double x = Util.toDouble(b, offset);
		offset += 8;
		double y = Util.toDouble(b, offset);
		offset += 8;
		double dir = Util.toDouble(b, offset);
		offset += 8;
		double spd = Util.toDouble(b, offset);
		offset += 8;

		int strLen = Util.toInt(b, offset);
		offset += 4;
		int idLen = Util.toInt(b, offset);
		offset += 4;
		String name = new String(b, offset, strLen);
		offset += strLen;
		String id = new String(b, offset, idLen);
	}

	@Override
	public String getId() {
		return id;
	}

}
