package it.unisalento.myfood;

import it.unisalento.myfood.Business.InvalidFormatException;
import it.unisalento.myfood.Business.UtenteBusiness;

import it.unisalento.myfood.Test.PopolaDatabase;
import it.unisalento.myfood.View.Frame;

public class Main {
    public static void main(String[] args) throws InvalidFormatException {
        executeApplication();
    }

    private static void executeApplication() throws InvalidFormatException {
        UtenteBusiness.getInstance().signUpCliente(UtenteBusiness.EMAIL_CLIENTE_UNIVERSALE, "Cliente", "Universale", "111", "01-01-2000", "Disoccupato", "Nel Database", true);
        PopolaDatabase.creaAmministratore();

        new Frame();
    }

    static {
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
    }
}