package myTD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class TileEditorPanel extends JPanel implements MouseListener, 
	MouseMotionListener {
	
	private static final long serialVersionUID = 1L;

	TDMap tileMap;
	
	private static int WIDTH;
	private static int HEIGHT;
	private static final int TILE_SIZE = 40;
	private static int PIX_WIDTH;
	private static int PIX_HEIGHT;
	
	private boolean dragging;
	private boolean dragged;
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	
	public TileEditorPanel(int newWidth, int newHeight) {
		WIDTH = newWidth;
		HEIGHT = newHeight;
		PIX_WIDTH = WIDTH * TILE_SIZE;
		PIX_HEIGHT = HEIGHT * TILE_SIZE;
		this.setPreferredSize(new Dimension(PIX_WIDTH, PIX_HEIGHT));
		tileMap = new TDMap(PIX_WIDTH, PIX_HEIGHT, TILE_SIZE, true);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		tileMap.draw(g);
		
		g.setColor(Color.BLACK);
		g.drawRect(0,  0, PIX_WIDTH - 1, PIX_HEIGHT - 1);
		
		for(int i = 0; i < WIDTH; i++) {
			g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, PIX_HEIGHT);
		}
		for(int i = 0; i < HEIGHT; i++) {
			g.drawLine(0, i * TILE_SIZE, PIX_WIDTH, i * TILE_SIZE);
		}
		
		if(dragging) {
			g.drawRect(startX, 
					startY, 
					endX - startX, 
					endY - startY);
		}
		
	}
	
	private void fillTiles() {
		/* 1               2
		 *  |-------------|
		 *  |             |
		 *  |             |
		 *  |-------------|
		 * 4               3
		 */
		
		//Fill top row
		tileMap.setTile(
				startX / TILE_SIZE, 
				startY / TILE_SIZE, 
				EditorPanel.getSelection()
				);
		int horizTiles = ((endX - startX) / TILE_SIZE) + 1;
		int vertTiles = ((endY - startY) / TILE_SIZE) + 1;
		int topLeftX = startX / TILE_SIZE;
		int topLeftY = startY / TILE_SIZE;
		
		for(int i = topLeftX; i < topLeftX + horizTiles; i++) {
			for(int j = topLeftY; j < topLeftY + vertTiles; j++) {
				tileMap.setTile(
						i, 
						j, 
						EditorPanel.getSelection()
						);
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent evt) {}

	@Override
	public void mouseEntered(MouseEvent evt) {}

	@Override
	public void mouseExited(MouseEvent evt) {}

	@Override
	public void mousePressed(MouseEvent evt) {
		int x = evt.getX() / TILE_SIZE;
		int y = evt.getY() / TILE_SIZE;
		
		dragging = true;
		
		startX = evt.getX();
		startY = evt.getY();
		
		int selection = EditorPanel.getSelection();
		
		tileMap.setTile(x,  y,  selection);
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		if(dragged) {
			fillTiles();
			dragged = false;
		}
		endX = 0;
		endY = 0;
		dragging = false;
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		if(dragging) {
			endX = evt.getX();
			endY = evt.getY();
			dragged = true;
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent evt) {}
	
}
