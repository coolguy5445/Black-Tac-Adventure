package game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import game.inputs.Keyboard;
import game.display.Screen;
import game.level.Level;

public class Game extends Canvas implements Runnable {	
	
	private static final long serialVersionUID = 1L;
	public static Game game;
	private JFrame frame;
	private Screen screen;
	private Thread thread;
	private boolean running = false;
	private BufferedImage image = new BufferedImage(Screen.WIDTH, Screen.HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    
    public static Level level;
	
	public Game() {		
		frame  = new JFrame();
		screen = new Screen();
        level = new Level();
        
		Dimension size = new Dimension(Screen.WIDTH, Screen.HEIGHT);
		this.setSize(size);
		
		this.addKeyListener(Keyboard.keyboard);
	}
		
	public synchronized void start() {
	    running = true;
	    thread = new Thread(this, "Game");
	    thread.start();
	}
    
	public synchronized void stop() {
	    running = false;
	    try {
	    	thread.join();
	    }
	    catch (InterruptedException e) {
	    	e.printStackTrace();
	    }
	}
	
	public void run() {
		long lastTime = System.nanoTime();
	    long timer = System.currentTimeMillis();
	    final double ns = 1000000000.0 / 30.0; // Conversion from nanoseconds to milliseconds
	    double delta = 0;
	    int frames = 0;
	    int updates = 0;
	    
	    this.requestFocus();
	    
	    while (running) {
	    	long now = System.nanoTime();
	    	delta += (now - lastTime) / ns;
	    	lastTime = now;
	        	// Update 30 times a second
	    	while (delta >= 1) {
	    		++updates;
	    		update();
	    		--delta;
	    	}
	    	
	    	render();
	    	++frames;

	    	if (System.currentTimeMillis() - timer > 1000) {
	    		timer += 1000;
	    		frames = 0;
	    		updates = 0;
	    	}
	    }
	}
	
	public void update() {
        level.update();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
        level.render(screen);
		
		for (int i = 0; i < pixels.length; ++i) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, Screen.WIDTH, Screen.HEIGHT, null);
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		game = new Game();
		game.frame.setResizable(false);
	    game.frame.add(game);
	    game.frame.pack();
	    game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    game.frame.setLocationRelativeTo(null);
        game.frame.setTitle("Black-Tac Adventure!");
	    game.frame.setVisible(true);
	    game.start();
	}

}
