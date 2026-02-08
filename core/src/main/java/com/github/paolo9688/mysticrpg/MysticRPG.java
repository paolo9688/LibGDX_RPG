package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MysticRPG extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture playerSheet;
    private Player player;
    private InputHandler inputHandler;
    private GameCamera gameCamera;
    private MapManager mapManager;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerSheet = new Texture("Cute_Fantasy_Free/Player/Player.png");
        String path = "Cute_Fantasy_Free/Maps/test.tmx";
        mapManager = new MapManager(path);
        player = new Player(playerSheet, 100, 100);
        inputHandler = new InputHandler();
        gameCamera = new GameCamera(1920, 1080); // Dimensione del mondo (adatta alla tua mappa)
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render() {
        // 1. Logica di Gioco (Update)
        inputHandler.update();
        
        // Passa anche le collisioni al player
        player.update(Gdx.graphics.getDeltaTime(), inputHandler, mapManager.getCollisionRects());

        // Aggiorna la telecamera per seguire il player
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

        // 5. Debug: disegna le collisioni
        shapeRenderer.setProjectionMatrix(gameCamera.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // "Line" per vedere solo i bordi
        shapeRenderer.setColor(Color.RED);

        for (Rectangle rect : mapManager.getCollisionRects()) {
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        
        // Disegniamo anche la collisione ai piedi del player per vedere se combacia!
        // (Usa le stesse proporzioni che abbiamo messo nella classe Player)
        shapeRenderer.setColor(Color.GREEN);
        float collisionWidth = player.getWidth() * 0.4f;
        float collisionHeight = player.getHeight() * 0.2f;
        float yOffset = 27f; // Lo stesso valore usato nel Player
        shapeRenderer.rect(
            player.getPosition().x + (player.getWidth() - collisionWidth) / 2, 
            player.getPosition().y + yOffset, 
            collisionWidth, 
            collisionHeight
        );

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerSheet.dispose();
        shapeRenderer.dispose();
        mapManager.dispose(); // Assicurati di chiamare dispose anche per il MapManager
    }
}