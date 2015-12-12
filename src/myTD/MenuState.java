package myTD;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class MenuState extends GameState {

	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, GamePanel.PIXEL_WIDTH, GamePanel.PIXEL_HEIGHT);
		g.setColor(Color.BLACK);
		g.drawString("Click anywhere to play", GamePanel.PIXEL_WIDTH / 2 - 50, GamePanel.PIXEL_HEIGHT / 2 + 5);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		//Start game
		gsm.setState(GameStateManager.PLAYSTATE);
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
}
