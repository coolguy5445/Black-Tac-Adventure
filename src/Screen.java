package game.display;

public class Screen {
	public static int WIDTH  = 640;
	public static int HEIGHT = 400;
	
	public int pixels[];
    
	public Screen() {
		pixels = new int[Screen.WIDTH * Screen.HEIGHT];
		clear(0);
	}
    
	public void clear(int colour) {
		for (int i = 0; i < pixels.length; ++i) {
			pixels[i] = colour;
		}
	}
}
