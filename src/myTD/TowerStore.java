package myTD;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TowerStore implements MouseListener{
	private int x;
	private int y;
	private int storeWidth;
	private int storeHeight;
	
	Tower[] storeTowers;
	
	public TowerStore(GamePanel newGame, int tileSize, int numTowerTypes) {
		
		//set store to 80% of parent width and 15% of height
		storeWidth = tileSize * numTowerTypes;
		storeHeight = tileSize;
		
		//Position 70% of the way down the panel, 10% from the left edge
		x = (int)((newGame.PIXEL_WIDTH - storeWidth) / 2);
		y = (int)(newGame.PIXEL_HEIGHT - tileSize);
		
		//fill tower types
		int towerX = x;
		storeTowers = new Tower[numTowerTypes];
		for(int i = 0; i < numTowerTypes; i++) {
			storeTowers[i] = new Tower(i, 
					towerX + (tileSize * i), 
					y, tileSize, true);
		}
	}
	
	public boolean isInStore(int checkX, int checkY) {
		if(checkX >= x &&
				checkX <= x + storeWidth &&
				checkY >= y &&
				checkY <= y + storeHeight) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public Tower getTowerAt(int getX, int getY) {
		for(Tower tower: storeTowers) {
			if(tower.coordsInTower(getX,  getY)) {
				return tower;
			}
		}
		return null;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(x,  y,  storeWidth,  storeHeight);
		
		for(Tower tower: storeTowers) {
			tower.draw(g);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
