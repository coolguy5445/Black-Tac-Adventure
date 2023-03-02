package game.display;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import game.display.Screen;
	
public class Sprite {
	public int w, h;
	public int[] pixels;
	
    public Sprite(int w, int h) {
        this.w = w;
        this.h = h;
        pixels = new int[w * h];
        for (int i = 0; i < pixels.length; ++i) pixels[i] = 0xFFFF00FF;
    }
    
	public Sprite(int x, int y, int w, int h, Spritesheet sheet) {
		this.w = w;
		this.h = h;
		load(x, y, sheet);
	}
	
	public void load(int x, int y, Spritesheet sheet) {
		pixels = new int[w * h];
		
		for (short j = 0; j < h; ++j) {
			for (short i = 0; i < w; ++i) {
				pixels[i + j * w] = sheet.pixels[(i + x) + (j + y) * sheet.w];
			}
		}
	}
    
    public void render(Screen screen, int x, int y) {
        for (int row = y; row < y + h && row < screen.HEIGHT; ++row) {
            if (row < 0) {
                row = -1;
                continue;
            }
            for (int col = x; col < x + w && col < Screen.WIDTH; ++col) {
                if (col < 0) {
                    col = -1;
                    continue;
                }
                if (pixels[(col - x) + (row - y) * w] == 0xFFFF00FF) continue;
                screen.pixels[col + row * Screen.WIDTH] = pixels[(col - x) + (row - y) * w];
            }
        }
    }
}
