package com.yosefu.game;

public class Stats {
    public int health = 10;
    public int maxHealth = 10;
    public int pp = 10;
    public int atk = 10;

    public Stats(int health, int pp, int atk) {
        this.health = health;
        maxHealth = health;
        this.pp = pp;
        this.atk = atk;
    }
}

