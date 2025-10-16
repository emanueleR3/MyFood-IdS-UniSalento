package it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione;

import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.Listener.Gestione.GestioneUtentiListener;

import javax.swing.*;

public class PulsantieraGestione extends Pulsantiera {

    public PulsantieraGestione(){
        JButton btnTornaDashboard = new JButton("Torna alla Dashboard");
        btnTornaDashboard.setActionCommand(GestioneUtentiListener.TORNA_DASHBOARD_BTN);
        pulsanti.add(btnTornaDashboard);
    }
}