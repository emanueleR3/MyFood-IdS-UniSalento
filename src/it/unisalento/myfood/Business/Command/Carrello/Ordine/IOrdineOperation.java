package it.unisalento.myfood.Business.Command.Carrello.Ordine;

import it.unisalento.myfood.model.OperationCarrelloResult;
import it.unisalento.myfood.model.OperationOrdineResult;

public interface IOrdineOperation {
        OperationOrdineResult execute();
}
