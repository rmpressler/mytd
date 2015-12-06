package myTD;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Tower {
	private int x;
	private int y;
	private int type;
	private int level;
	private int dmg;
	private int rateOfFire;
	private int fireTimer;
	private int range;
	private int cost;
	private Color color;
	
	private int tileSize;
	private int towerSize;
	
	public boolean placing;
	public boolean storeMode;
	private boolean firing;
	
	private int mouseX;
	private int mouseY;
	
	private Enemy target;
	private ArrayList<Projectile> projectiles;
	private int firingTimer;
	
	public Tower(int newType, int startX, int startY, int newTileSize, boolean thisStoreMode) {
		x = startX;
		y = startY;
		tileSize = newTileSize;
		towerSize = (int) (tileSize * .8);
		placing = true;
		firing = false;
		storeMode = thisStoreMode;
		type = newType;
		cost = 100;
		
		projectiles = new ArrayList<Projectile>();
		
		//FOR TESTING:
		color = Color.RED;
		range = (int)(2.5 * tileSize);
		dmg = 1;
		
		if(storeMode) {
			this.place(x,  y);
		}
	}
	
	public int getCost() {
		return cost;
	}
	
	public void move(int newX, int newY) {
		x = newX;
		y = newY;
	}
	
	//method for placing active towers
	public void place () {
		x = toTowerCoord(mouseX);
		y = toTowerCoord(mouseY);
		placing = false;
	}
	
	//method for placing store towers
	public void place (int newX, int newY) {
		x = newX + (int)(tileSize * 0.1);
		y = newY + (int)(tileSize * 0.1);
		placing = false;
	}
	
	public boolean isInRange(Enemy enemy) {
		int dx;		//x distance from tower
		int dy;		//y distance from tower
		double d;		//distance from tower
		
		dx = Math.abs((enemy.getX() + enemy.getR()) - (x + (towerSize / 2)));
		dy = Math.abs((enemy.getY() + enemy.getR()) - (y + (towerSize / 2)));
		d = Math.sqrt((dx * dx) + (dy * dy));
		
		if(d < range) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void fire() {
		projectiles.add(new Projectile(x + (towerSize / 2), y + (towerSize / 2), dmg, target));
	}
	
	public void update(int newX, int newY, ArrayList<Enemy> enemies) {
		
		//if tower in placing mode, move tower with mouse
		if(placing) {
			mouseX = newX;
			mouseY = newY;
			return;
		}
		
		//if tower is not firing, check for enemies in range and 
		//begin firing if found
		if(!firing && enemies != null) {
			int dx;		//x distance from tower
			int dy;		//y distance from tower
			double d;		//distance from tower
			
			Enemy closest = null;	//current closest enemy
			double closestD = range;
			for(Enemy enemy: enemies) {
				//get distance from tower
				dx = Math.abs(enemy.getX() - x);
				dy = Math.abs(enemy.getY() - y);
				d = Math.sqrt((dx * dx) + (dy * dy));
				
				//if distance is within this tower's range and is closest,
				//set it to closest.
				if(isInRange(enemy) && d < closestD) {
					closest = enemy;
					closestD = d;
				}
			}
			
			if(closest != null) {
				//target found. FIRE!
				target = closest;
				firing = true;
				firingTimer = 59;
			}
		}	//if firing, check to see if enemy is dead or out of range
		else {
			//if target has died or gone out of range, stop firing
			if(!target.isAlive() || !isInRange(target)) {
				target = null;
				firing = false;
			}	//otherwise, keep firing
			else {
				//Fire every two seconds
				if(firingTimer == 60) {
					fire();
					firingTimer = 0;
				}
				else {
					firingTimer++;
				}
			}
		}
		
		//move all existing projectiles
		if(projectiles.size() > 0) {
			for(int i = 0; i < projectiles.size(); i++) {
				if(projectiles.get(i).getHit() ||
						projectiles.get(i).getTarget() == null ||
						!projectiles.get(i).getTarget().isAlive()) {
					projectiles.remove(projectiles.get(i));
				}
				else {
					projectiles.get(i).update();
				}
			}
		}
	}
	
	public int getType() {
		return type;
	}
	
	public boolean coordsInTower(int coordX, int coordY) {
		if(coordX > x &&
				coordX < x + tileSize * 0.8 &&
				coordY > y &&
				coordY < y + tileSize * 0.8) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public int toTowerCoord(int i) {
		int temp = i / tileSize;
		return (temp * tileSize) + ((int)(tileSize * 0.1));
	}
	
	public void draw(Graphics g) {
		if(placing) {
			int newX = toTowerCoord(mouseX);
			int newY = toTowerCoord(mouseY);
			g.setColor(new Color(255, 0, 0, 180));
			g.fillRect(newX,  newY,  towerSize, towerSize);
			g.setColor(Color.RED);
			g.drawOval((newX + (towerSize / 2)) - range, (newY + (towerSize / 2)) - range, range * 2, range * 2);
		}
		else {
			g.setColor(new Color(255, 0, 0));
			g.fillRect(x,  y,  towerSize, towerSize);
			
			if(projectiles.size() > 0) {
				for(Projectile projectile: projectiles) {
					projectile.draw(g);
				}
			}
		}
	}
}
