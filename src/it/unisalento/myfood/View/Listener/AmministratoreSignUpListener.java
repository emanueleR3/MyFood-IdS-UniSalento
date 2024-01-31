package it.unisalento.myfood.View.Listener;

import it.unisalento.myfood.Business.InvalidFormatException;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormPersonaleDecorator;
import it.unisalento.myfood.View.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class AmministratoreSignUpListener implements ActionListener {
//TODO: se c'è tempo impplementare decorator

    private Frame frame;
    private FormPersonaleDecorator form;
    private TreeMap<String, JTextField> fields;

    public final static String CANCEL_BTN = "ABOUT_BTN";
    public final static String SIGNUP_BTN = "SIGNUP_BTN";


    public AmministratoreSignUpListener(FormPersonaleDecorator form, Frame frame) {
        this.frame = frame;
        this.form = form;
        this.fields = form.getTextFields();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case SIGNUP_BTN :
               if(FormPersonaleDecorator.CUCINA.equals(form.getRuolo().getActionCommand()))
                    CucinaSignUpRequested(fields.get("1. Email").getText(), fields.get("3. Nome").getText(), fields.get("2. Cognome").getText(), fields.get("4. Telefono").getText(), fields.get("4. Data di nascita").getText(), fields.get("Professione").getText());
                else if(FormPersonaleDecorator.AMMINISTRATORE.equals(form.getRuolo().getActionCommand()))
                   AmministratoreSignUpRequested(fields.get("1. Email").getText(), fields.get("3. Nome").getText(), fields.get("2. Cognome").getText(), fields.get("4. Telefono").getText(), fields.get("4. Data di nascita").getText(), fields.get("Professione").getText());
            case CANCEL_BTN : frame.showPanel(Frame.PANEL.LOGIN);
        }
    }
    private void CucinaSignUpRequested(String email, String nome, String cognome, String telefono, String dataDiNascita, String professione) {
        try {
             UtenteBusiness.getInstance().signUpCucina(email, nome, cognome, telefono, dataDiNascita, professione);
            if(UtenteBusiness.isSignedUpSuccessfully()) {
                JOptionPane.showMessageDialog(frame, "Utente registrato correttamente");
                frame.showPanel(Frame.PANEL.LOGIN);
            }
            if(UtenteBusiness.isUserAlreadyExit()) {
                JOptionPane.showMessageDialog(frame, "Questa mail è già associata ad un account");
            } else
                    JOptionPane.showMessageDialog(frame, "Si è verificato un errore, controlla i dati");
        } catch (InvalidFormatException e){
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }
    }

    private void AmministratoreSignUpRequested(String email, String nome, String cognome, String telefono, String dataDiNascita, String professione) {
        try {
            UtenteBusiness.getInstance().signUpAmministratore(email, nome, cognome, telefono, dataDiNascita, professione);
            if(UtenteBusiness.isSignedUpSuccessfully()) {
                JOptionPane.showMessageDialog(frame, "Utente registrato correttamente");
                frame.showPanel(Frame.PANEL.LOGIN);
            }
            if(UtenteBusiness.isUserAlreadyExit()) {
                JOptionPane.showMessageDialog(frame, "Questa mail è già associata ad un account");
            } else
                JOptionPane.showMessageDialog(frame, "Si è verificato un errore, controlla i dati");
        } catch (InvalidFormatException e){
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }
    }


}
