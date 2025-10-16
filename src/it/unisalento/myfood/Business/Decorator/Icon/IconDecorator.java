package it.unisalento.myfood.Business.Decorator.Icon;

import javax.swing.*;

public class IconDecorator extends Icon {
    Icon icon;

    @Override
    public ImageIcon getImageIcon() {
        return icon.getImageIcon();
    }
}
