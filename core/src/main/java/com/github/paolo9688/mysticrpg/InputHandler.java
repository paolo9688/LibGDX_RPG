package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public class InputHandler {
    private float moveX, moveY;
    private final float DEADZONE = 0.2f;
    private boolean interactJustPressed;
    private boolean buttonAPreviouslyPressed = false;

    public void update() {
        moveX = 0;
        moveY = 0;
        interactJustPressed = false;

        // 1a. Gestione Tastiera - Movimento
        if (Gdx.input.isKeyPressed(Input.Keys.W)) moveY = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) moveY = -1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) moveX = -1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) moveX = 1;

        // 1b. Gestione Tastiera - Interazione
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            interactJustPressed = true;
        }

        // 2. Gestione Controller (se connesso)
        if (Controllers.getControllers().size > 0) {
            Controller controller = Controllers.getControllers().first();
            
            // LEVETTA ANALOGICA (Asse)
            float stickX = controller.getAxis(controller.getMapping().axisLeftX);
            float stickY = -controller.getAxis(controller.getMapping().axisLeftY);

            if (Math.abs(stickX) > DEADZONE) moveX = stickX;
            if (Math.abs(stickY) > DEADZONE) moveY = stickY;

            // D-PAD (Pulsanti)
            // Se non stiamo già muovendo la levetta, controlliamo le frecce
            if (moveX == 0 && moveY == 0) {
                if (controller.getButton(controller.getMapping().buttonDpadUp)) moveY = 1;
                if (controller.getButton(controller.getMapping().buttonDpadDown)) moveY = -1;
                if (controller.getButton(controller.getMapping().buttonDpadLeft)) moveX = -1;
                if (controller.getButton(controller.getMapping().buttonDpadRight)) moveX = 1;
            }

            // LOGICA PULSANTE A CON MEMORIA DELLO STATO
            boolean buttonACurrentlyPressed = controller.getButton(controller.getMapping().buttonA);

            // "Just Pressed" = Premuto ora E NON premuto prima
            if (buttonACurrentlyPressed && !buttonAPreviouslyPressed) {
                interactJustPressed = true;
            }

            // Aggiorniamo la memoria per il prossimo frame
            buttonAPreviouslyPressed = buttonACurrentlyPressed;
        }
    }

    public float getMoveX() { return moveX; }
    public float getMoveY() { return moveY; }
    public boolean isMoving() { return moveX != 0 || moveY != 0; }
    public boolean isInteractJustPressed() { return interactJustPressed; }
}