package it.unisalento.myfood.View.Decorator.Icon;

import javax.swing.*;

public class OriginalIcon extends Icon{
    private String path;

    public OriginalIcon(String path) {
        this.path = path;
        imageIcon = new ImageIcon(path);
    }
}
