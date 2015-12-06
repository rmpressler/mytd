package myTD;

import java.awt.Color;
import java.awt.Graphics;

public class Projectile {
	
	//location fields
	private int x;
	private int y;
	private int r;
	private int speed;
	private int dmg;
	
	private boolean hit;
	private boolean killed;
	
	Enemy target;
	
	public Projectile(int newX, int newY, int newDmg, Enemy newTarget) {
		x = newX;
		y = newY;
		dmg = newDmg;
		target = newTarget;
		speed = 5;
		r = 5;
		hit = false;
	}
	
	public Enemy getTarget() {
		return target;
	}
	
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
	
	public boolean getHit() {
		return hit;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(x,  y,  r * 2,  r * 2);
		g.setColor(Color.yellow.darker());
		g.drawOval(x,  y, r * 2, r * 2);
	}
}
