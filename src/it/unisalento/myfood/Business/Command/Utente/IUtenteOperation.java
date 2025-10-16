package it.unisalento.myfood.Business.Command.Utente;

import it.unisalento.myfood.model.Result.OperationUtenteResult;

public interface IUtenteOperation {
    OperationUtenteResult execute();

    OperationUtenteResult undo();
}
