package com.github.paolo9688.mysticrpg.Components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.github.paolo9688.mysticrpg.WorldObject;

public class CollisionComponent {
    private float width, height;
    private float offsetX, offsetY;
    private float collisionWidth, collisionHeight;

    public CollisionComponent(float entityWidth, float entityHeight) {
        this.width = entityWidth;
        this.height = entityHeight;

        // Definiamo la hitbox (il rettangolo verde ai piedi)
        this.collisionWidth = width * 0.4f;
        this.collisionHeight = height * 0.2f;
        
        // Calcoliamo l'offset per centrare la hitbox alla base dello sprite
        this.offsetX = (width - collisionWidth) / 2f;
        this.offsetY = 27f; // L'offset che avevamo trovato prima
    }

    /**
     * Controlla se una data posizione collide con gli ostacoli del mondo
     */
    public boolean check(float x, float y, Array<WorldObject> objects) {
        Rectangle bounds = new Rectangle(x + offsetX, y + offsetY, collisionWidth, collisionHeight);
        
        for (WorldObject obj : objects) {
            if (bounds.overlaps(obj.bounds)) return true;
        }
        return false;
    }

    // Getter per il debug visivo
    public Rectangle getBounds(float x, float y) {
        return new Rectangle(x + offsetX, y + offsetY, collisionWidth, collisionHeight);
    }
}