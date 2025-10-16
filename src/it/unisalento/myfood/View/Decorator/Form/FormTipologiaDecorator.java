package it.unisalento.myfood.View.Decorator.Form;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class FormTipologiaDecorator extends FormDecorator{

    private JTextField nome;



    public FormTipologiaDecorator(Form form) {
        this.form = form;
        nome = new JTextField(20);
    }

    public ArrayList<JButton> getButtons(){
        buttons.addAll(this.form.getButtons());

        return buttons;
    }

    @Override
    public TreeMap<String, JTextField> getTextFields() {
        textFields.putAll(this.form.getTextFields());

        textFields.put("Nome", nome);
        return textFields;

    }

}
