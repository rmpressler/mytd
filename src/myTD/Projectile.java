package myTD;

import java.awt.Color;
import java.awt.Graphics;

public class Projectile {
	
	//**********************Fields************************
	
	//Properties
	private int x;				//x coordinate in pixels
	private int y;				//y coordinate in pixels
	private int r;				//radius in pixels
	private int speed;			//speed in pixels per frame
	private int dmg;			//damage done per hit
	
	//State variables
	private boolean hit;		//true when projectile hits [target]
	private boolean killed;		//true when [target] is killed
	
	//Other references
	Enemy target;				//Reference to enemy being targeted
	
	//**********************Constructor************************
	
	public Projectile(int newX, int newY, int newR, int newDmg, Enemy newTarget) {
		
		//init
		x = newX;
		y = newY;
		r = newR;
		speed = 5;
		dmg = newDmg;
		
		hit = false;
		killed = false;
		
		target = newTarget;
	}
	
	//**********************Update and draw************************
	
	public void update() {
		if(x < target.getX()) {
			x += speed;
		}
		else if(x > target.getX()) {
			x -= speed;
		}
		
		if(y < target.getY()) {
			y += speed;
		}
		else if(y > target.getY()) {
			y -= speed;
		}
		
		if(checkCollision()) {
			killed = target.hit(dmg);
			hit = true;
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(x,  y,  r * 2,  r * 2);
		g.setColor(Color.yellow.darker());
		g.drawOval(x,  y, r * 2, r * 2);
	}
	
	//**********************Getters************************
	
	public boolean getHit() {
		return hit;
	}
	
	public Enemy getTarget() {
		return target;
	}
	
	//**********************Game methods************************
	
	public boolean killed() {
		return killed;
	}
	
	public boolean checkCollision() {
		if(x >= target.getX() &&
				x <= target.getX() + target.getSize() &&
				y >= target.getY() &&
				y <= target.getY() + target.getSize()) {
			return true;
		}
		else {return false;}
	}
}
