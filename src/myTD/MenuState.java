package myTD;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MenuState extends GameState {
	private String[] options = {
			"PLAY",
			"MAP EDITOR",
			"CREDITS"
	};
	private int selected;

	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
	}
	
	@Override
	public void init() {
		selected = 0;
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN.darker());
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
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void keyPressed(int e) {
		if(e == KeyEvent.VK_ENTER) {
			gsm.setState(selected + 1);
		}
		else if(e == KeyEvent.VK_UP) {
			if(selected > 0)
				selected--;
		}
		else if(e == KeyEvent.VK_DOWN) {
			if(selected < options.length - 1)
			selected++;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
}
