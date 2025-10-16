package it.unisalento.myfood.View.Decorator.Form;

import it.unisalento.myfood.Listener.SignUp.ClienteSignUpListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class FormClienteDecorator extends FormDecorator {  //registrazione autonoma cliente

    private JTextField residenza;

    public FormClienteDecorator(Form form) {
        this.form = form;

        this.form.getTextFields().get("7. Residenza").setVisible(true);

        textFields.putAll(this.form.getTextFields());

    }

    @Override
    public TreeMap<String, JTextField> getTextFields() {
        return textFields;
    }

    public ArrayList<JButton> getButtons(){
        buttons.addAll(this.form.getButtons());

        ((FormUtente) form).getSignUp().setActionCommand(ClienteSignUpListener.SIGNUP_BTN);
        ((FormUtente) form).getCancel().setActionCommand(ClienteSignUpListener.CANCEL_BTN);

        return buttons;
    }




}
