package it.unisalento.myfood.Listener;

import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Panels.ChangePasswordPanel;
import it.unisalento.myfood.View.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePasswordListener implements ActionListener {

    private ChangePasswordPanel changePasswordPanel;
    private Frame frame;

    public final static String CHANGE_PASSWORD_BTN = "CHANGE_PASSWORD_BTN";

    private UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();

    public ChangePasswordListener(ChangePasswordPanel changePasswordPanel, Frame frame) {
        this.changePasswordPanel = changePasswordPanel;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case CHANGE_PASSWORD_BTN -> changePassword(changePasswordPanel.getPassword());
        }
    }

    private void changePassword(String password) {
        boolean done = utenteBusiness.changePasswordForLoggedUser(password);
        if (done) {
            utenteBusiness.setChangePasswordForLoggedUser(false);
            if (utenteBusiness.isLoggedCliente())
                frame.showPanel(Frame.PANEL.DASHBOARD_CLIENTE);
            else if (utenteBusiness.isLoggedCucina())
                frame.showPanel(Frame.PANEL.DASHBOARD_CUCINA);
            else if (utenteBusiness.isLoggedAmministratore())
                frame.showPanel(Frame.PANEL.DASHBOARD_ADMIN);
        } else {
            JOptionPane.showMessageDialog(frame, "ERRORE! La password non è stata cambiata.");
        }
    }
}
