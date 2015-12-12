package myTD;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class EnemyManager {
	
	//**************Fields****************
	
	//Enemy-related variables
	private Enemy[] wave;					//Holds the wave's Enemy s
	private ArrayList<Enemy> liveEnemies;	// TODO Find a better way to do this
	
	//State variables
	private boolean running;				//True when start() is called
	private int currentWave;				//Current wave #
	private int totalWaves;					//Total waves to spawn
	private int timer;						//Times spawning of next wave
	private int spawnDelay;					//Time between enemy spawns during wave (ms)
	
	//Spawn-related variables
	private boolean spawning;				//True while enemies are being sent
	private long lastSpawn;					//nanoTime() of last spawn; used by spawn timer
	private int spawnCounter;				//keeps track of which enemy in wave[] is being spawned
	
	//Spawn button properties
	private int buttonX;					//x coord of spawn button
	private int buttonY;					//y coord of spawn button
	private int buttonWidth;				//Width of spawn button
	private int buttonHeight;				//Height of spawn button
	
	private Player player;
	private TDMap tileMap;
	
	//*****************Constructor*****************
	
	public EnemyManager(TDMap newTileMap, Player newPlayer) {
		player = newPlayer;
		tileMap = newTileMap;
		
		totalWaves = 10;
		
		wave = null;
		
		spawning = false;
		
		timer = 0;
		liveEnemies = new ArrayList<Enemy>();
		
		lastSpawn = System.nanoTime();
		spawnCounter = 0;
		
		//Set button
		buttonWidth = (int)(GamePanel.PIXEL_WIDTH * 0.1);
		buttonHeight = (int)(GamePanel.PIXEL_HEIGHT * 0.05);
		buttonX = GamePanel.PIXEL_WIDTH - (buttonWidth + 10);
		buttonY = 10;
	}
	
	//********************Update and draw*********************
	
	public void update() {
		
		//Do nothing if GameManager is not running
		if(!running) {
			return;
		}
		
		//300 frames = 10 seconds
		//create and send next wave
		if(timer == 300) {
			timer = 0;
			sendWave();
		}
		
		//Spawning logic
		if(spawning) {
			spawnDelay = (int)(Math.random() * 1500);
			if((System.nanoTime() - lastSpawn) / 1000000 >= spawnDelay) {
				wave[spawnCounter].spawn();
				liveEnemies.add(wave[spawnCounter]);
				spawnCounter++;
				lastSpawn = System.nanoTime();
				
				if(spawnCounter == wave.length) {
					spawnCounter = 0;
					spawning = false;
				}
				
				spawnDelay = (int)(Math.random() * 1500);
			}
		}
		
		//Update all enemies currently being tracked
		int waveEnemyCount = 0;
		if(wave != null) {
			for(int i = 0;i < wave.length;i++) {
				//If off-screen and alive, kill and lose life
				if(wave[i].getX() >= GamePanel.PIXEL_WIDTH && wave[i].isAlive()) {
					wave[i].die(false);
					liveEnemies.remove(wave[i]);
					player.loseLives(1);
				}
				//If just died, remove enemy and credit reward to player
				else if(wave[i].justDied()) {
					liveEnemies.remove(wave[i]);
					player.addMoney(wave[i].getReward());
					wave[i].reset();
				}
				
				if(wave[i].isAlive()) {
					waveEnemyCount++;
					wave[i].update();
				}
			}
			
			if(waveEnemyCount == 0) {
				wave = null;
			}
		}
		else {
			timer++;
		}
	}

	public void draw(Graphics g) {
		//If enemy manager hasn't been started, do nothing.
		if(!running) {
			return;
		}
		
		//If there's a wave alive, draw each enemy
		if(wave != null) {
			for(int i = 0;i < wave.length;i++) {
				if(wave[i].isAlive() && wave[i].isSpawned()) {
					wave[i].draw(g);
				}
			}
		}
		else {
			g.setColor(Color.BLUE);
			g.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
			g.setColor(Color.WHITE);
			Font oldFont = g.getFont();
			g.setFont(new Font("Verdana", Font.BOLD, 12));
			int textWidth = g.getFontMetrics().stringWidth("SPAWN");
			int stringX = (buttonWidth - textWidth) / 2;
			int stringY = buttonHeight / 2;
			g.drawString("SPAWN", buttonX + stringX, buttonY + stringY + 5);
			g.setFont(oldFont);
		}
	}
	
	//**************Getters*****************
	
	public ArrayList<Enemy> getLiveEnemies() {
		return liveEnemies;
	}
	
	//***************Game methods*****************
	
	public void start() {
		running = true;
	}
	
	public void stop() {
		running = false;
	}
	
	private void sendWave() {
		spawning = true;
		
		int enemies = 0;
		int type = 0;
		
		switch(currentWave) {
		case 0:
			enemies = 11;
			type = 0;
			break;
		case 1:
			enemies = 6;
			type = 1;
			break;
		case 2:
			enemies = 7;
			type = 2;
			break;
		case 3:
			enemies = 8;
			type = 3;
			break;
		case 4:
			enemies = 9;
			type = 3;
			break;
		case 5:
			enemies = 9;
			type = 3;
			break;
		case 6:
			enemies = 9;
			type = 3;
			break;
		case 7:
			enemies = 9;
			type = 3;
			break;
		case 8:
			enemies = 9;
			type = 3;
			break;
		case 9:
			enemies = 9;
			type = 3;
			break;
		case 10:
			enemies = 9;
			type = 3;
			break;
		}
		
		//Create new Enemy[] in wave and initialize
		wave = new Enemy[enemies];
		for(int i = 0; i < enemies; i++) {
			wave[i] = new Enemy(type, tileMap);
		}
		
		if(currentWave < totalWaves) {
			currentWave++;
		}
	}
	
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if(x >= buttonX &&
				x <= buttonX + buttonWidth &&
				y >= buttonY &&
				y <= buttonY + buttonHeight) {
			timer = 0;
			sendWave();
		}
	}
}
