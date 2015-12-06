package myTD;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, 
												MouseListener, 
												MouseMotionListener{
	
	//Constant members
	public static final int WIDTH = 15;			//Width in tiles
	public static final int HEIGHT = 15;			//Height in tiles
	
	public static final int PIXEL_WIDTH = 600;		//Width in pixels
	public static final int PIXEL_HEIGHT = 600;	//Height in pixels
	
	public static final int TILE_SIZE = PIXEL_WIDTH / WIDTH;
	
	private static final int START_MONEY = 200;
	private static final int START_LIVES = 10;
	
	private static int FPS = 30;					//FPS cap
	
	//Instanced members
	private TDMap tileMap;		//Holds map data
	
	private Thread thread;		//Main game loop thread
	private boolean running;	//Flag tracking whether game running
	
	BufferedImage image;		//Back buffer
	Graphics2D graphicsBuffer;	//Graphics context of image
	
	double averageFPS;			//Actual FPS achieved
	
	EnemyManager eManager;
	TowerManager tManager;
	TowerStore tStore;
	Player player;
	
	private int mouseX;
	private int mouseY;
	
	public GamePanel() {
		
		setPreferredSize(new Dimension(PIXEL_WIDTH, PIXEL_HEIGHT));
		
	}
	
	@Override
	public void addNotify() {
		
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		
	}
	
	public void update() {
		eManager.update();
		player.update(eManager.getLivesLost());
		tManager.update(mouseX, mouseY, eManager.getLiveEnemies());
	}
	
	public void render() {
		tileMap.draw(graphicsBuffer);
		eManager.draw(graphicsBuffer);
		tManager.draw(graphicsBuffer);
		player.draw(graphicsBuffer);
		graphicsBuffer.setColor(Color.BLACK);
		//graphicsBuffer.drawString("FPS: " + averageFPS, 20, 30);
	}
	
	//Draws ImageBuffer to screen
	public void draw() {
		Graphics g = this.getGraphics();
		g.drawImage(image,  0,  0, null);
		g.dispose();
	}

	@Override
	public void run() {
		
		tileMap = new TDMap(PIXEL_WIDTH, PIXEL_HEIGHT, TILE_SIZE, false);
		tileMap.loadMap("C:\\Users\\r.pressler\\Java\\Games Workspace\\myTD\\largemap.tdm");
		int[] xCorners = tileMap.getXCorners();
		int[] yCorners = tileMap.getYCorners();
		
		image = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_RGB);
		graphicsBuffer = (Graphics2D) image.getGraphics();
		
		player = new Player(START_MONEY, START_LIVES);
		
		eManager = new EnemyManager(tileMap, this, player);
		eManager.start();
		eManager.setCorners(xCorners, yCorners);
		
		tManager = new TowerManager(PIXEL_WIDTH, PIXEL_HEIGHT, TILE_SIZE, player);
		
		running = true;
		
		addMouseListener(this);
		addMouseListener(tManager);
		addMouseMotionListener(this);
		
		averageFPS = 0;
		
		int frameCount = 1;
		long startTime;
		long elapsed;
		long frameTime = 1000 / FPS;
		
		long totalTime = 0;
		
		while(running) {
			
			startTime = System.nanoTime();
			
			update();
			render();
			draw();
			
			elapsed = (System.nanoTime() - startTime) / 1000000;
			
			//FPS throttling
			if(elapsed < frameTime) {
				try {
					Thread.sleep(frameTime - elapsed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
			totalTime += System.nanoTime() - startTime;
			
			if(frameCount == 30) {
				averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
				totalTime = 0;
				frameCount = 1;
			}
			else {
				frameCount++;
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(tManager.placing()) {
			player.subtractMoney(tManager.getPlacingTowerCost());
			int xTile = e.getX() / TILE_SIZE;
			int yTile = e.getY() / TILE_SIZE;
			if(!tileMap.isPath(xTile, yTile) &&
					!tManager.isInStore(e.getX(), e.getY())) {
				tManager.placeTower();
			}
		}
//		else {
//			tManager.createTower(0, e.getX(), e.getY());
//		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

}
