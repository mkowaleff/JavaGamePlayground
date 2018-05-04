import java.awt.*;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class ImageTest extends JFrame {
	
	public static void main(String[] args) {
		
		DisplayMode displayMode;
		
		if(args.length == 3) {
			displayMode = new DisplayMode(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), DisplayMode.REFRESH_RATE_UNKNOWN);				
		}
		
		else {
			displayMode = new DisplayMode(1920, 1080, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		}
		
		ImageTest test = new ImageTest();
		test.run(displayMode);
	}
	
	private static final int FONT_SIZE = 24;
	private static final long DEMO_TIME = 100000;
	
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
			loadImages();
			try {
				Thread.sleep(DEMO_TIME);
			}
			catch (InterruptedException ex) { }
		}
		
		finally {
			screen.restoreScreen();
		}
	}
	
	public void loadImages() {
		System.out.print("Loading background...");
		bgImage = loadImage("background.jpg");
		System.out.println("COMPLETE");
		
		System.out.println("Loading opaque...");
		opaqueImage = loadImage("opaque.png");
		System.out.println("COMPLETE");
		
		System.out.println("Loading transparent...");
		transparentImage = loadImage("transparent.png");
		System.out.println("COMPLETE");
		
		System.out.println("Loading translucent...");
		translucentImage = loadImage("translucent.png");
		System.out.println("COMPLETE");
		
		System.out.println("Loading antialiased...");
		antiAliasedImage = loadImage("antialiased.png");
		System.out.println("COMPLETE");
		
		imagesLoaded = true;
		
		System.out.println("(*)		CALLING REPAINT");
		repaint();
	}
	
	private Image loadImage(String fileName) {
		//return new ImageIcon(fileName).getImage();
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
			drawImage(g, opaqueImage, 50, 50, "Opaque");
			drawImage(g, opaqueImage, 320, 50, "Transparent");
			drawImage(g, opaqueImage, 50, 300, "Translucent");
			drawImage(g, opaqueImage, 320, 300, "Translucent (Anti-Aliased)");
			
		}
		
		else {
			g.drawString("Loading Images...", 50, 50);
			
		}
	}
	
	public void drawImage(Graphics g, Image image, int x, int y, String caption) {
		g.drawImage(image, x, y, null);
		g.drawString(caption, x+5, y + FONT_SIZE + image.getHeight(null));
	}

}
