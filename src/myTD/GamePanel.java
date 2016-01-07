package myTD;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, MouseListener, MouseMotionListener, KeyListener {
	
	//**********************Fields************************

	private static final long serialVersionUID = 1L;
	
	//Constants
	public static final int WIDTH = 12;
	public static final int HEIGHT = 12;
	
	public static final int PIXEL_WIDTH = 576;
	public static final int PIXEL_HEIGHT = 576;
	
	public static final int TILE_SIZE = PIXEL_WIDTH / WIDTH;
	
	private static int FPS = 30;				//FPS cap
	
	//State data
	private GameStateManager gsm;
	
	//Thread related
	private Thread thread;						//Main game loop thread
	volatile private boolean running;					//Flag tracking whether game running
	
	//Graphics related
	BufferedImage image;						//Back buffer - all writing is done to this
	Graphics2D graphicsBuffer;					//Graphics context of screen (front buffer)
	
	double averageFPS;							//Actual FPS achieved
	
	//**********************Constructor************************
	
	public GamePanel() {
		setPreferredSize(new Dimension(PIXEL_WIDTH, PIXEL_HEIGHT));
	}
	
	//**********************Update and draw************************
	
	public void update() {
		gsm.update();
	}
	
	public void render() {
		gsm.draw(graphicsBuffer);
	}
	
	//Draws ImageBuffer to screen
	public void draw() {
		Graphics g = this.getGraphics();
		g.drawImage(image,  0,  0, null);
		g.dispose();
	}
	
	//**********************Game methods************************
	
	//Triggered when this is added to a component; starts game loop
	@Override
	public void addNotify() {
		
		super.addNotify();
		
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		
		this.requestFocusInWindow();
	}
	
	//Called when thread starts, 
	@Override
	public void run() {
		//Create graphics buffer (back buffer)
		image = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_RGB);
		graphicsBuffer = (Graphics2D) image.getGraphics();
		
		gsm = new GameStateManager();
		
		running = true;

		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
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
	
	//**********************Event Handlers************************
	
	@Override
	public void mousePressed(MouseEvent e) {
		gsm.mousePressed(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		gsm.mouseMoved(e);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		gsm.keyPressed(e.getKeyCode());
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		gsm.mouseReleased(e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		gsm.mouseDragged(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
}
