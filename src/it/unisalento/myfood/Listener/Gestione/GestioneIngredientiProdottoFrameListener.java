package it.unisalento.myfood.Listener.Gestione;

import it.unisalento.myfood.View.GestioneIngredientiProdottoFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestioneIngredientiProdottoFrameListener implements ActionListener {

    public static final String BTN_ADD = "BTN_ADD";
    public static final String BTN_REMOVE = "BTN_REMOVE";
    public static final String BTN_CONFERMA_SALVA = "BTN_CONFERMA_SALVA";

    private GestioneIngredientiProdottoFrame frame;

    public GestioneIngredientiProdottoFrameListener(GestioneIngredientiProdottoFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComboBox) {
            String tipologiaIngrediente = frame.getTipologiaIngredienteSelezionata();
            if ("-".equals(tipologiaIngrediente))
                frame.initComponents(null);
            else
                frame.initComponents(tipologiaIngrediente);
        } else if (e.getSource() instanceof JButton) {
            switch (e.getActionCommand()) {
                case BTN_ADD -> {
                    Integer idIngrediente = frame.getLeftTableRowSelected();
                    if (idIngrediente != null) {
                        if (frame.getIdIngredientiSelezionati().contains(idIngrediente)) {
                            JOptionPane.showMessageDialog(frame, "Hai già inserito questo ingrediente");
                            return;
                        }

                        frame.getIdIngredientiSelezionati().add(idIngrediente);
                        frame.initComponents(null);
                    }
                }
                case BTN_REMOVE -> {
                    Integer idIngrediente = frame.getRightTableRowSelected();

                    frame.getIdIngredientiSelezionati().remove(idIngrediente);
                    frame.initComponents(null);
                }
                case BTN_CONFERMA_SALVA -> frame.dispose();
            }
        }
    }
}