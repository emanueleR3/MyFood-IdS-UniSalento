package it.unisalento.myfood.View.Listener;

import it.unisalento.myfood.Business.InterazioneUtenteBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.ArticoloPanel;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.QuantityCartFrame;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ArticoloPanelListener implements ActionListener {

    private static Frame frame;
    private static ArticoloPanel articoloPanel;

    private static final UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();

    public final static String ADD_TO_CART_BTN = "LOGIN_BTN";
    public final static String BACK_TO_CATALOGUE_BTN = "ABOUT_BTN";
    public final static String SEND_COMMENT_BTN = "SEND_COMMENT_BTN";
    public final static String EDIT_COMMENT_BTN = "EDIT_COMMENT_BTN";
    public final static String REMOVE_COMMENT_BTN = "REMOVE_COMMENT_BTN";

    public ArticoloPanelListener(Frame frame, ArticoloPanel articoloPanel) {
        ArticoloPanelListener.frame = frame;
        ArticoloPanelListener.articoloPanel = articoloPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ADD_TO_CART_BTN -> {
                if (utenteBusiness.isLoggedCliente()) {
                    new QuantityCartFrame(articoloPanel, frame).openQuantityFrame();

                } else if (utenteBusiness.isLoggedGuest()) {
                    JOptionPane.showMessageDialog(null, "Per poter aggiungere al carrello devi essere registrato");
                    articoloPanel.getTimer().cancel();
                    articoloPanel.getTimer().purge();
                    frame.showPanel(Frame.PANEL.SIGNUP);
                }
            }
            case BACK_TO_CATALOGUE_BTN -> {
                articoloPanel.getTimer().cancel();
                articoloPanel.getTimer().purge();
                frame.showPanel(Frame.PANEL.DASHBOARD_CLIENTE);
            }
            case SEND_COMMENT_BTN -> {
                int indexValutazione = articoloPanel.getComboBoxValutazione().getSelectedIndex();
                sendCommento(articoloPanel.getTextArea().getText(), articoloPanel.getComboBoxValutazione().getItemAt(indexValutazione));
            }
            case EDIT_COMMENT_BTN -> {
                articoloPanel.getTextArea().setEnabled(true);
                articoloPanel.getComboBoxValutazione().setEnabled(true);
                articoloPanel.getBtnInvioCommento().setEnabled(true);

                articoloPanel.getInvioCommentoPanel().removeAll();
                articoloPanel.getInvioCommentoPanel().add(articoloPanel.getBtnInvioCommento());
                articoloPanel.getInvioCommentoPanel().add(articoloPanel.getBtnRemoveCommento());

                articoloPanel.revalidate();
                articoloPanel.repaint();
            }
            case REMOVE_COMMENT_BTN -> removeCommento();
        }
    }

    private void sendCommento(String testo, String valutazione) {

        // TODO ATTIVARE DOPO I VARI TEST DI FUNZIONAMENTO!!! - dovrebbe funzionare
        /*
        if (!UtenteBusiness.getInstance().loggedUtenteHasPurchasedArticolo()) {
            JOptionPane.showMessageDialog(null, "Non puoi valutare un articolo che non hai acquistato!");
            return;
        }
        */

        if (testo.equals(articoloPanel.getPlaceholderHint())) testo = "";

        if (valutazione.equals("-")) {
            JOptionPane.showMessageDialog(frame, "Inserisci la valutazione da 1 a 5");
            return;
        }

        boolean done;
        if (articoloPanel.isAlreadyCommented()) {
            if (testo.equals(InterazioneUtenteBusiness.getInstance().getTestoCommentoViewArticolo()) && valutazione.equals(InterazioneUtenteBusiness.getInstance().getValutazioneCommentoViewArticolo())) {
                articoloPanel.updateUICommento();
                return;
            }
            done = InterazioneUtenteBusiness.getInstance().updateCommento(testo, valutazione);
        } else {
            done = InterazioneUtenteBusiness.getInstance().insertCommento(testo, valutazione);
        }

        if (!done) {
            JOptionPane.showMessageDialog(null, "ERRORE! Commento non inserito!");
        } else {
            JOptionPane.showMessageDialog(null, "Commento inserito con successo!");

            articoloPanel.updateUICommento();
        }
    }

    private void removeCommento() {
        int valueDone = InterazioneUtenteBusiness.getInstance().removeCommento();

        if (valueDone == 0) {
            JOptionPane.showMessageDialog(null, "ERRORE! Commento non eliminato!");
        } else if (valueDone == 1) {
            JOptionPane.showMessageDialog(null, "Commento eliminato con successo!");

            articoloPanel.updateUICommento();
        }
    }
}
