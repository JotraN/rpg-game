package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;

public class TransitionScreen implements Screen {
    private final Yosefu game;
    private ShapeRenderer shapeRenderer;
    private Timer timer;
    private int time = 0;
    private Color color;
    private float increment = 1;
    private Screen nextScreen;

    public TransitionScreen(Yosefu game, Screen nextScreen) {
        this.game = game;
        this.nextScreen = nextScreen;
        shapeRenderer = new ShapeRenderer();
        color = new Color(0, 0.1f, 0, 1);
        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (time > 10) {
                    timer.stop();
                }
                time += increment;
            }
        }, 0.025f, 0.025f);
        timer.start();
    }

    @Override
    public void render(float delta) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        if(time > 10) {
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
