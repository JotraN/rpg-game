package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Door extends Interactive {
    private final Level level;
    private int playerPosX = 0;
    private int playerPosY = 0;

    public Door(float x, float y, float width, float height, Level level) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.level = level;

        Texture spriteSheet;
        spriteSheet = new Texture(Gdx.files.internal("door.png"));
        int rows = 1, columns = 1;
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / columns, spriteSheet.getHeight() / rows);
        currentFrame = frames[0][0];
    }

    @Override
    public void draw(SpriteBatch batch, BitmapFont font, SpriteBatch staticBatch) {
        batch.begin();
        batch.draw(currentFrame, x, y);
        batch.end();
    }

    @Override
    public void update() {
    }

    public void activate(String details){
        String[] doorDetails = details.split("\\s");
        playerPosX = Integer.parseInt(doorDetails[1]);
        playerPosY = Integer.parseInt(doorDetails[2]);
        level.changeLevel(doorDetails[0]);
    }

    public int getPlayerPosX(){
        return playerPosX;
    }

    public int getPlayerPosY(){
        return playerPosY;
    }
}
