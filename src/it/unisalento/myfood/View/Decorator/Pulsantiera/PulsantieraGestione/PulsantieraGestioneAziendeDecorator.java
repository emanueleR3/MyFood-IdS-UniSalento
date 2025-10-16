package it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione;

import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.Listener.Gestione.GestioneAziendeListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PulsantieraGestioneAziendeDecorator extends PulsantieraGestioneDecorator {


   // private JButton btnVedi = new JButton("Vedi Azienda");
    private JButton btnAggiungi = new JButton("Aggiungi Azienda");
    private JButton btnModifica = new JButton("Modifica Azienda");
    private JButton btnRimuovi = new JButton("Rimuovi Azienda");

    public PulsantieraGestioneAziendeDecorator(Pulsantiera pulsantiera) {
        this.pulsantiera = pulsantiera;

    }
    @Override
    public ArrayList<JButton> getPulsanti() {
        pulsanti.addAll(this.pulsantiera.getPulsanti());
        //btnVedi.setActionCommand(GestioneAziendeListener.VEDI_BTN);
        btnAggiungi.setActionCommand(GestioneAziendeListener.AGGIUNGI_BTN);
        btnModifica.setActionCommand(GestioneAziendeListener.MODIFICA_BTN);
        btnRimuovi.setActionCommand(GestioneAziendeListener.RIMUOVI_BTN);


        //pulsanti.add(btnVedi);
        pulsanti.add(btnAggiungi);
        pulsanti.add(btnModifica);
        pulsanti.add(btnRimuovi);


        for (JButton btn : pulsanti) {
            btn.setPreferredSize(new Dimension(180, 25));
        }

        return (ArrayList<JButton>) pulsanti;
    }
}
