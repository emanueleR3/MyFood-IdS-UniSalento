package it.unisalento.myfood.View.Decorator.PulsantieraDashboard;

import it.unisalento.myfood.View.Listener.DashboardListener;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;


public class PulsantieraAmministratoreDecorator extends PulsantieraDasboardDecorator {

    public PulsantieraAmministratoreDecorator(PulsantieraDashboard pulsantiera) {
        this.pulsantiera = pulsantiera;
    }

    public List<JButton> getPulsanti() {
        pulsanti.addAll(this.pulsantiera.getPulsanti());
        pulsanti.get(0).setText("LogOut");

        pulsanti.remove(1);     // Rimuovo vedi in dettaglio

        JButton btnGestisciUtenti = new JButton("Gestisci Utenti");
        btnGestisciUtenti.setActionCommand(DashboardListener.GESTISCI_UTENTI_BTN);
        JButton btnGestisciOrdini = new JButton("Gestisci Ordini");
        btnGestisciOrdini.setActionCommand(DashboardListener.GESTISCI_ORDINI_BTN);
        JButton btnGestisciCommenti = new JButton("Gestisci Commenti");
        btnGestisciCommenti.setActionCommand(DashboardListener.GESTISCI_COMMENTI_BTN);
        JButton btnGestisciArticoli = new JButton("Gestisci Articoli");
        btnGestisciArticoli.setActionCommand(DashboardListener.GESTISCI_ARTICOLI_BTN);
        JButton btnGestisciTipProd = new JButton("Gestisci Tipologie Prodotti");
        btnGestisciTipProd.setActionCommand(DashboardListener.GESTISCI_TIP_PRODOTTI_BTN);
        JButton btnGestisciIngredienti = new JButton("Gestisci Ingredienti");
        btnGestisciIngredienti.setActionCommand(DashboardListener.GESTISCI_INGREDIENTI_BTN);
        JButton btnGestisciTipIngred = new JButton("Gestisci Tipologie Ingredienti");
        btnGestisciTipIngred.setActionCommand(DashboardListener.GESTISCI_TIP_INGREDIENTI_BTN);
        JButton btnGestisciAziende = new JButton("Gestisci Aziende");
        btnGestisciAziende.setActionCommand(DashboardListener.GESTISCI_AZIENDE_BTN);

        //aggiungi listener, action command
        pulsanti.add(btnGestisciUtenti);
        pulsanti.add(btnGestisciCommenti);
        pulsanti.add(btnGestisciArticoli);
        pulsanti.add(btnGestisciTipProd);
        pulsanti.add(btnGestisciIngredienti);
        pulsanti.add(btnGestisciTipIngred);
        pulsanti.add(btnGestisciAziende);
        pulsanti.add(btnGestisciOrdini);

        Iterator<JButton> iterator = pulsanti.listIterator();

        while (iterator.hasNext()){
            iterator.next().setPreferredSize(new Dimension(220, 25));
        }

        return pulsanti;
    }
}
