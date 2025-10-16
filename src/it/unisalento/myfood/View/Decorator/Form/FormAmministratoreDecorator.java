package it.unisalento.myfood.View.Decorator.Form;

import it.unisalento.myfood.Listener.SignUp.ClienteSignUpListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class FormAmministratoreDecorator extends FormDecorator{
    public static String CUCINA = "CUCINA";
    public static String AMMINISTRATORE = "AMMINISTRATORE";
    public static String CLIENTE = "CLIENTE";

    private ButtonGroup group;


    public FormAmministratoreDecorator(Form form) {    //registrazione utente generico: cliente, cucina, amministratore
        this.form = form;
        JRadioButton radioButton1 = new JRadioButton("Cliente");
        radioButton1.setActionCommand(CLIENTE);
        JRadioButton radioButton2 = new JRadioButton("Cucina");
        radioButton2.setActionCommand(CUCINA);
        JRadioButton radioButton3 = new JRadioButton("Amministratore");
        radioButton3.setActionCommand(AMMINISTRATORE);

        group = new ButtonGroup();
        group.add(radioButton1);
        group.add(radioButton2);
        group.add(radioButton3);

        radioButtons.add(radioButton1);
        radioButtons.add(radioButton2);
        radioButtons.add(radioButton3);
    }

    @Override
    public TreeMap<String, JTextField> getTextFields() {
        textFields.putAll(this.form.getTextFields());

        this.form.getTextFields().get("7. Residenza").setVisible(false);

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

        ((FormUtente) form).getSignUp().setActionCommand(ClienteSignUpListener.SIGNUP_BTN);
        ((FormUtente) form).getCancel().setActionCommand(ClienteSignUpListener.CANCEL_BTN);

        return buttons;
    }

}
