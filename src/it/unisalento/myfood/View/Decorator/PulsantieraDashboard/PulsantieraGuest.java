package it.unisalento.myfood.View.Decorator.PulsantieraDashboard;

import it.unisalento.myfood.View.Listener.DashboardListener;

import javax.swing.*;

public class PulsantieraGuest extends PulsantieraDashboard {

    public PulsantieraGuest() {
        JButton logOut = new JButton("Accedi o registrati");
        logOut.setActionCommand(DashboardListener.LOGOUT_BTN);
        pulsanti.add(logOut);
        JButton vedi = new JButton("Vedi in dettaglio");
        vedi.setActionCommand(DashboardListener.VEDI_ARTICOLO_BTN);
        //vedi.setEnabled(false);
        pulsanti.add(vedi);

    }

}
