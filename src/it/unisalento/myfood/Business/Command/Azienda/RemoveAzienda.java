package it.unisalento.myfood.Business.Command.Azienda;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.AziendaDAO;
import it.unisalento.myfood.DAO.IAziendaDAO;
import it.unisalento.myfood.model.Azienda;
import it.unisalento.myfood.model.Result.OperationAziendaResult;

public class RemoveAzienda implements IAziendaOperation {

    private Integer idAzienda;
    private Azienda azienda;

    private IAziendaDAO aziendaDAO = AziendaDAO.getInstance();
    private IResultFactory resultFactory = new ResultFactory("OperationAzienda");

    public RemoveAzienda(Integer idAzienda) {
        this.idAzienda = idAzienda;
    }

    @Override
    public OperationAziendaResult execute() {
        OperationAziendaResult editAziendaResult = (OperationAziendaResult) resultFactory.crea();

        boolean done = aziendaDAO.removeAzienda(idAzienda);
        if (!done) {
            editAziendaResult.setEditAziendaResult(OperationAziendaResult.EDIT_AZIENDA_RESULT.ERROR);
            editAziendaResult.setMessage("ERRORE: L'azienda non è stata rimossa!");

            return editAziendaResult;
        }

        azienda = aziendaDAO.findById(idAzienda);

        editAziendaResult.setEditAziendaResult(OperationAziendaResult.EDIT_AZIENDA_RESULT.REMOVED_SUCCESSFULLY);
        editAziendaResult.setMessage("L'azienda è stata rimossa correttamente!");

        return editAziendaResult;
    }

    @Override
    public OperationAziendaResult undo() {
        OperationAziendaResult editAziendaResult = (OperationAziendaResult) resultFactory.crea();

        boolean done = aziendaDAO.addAzienda(azienda.getNome(), azienda.getPartitaIVA());
        if (!done) {
            editAziendaResult.setEditAziendaResult(OperationAziendaResult.EDIT_AZIENDA_RESULT.ERROR);
            editAziendaResult.setMessage("ERRORE: L'azienda non è stata aggiunta!");

            return editAziendaResult;
        }

        editAziendaResult.setEditAziendaResult(OperationAziendaResult.EDIT_AZIENDA_RESULT.ADDED_SUCCESSFULLY);
        editAziendaResult.setMessage("L'azienda è stata aggiunta correttamente!");

        return editAziendaResult;
    }


}
