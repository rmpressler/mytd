package myTD;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class CreditState extends GameState {
	String[] creditTitles = {
			"Lead Developer",
			"Lead Designer"
	};
	String[] creditNames = {
			"Richard Pressler",
			"Richard Pressler"
	};
	
	public CreditState(GameStateManager gsm) {
		super.gsm = gsm;
		
		init();
	}

	@Override
	public HashMap<String, String> getStateData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g) {
		//Set background
		g.setColor(new Color(0, 0, 180, 180));
		g.fillRect(0, 0, GamePanel.PIXEL_WIDTH, GamePanel.PIXEL_HEIGHT);
		
		Font oldFont = g.getFont();
		g.setFont(new Font("Comic Sans", Font.BOLD, 20));
		
		int stringHeight = g.getFontMetrics().getHeight();
		int baseY = ((GamePanel.PIXEL_HEIGHT - ((int)(stringHeight * 2.5) * creditTitles.length)) / 2) + stringHeight;
		for(int i = 0; i < creditTitles.length; i++) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Comic Sans", Font.BOLD, 16));
			
			String string = creditTitles[i];
			int stringLength = g.getFontMetrics().stringWidth(string);
			int stringX = (GamePanel.PIXEL_WIDTH - stringLength) / 2;
			int stringY = baseY + (i * (int)(stringHeight * 2.5));
			
			g.drawString(creditTitles[i], stringX, stringY);
			
			g.setFont(new Font("Comic Sans", Font.BOLD, 20));
			
			string = creditNames[i];
			stringLength = g.getFontMetrics().stringWidth(string);
			stringX = (GamePanel.PIXEL_WIDTH - stringLength) / 2;
			stringY = baseY + (i * (int)(stringHeight * 2.5) + 25);
			
			g.drawString(creditNames[i], stringX, stringY);
		}
		
		g.setFont(oldFont);
	}

	@Override
	public void keyPressed(int e) {
		gsm.setState(GameStateManager.MENUSTATE);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseMoved(MouseEvent e) {}
	@Override
	public void mouseDragged(MouseEvent e) {}

}
