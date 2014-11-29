package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Engine implements Screen {
    private final Yosefu game;
    private OrthographicCamera camera, staticCamera;
    private Player player;
    private Level level;
    private ShapeRenderer shapeRenderer;

    public Engine(Yosefu game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        staticCamera = new OrthographicCamera();
        staticCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        player = new Player(game, this);
        level = new Level(game, this);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)) {
//            game.setScreen(new Battle(new Player(), new Enemy(0, 0), game, this));
//            Gdx.app.exit();
        }

        Gdx.gl.glClearColor(0.0f, 0.1f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(player.x + player.width/2, player.y + 250, 0);
        camera.update();

        level.update(camera);
        player.processEvents();
        player.update(level);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        level.draw();
        player.draw(game);
        game.batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        level.draw(camera, shapeRenderer, staticCamera);
        shapeRenderer.end();
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
        shapeRenderer.dispose();
    }
}
