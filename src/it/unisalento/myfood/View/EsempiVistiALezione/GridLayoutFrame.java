package it.unisalento.myfood.View.EsempiVistiALezione;

import javax.swing.*;
import java.awt.*;

public class GridLayoutFrame extends JFrame {

    JLabel label = new JLabel("Buona lezione");
    JButton button = new JButton("Click me!");

    public GridLayoutFrame() {
        super("Terza finestra");
        setSize(200, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(label);
        add(button);

        for (int i = 0; i < 10; i++) {
            add(new JButton("Pulsante " + i));
        }

        setLayout(new GridLayout(3, 5));    // Gestisce il layout degli elementi presenti nella finestra

        setVisible(true);
    }
}
