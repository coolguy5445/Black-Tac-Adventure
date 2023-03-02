package game.level;

import java.util.ArrayList;
import java.util.Random;

import game.display.Screen;
import game.display.Sprite;
import game.display.Spritesheet;
import game.display.Font;
import game.player.Player;
import game.audio.SoundEffect;
import game.audio.Music;

public class Level {
    private static Random random = new Random();
    
    private static final Font whiteFont = new Font(new Spritesheet("imageFiles/BasicWhiteFont.png"), 25, 25);
    private static final Font yellowFont = new Font(new Spritesheet("imageFiles/BasicYellowFont.png"), 25, 25);
    
    private static final Music music = new Music("soundFiles/Theme1.wav");
    
    private static final SoundEffect fallSound = new SoundEffect("soundFiles/pixel-death-66829.wav");
    private static final SoundEffect levelSound = new SoundEffect("soundFiles/message-incoming-132126.wav");
    private static final SoundEffect victorySound = new SoundEffect("soundFiles/success-fanfare-trumpets-6185.wav");
    
    private static final int[] BIOME_BACKGROUNDS = {0x71c5e7, 0x45b3e0, 0xd3d3d3, 0x87ceeb, 0x880808};
    
    public static final double GRAVITY = Screen.HEIGHT / 200;
    
    private static final int GAME_DURATION = 10 * 60000;
    private static final int MAX_DIFFICULTY = 100;
    private static final long TIME_UNTIL_DIFFICULTY_UP = GAME_DURATION / MAX_DIFFICULTY;
    
    private static final int MINIMUM_HEIGHT = Screen.HEIGHT / 4;
    private static final int MAXIMUM_HEIGHT = Screen.HEIGHT / 2;
    
    private static final double SCREENSCROLL_MAX = Screen.WIDTH / 32;
    private static final double SCREENSCROLL_BASE = Screen.WIDTH / 320;
    private static final double SCREENSCROLL_INCREMENT = (SCREENSCROLL_MAX - SCREENSCROLL_BASE) / MAX_DIFFICULTY;
    
    private static final double MIN_GAP_MAX = Screen.WIDTH / 5;
    private static final double MIN_GAP_BASE = Screen.WIDTH / 32;
    private static final double MIN_GAP_INCREMENT = (MIN_GAP_MAX - MIN_GAP_BASE) / MAX_DIFFICULTY;
    
    private static final double MAX_GAP_MAX = Screen.WIDTH / 2;
    private static final double MAX_GAP_BASE = Screen.WIDTH / 16;
    private static final double MAX_GAP_INCREMENT = (MAX_GAP_MAX - MAX_GAP_BASE) / MAX_DIFFICULTY;
    
    private static final double MIN_WIDTH_MIN = Screen.WIDTH / 16;
    private static final double MIN_WIDTH_BASE = Screen.WIDTH / 3;
    private static final double MIN_WIDTH_DECREMENT = (MIN_WIDTH_BASE - MIN_WIDTH_MIN) / MAX_DIFFICULTY;
    
    private static final double MAX_WIDTH_MIN = Screen.WIDTH / 8;
    private static final double MAX_WIDTH_BASE = Screen.WIDTH / 2;
    private static final double MAX_WIDTH_DECREMENT = (MAX_WIDTH_BASE - MAX_WIDTH_MIN) / MAX_DIFFICULTY;
    
    private static final double MAX_HEIGHT_DIFF_MAX = Screen.HEIGHT / 8;
    private static final double MAX_HEIGHT_DIFF_BASE = Screen.HEIGHT / 64;
    private static final double MAX_HEIGHT_DIFF_INCREMENT = (MAX_HEIGHT_DIFF_MAX - MAX_HEIGHT_DIFF_BASE) / MAX_DIFFICULTY;
    
    private Player player;
    
    public ArrayList<Platform> platforms = new ArrayList<Platform>();
    
    public int difficulty;
    private Sprite difficultyDisplay;
    
    private int screenScroll;
    private int minGap, maxGap, nextGap;
    private int minWidth, maxWidth, nextWidth;
    private int maxHeightDiff, nextHeight;
    
    private long levelTimer;
    
    public Level() {
        difficulty = 0;
        createStartingPlatform();
        incrementDifficulty();
        createNextPlatform();
        createPlayer();
        music.start();
    }
    
    private void createStartingPlatform() {
        platforms.removeAll(platforms);
        Platform startingPlatform = new Platform(Screen.WIDTH, MINIMUM_HEIGHT);
        startingPlatform.x = 0;
        platforms.add(startingPlatform);
    }
    
    private void createPlayer() {
        this.player = new Player();
    }
    
    public void incrementDifficulty() {
        ++difficulty;
        if (difficulty == MAX_DIFFICULTY) victorySound.start();
        else if (difficulty < MAX_DIFFICULTY) levelSound.start();
        
        if (difficulty >= MAX_DIFFICULTY) {
            difficulty = MAX_DIFFICULTY;
            difficultyDisplay = yellowFont.getSprite(Integer.toString(difficulty), 5);
        }
        else {
            difficultyDisplay = whiteFont.getSprite(Integer.toString(difficulty), 5);
        }
        
        updateData();
        levelTimer = System.currentTimeMillis();
    }
    
