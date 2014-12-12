package com.yosefu.game;

public class Stats {
    public int health = 10;
    public int maxHealth = 10;
    public int def = 10;
    public int atk = 10;

    public Stats(int health, int def, int atk) {
        this.health = health;
        maxHealth = health;
        this.def = def;
        this.atk = atk;
    }
}

