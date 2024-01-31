package it.unisalento.myfood.View.Decorator.PulsantieraDashboard;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class PulsantieraDashboard {

    List<JButton> pulsanti = new ArrayList<>();

    public List<JButton> getPulsanti() {
        return pulsanti;
    }
}
