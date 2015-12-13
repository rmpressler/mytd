package myTD;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GameStateManager {
	private int currentState;
	private ArrayList<GameState> gameStates;
	
	public static final int MENUSTATE = 0;
	public static final int PLAYSTATE = 1;
	public static final int EDITSTATE = 2;
	public static final int GAMEOVERSTATE = 3;
	public static final int PAUSESTATE = 4;
	
	public GameStateManager() {
		//Init
		gameStates = new ArrayList<GameState>();
		
		//Start on the menu
		currentState = MENUSTATE;
		
		//Load list of game states
		gameStates.add(new MenuState(this));
		gameStates.add(new PlayState(this));
		gameStates.add(new EditState(this));
		gameStates.add(new GameOverState(this));
		gameStates.add(new PauseState(this));
	}
	
	public void setState(int state) {
		int oldState = currentState;
		currentState = state;
		
		if(oldState != PAUSESTATE) {
			gameStates.get(currentState).init();
		}
	}
	
	public GameState getState(int i) {
		return gameStates.get(i);
	}
	
	public void update() {
		gameStates.get(currentState).update();
	}
	
	public void draw(Graphics2D g) {
		gameStates.get(currentState).draw(g);
	}
	
	public void mousePressed(MouseEvent e) {
		gameStates.get(currentState).mousePressed(e);
	}
	
	public void mouseMoved(MouseEvent e) {
		gameStates.get(currentState).mouseMoved(e);
	}
	
	public void keyPressed(int e) {
		gameStates.get(currentState).keyPressed(e);
	}
}
