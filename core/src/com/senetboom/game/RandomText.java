package com.senetboom.game;

import java.util.List;

public class RandomText {
    private List<String> texts;

    public RandomText() {
    }

    public void addText(String text) {
        texts.add(text);
    }

    public String getText() {
        return texts.get((int) (Math.random() * texts.size()));
    }
}
