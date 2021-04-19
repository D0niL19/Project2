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


	private MainMenu mainMenu;
	public PlayScreen playScreen;
	private Viewport viewport;
	private Camera camera;


	@Override
	public void create () {
		//camera = new PerspectiveCamera();

		playScreen = new PlayScreen();
		mainMenu = new MainMenu(MainC.this);
		setScreen(mainMenu);
	}


}
