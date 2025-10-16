package it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione;

import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;

import javax.swing.*;
import java.util.List;

public class PulsantieraGestioneDecorator extends Pulsantiera {
    Pulsantiera pulsantiera;

    public List<JButton> getPulsanti(){
        return this.pulsantiera.getPulsanti();
    }

}
