package it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione;

import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.Listener.Gestione.GestioneTipologieIngredientiListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PulsantieraGestioneTipologieIngredientiDecorator extends PulsantieraGestioneDecorator {


    private JButton btnAggiungi = new JButton("Aggiungi Tipologia");
    private JButton btnModifica = new JButton("Modifica Tipologia");
    private JButton btnRimuovi = new JButton("Rimuovi Tipologia");

    public PulsantieraGestioneTipologieIngredientiDecorator(Pulsantiera pulsantiera) {
        this.pulsantiera = pulsantiera;

    }

    @Override
    public ArrayList<JButton> getPulsanti() {
        pulsanti.addAll(this.pulsantiera.getPulsanti());

        btnAggiungi.setActionCommand(GestioneTipologieIngredientiListener.AGGIUNGI_BTN);
        btnModifica.setActionCommand(GestioneTipologieIngredientiListener.MODIFICA_BTN);
        btnRimuovi.setActionCommand(GestioneTipologieIngredientiListener.RIMUOVI_BTN);


        pulsanti.add(btnAggiungi);
        pulsanti.add(btnModifica);
        pulsanti.add(btnRimuovi);


        for (JButton btn : pulsanti) {
            btn.setPreferredSize(new Dimension(180, 25));
        }

        return (ArrayList<JButton>) pulsanti;
    }
}
