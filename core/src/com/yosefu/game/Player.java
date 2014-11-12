package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Player extends Rectangle {
    private float velX = 0, velY = 0;
    private final float SPEED = 5;
    private final float JUMP = 10;
    private final float GRAVITY = 0.75f;
    private boolean inAir = false;
    private Vector3 touchPos = new Vector3();
    private Texture spriteSheet;
    private TextureRegion currentFrame;
    private final Animation STANDING;
    private final Animation WALKING;
    private final Animation JUMPING;
    private float stateTime;

    public Player() {
        x = 50;
        y = 50;
        width = 50;
        height = 50;

        spriteSheet = new Texture(Gdx.files.internal("playersheet.png"));
        int rows = 3, columns = 5;
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / columns, spriteSheet.getHeight() / rows);
        STANDING = new Animation(0.05f, frames[0]);
        WALKING = new Animation(0.05f, frames[1]);
        JUMPING = new Animation(0.05f, frames[2]);
        stateTime = 0;
    }

    public void draw(Yosefu game) {
        game.batch.enableBlending();
        game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.batch.draw(currentFrame, x, y);
    }

    public void processEvents() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) velX = -SPEED;
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) velX = SPEED;
        else velX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) velY = SPEED;
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) velY = -SPEED;
        else velY = 0;
    }

    public void update(Level level) {
        updateTextureRegion();

        x += velX;
        String[][] tileMap = level.getTileMap();
        int blockWidth = 50;
        int mapWidth = tileMap[0].length * blockWidth;
        if (x < 0 || this.x > mapWidth - this.width)
            x -= velX;
        int col = (int) Math.floor((x + this.width - 1) / blockWidth);
        if (velX < 0)
            col = (int) Math.floor(x / blockWidth);
        int rowBottom = (int) Math.floor(y / blockWidth);
        int rowTop = (int) Math.floor((y + this.height - 1) / blockWidth);
        if (tileMap[rowBottom][col].equals(Level.WALL) || tileMap[rowTop][col].equals(Level.WALL))
            x -= velX;
        else if (tileMap[rowBottom][col].equals(Level.DOOR) || tileMap[rowTop][col].equals(Level.DOOR)) {
            x = 0;
            level.changeLevel();
        }

        y += velY;
        inAir = true;
        int row = (int) Math.floor((y + this.height - 1) / 50);
        int leftCol = (int) Math.floor(x / 50);
        int rightCol = (int) Math.floor((x + this.width - 1) / 50);
        if (velY < 0) row = (int) Math.floor(y / 50);
        if (tileMap[row][leftCol].equals(Level.WALL) || tileMap[row][rightCol].equals(Level.WALL)) {
            y -= velY;
            inAir = false;
        } else if (tileMap[row][leftCol].equals(Level.DOOR) || tileMap[row][rightCol].equals(Level.DOOR)) {
            x = 0;
            level.changeLevel();
        }
    }

    private void updateTextureRegion() {
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = WALKING.getKeyFrame(stateTime, true);
        if (velY > 0) currentFrame = JUMPING.getKeyFrame(stateTime, true);
        else if (velX > 0) {
            if (currentFrame.isFlipX()) currentFrame.flip(true, false);
        } else if (velX < 0) {
            if (!currentFrame.isFlipX()) currentFrame.flip(true, false);
        } else currentFrame = STANDING.getKeyFrame(stateTime, true);
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
