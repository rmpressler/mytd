package myTD;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy {
	
	//**************Fields****************
	
	//Location and size in PIXELS
	private int x;
	private int y;
	private int r;
	
	//start location in pixels WITHIN starting tile
	private int spawnX;
	private int spawnY;
	
	//Rank and color are correlated
	private int speed;					//Speed at a rate of pixels/frame
	private int life;					//Units of HP
	private int reward;					//Money rewarded for killing
	private Color color;				//Color of unit
	private boolean isAlive;			//False if dead
	private boolean spawned;			//Set to true once spawn() is called and false once dead
	private boolean justDied;			//Set to true in the frame that it died, then false after.
	
	private int tileSize;				//Tile size in px for calculating corner destinations
	private int xCorners[];				//Holds the x coordinate of each target corner in TILES
	private int yCorners[];				//Holds the y coordinate of each target corner in TILES
	
	private String direction;			//Only valid values are "left", "up", "down", and "right"
	
	private int currentCorner;			//State variable for remembering which corner to pursue
	
	//**************Constructor**************
	
	public Enemy(int rank, int newTileSize) {
		switch(rank) {
			case 0:
				color = Color.GREEN.darker();
				speed = 1;
				life = 2;
				reward = 50;
				break;
			case 1:
				color = Color.BLUE;
				speed = 2;
				life = 3;
				reward = 60;
				break;
			case 2:
				color = Color.RED;
				speed = 2;
				life = 4;
				reward = 70;
				break;
			case 3:
				color = Color.YELLOW;
				speed = 2;
				life = 5;
				reward = 80;
				break;
			case 4:
				color = Color.WHITE;
				speed = 2;
				life = 6;
				reward = 90;
				break;
		}
		
		tileSize = newTileSize;
		r = tileSize / 3;
		
		currentCorner = 0;
		
		justDied = false;
		spawned = false;
		isAlive = true;
	}
	
	public void update() {
		//If dead, don't bother updating
		if(!isAlive) {
			return;
		}
		
		//If at less than 0 hp, die
		if(life <= 0) {
			die(true);
		}
		
		//On final corner, just proceed to edge of map
		if(currentCorner == xCorners.length) {
			x += speed;
			return;
		}
		
		//Get pixel coordinates that we are travelling to
		int targetX = (xCorners[currentCorner] * tileSize) + spawnX;
		int targetY = (yCorners[currentCorner] * tileSize) + spawnY;
		
		//Corner detection
		if(direction == "right" && x >= targetX ||
				direction == "left" && x <= targetX ||
				direction == "up" && y <= targetY ||
				direction == "down" && y >= targetY) {
			currentCorner++;
			setDirection();
			return;
		}
		
		//Move unit based on current direction of travel
		if(direction == "right") {
			x += speed;
		}
		else if (direction == "left") {
			x -= speed;
		}
		else if(direction == "down") {
			y += speed;
		}
		else {
			y -= speed;
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(x,  y, r * 2, r * 2);
		
		g.setColor(color.darker());
		g.drawOval(x,  y, r * 2, r * 2);
	}
	
	//**************Getters*****************
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getLife() {
		return life;
	}
	
	public int getR() {
		return r;
	}
	
	public int getSize() {
		return r * 2;
	}
	
	public int getReward() {
		return reward;
	}
	
	//***************Setters*******************
	
	public void setCorners(int[] newXCorners, int[] newYCorners) {
		xCorners = newXCorners;
		yCorners = newYCorners;
	}
	
	public void setDirection() {
		if(currentCorner == xCorners.length) {
			direction = "right";
			return;
		}
		
		int targetX = (xCorners[currentCorner] * tileSize) + spawnX;
		int targetY = (yCorners[currentCorner] * tileSize) + spawnY;
		
		if(Math.abs(x - targetX) > Math.abs(y - targetY)) {
			//Moving on x-axis
			if(x < targetX) {
				direction = "right";
			}
			else if (x > targetX) {
				direction = "left";
			}
		}
		else {
			//Moving on y-axis
			if(y < targetY) {
				direction = "down";
			}
			else if(y > targetY) {
				direction = "up";
			}
		}
	}
	
	//***************Game methods*****************
	
	public boolean justDied() {
		return justDied;
	}
	
	public void reset() {
		justDied = false;
	}
	
	public void die(boolean rewarded) {
		if(!rewarded) {
			reward = 0;
		}
		justDied = true;
		isAlive = false;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public void spawn(int newXTile, int newYTile) {
		int newX = (int)(Math.random() * (tileSize - getSize()));
		int newY = (int)((Math.random() * (tileSize - getSize())) + (tileSize * newYTile));
		
		x = newX;
		spawnX = x % tileSize;
		y = newY;
		spawnY = y % tileSize;
		
		setDirection();
		
		spawned = true;
	}
	
	public boolean isSpawned() {
		return spawned;
	}
	
	public boolean hit(int dmg) {
		life -= dmg;
		if(life <= 0) {
			life = 0;
			return true;
		}
		return false;
	}
	
}
