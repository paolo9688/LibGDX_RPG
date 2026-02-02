package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MysticRPG extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture playerSheet;
    private Player player;
    private InputHandler inputHandler;
    private GameCamera gameCamera;
    private MapManager mapManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerSheet = new Texture("Cute_Fantasy_Free/Player/Player.png");
        String path = "Cute_Fantasy_Free/Maps/test.tmx";
        mapManager = new MapManager(path);
        player = new Player(playerSheet, 100, 100);
        inputHandler = new InputHandler();
        gameCamera = new GameCamera(800, 600);
    }

    @Override
    public void render() {
        // 1. Logica di Gioco (Update)
        inputHandler.update(); 
        player.update(Gdx.graphics.getDeltaTime(), inputHandler);
        gameCamera.update(player.getPosition(), player.getWidth(), player.getHeight());

        // 2. Pulizia dello schermo e disegno (Render)
        ScreenUtils.clear(0, 0, 0, 1);

        // 3. Disegniamo la mappa
        mapManager.render(gameCamera.getCamera());

        // 4. Applichiamo la telecamera al batch
        gameCamera.applyTo(batch); // Assicura che il batch segua la camera
        batch.begin();
        player.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerSheet.dispose();
    }
}