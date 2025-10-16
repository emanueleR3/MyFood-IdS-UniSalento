package it.unisalento.myfood.model.Result;

import it.unisalento.myfood.model.Utente;

public class OperationUtenteResult implements IResult {

    public enum EDIT_UTENTE_RESULT {
        CREATED_SUCCESSFULLY,   // Utente creato
        REMOVED_SUCCESSFULLY,   // Utente rimosso
        EDITED_SUCCESSFULLY,     // Utente modificato
        ERROR                   // errore
    }

    private EDIT_UTENTE_RESULT editUtenteResult;
    private String message;

    private Utente utente;

    public EDIT_UTENTE_RESULT getEditUtenteResult() {
        return editUtenteResult;
    }

    public void setEditUtenteResult(EDIT_UTENTE_RESULT editUtenteResult) {
        this.editUtenteResult = editUtenteResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}