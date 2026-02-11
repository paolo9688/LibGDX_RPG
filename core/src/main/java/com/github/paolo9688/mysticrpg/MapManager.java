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
    private Array<WorldObject> worldObjects = new Array<>();
    private float scale = 1f; // Scala per ingrandire i tile

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
                    tile.getTextureRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                }
            }
        }

        // Carica le collisioni dalla mappa
        loadCollisions();
    }

    public void loadCollisions() {
        worldObjects.clear();
        MapLayer layer = map.getLayers().get("Collisioni"); // Deve avere lo stesso nome che hai dato in Tiled
        if (layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();

                    // Leggiamo la proprietà "tipo" da Tiled
                    String tipo = object.getProperties().get("tipo", String.class);

                    // Leggiamo la proprietà "testo" da Tiled (per i cartelli)
                    String messaggio = object.getProperties().get("testo", String.class);
                    
                    // Applichiamo la scala per ingrandire le collisioni
                    Rectangle scaledRect = new Rectangle(
                        rect.x * scale, 
                        rect.y * scale, 
                        rect.width * scale, 
                        rect.height * scale
                    );

                    worldObjects.add(new WorldObject(scaledRect, tipo, messaggio));
                }
            }
        }
    }

    public Array<WorldObject> getWorldObjects() {
        return worldObjects;
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.getBatch().setProjectionMatrix(camera.combined);
        renderer.render();
    }

    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}