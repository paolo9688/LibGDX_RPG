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

    // Dimensioni "virtuali" per la telecamera (puoi regolarle a seconda di quanto vuoi zoomare)
    private float virtualWidth = 320; 
    private float virtualHeight = 180;

    private float accumulator = 0f;
    private final float TIME_STEP = 1/60f;

    @Override
    public void create() {
        Gdx.graphics.setVSync(true);
        batch = new SpriteBatch();
        playerSheet = new Texture("Cute_Fantasy_Free/Player/Player.png");
        playerSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        String path = "Cute_Fantasy_Free/Maps/test.tmx";
        mapManager = new MapManager(path);
        player = new Player(playerSheet, 100, 100); // Dimensioni 1:1
        inputHandler = new InputHandler();
        gameCamera = new GameCamera(virtualWidth, virtualHeight);
        shapeRenderer = new ShapeRenderer();
        dialogManager = new DialogManager();
    }

    @Override
    public void render() {
        // 1. Prendiamo il tempo reale passato dall'ultimo frame e lo mettiamo nel sacco
        float frameTime = Gdx.graphics.getDeltaTime();
        
        // Cap (limite): evitiamo la "spirale della morte" se il PC lagga di brutto
        if (frameTime > 0.25f) frameTime = 0.25f; { accumulator += frameTime; }

        while (accumulator >= TIME_STEP) {
            // --- LOGICA DI GIOCO FISSA (60Hz) ---
            inputHandler.update();
            player.update(TIME_STEP, inputHandler, mapManager.getWorldObjects());

            // GESTIONE INTERAZIONE (Spostata qui dentro!)
            if (inputHandler.isInteractJustPressed()) {
                Rectangle iRange = player.getInteractionRange();
                boolean oggettoTrovato = false;
                
                for (WorldObject obj : mapManager.getWorldObjects()) {
                    if (iRange.overlaps(obj.bounds)) {
                        oggettoTrovato = true;
                        if ("cassa".equals(obj.type)) {
                            dialogManager.showMessage("Hai aperto una cassa!", 3f);
                        } else if ("cartello".equals(obj.type)) {
                            dialogManager.showMessage(obj.customMessage, 4f);
                        }
                        break;
                    }
                }
                if (!oggettoTrovato) {
                    dialogManager.showMessage("Non c'è nulla con cui interagire.", 2f);
                }
            }
            
            accumulator -= TIME_STEP;
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
        Rectangle hitbox = player.getHitbox();
        shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

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