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
	private int startLife;				//Starting life; Used to track % life.
	private int reward;					//Money rewarded for killing
	private Color color;				//Color of unit
	private boolean isAlive;			//False if dead
	private boolean spawned;			//Set to true once spawn() is called and false once dead
	private boolean justDied;			//Set to true in the frame that it died, then false after.
	private boolean hit;				//True if hit this frame.
	
	private TDMap tileMap;				//Hold reference to tileMap being used in game.
	
	private int tileSize;				//Tile size in px for calculating corner destinations
	private int xCorners[];				//Holds the x coordinate of each target corner in TILES
	private int yCorners[];				//Holds the y coordinate of each target corner in TILES
	
	private String direction;			//Only valid values are "left", "up", "down", and "right"
	
	private int currentCorner;			//State variable for remembering which corner to pursue
	
	//**************Constructor**************
	
	public Enemy(int rank, TDMap tMap) {		
		switch(rank) {
			case 0:
				color = Color.GREEN.darker();
				speed = 1;
				life = 20;
				reward = 50;
				break;
			case 1:
				color = Color.BLUE;
				speed = 2;
				life = 30;
				reward = 60;
				break;
			case 2:
				color = Color.RED;
				speed = 2;
				life = 40;
				reward = 70;
				break;
			case 3:
				color = Color.YELLOW;
				speed = 2;
				life = 50;
				reward = 80;
				break;
			case 4:
				color = Color.WHITE;
				speed = 3;
				life = 60;
				reward = 90;
				break;
			case 5:
				color = Color.GRAY;
				speed = 3;
				life = 70;
				reward = 100;
				break;
		}
		
		startLife = life;
		
		tileMap = tMap;
		
		xCorners = tileMap.getXCorners();
		yCorners = tileMap.getYCorners();
		
		tileSize = tileMap.getTileSize();
		r = tileSize / 3;
		
		currentCorner = 0;
		
		justDied = false;
		spawned = false;
		hit = false;
		isAlive = true;
	}
	
	//********************Update and draw*********************
	
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
		
		//Draw enemy
		if(hit) {
			g.setColor(Color.WHITE);
			hit = false;
		}
		else {
			g.setColor(color);
		}
		g.fillOval(x,  y, r * 2, r * 2);
		
		g.setColor(color.darker());
		g.drawOval(x,  y, r * 2, r * 2);
		
		//Life bar
		//Draw remaining life
		double lifePct = (double)life/startLife;
		int barLeftWidth = (int)((r * 2) * lifePct);
		g.setColor(Color.GREEN);
		g.fillRect(x, y - 10, barLeftWidth, 5);
		
		//Draw lost life
		double lostLifePct = 1 - lifePct;
		int barRightWidth = (int)((r * 2) * lostLifePct);
		g.setColor(Color.RED);
		g.fillRect(x + barLeftWidth, y - 10, barRightWidth, 5);
		
		//Border
		g.setColor(Color.BLACK);
		g.drawRect(x,  y - 10, r * 2, 5);
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
	
	public void spawn() {
		//Get the spawning coordinates of spawn tile
		int newX = (int)(Math.random() * (tileSize - getSize()));
		int newY = (int)((Math.random() * (tileSize - getSize())) + (tileSize * tileMap.getStart()));
		
		//Assign pixel coordinates
		x = newX;
		y = newY;
		
		//Assign pixel coordinates WITHIN tile
		spawnX = x % tileSize;
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
		hit = true;
		return false;
	}
	
}
