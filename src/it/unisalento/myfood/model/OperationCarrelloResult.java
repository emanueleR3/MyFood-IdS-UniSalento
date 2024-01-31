package it.unisalento.myfood.model;

import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.IResult;

import java.util.ArrayList;

public class OperationCarrelloResult implements IResult {

    // Per gestire errori eventuali del carrello, in modo da avvisare il Cliente
    // (esempio --> salta la connessione al db e non aggiunge l'articolo al carrello e avvisiamo l'utente con un messaggio)
    // TODO: serve distinguere i casi di errore? (NOT_ADDED e NOT_REMOVED) Forse no. Però se dovesse servire ricordiamoci che si può fare
    public enum EDIT_CART_RESULT {
        ADDED_SUCCESSFULLY,     // Aggiunta di prodotti avvenuta
        REMOVED_SUCCESSFULLY,   // Rimozione di un prodotto avvenuta
        EMPTIED_SUCCESSFFULLY,   // Svuotamento del carrello avvenuto
        ERROR       // errore
    }

    private EDIT_CART_RESULT editCartResult;
    private String message;

    private Carrello carrello;

    public EDIT_CART_RESULT getEditCartResult() {
        return editCartResult;
    }

    public void setEditCartResult(EDIT_CART_RESULT editCartResult) {
        this.editCartResult = editCartResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Carrello getCarrello() {
        return carrello;
    }

    public void setCarrello(Carrello carrello) {
        this.carrello = carrello;
    }
}
