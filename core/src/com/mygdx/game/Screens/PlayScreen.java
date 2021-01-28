package com.mygdx.game.Screens;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.mygdx.game.Assets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayScreen implements Screen {
    private ModelBatch mbatch;
    private PerspectiveCamera camera;
    private ModelInstance model;
    private ArrayList <ModelInstance> models;

    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public boolean loading;
    public AssetManager assets;

    private CameraInputController controll;
    private Environment env;
    private String change;



    @Override
    public void show() {
        //--------------------------------------RANDOM
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

        //--------------------------------------RANDOM


        env = new Environment();
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
        env.add(new DirectionalLight().set(Color.WHITE, -.1f, -1f, -.1f));
        env.add(new PointLight().set(Color.BLUE, 0, 0, 0, 5));

        mbatch = new ModelBatch();

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(5, 5, 5);
        camera.lookAt(0, 0, 0);
        camera.far = 100;
        camera.near = 0.1f;

        controll = new CameraInputController(camera);
        controll.autoUpdate = true;

        Gdx.input.setInputProcessor(controll);

        // D3DJ ------------------------------------------------------
        G3dModelLoader modelLoader = new G3dModelLoader(new JsonReader());
        Model model1 = modelLoader.loadModel(Gdx.files.getFileHandle("Cubes/Ready.g3dj", Files.FileType.Internal));
        ModelInstance modelInstance1 = new ModelInstance(model1);
        instances.add(modelInstance1);

        for (int i = 1; i <= 6; i++) {

            instances.get(0).getMaterial("Mat" + arr.get(i) ).clear();
            instances.get(0).getMaterial("Mat" + arr.get(i)).set(new TextureAttribute(TextureAttribute.Diffuse, new Texture("" + Location_Images.get(i))));
        }



    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


            //Model cube = assets.get("Cubes/abc.g3dj", Model.class);
        //camera.update();
        controll.update();

        mbatch.begin(camera);
//        model.getNode("Plane2").globalTransform.scl(1.001f);
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
}