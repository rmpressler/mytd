package myTD;

import java.awt.Color;

public class Tile {
	
	//**********************Fields************************
	
	//Constants for representing tile types
	public static final int GRASS = 0;
	public static final int PATH = 1;
	public static final int MOUNTAIN = 2;
	public static final int FOREST = 3;
	
	//**********************Constructor************************
	
	public static String getImg(int type) {
		switch(type) {
		case GRASS:
			return "grass_200_200.png";
		case PATH:
			return "";
		case MOUNTAIN:
			return "";
		case FOREST:
			return "";
		default:
			return "";
	}
	}
	
	public static Color getColor(int type) {
		switch(type) {
			case GRASS:
				return Color.GREEN;
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
