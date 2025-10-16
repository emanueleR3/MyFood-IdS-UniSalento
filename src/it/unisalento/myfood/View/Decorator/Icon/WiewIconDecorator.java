package it.unisalento.myfood.View.Decorator.Icon;

import javax.swing.*;
import java.awt.*;

public class WiewIconDecorator extends IconDecorator{
    private static final int WIDTH = 200;
    private static final int HEIGH = 200;


    public WiewIconDecorator(Icon icon) {
        this.icon = icon;
    }

    @Override
    public ImageIcon getImageIcon() {
        Image resizedImage = icon.imageIcon.getImage().getScaledInstance(WIDTH, HEIGH, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}
