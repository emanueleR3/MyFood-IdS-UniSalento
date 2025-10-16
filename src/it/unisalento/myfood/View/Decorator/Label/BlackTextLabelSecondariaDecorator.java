package it.unisalento.myfood.View.Decorator.Label;

import javax.swing.*;
import java.awt.*;

public class BlackTextLabelSecondariaDecorator extends TextLabelDecorator{
    public BlackTextLabelSecondariaDecorator(Label label) {this.label = label;
    }

    @Override
    public JLabel getLabel() {
        JLabel decoratedLabel = label.getLabel();
        decoratedLabel.setFont(new Font("Courier New", Font.PLAIN, 14));
        decoratedLabel.setForeground(Color.BLACK);
        return decoratedLabel;
    }
}
