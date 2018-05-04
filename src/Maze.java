
public class Maze {

	private int playerX;
	private int playerY;
	
	public synchronized boolean isAtExit() {
		return (playerX == 0 && playerY == 0);
	}
	
	public synchronized void setPosition(int x, int y) {
		playerX = x;
		playerY = y;
	}
}
