package it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione;

import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.Listener.Gestione.GestioneCommentiListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PulsantieraGestioneCommentiDecorator extends PulsantieraGestioneDecorator {


    private JButton btnAggiungi = new JButton("Aggiungi Riposta");
    private JButton btnRimuoviRisposta = new JButton("Rimuovi Risposta");
    private JButton btnRimuovi = new JButton("Rimuovi Commento");

    public PulsantieraGestioneCommentiDecorator(Pulsantiera pulsantiera) {
        this.pulsantiera = pulsantiera;

    }
    @Override
    public ArrayList<JButton> getPulsanti() {
        pulsanti.addAll(this.pulsantiera.getPulsanti());
        btnAggiungi.setActionCommand(GestioneCommentiListener.AGGIUNGI_BTN);
        btnRimuoviRisposta.setActionCommand(GestioneCommentiListener.RIMUOVI_RISPOSTA_BTN);
        btnRimuovi.setActionCommand(GestioneCommentiListener.RIMUOVI_COMMENTO_BTN);


        pulsanti.add(btnAggiungi);
        pulsanti.add(btnRimuoviRisposta);
        pulsanti.add(btnRimuovi);


        for (JButton btn : pulsanti) {
            btn.setPreferredSize(new Dimension(180, 25));
        }

        return (ArrayList<JButton>) pulsanti;
    }
}
