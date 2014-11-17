package com.yosefu.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Yosefu extends Game {
	public SpriteBatch batch;
    public SpriteBatch staticBatch;
    public BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();

        OrthographicCamera staticCamera = new OrthographicCamera();
        staticCamera.setToOrtho(false, 1280, 720);
        staticBatch = new SpriteBatch();
        staticBatch.setProjectionMatrix(staticCamera.combined);

        this.setScreen(new Engine(this));
        font = new BitmapFont();
	}

	@Override
	public void render () {
        super.render();
	}
}
