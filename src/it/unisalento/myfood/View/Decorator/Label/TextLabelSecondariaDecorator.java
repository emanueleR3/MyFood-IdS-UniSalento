package it.unisalento.myfood.View.Decorator.Label;

import javax.swing.*;
import java.awt.*;

public class TextLabelSecondariaDecorator extends TextLabelDecorator{
    public TextLabelSecondariaDecorator(Label label) {this.label = label;
    }

    @Override
    public JLabel getLabel() {
        JLabel decoratedLabel = label.getLabel();
        decoratedLabel.setFont(new Font("Courier New", Font.PLAIN, 12));
        return decoratedLabel;
    }
}
