package it.unisalento.myfood.model.Result;

public class OperationInterazioneResult implements IResult {

    public enum EDIT_INTERAZIONE_RESULT {
        CREATED_SUCCESSFULLY,
        REMOVED_SUCCESSFULLY,
        ERROR
    }

    private EDIT_INTERAZIONE_RESULT editInterazioneResult;
    private String message;


    public EDIT_INTERAZIONE_RESULT getEditInterazioneResult() {
        return editInterazioneResult;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setEditInterazioneResult(EDIT_INTERAZIONE_RESULT editInterazioneResult) {
        this.editInterazioneResult = editInterazioneResult;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}