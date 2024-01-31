package it.unisalento.myfood.View.Decorator.Label;

import javax.swing.*;
import java.awt.*;

public class TitleTextLabelDecorator extends TextLabelDecorator{
    public TitleTextLabelDecorator(Label label) {
        this.label = label;
    }

    @Override
    public JLabel getLabel() {
        JLabel decoratedLabel = label.getLabel();
        decoratedLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        return decoratedLabel;
    }
}
