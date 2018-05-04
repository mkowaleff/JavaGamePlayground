//import java.applet.Applet;

public class Game /*extends Applet*/ implements Runnable {
	
	//Thread t;
	
	public Game() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		System.out.println("Do something cool here.");
		System.out.println("Hello World!");
	}
}
