package it.unisalento.myfood.Business.Command.Utente;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.UtenteDAO;
import it.unisalento.myfood.model.Result.OperationUtenteResult;
import it.unisalento.myfood.model.Utente;

public class RemoveUtente implements IUtenteOperation{
    private Utente utente;
    private UtenteDAO utenteDAO = UtenteDAO.getInstance();
    private IResultFactory resultFactory = new ResultFactory("OperationUtente");

    public RemoveUtente(Utente utente) {
        this.utente = utente;
    }

    @Override
    public OperationUtenteResult execute() {
        OperationUtenteResult editUtenteResult = (OperationUtenteResult) resultFactory.crea();
        boolean done = utenteDAO.removeByEmail(utente.getEmail());

        if (!done) {
            editUtenteResult.setEditUtenteResult(OperationUtenteResult.EDIT_UTENTE_RESULT.ERROR);
            editUtenteResult.setMessage("ERRORE: L'utente non è stato rimosso!");

            return editUtenteResult;
        }

        editUtenteResult.setEditUtenteResult(OperationUtenteResult.EDIT_UTENTE_RESULT.REMOVED_SUCCESSFULLY);
        editUtenteResult.setMessage("L'utente è stato rimosso correttamente!");

        return editUtenteResult;
    }

    @Override
    public OperationUtenteResult undo() {
        OperationUtenteResult editUtenteResult = (OperationUtenteResult) resultFactory.crea();

        boolean done = utenteDAO.addUtente(utente);
        if (!done) {
            editUtenteResult.setEditUtenteResult(OperationUtenteResult.EDIT_UTENTE_RESULT.ERROR);
            editUtenteResult.setMessage("ERRORE: L'utente non è stato aggiunto!");

            return editUtenteResult;
        }

        editUtenteResult.setEditUtenteResult(OperationUtenteResult.EDIT_UTENTE_RESULT.CREATED_SUCCESSFULLY);
        editUtenteResult.setMessage("L'utente è stato aggiunto correttamente!");

        return editUtenteResult;
    }
}
