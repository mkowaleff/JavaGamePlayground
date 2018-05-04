import java.awt.*;
import java.net.URL;

import javax.swing.JFrame;

public class AnimationTest2 {
	
	public static void main(String[] args) {
		
		
		AnimationTest2 test = new AnimationTest2();
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
	
	
	private static final long DEMO_TIME = 10000;
	
	private ScreenManager screen;
	private Image bgImage;
	private Animation anim;
	
	
	public void loadImages() {
		// 	Load images
		bgImage 		= loadImage("background.jpg").getScaledInstance(1679, 1049, 0);
		
		Image[] animationFrames = {
				loadImage("animation0/a4_b0_0.png"),
				loadImage("animation0/a4_b0_1.png"),
				loadImage("animation0/a4_b0_2.png"),
				loadImage("animation0/a4_b0_3.png"),
				loadImage("animation0/a4_b0_4.png")
		};
		int[] frameDurations = {250, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 250};
		
		anim = new Animation();

		for(int i = 0; i < animationFrames.length; i++) {
				anim.addFrame(animationFrames[i], frameDurations[i]);
		}
		
		/* Original code from the book
		 * 
		 * Image test0 	= loadImage("animation0/a4_b0_0.png");
		Image test1 	= loadImage("animation0/a4_b0_1.png");
		Image test2 	= loadImage("animation0/a4_b0_2.png");
		Image test3		= loadImage("animation0/a4_b0_3.png");
		Image test4 	= loadImage("animation0/a4_b0_4.png");*/
		
		
		//	 	Create animation
		
		/*anim = new Animation();
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
		*/
	}
	
	
	private Image loadImage(String fileName) {
		
		fileName 		= "/images/" + fileName;
		
		URL resource 	= ImageTest.class.getResource(fileName);
		
		
		try{
			
			return Toolkit.getDefaultToolkit().getImage(resource);
			
		}
		
		catch(Exception e) {
			System.err.println("(!) ERROR: Failed to load file " + fileName);
			return null;
		}
		
		finally {
			if(resource != null) {
				System.out.println("(*) Loading " + resource);
			}
		}
		
		
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
