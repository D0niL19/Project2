package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

import java.util.ArrayList;

public class PlayScreen implements Screen {
    private ModelBatch mbatch;
    private PerspectiveCamera camera;
    private ModelInstance model;
    private ArrayList <ModelInstance> models;

    private CameraInputController controll;
    private Environment env;
    @Override
    public void show() {
        env = new Environment();
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
        env.add(new DirectionalLight().set(Color.WHITE, -.1f, -1f, -.1f));
        env.add(new PointLight().set(Color.BLUE, 0,0,0,5));

        mbatch = new ModelBatch();

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(5,5,5);
        camera.lookAt(0,0,0);
        camera.far = 100;
        camera.near = 0.1f;

        controll = new CameraInputController(camera);
        controll.autoUpdate = true;

        Gdx.input.setInputProcessor(controll);

        ObjLoader loader = new ObjLoader();
        model = new ModelInstance(loader.loadModel(Gdx.files.internal("CUbe.obj")));
        model.transform.scl(1.5f);

        Node Plane1Node = model.nodes.get(0);
        NodePart Plane1Part = Plane1Node.
        Material mat1 = Plane1Part.material;
        mat1.set(TextureAttribute.createDiffuse(new Texture("one.png")));






    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //camera.update();
        controll.update();

        mbatch.begin(camera);
        model.getNode("Plane2").globalTransform.scl(1.001f);
        mbatch.render(model,env);
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

    }
}
