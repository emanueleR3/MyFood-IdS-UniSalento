package it.unisalento.myfood.Listener.Gestione;

import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.GestioneArticoliMenuFrame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowListenerFrame extends WindowAdapter {

    private JFrame frame;

    public WindowListenerFrame(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (e.getSource() instanceof GestioneArticoliMenuFrame) {
            if (!UtenteBusiness.OPERATIONS.VISUALIZZA.equals(UtenteBusiness.getSession().get(UtenteBusiness.OPERATION))) {
                int choice = JOptionPane.showConfirmDialog(frame, "ATTENZIONE! Chiudendo senza salvare il menù verrà svuotato. Vuoi continuare?", "Uscire senza salvare", JOptionPane.YES_NO_CANCEL_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    ((GestioneArticoliMenuFrame) frame).getIdArticoliSelezionati().clear();
                    ((GestioneArticoliMenuFrame) frame).setEnableComboBoxComposizioni(true);
                } else {
                    return;
                }
            }
        }

        frame.dispose();
    }
}