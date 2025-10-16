package it.unisalento.myfood.Listener;

import it.unisalento.myfood.View.VisualizzaArticoliFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VisualizzaArticoliFrameListener implements ActionListener {

    public static final String CLOSE_BTN = "CLOSE_BTN";

    private final VisualizzaArticoliFrame frame;

    public VisualizzaArticoliFrameListener(VisualizzaArticoliFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case CLOSE_BTN -> frame.dispose();
        }
    }
}
