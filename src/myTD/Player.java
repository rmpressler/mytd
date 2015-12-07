package myTD;

import java.awt.Color;
import java.awt.Graphics;

public class Player {
	
	//**********************Fields************************
	
	//State variables
	private int money;
	private int lives;
	
	//**********************Constructor************************
	
	public Player(int newMoney, int newLives) {
		money = newMoney;
		lives = newLives;
	}
	
	//**********************Update and draw************************
	
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString("Lives: " + lives, 20,  20);
		g.drawString("Money: $" + money,  20,  35);
	}
	
	//**********************Getters************************
	
	public int getMoney() {
		return money;
	}
	
	public int getLives() {
		return lives;
	}
	
	//**********************Setters************************
	
	public void setLives(int newLives) {
		lives = newLives;
	}
	
	//**********************Game methods************************
	
	public void subtractMoney(int moneyLost) {
		money -= moneyLost;
	}
	
	public void addMoney(int moneyGained) {
		money += moneyGained;
	}
	
	public void loseLives(int lostLives) {
		lives -= lostLives;
	}
	
	public void gainLives(int gainedLives) {
		lives += gainedLives;
	}
}
