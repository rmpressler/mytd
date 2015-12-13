package myTD;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public abstract class GameState {
	protected GameStateManager gsm;
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void mousePressed(MouseEvent e);
	public abstract void mouseMoved(MouseEvent e);
	public abstract void keyPressed(int e);
}