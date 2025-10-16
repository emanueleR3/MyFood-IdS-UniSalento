package it.unisalento.myfood.model.Result;

public class OperationTipologiaProdottoResult implements IResult {

    public enum EDIT_TIPOLOGIA_PRODOTTO_RESULT {
        ADDED_SUCCESSFULLY,
        REMOVED_SUCCESSFULLY,
        EDITED_SUCCESSFULLY,
        ERROR
    }

    private EDIT_TIPOLOGIA_PRODOTTO_RESULT editTipologiaProdottoResult;
    private String message;

    public EDIT_TIPOLOGIA_PRODOTTO_RESULT getEditTipologiaProdottoResult() {
        return editTipologiaProdottoResult;
    }

    public void setEditTipologiaProdottoResult(EDIT_TIPOLOGIA_PRODOTTO_RESULT editTipologiaProdottoResult) {
        this.editTipologiaProdottoResult = editTipologiaProdottoResult;
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
