package it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione;

import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.Listener.Gestione.GestioneArticoliListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PulsantieraGestioneArticoliDecorator extends PulsantieraGestioneDecorator {


    private JButton btnVedi = new JButton("Vedi Articolo");
    private JButton btnAggiungiProd = new JButton("Aggiungi Prodotto");
    private JButton btnAggiungiMen = new JButton("Aggiungi Menu");
    private JButton btnModifica = new JButton("Modifica Articolo");
    private JButton btnRimuovi = new JButton("Rimuovi Articolo");

    public PulsantieraGestioneArticoliDecorator(Pulsantiera pulsantiera) {
        this.pulsantiera = pulsantiera;

    }
    @Override
    public ArrayList<JButton> getPulsanti() {
        pulsanti.addAll(this.pulsantiera.getPulsanti());
        btnVedi.setActionCommand(GestioneArticoliListener.VEDI_BTN);
        btnAggiungiProd.setActionCommand(GestioneArticoliListener.AGGIUNGI_PROD_BTN);
        btnAggiungiMen.setActionCommand(GestioneArticoliListener.AGGIUNGI_MENU_BTN);
        btnModifica.setActionCommand(GestioneArticoliListener.MODIFICA_BTN);
        btnRimuovi.setActionCommand(GestioneArticoliListener.RIMUOVI_BTN);


        pulsanti.add(btnVedi);
        pulsanti.add(btnAggiungiProd);
        pulsanti.add(btnAggiungiMen);
        pulsanti.add(btnModifica);
        pulsanti.add(btnRimuovi);


        for (JButton btn : pulsanti) {
            btn.setPreferredSize(new Dimension(180, 25));
        }

        return (ArrayList<JButton>) pulsanti;
    }
}
