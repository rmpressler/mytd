package myTD;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class TowerManager implements MouseListener {
	
	//******************Fields*****************
	
	//Properties
	private int tileSize;				//Tile size in pixels from GamePanel
	
	//State data
	private ArrayList<Tower> towers;			//Array containing current towers being tracked
	private Tower selectedTower;					//Holds currently selected tower.
	private Tower placingTower;					//Tower currently being placed by player.
	private boolean placing;					//True when player is placing a tower.
	
	//References to related objects
	private Player player;				//Reference to player for handling money.
	private TDMap tileMap;
	
	//Sub-components
	TowerStore store;					//Store that is managed by this class
	
	//*****************Constructor*****************
	
	public TowerManager(GamePanel game, TDMap tMap, Player newPlayer) {
		
		//Init
		towers = new ArrayList<Tower>();
		
		placingTower = null;
		selectedTower = null;
		
		tileSize = tMap.getTileSize();
		player = newPlayer;
		tileMap = tMap;
		
		placing = false;
		
		store = new TowerStore(game, tileSize, 2);
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
			if(tower != selectedTower) {
				tower.draw(g);
			}
		}
		if(selectedTower != null) {
			selectedTower.draw(g);
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
	
	public Tower getTowerAt(int getX, int getY) {
		for(Tower tower: towers) {
			if(tower.coordsInTower(getX,  getY)) {
				return tower;
			}
		}
		return null;
	}

	//****************************Event handlers****************************
	
	@Override
	public void mousePressed(MouseEvent e) {
		
		//If placing and location is valid, place and quit
		if(placing) {
			//If right click, cancel operation
			if(SwingUtilities.isRightMouseButton(e)) {
				placingTower = null;
				placing = false;
				return;
			}
			
			//Get tile location
			int xTile = e.getX() / tileSize;
			int yTile = e.getY() / tileSize;
			
			//Check for valid tile and place
			if(!tileMap.isPath(xTile, yTile) &&
					!isInStore(e.getX(), e.getY()) &&
					getTowerAt(e.getX(), e.getY()) == null) {
				player.subtractMoney(placingTower.getCost());
				placeTower();
			}
			return;
		}
		
		//Do nothing if right-click
		if(SwingUtilities.isRightMouseButton(e)) {
			return;
		}
		
		//Check for an in-store click
		Tower clickedTower = store.getTowerAt(e.getX(), e.getY());
		if(clickedTower != null && clickedTower.getCost() <= player.getMoney()) {
			//Start placing new tower
			createTower(clickedTower.getType(), e.getX(), e.getY());
			return;
		}
		
		//Check for option window click
		if(selectedTower != null) {
			if(selectedTower.coordsInOption(e.getX(), e.getY())) {
				//Option clicked, upgrade
				player.subtractMoney(selectedTower.getCost());
				selectedTower.upgrade();
				selectedTower.deselect();
				selectedTower = null;
				return;
			}
		}
		
		//Check for an existing tower click
		clickedTower = getTowerAt(e.getX(), e.getY());
		if(clickedTower != null) {
			
			//deselect current selection
			if(selectedTower != null) {
				selectedTower.deselect();
				selectedTower = null;
			}
			
			//Select tower; show options
			selectedTower = clickedTower;
			selectedTower.select();
			
			return;
		}

		//Nothing clicked, deselect tower, if any
		if(selectedTower != null) {
			selectedTower.deselect();
			selectedTower = null;
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
