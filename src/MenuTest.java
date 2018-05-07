import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Extends the InputManagerTest demo and adds Swing buttons for pause, settings and quit.

public class MenuTest extends InputManagerTest implements ActionListener {

	
	public static void main(String[] args) {
		new MenuTest().run();
	}
	
	
	protected GameAction configAction;
	
	private JButton playButton;
	private JButton configButton;
	private JButton quitButton;
	private JButton pauseButton;
	private JPanel playButtonSpace;
	
	public void init() {
		super.init();
		
		// make sure Swing components don't paint themselves by calling the NullRepaintManager
		// class that I made
		NullRepaintManager.install();
		
		// create an additional GameAction for "config"
		configAction = new GameAction("config");
		
		// create buttons
		quitButton = createButton("quit", "Quit");
		playButton = createButton("play", "Continue");
		pauseButton = createButton("pause", "Pause");
		configButton = createButton("config", "Change Settings");
		
		// create the space where the play/pause buttons go
		playButtonSpace = new JPanel();
		playButtonSpace.setOpaque(false);
		playButtonSpace.add(pauseButton);
		
		JFrame frame = super.screen.getFullScreenWindow();
		Container contentPane = frame.getContentPane();
		
		// make sure the content pane is transparent
		if (contentPane instanceof JComponent) {
			((JComponent)contentPane).setOpaque(false);
		}
		
		// add components to the screen's content pane
		contentPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		contentPane.add(playButtonSpace);
		contentPane.add(configButton);
		contentPane.add(quitButton);
		
		// explicitly lay out components (needed on some systems)
		frame.validate();
	}
	
	
	// Extends InputManagerTest's functionality to draw all Swing components.
	public void draw(Graphics2D g) {
		super.draw(g);
        JFrame frame = super.screen.getFullScreenWindow();
		
		// the layered pane contains things like popups (tooltips,
		// popup menus) and the content pane.
		frame.getLayeredPane().paintComponents(g);	
	}
	
	
	// Changes the pause/play button whenever the pause state changes
	public void setPaused(boolean p) {
		super.setPaused(p);
		playButtonSpace.removeAll();
		if( isPaused()) {
			playButtonSpace.add(playButton);
		}
		else {
			playButtonSpace.add(pauseButton);
		}
	}
	
	
	// Called by the AWT event dispatch thread when a button is pressed.
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		
		if(src == quitButton) {
			// fire the "exit" gameAction
			super.exit.tap();
		}
		else if (src == configButton) {
			// doesn't do anything (for now)
			configAction.tap();
		}
		else if (src == playButton || src == pauseButton) {
			// fire the "pause" gameAction
			super.pause.tap();
		}
	}
	
	
	/* Creates a Swing JButton. The image used for the button is located at
	 * "../images/menu/" + ".png". The image is modified to create a "default"
	 * look (translucent) and a "pressed" look (moved down and to the right).
	 * The Button doesn't use Swing's look-and-feel and instead just uses the
	 * image.
	 */
	public JButton createButton(String name, String tooltip) {
		// load the image
		String imagePath = "../images/menu/" + name + ".png";
		ImageIcon iconRollover = new ImageIcon(imagePath);
		int w = iconRollover.getIconWidth();
		int h = iconRollover.getIconHeight();
		
		// get the cursor for this button
		Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
		
		// make translucent default image
		Image image = screen.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		Graphics2D g = (Graphics2D)image.getGraphics();
		Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
		g.setComposite(alpha);
		g.drawImage(iconRollover.getImage(), 0, 0, null);
		g.dispose();
		ImageIcon iconDefault = new ImageIcon(image);
		
		// make a pressed image
		image = screen.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		g = (Graphics2D)image.getGraphics();
		g.drawImage(iconRollover.getImage(), 2, 2, null);
		g.dispose();
		ImageIcon iconPressed = new ImageIcon(image);
		
		// create the button
		JButton button = new JButton();
		button.addActionListener(this);
		button.setIgnoreRepaint(true);
		button.setFocusable(false);
		button.setToolTipText(tooltip);
		button.setBorder(null);
		button.setContentAreaFilled(false);
		button.setCursor(cursor);
		button.setIcon(iconDefault);
		button.setPressedIcon(iconPressed);
		
		return button;
	}
}
