package it.unisalento.myfood.Business.Command.TipologiaIngrediente;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.ITipologiaIngredienteDAO;
import it.unisalento.myfood.DAO.TipologiaIngredienteDAO;
import it.unisalento.myfood.model.Result.OperationTipologiaIngredienteResult;
import it.unisalento.myfood.model.TipologiaIngrediente;

public class EditTipologiaIngrediente implements ITipoloagiaIngredienteOperation {
    private TipologiaIngrediente tipologiaIngrediente;
    private String newNome;
    private ITipologiaIngredienteDAO tipologiaIngredienteDAO = TipologiaIngredienteDAO.getInstance();
    private IResultFactory resultFactory = new ResultFactory("OperationTipologiaIngrediente");

    public EditTipologiaIngrediente(TipologiaIngrediente tipologiaIngrediente, String newNome) {
        this.tipologiaIngrediente = tipologiaIngrediente;
        this.newNome = newNome;
    }

    @Override
    public OperationTipologiaIngredienteResult execute() {
        OperationTipologiaIngredienteResult editTipologiaIngredienteResult = (OperationTipologiaIngredienteResult) resultFactory.crea();

        boolean done = tipologiaIngredienteDAO.editTipologia(tipologiaIngrediente.getId(), newNome);
        if (!done) {
            editTipologiaIngredienteResult.setEditTipologiaIngredienteResult(OperationTipologiaIngredienteResult.EDIT_TIPOLOGIA_INGREDIENTE_RESULT.ERROR);
            editTipologiaIngredienteResult.setMessage("ERRORE: La tipologia non è stata modificata!");

            return editTipologiaIngredienteResult;
        }


        editTipologiaIngredienteResult.setEditTipologiaIngredienteResult(OperationTipologiaIngredienteResult.EDIT_TIPOLOGIA_INGREDIENTE_RESULT.EDITED_SUCCESSFULLY);
        editTipologiaIngredienteResult.setMessage("La tipologia è stata modificata correttamente!");

        return editTipologiaIngredienteResult;
    }

    @Override
    public OperationTipologiaIngredienteResult undo() {
        OperationTipologiaIngredienteResult editTipologiaIngredienteResult = (OperationTipologiaIngredienteResult) resultFactory.crea();

        boolean done = tipologiaIngredienteDAO.editTipologia(tipologiaIngrediente.getId(), tipologiaIngrediente.getNome());
        if (!done) {
            editTipologiaIngredienteResult.setEditTipologiaIngredienteResult(OperationTipologiaIngredienteResult.EDIT_TIPOLOGIA_INGREDIENTE_RESULT.ERROR);
            editTipologiaIngredienteResult.setMessage("ERRORE: La tipologia non è stata modificata!");

            return editTipologiaIngredienteResult;
        }

        editTipologiaIngredienteResult.setEditTipologiaIngredienteResult(OperationTipologiaIngredienteResult.EDIT_TIPOLOGIA_INGREDIENTE_RESULT.EDITED_SUCCESSFULLY);
        editTipologiaIngredienteResult.setMessage("La tipologia è stata modificata correttamente!");

        return editTipologiaIngredienteResult;
    }
}
