package config;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ui.Renderable;

public class BackgroundParser {
	private static BufferedImage test;
	private static boolean[][] cachedCollisions;
	static {
		try {
			test = ImageIO.read(new File("resources/backgrounds/test.bmp"));
		} catch (IOException ie) {
		}
	}

	public static Renderable getBackgroundSprite(int width, int height) {
		BufferedImage copy = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		copy.getGraphics().drawImage(test, 0, 0, width, height, null);

		return new Renderable() {

			@Override
			public int getRenderY() {
				// TODO Auto-generated method stub
				return height / 2;
			}

			@Override
			public int getRenderX() {
				// TODO Auto-generated method stub
				return width / 2;
			}

			@Override
			public int getRenderPriority() {
				return 4;
			}

			@Override
			public Image getImg() {
				return copy;
			}

		};
	}

	public static boolean[][] getBackgroundCollisions(int width, int height) {
		if (cachedCollisions == null || cachedCollisions.length != width || cachedCollisions[0].length != height) {
			cachedCollisions = generateBackgroundCollisions(width, height);
		}
		return cachedCollisions;
	}

	public static boolean[][] generateBackgroundCollisions(int width, int height) {
		BufferedImage copy = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		copy.getGraphics().drawImage(test, 0, 0, width, height, null);

		boolean[][] ret = new boolean[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Color c = new Color(copy.getRGB(i, j));
				int shade = (c.getRed() + c.getBlue() + c.getGreen()) / 3;

				ret[i][j] = shade < 250;
			}
		}
		return ret;
	}
}
