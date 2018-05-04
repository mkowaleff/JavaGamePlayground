import java.awt.*;
import java.net.URL;

public class SpriteTest1 {

	public static void main(String args[]) {
		SpriteTest1 test = new SpriteTest1();
		test.run();
	}
	
	
	private static final DisplayMode POSSIBLE_MODES[] = {
			new DisplayMode(1920, 1080, 32, 0),
			new DisplayMode(1920, 1080, 24, 0),
			new DisplayMode(1920, 1080, 16, 0),
			new DisplayMode(1280, 720, 32, 0),
			new DisplayMode(1280, 720, 24, 0),
			new DisplayMode(1280, 720, 16, 0)
	};
	
	
	private static final long DEMO_TIME = 1000000;
	
	private ScreenManager screen;
	private Image bgImage;
	private Sprite sprite1;
	private Sprite sprite2;
	private Sprite[] sprites = {
			sprite1, sprite2
	};
	
	
	public void loadImages() {
		// 	Load images
		bgImage 		= loadImage("background.jpg").getScaledInstance(1280, 800, 0);
		System.out.println();
		
		/*Image test0 	= loadImage("animation0/bs_g1_dn_0.png");
		Image test1 	= loadImage("animation0/bs_g1_dn_1.png");
		Image test2 	= loadImage("animation0/bs_g1_dn_2.png");
		Image test3		= loadImage("animation0/bs_g1_dn_3.png");
		Image test4 	= loadImage("animation0/bs_g1_dn_4.png");*/
		
		Image[] sprite1Img = { 
				loadImage("animation0/a4_b0_0.png"),
				loadImage("animation0/a4_b0_1.png"),
				loadImage("animation0/a4_b0_2.png"),
				loadImage("animation0/a4_b0_3.png"),
				loadImage("animation0/a4_b0_4.png")	
		};
		System.out.println();
		
		Image[] sprite2Img = {
				loadImage("animation1/a2_b0_0.png"),
				loadImage("animation1/a2_b0_1.png"),
				loadImage("animation1/a2_b0_2.png"),
				loadImage("animation1/a2_b0_3.png"),
				loadImage("animation1/a2_b0_4.png")		
		};
		
		System.out.println();
		System.out.println("(*) IMAGE LOADING COMPLETE (*)");
		System.out.println();
		System.out.println();
		
		//	 	Create Sprite
		Animation anim1 = new Animation();
		Animation anim2 = new Animation();
		
		/* anim1.addFrame(sprite1Img[0],  250);
		anim1.addFrame(sprite1Img[1], 150);
		anim1.addFrame(sprite1Img[2], 150);
		anim1.addFrame(sprite1Img[3], 150);
		anim1.addFrame(sprite1Img[4], 150);
		anim1.addFrame(sprite1Img[1], 150);
		anim1.addFrame(sprite1Img[2], 150);
		anim1.addFrame(sprite1Img[3], 150);
		anim1.addFrame(sprite1Img[4], 150);
		anim1.addFrame(sprite1Img[1], 150);
		anim1.addFrame(sprite1Img[2], 150);
		anim1.addFrame(sprite1Img[3], 150);
		anim1.addFrame(sprite1Img[4], 150);
		anim1.addFrame(sprite1Img[0], 250);
		*/
		// This code does the same as the commented out one above:
		final int[] indexOrderForWalking = { 0, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 0};
		final int[] durationPerFrameForWalking = {250, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 250};

		for(int i = 0; i < indexOrderForWalking.length; i++) {
			anim1.addFrame(sprite1Img[indexOrderForWalking[i]], durationPerFrameForWalking[i]);
			anim2.addFrame(sprite2Img[indexOrderForWalking[i]], durationPerFrameForWalking[i]);
		}
		
		
		sprite1 = new Sprite(anim1);
		sprite2 = new Sprite(anim2);
		
		sprite2.setX(1216);
		
		
		
		// 	Start the sprite off moving down and to the right
		sprite1.setVelocityX(0.1f);
		sprite1.setVelocityY(0.1f);
		
		sprite2.setVelocityX(0.1f);
		sprite2.setVelocityY(0.1f);
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
		
		while(currTime - startTime < DEMO_TIME) {
			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;
			
			
			// 	Update the sprites
			update(elapsedTime);
			
			// 	Draw and update the screen
			Graphics2D g = screen.getGraphics();
			draw(g);
			g.dispose();
			screen.update();
			
			// 	Take a nap
			try {
				Thread.sleep(20);
			}
			catch (InterruptedException ex) { }
		}
	}
	
	
	public void update(long elapsedTime) {
		
		
		// 	Sprite motion
		// 	Check Sprite bounds
		if(sprite1.getX() < 0) {
			sprite1.setVelocityX(Math.abs(sprite1.getVelocityX()));
		}
		else if(sprite1.getX() + sprite1.getWidth() >= screen.getWidth()) {
			sprite1.setVelocityX(-Math.abs(sprite1.getVelocityX()));
		}
		
		if(sprite1.getY() < 0) {
			sprite1.setVelocityY(Math.abs(sprite1.getVelocityY()));
		}
		else if(sprite1.getY() + sprite1.getHeight() >= screen.getHeight()) {
			sprite1.setVelocityY(-Math.abs(sprite1.getVelocityY()));
		}
		
		
		
		
		if(sprite2.getX() < 0) {
			sprite2.setVelocityX(Math.abs(sprite2.getVelocityX()));
		}
		else if(sprite2.getX() + sprite2.getWidth() >= screen.getWidth()) {
			sprite2.setVelocityX(-Math.abs(sprite2.getVelocityX()));
		}
		
		if(sprite2.getY() < 0) {
			sprite1.setVelocityY(Math.abs(sprite2.getVelocityY()));
		}
		else if(sprite2.getY() + sprite2.getHeight() >= screen.getHeight()) {
			sprite2.setVelocityY(-Math.abs(sprite2.getVelocityY()));
		}
		
		
		
		
		// 	Update sprite
		sprite1.update(elapsedTime);
		sprite2.update(elapsedTime);
	}
	
	
	public void draw(Graphics g) {
		// 	Draw background
		g.drawImage(bgImage, 0, 0, null);
		
		// 	Draw Sprite
		g.drawImage(sprite1.getImage(), Math.round(sprite1.getX()), Math.round(sprite1.getY()), null);
		g.drawImage(sprite2.getImage(), Math.round(sprite2.getX()), Math.round(sprite2.getY()), null);
	}
}
