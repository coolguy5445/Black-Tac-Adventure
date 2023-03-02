package game.player;

import java.awt.event.KeyEvent;

import game.display.Screen;
import game.level.Level;
import game.inputs.Keyboard;
import game.audio.SoundEffect;

public class Player {
    private static final SoundEffect jumpSound = new SoundEffect("soundFiles/cartoon-jump-6462.wav");
    
    public int x, y, w, h;
    private double velY;
    private static int JUMP_POWER = Screen.HEIGHT / 20;
    
    public Player() {
        this.w = Screen.WIDTH / 64;
        this.h = Screen.HEIGHT / 24;
        respawn();
    }
    
    public void render(Screen screen) {
        for (int row = y; row < y + h && row < Screen.HEIGHT; ++row) {
            if (row < 0) {
                row = -1;
                continue;
            }
            for (int col = x; col < x + w && col < Screen.WIDTH; ++col) {
                if (col < 0) {
                    col = -1;
                    continue;
                }
                screen.pixels[col + row * Screen.WIDTH] = 0x000000;
            }
        }
    }
    
    public void update(Level level) {
        int[] hitbox = level.collision(x, y, w, h);
        if (hitbox[0] != -1) {
            this.x = hitbox[0] - this.w;
        }
        
        boolean onGround = false;
        velY += Level.GRAVITY;
        y += velY;
        hitbox = level.collision(x, y, w, h);
        if (hitbox[1] != -1) {
            this.y = hitbox[1] - this.h;
            this.velY = 0;
            onGround = true;
        }
        
        if (Keyboard.keyboard.keyStates[KeyEvent.VK_SPACE]) {
            if (onGround) {
                velY -= JUMP_POWER;
                jumpSound.start();
            }
            else velY -= Level.GRAVITY / 2;
        }
    }
    
    public void respawn() {
        this.x = Screen.WIDTH / 8;
        this.y = 0;
        this.velY = 0.0;
    }
}