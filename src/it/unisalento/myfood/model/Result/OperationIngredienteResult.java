package it.unisalento.myfood.model.Result;


import it.unisalento.myfood.model.Ingrediente;

public class OperationIngredienteResult implements IResult {

    public enum EDIT_INGREDIENTE_RESULT {
        ADDED_SUCCESSFULLY,
        REMOVED_SUCCESSFULLY,
        EDITED_SUCCESSFFULLY,
        ERROR
    }

    private EDIT_INGREDIENTE_RESULT editIngredienteResult;
    private String message;

    private Ingrediente ingrediente;

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public void setEditIngredienteResult(EDIT_INGREDIENTE_RESULT editIngredienteResult) {
        this.editIngredienteResult = editIngredienteResult;
    }

    public EDIT_INGREDIENTE_RESULT getEditIngredienteResult() {
        return editIngredienteResult;
    }
}