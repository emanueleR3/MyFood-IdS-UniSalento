package it.unisalento.myfood.View.EsempiVistiALezione;

import javax.swing.*;

public class MyFrame extends JFrame {

    public MyFrame() {
        super("Prima finestra");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new JLabel("Buona lezione"));
        add(new JButton("Click me"));

        setVisible(true);
    }
}
