package com.yosefu.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Block extends Rectangle {
    protected Color color;
    public static final Color WALL = new Color(0.3f, 0.3f, 0.3f, 1);
    public static final Color DOOR = new Color(0.5f, 0.4f, 0.3f, 1);

    public Block(float x, float y, float width, float height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, width, height);
    }

    public void update(String[][] tileMap){};
}
