package it.unisalento.myfood.View.Decorator.Form;

import javax.swing.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class FormIngredienteDecorator extends FormDecorator{

    private JTextField nome;
    public static final String ADD_DISTR= "ADD_DISTR";
    public static final String ADD_PROD= "ADD_PROD";


    public FormIngredienteDecorator(Form form) {
        this.form = form;
        nome = new JTextField(20);
    }

    public ArrayList<JButton> getButtons(){
        buttons.addAll(this.form.getButtons());

        JButton prodButt = new JButton("Seleziona Produttore");
        prodButt.setActionCommand(ADD_PROD);
        JButton distributoreButt = new JButton("Aggiungi Distributore");
        distributoreButt.setActionCommand(ADD_DISTR);

        buttons.add(prodButt);
        buttons.add(distributoreButt);
        return buttons;
    }

    @Override
    public TreeMap<String, JTextField> getTextFields() {
        textFields.putAll(this.form.getTextFields());

        textFields.put("Nome", nome);
        return textFields;

    }

}
