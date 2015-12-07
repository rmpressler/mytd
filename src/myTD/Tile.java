package myTD;

import java.awt.Color;

public class Tile {
	
	//**********************Fields************************
	
	//Constants for representing tile types
	public static final int GRASS = 0;
	public static final int PATH = 1;
	
	//**********************Constructor************************
	
	public static Color getColor(int type) {
		if(type == GRASS) {
			return Color.GREEN;
		}
		else if(type == PATH) {
			return Color.GRAY;
		}
		else {
			return Color.WHITE;
		}
	}
}
