package it.unisalento.myfood.Business.Command.Ingrediente;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.IngredienteDAO;
import it.unisalento.myfood.model.Ingrediente;
import it.unisalento.myfood.model.Result.OperationIngredienteResult;

public class AddIngrediente implements IIngredienteOperation{

    private IngredienteDAO ingredienteDAO = IngredienteDAO.getInstance();
    private Ingrediente ingrediente;
    private IResultFactory resultFactory = new ResultFactory("OperationIngrediente");

    public AddIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    @Override
    public OperationIngredienteResult execute() {
        OperationIngredienteResult editIngredienteResult = (OperationIngredienteResult) resultFactory.crea();

        boolean done  = ingredienteDAO.addIngrediente(ingrediente);
        if (!done) {
            editIngredienteResult.setEditIngredienteResult(OperationIngredienteResult.EDIT_INGREDIENTE_RESULT.ERROR);
            editIngredienteResult.setMessage("ERRORE: L'ingrediente non è stato aggiunto!");

            return editIngredienteResult;
        }
        ingrediente.setId(ingredienteDAO.getLastInsertId());

        editIngredienteResult.setEditIngredienteResult(OperationIngredienteResult.EDIT_INGREDIENTE_RESULT.ADDED_SUCCESSFULLY);
        editIngredienteResult.setMessage("L'ingrediente è stato aggiunto correttamente!");

        return editIngredienteResult;

    }

    @Override
    public OperationIngredienteResult undo() {
        OperationIngredienteResult editIngredienteResult = (OperationIngredienteResult) resultFactory.crea();

        boolean done  = ingredienteDAO.removeIngrediente(ingrediente.getId());
        if (!done) {
            editIngredienteResult.setEditIngredienteResult(OperationIngredienteResult.EDIT_INGREDIENTE_RESULT.ERROR);
            editIngredienteResult.setMessage("ERRORE: L'ingrediente non è stato rimosso!");

            return editIngredienteResult;
        }

        editIngredienteResult.setEditIngredienteResult(OperationIngredienteResult.EDIT_INGREDIENTE_RESULT.REMOVED_SUCCESSFULLY);
        editIngredienteResult.setMessage("L'ingrediente è stato rimosso correttamente!");

        return editIngredienteResult;

    }
}
