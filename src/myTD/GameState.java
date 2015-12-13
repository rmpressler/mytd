package myTD;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public abstract class GameState {
	protected GameStateManager gsm;
	@SuppressWarnings("unused")
	private boolean active;
	
	public void setActive() {
		active = true;
	}
	public void setInactive() {
		active = false;
	}
	
	public abstract HashMap<String,String> getStateData();
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void mousePressed(MouseEvent e);
	public abstract void mouseReleased(MouseEvent e);
	public abstract void mouseMoved(MouseEvent e);
	public abstract void mouseDragged(MouseEvent e);
	public abstract void keyPressed(int e);
}