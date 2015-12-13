package myTD;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class TDMap {
	
	//**********************Fields************************
	
	//Properties
	private int width;				//Width in tiles
	private int height;				//Height in tiles
	private int pixelWidth;			//Width in pixels
	private int pixelHeight;		//Height in pixels
	private int tileSize;			//Tile size in pixels
	private int start;				//Starting tile y coordinate (x is always 0)
	private int tiles[][];			//Stores int values representing the tile types
	
	private BufferedImage grassImg;	
	private BufferedImage treeImg;
	private BufferedImage mountainImg;
	
	ArrayList<Integer> xCorners;	//x coordinates of each corner in tiles
	ArrayList<Integer> yCorners;	//y coordinates of each corner in tiles
	
	//State variables
	private boolean editorMode;		//true if map is loaded into Editor object
	
	//**********************Constructor************************
	
	public TDMap(int newWidth, int newHeight, int newTileSize, boolean isEditorMode) {
		
		//Set fields
		this.width = newWidth / newTileSize;
		this.height = newHeight / newTileSize;
		this.pixelWidth = newWidth;
		this.pixelHeight = newHeight;
		this.tileSize = newTileSize;
		this.editorMode = isEditorMode;
		
		try {
			grassImg = ImageIO.read(new File("grass_40_40.png"));
			treeImg = ImageIO.read(new File("tree_40_40.png"));
			mountainImg = ImageIO.read(new File("mountain_40_40.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Initialize all tiles to -1 (whitespace)
		this.tiles = new int[width][height];
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[i].length; j++) {
				this.tiles[i][j] = -1;
			}
		}
		
		if(!editorMode) {
		
			xCorners = new ArrayList<Integer>();
			yCorners = new ArrayList<Integer>();
		}
		
	}
	
	//**********************Update and draw************************
	
	public void draw(Graphics g) {
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if(tiles[i][j] == 0) {
					g.drawImage(grassImg, i * this.tileSize, 
							j * this.tileSize, 
							null);
				}
				else if(tiles[i][j] == 2) {
					g.drawImage(mountainImg, i * this.tileSize, 
							j * this.tileSize, 
							null);
				}
				else if(tiles[i][j] == 3) {
					g.drawImage(treeImg, i * this.tileSize, 
							j * this.tileSize, 
							null);
				}
				else {
					g.setColor(Tile.getColor(tiles[i][j]));
					g.fillRect(i * this.tileSize, 
							j * this.tileSize, 
							this.tileSize, 
							this.tileSize);
				}
			}
		}
	}
	
	//**********************Getters************************
	
	public int getWidth() {
		return tiles.length;
	}
	
	public int getHeight() {
		return tiles[0].length;
	}
	
	public int getStart() {
		return start;
	}
	
	public int[] getXCorners() {
		int[] returnValue = new int[xCorners.size()];
		for(int i = 0; i < xCorners.size(); i++) {
			returnValue[i] = xCorners.get(i).intValue();
		}
		
		return returnValue;
	}
	
	public int[] getYCorners() {
		int[] returnValue = new int[yCorners.size()];
		for(int i = 0; i < yCorners.size(); i++) {
			returnValue[i] = yCorners.get(i).intValue();
		}
		
		return returnValue;
	}
	
	public int getTileSize() {
		return tileSize;
	}
	
	//**********************Setters************************
	
	//Sets a tile type located at (i, j)
	public void setTile(int i, int j, int type) {
		tiles[i][j] = type;
	}
	
	//**********************Game methods************************
	
	public boolean isPlaceable(int thisX, int thisY) {
		if(tiles[thisX][thisY] == Tile.GRASS) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//Detects if tile at (thisX, thisY) is a Tile.PATH tile 
	public boolean isPath(int thisX, int thisY) {
		return tiles[thisX][thisY] == Tile.PATH;
	}
	
	//returns [0] = x and [1] = y coordinates of next corner
	public int[] getNextCorner(int lastCorner) {
		if(lastCorner + 1 >= xCorners.size()) {
			return new int[1];
		}
		
		int[] coords = new int[2];
		coords[0] = xCorners.get(lastCorner + 1);
		coords[1] = yCorners.get(lastCorner + 1);
		
		return coords;
	}
	
	//Fills the tiles[][] array from a FileReader. Only called by
	//one of the other loadMap() functions that takes a String
	//or File argument.
	private void loadMap(FileReader reader) throws Exception {
		
		//Move the entire file to a char[] array
		char[] input = new char[1000]; 
		reader.read(input);
		
		//Extract width and height from line 1
		int counter = 0;
		String buffer = "";
		while(input[counter] != '\r') {
			if(input[counter] == ' ') {
				width = Integer.parseInt(buffer);
				buffer = "";
			}
			else {
				buffer += input[counter];
			}
			counter++;
		}
		height = Integer.parseInt(buffer);
		
		//Throw error if wrong map size
		if(width * tileSize != pixelWidth || height * tileSize != pixelHeight) {
			throw new Exception("Map file represents a " +
								width + 
								" x " + 
								height +
								" map, but current game setting is " +
								pixelWidth / tileSize +
								" x " +
								pixelHeight / tileSize);
		}
		
		//Extract all remaining tiles.
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[i].length; j++) {
				while(!Character.isDigit(input[counter])) {
					counter++;
				}
				if(input[counter-1] == '-') {
					tiles[i][j] = -Character.getNumericValue(input[counter]);
				}
				else {
					tiles[i][j] = Character.getNumericValue(input[counter]);
				}
				counter++;
			}
		}
		
		//find start
		for(int i = 0;i < height; i++) {
			if(tiles[0][i] == Tile.PATH) {
				start = i;
			}
		}
		
		//detect corners
		int xRunner = 0;
		int yRunner = start;
		String direction = "right";
		
		boolean[][] visited = new boolean[width][height];
		for(int i = 0; i < width;i++) {
			for(int j = 0; j < height;j++) {
				visited[i][j] = false;
			}
		}
		
		while(true) {
			//ending is ([width] - 1, y)
			if(xRunner + 1 == width) {
				break;
			}
			
			visited[xRunner][yRunner] = true;
			
			//if a new direction is to be taken, found a corner. Log it.
			if(visited[xRunner + 1][yRunner] == false && 
					tiles[xRunner + 1][yRunner] == Tile.PATH) {
				if(direction != "right") {
					xCorners.add(xRunner);
					yCorners.add(yRunner);
					direction = "right";
				}
				
				xRunner += 1;
			}
			else if(visited[xRunner - 1][yRunner] == false && 
					tiles[xRunner - 1][yRunner] == Tile.PATH) {
				if(direction != "left") {
					xCorners.add(xRunner);
					yCorners.add(yRunner);
					direction = "left";
				}
				
				xRunner -= 1;
			}
			else if(visited[xRunner][yRunner + 1] == false && 
					tiles[xRunner][yRunner + 1] == Tile.PATH) {
				if(direction != "down") {
					xCorners.add(xRunner);
					yCorners.add(yRunner);
					direction = "down";
				}
				
				yRunner += 1;
			}
			else if(visited[xRunner][yRunner - 1] == false && 
					tiles[xRunner][yRunner - 1] == Tile.PATH) {
				if(direction != "up") {
					xCorners.add(xRunner);
					yCorners.add(yRunner);
					direction = "up";
				}
				
				yRunner -= 1;
			}
			
		}
	}
	
	//Publicly accessible method for loading a map with a 
	//String as a filename
	public void loadMap(String fileName) {
		try {
			FileReader reader = new FileReader(fileName);
			loadMap(reader);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Publicly accessible method for loading a map with a 
	//File as a filename
	public void loadMap(File file) {
		try {
			FileReader reader = new FileReader(file);
			loadMap(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//Publicly accessible method for loading a user-selected file
	public void loadMap() {
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.showOpenDialog(null);
		
		try {
			FileReader reader = new FileReader(fileChooser.getSelectedFile());
			loadMap(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//Saves a map to a .tdm file
	public void saveMap() {
		
		//Prompt user to choose location to save to
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.showOpenDialog(null);
		
		//Open file and write the dimensions in the first line,
		//then each tile's type on a new line.
		try {
			PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile());
			writer.println(width + " " + height);
			for(int i = 0; i < tiles.length; i++) {
				for(int j = 0; j < tiles[i].length; j++) {
					writer.println(tiles[i][j]);
				}
			}
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
