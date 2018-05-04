import java.awt.*;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class ImageSpeedTest extends JFrame {

	
	public static void main(String args[]) {
		DisplayMode displayMode;
		
		if(args.length == 3) {
			displayMode = new DisplayMode(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), DisplayMode.REFRESH_RATE_UNKNOWN);				
		}
		
		else {
			displayMode = new DisplayMode(1920, 1080, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		}
		
		ImageSpeedTest test = new ImageSpeedTest();
		test.run(displayMode);
	}
	
	private static final int FONT_SIZE = 24;
	private static final long TIME_PER_IMAGE = 1500;
	
	private SimpleScreenManager screen;
	private Image bgImage;
	private Image opaqueImage;
	private Image transparentImage;
	private Image translucentImage;
	private Image antiAliasedImage;
	private boolean imagesLoaded;
	
	public void run(DisplayMode displayMode) {
		setBackground(Color.blue);
		setForeground(Color.white);
		setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
		imagesLoaded = false;
		
		screen = new SimpleScreenManager();
		
		try {
			screen.setFullScreen(displayMode, this);
			
			synchronized(this) {
				loadImages();
				
				try {
					wait();
				}
				
				catch (InterruptedException ex) { }
			}
		}
		
		finally {
			screen.restoreScreen();
		}
	}
	
	public void loadImages() {
		System.out.print("Loading background...");
		bgImage = loadImage("background.jpg");
		System.out.println("COMPLETE");
		System.out.println();
		
		System.out.println("Loading opaque...");
		opaqueImage = loadImage("opaque.png");
		System.out.println("COMPLETE");
		System.out.println();
		
		System.out.println("Loading transparent...");
		transparentImage = loadImage("transparent.png");
		System.out.println("COMPLETE");
		System.out.println();
		
		System.out.println("Loading translucent...");
		translucentImage = loadImage("translucent.png");
		System.out.println("COMPLETE");
		System.out.println();
		
		System.out.println("Loading antialiased...");
		antiAliasedImage = loadImage("antialiased.png");
		System.out.println("COMPLETE");
		System.out.println();
		
		imagesLoaded = true;
		System.out.println();
		System.out.println();
		System.out.println("(*)		CALLING REPAINT");
		repaint();
	}
	
	private final Image loadImage(String fileName) {
		URL resource = ImageTest.class.getResource("/images/" + fileName);
		System.out.println("fileName: " + fileName);
		System.out.println("URL: " + resource);
		return Toolkit.getDefaultToolkit().getImage(resource);
	}
	
	public void paint(Graphics g) {
		
		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		
		if(imagesLoaded) {
			g.drawImage(bgImage, 0, 0, this);
			drawImage(g, opaqueImage, "Opaque");
			drawImage(g, opaqueImage, "Transparent");
			drawImage(g, opaqueImage, "Translucent");
			drawImage(g, opaqueImage, "Translucent (Anti-Aliased)");
			
			synchronized(this) {
				notify();
			}
		}
		
		else{
			g.drawString("Loading Images...", 50, 50);
		}
	}
	
	public void drawImage(Graphics g, Image image, String name) {
		int width = screen.getFullScreenWindow().getWidth() - image.getWidth(null);
		int height = screen.getFullScreenWindow().getHeight() - image.getHeight(null);
		int numImages = 0;
		
		g.drawImage(bgImage, 0, 0, null);
		
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < TIME_PER_IMAGE) {
			int x = Math.round( (float)Math.random() * width);
			int y = Math.round( (float)Math.random() * height);
			g.drawImage(image, x, y, null);
			numImages++;
		}
		
		long time = System.currentTimeMillis() - startTime;
		float speed = numImages * 1000f / time;
		System.out.println(name + ": " + speed + " images/sec");
	}
}
