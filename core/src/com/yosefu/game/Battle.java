package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Battle implements Screen {
    private final Yosefu game;
    private final Screen screen;
    private final Player player;
    private final Enemy enemy;
//    private Array<Enemy> enemies;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Texture bg;
    private Color textColor = new Color(0.2f, 0.3f, 0.2f, 1.0f);
    private Color highlightColor = new Color(0.6f, 0.7f, 0.6f, 1.0f);
    private String[] textOptions = {
            "attack", "breathe", "talk", "run"
    };
    private int current = 0;

    public Battle(Player player, Enemy enemy, Yosefu game, Screen screen) {
        this.player = player;
        this.enemy = enemy;
        this.game = game;
        this.screen = screen;
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        bg = new Texture(Gdx.files.internal("battlebg.png"));
    }

    @Override
    public void render(float delta) {
        if(player.getStats().getHealth() < 0) return;
        if(enemy.getStats().getHealth() < 0) {
            enemy.kill();
            game.setScreen(new TransitionScreen(game, screen));
        }
        draw();
        processEvents();
    }

    private void draw() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.setColor(highlightColor);
        game.font.setScale(1);
        game.batch.draw(bg, 0, 0);
        game.batch.draw(player.getTexture(), 300, 340);
        game.font.draw(game.batch, "hp: " + player.getStats().getHealth(), 300, 340 + player.height + 25);
        game.batch.draw(enemy.getTexture(), 900, 340);
        game.font.draw(game.batch, "hp: " + enemy.getStats().getHealth(), 900, 340 + enemy.height + 25);
        game.batch.end();

        float x = 50;
        float y = 40;
        float width = 150;
        float height = 85;
        int border = 4;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Draw player box.
        shapeRenderer.setColor(0.41f, 0.51f, 0.36f, 1.0f);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.setColor(0.99f, 1.0f, 0.99f, 1.0f);
        shapeRenderer.rect(x + border, y + border, width - border * 2, height - border * 2);
        shapeRenderer.end();

        game.batch.begin();
        // Draw text options.
        int padding = 15;
        y += height;
        for(int i = 0; i < textOptions.length; i++){
            game.font.setColor(textColor);
            if(current == i)
                game.font.setColor(highlightColor);
            game.font.draw(game.batch, textOptions[i], x + padding, y - padding);
            y -= padding;
        }
//        game.font.draw(game.batch, "pp: " + player.getStats().getPp(), xLoc, yLoc * 2);
        game.batch.end();
    }

    private void processEvents() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (current == 0) return;
            current--;
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed((Input.Keys.DOWN))) {
            if(current == textOptions.length - 1) return;
            current++;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O))
            act();
    }

    private void act(){
        switch (current){
            case 0:
                enemy.getStats().setHealth(enemy.getStats().getHealth() - player.getStats().getAtk());
                player.getStats().setHealth(player.getStats().getHealth() - enemy.getStats().getAtk());
                break;
            default:
                break;
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
    }
}
