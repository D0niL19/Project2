package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BulletInputProcessor extends InputAdapter {

    private Viewport pickingViewport;
    private btCollisionWorld collisionWorld;

    // a constructor which takes a Viewport and the collision world and stores them
    public BulletInputProcessor(Array<ModelInstance> inst, Viewport view) {

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            Ray pickRay = pickingViewport.getPickRay(screenX, screenY);

            btCollisionObject body = BulletUtil.rayTest(collisionWorld, pickRay);
            if (body != null) {
                // do whatever you want with this body now
                return true;
            }
        }

        return false;
    }
}
