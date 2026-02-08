package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.math.Rectangle;

public class WorldObject {
    public Rectangle bounds;
    public String type;

    public WorldObject(Rectangle bounds, String type) {
        this.bounds = bounds;
        this.type = type != null ? type : "muro";
    }
}