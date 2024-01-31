package it.unisalento.myfood.model;

public class OperationOrdineResult implements IResult {

    // Per gestire errori eventuali dell'ordine, in modo da avvisare il Cliente
    // (esempio --> salta la connessione al db e non crea l'ordine e avvisiamo l'utente con un messaggio)
    // TODO: serve distinguere i casi di errore? (NOT_ADDED e NOT_REMOVED) Forse no. Però se dovesse servire ricordiamoci che si può fare
    public enum EDIT_ORDINE_RESULT {
        CREATED_SUCCESSFULLY,   // Ordine creato
        REMOVED_SUCCESSFULLY,   // Ordine rimosso
        EDITED_SUCCESSFULLY,     // Ordine modificato
        ERROR                   // errore
    }

    private EDIT_ORDINE_RESULT editOrdineResult;
    private String message;

    private Ordine ordine;

    public EDIT_ORDINE_RESULT getEditOrdineResult() {
        return editOrdineResult;
    }

    public void setEditOrdineResult(EDIT_ORDINE_RESULT editOrdineResult) {
        this.editOrdineResult = editOrdineResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }

    public Integer getIdOrdine() {
        return ordine.getIdOrdine();
    }
}
