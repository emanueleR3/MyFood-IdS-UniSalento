package it.unisalento.myfood.Business.Command.TipologiaIngrediente;

import it.unisalento.myfood.model.Result.OperationTipologiaIngredienteResult;

public interface ITipoloagiaIngredienteOperation {
        OperationTipologiaIngredienteResult execute();

        OperationTipologiaIngredienteResult undo();


}
