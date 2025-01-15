package com.webserva.wings.android.rubik;

public class ExplanationItem {
    private final String letter;
    private final String description;

    public ExplanationItem(String letter, String description){
        this.letter = letter;
        this.description = description;
    }

    public String getLetter() {
        return letter;
    }

    public String getDescription() {
        return description;
    }
}
