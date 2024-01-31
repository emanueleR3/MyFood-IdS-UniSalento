package it.unisalento.myfood.View.Decorator.PulsantieraDashboard;

import it.unisalento.myfood.View.Listener.DashboardListener;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class PulsantieraClienteDecorator extends PulsantieraDasboardDecorator {

    public PulsantieraClienteDecorator(PulsantieraDashboard pulsantiera) {
        this.pulsantiera = pulsantiera;
    }

    public List<JButton> getPulsanti() {
        pulsanti.addAll(this.pulsantiera.getPulsanti());
        pulsanti.get(0).setText("LogOut");

        JButton add = new JButton("Aggiungi al carrello");
        add.setActionCommand(DashboardListener.ADD_TO_CART_BTN);
        JButton carr = new JButton("Visualizza Carrello");
        carr.setActionCommand(DashboardListener.VEDI_CARRELLO_BTN);
        JButton ord = new JButton("Visualizza Ordini");
        ord.setActionCommand(DashboardListener.VEDI_ORDINE_BTN);

        //aggiungi listener, action command
        pulsanti.add(add);
        pulsanti.add(carr);
        pulsanti.add(ord);

        Iterator<JButton> iterator = pulsanti.listIterator();

        while (iterator.hasNext()){
            iterator.next().setPreferredSize(new Dimension(180, 25));

        }


        return pulsanti;
    }
}
