package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class NPC extends Rectangle {
    private TextureRegion currentFrame;
    private TextBox textBox;
    private boolean talking = false;

    public NPC(float x, float y, String text) {
        this.x = x;
        this.y = y;
        this.width = 256;
        this.height = 256;
        textBox = new TextBox(text);

        Texture spriteSheet;
        spriteSheet = new Texture(Gdx.files.internal("playersheet.png"));
        int rows = 4, columns = 4;
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / columns, spriteSheet.getHeight() / rows);
        currentFrame = frames[1][0];
    }

    public void draw(SpriteBatch batch, BitmapFont font, SpriteBatch staticBatch, OrthographicCamera camera) {
        batch.begin();
        batch.draw(currentFrame, x, y);
        batch.end();

        if(talking)
            textBox.draw(font, staticBatch, camera);
    }

    public void update(){
        if(talking)
             textBox.processEvents();
        if(textBox.isFinished())
            talking = false;
    }

    public void talk(){
        talking = true;
        textBox.reset();
    }

    public boolean talking(){
        return talking;
    }
}
