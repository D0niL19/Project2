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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.mygdx.game.BulletInputProcessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class PlayScreen implements Screen, InputProcessor {
    private ModelBatch mbatch;
    private PerspectiveCamera camera;
    private ModelInstance model;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public AssetManager assets;

    private CameraInputController controll;
    private Environment env;
    private int count;



    // RAYCAST---------------------------------------------------
    public  static btDynamicsWorld world;
    private btCollisionConfiguration collisioConfig;
    private btDispatcher dispatcher;
    private btBroadphaseInterface broadphase;
    private btConstraintSolver constrainSolver;

    private BoundingBox box = new BoundingBox();
    // RAYCAST---------------------------------------------------


    @Override
    public void show() {

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
        // CAMERA--------------------------------------
        camera = new PerspectiveCamera(91, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(4, 4, 4);
        camera.lookAt(0, 0, 0);
        camera.far = 10;
        camera.near = 0.01f;

        controll = new CameraInputController(camera);
        controll.autoUpdate = true;
        // CAMERA--------------------------------------

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(controll);

        Gdx.input.setInputProcessor(controll);


        // D3DJ ------------------------------------------------------
        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        Model model1 = modelLoader.loadModel(Gdx.files.getFileHandle("Cubes/Ready.g3dj", Files.FileType.Internal));
        ModelInstance modelInstance1 = new ModelInstance(model1);
        instances.add(modelInstance1);

        for (int i = 1; i <= 6; i++) {

            instances.get(0).getMaterial("Mat" + arr.get(i) ).clear();
            instances.get(0).getMaterial("Mat" + arr.get(i)).set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("" + Location_Images.get(i))));
            System.out.println(arr);
        }
        //instances.get(0).getMaterial("Mat5").set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("Cubes/badlogic.jpg")));
        // D3DJ ------------------------------------------------------

    }



    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        controll.update();
        if(Gdx.input.justTouched()){
            Ray ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());
            count += 1;
            System.out.println(count);
            for (int i = 0; i < instances.get(0).getNode("Cube").getChildCount(); i++) {

                Node node = instances.get(0).getNode("Cube").getChild(i);
                node.calculateBoundingBox(box);
                if (Intersector.intersectRayBounds(ray, box, null) && count >2) {
                    node.parts.get(0).material.set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("Cubes/badlogic.jpg")));
                    System.out.println("aaaaaa");
                    break;
                }
            }
        }


        mbatch.begin(camera);
        mbatch.render(instances,env);
        mbatch.end();
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        /*System.out.println(22);
        Ray ray = camera.getPickRay(screenX,screenY);
        for (int i = 0; i < instances.get(0).getNode("Cube").getChildCount(); i++) {

            Node node = instances.get(0).getNode("Cube").getChild(i);
            node.calculateBoundingBox(box);

            if(Intersector.intersectRayBoundsFast(ray,box)){
                node.detach();
            }
        }*/
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println(11);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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