package it.unisalento.myfood.View.Decorator.Form;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class FormRispostaDecorator extends FormDecorator{

    private JTextField testo;



    public FormRispostaDecorator(Form form) {
        this.form = form;
        testo = new JTextField(20);
    }

    public ArrayList<JButton> getButtons(){
        buttons.addAll(this.form.getButtons());

        return buttons;
    }

    @Override
    public TreeMap<String, JTextField> getTextFields() {
        textFields.putAll(this.form.getTextFields());

        textFields.put("Testo", testo);
        return textFields;

    }

}
