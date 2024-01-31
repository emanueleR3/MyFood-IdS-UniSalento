package it.unisalento.myfood.View.EsempiVistiALezione;

import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.model.Utente;

import javax.swing.*;
import java.awt.*;

public class LoggedInPanel extends JPanel {

    private JButton logout = new JButton("Logout");

    public LoggedInPanel() {
        setLayout(new FlowLayout());

        // TODO: per fare vedere l'avatar di login, ovviamente la path cambierà per ogni computer, a meno che non la mettiamo nella cartella src
        ImageIcon img = new ImageIcon("C:\\Users\\Utente\\Documents\\MyFood_images\\avatar.png");
        add(new JLabel(img));

        Utente u = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER);
        add(new JLabel("Benvenuto, " + u.getNome() + "!"));

        add(logout);
    }
}
