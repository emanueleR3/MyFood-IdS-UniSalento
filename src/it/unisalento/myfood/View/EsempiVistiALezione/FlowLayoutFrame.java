package it.unisalento.myfood.View.EsempiVistiALezione;

import javax.swing.*;
import java.awt.*;

public class FlowLayoutFrame extends JFrame {

    JLabel label = new JLabel("Buona lezione");
    JButton button = new JButton("Click me!");

    public FlowLayoutFrame() {
        super("Seconda finestra");
        setSize(200, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(label);
        add(button);

        setLayout(new FlowLayout());    // Gestisce il layout degli elementi presenti nella finestra

        setVisible(true);
    }
}
