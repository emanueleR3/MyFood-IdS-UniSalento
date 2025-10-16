package it.unisalento.myfood.View.Decorator.Form;

import javax.swing.*;

public class FormEdit extends Form{
    public static final String MOD_BUTT = "MOD_BUTT";
    public static final String ANN_BUTT = "ANN_BUTT";

    private JButton modifica = new JButton("Modifica");
    private JButton annulla = new JButton("Annulla");

    public FormEdit() {
        modifica.setActionCommand(MOD_BUTT);
        buttons.add(modifica);
        annulla.setActionCommand(ANN_BUTT);
        buttons.add(annulla);
    }


}
