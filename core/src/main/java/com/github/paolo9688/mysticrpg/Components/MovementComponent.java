package com.github.paolo9688.mysticrpg.Components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.paolo9688.mysticrpg.WorldObject;

public class MovementComponent {
    private Vector2 position;
    private float speed;
    private CollisionComponent collision;

    public MovementComponent(Vector2 position, float speed, CollisionComponent collision) {
        this.position = position;
        this.speed = speed;
        this.collision = collision;
    }

    public void update(float dt, float moveX, float moveY, Array<WorldObject> obstacles) {
        if (moveX != 0 || moveY != 0) {
            // Calcolo del movimento orizzontale
            float nextX = position.x + moveX * speed * dt;
            if (!collision.check(nextX, position.y, obstacles)) {
                position.x = nextX;
            }

            // Calcolo del movimento verticale
            float nextY = position.y + moveY * speed * dt;
            if (!collision.check(position.x, nextY, obstacles)) {
                position.y = nextY;
            }
        }
    }

    public Vector2 getPosition() {
        return position;
    }
}