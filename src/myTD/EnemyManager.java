package myTD;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class EnemyManager {
	
	private boolean spawning;
	private boolean running;
	private int currentWave;
	private int totalWaves = 0;
	private int startTile;
	private int tileSize;
	private int timer;
	private ArrayList<Enemy[]> activeWaves;
	private ArrayList<Enemy> liveEnemies;
	private long lastSpawn;
	private int spawnCounter;
	
	private int rewardMoney;
	
	private int numLiveEnemies;
	private int runningWaves;
	private int livesLost;
	
	private int mapWidth;
	private int mapHeight;
	private int[] xCorners;
	private int[] yCorners;
	
	private Player player;
	private TDMap tileMap;
	
	public EnemyManager(TDMap newTileMap, GamePanel game, Player newPlayer) {
		startTile = newTileMap.getStart();
		tileSize = game.TILE_SIZE;
		
		player = newPlayer;
		tileMap = newTileMap;
		
		mapWidth = game.PIXEL_WIDTH;
		mapHeight = game.PIXEL_HEIGHT;
		
		livesLost = 0;
		
		spawning = false;
		
		timer = 0;
		activeWaves = new ArrayList<Enemy[]>();
		liveEnemies = new ArrayList<Enemy>();
		
		lastSpawn = System.nanoTime();
		spawnCounter = 0;
		
		rewardMoney = 0;
	}
	
	public void setCorners(int[] newXCorners, int[] newYCorners) {
		xCorners = newXCorners;
		yCorners = newYCorners;
	}
	
	public int getLivesLost() {
		return livesLost;
	}
	
	public void resetLivesLost() {
		livesLost = 0;
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
			
		Enemy[] wave = new Enemy[enemies];
		
		for(int i = 0; i < enemies; i++) {
			wave[i] = new Enemy(type, tileSize);
		}
		for(int i = 0; i < enemies; i++) {
			wave[i].setCorners(xCorners, yCorners);
		}

		activeWaves.add(wave);
		
		if(currentWave == 2) {
			currentWave = 0;
		}
		else {
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
		
		if(spawning) {
			Enemy[] wave = activeWaves.get(activeWaves.size() - 1);
			
			if((System.nanoTime() - lastSpawn) / 1000000 >= 2000) {
				System.out.println("Spawning enemy " + spawnCounter);
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
		
		if(!activeWaves.isEmpty()) {
			//Update all enemies currently being tracked
			//for(Enemy[] enemy: activeWaves) {
			for(int count = 0; count < activeWaves.size(); count++) {
				Enemy[] enemy = activeWaves.get(count);
				
				int waveEnemyCount = 0;
				
				for(int i = 0;i < enemy.length;i++) {
					if(enemy[i].getX() >= mapWidth && enemy[i].isAlive()) {
						enemy[i].die(false);
						liveEnemies.remove(enemy[i]);
						player.loseLives(1);
					}
					else if(!enemy[i].isAlive()) {
						if(enemy[i].justDied()) {
							liveEnemies.remove(enemy[i]);
							int reward = enemy[i].getReward();
							player.addMoney(reward);
							enemy[i].reset();
						}
					}
					
					if(enemy[i].isAlive()) {
						waveEnemyCount++;
						enemy[i].update();
					}
				}
				
				if(waveEnemyCount == 0) {
					activeWaves.remove(count);
				}
				
				enemyCount += waveEnemyCount;
			}
			numLiveEnemies = enemyCount;
		}
		
		if(activeWaves.size() == 0) {
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
		
		for(Enemy[] enemy: activeWaves) {
			for(int i = 0;i < enemy.length;i++) {
				if(enemy[i].isAlive() && enemy[i].isSpawned()) {
					enemy[i].draw(g);
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
