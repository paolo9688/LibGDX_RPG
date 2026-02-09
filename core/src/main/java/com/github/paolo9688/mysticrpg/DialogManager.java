package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

public class DialogManager {
    private BitmapFont font;
    private String currentMessage = "";
    private float displayTimer = 0;
    private boolean isVisible = false;

    public DialogManager() {
        // 1. Carichiamo il file TTF
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Alkhemikal.ttf"));
        
        // 2. Impostiamo i parametri (dimensione, bordi, etc.)
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 32; // La dimensione reale in pixel
        parameter.borderWidth = 1; // Un piccolo bordo nero aiuta la leggibilità
        parameter.borderColor = Color.BLACK;
        parameter.color = Color.WHITE;
        parameter.minFilter = Texture.TextureFilter.Nearest; // Fondamentale per la pixel art
        parameter.magFilter = Texture.TextureFilter.Nearest;
        
        // 3. Generiamo la BitmapFont effettiva
        font = generator.generateFont(parameter);
        
        // Fondamentale per la pixel art:
        font.getRegion().getTexture().setFilter(parameter.minFilter, parameter.magFilter);
        
        // 4. Liberiamo la memoria del generatore (non ci serve più dopo aver creato il font)
        generator.dispose();
    }

    public void showMessage(String message, float duration) {
        this.currentMessage = message;
        this.displayTimer = duration;
        this.isVisible = true;
    }

    public void update(float dt) {
        if (displayTimer > 0) {
            displayTimer -= dt;
        } else {
            isVisible = false;
        }
    }

    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, float screenWidth, float screenHeight) {
        if (!isVisible) return;

        // 1. Disegniamo la Box (rettangolo nero semitrasparente)
        // Ora boxX e boxY sono relativi allo SCHERMO, non al MONDO
        float boxWidth = Math.round(screenWidth * 0.8f);
        float boxHeight = 150f;
        float boxX = Math.round((screenWidth - boxWidth) / 2f); // Centrata orizzontalmente
        float boxY = 50f; // 50 pixel dal fondo dello schermo

        // FONDAMENTALE: resettiamo la proiezione per "disegnare sullo schermo"
        // Usiamo una matrice d'identità che va da 0 a screenWidth/Height
        Matrix4 uiProjection = new Matrix4().setToOrtho2D(0, 0, screenWidth, screenHeight);

        // Applichiamo la matrice a entrambi
        shapeRenderer.setProjectionMatrix(uiProjection);
        batch.setProjectionMatrix(uiProjection);

        // Abilitiamo il blending per la trasparenza
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f); // Nero al 70% di opacità
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();

        // Disabilitiamo il blending per evitare problemi con altri disegni
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Disegniamo un bordino bianco
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();

        batch.begin(); // Riattiviamo il batch per il testo
        font.setColor(Color.WHITE);
        font.draw(batch, currentMessage, Math.round(boxX + 20), Math.round(boxY + boxHeight - 40));
        batch.end();
    }

    public void dispose() {
        font.dispose();
    }
}