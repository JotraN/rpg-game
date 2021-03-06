package com.yosefu.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
        height = 100;
        this.text = text;
        shapeRenderer = new ShapeRenderer();
        setup();
    }

    private void setup() {
        splitText();
        linesDrawn.addAll(Arrays.asList(lines));
    }

    public void draw(BitmapFont font, SpriteBatch target) {
        if (!finished) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.41f, 0.51f, 0.36f, 1.0f);
            shapeRenderer.rect(x, y, width, height);
            shapeRenderer.setColor(0.99f, 1.0f, 0.99f, 1.0f);
            shapeRenderer.rect(x + border, y + border, width - border * 2, height - border * 2);
            shapeRenderer.end();

            int padding = 14;
            int textX = (int) (x + padding);
            int textY = (int) (y + height - padding);
            target.begin();
            int pos = 0;
            font.setScale(1);
            for (int i = 0; i < lines.length; i++) {
                font.setColor(textColor);
                font.draw(target, lines[i], textX, textY);
                textY -= 20;
                boolean boxFull = textY <= y + padding;
                if (boxFull) {
                    pos = ++i;
                    break;
                }
            }
            if (!nextBox) {
                linesDrawn = new ArrayList<String>(linesDrawn.subList(pos, linesDrawn.size()));
                nextBox = true;
                if (pos == 0)
                    linesDrawn.clear();
            }
            target.end();
        }
    }

    public void processEvents() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            if (!finished) {
                if (linesDrawn.isEmpty())
                    finished = true;
                if (nextBox)
                    lines = linesDrawn.toArray(new String[linesDrawn.size()]);
                nextBox = false;
            }
        }
    }

    public void reset() {
        finished = false;
        splitText();
        linesDrawn.clear();
        linesDrawn.addAll(Arrays.asList(lines));
    }

    private void splitText(){
        String tmp = "";
        String[] words = text.split(" ");
        int lineLength = 27, currLength = lineLength;
        for(int i = 0; i < words.length; i++){
            if(tmp.length() + words[i].length() >= currLength) {
                tmp += "\\\\n";
                currLength += lineLength;
            }
            tmp += words[i];
            tmp += ' ';
        }
        lines = tmp.split("\\\\n");
    }

    public boolean isFinished() {
        return finished;
    }
}
