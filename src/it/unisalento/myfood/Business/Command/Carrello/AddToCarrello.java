package it.unisalento.myfood.Business.Command.Carrello;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.CarrelloDAO;
import it.unisalento.myfood.model.Result.OperationCarrelloResult;

public class AddToCarrello implements ICartOperation {
    private Integer idUtente;
    private Integer quantita;
    private Integer idArticolo;
    private CarrelloDAO CDAO = CarrelloDAO.getInstance();
    private IResultFactory resultFactory;

    public AddToCarrello(Integer idUtente, Integer quantita, Integer idArticolo) {
        this.idUtente = idUtente;
        this.quantita = quantita;
        this.idArticolo = idArticolo;
        resultFactory = new ResultFactory("OperationCarrello");

    }

    @Override
    public OperationCarrelloResult execute() {

        OperationCarrelloResult operationCarrelloResult = (OperationCarrelloResult) resultFactory.crea();
        boolean addedSuccessfully = CDAO.addArticoloToCarrello(idArticolo, quantita, idUtente);
        if (!addedSuccessfully) {
            // l'articolo NON è stato inserito
            operationCarrelloResult.setEditCartResult(OperationCarrelloResult.EDIT_CART_RESULT.ERROR);
            operationCarrelloResult.setMessage("ERRORE: l'articolo (ID: " + idArticolo + ") non è stato aggiunto!");

            return operationCarrelloResult;
        }
        operationCarrelloResult.setEditCartResult(OperationCarrelloResult.EDIT_CART_RESULT.ADDED_SUCCESSFULLY);
        operationCarrelloResult.setMessage("Articoli inseriti correttamente!");
        return operationCarrelloResult;
    }

}
