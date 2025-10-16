package it.unisalento.myfood.Business.Command.Carrello;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.CarrelloDAO;
import it.unisalento.myfood.model.Carrello;
import it.unisalento.myfood.model.Result.OperationCarrelloResult;

public class LoadCarrello implements ICartOperation{
    private Integer idUtente;
    private CarrelloDAO CDAO = CarrelloDAO.getInstance();
    private IResultFactory resultFactory;

    public LoadCarrello(Integer idUtente) {
        this.idUtente = idUtente;
        resultFactory = new ResultFactory("OperationCarrello");
    }

    @Override
    public OperationCarrelloResult execute() {
        Carrello carrello = CDAO.loadCarrelloPerIdUtente(idUtente);
        OperationCarrelloResult operationCarrelloResult = (OperationCarrelloResult) resultFactory.crea();
        operationCarrelloResult.setCarrello(carrello);
        return operationCarrelloResult;
    }
}
