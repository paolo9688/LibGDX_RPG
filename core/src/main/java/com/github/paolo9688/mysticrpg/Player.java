package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player {
    private Vector2 position;
    private float speed;
    private float stateTime;
    
    private Animation<TextureRegion> idleDownAnim, idleUpAnim, idleHorizontalAnim;
    private Animation<TextureRegion> walkHorizontalAnim, walkUpAnim, walkDownAnim;
    private Animation<TextureRegion> currentAnim;
    
    private boolean faceRight = true;

    // Se usi la risoluzione 640x360, 64 o 128 è meglio di 256 (che sarebbe enorme)
    private float width = 128; // Dimensioni del disegno (8 frame * 32 pixel)
    private float height = 128; // Dimensioni del disegno (8 frame * 32 pixel)

    public Player(Texture sheet, float x, float y) {
        this.position = new Vector2(x, y);
        this.speed = 150f;
        this.stateTime = 0f;
        
        // Dividiamo lo sheet (32x32 pixel per frame)
        TextureRegion[][] tmp = TextureRegion.split(sheet, 32, 32);

        // Configurazione animazioni
        idleDownAnim       = new Animation<>(0.15f, tmp[0]);
        idleHorizontalAnim = new Animation<>(0.15f, tmp[1]);
        idleUpAnim         = new Animation<>(0.15f, tmp[2]);
        walkDownAnim       = new Animation<>(0.15f, tmp[3]);
        walkHorizontalAnim = new Animation<>(0.15f, tmp[4]);
        walkUpAnim         = new Animation<>(0.15f, tmp[5]);

        currentAnim = idleDownAnim;
    }

    public void update(float dt, InputHandler input, Array<Rectangle> obstacles) {
        stateTime += dt;
        
        float moveX = input.getMoveX();
        float moveY = input.getMoveY();

        if (moveX != 0 || moveY != 0) {
            // 1. Gestione Animazioni
            if (moveX != 0) currentAnim = walkHorizontalAnim;
            else if (moveY > 0) currentAnim = walkUpAnim;
            else if (moveY < 0) currentAnim = walkDownAnim;

            if (moveX > 0) faceRight = true;
            else if (moveX < 0) faceRight = false;

            // 2. Calcolo collisioni separato (Scivolamento sui muri)
            
            // Prova movimento X
            float nextX = position.x + moveX * speed * dt;
            if (!isColliding(nextX, position.y, obstacles)) {
                position.x = nextX;
            }

            // Prova movimento Y
            float nextY = position.y + moveY * speed * dt;
            if (!isColliding(position.x, nextY, obstacles)) {
                position.y = nextY;
            }

        } else {
            // Stato IDLE
            if (currentAnim == walkHorizontalAnim) currentAnim = idleHorizontalAnim;
            else if (currentAnim == walkUpAnim) currentAnim = idleUpAnim;
            else if (currentAnim == walkDownAnim) currentAnim = idleDownAnim;
        }
    }

    // Metodo di supporto per controllare se la posizione è libera
    private boolean isColliding(float x, float y, Array<Rectangle> obstacles) {
        // Creiamo un rettangolo di collisione per il player (box piedi)
        // Lo facciamo più piccolo della sprite per non far incastrare i capelli nei muri!
        float collisionWidth = width * 0.4f;
        float collisionHeight = height * 0.2f;

        float yOffset = 27f; // Spostiamo il rettangolo un po' più in basso per coprire solo i piedi

        Rectangle playerRect = new Rectangle(
            x + (width - collisionWidth) / 2, 
            y + yOffset, // <--- Alziamo il punto di origine
            collisionWidth, 
            collisionHeight
        );

        for (Rectangle rect : obstacles) {
            if (playerRect.overlaps(rect)) return true;
        }
        return false;
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