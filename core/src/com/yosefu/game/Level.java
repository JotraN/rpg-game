package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class Level {
    public static final String EMPTY = "0";
    public static final String WALL = "1";
    public static final String DOOR = "abcdefhijklmn";
    public static final String ENEMY = "opqrstuvwxyz";
    public static final String NPC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String current = "map_01";
    private String[][] tileMap;
    private Array<Block> blocks = new Array<Block>();
    private Array<NPC> npcs = new Array<NPC>();
    private Array<Enemy> enemies = new Array<Enemy>();
    private static HashMap<String, String> objectVariables = new HashMap<String, String>();
    private final Yosefu game;
    private final Engine currentEngine;
    private Texture bg;

    public Level(Yosefu game, Engine currentEngine) {
        this.game = game;
        this.currentEngine = currentEngine;
        loadLevel();
    }

    private void loadLevel() {
        FileHandle file = Gdx.files.internal("levels/" + current);
        String[] rows = file.readString().split("\\n");
        int start = 0;
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
//        bg = new Texture(Gdx.files.internal("levels/map_0" + current + ".png"));
        bg = new Texture(Gdx.files.internal("grasssprite.png"));
        bg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        setupLevel();
    }

    private void setupLevel() {
        float x = 0, y = 0;
        float blockSize = 64;
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
//                if (tileMap[i][j].equals(WALL))
//                    blocks.add(new Block(x, y, blockSize, blockSize, Block.WALL));
                if (DOOR.contains(tileMap[i][j]))
                    blocks.add(new Block(x, y, blockSize, blockSize, Block.DOOR));
                else if (NPC.contains(tileMap[i][j])) {
                    String text = objectVariables.get(tileMap[i][j]);
                    if(text == null)
                        text = "oldman.png#ERROR ERROR ERROR ERROR ERROR";
                    npcs.add(new NPC(x, y, text.substring(0, text.indexOf('#')), text.substring(text.indexOf('#')+1)));
                } else if(ENEMY.contains(tileMap[i][j])){
                    enemies.add(new Enemy(x, y, objectVariables.get(tileMap[i][j])));
                }
                x += blockSize;
            }
            x = 0;
            y += blockSize;
        }
    }

    public void draw(OrthographicCamera camera, ShapeRenderer shapeRenderer, OrthographicCamera staticCamera) {
        for (Block block : blocks) {
            boolean onScreen = (camera.position.x + camera.viewportWidth / 2 > block.x) && (block.x > camera.position.x - camera.viewportWidth)
                    && (camera.position.y + camera.viewportHeight / 2 > block.y) && (block.y > camera.position.y - camera.viewportHeight);
            if (onScreen)
                block.draw(shapeRenderer);
        }
        for (NPC npc : npcs) {
            boolean onScreen = (camera.position.x + camera.viewportWidth / 2 > npc.x) && (npc.x > camera.position.x - camera.viewportWidth)
                    && (camera.position.y + camera.viewportHeight / 2 > npc.y) && (npc.y > camera.position.y - camera.viewportHeight);
            if (onScreen)
                npc.draw(game.batch, game.font, game.staticBatch, staticCamera);
        }
        for (Enemy enemy : enemies) {
            boolean onScreen = (camera.position.x + camera.viewportWidth / 2 > enemy.x) && (enemy.x > camera.position.x - camera.viewportWidth)
                    && (camera.position.y + camera.viewportHeight / 2 > enemy.y) && (enemy.y > camera.position.y - camera.viewportHeight);
            if (onScreen)
                enemy.draw(game.batch);
        }
    }

    public void draw() {
        game.batch.draw(bg, 64, 64, bg.getWidth() * (tileMap[0].length - 2), bg.getHeight() * (tileMap.length - 2), 0, tileMap.length - 2, tileMap[0].length - 2, 0);
    }


    public void update(OrthographicCamera camera) {
        for (NPC npc : npcs) {
            boolean onScreen = (camera.position.x + camera.viewportWidth / 2 > npc.x) && (npc.x > camera.position.x - camera.viewportWidth)
                    && (camera.position.y + camera.viewportHeight / 2 > npc.y) && (npc.y > camera.position.y - camera.viewportHeight);
            if (onScreen)
                npc.update();
        }
    }

    public void dispose() {
        getObjectVariables().clear();
        blocks.clear();
        npcs.clear();
        enemies.clear();
        tileMap = null;
    }

    public String[][] getTileMap() {
        return tileMap;
    }

    public Array<NPC> getNpcs() {
        return npcs;
    }

    public Array<Enemy> getEnemies(){
        return enemies;
    }

    public void changeLevel(String name) {
        current = name;
        dispose();
        game.setScreen(new TransitionScreen(game, currentEngine));
        loadLevel();
    }

    public static HashMap<String, String> getObjectVariables(){
        return objectVariables;
    }
}
