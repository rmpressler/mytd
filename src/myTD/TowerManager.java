package myTD;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class TowerManager implements MouseListener {
	ArrayList<Tower> towers;
	Tower placingTower;
	EnemyManager eManager;
	private boolean placing;
	private int tileSize;
	private int balance;
	private Player player;
	
	TowerStore store;
	
	public TowerManager(int parentWidth, int parentHeight, int newTileSize, Player newPlayer) {
		towers = new ArrayList<Tower>();
		placingTower = null;
		tileSize = newTileSize;
		placing = false;
		player = newPlayer;
		
		store = new TowerStore(parentWidth, parentHeight, newTileSize, 2);
	}
	
	public int getPlacingTowerCost() {
		return placingTower.getCost();
	}
	
	public boolean placing() {
		return placing;
	}
	
	public void createTower(int newType, int x, int y) {
		placingTower = new Tower(newType, x, y, tileSize, false);
		placing = true;
	}
	
	public void placeTower() { 
		placingTower.place();
		towers.add(placingTower);
		placingTower = null;
		placing = false;
	}
	
	public boolean isInStore(int checkX, int checkY) {
		return store.isInStore(checkX, checkY);
	}
	
	public void update(int x, int y, ArrayList<Enemy> livingEnemies) {
		
		if(placing) {
			placingTower.update(x, y, null);
		}
		for(Tower tower: towers) {
			tower.update(0, 0, livingEnemies);
		}
	}
	
	public void draw(Graphics g) {
		if(placing) {
			placingTower.draw(g);
		}
		
		for(Tower tower: towers) {
			tower.draw(g);
		}
		
		store.draw(g);
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
		Tower clickedTower = store.getTowerAt(e.getX(), e.getY());
		
		if(clickedTower != null && clickedTower.getCost() <= player.getMoney()) {
			createTower(clickedTower.getType(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
