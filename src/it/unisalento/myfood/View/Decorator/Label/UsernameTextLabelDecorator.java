package it.unisalento.myfood.View.Decorator.Label;

import javax.swing.*;
import java.awt.*;

public class UsernameTextLabelDecorator extends TextLabelDecorator{
    public UsernameTextLabelDecorator(Label label) {
        this.label = label;
    }

    @Override
    public JLabel getLabel() {
        JLabel decoratedLabel = label.getLabel();
        decoratedLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        return decoratedLabel;
    }
}
