package it.unisalento.myfood.Listener.Gestione;

import it.unisalento.myfood.View.GestioneProduttoreIngredienteFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestioneProduttoreIngredienteFrameListener implements ActionListener {

    public static final String BTN_ADD = "BTN_ADD";
    public static final String BTN_REMOVE = "BTN_REMOVE";
    public static final String BTN_CONFERMA_SALVA = "BTN_CONFERMA_SALVA";

    private GestioneProduttoreIngredienteFrame frame;

    public GestioneProduttoreIngredienteFrameListener(GestioneProduttoreIngredienteFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case BTN_ADD -> {
                    Integer idAzienda = frame.getLeftTableRowSelected();
                    if (idAzienda != null) {

                        frame.setIdProduttoreSelezionato(idAzienda);
                        frame.initComponents();
                    }
                }
                case BTN_REMOVE -> {

                    frame.setIdProduttoreSelezionato(null);
                    frame.initComponents();
                }
                case BTN_CONFERMA_SALVA -> frame.dispose();
            }
    }
}