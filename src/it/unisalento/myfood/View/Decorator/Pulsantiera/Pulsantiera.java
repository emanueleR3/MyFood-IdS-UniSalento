package it.unisalento.myfood.View.Decorator.Pulsantiera;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Pulsantiera {

    public List<JButton> pulsanti = new ArrayList<>();

    public List<JButton> getPulsanti() {
        return pulsanti;
    }
}
