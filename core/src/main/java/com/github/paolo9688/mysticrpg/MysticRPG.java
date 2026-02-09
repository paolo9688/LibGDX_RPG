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
    private DialogManager dialogManager;
    private float worldWidth = 1280; // Dimensione del mondo (adatta alla tua mappa)
    private float worldHeight = 720; // Dimensione del mondo (adatta alla tua mappa)

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerSheet = new Texture("Cute_Fantasy_Free/Player/Player.png");
        String path = "Cute_Fantasy_Free/Maps/test.tmx";
        mapManager = new MapManager(path);
        player = new Player(playerSheet, 100, 100);
        inputHandler = new InputHandler();
        gameCamera = new GameCamera(worldWidth, worldHeight); // Dimensione del mondo (adatta alla tua mappa)
        shapeRenderer = new ShapeRenderer();
        dialogManager = new DialogManager();
    }

    @Override
    public void render() {
        // 1. Logica di Gioco (Update)
        inputHandler.update();

        // Passa input e ostacoli al player per aggiornare posizione e animazione
        player.update(Gdx.graphics.getDeltaTime(), inputHandler, mapManager.getWorldObjects());

        // Gestione dell'interazione quando il tasto è appena stato premuto
        if (inputHandler.isInteractJustPressed()) {
            Rectangle iRange = player.getInteractionRange();
            boolean oggettoTrovato = false;
            
            for (WorldObject obj : mapManager.getWorldObjects()) {
                if (iRange.overlaps(obj.bounds)) {
                    oggettoTrovato = true;

                    if ("cassa".equals(obj.type)) {
                        dialogManager.showMessage("Hai aperto una cassa!", 3f);
                    }
                    else if ("cartello".equals(obj.type)) {
                        // Leggiamo la proprietà personalizzata "testo" che abbiamo messo in Tiled
                        // Nota: dobbiamo aver salvato le proprietà nel WorldObject
                        dialogManager.showMessage(obj.customMessage, 4f);
                    }
                    break; // Fermati al primo oggetto trovato
                }
            }

            // Se dopo aver controllato tutti gli oggetti non ne abbiamo trovato nessuno...
            if (!oggettoTrovato) {
                dialogManager.showMessage("Non c'è nulla con cui interagire.", 2f);
            }
        }

        // Aggiorna il dialogo (per gestire il timer di visualizzazione)
        dialogManager.update(Gdx.graphics.getDeltaTime());

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

        dialogManager.draw(batch, shapeRenderer, worldWidth, worldHeight);

        // 5. Debug: disegna le collisioni
        shapeRenderer.setProjectionMatrix(gameCamera.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // "Line" per vedere solo i bordi
        shapeRenderer.setColor(Color.RED);

        for (WorldObject obj : mapManager.getWorldObjects()) {
            // Usiamo obj.bounds per disegnare il rettangolo rosso
            shapeRenderer.rect(obj.bounds.x, obj.bounds.y, obj.bounds.width, obj.bounds.height);
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

        // Disegniamo anche l'area di interazione del player in giallo per debug
        shapeRenderer.setColor(Color.YELLOW);
        Rectangle iRange = player.getInteractionRange();
        shapeRenderer.rect(iRange.x, iRange.y, iRange.width, iRange.height);

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
        mapManager.dispose();
        dialogManager.dispose();
    }
}