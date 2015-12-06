package myTD;

import java.awt.Color;
import java.awt.Graphics;

public class Player {
	private int money;
	private int lives;
	private int startLives;
	
	
	public Player(int newMoney, int newLives) {
		money = newMoney;
		startLives = newLives;
		lives = newLives;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void subtractMoney(int moneyLost) {
		money -= moneyLost;
	}
	
	public void addMoney(int moneyGained) {
		money += moneyGained;
	}
	
	public int getLives() {
		return lives;
	}
	
	public void setLives(int newLives) {
		lives = newLives;
	}
	
	public void loseLives(int lostLives) {
		lives -= lostLives;
	}
	
	public void gainLives(int gainedLives) {
		lives += gainedLives;
	}
	
	public void update(int livesLost) {
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString("Lives: " + lives, 20,  20);
		g.drawString("Money: $" + money,  20,  35);
	}
}
