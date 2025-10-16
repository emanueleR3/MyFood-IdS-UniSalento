package it.unisalento.myfood.Listener;

import it.unisalento.myfood.View.VisualizzaCommentiFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VisualizzaCommentiFrameListener implements ActionListener {

    private final VisualizzaCommentiFrame frame;

    public static final String CLOSE_BTN = "CLOSE_BTN";

    public VisualizzaCommentiFrameListener(VisualizzaCommentiFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case CLOSE_BTN -> frame.dispose();
        }
    }
}
