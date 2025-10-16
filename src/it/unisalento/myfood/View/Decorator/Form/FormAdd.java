package it.unisalento.myfood.View.Decorator.Form;

import javax.swing.*;

public class FormAdd extends Form{
    public static final String ADD_BUTT = "ADD_BUTT";
    public static final String ANN_BUTT = "ANN_BUTT";

    private JButton modifica = new JButton("Aggiungi");
    private JButton annulla = new JButton("Annulla");

    public FormAdd() {
        modifica.setActionCommand(ADD_BUTT);
        buttons.add(modifica);
        annulla.setActionCommand(ANN_BUTT);
        buttons.add(annulla);
    }


}
