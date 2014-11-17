package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class Level {
    public static final String EMPTY = "0";
    public static final String WALL = "1";
    public static final String DOOR = "2";
    public static final String NPC = "3";
    public static final int max = 1;
    private static int current = 1;
    private String[][] tileMap;
    private Array<Block> blocks = new Array<Block>();
    private Array<NPC> npcs = new Array<NPC>();
    private final Yosefu game;
    private final Engine currentEngine;
    private Texture bg;

    public Level(Yosefu game, Engine currentEngine) {
        this.game = game;
        this.currentEngine = currentEngine;
        loadLevel();
    }

    private void loadLevel() {
        FileHandle file = Gdx.files.internal("levels/map_0" + current);
        String[] rows = file.readString().split("\\n");
        tileMap = new String[rows.length][rows[0].split(", ").length];
        for (int i = 0, j = 0; i < tileMap.length; i++, j++) {
            tileMap[i] = rows[j].split(", ");
        }
        bg = new Texture(Gdx.files.internal("levels/map_0" + current + ".png"));
        setupLevel();
    }

    private void setupLevel() {
        float x = 0, y = 0;
        float blockSize = 256;
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                if (tileMap[i][j].equals(WALL))
                    blocks.add(new Block(x, y, blockSize, blockSize, Block.WALL));
                else if (tileMap[i][j].equals(DOOR))
                    blocks.add(new Block(x, y, blockSize, blockSize, Block.DOOR));
                else if(tileMap[i][j].equals(NPC))
                    npcs.add(new NPC(x + 40, y,
                            "tesst\ndasds\nasas\nqdwqad\ndwdwafaf" +
                                    "\ndwdawf\nfawfff\nasadasadwa" +
                                    "\ndwdawf\nfawfff\nasadasadwa" +
                                    "\ndwdawf\nfawfff\nasadasadwa" +
                                    "\ndwdawf\nfawfff\nasadasadwa"
                            ));
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
        for(NPC npc : npcs){
            boolean onScreen = (camera.position.x + camera.viewportWidth / 2 > npc.x) && (npc.x > camera.position.x - camera.viewportWidth)
                    && (camera.position.y + camera.viewportHeight / 2 > npc.y) && (npc.y > camera.position.y - camera.viewportHeight);
            if (onScreen)
                npc.draw(game.batch, game.font, game.staticBatch, staticCamera);
        }
    }

    public void draw(){
        game.batch.draw(bg, 0, 0);
    }


    public void update(OrthographicCamera camera) {
        for(NPC npc : npcs){
            boolean onScreen = (camera.position.x + camera.viewportWidth / 2 > npc.x) && (npc.x > camera.position.x - camera.viewportWidth)
                    && (camera.position.y + camera.viewportHeight / 2 > npc.y) && (npc.y > camera.position.y - camera.viewportHeight);
            if (onScreen)
                npc.update();
        }
    }

    public void dispose() {
        blocks.clear();
        tileMap = null;
    }

    public String[][] getTileMap() {
        return tileMap;
    }

    public Array<NPC> getNpcs(){
        return npcs;
    }

    public void changeLevel() {
        if (current >= max) {
            return;
        }
        current++;
        dispose();
        loadLevel();
        game.setScreen(new TransitionScreen(game, currentEngine));
    }
}
