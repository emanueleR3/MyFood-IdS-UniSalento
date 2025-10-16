package it.unisalento.myfood.Business.Command.Ordine;

import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.OrdineDAO;
import it.unisalento.myfood.model.Result.OperationOrdineResult;
import it.unisalento.myfood.model.Ordine;

public class RemoveOrdine implements IOrdineOperation{

    private OrdineDAO ordineDAO = OrdineDAO.getInstance();
    private Ordine ordine;
    private ResultFactory resultFactory;

    public RemoveOrdine(Ordine ordine) {
        this.ordine = ordine;
        resultFactory = new ResultFactory("OperationOrdine");
    }


    @Override
    public OperationOrdineResult execute() {

        OperationOrdineResult removeOrdineResult = (OperationOrdineResult) resultFactory.crea();

        boolean done = ordineDAO.remove(ordine);

        if (!done) {
            removeOrdineResult.setEditOrdineResult(OperationOrdineResult.EDIT_ORDINE_RESULT.ERROR);
            removeOrdineResult.setMessage("ERRORE: L'ordine non è stato rimosso!");

            return removeOrdineResult;
        }

        removeOrdineResult.setEditOrdineResult(OperationOrdineResult.EDIT_ORDINE_RESULT.REMOVED_SUCCESSFULLY);
        removeOrdineResult.setMessage("L'ordine è stato rimosso correttamente!");

        return removeOrdineResult;
    }

}
