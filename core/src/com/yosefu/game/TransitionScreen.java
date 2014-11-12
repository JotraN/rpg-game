package com.yosefu.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;

public class TransitionScreen implements Screen {
    private final Yosefu game;
    private ShapeRenderer shapeRenderer;
    private Timer timer;
    private float color = 0.5f;
    private float increment = -0.1f;
    private Screen nextScreen;

    public TransitionScreen(Yosefu game, Screen nextScreen) {
        this.game = game;
        this.nextScreen = nextScreen;
        shapeRenderer = new ShapeRenderer();
        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (color > 0.5f) {
                    timer.stop();
                }
                if (color < 0) increment = -increment;
                color += increment;
            }
        }, 0.025f, 0.025f);
        timer.start();
    }

    @Override
    public void render(float delta) {
//        shapeRenderer.setProjectionMatrix(game.staticCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color, color, color, 1.0f);
//        shapeRenderer.rect(0, 0, game.staticCamera.viewportWidth, game.staticCamera.viewportHeight);
        shapeRenderer.end();
        if(color > 0.5f) {
            this.dispose();
            game.setScreen(nextScreen);
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
        shapeRenderer.dispose();
        timer.clear();
    }
}
