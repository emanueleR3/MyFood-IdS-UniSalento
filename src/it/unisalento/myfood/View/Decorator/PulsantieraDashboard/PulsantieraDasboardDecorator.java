package it.unisalento.myfood.View.Decorator.PulsantieraDashboard;

import javax.swing.*;
import java.util.List;

public class PulsantieraDasboardDecorator extends PulsantieraDashboard {

    PulsantieraDashboard pulsantiera;

    public List<JButton> getPulsanti(){
        return this.pulsantiera.getPulsanti();
    }
}