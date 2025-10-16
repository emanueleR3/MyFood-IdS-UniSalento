package it.unisalento.myfood.Business.Command.Articolo;

import it.unisalento.myfood.model.Result.OperationArticoloResult;

public interface IArticoloOperation {
    OperationArticoloResult execute();

    OperationArticoloResult undo();

}
