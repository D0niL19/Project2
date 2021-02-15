package com.mygdx.game;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;

public class BulletUtil {
    private static final Vector3 rayFrom = new Vector3();
    private static final Vector3 rayTo = new Vector3();
    private static final ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);

    public static btCollisionObject rayTest(btCollisionWorld collisionWorld, Ray ray) {
        rayFrom.set(ray.origin);
        // 50 meters max from the origin
        rayTo.set(ray.direction).scl(50f).add(rayFrom);

        // we reuse the ClosestRayResultCallback, thus we need to reset its
        // values
        callback.setCollisionObject(null);
        callback.setClosestHitFraction(1f);
        Vector3 rayFromWorld = new Vector3();
        rayFromWorld.set(rayFrom.x, rayFrom.y, rayFrom.z);
        Vector3 rayToWorld   = new Vector3();
        rayToWorld.set(rayTo.x, rayTo.y, rayTo.z);

        collisionWorld.rayTest(rayFrom, rayTo, callback);

        if (callback.hasHit()) {
            return callback.getCollisionObject();
        }

        return null;
    }
}
