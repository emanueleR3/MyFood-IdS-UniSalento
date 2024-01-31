package it.unisalento.myfood.View.Decorator.Form;

import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.Listener.ClienteSignUpListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class FormClienteDecorator extends FormDecorator {  //viene mostrato quando si registra il guest

    private JTextField residenza;
    private Frame frame;

    public FormClienteDecorator(Form form, Frame frame) {
        this.form = form;
        this.frame = frame;

        textFields.putAll(this.form.getTextFields());

        residenza = new JTextField();
        textFields.put("7. Residenza", residenza);
    }

    @Override
    public TreeMap<String, JTextField> getTextFields() {
        return textFields;
    }

    public ArrayList<JButton> getButtons(){
        buttons.addAll(this.form.getButtons());

/*
        ((FormUtente) form).getSignUp().addActionListener(signUpListener);
        ((FormUtente) form).getCancel().addActionListener(signUpListener);*/
        ((FormUtente) form).getSignUp().setActionCommand(ClienteSignUpListener.SIGNUP_BTN);
        ((FormUtente) form).getCancel().setActionCommand(ClienteSignUpListener.CANCEL_BTN);

        return buttons;
    }




}
