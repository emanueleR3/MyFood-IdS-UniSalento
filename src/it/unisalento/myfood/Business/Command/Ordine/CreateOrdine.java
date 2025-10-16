package it.unisalento.myfood.Business.Command.Ordine;

import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.Business.Bridge.Documento;
import it.unisalento.myfood.Business.Bridge.DocumentoOrdine;
import it.unisalento.myfood.Business.Bridge.PdfBoxAPI;
import it.unisalento.myfood.DAO.OrdineDAO;
import it.unisalento.myfood.model.Carrello;
import it.unisalento.myfood.model.Result.OperationOrdineResult;
import it.unisalento.myfood.model.Ordine;

public class CreateOrdine implements IOrdineOperation{

    private OrdineDAO ordineDAO = OrdineDAO.getInstance();
    private Carrello carrello;
    private ResultFactory resultFactory;

    public CreateOrdine(Carrello carrello) {
        this.carrello = carrello;
        resultFactory = new ResultFactory("OperationOrdine");
    }


    @Override
    public OperationOrdineResult execute() {

        OperationOrdineResult createOrdineResult = (OperationOrdineResult) resultFactory.crea();

        if(carrello.getArticoli() == null || carrello.getArticoli().size() == 0){
            createOrdineResult.setEditOrdineResult(OperationOrdineResult.EDIT_ORDINE_RESULT.ERROR);
            createOrdineResult.setMessage("Impossibile procedere con l'ordine: il carrello è vuoto");
            return createOrdineResult;
        }

        boolean createdSuccessfully = ordineDAO.createOrdine(carrello);

        if (!createdSuccessfully) {
            createOrdineResult.setEditOrdineResult(OperationOrdineResult.EDIT_ORDINE_RESULT.ERROR);
            createOrdineResult.setMessage("ERRORE: L'ordine non è stato creato!");

            return createOrdineResult;
        }

        Ordine ordine = ordineDAO.findById(ordineDAO.getLastInsertId());
        createOrdineResult.setOrdine(ordine);

        createOrdineResult.setEditOrdineResult(OperationOrdineResult.EDIT_ORDINE_RESULT.CREATED_SUCCESSFULLY);
        createOrdineResult.setMessage("L'ordine è stato creato correttamente!");


        Documento listaPagamento = new DocumentoOrdine(new PdfBoxAPI(), ordine);
        listaPagamento.inviaListaOrdineEffettuato(carrello.getCliente().getEmail());



        return createOrdineResult;
    }
}
