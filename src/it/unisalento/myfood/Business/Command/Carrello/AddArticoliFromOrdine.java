package it.unisalento.myfood.Business.Command.Carrello;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.CarrelloDAO;
import it.unisalento.myfood.DAO.OrdineDAO;
import it.unisalento.myfood.model.Result.OperationCarrelloResult;
import it.unisalento.myfood.model.Ordine;

import java.util.Iterator;
import java.util.Map;

public class AddArticoliFromOrdine implements ICartOperation {
    private Integer idUtente;

    private CarrelloDAO CDAO = CarrelloDAO.getInstance();
    private IResultFactory resultFactory;
    private OrdineDAO ordineDAO;
    private Integer idOrdine;

    public AddArticoliFromOrdine(Integer idUtente, Integer idOrdine) {
        this.idUtente = idUtente;
        this.idOrdine = idOrdine;
        resultFactory = new ResultFactory("OperationCarrello");
        ordineDAO = OrdineDAO.getInstance();

    }

    @Override
    public OperationCarrelloResult execute() {

        OperationCarrelloResult operationCarrelloResult = (OperationCarrelloResult) resultFactory.crea();
        Ordine ordine = ordineDAO.findById(idOrdine);
        Iterator<Map.Entry<Integer, Integer>> iterator = ordine.getArticoli().entrySet().iterator();


        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();

            Integer idArticolo = entry.getKey();
            Integer quantita = entry.getValue();

            boolean addedSuccessfully = CDAO.addArticoloToCarrello(idArticolo, quantita, idUtente);

            if (!addedSuccessfully) {
                // l'articolo NON è stato inserito
                operationCarrelloResult.setEditCartResult(OperationCarrelloResult.EDIT_CART_RESULT.ERROR);
                operationCarrelloResult.setMessage("ERRORE: l'articolo (ID: " + idArticolo + ") non è stato aggiunto!");

                return operationCarrelloResult;
            }
        }
        operationCarrelloResult.setEditCartResult(OperationCarrelloResult.EDIT_CART_RESULT.ADDED_SUCCESSFULLY);
        operationCarrelloResult.setMessage("Articoli inseriti correttamente!");
        return operationCarrelloResult;
    }

}
