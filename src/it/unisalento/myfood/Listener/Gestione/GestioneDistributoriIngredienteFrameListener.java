package it.unisalento.myfood.Listener.Gestione;


import it.unisalento.myfood.View.GestioneDistributoriIngredienteFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestioneDistributoriIngredienteFrameListener implements ActionListener {

    public static final String BTN_ADD = "BTN_ADD";
    public static final String BTN_REMOVE = "BTN_REMOVE";
    public static final String BTN_CONFERMA_SALVA = "BTN_CONFERMA_SALVA";

    private GestioneDistributoriIngredienteFrame frame;

    public GestioneDistributoriIngredienteFrameListener(GestioneDistributoriIngredienteFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case BTN_ADD -> {
                    Integer idAzienda = frame.getLeftTableRowSelected();
                    if (idAzienda != null) {
                        if (frame.getIdDistributoriSelezionati().contains(idAzienda)) {
                            JOptionPane.showMessageDialog(frame, "Azienda già selezionata");
                            return;
                        }

                        frame.getIdDistributoriSelezionati().add(idAzienda);
                        frame.initComponents();
                    }
                }
                case BTN_REMOVE -> {
                    Integer idAzienda = frame.getRightTableRowSelected();

                    frame.getIdDistributoriSelezionati().remove(idAzienda);
                    frame.initComponents();
                }
                case BTN_CONFERMA_SALVA -> frame.dispose();

            }
    }
}