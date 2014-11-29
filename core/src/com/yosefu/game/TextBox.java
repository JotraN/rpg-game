package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Arrays;

public class TextBox {
    private final String text;
    private String[] lines;
    private ArrayList<String> linesDrawn = new ArrayList<String>();
    private ShapeRenderer shapeRenderer;
    private float x, y;
    private float width, height;
    private float border = 4;
    private boolean finished = false;
    private boolean nextBox = false;
    private Color textColor = new Color(0.2f, 0.3f, 0.2f, 1.0f);

    public TextBox(String text) {
        x = 450;
        y = 10;
        width = 400;
        height = 150;
        this.text = text;
        shapeRenderer = new ShapeRenderer();
        setup();
    }

    private void setup(){
        // TODO Split text based on length
        lines = text.split("\\\\n");
        linesDrawn.addAll(Arrays.asList(lines));
    }

    public void draw(BitmapFont font, SpriteBatch target){
        if(!finished) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.41f, 0.51f, 0.36f, 1.0f);
            shapeRenderer.rect(x, y, width, height);
            shapeRenderer.setColor(0.99f, 1.0f, 0.99f, 1.0f);
            shapeRenderer.rect(x + border, y + border, width - border*2, height - border*2);
            shapeRenderer.end();

            int textX = (int) (x + 8);
            int textY = (int) (y + 142);
            target.begin();
            int pos = 0;
            font.setScale(1);
            for(int i = 0; i < lines.length; i++){
                font.setColor(textColor);
                font.draw(target, lines[i], textX, textY);
                textY -= 15;
                boolean boxFull = textY <= y + border*2;
                if (boxFull) {
                    pos = i;
                    break;
                }
            }
            if(!nextBox) {
                linesDrawn = new ArrayList<String>(linesDrawn.subList(pos, linesDrawn.size()));
                nextBox = true;
                if(pos == 0)
                    linesDrawn.clear();
            }
            target.end();
        }
    }

    public void processEvents(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.O)){
            if(!finished) {
                if (linesDrawn.isEmpty())
                    finished = true;
                if (nextBox)
                    lines = linesDrawn.toArray(new String[linesDrawn.size()]);
                nextBox = false;
            }
        }
    }

    public void reset(){
        finished = false;
        lines = text.split("\\\\n");
        linesDrawn.clear();
        linesDrawn.addAll(Arrays.asList(lines));
    }

    public boolean isFinished(){
        return finished;
    }
}
