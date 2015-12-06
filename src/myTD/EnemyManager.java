package myTD;

import java.awt.Graphics;
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
	
	//Spawn-related variables
	private boolean spawning;				//True while enemies are being sent
	private long lastSpawn;					//nanoTime() of last spawn; used by spawn timer
	private int spawnCounter;				//keeps track of which enemy in wave[] is being spawned
	
	private Player player;
	private GamePanel game;
	private TDMap tileMap;
	
	//*****************Constructor*****************
	
	public EnemyManager(TDMap newTileMap, GamePanel newGame, Player newPlayer) {
		game = newGame;
		player = newPlayer;
		tileMap = newTileMap;
		
		totalWaves = 10;
		
		wave = null;
		
		spawning = false;
		
		timer = 0;
		liveEnemies = new ArrayList<Enemy>();
		
		lastSpawn = System.nanoTime();
		spawnCounter = 0;
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
			//Wait two seconds between each unit
			if((System.nanoTime() - lastSpawn) / 1000000 >= 2000) {
				wave[spawnCounter].spawn();
				liveEnemies.add(wave[spawnCounter]);
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
				//If off-screen and alive, kill and lose life
				if(wave[i].getX() >= game.PIXEL_WIDTH && wave[i].isAlive()) {
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
		else if(currentWave == 3) {
			enemies = 4;
			type = 3;
		}
			
		wave = new Enemy[enemies];
		
		for(int i = 0; i < enemies; i++) {
			wave[i] = new Enemy(type, game.TILE_SIZE, tileMap);
		}
		
		if(currentWave < totalWaves) {
			currentWave++;
		}
	}
}
