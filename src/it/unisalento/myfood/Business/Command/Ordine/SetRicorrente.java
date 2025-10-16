package it.unisalento.myfood.Business.Command.Ordine;

import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.OrdineDAO;
import it.unisalento.myfood.model.Result.OperationOrdineResult;

public class SetRicorrente implements IOrdineOperation{

    private OrdineDAO ordineDAO = OrdineDAO.getInstance();
    private ResultFactory resultFactory;
    private boolean state;
    private Integer idOrdine;

    public SetRicorrente(Integer idOrdine, boolean state) {
        this.state = state;
        this.idOrdine = idOrdine;
        resultFactory = new ResultFactory("OperationOrdine");
    }


    @Override
    public OperationOrdineResult execute() {

        OperationOrdineResult createOrdineResult = (OperationOrdineResult) resultFactory.crea();
        boolean done = ordineDAO.setRicorrente(idOrdine, state);

        if (!done) {
            createOrdineResult.setEditOrdineResult(OperationOrdineResult.EDIT_ORDINE_RESULT.ERROR);
            createOrdineResult.setMessage("ERRORE: L'ordine non è stato modificato!");

            return createOrdineResult;
        }

        createOrdineResult.setEditOrdineResult(OperationOrdineResult.EDIT_ORDINE_RESULT.EDITED_SUCCESSFULLY);
        createOrdineResult.setMessage("L'ordine è stato modificato correttamente!");


        return createOrdineResult;
    }
}
