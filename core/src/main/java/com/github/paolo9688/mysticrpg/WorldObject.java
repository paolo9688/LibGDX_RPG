package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.math.Rectangle;

public class WorldObject {
    public Rectangle bounds;
    public String type;
    public String customMessage; // Per memorizzare messaggi personalizzati (es. testo del cartello)

    public WorldObject(Rectangle bounds, String type, String customMessage) {
        this.bounds = bounds;
        this.type = type != null ? type : "muro";
        this.customMessage = customMessage != null ? customMessage : "Non succede nulla.";
    }
}