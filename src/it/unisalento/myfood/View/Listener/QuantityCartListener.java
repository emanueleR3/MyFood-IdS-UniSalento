package it.unisalento.myfood.View.Listener;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Business.CarrelloBusiness;
import it.unisalento.myfood.View.ArticoloPanel;
import it.unisalento.myfood.View.CarrelloPanel;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.QuantityCartFrame;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuantityCartListener implements ActionListener {

    private Frame frame;
    private JPanel panel;
    private JFrame quantityFrame;

    public final static String ADD_BTN = "ADD_BTN";
    public final static String REMOVE_BTN = "REMOVE_BTN";

    private static final CarrelloBusiness carrelloBusiness = CarrelloBusiness.getInstance();

    public QuantityCartListener(Frame frame, JFrame quantityFrame, JPanel panel) {
        this.frame = frame;
        this.panel = panel;
        this.quantityFrame = quantityFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ADD_BTN -> {
                int quantitaSelezionata = (int) QuantityCartFrame.getSpinnerQuantita().getValue();
                aggiungiAlCarrello(quantitaSelezionata);

                if (panel instanceof ArticoloPanel) {
                    frame.showPanel(Frame.PANEL.ARTICOLO);
                }
                if(panel instanceof CarrelloPanel){
                    ((CarrelloPanel) panel).loadArticoli();
                    frame.showPanel(Frame.PANEL.CARRELLO);
                }


                quantityFrame.dispose(); // Chiudi la finestra pop-up
            }
            case REMOVE_BTN -> {
                rimuoviArticoloDalCarrello();

                if (panel instanceof ArticoloPanel) {
                    ((ArticoloPanel)panel).updateUIQuantita(0);
                    frame.showPanel(Frame.PANEL.CARRELLO);
                }
                if(panel instanceof CarrelloPanel){
                    ((CarrelloPanel) panel).loadArticoli();
                    frame.showPanel(Frame.PANEL.CARRELLO);
                }

                quantityFrame.dispose();
            }
        }
    }

    private void aggiungiAlCarrello(int quantita) {
        carrelloBusiness.addSelectedArticoloToCarrello(quantita);
        carrelloBusiness.loadCarrelloToSession();
        if(!CarrelloBusiness.isAddeddSuccessfully())
            JOptionPane.showMessageDialog(frame, CarrelloBusiness.getMessage());

    }

    private void rimuoviArticoloDalCarrello() {
        carrelloBusiness.removeSelectedArticoloFromCart();
        carrelloBusiness.loadCarrelloToSession();
        if(!CarrelloBusiness.isRemovedSuccessfully())
            JOptionPane.showMessageDialog(frame, CarrelloBusiness.getMessage());
    }
}
