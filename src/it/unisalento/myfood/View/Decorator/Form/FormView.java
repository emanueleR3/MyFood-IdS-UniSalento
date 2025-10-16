package it.unisalento.myfood.View.Decorator.Form;

import javax.swing.*;

public class FormView extends Form{
    public static final String EXIT_BUTT = "EXIT_BUTT";

    private JButton esci = new JButton("Torna alla lista");


    public FormView() {
        esci.setActionCommand(EXIT_BUTT);
        buttons.add(esci);

    }


}
