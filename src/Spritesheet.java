package game.display;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Spritesheet {
	public int w, h;
	public int[] pixels;
	
	public Spritesheet(String path) {
		load(path);
	}
	
	protected void load(String path) {
		try {
			BufferedImage image = ImageIO.read(Spritesheet.class.getResource(path));
			w = image.getWidth();
			h = image.getHeight();
			pixels = new int[w * h];
			image.getRGB(0, 0, w, h, pixels, 0, w);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