    private void updateData() {
        screenScroll = (int) (SCREENSCROLL_BASE + SCREENSCROLL_INCREMENT * difficulty);
        if (screenScroll > SCREENSCROLL_MAX) screenScroll = (int) (SCREENSCROLL_MAX);
        
        minGap = (int) (MIN_GAP_BASE + MIN_GAP_INCREMENT * difficulty);
        if (minGap > MIN_GAP_MAX) minGap = (int) (MIN_GAP_MAX);
        
        maxGap = (int) (MAX_GAP_BASE + MAX_GAP_INCREMENT * difficulty);
        if (maxGap > MAX_GAP_MAX) maxGap = (int) (MAX_GAP_MAX);
        
        minWidth = (int) (MIN_WIDTH_BASE - MIN_WIDTH_DECREMENT * difficulty);
        if (minWidth < MIN_WIDTH_MIN) minWidth = (int) (MIN_WIDTH_MIN);
        
        maxWidth = (int) (MAX_WIDTH_BASE - MAX_WIDTH_DECREMENT * difficulty);
        if (maxWidth < MAX_WIDTH_MIN) maxWidth = (int) (MAX_WIDTH_MIN);
        
        maxHeightDiff = (int) (MAX_HEIGHT_DIFF_BASE + MAX_HEIGHT_DIFF_INCREMENT * difficulty);
        if (maxHeightDiff > MAX_HEIGHT_DIFF_MAX) maxHeightDiff = (int) (MAX_HEIGHT_DIFF_MAX);
    }
    
    private void createNextPlatform() {
        getNextGap();
        getNextWidth();
        getNextHeight();
    }
    
    private void createNextFlat() {
        getNextGap();
        nextWidth = (int) (Screen.WIDTH * (1 + 0.25 * difficulty));
        nextHeight = MINIMUM_HEIGHT;
    }
    
    private void getNextGap() {
        if (maxGap == minGap) nextGap = minGap;
        else nextGap = minGap + random.nextInt(maxGap - minGap);
    }
    
    private void getNextWidth() {
        if (maxWidth == minWidth) {
            nextWidth = minWidth;
            return;
        }
        nextWidth = minWidth + random.nextInt(maxWidth - minWidth);
    }
    
    private void getNextHeight() {
        if (platforms.isEmpty()) {
            nextHeight = MINIMUM_HEIGHT;
            return;
        }
        int lastHeight = Screen.HEIGHT - platforms.get(platforms.size() - 1).y;
        if (maxHeightDiff == 0) {
            nextHeight = lastHeight;
            return;
        }
        int modifier = random.nextInt(maxHeightDiff * 2) - maxHeightDiff;
        nextHeight = lastHeight + modifier;
        if (nextHeight < MINIMUM_HEIGHT) nextHeight = MINIMUM_HEIGHT;
        if (nextHeight > MAXIMUM_HEIGHT) nextHeight = MAXIMUM_HEIGHT;
    }
    
    private int currentGap() {
        if (platforms.isEmpty()) return Integer.MAX_VALUE;
        Platform lastPlatform = platforms.get(platforms.size() -1);
        return Screen.WIDTH - (lastPlatform.x + lastPlatform.w);
    }
    
        // Returns x, y & w of colliding platform
        // -1s for not collision
    public int[] collision(int x, int y, int w, int h) {
        for (int i = 0; i < platforms.size(); ++i) {
            Platform platform = platforms.get(i);
            if ((x < platform.x + platform.w && x + w > platform.x) && y + h > platform.y) {
                int[] hitbox = {platform.x, platform.y};
                return hitbox;
            }
        }
        int[] noCollision = {-1, -1};
        return noCollision;
    }
    
    public void update() {
        for (int i = 0; i < platforms.size(); ++i) {
            Platform platform = platforms.get(i);
            platform.update(screenScroll);
            if (platform.x + platform.w <= 0) platforms.remove(platform);
        }
        
        if (currentGap() >= nextGap) {
            platforms.add(new Platform(nextWidth, nextHeight));
            createNextPlatform();
        }
        
        player.update(this);
        if (player.y > Screen.HEIGHT) {
            fallSound.start();
            createStartingPlatform();
            player.respawn();
            levelTimer = System.currentTimeMillis();
        }
        
        if (System.currentTimeMillis() - levelTimer > TIME_UNTIL_DIFFICULTY_UP) {
            incrementDifficulty();
        }
    }
    
    public void render(Screen screen) {
        int biome = (difficulty - 1) / (MAX_DIFFICULTY / 5);
        if (biome > 5) biome = 5;
        screen.clear(BIOME_BACKGROUNDS[biome]);
        for (Platform p : platforms) {
            p.render(screen, biome);
        }
        player.render(screen);
        difficultyDisplay.render(screen, (int) (Screen.WIDTH * 0.9) - difficultyDisplay.w, (int) (Screen.HEIGHT * 0.1));
    }
}