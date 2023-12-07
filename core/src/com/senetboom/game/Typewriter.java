package com.senetboom.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Typewriter {
    /*
    Typewrite is used to print text on the screen.
    It prints one character every 0.1 seconds.
    It is used to display speech.
    It has enums for emotion and character (colour white or black)
     */

    public enum Emotion {
        NORMAL,
        HAPPY,
        SAD,
        ANGRY,
        CONFUSED
    }

    public enum Character {
        WHITE,
        BLACK
    }

    private final RandomText normalText;
    private final RandomText happyText;
    private final RandomText sadText;
    private final RandomText angryText;
    private final RandomText confusedText;


    public Typewriter() {
        normalText = new RandomText();
        normalText.addText("Let us see where the pieces fall.");
        normalText.addText("A fair move, I suppose.");
        normalText.addText("The game progresses as expected.");
        normalText.addText("May the best player win.");
        normalText.addText("It's your turn, proceed.");
        normalText.addText("This is a decent challenge.");
        normalText.addText("We play as the gods watch.");
        normalText.addText("A typical turn of events.");
        normalText.addText("Let's maintain our focus.");
        normalText.addText("Steady as the journey of Ra across the sky.");

        happyText = new RandomText();
        happyText.addText("Blessings of Isis upon my pieces!\n");
        happyText.addText("I move as gracefully as the Nile!\n");
        happyText.addText("Fortune smiles upon me today!\n");
        happyText.addText("I am favored by the gods!\n");
        happyText.addText("This is a joyous turn of events!\n");
        happyText.addText("My heart is as light as Ma'at's feather!\n");
        happyText.addText("Victory shall be mine!\n");
        happyText.addText("What a splendid move!\n");
        happyText.addText("I dance with the joy of Hathor!\n");
        happyText.addText("The gods grant me their favor!\n");

        sadText = new RandomText();
        sadText.addText("My heart sinks\n" +
                        "like the setting sun!\n");
        sadText.addText("I am lost in the desert of defeat!\n");
        sadText.addText("The gods have turned their backs on me!\n");
        sadText.addText("My spirit is as heavy as lead!\n");
        sadText.addText("I mourn my impending loss!\n");
        sadText.addText("Such sorrow fills my heart!\n");
        sadText.addText("I am adrift on the river of despair!\n");
        sadText.addText("This game mirrors my misfortunes!\n");
        sadText.addText("Sadness cloaks me like the night!\n");
        sadText.addText("I am a servant to ill fate!\n");

        angryText = new RandomText();
        angryText.addText("By the gods, this is unfair!\n");
        angryText.addText("I shall not play this treacherous game!\n");
        angryText.addText("The gods have abandoned me!\n");
        angryText.addText("Such misfortune is unheard of!\n");
        angryText.addText("I am the scorn of Thoth!\n");
        angryText.addText("This is an outrage!\n");
        angryText.addText("I cannot believe my bad luck!\n");
        angryText.addText("This game is cursed!\n");
        angryText.addText("How can the pieces move like this?\n");
        angryText.addText("You must be cheating, there's no way!\n");

        confusedText = new RandomText();
        confusedText.addText("What strange magic is this?\n");
        confusedText.addText("The pieces move in mysterious ways.\n");
        confusedText.addText("I do not understand this outcome.\n");
        confusedText.addText("Is this a trick of Anubis?\n");
        confusedText.addText("How perplexing!\n");
        confusedText.addText("I am puzzled by this turn.\n");
        confusedText.addText("The gods play games with my mind.\n");
        confusedText.addText("What does this mean?\n");
        confusedText.addText("I am lost in the labyrinth of this game.\n");
        confusedText.addText("This is beyond my understanding.\n");
    }

    public void makeSpeech(Emotion emotion, Character character){
        // create a new speech
        Speech speech = new Speech(getText(emotion), character);
        // add it to the stage
        SenetBoom.typeWriterStage.addActor(speech);
    }

    private String getText(Emotion emotion){
        // return the text depending on the emotion
        switch(emotion){
            case HAPPY:
                return happyText.getText();
            case SAD:
                return sadText.getText();
            case ANGRY:
                return angryText.getText();
            case CONFUSED:
                return confusedText.getText();
            default:
                return normalText.getText();
        }
    }

    class Speech extends Actor {
        private final String text;
        private final StringBuilder displayedText;
        private final float x;
        private final float y;
        private final float time;
        private float timePassed;
        private float displayDuration;
        private final BitmapFont font;

        public Speech(String text, Character character){

            this.text = text;
            this.displayedText = new StringBuilder();

            if (character == Character.WHITE) { // if the character is white, low right
                this.x = Gdx.graphics.getWidth() - 5*SenetBoom.tileSize;
                this.y = 2*SenetBoom.tileSize;
            }
            else { // if the character is black, top right
                this.x = Gdx.graphics.getWidth() - 5*SenetBoom.tileSize;
                this.y = Gdx.graphics.getHeight() - 2*SenetBoom.tileSize;
            }

            this.time = 0.1f; // Time interval to add next character
            this.timePassed = 0f;
            this.displayDuration = 2f; // Duration to display the complete text

            // Initialize the font
            font = new BitmapFont(); // Uses LibGDX's default font.
            font.setColor(Color.BLACK); // Set the font color to black
        }

        @Override
        public void act(float delta) {
            // Update the time passed
            timePassed += delta;

            // If the time passed is bigger than the time and text is not fully displayed
            if (timePassed > time && displayedText.length() < text.length()) {
                // Add the next character to the displayed text
                displayedText.append(text.charAt(displayedText.length()));
                // Reset the time passed
                timePassed = 0f;
            }

            // If the text is fully displayed, start counting down to remove the speech bubble
            if (displayedText.length() == text.length()) {
                displayDuration -= delta;
                if (displayDuration <= 0) {
                    this.dispose();
                    // Remove the speech bubble from the stage after the display duration is over
                    this.remove();
                }
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            font.draw(batch, displayedText.toString(), x, y);
        }

        public void dispose() {
            // To prevent memory leaks, the font must be disposed
            font.dispose();
        }
    }
}
