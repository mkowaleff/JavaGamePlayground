import java.awt.*;
//import com.brackeen.javagamebook.graphics.ScreenManager;
import java.net.URL;


// 	Simple abstract class used for testing. Sublcasses should implement the draw() method
public abstract class GameCore {
	
	protected static final int FONT_SIZE = 24;
	
	private static final DisplayMode POSSIBLE_MODES[] = {
			new DisplayMode(1920, 1080, 32, 0),
			new DisplayMode(1920, 1080, 24, 0),
			new DisplayMode(1920, 1080, 16, 0),
			new DisplayMode(1280, 720, 32, 0),
			new DisplayMode(1280, 720, 24, 0),
			new DisplayMode(1280, 720, 16, 0)
	};
	
	private boolean isRunning;
	protected ScreenManager screen;
	
	// 	Signals the game loop that it's time to quit
	public void stop() {
		isRunning = false;
	}
	
	
	// 	Calls init() and gameLoop()
	public void run() {
		try {
			init();
			gameLoop();
		}
		finally {
			screen.restoreScreen();
		}
	}
	
	
	// 	Sets full screen mode and initiates and objects.
	public void init() {
		screen = new ScreenManager();
		DisplayMode displayMode = screen.findFirstCompatibleMode(POSSIBLE_MODES);
		screen.setFullScreen(displayMode);
		
		Window window = screen.getFullScreenWindow();
		window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
		window.setBackground(Color.blue);
		window.setForeground(Color.white);
		
		isRunning = true;
	}
	
	
	public Image loadImage(String fileName) {
		URL resource = ImageTest.class.getResource("/images/" + fileName);
		System.out.println("(*) Loading " + resource);
		return Toolkit.getDefaultToolkit().getImage(resource);
	}
	
	
	// 	Runs through the game loop until stop() is called.
	public void gameLoop() {
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		
		while(isRunning) {
			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;
			
			// Update
			update(elapsedTime);
			
			
			// Draw the screen
			Graphics2D g = screen.getGraphics();
			draw(g);
			g.dispose();
			screen.update();
			
			// Take a nap
			try {
				Thread.sleep(20);
			}
			catch (InterruptedException ex) { }
		}
	}
	
	
	// 	Updates the state of the game/animation based on the amount of elapsed time that has passed.
	public void update(long elapsedTime) {
		// do nothing
	}
	
	
	// 	Draws to the screen. Subclasses must override this method.
	public abstract void draw(Graphics2D g);

}
