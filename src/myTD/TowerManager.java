package myTD;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class TowerManager implements MouseListener {
	
	//******************Fields*****************
	
	//Properties
	private int tileSize;				//Tile size in pixels from GamePanel
	
	//State data
	ArrayList<Tower> towers;			//Array containing current towers being tracked
	Tower placingTower;					//Tower currently being placed by player.
	private boolean placing;			//True when player is placing a tower.
	
	//References to related objects
	private Player player;				//Reference to player for handling money.
	
	//Sub-components
	TowerStore store;					//Store that is managed by this class
	
	//*****************Constructor*****************
	
	public TowerManager(GamePanel game, int newTileSize, Player newPlayer) {
		
		//Init
		towers = new ArrayList<Tower>();
		
		placingTower = null;
		
		tileSize = newTileSize;
		player = newPlayer;
		
		placing = false;
		
		store = new TowerStore(game, newTileSize, 2);
	}
	
	//********************Update and draw*********************
	
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
	
	//**************************Getters**********************************
	
	public int getPlacingTowerCost() {
		return placingTower.getCost();
	}
	
	//************************Game methods*******************************
	
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

	//****************************Event handlers****************************
	
	@Override
	public void mousePressed(MouseEvent e) {
		Tower clickedTower = store.getTowerAt(e.getX(), e.getY());
		
		if(clickedTower != null && clickedTower.getCost() <= player.getMoney()) {
			createTower(clickedTower.getType(), e.getX(), e.getY());
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}
