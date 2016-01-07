package myTD;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class EditState extends GameState {
	
	private boolean dragging;
	private boolean dragged;
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	
	private int selection;
	
	TDMap tileMap;
	
	public EditState(GameStateManager gsm) {
		this.gsm = gsm;
		
		init();
	}

	@Override
	public void init() {
		tileMap = new TDMap(GamePanel.PIXEL_WIDTH, GamePanel.PIXEL_HEIGHT, GamePanel.TILE_SIZE, true);
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void setActive() {
		super.setActive();
		
		JFrame window = new JFrame("Pallette");
		window.setContentPane(new ButtonPanel());
		window.pack();
		window.setVisible(true);
	}

	@Override
	public void draw(Graphics2D g) {
		tileMap.draw(g);
		
		g.setColor(Color.BLACK);
		g.drawRect(0,  0, GamePanel.PIXEL_WIDTH - 1, GamePanel.PIXEL_HEIGHT - 1);
		
		for(int i = 0; i < GamePanel.WIDTH; i++) {
			g.drawLine(i * GamePanel.TILE_SIZE, 0, i * GamePanel.TILE_SIZE, GamePanel.PIXEL_HEIGHT);
		}
		for(int i = 0; i < GamePanel.HEIGHT; i++) {
			g.drawLine(0, i * GamePanel.TILE_SIZE, GamePanel.PIXEL_WIDTH, i * GamePanel.TILE_SIZE);
		}
		
		if(dragging) {
			g.drawRect(startX, 
					startY, 
					endX - startX, 
					endY - startY);
		}
	}
	
	private void fillTiles() {
		/* 1               2
		 *  |-------------|
		 *  |             |
		 *  |             |
		 *  |-------------|
		 * 4               3
		 */
		
		//Fill top row
		tileMap.setTile(
				startX / GamePanel.TILE_SIZE, 
				startY / GamePanel.TILE_SIZE, 
				selection
				);
		int horizTiles = ((endX - startX) / GamePanel.TILE_SIZE) + 1;
		int vertTiles = ((endY - startY) / GamePanel.TILE_SIZE) + 1;
		int topLeftX = startX / GamePanel.TILE_SIZE;
		int topLeftY = startY / GamePanel.TILE_SIZE;
		
		for(int i = topLeftX; i < topLeftX + horizTiles; i++) {
			for(int j = topLeftY; j < topLeftY + vertTiles; j++) {
				tileMap.setTile(
						i, 
						j, 
						selection
						);
			}
		}
		
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		int x = evt.getX() / GamePanel.TILE_SIZE;
		int y = evt.getY() / GamePanel.TILE_SIZE;
		
		dragging = true;
		
		startX = evt.getX();
		startY = evt.getY();
		
		tileMap.setTile(x,  y,  selection);
	}
	
	@Override
	public void mouseReleased(MouseEvent evt) {
		if(dragged) {
			fillTiles();
			dragged = false;
		}
		endX = 0;
		endY = 0;
		dragging = false;
	}
	
	@Override
	public void mouseDragged(MouseEvent evt) {
		if(dragging) {
			endX = evt.getX();
			endY = evt.getY();
			dragged = true;
		}
	}
	
	class ButtonPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		public void setSelection(int selection) {
			EditState.this.selection = selection;
		}
		
		public ButtonPanel() {
			this.setLayout(new GridLayout(4, 1));
			
			JButton button0 = new JButton("Grass0");
			button0.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setSelection(0);
				}
			});
			this.add(button0);
			
			JButton button1 = new JButton("Grass1");
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setSelection(1);
				}
			});
			this.add(button1);
			
			JButton button2 = new JButton("Grass2");
			button2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setSelection(2);
				}
			});
			this.add(button2);
			
			JButton button3 = new JButton("Grass3");
			button3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setSelection(3);
				}
			});
			this.add(button3);
			
			JButton button4 = new JButton("Grass4");
			button4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setSelection(4);
				}
			});
			this.add(button4);
			
			JButton button5 = new JButton("Grass5");
			button5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setSelection(5);
				}
			});
			this.add(button5);
			
			JButton button6 = new JButton("Grass6");
			button6.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setSelection(6);
				}
			});
			this.add(button6);
			
			JButton button7 = new JButton("Grass7");
			button7.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setSelection(7);
				}
			});
			this.add(button7);
			
			JButton button8 = new JButton("Grass8");
			button8.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setSelection(8);
				}
			});
			this.add(button8);
			
			JButton button9 = new JButton("Path");
			button9.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setSelection(9);
				}
			});
			this.add(button9);
			
			JButton button10 = new JButton("Mountain");
			button10.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setSelection(10);
				}
			});
			this.add(button10);
			
			JButton button11 = new JButton("Forest");
			button11.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setSelection(11);
				}
			});
			this.add(button11);
			
			JButton saveButton = new JButton("Save Map");
			saveButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					EditState.this.tileMap.saveMap();
				}
			});
			this.add(saveButton);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {}
	@Override
	public void keyPressed(int e) {}
	@Override
	public HashMap<String, String> getStateData() {
		return null;
	}
}
