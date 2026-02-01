package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public class InputHandler {
    private float moveX, moveY;

    public void update() {
        moveX = 0;
        moveY = 0;

        // 1. Gestione Tastiera
        if (Gdx.input.isKeyPressed(Input.Keys.W)) moveY = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) moveY = -1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) moveX = -1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) moveX = 1;

        // 2. Gestione Controller (se connesso)
        if (Controllers.getControllers().size > 0) {
            Controller controller = Controllers.getControllers().first();
            
            // Levetta sinistra (Solitamente asse 0 e 1)
            float deadzone = 0.2f; // Per evitare movimenti fantasma se la levetta è usurata
            float stickX = controller.getAxis(controller.getMapping().axisLeftX);
            float stickY = -controller.getAxis(controller.getMapping().axisLeftY); // Invertito nell'asse Y

            if (Math.abs(stickX) > deadzone) moveX = stickX;
            if (Math.abs(stickY) > deadzone) moveY = stickY;
        }
    }

    public float getMoveX() { return moveX; }
    public float getMoveY() { return moveY; }
    public boolean isMoving() { return moveX != 0 || moveY != 0; }
}