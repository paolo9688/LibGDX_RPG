package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.paolo9688.mysticrpg.Components.AnimationComponent;
import com.github.paolo9688.mysticrpg.Components.CollisionComponent;

public class Player {
    private Vector2 position;
    private float speed = 150f; // Velocità di movimento in pixel al secondo
    private boolean faceRight = true;

    // Componenti per animazione e collisione
    private AnimationComponent animator;
    private CollisionComponent collision;

    private float width = 128; // Dimensioni del disegno (8 frame * 32 pixel)
    private float height = 128; // Dimensioni del disegno (8 frame * 32 pixel)

    public Player(Texture sheet, float x, float y) {
        this.position = new Vector2(x, y);
        this.animator = new AnimationComponent(sheet, 0.15f);
        this.collision = new CollisionComponent(width, height);
    }

    public void update(float dt, InputHandler input, Array<WorldObject> obstacles) {
        float moveX = input.getMoveX();
        float moveY = input.getMoveY();

        // 1. Aggiorna l'animazione delegando tutto al componente
        animator.update(dt, moveX, moveY, moveX != 0 || moveY != 0);

        // 2. Gestisci il movimento e le collisioni
        handleMovement(dt, moveX, moveY, obstacles);

        // 3. Aggiorna la direzione del voltoa seconda del movimento orizzontale
        lookAt(moveX);
    }

    private void lookAt(float moveX){
        if (moveX > 0) faceRight = true;
        else if (moveX < 0) faceRight = false;
    }

    private void handleMovement(float dt, float moveX, float moveY, Array<WorldObject> obstacles) {
        if (moveX != 0 || moveY != 0) {
            float nextX = position.x + moveX * speed * dt;
            if (!collision.check(nextX, position.y, obstacles)) position.x = nextX;

            float nextY = position.y + moveY * speed * dt;
            if (!collision.check(position.x, nextY, obstacles)) position.y = nextY;
        }
    }

    public Rectangle getInteractionRange() {
        float rangeSize = 40f; // Quanto lontano può arrivare il braccio del player

        // Partiamo dal centro del player e poi spostiamo il rettangolo in avanti rispetto alla direzione in cui guarda
        float x = position.x + (width - rangeSize) / 2f;
        float y = position.y + (height - rangeSize) / 2f;

        // Chiediamo al componente in che direzione siamo rivolti
        AnimationComponent.Direction dir = animator.getLastDirection();

        if (dir == AnimationComponent.Direction.UP) {
            y += 45f;
        } else if (dir == AnimationComponent.Direction.DOWN) {
            y -= 45f;
        } else {
            if (faceRight) x += 45f;
            else x -= 45f;
        }

        return new Rectangle(x, y, rangeSize, rangeSize);
    }

    public void draw(SpriteBatch batch) {
        TextureRegion frame = animator.getCurrentFrame();

        int drawX = Math.round(faceRight ? position.x : position.x + width);
        int drawY = Math.round(position.y);
        float drawWidth = faceRight ? width : -width;

        batch.draw(frame, drawX, drawY, drawWidth, height);
    }

    // Metodo helper per il debug nel Main
    public Rectangle getHitbox() { return collision.getBounds(position.x, position.y);}

    // Getter per la posizione
    public Vector2 getPosition() { return position; }

    // Getter per dimensioni
    public float getWidth() { return width; }
    public float getHeight() { return height; }
}