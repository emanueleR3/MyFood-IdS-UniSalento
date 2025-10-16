package it.unisalento.myfood.model.Result;

public class OperationAziendaResult implements IResult {

    public enum EDIT_AZIENDA_RESULT {
        ADDED_SUCCESSFULLY,
        REMOVED_SUCCESSFULLY,
        EDITED_SUCCESSFULLY,
        ERROR
    }

    private EDIT_AZIENDA_RESULT editAziendaResult;
    private String message;

    public EDIT_AZIENDA_RESULT getEditAziendaResult() {
        return editAziendaResult;
    }

    public void setEditAziendaResult(EDIT_AZIENDA_RESULT editAziendaResult) {
        this.editAziendaResult = editAziendaResult;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
