package com.github.paolo9688.mysticrpg.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationComponent {
    private float stateTime;
    private Animation<TextureRegion> idleDown, idleUp, idleHorizontal;
    private Animation<TextureRegion> walkDown, walkUp, walkHorizontal;
    private Animation<TextureRegion> currentAnim;

    public AnimationComponent(Texture sheet, float frameDuration) {
        this.stateTime = 0f;
        TextureRegion[][] tmp = TextureRegion.split(sheet, 32, 32);

        idleDown       = new Animation<>(frameDuration, tmp[0]);
        idleHorizontal = new Animation<>(frameDuration, tmp[1]);
        idleUp         = new Animation<>(frameDuration, tmp[2]);
        walkDown       = new Animation<>(frameDuration, tmp[3]);
        walkHorizontal = new Animation<>(frameDuration, tmp[4]);
        walkUp         = new Animation<>(frameDuration, tmp[5]);

        currentAnim = idleDown;
    }

    public void update(float dt, float moveX, float moveY, boolean isMoving) {
        stateTime += dt;

        if (isMoving) {
            if (moveX != 0) currentAnim = walkHorizontal;
            else if (moveY > 0) currentAnim = walkUp;
            else if (moveY < 0) currentAnim = walkDown;
        } else {
            if (currentAnim == walkHorizontal) currentAnim = idleHorizontal;
            else if (currentAnim == walkUp) currentAnim = idleUp;
            else if (currentAnim == walkDown) currentAnim = idleDown;
        }
    }

    public TextureRegion getCurrentFrame() {
        return currentAnim.getKeyFrame(stateTime, true);
    }
}