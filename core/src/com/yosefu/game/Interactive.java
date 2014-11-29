package com.yosefu.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Interactive extends Rectangle{
    protected TextureRegion currentFrame;
    public abstract void draw(SpriteBatch batch, BitmapFont font, SpriteBatch staticBatch);
    public abstract void update();
}
