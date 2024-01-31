package it.unisalento.myfood.View.Decorator.Form;

import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.Listener.AmministratoreSignUpListener;
import it.unisalento.myfood.View.Listener.ClienteSignUpListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class FormPersonaleDecorator extends FormDecorator{ //viene mostrato quando l'amministratore aggiunge cucina e amministratori

    public static String CUCINA = "CUCINA";
    public static String AMMINISTRATORE = "AMMINISTRATORE";
    private ButtonGroup group;

    private AmministratoreSignUpListener amministratoreSignUpListener;

    public FormPersonaleDecorator(Form form, Frame frame) {
        this.form = form;
        amministratoreSignUpListener = new AmministratoreSignUpListener(this, frame);
        JRadioButton radioButton1 = new JRadioButton("Cucina");
        radioButton1.setActionCommand(CUCINA);
        JRadioButton radioButton2 = new JRadioButton("Amministratore");
        radioButton2.setActionCommand(AMMINISTRATORE);
        group = new ButtonGroup();
        group.add(radioButton1);
        group.add(radioButton2);

        radioButtons.add(radioButton1);
        radioButtons.add(radioButton2);
    }

    @Override
    public TreeMap<String, JTextField> getTextFields() {
        textFields.putAll(this.form.getTextFields());

        return textFields;
    }

    @Override
    public ArrayList<JRadioButton> getRadioButtons() {
        radioButtons.addAll(this.form.getRadioButtons());

        return radioButtons;
    }

    public ButtonModel getRuolo(){
        return group.getSelection();
    }

    public ArrayList<JButton> getButtons(){
        buttons.addAll(this.form.getButtons());

        ((FormUtente) form).getSignUp().addActionListener(amministratoreSignUpListener);
        ((FormUtente) form).getCancel().addActionListener(amministratoreSignUpListener);
        ((FormUtente) form).getSignUp().setActionCommand(ClienteSignUpListener.SIGNUP_BTN);
        ((FormUtente) form).getCancel().setActionCommand(ClienteSignUpListener.CANCEL_BTN);

        return buttons;
    }

}
