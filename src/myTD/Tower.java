package myTD;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Tower {
	
	//******************Fields*****************
	
	private int x;								//x coordinate in pixels
	private int y;								//y coordinate in pixels
	private int type;							//type of tower. See constructor for valid types
	private int attackType;						//Uses constants below to set attack type
	private int level;							//current level of tower
	private int dmg;							//damage per hit
	private int rateOfFire;						//rate of fire in milliseconds between shots
	private int range;							//Range in pixels
	private int cost;							//cost
	private Color color1;						//color of tower
	private Color color2;						//color of tower during placing
	
	private long lastShot;						//last shot in milliseconds
	
	private int tileSize;						//tile size in pixels
	private int towerSize;						//tower size in pixels
	
	public boolean placing;						//true when tower is being placed
	public boolean storeMode;					//true when tower is only to be displayed in store
	private boolean firing;						//true when firing
	
	private int mouseX;							//x coords of mouse, used during placing
	private int mouseY;							//y coords of mouse, used during placing
	
	private Enemy target;
	private ArrayList<Projectile> projectiles;	//Tracks projectiles fired from this tower	
	
	//*****************Constants********************
	
	private final static int PROJECTILE = 0;
	private final static int NOVA = 1;
	
	//*****************Constructor*****************
	
	public Tower(int newType, int startX, int startY, int newTileSize, boolean thisStoreMode) {
		
		//Initialize variables
		x = startX;
		y = startY;
		tileSize = newTileSize;
		towerSize = (int) (tileSize * .8);
		placing = true;
		firing = false;
		storeMode = thisStoreMode;
		type = newType;
		level = 1;
		lastShot = System.nanoTime() / 1000000;
		
		//Types
		switch(type) {
			case 0:
				color1 = new Color(255, 0, 0);
				color2 = new Color(255, 0, 0, 180);
				range = (int)(2.5 * tileSize);
				dmg = 1;
				attackType = PROJECTILE;
				rateOfFire = 1000;
				cost = 100;
				break;
			case 1:
				color1 = new Color(0, 0, 255);
				color2 = new Color(0, 0, 255, 180);
				range = (int)(1.5 * tileSize);
				dmg = 2;
				attackType = NOVA;
				rateOfFire = 2000;
				cost = 150;
				break;
		}
		
		//If store mode, immediately place and end init
		if(storeMode) {
			this.place(x,  y);
			return;
		}
		
		//Initialize attack types
		if(attackType == PROJECTILE) {
			projectiles = new ArrayList<Projectile>();
		}
	}
	
	//********************Update and draw*********************
	
	public void update(int newX, int newY, ArrayList<Enemy> enemies) {
		
		//if tower in placing mode, move tower with mouse
		if(placing) {
			mouseX = newX;
			mouseY = newY;
			return;
		}
		
		/************************Projectile Logic*********************/
		if(attackType == PROJECTILE) {
			//if tower is not firing, check for enemies in range and 
			//begin firing if found
			if(!firing && enemies != null) {
				int dx;			//x distance from tower
				int dy;			//y distance from tower
				double d;		//distance from tower
				
				Enemy closest = null;		//current closest enemy
				double closestD = range;	//closest enemy's distance from tower
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
				}
			}	//if firing, check to see if enemy is dead or out of range
			else {
				//if target has died or gone out of range, stop firing
				if(!target.isAlive() || !isInRange(target)) {
					target = null;
					firing = false;
				}
				else {
					//Fire every two seconds
					if((System.nanoTime() / 1000000) - lastShot >= rateOfFire) {
						fire();
					}
				}
			}
			
			//move all existing projectiles
			if(projectiles.size() > 0) {								//Handles checking if this is a projectile tower
				for(int i = 0; i < projectiles.size(); i++) {
					Projectile thisProjectile = projectiles.get(i);		//if
					if(thisProjectile.getHit() ||						//Projectile has hit target,
							thisProjectile.getTarget() == null ||		//target no longer exists
							!thisProjectile.getTarget().isAlive()) {	//or target has died
						projectiles.remove(thisProjectile);				//then: remove projectile
					}
					else {
						thisProjectile.update();
					}
				}
			}
		}
		
		/******************************End projectile logic**************************/
		
		
		/*********************************Nova logic*********************************/
		
		else if(attackType == NOVA) {
			boolean fired = false;
			if((System.nanoTime() / 1000000) - lastShot >= rateOfFire) {
				for(Enemy enemy: enemies) {
					if(isInRange(enemy)) {
						enemy.hit(dmg);
						fired = true;
					}
				}
				if(fired) {
					lastShot = System.nanoTime() / 1000000;
				}
			}
		}
		
		/*******************************End nova logic******************************/
	}
	
	public void draw(Graphics g) {
		if(placing) {
			//convert current mouse coords to tower coords
			int newX = toTowerCoord(mouseX);
			int newY = toTowerCoord(mouseY);
			
			//draw tower
			g.setColor(color2);
			g.fillRect(newX,  newY,  towerSize, towerSize);
			
			//Draw range circle
			g.setColor(color1);
			g.drawOval((newX + (towerSize / 2)) - range, (newY + (towerSize / 2)) - range, range * 2, range * 2);
		}
		else {
			//draw tower
			g.setColor(color1);
			g.fillRect(x,  y,  towerSize, towerSize);
			
			//draw level
			g.setColor(Color.BLACK);
			g.drawString(Integer.toString(level), x + (towerSize / 2) - 3, y + (towerSize / 2) + 4);
			
			//Draw projectiles
			if(!storeMode && attackType == PROJECTILE) {
				//draw projectiles
				if(projectiles.size() > 0) {
					for(Projectile projectile: projectiles) {
						projectile.draw(g);
					}
				}
			}
			
			//Draw nova
			else if (!storeMode && attackType == NOVA) {
				if((System.nanoTime() / 1000000) - lastShot < 1000) {
					g.setColor(color1);
					g.drawOval((x + (towerSize / 2)) - range, (y + (towerSize / 2)) - range, range * 2, range * 2);
				}
			}
		}
	}
	
	//****************Getters*******************
	
	public int getCost() {
		return cost;
	}
	
	public int getType() {
		return type;
	}
	
	//***************Game methods*****************
	
	public void upgrade() {
		level++;
		dmg++;
		cost += 100;
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
	
	//Measure if enemy is within [range] pixels of this
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
	
	//Creates a new projectile and adds it to [projectiles]
	public void fire() {
		if(attackType == PROJECTILE) {
			projectiles.add(new Projectile(x + (towerSize / 2), y + (towerSize / 2), dmg, target));
			lastShot = System.nanoTime() / 1000000;
		}
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
		//Get tile #
		int temp = i / tileSize;
		
		//Return coordinates of tile + padding at edge of tile to center in tile
		return (temp * tileSize) + ((int)(tileSize * 0.1));
	}
}
