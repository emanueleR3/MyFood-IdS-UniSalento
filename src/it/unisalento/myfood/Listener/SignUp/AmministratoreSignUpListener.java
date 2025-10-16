package it.unisalento.myfood.Listener.SignUp;

import it.unisalento.myfood.Business.InvalidFormatException;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormAmministratoreDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.Panels.SignUpPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class AmministratoreSignUpListener implements ActionListener {

    private Frame frame;
    private FormAmministratoreDecorator form;
    private TreeMap<String, JTextField> fields;
    private SignUpPanel signUpPanel;

    public final static String CANCEL_BTN = "ABOUT_BTN";
    public final static String SIGNUP_BTN = "SIGNUP_BTN";
    private UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();


    public AmministratoreSignUpListener(FormAmministratoreDecorator form, Frame frame, SignUpPanel signUpPanel) {
        this.frame = frame;
        this.form = form;
        this.fields = form.getTextFields();
        this.signUpPanel = signUpPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() instanceof JRadioButton){
            if(FormAmministratoreDecorator.CLIENTE.equals(form.getRuolo().getActionCommand()))
                signUpPanel.showResidenza();
            if(FormAmministratoreDecorator.CUCINA.equals(form.getRuolo().getActionCommand()))
                signUpPanel.hideResidenza();
            if(FormAmministratoreDecorator.AMMINISTRATORE.equals(form.getRuolo().getActionCommand()))
                signUpPanel.hideResidenza();

        }

        switch (e.getActionCommand()) {

            case SIGNUP_BTN :
                if(FormAmministratoreDecorator.CUCINA.equals(form.getRuolo().getActionCommand()))
                    cucinaSignUpRequested(fields.get("3. Email").getText(), fields.get("1. Nome").getText(), fields.get("2. Cognome").getText(), fields.get("4. Telefono").getText(), fields.get("5. Data di nascita").getText(), fields.get("6. Professione").getText());
                else if(FormAmministratoreDecorator.AMMINISTRATORE.equals(form.getRuolo().getActionCommand()))
                    amministratoreSignUpRequested(fields.get("3. Email").getText(), fields.get("1. Nome").getText(), fields.get("2. Cognome").getText(), fields.get("4. Telefono").getText(), fields.get("5. Data di nascita").getText(), fields.get("6. Professione").getText());
                else if(FormAmministratoreDecorator.CLIENTE.equals(form.getRuolo().getActionCommand()))
                   clienteSignUpRequested(fields.get("3. Email").getText(), fields.get("1. Nome").getText(), fields.get("2. Cognome").getText(), fields.get("4. Telefono").getText(), fields.get("5. Data di nascita").getText(), fields.get("6. Professione").getText(), fields.get("7. Residenza").getText());
                break;
            case CANCEL_BTN : frame.showPanel(Frame.PANEL.GESTIONE_UTENTI);
                break;
        }
    }

    private void clienteSignUpRequested(String email, String nome, String cognome, String telefono, String dataDiNascita, String professione, String residenza) {

        try {
            UtenteBusiness.getInstance().signUpCliente(email, nome, cognome, telefono, dataDiNascita, professione, residenza, false);
            if(utenteBusiness.isSignedUpSuccessfully()) {
                JOptionPane.showMessageDialog(frame, "Utente registrato correttamente,\nriceverai una mail con la password temporanea da modificare al primo accesso");
                frame.showPanel(Frame.PANEL.GESTIONE_UTENTI);
            }
            else if(utenteBusiness.isUserAlreadyExit())
                JOptionPane.showMessageDialog(frame, "Questa mail è già associata ad un account");
            else
                JOptionPane.showMessageDialog(frame, "Si è verificato un errore, controlla i tuoi dati");
        } catch (InvalidFormatException e){
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }

    }

    private void cucinaSignUpRequested(String email, String nome, String cognome, String telefono, String dataDiNascita, String professione) {
        try {
             UtenteBusiness.getInstance().signUpCucina(email, nome, cognome, telefono, dataDiNascita, professione, false);
            if(utenteBusiness.isSignedUpSuccessfully()) {
                JOptionPane.showMessageDialog(frame, "Utente registrato correttamente,\nriceverà una mail con la password temporanea da modificare al primo accesso");
                frame.showPanel(Frame.PANEL.GESTIONE_UTENTI);
            }else if(utenteBusiness.isUserAlreadyExit()) {
                JOptionPane.showMessageDialog(frame, "Questa mail è già associata ad un account");
            } else
                    JOptionPane.showMessageDialog(frame, "Si è verificato un errore, controlla i dati");
        } catch (InvalidFormatException e){
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }
    }

    private void amministratoreSignUpRequested(String email, String nome, String cognome, String telefono, String dataDiNascita, String professione) {

        try {
            UtenteBusiness.getInstance().signUpAmministratore(email, nome, cognome, telefono, dataDiNascita, professione, false);
            if(utenteBusiness.isSignedUpSuccessfully()) {
                JOptionPane.showMessageDialog(frame, "Utente registrato correttamente,\nriceverà una mail con la password temporanea da modificare al primo accesso");
                frame.showPanel(Frame.PANEL.GESTIONE_UTENTI);
            } else if(utenteBusiness.isUserAlreadyExit()) {
                JOptionPane.showMessageDialog(frame, "Questa mail è già associata ad un account");
            } else
                JOptionPane.showMessageDialog(frame, "Si è verificato un errore, controlla i dati");
        } catch (InvalidFormatException e){
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }
    }


}
