package it.unisalento.myfood.Business.Command.Carrello;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.CarrelloDAO;
import it.unisalento.myfood.model.OperationCarrelloResult;

public class EmptyCarrello implements ICartOperation{
    private Integer idUtente;
    private CarrelloDAO CDAO = CarrelloDAO.getInstance();
    private IResultFactory resultFactory;

    public EmptyCarrello(Integer idUtente) {
        this.idUtente = idUtente;
        resultFactory = new ResultFactory("OperationCarrello");
    }

    @Override
    public OperationCarrelloResult execute() {
        OperationCarrelloResult operationCarrelloResult = (OperationCarrelloResult) resultFactory.crea();
        boolean emptiedSuccesfully = CDAO.emptyCarrello(idUtente);

        if (!emptiedSuccesfully) {
            operationCarrelloResult.setEditCartResult(OperationCarrelloResult.EDIT_CART_RESULT.ERROR);
            operationCarrelloResult.setMessage("ERRORE! Il carrello non è stato svuotato!");

            return operationCarrelloResult;
        }

        operationCarrelloResult.setEditCartResult(OperationCarrelloResult.EDIT_CART_RESULT.EMPTIED_SUCCESSFFULLY);
        operationCarrelloResult.setMessage("Carrello svuotato correttamente! ");
        return operationCarrelloResult;
    }
}
