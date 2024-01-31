package it.unisalento.myfood.View.EsempiVistiALezione;

import javax.swing.*;
import java.awt.*;

public class BorderLayoutFrame extends JFrame {

    JLabel label = new JLabel("Buona lezione");
    JButton button = new JButton("Click me!");

    public BorderLayoutFrame() {
        super("Quarta finestra");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());    // Gestisce il layout degli elementi presenti nella finestra

        add(label, BorderLayout.NORTH);
        add(button, BorderLayout.SOUTH);
        add(new JTextField(), BorderLayout.CENTER);

        setVisible(true);
    }
}
