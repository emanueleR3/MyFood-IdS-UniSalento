package it.unisalento.myfood.View.Decorator.Label;

import javax.swing.*;
import java.awt.*;

public class WhiteTextLabelSecondariaDecorator extends TextLabelDecorator{
    public WhiteTextLabelSecondariaDecorator(Label label) {this.label = label;
    }

    @Override
    public JLabel getLabel() {
        JLabel decoratedLabel = label.getLabel();
        decoratedLabel.setFont(new Font("Courier New", Font.PLAIN, 12));
        decoratedLabel.setForeground(Color.WHITE);
        return decoratedLabel;
    }
}
