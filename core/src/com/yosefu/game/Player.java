package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Player extends Rectangle {
    private final Yosefu game;
    private final Engine engine;
    private int velX = 0, velY = 0;
    private final int SPEED = 6;
    private final Animation LEFTRIGHT;
    private final Animation UP;
    private final Animation DOWN;
    private float stateTime;
    private boolean collided = false;
    private boolean checkInteraction = false;
    private boolean interacting = false;
    private Texture spriteSheet;
    private TextureRegion currentFrame;
    private Location playerLocation;
    private Stats stats;

    public Player(Yosefu game, Engine engine) {
        this.game = game;
        this.engine = engine;
        x = 320;
        y = 64;
        width = 46;
        height = 62;

        spriteSheet = new Texture(Gdx.files.internal("playersheet.png"));
        int rows = 3, columns = 4;
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / columns, spriteSheet.getHeight() / rows);
        LEFTRIGHT = new Animation(0.1f, frames[0]);
        DOWN = new Animation(0.1f, frames[1]);
        UP = new Animation(0.1f, frames[2]);
        // Set default appearance.
        currentFrame = UP.getKeyFrame(0);
        stateTime = 0;
        playerLocation = new Location(this, 64);
        stats = new Stats(10, 10, 3);
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

    public void update() {
        updateTextureRegion();
        moveAndCollide();
        interact();
    }

    private void updateTextureRegion() {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion original;
        if (currentFrame != null)
            original = currentFrame;
        else
            original = DOWN.getKeyFrame(0);
        currentFrame = LEFTRIGHT.getKeyFrame(stateTime, true);
        if (velY > 0) currentFrame = UP.getKeyFrame(stateTime, true);
        else if (velY < 0) currentFrame = DOWN.getKeyFrame(stateTime, true);
        else if (velX > 0) {
            if (currentFrame.isFlipX())
                currentFrame.flip(true, false);
        } else if (velX < 0) {
            if (!currentFrame.isFlipX())
                currentFrame.flip(true, false);
        } else currentFrame = original;
    }

    public void dispose() {
        spriteSheet.dispose();
    }

    private void moveAndCollide() {
        x += velX;
        playerLocation.update(this);
        String tileBottom = Level.getTile(playerLocation.bottom, playerLocation.right);
        String tileTop = Level.getTile(playerLocation.top, playerLocation.right);
        if (velX < 0) {
            tileBottom = Level.getTile(playerLocation.bottom, playerLocation.left);
            tileTop = Level.getTile(playerLocation.top, playerLocation.left);
        }
        collide(tileBottom, velX, 0);
        if(!collided) collide(tileTop, velX, 0);

        y += velY;
        playerLocation.update(this);
        String tileRight = Level.getTile(playerLocation.top, playerLocation.right);
        String tileLeft = Level.getTile(playerLocation.top, playerLocation.left);
        if (velY < 0) {
            tileRight = Level.getTile(playerLocation.bottom, playerLocation.right);
            tileLeft = Level.getTile(playerLocation.bottom, playerLocation.left);
        }
        collide(tileRight, 0, velY);
        if(!collided) collide(tileLeft, 0, velY);
    }

    private void collide(String tile, int moveX, int moveY) {
        if (tile.equals(Level.EMPTY)){ collided = false; return;}
        if (tile.equals(Level.WALL)){ x -= moveX; y -= moveY;}
        else if (Level.NPC.contains(tile)){ x -= moveX; y -= moveY;}
        else if (Level.ENEMY.contains(tile)) {
            Enemy enemy = (Enemy) Level.getObject(tile);
            if (enemy.isAlive())
                game.setScreen(new TransitionScreen(game, new Battle(this, enemy, game, engine)));
        } else if (Level.DOOR.contains(tile)) {
            Door door = (Door) Level.getObject(tile);
            door.activate(Level.getObjectVariables().get(tile));
            x = door.getPlayerPosX();
            y = door.getPlayerPosY();
        }
        collided = true;
    }

    private void interact() {
        if (checkInteraction || interacting) {
            playerLocation.update(this);
            String tileRight = Level.getTile(playerLocation.bottom+1, playerLocation.right);
            String tileLeft = Level.getTile(playerLocation.bottom+1, playerLocation.left);
            String npcCode = null;
            if(Level.NPC.contains(tileRight)) npcCode = tileRight;
            else if(Level.NPC.contains(tileLeft)) npcCode = tileLeft;
            if (npcCode != null) {
                currentFrame = UP.getKeyFrame(0, true);
                NPC npc = (NPC) Level.getObject(npcCode);
                if (interacting) {
                    // Update interaction status.
                    interacting = npc.talking();
                } else {
                    // Start interacting.
                    velX = 0;
                    velY = 0;
                    npc.talk();
                    interacting = npc.talking();
                }
            }
            checkInteraction = false;
        }
    }

    public Stats getStats() {
        return stats;
    }

    public TextureRegion getTexture() {
        // Flip to face right if facing left.
        if (LEFTRIGHT.getKeyFrame(0).isFlipX())
            LEFTRIGHT.getKeyFrame(0).flip(true, false);
        return LEFTRIGHT.getKeyFrame(0);
    }

    public int getVelX() {
        return velX;
    }

    public int getVelY() {
        return velY;
    }
}
