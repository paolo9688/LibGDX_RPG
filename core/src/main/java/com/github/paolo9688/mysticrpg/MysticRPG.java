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

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerSheet = new Texture("Cute_Fantasy_Free/Player/Player.png");
        player = new Player(playerSheet, 100, 100);
        inputHandler = new InputHandler();
    }

    @Override
    public void render() {
        // 1. Logica di Gioco (Update)
        inputHandler.update(); 
        
        player.update(Gdx.graphics.getDeltaTime(), inputHandler); 
        
        // 2. Pulizia dello schermo e disegno (Render)
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        
        batch.begin();
        player.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerSheet.dispose();
    }
}