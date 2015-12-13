package myTD;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class GameOverState extends GameState {
	private int lastWave;
	
	public GameOverState(GameStateManager gsm) {
		this.gsm = gsm;
		
		init();
	}

	@Override
	public void init() {
		lastWave = 0;
	}

	@Override
	public void update() {
		lastWave = Integer.parseInt(gsm.getStateData(GameStateManager.PLAYSTATE).get("wave"));
	}

	@Override
	public void draw(Graphics2D g) {
		gsm.getState(GameStateManager.PLAYSTATE).draw(g);
		
		String[] lines = new String[3];
		lines[0] = "GAME OVER";
		lines[1] = "Reached wave " + lastWave + ".";
		lines[2] = "Press Enter to return to menu.";
		
		g.setColor(new Color(0, 0, 0, 180));
		g.fillRect(0, 0, GamePanel.PIXEL_WIDTH, GamePanel.PIXEL_HEIGHT);
		
		Font oldFont = g.getFont();
		g.setFont(new Font("Comic Sans", Font.BOLD, 20));
		
		int stringHeight = g.getFontMetrics().getHeight();
		int baseY = ((GamePanel.PIXEL_HEIGHT - stringHeight) / 2) + stringHeight;

		for(int i = 0; i < lines.length; i++) {
			g.setColor(Color.WHITE);
			
			String string = lines[i];
			int stringLength = g.getFontMetrics().stringWidth(string);
			int stringX = (GamePanel.PIXEL_WIDTH - stringLength) / 2;
			int stringY = baseY + (i * (int)(stringHeight * 1.2));
			
			g.drawString(lines[i], stringX, stringY);
		}
		
		g.setFont(oldFont);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(int e) {
		if(e == KeyEvent.VK_ENTER) {
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}

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
