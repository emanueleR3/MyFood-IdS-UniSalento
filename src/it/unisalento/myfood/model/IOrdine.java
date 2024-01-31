package it.unisalento.myfood.model;

public interface IOrdine {
    enum STATO {
        NON_PAGATO,
        PAGATO,
        IN_LAVORAZIONE,
        CONSEGNATO
    }
}
