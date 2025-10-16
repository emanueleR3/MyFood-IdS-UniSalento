package it.unisalento.myfood.Listener.Edit;

import it.unisalento.myfood.View.Panels.Edit.EditUtentePanel;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.SignUp.ClienteSignUpListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditUtenteListener implements ActionListener {
    private EditUtentePanel editUtentePanel;
    private Frame frame;

    public EditUtenteListener(EditUtentePanel editUtentePanel, Frame frame) {
        this.editUtentePanel = editUtentePanel;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case ClienteSignUpListener.SIGNUP_BTN -> frame.showPanel(Frame.PANEL.GESTIONE_UTENTI);
            case ClienteSignUpListener.CANCEL_BTN -> frame.showPanel(Frame.PANEL.GESTIONE_UTENTI);
        }
    }
}
