import java.awt.*;
import java.net.URL;

import javax.swing.JFrame;

public class AnimationTest1 {

	public static void main(String[] args) {
		
		DisplayMode displayMode;
		
		if (args.length == 3) {
			displayMode = new DisplayMode(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), DisplayMode.REFRESH_RATE_UNKNOWN);				
		}
		
		else {
			displayMode = new DisplayMode(1920, 1080, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		}
		
		AnimationTest1 test = new AnimationTest1();
		test.run(displayMode);
	}
	
	
	private static final long DEMO_TIME = 50000;
	
	private SimpleScreenManager screen;
	private Image bgImage;
	private Animation anim;
	
	public void loadImages() {
		
		
		// 	Load images
		bgImage 		= loadImage("background.jpg");
		Image player1 	= loadImage("player1.png");
		Image player2 	= loadImage("player2.png");
		Image player3 	= loadImage("player3.png");
		
		Image test0 	= loadImage("/animation0/bs_g1_dn_0.png");
		Image test1 	= loadImage("/animation0/bs_g1_dn_1.png");
		Image test2 	= loadImage("/animation0/bs_g1_dn_2.png");
		Image test3		= loadImage("/animation0/bs_g1_dn_3.png");
		Image test4 	= loadImage("/animation0/bs_g1_dn_4.png");
		
		
		// 	Create animation
		anim = new Animation();
		anim.addFrame(test0,  250);
		anim.addFrame(test1, 150);
		anim.addFrame(test2, 150);
		anim.addFrame(test3, 150);
		anim.addFrame(test4, 150);
		anim.addFrame(test1, 150);
		anim.addFrame(test2, 150);
		anim.addFrame(test3, 150);
		anim.addFrame(test4, 150);
		anim.addFrame(test1, 150);
		anim.addFrame(test2, 150);
		anim.addFrame(test3, 150);
		anim.addFrame(test4, 150);
		anim.addFrame(test0, 250);
	}
	
	private Image loadImage(String fileName) {
		URL resource = ImageTest.class.getResource("/images/" + fileName);
		return Toolkit.getDefaultToolkit().getImage(resource);
	}
	
	public void run(DisplayMode displayMode) {
		screen = new SimpleScreenManager();
		
		try {
			screen.setFullScreen(displayMode, new JFrame());
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
			
			
			// 	Update animation
			anim.update(elapsedTime);
			
			// 	Draw to screen
			Graphics g = screen.getFullScreenWindow().getGraphics();
			draw(g);
			g.dispose();
			
			// 	Take a nap
			try {
				Thread.sleep(20);
			}
			catch (InterruptedException ex) { }
		}
	}
	
	
	public void draw(Graphics g) {
		// 	Draw background
		g.drawImage(bgImage, 0, 0, null);
		
		// 	Draw image
		g.drawImage(anim.getImage(), 250, 250, null);
		g.drawImage(anim.getImage(), 300, 250, null);
		g.drawImage(anim.getImage(), 350, 250, null);
	}
}
