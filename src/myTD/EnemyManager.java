package myTD;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class EnemyManager {
	
	//**************Fields****************
	
	//Enemy-related variables
	private Enemy[] wave;					//Holds the wave's Enemy s
	private ArrayList<Enemy> liveEnemies;	//Tracks current live enemies
	
	//State variables
	private boolean running;
	private int currentWave;
	private int totalWaves;
	private int timer;
	
	private int numLiveEnemies;
	
	//Spawn-related variables
	private boolean spawning;
	private long lastSpawn;
	private int spawnCounter;
	
	//Map details
	private int startTile;
	private int tileSize;
	private int mapWidth;
	private int[] xCorners;
	private int[] yCorners;
	
	private Player player;
	
	public EnemyManager(TDMap newTileMap, GamePanel game, Player newPlayer) {
		startTile = newTileMap.getStart();
		tileSize = game.TILE_SIZE;
		
		totalWaves = 10;
		
		player = newPlayer;
		
		mapWidth = game.PIXEL_WIDTH;
		
		wave = null;
		
		spawning = false;
		
		timer = 0;
		liveEnemies = new ArrayList<Enemy>();
		
		lastSpawn = System.nanoTime();
		spawnCounter = 0;
	}
	
	public void setCorners(int[] newXCorners, int[] newYCorners) {
		xCorners = newXCorners;
		yCorners = newYCorners;
	}
	
	public void setWaves(int i) {
		if(!running) {
			totalWaves = i;
		}
		else {
			System.out.println("Cannot set waves while EnemyManager is running.");
		}
	}
	
	public int getnumLiveEnemies() {
		return numLiveEnemies;
	}
	
	public void start() {
		running = true;
	}
	
	public void stop() {
		running = false;
	}
	
	//Add new wave to queue [activeWaves]
	private void sendWave() {
		spawning = true;
		
		int enemies = 0;
		int type = 0;
		
		if(currentWave == 0) {
			enemies = 3;
			type = 0;
		}
		else if(currentWave == 1) {
			enemies = 3;
			type = 1;
		}
		else if(currentWave == 2) {
			enemies = 3;
			type = 2;
		}
			
		wave = new Enemy[enemies];
		
		for(int i = 0; i < enemies; i++) {
			wave[i] = new Enemy(type, tileSize);
		}
		for(int i = 0; i < enemies; i++) {
			wave[i].setCorners(xCorners, yCorners);
		}
		
		if(currentWave < totalWaves) {
			currentWave++;
		}
	}
	
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
		
		int enemyCount = 0;
		
		//Spawning logic
		if(spawning) {
			if((System.nanoTime() - lastSpawn) / 1000000 >= 2000) {
				wave[spawnCounter].spawn(0, startTile);
				liveEnemies.add(wave[spawnCounter]);
				numLiveEnemies++;
				spawnCounter++;
				lastSpawn = System.nanoTime();
				
				if(spawnCounter == wave.length) {
					spawnCounter = 0;
					spawning = false;
				}
			}
		}
		
		//Update all enemies currently being tracked
		int waveEnemyCount = 0;
		if(wave != null) {
			for(int i = 0;i < wave.length;i++) {
				if(wave[i].getX() >= mapWidth && wave[i].isAlive()) {
					wave[i].die(false);
					liveEnemies.remove(wave[i]);
					player.loseLives(1);
				}
				else if(!wave[i].isAlive()) {
					if(wave[i].justDied()) {
						liveEnemies.remove(wave[i]);
						int reward = wave[i].getReward();
						player.addMoney(reward);
						wave[i].reset();
					}
				}
				
				if(wave[i].isAlive()) {
					waveEnemyCount++;
					wave[i].update();
				}
			}
			
			if(waveEnemyCount == 0) {
				wave = null;
			}
			
			enemyCount += waveEnemyCount;
			numLiveEnemies = enemyCount;
		}
		else {
			timer++;
		}
	}
	
	public ArrayList<Enemy> getLiveEnemies() {
		return liveEnemies;
	}
	
	public void draw(Graphics g) {
		
		if(!running) {
			return;
		}
		
		if(wave != null) {
			for(int i = 0;i < wave.length;i++) {
				if(wave[i].isAlive() && wave[i].isSpawned()) {
					wave[i].draw(g);
				}
			}
		}
		
		g.setColor(Color.BLACK);
//		g.drawString("Timer: " + timer / 30, 20,  40);
//		g.drawString("Enemies: " + numLiveEnemies, 20, 50);
//		g.drawString("Waves alive: " + activeWaves.size(), 20, 60);
//		g.drawString("liveEnemies: " + liveEnemies.size(), 20, 80);
		
	}
	
}
