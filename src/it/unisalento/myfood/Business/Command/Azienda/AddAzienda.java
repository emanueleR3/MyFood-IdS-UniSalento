package it.unisalento.myfood.Business.Command.Azienda;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;

import it.unisalento.myfood.DAO.AziendaDAO;
import it.unisalento.myfood.DAO.IAziendaDAO;

import it.unisalento.myfood.model.Azienda;
import it.unisalento.myfood.model.Result.OperationAziendaResult;

public class AddAzienda implements IAziendaOperation {
    private String nome;
    private String partitaIVA;
    private Azienda azienda;

    private IAziendaDAO aziendaDAO = AziendaDAO.getInstance();
    private IResultFactory resultFactory = new ResultFactory("OperationAzienda");

    public AddAzienda(String nome, String partitaIVA) {
        this.nome = nome;
        this.partitaIVA = partitaIVA;
    }

    @Override
    public OperationAziendaResult execute() {
        OperationAziendaResult editAziendaResult = (OperationAziendaResult) resultFactory.crea();

        boolean done = aziendaDAO.addAzienda(nome, partitaIVA);
        if (!done) {
            editAziendaResult.setEditAziendaResult(OperationAziendaResult.EDIT_AZIENDA_RESULT.ERROR);
            editAziendaResult.setMessage("ERRORE: L'azienda non è stata aggiunta!");

            return editAziendaResult;
        }

        azienda = aziendaDAO.findById(aziendaDAO.getLastInsertId());

        editAziendaResult.setEditAziendaResult(OperationAziendaResult.EDIT_AZIENDA_RESULT.ADDED_SUCCESSFULLY);
        editAziendaResult.setMessage("L'azienda è stata aggiunta correttamente!");

        return editAziendaResult;
    }

    @Override
    public OperationAziendaResult undo() {
        OperationAziendaResult editAziendaResult = (OperationAziendaResult) resultFactory.crea();

        boolean done = aziendaDAO.removeAzienda(azienda.getId());
        if (!done) {
            editAziendaResult.setEditAziendaResult(OperationAziendaResult.EDIT_AZIENDA_RESULT.ERROR);
            editAziendaResult.setMessage("ERRORE: L'azienda non è stata rimossa!");

            return editAziendaResult;
        }

        editAziendaResult.setEditAziendaResult(OperationAziendaResult.EDIT_AZIENDA_RESULT.REMOVED_SUCCESSFULLY);
        editAziendaResult.setMessage("L'azienda è stata rimossa correttamente!");

        return editAziendaResult;
    }
}
