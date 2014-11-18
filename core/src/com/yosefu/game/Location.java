package com.yosefu.game;

import com.badlogic.gdx.math.Rectangle;

public class Location {
    public int top;
    public int bottom;
    public int left;
    public int right;
    private int blockWidth;

    public Location(Rectangle rect, int blockWidth) {
        bottom = (int) Math.floor(rect.y / blockWidth);
        top = (int) Math.floor((rect.y + rect.height - 1) / blockWidth);
        left = (int) Math.floor(rect.x / blockWidth);
        right = (int) Math.floor((rect.x + rect.width - 1) / blockWidth);
        this.blockWidth = blockWidth;
    }

    public void update(Rectangle rect) {
        bottom = (int) Math.floor(rect.y / blockWidth);
        top = (int) Math.floor((rect.y + rect.height - 1) / blockWidth);
        left = (int) Math.floor(rect.x / blockWidth);
        right = (int) Math.floor((rect.x + rect.width - 1) / blockWidth);
    }
}
