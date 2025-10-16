package it.unisalento.myfood.View.Decorator.Form;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class FormMenuDecorator extends FormDecorator{


    private JTextField nome;
    private JTextField descrizione;
    private JTextField prezzo;
    private static final String ADD_ARTICOLO_TO_MENU = "ADD_ARTICOLO_TO_MENU";

    public FormMenuDecorator(Form form) {
        this.form = form;

        nome = new JTextField(20);
        descrizione = new JTextField(20);
        prezzo = new JTextField(20);
    }

    public ArrayList<JButton> getButtons(){
        buttons.addAll(this.form.getButtons());

        JButton button = new JButton("Aggiungi Articolo");
        button.setActionCommand(ADD_ARTICOLO_TO_MENU);

        buttons.add(button);

        return buttons;
    }

    @Override
    public TreeMap<String, JTextField> getTextFields() {
        textFields.putAll(this.form.getTextFields());

        textFields.put("Nome", nome);
        textFields.put("Descrizione", descrizione);
        textFields.put("Sconto (0-100 %)", prezzo);
        return textFields;

    }

}
