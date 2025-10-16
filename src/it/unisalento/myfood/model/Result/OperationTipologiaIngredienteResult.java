package it.unisalento.myfood.model.Result;

public class OperationTipologiaIngredienteResult implements IResult {

    public enum EDIT_TIPOLOGIA_INGREDIENTE_RESULT {
        ADDED_SUCCESSFULLY,
        REMOVED_SUCCESSFULLY,
        EDITED_SUCCESSFULLY,
        ERROR
    }

    private EDIT_TIPOLOGIA_INGREDIENTE_RESULT editTipologiaIngredienteResult;
    private String message;

    public EDIT_TIPOLOGIA_INGREDIENTE_RESULT getEditTipologiaIngredienteResult() {
        return editTipologiaIngredienteResult;
    }

    public void setEditTipologiaIngredienteResult(EDIT_TIPOLOGIA_INGREDIENTE_RESULT editTipologiaIngredienteResult) {
        this.editTipologiaIngredienteResult = editTipologiaIngredienteResult;
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
