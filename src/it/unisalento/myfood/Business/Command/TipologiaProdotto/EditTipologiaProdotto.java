package it.unisalento.myfood.Business.Command.TipologiaProdotto;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.TipologiaProdottoDAO;
import it.unisalento.myfood.model.Result.OperationTipologiaProdottoResult;
import it.unisalento.myfood.model.TipologiaProdotto;

public class EditTipologiaProdotto implements ITipoloagiaProdottoOperation {
    private String newName;
    private TipologiaProdotto tipologiaProdotto;
    private TipologiaProdottoDAO tipologiaProdottoDAO = TipologiaProdottoDAO.getInstance();
    private IResultFactory resultFactory = new ResultFactory("OperationTipologiaProdotto");

    public EditTipologiaProdotto(TipologiaProdotto tipologiaProdotto, String newName) {
        this.newName = newName;
        this.tipologiaProdotto = tipologiaProdotto;
    }

    @Override
    public OperationTipologiaProdottoResult execute() {
        OperationTipologiaProdottoResult editTipologiaProdottoResult = (OperationTipologiaProdottoResult) resultFactory.crea();

        boolean done = tipologiaProdottoDAO.editTipologia(tipologiaProdotto.getId(), newName);
        if (!done) {
            editTipologiaProdottoResult.setEditTipologiaProdottoResult(OperationTipologiaProdottoResult.EDIT_TIPOLOGIA_PRODOTTO_RESULT.ERROR);
            editTipologiaProdottoResult.setMessage("ERRORE: La tipologia non è stata modificata!");

            return editTipologiaProdottoResult;
        }

        editTipologiaProdottoResult.setEditTipologiaProdottoResult(OperationTipologiaProdottoResult.EDIT_TIPOLOGIA_PRODOTTO_RESULT.EDITED_SUCCESSFULLY);
        editTipologiaProdottoResult.setMessage("La tipologia è stata modificata correttamente!");

        return editTipologiaProdottoResult;
    }

    @Override
    public OperationTipologiaProdottoResult undo() {
        OperationTipologiaProdottoResult editTipologiaProdottoResult = (OperationTipologiaProdottoResult) resultFactory.crea();
        boolean done = tipologiaProdottoDAO.editTipologia(tipologiaProdotto.getId(), tipologiaProdotto.getNome());

        if (!done) {
            editTipologiaProdottoResult.setEditTipologiaProdottoResult(OperationTipologiaProdottoResult.EDIT_TIPOLOGIA_PRODOTTO_RESULT.ERROR);
            editTipologiaProdottoResult.setMessage("ERRORE: La tipologia non è stata modificata!");

            return editTipologiaProdottoResult;
        }

        editTipologiaProdottoResult.setEditTipologiaProdottoResult(OperationTipologiaProdottoResult.EDIT_TIPOLOGIA_PRODOTTO_RESULT.EDITED_SUCCESSFULLY);
        editTipologiaProdottoResult.setMessage("La tipologia è stata modificata correttamente!");

        return editTipologiaProdottoResult;
    }
}
