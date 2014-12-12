package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Battle implements Screen {
    private final Yosefu game;
    private final Screen screen;
    private final Player player;
    private final Enemy enemy;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Texture bg;
    private TextureRegion playerTexture, enemyTexture;
    private Color textColor = new Color(0.2f, 0.3f, 0.2f, 1.0f);
    private Color highlightColor = new Color(0.6f, 0.7f, 0.6f, 1.0f);
    private String[] textOptions = {
            "attack", "defend", "talk", "run"
    };
    private int selection = 0;
    private boolean attacking = false, defending = false, running = false;
    private boolean initialAnimation = false;
    private int groundHeight = 340, groundEdge = 250, groundPlayerPosX = 300;
    private int playerX = groundPlayerPosX, playerY = groundHeight, enemyX = 900, enemyY = 340;
    private int speed = 5;

    public Battle(Player player, Enemy enemy, Yosefu game, Screen screen) {
        this.player = player;
        this.enemy = enemy;
        this.game = game;
        this.screen = screen;
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        bg = new Texture(Gdx.files.internal("battlebg.png"));
        playerTexture = player.getTexture();
        enemyTexture = enemy.getTexture();
    }

    public void render(float delta) {
        // TODO Game over.
        if (player.getStats().health <= 0) game.setScreen(new TransitionScreen(game, new MainMenu(game)));
        if (enemy.getStats().health <= 0) {
            enemy.kill();
            game.setScreen(new TransitionScreen(game, screen));
        }
        draw();
        if(attacking)
            animateAttack();
        else if(defending)
            animateDefend();
        else if(running)
            animateRun();
        else
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
        game.batch.draw(playerTexture, playerX, playerY);
        game.font.draw(game.batch, "hp: " + player.getStats().health, 300, groundHeight + player.height + 25);
        game.batch.draw(enemyTexture, enemyX, enemyY);
        game.font.draw(game.batch, "hp: " + enemy.getStats().health, 900, groundHeight + enemy.height + 25);
        game.batch.end();

        int x = 50;
        int y = 40;
        int width = 150;
        int height = 85;
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
        for (int i = 0; i < textOptions.length; i++) {
            game.font.setColor(textColor);
            if (selection == i)
                game.font.setColor(highlightColor);
            game.font.draw(game.batch, textOptions[i], x + padding, y - padding);
            y -= padding;
        }
        game.batch.end();
    }

    private void processEvents() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (selection == 0) return;
            selection--;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed((Input.Keys.DOWN))) {
            if (selection == textOptions.length - 1) return;
            selection++;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O))
            act();
    }

    private void act() {
        switch (selection) {
            case 0:
                attacking = true;
                initialAnimation = true;
                break;
            case 1:
                defending = true;
                initialAnimation = true;
                break;
            case 2:
                break;
            case 3:
                running = true;
                initialAnimation = true;
                playerTexture.flip(true, false);
                break;

            default:
                break;
        }
    }

    private void animateAttack(){
        int maxHeight = groundHeight + 20;
        if(initialAnimation) {
            playerY += speed;
            if (playerY > maxHeight)
                speed = -speed;
            if (playerY < groundHeight) {
                playerY = groundHeight;
                initialAnimation = false;
                speed = -speed;
                attack(enemy.getStats(), player.getStats().atk);
            }
        } else{
            // Animate enemy attack.
            enemyY += speed;
            if (enemyY > maxHeight)
                speed = -speed;
            if (enemyY < groundHeight) {
                enemyY = groundHeight;
                attacking = false;
                speed = -speed;
                attack(player.getStats(), enemy.getStats().atk);
            }
        }
    }

    private void animateDefend(){
        int maxHeight = groundHeight + 20;
        if(initialAnimation) {
            // Animate enemy attack.
            enemyY += speed;
            if (enemyY > maxHeight)
                speed = -speed;
            if (enemyY < groundHeight) {
                enemyY = groundHeight;
                speed = -speed;
                initialAnimation = false;
            }
        } else{
            playerX -= speed;
            if (playerX <= groundEdge)
                speed = -speed;
            if(playerX == groundPlayerPosX) {
                defending = false;
                speed = -speed;
                defend(player.getStats(), enemy.getStats().atk, player.getStats().def);
            }
        }
    }

    private void animateRun(){
        if(initialAnimation) {
            playerX -= speed;
            if (playerX <= groundEdge)
                initialAnimation = false;
        } else{
            enemy.getStats().health = enemy.getStats().maxHealth;
            player.x -= player.getVelX();
            player.y -= player.getVelY();
            game.setScreen(new TransitionScreen(game, screen));
        }
    }

    private void attack(Stats target, int atk){
        target.health -= Math.ceil(Math.random() * atk) + atk;
    }

    private void defend(Stats target, int atk, int def){
        target.health -= Math.max(0, atk - (Math.random() * def));
    }

    public void resize(int width, int height) {
    }

    public void show() {

    }

    public void hide() {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
