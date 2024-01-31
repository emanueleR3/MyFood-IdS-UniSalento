package it.unisalento.myfood.model.Composite;

import it.unisalento.myfood.model.Utente;

import java.sql.Timestamp;
import java.util.Date;

public interface IInterazioneUtente {

    enum INDICE_GRADIMENTO{ //per ragioni di sicurezza
        UNO,
        DUE,
        TRE,
        QUATTRO,
        CINQUE
    }

    Integer getId();

    String getTesto();

    Utente getUtente();

    Timestamp getDataEOra();
}
