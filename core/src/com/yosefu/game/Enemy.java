package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Enemy extends Rectangle{
    private TextureRegion currentFrame;
    private boolean alive = true;
    private Stats stats;
    private String levelAction = null;

    public Enemy(float x, float y, String variables){
        stats = new Stats(10, 10, 0.01f);
        this.x = x;
        this.y = y;
        this.width = 64;
        this.height = 64;
        Texture spriteSheet;
        String[] enemyVariables = variables.split(" ");
        if(variables.contains("#")) {
            enemyVariables = variables.substring(0, variables.indexOf('#')).split(" ");
            levelAction = variables.substring(variables.indexOf('#') + 1);
        }

        spriteSheet = new Texture(Gdx.files.internal("enemy/" + enemyVariables[0]));
        int rows = 1, columns = 1;
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / columns, spriteSheet.getHeight() / rows);
        // Center texture.
        this.x += (this.width - spriteSheet.getWidth()/columns)/2;
        currentFrame = frames[0][0];
    }

    public void draw(SpriteBatch batch) {
        if(alive) {
            batch.begin();
            batch.draw(currentFrame, x, y);
            batch.end();
        }
    }

    public Stats getStats(){
        return stats;
    }

    public boolean isAlive(){
        return alive;
    }

    public void kill(){
        alive = false;
        if(levelAction != null) {
            Level.getObjectVariables().remove(levelAction.substring(0, 1));
            Level.getObjectVariables().put(levelAction.substring(0, 1), levelAction.substring(2));
        }
    }

    public TextureRegion getTexture(){
        return currentFrame;
    }
}
