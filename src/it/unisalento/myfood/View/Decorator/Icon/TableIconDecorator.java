package it.unisalento.myfood.View.Decorator.Icon;

import javax.swing.*;
import java.awt.*;

public class TableIconDecorator extends IconDecorator{
    private static final int WIDTH = 80;
    private static final int HEIGHT = 80;


    public TableIconDecorator(Icon icon) {
        this.icon = icon;
    }

    @Override
    public ImageIcon getImageIcon() {
        Image resizedImage = icon.imageIcon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}
