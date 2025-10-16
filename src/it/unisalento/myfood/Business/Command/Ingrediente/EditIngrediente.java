package it.unisalento.myfood.Business.Command.Ingrediente;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.IngredienteDAO;
import it.unisalento.myfood.model.Ingrediente;
import it.unisalento.myfood.model.Result.OperationIngredienteResult;

public class EditIngrediente implements IIngredienteOperation{

    private IngredienteDAO ingredienteDAO = IngredienteDAO.getInstance();
    private Ingrediente ingrediente;
    private Ingrediente resetIngrediente;
    private IResultFactory resultFactory = new ResultFactory("OperationIngrediente");

    public EditIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    @Override
    public OperationIngredienteResult execute() {
        OperationIngredienteResult editIngredienteResult = (OperationIngredienteResult) resultFactory.crea();

        resetIngrediente = ingredienteDAO.findIngredienteById(ingrediente.getId());

        boolean done  = ingredienteDAO.editIngrediente(ingrediente);
        if (!done) {
            editIngredienteResult.setEditIngredienteResult(OperationIngredienteResult.EDIT_INGREDIENTE_RESULT.ERROR);
            editIngredienteResult.setMessage("ERRORE: L'ingrediente non è stato modificato!");

            return editIngredienteResult;
        }

        editIngredienteResult.setEditIngredienteResult(OperationIngredienteResult.EDIT_INGREDIENTE_RESULT.EDITED_SUCCESSFFULLY);
        editIngredienteResult.setMessage("L'ingrediente è stato modificato correttamente!");

        return editIngredienteResult;

    }

    @Override
    public OperationIngredienteResult undo() {
        OperationIngredienteResult editIngredienteResult = (OperationIngredienteResult) resultFactory.crea();

        boolean done  = ingredienteDAO.editIngrediente(resetIngrediente);
        if (!done) {
            editIngredienteResult.setEditIngredienteResult(OperationIngredienteResult.EDIT_INGREDIENTE_RESULT.ERROR);
            editIngredienteResult.setMessage("ERRORE: L'ingrediente non è stato modificato!");

            return editIngredienteResult;
        }

        editIngredienteResult.setEditIngredienteResult(OperationIngredienteResult.EDIT_INGREDIENTE_RESULT.EDITED_SUCCESSFFULLY);
        editIngredienteResult.setMessage("L'ingrediente è stato modificato correttamente!");

        return editIngredienteResult;

    }
}
