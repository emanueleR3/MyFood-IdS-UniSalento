package it.unisalento.myfood.Business.Command.TipologiaProdotto;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.TipologiaProdottoDAO;
import it.unisalento.myfood.model.Result.OperationTipologiaProdottoResult;
import it.unisalento.myfood.model.TipologiaProdotto;

public class RemoveTipologiaProdotto implements ITipoloagiaProdottoOperation {
    private Integer idTipologia;
    private TipologiaProdotto tipologiaProdotto;
    private TipologiaProdottoDAO tipologiaProdottoDAO = TipologiaProdottoDAO.getInstance();
    private IResultFactory resultFactory = new ResultFactory("OperationTipologiaProdotto");

    public RemoveTipologiaProdotto(Integer idTipologia) {
        this.idTipologia = idTipologia;
        tipologiaProdotto = tipologiaProdottoDAO.findTipologiaById(idTipologia);
    }

    @Override
    public OperationTipologiaProdottoResult execute() {
        OperationTipologiaProdottoResult editTipologiaProdottoResult = (OperationTipologiaProdottoResult) resultFactory.crea();

        boolean done = tipologiaProdottoDAO.removeTipologia(tipologiaProdotto.getId());


        if (!done) {
            editTipologiaProdottoResult.setEditTipologiaProdottoResult(OperationTipologiaProdottoResult.EDIT_TIPOLOGIA_PRODOTTO_RESULT.ERROR);
            editTipologiaProdottoResult.setMessage("ERRORE: La tipologia non è stata rimossa!");

            return editTipologiaProdottoResult;
        }

        editTipologiaProdottoResult.setEditTipologiaProdottoResult(OperationTipologiaProdottoResult.EDIT_TIPOLOGIA_PRODOTTO_RESULT.REMOVED_SUCCESSFULLY);
        editTipologiaProdottoResult.setMessage("La tipologia è stata rimossa correttamente!");

        return editTipologiaProdottoResult;
    }

    @Override
    public OperationTipologiaProdottoResult undo() {
        OperationTipologiaProdottoResult editTipologiaProdottoResult = (OperationTipologiaProdottoResult) resultFactory.crea();

        boolean done = tipologiaProdottoDAO.addTipologia(tipologiaProdotto.getNome());
        tipologiaProdotto = tipologiaProdottoDAO.findTipologiaByName(tipologiaProdotto.getNome());
        if (!done) {
            editTipologiaProdottoResult.setEditTipologiaProdottoResult(OperationTipologiaProdottoResult.EDIT_TIPOLOGIA_PRODOTTO_RESULT.ERROR);
            editTipologiaProdottoResult.setMessage("ERRORE: La tipologia non è stata aggiunta!");

            return editTipologiaProdottoResult;
        }

        editTipologiaProdottoResult.setEditTipologiaProdottoResult(OperationTipologiaProdottoResult.EDIT_TIPOLOGIA_PRODOTTO_RESULT.ADDED_SUCCESSFULLY);
        editTipologiaProdottoResult.setMessage("La tipologia è stata aggiunta correttamente!");

        return editTipologiaProdottoResult;
    }


}
