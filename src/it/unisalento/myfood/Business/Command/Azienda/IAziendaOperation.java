package it.unisalento.myfood.Business.Command.Azienda;

import it.unisalento.myfood.model.Result.OperationAziendaResult;


public interface IAziendaOperation {
        OperationAziendaResult execute();

        OperationAziendaResult undo();
}
