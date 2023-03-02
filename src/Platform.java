package game.level;

import game.display.Screen;

public class Platform {
        // Colour 1 = tops of platforms
        // Colour 2 = body of platforms
    private static final int[][] PLATFORM_COLOURS = {
        {0x7cfc00, 0x836539},
        {0x2C5E1A, 0x1A4314},
        {0xB7B09C, 0x787366},
        {0xF4F5E2, 0x796342},
        {0x7f7f7f, 0x222222}
    };
    
    public int x, y, w;
    
    public Platform(int w, int h) {
        this.x = Screen.WIDTH;
        this.y = Screen.HEIGHT - h;
        this.w = w;
    }
    
    public void update(int screenScrollSpeed) {
        this.x -= screenScrollSpeed;
    }
    
    public void render(Screen screen, int biome) {
        for (int row = y; row < Screen.HEIGHT; ++row) {
            for (int col = x; col < x + w && col < Screen.WIDTH; ++col) {
                if (col < 0) {
                    col = -1;
                    continue;
                }
                    // Top of platform is a different colour ...
                if (row < y + 8) screen.pixels[col + row * Screen.WIDTH] = PLATFORM_COLOURS[biome][0];
                    // ... to the body
                else screen.pixels[col + row * Screen.WIDTH] = PLATFORM_COLOURS[biome][1];
            }
        }
    }
    
    public boolean collision(int playerX, int playerY, int playerW, int playerH) {
        return (playerX < x + w && playerX + playerW > x) && (playerY + playerH > y);
    }
}