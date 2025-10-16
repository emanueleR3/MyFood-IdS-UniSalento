package it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione;

import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.Listener.Gestione.GestioneOrdiniListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PulsantieraGestioneOrdiniDecorator extends PulsantieraGestioneDecorator {


    private JButton btnVedi = new JButton("Vedi Ordine");
    private JButton btnRimuovi = new JButton("Rimuovi Ordine");

    public PulsantieraGestioneOrdiniDecorator(Pulsantiera pulsantiera) {
        this.pulsantiera = pulsantiera;

    }
    @Override
    public ArrayList<JButton> getPulsanti() {
        pulsanti.addAll(this.pulsantiera.getPulsanti());
        btnVedi.setActionCommand(GestioneOrdiniListener.VEDI_BTN);
        btnRimuovi.setActionCommand(GestioneOrdiniListener.RIMUOVI_BTN);


        pulsanti.add(btnVedi);
        pulsanti.add(btnRimuovi);


        for (JButton btn : pulsanti) {
            btn.setPreferredSize(new Dimension(180, 25));
        }

        return (ArrayList<JButton>) pulsanti;
    }
}
