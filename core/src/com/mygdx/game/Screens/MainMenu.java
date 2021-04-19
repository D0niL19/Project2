package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MainC;

public class MainMenu implements Screen {
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private Stage stagebg;
    private Vector2 btnSize;
    private Texture background = new Texture("bg.jpg");
    private Texture playbtn = new Texture("playbtn.png");
    private Texture settingsbtn = new Texture("settingsbtn.png");
    private Texture shopbtn = new Texture("shopbtn.png");
    private Texture icon = new Texture("icosahedron.png");
    private Texture IntroText = new Texture("IntroText.png");

    public static boolean InGame = false;

    private MainC mc;
    public MainMenu(MainC mainC){
        mc = mainC;
    }

    /*public boolean Ingame(boolean g){
        g = this.InGame;
        return g;
    }*/

    @Override
    public void show() {
        MainC.WIDTH = Gdx.app.getGraphics().getWidth();
        MainC.HEIGHT = Gdx.app.getGraphics().getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        //viewport = new ExtendViewport(MainC.WIDTH, MainC.HEIGHT, camera);
        //viewport = new FitViewport(MainC.WIDTH, MainC.HEIGHT, camera);
        viewport = new FitViewport(Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight(), camera);
        stage = new Stage(viewport);
        stagebg = new Stage(viewport);

        btnSize = new Vector2(playbtn.getWidth(),playbtn.getHeight() );

        Button startBtn = new Button(new SpriteDrawable(new Sprite(playbtn)));
        Button settingsBtn = new Button(new SpriteDrawable(new Sprite(settingsbtn)));
        Button shopBtn = new Button(new SpriteDrawable(new Sprite(shopbtn)));

        startBtn.setBounds((MainC.WIDTH/2) - playbtn.getWidth()/ 2 , MainC.HEIGHT/ 7*2, btnSize.x , btnSize.y);
        settingsBtn.setBounds(MainC.WIDTH / 2 - settingsbtn.getWidth() + playbtn.getWidth() / 2, MainC.HEIGHT / 7 * 2 - settingsbtn.getHeight(), 78, 78);
        shopBtn.setBounds(MainC.WIDTH / 2 - playbtn.getWidth() / 2, MainC.HEIGHT / 7 * 2 - shopbtn.getHeight() , 78, 78);


        startBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mc.setScreen(mc.playScreen);
                InGame = true;
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        stage.addActor(startBtn);
        stage.addActor(settingsBtn);
        stage.addActor(shopBtn);


        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(background, 0,0, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
        stage.getBatch().draw(icon, MainC.WIDTH /2 - icon.getWidth() / 2  , MainC.HEIGHT / 5 * 3);
        stage.getBatch().draw(IntroText, MainC.WIDTH / 2 - IntroText.getWidth() / 2, MainC.HEIGHT / 5 * 4);
        stage.getBatch().end();

        stage.act();
        stage.draw();
    }
    @Override
    public void resize(int width, int height) {
//        stage.getViewport().setScreenSize(width, height);
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private NinePatch getScaledNinePatch(Texture texture, int left, int right, int top, int bottom) {
        NinePatch ninePatch = new NinePatch(texture, left, right, top, bottom);
        float scaleX = btnSize.x / texture.getWidth();
        float scaleY = btnSize.y / texture.getHeight();
        float scale = scaleX > scaleY ? scaleY : scaleX;
        ninePatch.scale(scale, scale);
        return ninePatch;
    }

}
