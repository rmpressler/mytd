package myTD;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class PlayState extends GameState {
	//State data
	private boolean paused;
	
	//Game component objects
	private TDMap tileMap;
	private EnemyManager eManager;
	private TowerManager tManager;
	private Player player;
	
	//Game settings
	private final int START_MONEY = 500;
	private final int START_LIVES = 10;
	
	public PlayState(GameStateManager gsm) {
		this.gsm = gsm;
		
		init();
	}
	
	public void init() {
		//Create map object and load map file
		tileMap = new TDMap(GamePanel.PIXEL_WIDTH, GamePanel.PIXEL_HEIGHT, GamePanel.TILE_SIZE, false);
		tileMap.loadMap("new2.tdm");
		
		player = new Player(START_MONEY, START_LIVES);
		
		eManager = new EnemyManager(tileMap, player);
		eManager.start();
		
		tManager = new TowerManager(tileMap, player);
		
		paused = false;
	}

	@Override
	public void update() {
		if(paused) {
			return;
		}
		eManager.update();
		tManager.update(eManager.getLiveEnemies());
		
		if(player.getLives() <= 0) {
			gsm.setState(GameStateManager.GAMEOVERSTATE);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		tileMap.draw(g);
		eManager.draw(g);
		tManager.draw(g);
		player.draw(g);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		tManager.mousePressed(e);
		eManager.mousePressed(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		tManager.mouseMoved(e);
	}

	@Override
	public void keyPressed(int e) {
		if(e == KeyEvent.VK_ESCAPE) {
			gsm.setState(GameStateManager.PAUSESTATE);
		}
	}

	@Override
	public HashMap<String, String> getStateData() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("wave", Integer.toString(eManager.getWave()));
		return data;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
