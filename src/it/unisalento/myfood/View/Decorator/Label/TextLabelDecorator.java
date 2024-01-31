package it.unisalento.myfood.View.Decorator.Label;

import javax.swing.*;

public class TextLabelDecorator extends Label{
    Label label;

    @Override
    public JLabel getLabel() {
        return label.getLabel();
    }
}
