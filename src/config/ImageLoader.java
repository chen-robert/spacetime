package config;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	public static BufferedImage Craft_Test;
	
	public ImageLoader() {
		try {
			Craft_Test = ImageIO.read(new File("resources/sprites/craft_test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
