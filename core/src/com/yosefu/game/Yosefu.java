package com.yosefu.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

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

        font = new BitmapFont();
        FileHandle fontFile = Gdx.files.internal("early_gameboy.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;
        font = generator.generateFont(parameter);
        generator.dispose();

        this.setScreen(new Engine(this));
	}

	@Override
	public void render () {
        super.render();
	}
}
