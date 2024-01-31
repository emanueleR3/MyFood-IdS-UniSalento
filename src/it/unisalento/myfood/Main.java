package it.unisalento.myfood;

import it.unisalento.myfood.Test.PopolaDatabase;
import it.unisalento.myfood.View.Frame;

public class Main {
    public static void main(String[] args){

        //UtenteDAO.getInstance().removeByEmail("emanuele.romano2@studenti.unisalento.it");
        PopolaDatabase.creaCliente();
        PopolaDatabase.creaCucina();
        PopolaDatabase.creaAmministratore();

        new Frame();
    }
}

//decorator per la pulsantiera
//decorator per i form di registrazione

