package com.github.paolo9688.mysticrpg;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class MapManager {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Array<Rectangle> collisionRects = new Array<>();
    private float scale = 4f; // Scala per ingrandire i tile

    public MapManager(String path) {
        // Carica il file .tmx
        map = new TmxMapLoader().load(path);
        // Il renderer disegna la mappa. 
        // Nota: 1f significa "scala 1:1". Se vuoi i tile grandi come prima, useremo la camera.
        renderer = new OrthogonalTiledMapRenderer(map, scale);

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

        // Carica le collisioni dalla mappa
        loadCollisions();
    }

    public void loadCollisions() {
        collisionRects.clear();
        MapLayer layer = map.getLayers().get("Collisioni"); // Deve avere lo stesso nome che hai dato in Tiled
        if (layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    
                    // Applichiamo la scala per ingrandire le collisioni
                    collisionRects.add(new Rectangle(
                        rect.x * scale, 
                        rect.y * scale, 
                        rect.width * scale, 
                        rect.height * scale
                    ));
                }
            }
        }
    }

    public Array<Rectangle> getCollisionRects() {
        return collisionRects;
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