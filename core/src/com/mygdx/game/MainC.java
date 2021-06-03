package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Screens.MainMenu;
import com.mygdx.game.Screens.PlayScreen;

public class MainC extends Game {
	public static int WIDTH = 480;
	public static int HEIGHT = 800;
	public static final String TITLE = "Spatial memory";


	public MainMenu mainMenu;
	public PlayScreen playScreen;
	private Viewport viewport;
	private Camera camera;

	public static MainC mainC;

	static {
		mainC = new MainC();
	}

	MainC() {

	}

	@Override
	public void create() {
		//camera = new PerspectiveCamera();
		playScreen = new PlayScreen();
		mainMenu = new MainMenu();
		setScreen(mainMenu);
	}

	public void playAgain(){
		playScreen = new PlayScreen();
	}

	public void GoHome(){
		mainMenu = new MainMenu();
		playScreen = new PlayScreen();
	}
}
