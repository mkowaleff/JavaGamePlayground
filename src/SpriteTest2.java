import java.awt.*;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class SpriteTest2 {
	
	public static void main(String args[]) {
		SpriteTest2 test = new SpriteTest2();
		test.run();
	}
	
	
	private static final DisplayMode POSSIBLE_MODES[] = {
			/*new DisplayMode(2560, 1600, 32, 0),
			new DisplayMode(2560, 1600, 24, 0),*/
			new DisplayMode(1680, 1050, 16, 0),
			new DisplayMode(1680, 1050, 32, 0),
			new DisplayMode(1440, 900, 24, 0),
			new DisplayMode(1440, 900, 16, 0)
	};
	
	/*
	 * private static final DisplayMode POSSIBLE_MODES[] = {
			new DisplayMode(1920, 1080, 32, 0),
			new DisplayMode(1920, 1080, 24, 0),
			new DisplayMode(1920, 1080, 16, 0),
			new DisplayMode(1280, 720, 32, 0),
			new DisplayMode(1280, 720, 24, 0),
			new DisplayMode(1280, 720, 16, 0)
	};
	 */
	
	
	private static final long DEMO_TIME = 10000;
	private static final long FADE_TIME = 1000;
	private static final int NUM_SPRITES = 3;
	
	private ScreenManager screen;
	private Image bgImage;
	private Sprite sprites[];
	
	public void loadImages() {
		// 	Load images
		//bgImage 		= loadImage("background.jpg").getScaledInstance(2560, 1600, 0);
		bgImage 		= loadImage("background.jpg").getScaledInstance(1680, 1050, 0);
		System.out.println();
		
		Image player1 = loadImage("player1.png");
		Image player2 = loadImage("player2.png");
		Image player3 = loadImage("player3.png");
		
		// 	Create and init sprites
		sprites = new Sprite[NUM_SPRITES];
		
		for(int i = 0; i < NUM_SPRITES; i++) {
			Animation anim = new Animation();
			anim.addFrame(player1, 250);
			anim.addFrame(player2, 150);
			anim.addFrame(player1, 150);
			anim.addFrame(player2, 150);
			anim.addFrame(player3, 200);
			anim.addFrame(player1, 150);
			sprites[i] = new Sprite(anim);
			
			// 	Select random starting location
			sprites[i].setX( (float)Math.random() * (screen.getWidth() - sprites[i].getWidth()) );
			sprites[i].setY( (float)Math.random() * (screen.getHeight() - sprites[i].getHeight()) );
			
			
			// 	Select random velocity
			sprites[i].setVelocityX((float)Math.random() - 0.5f);
            sprites[i].setVelocityY((float)Math.random() - 0.5f);
		}
	}
	
	
	private Image loadImage(String fileName) {
		URL resource = ImageTest.class.getResource("/images/" + fileName);
		System.out.println("(*) Loading " + resource);
		return Toolkit.getDefaultToolkit().getImage(resource);
	}
	
	
	public void run() {
		screen = new ScreenManager();
		try {
			DisplayMode displayMode = screen.findFirstCompatibleMode(POSSIBLE_MODES);
			screen.setFullScreen(displayMode);
			loadImages();
			animationLoop();
		}
		finally {
			screen.restoreScreen();
		}
	}
	
	
	public void animationLoop() {
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		
		while (currTime - startTime < DEMO_TIME) {
			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;
			
			// 	Update the sprites
			update(elapsedTime);
			
			// 	Draw and update screen
			Graphics2D g = screen.getGraphics();
			draw(g);
			drawFade(g, currTime - startTime);
			g.dispose();
			screen.update();
			
			// 	Take a nap
			try {
				Thread.sleep(20);
			}
			catch (InterruptedException ex) { }
		}
	}
	
	
	public void drawFade(Graphics2D g, long currTime) {
		long time = 0;
		if (currTime <= FADE_TIME) {
			time = FADE_TIME - currTime;
		}
		else if (currTime > DEMO_TIME - FADE_TIME) {
			time = FADE_TIME - DEMO_TIME + currTime;
		}
		else{
			return;
		}
		
		byte numBars = 8;
		int barHeight = screen.getHeight() / numBars;
		int blackHeight = (int)(time * barHeight / FADE_TIME);
		
		g.setColor(Color.black);
		for (int i = 0; i < numBars; i++) {
			int y = i * barHeight + (barHeight - blackHeight) / 2;
			g.fillRect(0, y, screen.getWidth(), blackHeight);
		}
	}
	
	
	public void update(long elapsedTime) {
		
		for(int i = 0; i < NUM_SPRITES; i++) {
			
			Sprite s = sprites[i];
			
			// 	Check sprite bounds
			if(s.getX() < 0) {
				s.setVelocityX(Math.abs(s.getVelocityX()));
			}
			else if(s.getX() + s.getWidth() >= screen.getWidth()) {
				s.setVelocityX(-Math.abs(s.getVelocityX()));
			}
			
			if(s.getY() < 0) {
				s.setVelocityY(Math.abs(s.getVelocityY()));
			}
			else if(s.getY() + s.getHeight() >= screen.getHeight()) {
				s.setVelocityY(-Math.abs(s.getVelocityY()));
			}
			
			// update sprite's locations
            s.update(elapsedTime);
		}
	}
	
	
	public void draw(Graphics2D g) {
		// 	Draw background
		g.drawImage(bgImage, 0, 0, null);
		
		AffineTransform transform = new AffineTransform();
		
		for(int i = 0; i < NUM_SPRITES; i++) {
			
			Sprite sprite = sprites[i];
			
			// 	Translate the sprite
			transform.setToTranslation(sprite.getX(), sprite.getY());
			// 	If the sprite is moving left, flip the image
			if(sprite.getVelocityX() < 0) {
				transform.scale(-1, 1);
				transform.translate(-sprite.getWidth(), 0);
			}
			
			// 	Draw it
			g.drawImage(sprite.getImage(), transform, null);
			
		}
	}
}
