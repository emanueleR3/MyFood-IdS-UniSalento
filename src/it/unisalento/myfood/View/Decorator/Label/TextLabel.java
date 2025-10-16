package it.unisalento.myfood.View.Decorator.Label;

import javax.swing.*;

public class TextLabel extends Label{
    private String text;

    public TextLabel(String text) {
        this.text = text;
        label = new JLabel(text);
    }
}
