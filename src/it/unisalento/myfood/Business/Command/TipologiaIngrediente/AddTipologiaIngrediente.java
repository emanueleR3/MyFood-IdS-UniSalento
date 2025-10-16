package it.unisalento.myfood.Business.Command.TipologiaIngrediente;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.ITipologiaIngredienteDAO;
import it.unisalento.myfood.DAO.TipologiaIngredienteDAO;
import it.unisalento.myfood.model.*;
import it.unisalento.myfood.model.Result.OperationTipologiaIngredienteResult;

public class AddTipologiaIngrediente implements ITipoloagiaIngredienteOperation {
    private String nomeTipologia;
    private TipologiaIngrediente tipologiaIngrediente;
    private ITipologiaIngredienteDAO tipologiaIngredienteDAO = TipologiaIngredienteDAO.getInstance();
    private IResultFactory resultFactory = new ResultFactory("OperationTipologiaIngrediente");

    public AddTipologiaIngrediente(String nomeTipologia) {
        this.nomeTipologia = nomeTipologia;
    }

    @Override
    public OperationTipologiaIngredienteResult execute() {
        OperationTipologiaIngredienteResult editTipologiaIngredienteResult = (OperationTipologiaIngredienteResult) resultFactory.crea();

        boolean done = tipologiaIngredienteDAO.addTipologia(nomeTipologia);
        if (!done) {
            editTipologiaIngredienteResult.setEditTipologiaIngredienteResult(OperationTipologiaIngredienteResult.EDIT_TIPOLOGIA_INGREDIENTE_RESULT.ERROR);
            editTipologiaIngredienteResult.setMessage("ERRORE: La tipologia non è stata aggiunta!");

            return editTipologiaIngredienteResult;
        }

        tipologiaIngrediente = tipologiaIngredienteDAO.findTipologiaById(tipologiaIngredienteDAO.getLastInsertId());

        editTipologiaIngredienteResult.setEditTipologiaIngredienteResult(OperationTipologiaIngredienteResult.EDIT_TIPOLOGIA_INGREDIENTE_RESULT.ADDED_SUCCESSFULLY);
        editTipologiaIngredienteResult.setMessage("La tipologia è stata aggiunta correttamente!");

        return editTipologiaIngredienteResult;
    }

    @Override
    public OperationTipologiaIngredienteResult undo() {
        OperationTipologiaIngredienteResult editTipologiaIngredienteResult = (OperationTipologiaIngredienteResult) resultFactory.crea();

        boolean done = tipologiaIngredienteDAO.removeTipologia(tipologiaIngrediente.getId());
        if (!done) {
            editTipologiaIngredienteResult.setEditTipologiaIngredienteResult(OperationTipologiaIngredienteResult.EDIT_TIPOLOGIA_INGREDIENTE_RESULT.ERROR);
            editTipologiaIngredienteResult.setMessage("ERRORE: La tipologia non è stata rimossa!");

            return editTipologiaIngredienteResult;
        }

        editTipologiaIngredienteResult.setEditTipologiaIngredienteResult(OperationTipologiaIngredienteResult.EDIT_TIPOLOGIA_INGREDIENTE_RESULT.REMOVED_SUCCESSFULLY);
        editTipologiaIngredienteResult.setMessage("La tipologia è stata rimossa correttamente!");

        return editTipologiaIngredienteResult;
    }
}
