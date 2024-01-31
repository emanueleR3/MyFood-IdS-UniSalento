package it.unisalento.myfood.View.Listener;

import it.unisalento.myfood.Business.InvalidFormatException;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormClienteDecorator;
import it.unisalento.myfood.View.Frame;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class ClienteSignUpListener implements ActionListener {

    private Frame frame;
    private FormClienteDecorator decForm;
    private TreeMap<String, JTextField> fields;

    public final static String CANCEL_BTN = "ABOUT_BTN";
    public final static String SIGNUP_BTN = "SIGNUP_BTN";


    public ClienteSignUpListener(FormClienteDecorator form, Frame frame) {
        this.frame = frame;
        this.decForm = form;
        fields = decForm.getTextFields();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case SIGNUP_BTN -> signUpRequested(fields.get("3. Email").getText(), fields.get("1. Nome").getText(), fields.get("2. Cognome").getText(), fields.get("4. Telefono").getText(), fields.get("5. Data di nascita").getText(), fields.get("6. Professione").getText(), fields.get("7. Residenza").getText());
            case CANCEL_BTN -> frame.showPanel(Frame.PANEL.LOGIN);
        }
    }
    private void signUpRequested(String email, String nome, String cognome, String telefono, String dataDiNascita, String professione, String residenza) {

        try {
            UtenteBusiness.getInstance().signUpCliente(email, nome, cognome, telefono, dataDiNascita, professione, residenza);
            if(UtenteBusiness.isSignedUpSuccessfully()) {
                JOptionPane.showMessageDialog(frame, "Utente registrato correttamente");
                frame.showPanel(Frame.PANEL.LOGIN);
            }
                    else if(UtenteBusiness.isUserAlreadyExit())
                    JOptionPane.showMessageDialog(frame, "Questa mail è già associata ad un account");
                else
                    JOptionPane.showMessageDialog(frame, "Si è verificato un errore, controlla i tuoi dati");
        } catch (InvalidFormatException e){
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }

    }


}
