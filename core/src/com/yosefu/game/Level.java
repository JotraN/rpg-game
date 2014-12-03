package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class Level {
    private final Yosefu game;
    private final Engine currentEngine;
    public static final String EMPTY = "0";
    public static final String WALL = "1";
    public static final String DOOR = "abcdefhijklmn";
    public static final String ENEMY = "opqrstuvwxyz";
    public static final String NPC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String[][] tileMap;
    private static HashMap<String, String> objectVariables = new HashMap<String, String>();
    private static HashMap<String, Interactive> objects = new HashMap<String, Interactive>();
    private String current = "map_01";
    private Texture bg;
    private Texture bgOver;

    public Level(Yosefu game, Engine currentEngine) {
        this.game = game;
        this.currentEngine = currentEngine;
        loadLevel();
    }

    private void loadLevel() {
        FileHandle file = Gdx.files.internal("levels/" + current);
        String[] rows = file.readString().split("\\n");
        int start = 0;
        // Get location of start of tile map.
        for (int i = 0; i < rows.length; i++) {
            if (rows[i].startsWith("1")) {
                start = i;
                break;
            }
        }

        for (int i = 0; i < start; i++)
            objectVariables.put(rows[i].substring(0, 1), rows[i].substring(2));

        tileMap = new String[rows.length - start][rows[start].split(", ").length];
        for (int i = 0, j = start; i < tileMap.length; i++, j++) {
            tileMap[i] = rows[j].split(", ");
        }

        bg = new Texture(Gdx.files.internal("grasssprite.png"));
        bg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
//        bgOver = new Texture(Gdx.files.internal("levels/town_01.png"));
        setupLevel();
    }

    private void setupLevel() {
        float x = 0, y = 0;
        float blockSize = 64;
        for (String[] row : tileMap) {
            for (String item : row) {
                if (DOOR.contains(item))
                    objects.put(item, new Door(x, y, blockSize, blockSize, this));
                else if (NPC.contains(item)) {
                    String text = objectVariables.get(item);
                    objects.put(item, new NPC(x, y, text.substring(0, text.indexOf('#')), text.substring(text.indexOf('#') + 1)));
                } else if (ENEMY.contains(item)) {
                    objects.put(item, new Enemy(x, y, objectVariables.get(item)));
                }
                x += blockSize;
            }
            x = 0;
            y += blockSize;
        }
    }

    public void draw(OrthographicCamera camera) {
        for (Interactive object : objects.values()) {
            boolean onScreen = (camera.position.x + camera.viewportWidth / 2 > object.x) && (object.x > camera.position.x - camera.viewportWidth)
                    && (camera.position.y + camera.viewportHeight / 2 > object.y) && (object.y > camera.position.y - camera.viewportHeight);
            if (onScreen)
                object.draw(game.batch, game.font, game.staticBatch);
        }
    }

    public void draw() {
        // Draw bg texture repeated to fill tile map.
        game.batch.draw(bg, 64, 64, bg.getWidth() * (tileMap[0].length - 2), bg.getHeight() * (tileMap.length - 2), 0, tileMap.length - 2, tileMap[0].length - 2, 0);
//        game.batch.draw(bgOver, 0, 0);
    }


    public void update(OrthographicCamera camera) {
        for (Interactive object : objects.values()) {
            boolean onScreen = (camera.position.x + camera.viewportWidth / 2 > object.x) && (object.x > camera.position.x - camera.viewportWidth)
                    && (camera.position.y + camera.viewportHeight / 2 > object.y) && (object.y > camera.position.y - camera.viewportHeight);
            if (onScreen)
                object.update();
        }
    }

    public void dispose() {
        objectVariables.clear();
        objects.clear();
        tileMap = null;
    }

    public void changeLevel(String name) {
        current = name;
        dispose();
        game.setScreen(new TransitionScreen(game, currentEngine));
        loadLevel();
    }

    public static String getTile(int row, int col){
        return tileMap[row][col];
    }

    public static HashMap<String, String> getObjectVariables() {
        return objectVariables;
    }

    public static Interactive getObject(String key) {
        return objects.get(key);
    }
}
