package it.unisalento.myfood.model.Result;

import it.unisalento.myfood.model.Carrello;

public class OperationCarrelloResult implements IResult {

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

    @Override
    public String getMessage() {
        return message;
    }

    @Override
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
