package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Player extends Rectangle {
    private float velX = 0, velY = 0;
    private final float SPEED = 10;
    private Texture spriteSheet;
    private TextureRegion currentFrame;
    private final Animation STANDING;
    private final Animation LEFTRIGHT;
    private final Animation UP;
    private final Animation DOWN;
    private float stateTime;
    private boolean checkInteraction = false;
    private boolean interacting = false;

    public Player() {
        x = 256;
        y = 256;
        width = 176;
        height = 240;

        spriteSheet = new Texture(Gdx.files.internal("playersheet.png"));
        int rows = 4, columns = 4;
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / columns, spriteSheet.getHeight() / rows);
        LEFTRIGHT = new Animation(0.05f, frames[0]);
        STANDING = new Animation(0.1f, frames[1]);
        UP = new Animation(0.05f, frames[2]);
        DOWN = new Animation(0.05f, frames[3]);
        stateTime = 0;
    }

    public void draw(Yosefu game) {
        game.batch.enableBlending();
        game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.batch.draw(currentFrame, x, y);
    }

    public void processEvents() {
        if (interacting) return;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
            velX = -SPEED;
        else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            velX = SPEED;
        else velX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))
            velY = SPEED;
        else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed((Input.Keys.DOWN)))
            velY = -SPEED;
        else velY = 0;
        if (Gdx.input.isKeyJustPressed(Input.Keys.O))
            checkInteraction = true;
    }

    public void update(Level level) {
        updateTextureRegion();
        moveAndCollide(level);
        interact(level);
    }

    private void updateTextureRegion() {
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = LEFTRIGHT.getKeyFrame(stateTime, true);
        if (velY > 0) currentFrame = UP.getKeyFrame(stateTime, true);
        else if (velY < 0) currentFrame = DOWN.getKeyFrame(stateTime, true);
        else if (velX > 0) {
            if (currentFrame.isFlipX())
                currentFrame.flip(true, false);
        } else if (velX < 0) {
            if (!currentFrame.isFlipX())
                currentFrame.flip(true, false);
        } else currentFrame = STANDING.getKeyFrame(stateTime, true);
    }

    public void dispose() {
        spriteSheet.dispose();
    }

    private void moveAndCollide(Level level) {
        x += velX;
        String[][] tileMap = level.getTileMap();
        int blockWidth = 256;
        int mapWidth = tileMap[0].length * blockWidth;
        if (x < 0 || x > mapWidth - width)
            x -= velX;
        int col = (int) Math.floor((x + this.width - 1) / blockWidth);
        if (velX < 0)
            col = (int) Math.floor(x / blockWidth);
        int rowBottom = (int) Math.floor(y / blockWidth);
        int rowTop = (int) Math.floor((y + this.height - 1) / blockWidth);
        if (!tileMap[rowBottom][col].equals(Level.EMPTY) || !tileMap[rowTop][col].equals(Level.EMPTY)) {
            if (tileMap[rowBottom][col].equals(Level.WALL) || tileMap[rowTop][col].equals(Level.WALL))
                x -= velX;
            else if (tileMap[rowBottom][col].equals(Level.NPC) || tileMap[rowTop][col].equals(Level.NPC)) {
                x -= velX;
            } else if (tileMap[rowBottom][col].equals(Level.DOOR) || tileMap[rowTop][col].equals(Level.DOOR)) {
                x = 256;
                y = 256;
                level.changeLevel();
            }
        }

        y += velY;
        int row = (int) Math.floor((y + this.height - 1) / blockWidth);
        int leftCol = (int) Math.floor(x / blockWidth);
        int rightCol = (int) Math.floor((x + this.width - 1) / blockWidth);
        if (velY < 0) row = (int) Math.floor(y / blockWidth);
        if (!tileMap[row][leftCol].equals(Level.EMPTY) || !tileMap[row][rightCol].equals(Level.EMPTY)) {
            if (tileMap[row][leftCol].equals(Level.WALL) || tileMap[row][rightCol].equals(Level.WALL))
                y -= velY;
            else if (tileMap[row][leftCol].equals(Level.NPC) || tileMap[row][rightCol].equals(Level.NPC))
                y -= velY;
            else if (tileMap[row][leftCol].equals(Level.DOOR) || tileMap[row][rightCol].equals(Level.DOOR)) {
                x = 256;
                y = 256;
                level.changeLevel();
            }
        }
    }

    private void interact(Level level) {
        if (checkInteraction || interacting) {
            Array<NPC> tmp = level.getNpcs();
            int blockWidth = 256;
            // TODO collision checking still needs some work to accommodate player width and height
            int row = (int) Math.floor((y + this.height - 1) / blockWidth);
            int col = (int) Math.floor(x / blockWidth);
            for (NPC npc : tmp) {
                int npcRow = (int) Math.floor(npc.y / blockWidth);
                int npcCol = (int) Math.floor(npc.x / blockWidth);
                if ((row == npcRow - 1 && col == npcCol) || (row == npcRow + 1 && col == npcCol)
                        || (row == npcRow && col == npcCol - 1)
                        || (row == npcRow && col == npcCol + 1)) {
                    if (interacting) {
                        // Update interaction status.
                        interacting = npc.talking();
                        return;
                    } else {
                        // Start interacting.
                        npc.talk();
                        interacting = npc.talking();
                    }
                    break;
                }
                interacting = false;
            }
            checkInteraction = false;
        }
    }
}
