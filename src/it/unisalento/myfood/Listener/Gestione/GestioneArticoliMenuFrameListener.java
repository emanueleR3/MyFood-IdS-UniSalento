package it.unisalento.myfood.Listener.Gestione;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.View.GestioneArticoliMenuFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestioneArticoliMenuFrameListener implements ActionListener {

    public static final String BTN_ADD = "BTN_ADD";
    public static final String BTN_REMOVE = "BTN_REMOVE";
    public static final String BTN_CONFERMA_SALVA = "BTN_CONFERMA_SALVA";

    private GestioneArticoliMenuFrame frame;

    public GestioneArticoliMenuFrameListener(GestioneArticoliMenuFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComboBox) {
            frame.initComponents(frame.getTipologiaProdottoSelezionata());
        } else if (e.getSource() instanceof JButton) {
            switch (e.getActionCommand()) {
                case BTN_ADD -> {
                    Integer idArticolo = frame.getLeftTableRowSelected();
                    if (idArticolo != null) {
                        if (idArticolo.equals(frame.getIdMenuSelezionato())) {
                            JOptionPane.showMessageDialog(frame, "ERRORE! Non puoi aggiungere al menu se stesso");
                            return;
                        }

                        if (frame.getIdArticoliSelezionati().contains(idArticolo)) {
                            JOptionPane.showMessageDialog(frame, "ERRORE! Hai già inserito questo articolo");
                            return;
                        }

                        if (ArticoloBusiness.getInstance().isSupermenu(idArticolo)) {
                            JOptionPane.showMessageDialog(frame, "ERRORE! Non puoi inserire un supermenu all'interno di un Menu");
                        } else {
                            frame.getIdArticoliSelezionati().add(idArticolo);
                            frame.initComponents(frame.getTipologiaProdottoSelezionata());
                        }
                    }
                }
                case BTN_REMOVE -> {
                    Integer idArticolo = frame.getRightTableRowSelected();

                    frame.getIdArticoliSelezionati().remove(idArticolo);
                    frame.initComponents(frame.getTipologiaProdottoSelezionata());
                }
                case BTN_CONFERMA_SALVA -> {
                    if (checkComposizione()) {
                        frame.setEnableComboBoxComposizioni(true);
                        frame.dispose();
                    } else {
                        showErrorMessage();
                    }
                }
            }
        }
    }

    private boolean checkComposizione() {
        return ArticoloBusiness.getInstance().isComposizioneValida(frame.getComposizioneScelta(), frame.getIdArticoliSelezionati());
    }

    private void showErrorMessage() {
        String composizioneScelta = frame.getComposizioneScelta();

        switch (composizioneScelta) {
            case ArticoloBusiness.COMPOSIZIONE_INSALATA -> JOptionPane.showMessageDialog(frame, "Devi inserire almeno 1 insalata e 1 bevanda");
            case ArticoloBusiness.COMPOSIZIONE_PANINO -> JOptionPane.showMessageDialog(frame, "Devi inserire almeno 1 panino e 1 bevanda");
            case ArticoloBusiness.SOLO_MENU -> JOptionPane.showMessageDialog(frame, "Devi inserire almeno 2 menu");
        }
    }
}
