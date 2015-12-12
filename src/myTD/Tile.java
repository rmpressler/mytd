package myTD;

import java.awt.Color;

public class Tile {
	
	//**********************Fields************************
	
	//Constants for representing tile types
	public static final int GRASS = 0;
	public static final int PATH = 1;
	public static final int MOUNTAIN = 2;
	
	//**********************Constructor************************
	
	public static Color getColor(int type) {
		switch(type) {
			case GRASS:
				return Color.GREEN;
			case PATH:
				return Color.GRAY;
			case MOUNTAIN:
				return Color.ORANGE;
			default:
				return Color.WHITE;
		}
	}
}
