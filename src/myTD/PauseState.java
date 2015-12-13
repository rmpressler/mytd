package myTD;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class PauseState extends GameState {
	private String[] options = {
			"RESUME",
			"START OVER",
			"QUIT"
	};
	private int selected;
	
	public PauseState(GameStateManager gsm) {
		this.gsm = gsm;
		
		init();
	}

	@Override
	public void init() {
		selected = 0;
	}

	@Override
	public void update() {}

	@Override
	public void draw(Graphics2D g) {
		gsm.getState(GameStateManager.PLAYSTATE).draw(g);
		
		g.setColor(new Color(0, 0, 0, 180));
		g.fillRect(0, 0, GamePanel.PIXEL_WIDTH, GamePanel.PIXEL_HEIGHT);
		
		Font oldFont = g.getFont();
		g.setFont(new Font("Comic Sans", Font.BOLD, 20));
		
		int stringHeight = g.getFontMetrics().getHeight();
		int baseY = ((GamePanel.PIXEL_HEIGHT - ((int)(stringHeight * 1.2) * options.length)) / 2) + stringHeight;
		for(int i = 0; i < options.length; i++) {
			if(i == selected) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.WHITE);
			}
			
			String string = options[i];
			int stringLength = g.getFontMetrics().stringWidth(string);
			int stringX = (GamePanel.PIXEL_WIDTH - stringLength) / 2;
			int stringY = baseY + (i * (int)(stringHeight * 1.2));
			
			g.drawString(options[i], stringX, stringY);
		}
		
		g.setFont(oldFont);
	}
	
	@Override
	public void keyPressed(int e) {
		switch(e) {
			case KeyEvent.VK_ESCAPE:
				gsm.setState(GameStateManager.PLAYSTATE);
				break;
			case KeyEvent.VK_ENTER:
				switch(selected) {
					case 0:
						gsm.setState(GameStateManager.PLAYSTATE);
						break;
					case 1:
						gsm.getState(GameStateManager.PLAYSTATE).init();
						gsm.setState(GameStateManager.PLAYSTATE);
						break;
					case 2:
						gsm.setState(GameStateManager.MENUSTATE);
				}
				break;
			case KeyEvent.VK_UP:
				if(selected > 0) {
					selected--;
				}
				break;
			case KeyEvent.VK_DOWN:
				if(selected < options.length - 1) {
					selected++;
				}
				break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public HashMap<String, String> getStateData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
