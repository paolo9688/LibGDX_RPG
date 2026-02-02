package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapManager {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public MapManager(String path) {
        // Carica il file .tmx
        map = new TmxMapLoader().load(path);
        // Il renderer disegna la mappa. 
        // Nota: 1f significa "scala 1:1". Se vuoi i tile grandi come prima, useremo la camera.
        renderer = new OrthogonalTiledMapRenderer(map, 4f);

        // Iteriamo su tutti i tileset della mappa per trovare le texture
        for (TiledMapTileSet tileset : map.getTileSets()) {
            // Ogni tileset può avere delle proprietà o riferimenti alle immagini
            // Il modo più diretto in libGDX per accedere alle texture caricate è questo:
            for (TiledMapTile tile : tileset) {
                if (tile.getTextureRegion() != null) {
                    Texture tex = tile.getTextureRegion().getTexture();
                    tex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                }
            }
        }
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}