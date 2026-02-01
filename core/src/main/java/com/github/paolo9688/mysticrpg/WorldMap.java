package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WorldMap {
    private TextureRegion grassTile;
    private TextureRegion waterTile;
    private final int TILE_SIZE = 64; // Dimensione a schermo di ogni mattonella

    public WorldMap(Texture grassTex, Texture waterTex) {
        // Prendiamo un tile di erba dallo sheet. 
        // Supponendo che nel tuo sheet l'erba sia a riga 9, colonna 0 (cambia se necessario)
        grassTile = new TextureRegion(grassTex);
        waterTile = new TextureRegion(waterTex);
    }

    public void draw(SpriteBatch batch, float playerX, float playerY) {
        // Calcoliamo quanti tile disegnare attorno al giocatore
        // 15 tile di raggio coprono abbondantemente una risoluzione 800x600
        int startX = (int) (playerX / TILE_SIZE) - 15;
        int startY = (int) (playerY / TILE_SIZE) - 15;

        for (int x = startX; x < startX + 30; x++) {
            for (int y = startY; y < startY + 30; y++) {
                
                // LOGICA PROCEDURALE SEMPLICE:
                // Se le coordinate X e Y sono comprese tra 0 e 10, disegna Erba.
                // Altrimenti disegna Acqua (creando un'isola 10x10).
                TextureRegion currentTile;
                if (x >= 0 && x < 10 && y >= 0 && y < 10) {
                    currentTile = grassTile;
                } else {
                    currentTile = waterTile;
                }

                // Nel loop della mappa
                batch.draw(currentTile, Math.round(x * TILE_SIZE), Math.round(y * TILE_SIZE), TILE_SIZE, TILE_SIZE);
            }
        }
    }
}