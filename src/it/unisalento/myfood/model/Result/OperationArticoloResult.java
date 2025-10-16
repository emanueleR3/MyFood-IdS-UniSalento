package it.unisalento.myfood.model.Result;

public class OperationArticoloResult implements IResult {

    public enum EDIT_ARTICOLO_RESULT {
        ADDED_SUCCESSFULLY,
        REMOVED_SUCCESSFULLY,
        EDITED_SUCCESSFULLY,
        ADDED_TO_MENU_SUCCESSFULLY,
        REMOVED_FROM_MENU_SUCCESSFULLY,
        INGREDIENTE_ADDED_SUCCESSFULLY,
        INGREDIENTE_REMOVED_SUCCESSFULLY,
        ERROR
    }

    private EDIT_ARTICOLO_RESULT editArticoloResult;
    private String message;

    public EDIT_ARTICOLO_RESULT getEditArticoloResult() {
        return editArticoloResult;
    }

    public void setEditArticoloResult(EDIT_ARTICOLO_RESULT editArticoloResult) {
        this.editArticoloResult = editArticoloResult;
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
