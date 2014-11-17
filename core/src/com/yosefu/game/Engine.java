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
    private TextBox textBox;

    public Engine(Yosefu game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        camera.setToOrtho(false, 1280, 720);
        staticCamera = new OrthographicCamera();
        staticCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        player = new Player();
        level = new Level(game, this);
        shapeRenderer = new ShapeRenderer();
        textBox = new TextBox(
                "tesst\ndasds\nasas\nqdwqad\ndwdwafaf" +
                "\ndwdawf\nfawfff\nasadasadwa" +
                "\ndwdawf\nfawfff\nasadasadwa" +
                "\ndwdawf\nfawfff\nasadasadwa" +
                "\ndwdawf\nfawfff\nasadasadwa" +
                "\ndwdawf\nfawfff\nasadasadwa" +
                "\ndwdawf\nfawfff\nasadasadwa" +
                "\ndwdawf\nfawfff\nasadasadwa"
        );
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
//            Gdx.app.exit();
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(player.x + player.width/2, player.y + 250, 0);
        camera.update();

        level.update(camera);
        player.processEvents();
        player.update(level);
//        textBox.processEvents();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        level.draw();
        player.draw(game);
        game.batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        level.draw(camera, shapeRenderer, staticCamera);
        shapeRenderer.end();

//        textBox.draw(game.font, game.batch, staticCamera);
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
