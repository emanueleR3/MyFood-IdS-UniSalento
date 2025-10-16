package it.unisalento.myfood.Business.Command.Carrello;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.CarrelloDAO;
import it.unisalento.myfood.model.Result.OperationCarrelloResult;

public class RemoveFromCarrello implements ICartOperation{
    private CarrelloDAO CDAO = CarrelloDAO.getInstance();
    private Integer idUtente;
    private Integer idArticolo;
    private IResultFactory resultFactory;

    public RemoveFromCarrello(Integer idUtente, Integer idArticolo) {
        this.idUtente = idUtente;
        this.idArticolo = idArticolo;
        resultFactory = new ResultFactory("OperationCarrello");
    }

    @Override
    public OperationCarrelloResult execute() {
        OperationCarrelloResult operationCarrelloResult = (OperationCarrelloResult) resultFactory.crea();

        boolean removedSuccessfully = CDAO.removeArticoloFromCarrello(idArticolo, idUtente);
        if (!removedSuccessfully) {
            // l'articolo NON è stato rimosso
            operationCarrelloResult.setEditCartResult(OperationCarrelloResult.EDIT_CART_RESULT.ERROR);
            operationCarrelloResult.setMessage("ERRORE: l'articolo (ID: " + idArticolo + ") non è stato rimosso!");
        }
        operationCarrelloResult.setEditCartResult(OperationCarrelloResult.EDIT_CART_RESULT.REMOVED_SUCCESSFULLY);
        operationCarrelloResult.setMessage("Articolo rimosso correttamente!");
        return operationCarrelloResult;

    }
}
