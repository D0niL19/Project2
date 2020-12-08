package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.mygdx.game.Screens.MainMenu;
import com.mygdx.game.Screens.PlayScreen;

public class MainC extends Game {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final String TITLE = "Spatial memory";
	private MainMenu mainMenu;
	public PlayScreen playScreen;
	
	@Override
	public void create () {
		playScreen = new PlayScreen();
		mainMenu = new MainMenu(MainC.this);
		setScreen(mainMenu);
	}

}
