package com.mygdx.game.Screens;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MainC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayScreen implements Screen, InputProcessor {
    private ModelBatch mbatch;
    private PerspectiveCamera camera;
    private ModelInstance model;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public AssetManager assets;

    private CameraInputController controll;
    private Environment env;
    private int count;

    // STAGE------------------------------------------------
    private Stage stage;
    private Texture playbtn = new Texture("playbtn.png");
    private Texture settingsbtn = new Texture("settingsbtn.png");
    private Texture shopbtn = new Texture("shopbtn.png");

    private Vector2 btnSize = new Vector2();

    private boolean Touched = false;
    private boolean NodeDone = false;
    private boolean WindowIsOpen = false;
    // STAGE------------------------------------------------


    // RAYCAST---------------------------------------------------
    public static btDynamicsWorld world;
    private btCollisionConfiguration collisioConfig;
    private btDispatcher dispatcher;
    private btBroadphaseInterface broadphase;
    private btConstraintSolver constrainSolver;

    private BoundingBox box;
    private Vector3 intersection;

    private List<BoundingBox> boundingBoxes = new ArrayList<>();
    // RAYCAST---------------------------------------------------


    @Override
    public void show() {

        intersection = new Vector3();
//        boundingBoxes.add(new BoundingBox(new Vector3(.5f, .5f, .5f), new Vector3(-.5f, -.5f, -.5f)));
        boundingBoxes.add(new BoundingBox(new Vector3(.5f, .6f, .5f), new Vector3(.6f, -.6f, -.5f)));
        boundingBoxes.add(new BoundingBox(new Vector3(-.5f, .6f, .5f), new Vector3(-.6f, -.6f, -.5f)));
        boundingBoxes.add(new BoundingBox(new Vector3(-.5f, .6f, .5f), new Vector3(.5f, .5f, -.5f)));
        boundingBoxes.add(new BoundingBox(new Vector3(-.5f, -.5f, .5f), new Vector3(.5f, -.6f, -.5f)));
        boundingBoxes.add(new BoundingBox(new Vector3(-.5f, -.5f, .5f), new Vector3(.5f, .5f, .6f)));
        boundingBoxes.add(new BoundingBox(new Vector3(-.5f, -.5f, -.5f), new Vector3(.5f, .5f, -.6f)));

        // RANDOM--------------------------------------
        List<Integer> arr = new ArrayList<>();
        ArrayList Location_Images = new ArrayList();
        Location_Images.add(-1);
        Location_Images.add("Cubes/one.png");
        Location_Images.add("Cubes/two.png");
        Location_Images.add("Cubes/three.png");
        Location_Images.add("Cubes/four.png");
        Location_Images.add("Cubes/five.png");
        Location_Images.add("Cubes/six.png");
        for (int i = 1; i <= 6; i++) {
            arr.add(i);
        }
        Collections.shuffle(arr);
        Integer[] shuffledArray = arr.toArray(new Integer[0]);
        arr.add(-1);
        Collections.reverse(arr);

        int count = 0;

        // RANDOM--------------------------------------

        // Bullet--------------------------------------
        Bullet.init();
        collisioConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisioConfig);
        broadphase = new btDbvtBroadphase();
        constrainSolver = new btSequentialImpulseConstraintSolver();

        world = new btDiscreteDynamicsWorld(dispatcher, broadphase, constrainSolver, collisioConfig);


        // Bullet--------------------------------------

        // ENVIRONMENT--------------------------------------
        env = new Environment();
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
        env.add(new DirectionalLight().set(Color.WHITE, -.1f, -1f, -.1f));
        env.add(new PointLight().set(Color.BLUE, 0, 0, 0, 5));

        mbatch = new ModelBatch();
        // ENVIRONMENT--------------------------------------

        //STAGE----------------------------------------------
        stage = new Stage();
        Button startBtn = new Button(new SpriteDrawable(new Sprite(playbtn)));
        Button settingsBtn = new Button(new SpriteDrawable(new Sprite(settingsbtn)));
        Button shopBtn = new Button(new SpriteDrawable(new Sprite(shopbtn)));

        btnSize = new Vector2(178, 178);

        startBtn.setBounds((MainC.WIDTH/2) - playbtn.getWidth()/ 2 , MainC.HEIGHT/ 7*2, btnSize.x , btnSize.y);

        startBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Touched = false;
                WindowIsOpen = false;
                //return super.touchDown(event, x, y, pointer, button);
                return false;
            }
        });

        stage.addActor(startBtn);

        //STAGE----------------------------------------------



        // CAMERA--------------------------------------
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(-2, 2, 2);
        camera.lookAt(0, 0, 0);
        camera.far = 100;
        camera.near = 0.01f;

        controll = new CameraInputController(camera);
        controll.autoUpdate = true;


        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(controll);

        Gdx.input.setInputProcessor(multiplexer);
        // CAMERA--------------------------------------
        // D3DJ ------------------------------------------------------
        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        Model model1 = modelLoader.loadModel(Gdx.files.getFileHandle("Cubes/Ready.g3dj", Files.FileType.Internal));
        ModelInstance modelInstance1 = new ModelInstance(model1);
        instances.add(modelInstance1);

        for (int i = 1; i <= 6; i++) {

            instances.get(0).getMaterial("Mat" + arr.get(i)).clear();
            instances.get(0).getMaterial("Mat" + arr.get(i)).set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("" + Location_Images.get(i))));
            System.out.println(arr);
        }
        instances.get(0).getNode("Cube").getChild(5);
        // D3DJ ------------------------------------------------------

        box = new BoundingBox();
    }


    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();
        controll.update();



        mbatch.begin(camera);
        mbatch.render(instances, env);
        mbatch.end();
        if(Touched) {
            WindowIsOpen = true;
            stage.act();
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {

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
        instances.clear();
        assets.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    private boolean isDragged;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isDragged = false;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isDragged || WindowIsOpen) return false;
//        Ray ray = camera.getPickRay(screenX, screenY);
//        count += 1;
//        System.out.println(count);
//        for (int i = 0; i < instances.get(0).getNode("Cube").getChildCount(); i++) {
//            Node node = instances.get(0).getNode("Cube").getChild(i);
//            for (BoundingBox box : boundingBoxes) {
//                if (Intersector.intersectRayBounds(ray, box, intersection) && count > 2) {
//                    // node.parts.get(0).material.set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("Cubes/badlogic.jpg")));
//                    System.out.println("Intersection " + intersection);
////                    System.out.println(instances.get(0).getNode("Cube").getChild(i).id);
//                    return false;
//                }
//            }
//
//        }

        Ray ray = camera.getPickRay(screenX, screenY);
        count += 1;
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < instances.get(0).getNode("Cube").getChildCount(); i++) {
            Node node = instances.get(0).getNode("Cube").getChild(i);
            node.calculateBoundingBox(box);
            if (Intersector.intersectRayBoundsFast(ray, box))  {
                nodes.add(node);
                NodeDone = true;
            }
        }
        if(count > 1 && NodeDone){
            Node closest = nodes.get(0);
            Vector3 boxLocation = new Vector3();
            closest.calculateBoundingBox(box).getCenter(boxLocation);
            float minLength = new Vector3(camera.position.x - boxLocation.x, camera.position.y - boxLocation.y, camera.position.z - boxLocation.z).len();
            for (int i = 1; i < nodes.size(); i++) {
                nodes.get(i).calculateBoundingBox(box).getCenter(boxLocation);
                float otherLength =  new Vector3(camera.position.x - boxLocation.x, camera.position.y - boxLocation.y, camera.position.z - boxLocation.z).len();
                if (minLength > otherLength) {
                    minLength = otherLength;
                    closest = nodes.get(i);
                }
            }
            closest.parts.get(0).material.set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("Cubes/badlogic.jpg")));
            Touched = true;
            NodeDone = false;
        }
        return false;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        isDragged = true;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}