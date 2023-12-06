package com.senetboom.game;

public class Dices {
    private int dice1;
    private int dice2;

    public Dices() {
        this.dice1 = 0;
        this.dice2 = 0;
    }

    public void roll() {
        this.dice1 = (int) (Math.random() * 6) + 1;
        this.dice2 = (int) (Math.random() * 6) + 1;
    }

    public int getDice1() {
        return this.dice1;
    }

    public int getDice2() {
        return this.dice2;
    }
}
