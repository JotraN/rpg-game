package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

public class MainMenu implements Screen {
    private final Yosefu game;
    private Texture bg;
    private TextureRegion currentFrame;
    private Animation waveAnimation;
    private float stateTime = 0;
    private Texture spriteSheet;

    public MainMenu(Yosefu game) {
        this.game = game;
        bg = new Texture(Gdx.files.internal("mainmenu.png"));
        spriteSheet = new Texture(Gdx.files.internal("watersprite.png"));
        int rows = 1, columns = 4;
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / columns, spriteSheet.getHeight() / rows);
        waveAnimation = new Animation(0.07f, frames[0]);
        currentFrame = waveAnimation.getKeyFrame(0, true);
        spriteSheet.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    public void render(float delta) {
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = waveAnimation.getKeyFrame(stateTime, true);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.staticBatch.begin();
        game.staticBatch.draw(bg, 0, 0);
        game.font.setColor(0.0f, 0.1f, 0.0f, 1.0f);
        game.font.setScale(1.0f);
        game.font.draw(game.staticBatch, "Press ENTER to start.", 500, 280);
        TiledDrawable tile = new TiledDrawable(currentFrame);
        tile.draw(game.staticBatch, 0, 0, 1280, 64 * 4);
        game.staticBatch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new TransitionScreen(game, new Engine(game)));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        spriteSheet.dispose();
    }
}
