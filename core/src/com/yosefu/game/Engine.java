package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Engine implements Screen {
    private final Yosefu game;
    private OrthographicCamera camera;
    private Player player;
    private Level level;

    public Engine(Yosefu game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        player = new Player(game, this);
        level = new Level(game, this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.1f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(player.x + player.width / 2, player.y + 250, 0);
        camera.update();

        level.update(camera);
        player.processEvents();
        player.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        level.draw();
        player.draw(game);
        game.batch.end();

        level.draw(camera);
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
        player.dispose();
        game.dispose();
        level.dispose();
    }
}
