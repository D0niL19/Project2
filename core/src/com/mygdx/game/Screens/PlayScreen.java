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

import sun.security.krb5.internal.crypto.CksumType;

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
    private Stage stage_start;

    private boolean stage_remembered;

    private Texture number1 = new Texture("numbers/one.png");
    private Texture number2 = new Texture("numbers/two.png");
    private Texture number3 = new Texture("numbers/three.png");
    private Texture number4 = new Texture("numbers/four.png");
    private Texture number5 = new Texture("numbers/five.png");
    private Texture number6 = new Texture("numbers/six.png");
    private Texture remembered = new Texture("badlogic.jpg");

    private Vector2 btnSize = new Vector2();

    private int number;

    private boolean Touched = false;
    // STAGE------------------------------------------------
    // RANDOM----------------------------------------------
    public ArrayList Location_Images = new ArrayList();

    // RANDOM----------------------------------------------

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

        // RANDOM--------------------------------------
        final List<Integer> arr = new ArrayList<>();
        final List<Integer> arrdeleted = new ArrayList<>();
//        ArrayList Location_Images = new ArrayList();
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
        System.out.println(arr);

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

        // D3DJ ------------------------------------------------------
        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        Model model1 = modelLoader.loadModel(Gdx.files.getFileHandle("Cubes/Ready.g3dj", Files.FileType.Internal));
        ModelInstance modelInstance1 = new ModelInstance(model1);
        instances.add(modelInstance1);

        for (int i = 1; i <= 6; i++) {
            instances.get(0).getMaterial("Mat" + arr.get(i)).clear();
            instances.get(0).getMaterial("Mat" + arr.get(i)).set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("" + Location_Images.get(i))));
        }
        instances.get(0).getNode("Cube").getChild(5);
        // D3DJ ------------------------------------------------------

        //STAGE----------------------------------------------
        stage = new Stage();
        stage_start = new Stage();
        stage_remembered = false;
        Button number_1 = new Button(new SpriteDrawable(new Sprite(number1)));
        Button number_2 = new Button(new SpriteDrawable(new Sprite(number2)));
        Button number_3 = new Button(new SpriteDrawable(new Sprite(number3)));
        Button number_4 = new Button(new SpriteDrawable(new Sprite(number4)));
        Button number_5 = new Button(new SpriteDrawable(new Sprite(number5)));
        Button number_6 = new Button(new SpriteDrawable(new Sprite(number6)));

        Button Remembered = new Button(new SpriteDrawable(new Sprite(remembered)));

        btnSize = new Vector2(140, 140);
        number = 1;

        number_1.setBounds(MainC.WIDTH / 2 - btnSize.x / 2 - btnSize.x - 20, MainC.HEIGHT / 9 * 5, btnSize.x, btnSize.y);
        number_2.setBounds(MainC.WIDTH / 2 - btnSize.x / 2 , MainC.HEIGHT / 9 * 5, btnSize.x, btnSize.y);
        number_3.setBounds(MainC.WIDTH / 2 - btnSize.x / 2  + btnSize.x + 20, MainC.HEIGHT / 9 * 5, btnSize.x, btnSize.y);
        number_4.setBounds(MainC.WIDTH / 2 - btnSize.x / 2 - btnSize.x - 20, MainC.HEIGHT / 9 * 5 - btnSize.y - 20, btnSize.x, btnSize.y);
        number_5.setBounds(MainC.WIDTH / 2 - btnSize.x / 2 , MainC.HEIGHT / 9 * 5 - btnSize.y - 20, btnSize.x, btnSize.y);
        number_6.setBounds(MainC.WIDTH / 2 - btnSize.x / 2 + btnSize.x + 20, MainC.HEIGHT / 9 * 5 - btnSize.y - 20, btnSize.x, btnSize.y);

        Remembered.setBounds(MainC.WIDTH / 2 - btnSize.x / 2, 30, btnSize.x, btnSize.y);

        final InputMultiplexer multiplexer = new InputMultiplexer();

        number_1.addListener(new ClickListener(){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(!Touched)return;
                Touched = false;
                number = 1;
                closest.parts.get(0).material.set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("" + Location_Images.get(number))));
                Gdx.input.setInputProcessor(multiplexer);
                super.touchUp(event, x, y, pointer, button);
            }
        });
        number_2.addListener(new ClickListener(){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(!Touched)return;
                Touched = false;
                number = 2;
                closest.parts.get(0).material.set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("" + Location_Images.get(number))));
                Gdx.input.setInputProcessor(multiplexer);
                super.touchUp(event, x, y, pointer, button);
            }
        });
        number_3.addListener(new ClickListener(){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(!Touched)return;
                Touched = false;
                number = 3;
                closest.parts.get(0).material.set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("" + Location_Images.get(number))));
                Gdx.input.setInputProcessor(multiplexer);
                super.touchUp(event, x, y, pointer, button);
            }
        });
        number_4.addListener(new ClickListener(){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(!Touched)return;
                Touched = false;
                number = 4;
                closest.parts.get(0).material.set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("" + Location_Images.get(number))));
                Gdx.input.setInputProcessor(multiplexer);
                super.touchUp(event, x, y, pointer, button);
            }
        });
        number_5.addListener(new ClickListener(){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(!Touched)return;
                Touched = false;
                number = 5;
                closest.parts.get(0).material.set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("" + Location_Images.get(number))));
                Gdx.input.setInputProcessor(multiplexer);
                super.touchUp(event, x, y, pointer, button);
            }
        });
        number_6.addListener(new ClickListener(){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(!Touched)return;
                Touched = false;
                number = 6;
                closest.parts.get(0).material.set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("" + Location_Images.get(number))));
                Gdx.input.setInputProcessor(multiplexer);
                super.touchUp(event, x, y, pointer, button);
            }
        });
        Remembered.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stage_remembered = true;
                for (int i = 1; i <= 6; i++) {
                    arrdeleted.add(i);
                }
                Collections.shuffle(arrdeleted);
                arrdeleted.remove(0);
                for (int i :arrdeleted) {
                    instances.get(0).getMaterial("Mat" + arr.get(i)).clear();
                }
                multiplexer.removeProcessor(stage_start);
                Gdx.input.setInputProcessor(multiplexer);
                super.touchUp(event, x, y, pointer, button);
            }

            /*@Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return super.touchDown(event, x, y, pointer, button);
            }*/

        });

        stage.addActor(number_1);
        stage.addActor(number_2);
        stage.addActor(number_3);
        stage.addActor(number_4);
        stage.addActor(number_5);
        stage.addActor(number_6);
        stage_start.addActor(Remembered);
        //STAGE----------------------------------------------

        // CAMERA--------------------------------------
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(-2, 2, 2);
        camera.lookAt(0, 0, 0);
        camera.far = 100;
        camera.near = 0.01f;

        controll = new CameraInputController(camera);
        controll.autoUpdate = true;
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage_start);
        multiplexer.addProcessor(controll);


        Gdx.input.setInputProcessor(multiplexer);
        // CAMERA--------------------------------------


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
        if (Touched) {
            stage.act();
            stage.draw();
        }
        if(!stage_remembered){
            stage_start.act();
            stage_start.draw();
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

    private Node closest;

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isDragged || Touched || !stage_remembered) return false;

        Ray ray = camera.getPickRay(screenX, screenY);
        count += 1;
        System.out.println(count);
        ArrayList<Node> nodes = new ArrayList<>();
        boolean nodeDone = false;
        for (int i = 0; i < instances.get(0).getNode("Cube").getChildCount(); i++) {
            Node node = instances.get(0).getNode("Cube").getChild(i);
            node.calculateBoundingBox(box);
            if (Intersector.intersectRayBoundsFast(ray, box)) {
                nodes.add(node);
                System.out.println("Done");
                nodeDone = true;
            }
        }
        if (count > 1 && nodeDone) {
            closest = nodes.get(0);
            Vector3 boxLocation = new Vector3();
            closest.calculateBoundingBox(box).getCenter(boxLocation);
            float minLength = new Vector3(camera.position.x - boxLocation.x, camera.position.y - boxLocation.y, camera.position.z - boxLocation.z).len();
            for (int i = 1; i < nodes.size(); i++) {
                nodes.get(i).calculateBoundingBox(box).getCenter(boxLocation);
                float otherLength = new Vector3(camera.position.x - boxLocation.x, camera.position.y - boxLocation.y, camera.position.z - boxLocation.z).len();
                if (minLength > otherLength) {
                    minLength = otherLength;
                    closest = nodes.get(i);
                }
            }
            Touched = true;
            //NodeDone = false;
            Gdx.input.setInputProcessor(stage);
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