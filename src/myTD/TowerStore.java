package myTD;

import java.awt.Color;
import java.awt.Graphics;

public class TowerStore{
	
	//**********************Fields************************
	
	//Properties
	private int x;				//x location in pixels
	private int y;				//y location in pixels
	private int storeWidth;		//width in pixels
	private int storeHeight;	//height in pixels
	
	//State variables
	Tower[] storeTowers;		//Holds the storeMode towers
	
	//**********************Constructor************************
	
	public TowerStore(int tileSize, int numTowerTypes) {
		
		//set store to 80% of parent width and 15% of height
		storeWidth = tileSize * numTowerTypes;
		storeHeight = tileSize + 15;		//Buffer to leave room for price
		
		//Position at bottom of panel
		x = (int)((GamePanel.PIXEL_WIDTH - storeWidth) / 2);
		y = (int)(GamePanel.PIXEL_HEIGHT - tileSize);
		
		//fill tower types
		int towerX = x;
		storeTowers = new Tower[numTowerTypes];
		for(int i = 0; i < numTowerTypes; i++) {
			storeTowers[i] = new Tower(i, 
					towerX + (tileSize * i), 
					y, tileSize, true);
		}
	}
	
	//**********************Update and draw************************
	
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(x,  y - 15,  storeWidth,  storeHeight);
		
		for(Tower tower: storeTowers) {
			tower.draw(g);
			
			//Draw price
			g.drawString("$" + tower.getCost(), tower.getX(), tower.getY() - 5);
		}
	}
	
	//**********************Game methods************************
	
	//Checks to see if coordinates are within the store.
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
	
	//Returns a reference to the tower that the given coordinates are within. null if nothing
	public Tower getTowerAt(int getX, int getY) {
		for(Tower tower: storeTowers) {
			if(tower.coordsInTower(getX,  getY)) {
				return tower;
			}
		}
		return null;
	}
}
