package it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraDashboard;

import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.Listener.DashboardListener;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class PulsantieraCucinaDecorator extends PulsantieraDasboardDecorator {

    public PulsantieraCucinaDecorator(Pulsantiera pulsantiera) {
        this.pulsantiera = pulsantiera;
    }

    public List<JButton> getPulsanti() {
        pulsanti.addAll(this.pulsantiera.getPulsanti());
        pulsanti.get(0).setText("LogOut");

        pulsanti.remove(1);  // Rimuovo il pulsante "vedi in dettaglio"

        JButton btnVediOrdine = new JButton("Vedi Dettaglio Ordine");
        btnVediOrdine.setActionCommand(DashboardListener.VEDI_ORDINE_CUCINA_BTN);

        JButton btnIniziaLavorazione = new JButton("Inizia Lavorazione");
        btnIniziaLavorazione.setActionCommand(DashboardListener.INIZIA_LAVORAZIONE_BTN);

        JButton btnOrdineCompletato = new JButton("Segna come completato");
        btnOrdineCompletato.setActionCommand(DashboardListener.ORDINE_COMPLETATO_BTN);

        pulsanti.add(btnVediOrdine);
        pulsanti.add(btnIniziaLavorazione);
        pulsanti.add(btnOrdineCompletato);


        Iterator<JButton> iterator = pulsanti.listIterator();

        while (iterator.hasNext()){
            iterator.next().setPreferredSize(new Dimension(180, 25));

        }


        return pulsanti;
    }
}
