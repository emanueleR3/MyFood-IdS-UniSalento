package it.unisalento.myfood.View.Listener;

import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.LoginPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginListener implements ActionListener {

    private LoginPanel loginPanel;
    private Frame frame;

    public final static String LOGIN_BTN = "LOGIN_BTN";
    public final static String ABOUT_BTN = "ABOUT_BTN";
    public final static String SIGNUP_BTN = "SIGNUP_BTN";
    public final static String GUEST_BTN = "GUEST_BTN";

    // TODO rimuovere
    public final static String CLIENTE_TEMPORANEO = "CLIENTE_TEMPORANEO";
    // ----------------------------

    private UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();


    public LoginListener(LoginPanel loginPanel, Frame frame) {
        this.loginPanel = loginPanel;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case LOGIN_BTN :
                loginRequested(loginPanel.getEmail(), loginPanel.getPassword());
                break;
            case ABOUT_BTN :
                JOptionPane.showMessageDialog(frame, "Software creato da Emanuele Romano e Andrea Barone, 2023");
                break;
            case SIGNUP_BTN :
                frame.showPanel(Frame.PANEL.SIGNUP);
                break;
            case GUEST_BTN:
                utenteBusiness.login();
                frame.showPanel(Frame.PANEL.DASHBOARD_CLIENTE);
                break;
            // TODO RIMUOVERE
            case CLIENTE_TEMPORANEO:
                //loginRequested("emanuele.romano.03@gmail.com", "prova");
                loginRequested("emanuele.romano2@studenti.unisalento.it", "prova");
                break;
            // ----------------------------
        }

        // non va bene in questo caso perchè sono entrambi JButton e quindi non è un discriminante
        //if(e.getSource() instanceof JButton) {
        //    System.out.println(loginPanel.getEmail() + " - " + loginPanel.getPassword());
        //}
    }

    private void loginRequested(String email, String password) {
        System.out.println(loginPanel.getEmail() + " sta provando ad effettuare l'accesso");

        utenteBusiness.login(email, password);

        if (UtenteBusiness.isLoginOk()) {
            if (utenteBusiness.isFirstAccess(email)) {
                frame.showPanel(Frame.PANEL.CHANGE_PASSWORD);
            } else if (utenteBusiness.isLoggedCliente()) {
                frame.showPanel(Frame.PANEL.DASHBOARD_CLIENTE);
            } else if (utenteBusiness.isLoggedAmministratore()) {
                frame.showPanel(Frame.PANEL.DASHBOARD_ADMIN);
            } else if (utenteBusiness.isLoggedCucina()) {
                frame.showPanel(Frame.PANEL.DASHBOARD_CUCINA);
            }
        }
        else {
            JOptionPane.showMessageDialog(frame, UtenteBusiness.getMessage());
        }
    }
}
