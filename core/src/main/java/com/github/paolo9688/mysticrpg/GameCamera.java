package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameCamera {
    private OrthographicCamera camera;
    private Viewport viewport;
    private float lerp = 0.1f; // Intensità della morbidezza (0.1f = molto fluida, 1.0f = istantanea)

    public GameCamera(float width, float height) {
        camera = new OrthographicCamera();
        // FitViewport mantiene le proporzioni aggiungendo barre nere se necessario
        viewport = new FitViewport(width, height, camera);
        camera.position.set(width / 2, height / 2, 0);
    }

    public void update(Vector2 targetPosition, float targetWidth, float targetHeight) {
        // Calcolo della posizione "morbida"
        Vector3 position = camera.position;

        // Target centrato sul giocatore
        float targetX = targetPosition.x + targetWidth / 2;
        float targetY = targetPosition.y + targetHeight / 2;
        
        // Target x, y (aggiungiamo un offset se vogliamo centrare meglio lo sprite)
        position.x += (targetX - position.x) * lerp;
        position.y += (targetY - position.y) * lerp;
        camera.update();
    }

    public void applyTo(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}