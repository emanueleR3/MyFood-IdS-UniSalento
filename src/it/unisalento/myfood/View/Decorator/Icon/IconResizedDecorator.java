package it.unisalento.myfood.View.Decorator.Icon;

import javax.swing.*;
import java.awt.*;

public class IconResizedDecorator extends IconDecorator{
    private int width;
    private int heigh;

    public IconResizedDecorator(Icon icon, int width, int heigh) {
        this.icon = icon;
        this.width = width;
        this.heigh = heigh;
    }

    @Override
    public ImageIcon getImageIcon() {
        Image resizedImage = icon.imageIcon.getImage().getScaledInstance(width, heigh, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}
