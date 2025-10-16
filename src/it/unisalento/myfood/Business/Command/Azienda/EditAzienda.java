package it.unisalento.myfood.Business.Command.Azienda;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.AziendaDAO;
import it.unisalento.myfood.DAO.IAziendaDAO;
import it.unisalento.myfood.model.Azienda;
import it.unisalento.myfood.model.Result.OperationAziendaResult;

public class EditAzienda implements IAziendaOperation {
    private Azienda azienda;
    private Azienda resetAzienda;

    private IAziendaDAO aziendaDAO = AziendaDAO.getInstance();
    private IResultFactory resultFactory = new ResultFactory("OperationAzienda");

    public EditAzienda(Azienda azienda) {
      this.azienda = azienda;
    }

    @Override
    public OperationAziendaResult execute() {
        OperationAziendaResult editAziendaResult = (OperationAziendaResult) resultFactory.crea();

        resetAzienda = aziendaDAO.findById(azienda.getId());

        boolean done = aziendaDAO.updateAzienda(azienda.getId(), azienda.getNome(), azienda.getPartitaIVA());
        if (!done) {
            editAziendaResult.setEditAziendaResult(OperationAziendaResult.EDIT_AZIENDA_RESULT.ERROR);
            editAziendaResult.setMessage("ERRORE: L'azienda non è stata modificata!");

            return editAziendaResult;
        }

        editAziendaResult.setEditAziendaResult(OperationAziendaResult.EDIT_AZIENDA_RESULT.EDITED_SUCCESSFULLY);
        editAziendaResult.setMessage("L'azienda è stata modificata correttamente!");

        return editAziendaResult;
    }

    @Override
    public OperationAziendaResult undo() {
        OperationAziendaResult editAziendaResult = (OperationAziendaResult) resultFactory.crea();

        boolean done = aziendaDAO.updateAzienda(azienda.getId(), resetAzienda.getNome(), resetAzienda.getPartitaIVA());
        if (!done) {
            editAziendaResult.setEditAziendaResult(OperationAziendaResult.EDIT_AZIENDA_RESULT.ERROR);
            editAziendaResult.setMessage("ERRORE: L'azienda non è stata modificata!");

            return editAziendaResult;
        }

        editAziendaResult.setEditAziendaResult(OperationAziendaResult.EDIT_AZIENDA_RESULT.REMOVED_SUCCESSFULLY);
        editAziendaResult.setMessage("L'azienda è stata modificata correttamente!");

        return editAziendaResult;
    }
}
