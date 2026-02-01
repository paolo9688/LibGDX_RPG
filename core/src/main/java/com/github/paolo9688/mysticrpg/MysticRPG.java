package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MysticRPG extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture playerSheet;
    private Animation<TextureRegion> idleAnimation; // Gestisce i 6 frame
    private float stateTime; // Il timer per l'animazione
    TextureRegion idleFrame;

    float playerX, playerY;
    float speed = 100f; // Velocità di movimento del personaggio

    @Override
    public void create() {
        batch = new SpriteBatch();
        
        // CARICAMENTO (Assicurati che il percorso sia esatto)
        playerSheet = new Texture("Cute_Fantasy_Free/Player/Player.png");

        // DIVISIONE DELLO SHEET
        // split(texture, larghezzaFrame, altezzaFrame)
        TextureRegion[][] tmp = TextureRegion.split(playerSheet, 32, 32);

        // PRENDIAMO I PRIMI 6 FRAME (Prima riga)
        TextureRegion[] idleFrames = new TextureRegion[6];
        for (int i = 0; i < 6; i++) {
            idleFrames[i] = tmp[0][i]; 
        }

        // CREAZIONE ANIMAZIONE: 0.1f è la velocità (un frame ogni 0.1 secondi)
        idleAnimation = new Animation<TextureRegion>(0.1f, idleFrames);
        
        playerX = 100;
        playerY = 100;
        stateTime = 0f;
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        handleInput();

        // Aggiorna il cronometro dell'animazione
        stateTime += Gdx.graphics.getDeltaTime();
        
        // Recupera il frame corretto in base al tempo trascorso
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);

        batch.begin();
        // Disegna il frame corrente
        batch.draw(currentFrame, playerX, playerY, 64, 64); // Scala il personaggio a 64x64
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerSheet.dispose();
    }

    private void handleInput() {
    float dt = Gdx.graphics.getDeltaTime(); // Tempo trascorso dall'ultimo frame
    if (Gdx.input.isKeyPressed(Input.Keys.W)) playerY += speed * dt;
    if (Gdx.input.isKeyPressed(Input.Keys.S)) playerY -= speed * dt;
    if (Gdx.input.isKeyPressed(Input.Keys.A)) playerX -= speed * dt;
    if (Gdx.input.isKeyPressed(Input.Keys.D)) playerX += speed * dt;
    }
}