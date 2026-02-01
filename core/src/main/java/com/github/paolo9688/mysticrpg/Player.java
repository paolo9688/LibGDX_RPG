package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Vector2 position;
    private float speed;
    private float stateTime;
    
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> walkAnim;
    private Animation<TextureRegion> currentAnim;
    
    private boolean faceRight = true;

    private float width = 128;
    private float height = 128;

    public Player(Texture sheet, float x, float y) {
        this.position = new Vector2(x, y);
        this.speed = 100f;
        this.stateTime = 0f;
        
        // Dividiamo lo sheet (32x32 pixel per frame)
        TextureRegion[][] tmp = TextureRegion.split(sheet, 32, 32);

        // Configurazione animazioni
        idleAnim = new Animation<>(0.1f, tmp[0]);
        walkAnim = new Animation<>(0.1f, tmp[4]);

        currentAnim = idleAnim;
    }

    public void update(float dt, InputHandler input) {
        stateTime += dt;
        
        if (input.isMoving()) {
            position.x += input.getMoveX() * speed * dt;
            position.y += input.getMoveY() * speed * dt;

            currentAnim = walkAnim;

            // Aggiorna la direzione dello sguardo
            if (input.getMoveX() > 0) faceRight = true;
            else if (input.getMoveX() < 0) faceRight = false;
        } else {
            currentAnim = idleAnim;
        }
    }

    public void draw(SpriteBatch batch) {
        TextureRegion frame = currentAnim.getKeyFrame(stateTime, true);

        // Calcolo per il flip senza modificare l'asset originale
        int drawX = Math.round(faceRight ? position.x : position.x + width);
        int drawY = Math.round(position.y);
        float drawWidth = faceRight ? width : -width;

        batch.draw(frame, drawX, drawY, drawWidth, height);
    }

    // Getter per la posizione
    public Vector2 getPosition() { return position; }

    // Getter per dimensioni
    public float getWidth() { return width; }
    public float getHeight() { return height; }
}