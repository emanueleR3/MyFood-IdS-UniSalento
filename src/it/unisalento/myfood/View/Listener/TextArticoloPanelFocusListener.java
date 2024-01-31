package it.unisalento.myfood.View.Listener;

import it.unisalento.myfood.View.ArticoloPanel;
import it.unisalento.myfood.View.Frame;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextArticoloPanelFocusListener implements FocusListener {

    private static Frame frame;
    private static ArticoloPanel articoloPanel;

    public TextArticoloPanelFocusListener(Frame frame, ArticoloPanel articoloPanel) {
        TextArticoloPanelFocusListener.frame = frame;
        TextArticoloPanelFocusListener.articoloPanel = articoloPanel;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(articoloPanel.getTextArea().getText().equals(articoloPanel.getPlaceholderHint())) {
            articoloPanel.getTextArea().setText("");
            articoloPanel.getTextArea().setForeground(Color.BLACK);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (articoloPanel.getTextArea().getText().isEmpty()) {
            articoloPanel.getTextArea().setText(articoloPanel.getPlaceholderHint());
            articoloPanel.getTextArea().setForeground(Color.GRAY);
        }
    }
}
