package myTD;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tile {
	
	//**********************Fields************************
	
	//Constants for representing tile types
	public static final int GRASS0 = 0;
	public static final int GRASS1 = 1;
	public static final int GRASS2 = 2;
	public static final int GRASS3 = 3;
	public static final int GRASS4 = 4;
	public static final int GRASS5 = 5;
	public static final int GRASS6 = 6;
	public static final int GRASS7 = 7;
	public static final int GRASS8 = 8;
	public static final int PATH = 9;
	public static final int MOUNTAIN = 10;
	public static final int FOREST = 11;
	
	public static BufferedImage grassPathTileSheet;
	public static BufferedImage[] grassPathTile;
	public static BufferedImage pathTile;
	
	//**********************Constructor************************
	
	public static void init() {
		
		//initialize and parse grass path variables 
		try {
			grassPathTileSheet = ImageIO.read(new File("grass_path_tileset.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		grassPathTile = new BufferedImage[9];
		
		for(int i = 0;i < 9; i++) {
			int cell = i % 3;
			int row = i / 3;
			grassPathTile[i] = grassPathTileSheet.getSubimage(cell * 64,  row * 64,  64,  64);
		}
		
		//initialize and parse path tile
		try {
			pathTile = ImageIO.read(new File("path_64_64.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static BufferedImage getImg(int type) {
		if(type >= GRASS0 && type <= GRASS8) {
			return grassPathTile[type];
		}
		else if(type == PATH) {
			return pathTile;
		}
		else {
			return null;
		}
		
//		switch(type) {
//		case GRASS0:
//			return "grass_200_200.png";
//		case PATH:
//			return "";
//		case MOUNTAIN:
//			return "";
//		case FOREST:
//			return "";
//		default:
//			return "";
	}
	
	public static Color getColor(int type) {
		switch(type) {
			case PATH:
				return Color.GRAY;
			case MOUNTAIN:
				return Color.ORANGE;
			case FOREST:
				return Color.GREEN.darker();
			default:
				return Color.WHITE;
		}
	}
}
