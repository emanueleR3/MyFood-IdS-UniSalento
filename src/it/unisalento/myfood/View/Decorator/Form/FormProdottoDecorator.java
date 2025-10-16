package it.unisalento.myfood.View.Decorator.Form;

import javax.swing.*;
import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.TreeMap;

public class FormProdottoDecorator extends FormDecorator{

    private JTextField nome;
    private JTextField descrizione;
    private JTextField prezzo;
    private JTextField disponibilita;
    private String ADD_ING = "ADD_ING";



    public FormProdottoDecorator(Form form) {
        this.form = form;

        nome = new JTextField(20);
        descrizione = new JTextField(20);
        prezzo = new JTextField(20);
        disponibilita = new JTextField(20);
    }

    public ArrayList<JButton> getButtons(){
        buttons.addAll(this.form.getButtons());


        JButton button = new JButton("Aggiungi Ingrediente");
        button.setActionCommand(ADD_ING);

        buttons.add(button);

        return buttons;
    }

    @Override
    public TreeMap<String, JTextField> getTextFields() {
        textFields.putAll(this.form.getTextFields());

        textFields.put("Nome", nome);
        textFields.put("Descrizione", descrizione);
        textFields.put("Prezzo", prezzo);
        textFields.put("Disponibilita", disponibilita);
        return textFields;

    }

}
