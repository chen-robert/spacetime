package config;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageLoader {
	private final static HashMap<String, Image> IMGS = new HashMap<>();
	private final static String SPRITE_PATH = "resources/sprites/";
	static {
		loadImage("Test", "craft_test.png");
	}

	/**
	 * Associates a given name with a sprite.
	 * 
	 * @param name
	 * @param file place to be loaded from
	 */
	private static void loadImage(String name, String file) {
		try {
			IMGS.put(name, ImageIO.read(new File(SPRITE_PATH + file)));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.printf("Failed to load %s", name);
		}
	}

	public static Image getImg(String name) {
		return IMGS.get(name);
	}
}