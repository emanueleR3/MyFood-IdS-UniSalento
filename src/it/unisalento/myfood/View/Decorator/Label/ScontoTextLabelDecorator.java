package it.unisalento.myfood.View.Decorator.Label;

import javax.swing.*;
import java.awt.*;

public class ScontoTextLabelDecorator extends TextLabelDecorator{
    public ScontoTextLabelDecorator(Label label) {
        this.label = label;
    }

    @Override
    public JLabel getLabel() {
        JLabel decoratedLabel = label.getLabel();
        decoratedLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        decoratedLabel.setForeground(Color.WHITE);
        decoratedLabel.setOpaque(true);
        decoratedLabel.setBackground(Color.RED);
        return decoratedLabel;
    }
}
