package it.unisalento.myfood.Business.Command.Ingrediente;

import it.unisalento.myfood.model.Result.OperationIngredienteResult;

public interface IIngredienteOperation {
    OperationIngredienteResult execute();

    OperationIngredienteResult undo();


}
