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
    private Texture grassTex;
    private Texture waterTex;
    private Player player;
    private InputHandler inputHandler;
    private GameCamera gameCamera;
    private WorldMap worldMap;

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerSheet = new Texture("Cute_Fantasy_Free/Player/Player.png");
        grassTex = new Texture("Cute_Fantasy_Free/Tiles/Grass_Middle.png");
        waterTex = new Texture("Cute_Fantasy_Free/Tiles/Water_Middle.png");
        grassTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        waterTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        player = new Player(playerSheet, 100, 100);
        inputHandler = new InputHandler();
        gameCamera = new GameCamera(800, 600);
        worldMap = new WorldMap(grassTex, waterTex);
    }

    @Override
    public void render() {
        // 1. Logica di Gioco (Update)
        inputHandler.update(); 
        player.update(Gdx.graphics.getDeltaTime(), inputHandler);
        gameCamera.update(player.getPosition(), player.getWidth(), player.getHeight());

        // 2. Pulizia dello schermo e disegno (Render)
        ScreenUtils.clear(0, 0, 0, 1);

        // 3. Applichiamo la telecamera al batch
        gameCamera.applyTo(batch);
        
        batch.begin();
        worldMap.draw(batch, player.getPosition().x, player.getPosition().y);
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
        grassTex.dispose();
        waterTex.dispose();
    }
}