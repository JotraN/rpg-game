package com.yosefu.game;

public class Stats {
    private float health = 10;
    private float pp = 10;
    private float atk = 10;

    public Stats(float health, float pp, float atk) {
        this.health = health;
        this.pp = pp;
        this.atk = atk;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setPp(float pp) {
        this.pp = pp;
    }

    public void setAtk(float atk) {
        this.atk = atk;
    }

    public float getHealth() {
        return health;
    }

    public float getPp() {
        return pp;
    }

    public float getAtk() {
        return atk;
    }
}

