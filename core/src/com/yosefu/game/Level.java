package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class Level {
    public static final String WALL = "1";
    public static final String DOOR = "2";
    public static final int max = 1;
    private static int current = 1;
    private String[][] tileMap;
    private Array<Block> blocks = new Array<Block>();
    private final Yosefu game;
    private final Engine currentEngine;

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
        setupLevel();
    }

    private void setupLevel() {
        float x = 0, y = 0;
        float blockSize = 50;
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                if (tileMap[i][j].equals(WALL))
                    blocks.add(new Block(x, y, blockSize, blockSize, Block.WALL));
                else if (tileMap[i][j].equals(DOOR))
                    blocks.add(new Block(x, y, blockSize, blockSize, Block.DOOR));
                x += blockSize;
            }
            x = 0;
            y += blockSize;
        }
    }

    public void draw(OrthographicCamera camera, ShapeRenderer shapeRenderer) {
        for (Block block : blocks) {
            boolean onScreen = (camera.position.x + camera.viewportWidth / 2 > block.x) && (block.x > camera.position.x - camera.viewportWidth / 1.5)
                    && (camera.position.y + camera.viewportHeight / 2 > block.y) && (block.y > camera.position.y - camera.viewportHeight / 1.5);
            if (onScreen)
                block.draw(shapeRenderer);
        }
    }

    public void update() {
    }

    public void dispose() {
        blocks.clear();
        tileMap = null;
    }

    public String[][] getTileMap() {
        return tileMap;
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
