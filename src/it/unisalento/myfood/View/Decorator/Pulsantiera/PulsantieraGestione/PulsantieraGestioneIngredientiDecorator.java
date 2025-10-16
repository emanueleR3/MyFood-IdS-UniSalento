package it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione;

import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.Listener.Gestione.GestioneIngredientiListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PulsantieraGestioneIngredientiDecorator extends PulsantieraGestioneDecorator {


    private JButton btnVedi = new JButton("Vedi Ingrediente");
    private JButton btnAggiungi = new JButton("Aggiungi Ingrediente");
    private JButton btnModifica = new JButton("Modifica Ingrediente");
    private JButton btnRimuovi = new JButton("Rimuovi Ingrediente");

    public PulsantieraGestioneIngredientiDecorator(Pulsantiera pulsantiera) {
        this.pulsantiera = pulsantiera;

    }
    @Override
    public ArrayList<JButton> getPulsanti() {
        pulsanti.addAll(this.pulsantiera.getPulsanti());
        btnVedi.setActionCommand(GestioneIngredientiListener.VEDI_BTN);
        btnAggiungi.setActionCommand(GestioneIngredientiListener.AGGIUNGI_BTN);
        btnModifica.setActionCommand(GestioneIngredientiListener.MODIFICA_BTN);
        btnRimuovi.setActionCommand(GestioneIngredientiListener.RIMUOVI_BTN);


        pulsanti.add(btnVedi);
        pulsanti.add(btnAggiungi);
        pulsanti.add(btnModifica);
        pulsanti.add(btnRimuovi);


        for (JButton btn : pulsanti) {
            btn.setPreferredSize(new Dimension(180, 25));
        }

        return (ArrayList<JButton>) pulsanti;
    }
}
