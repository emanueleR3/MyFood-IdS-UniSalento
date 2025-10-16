package it.unisalento.myfood.Business.Command.Utente;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.UtenteDAO;
import it.unisalento.myfood.model.Result.OperationUtenteResult;
import it.unisalento.myfood.model.Utente;

public class EditUtente implements IUtenteOperation {
    private Utente utente;
    private UtenteDAO utenteDAO = UtenteDAO.getInstance();
    private IResultFactory resultFactory = new ResultFactory("OperationUtente");
    private Utente resetUtente;

    public EditUtente(Utente utente) {
        this.utente = utente;
        resetUtente = utenteDAO.findById(utente.getId());
    }

    @Override
    public OperationUtenteResult execute() {
        OperationUtenteResult editUtenteResult = (OperationUtenteResult) resultFactory.crea();


        boolean done = utenteDAO.update(utente);

        if (!done) {
            editUtenteResult.setEditUtenteResult(OperationUtenteResult.EDIT_UTENTE_RESULT.ERROR);
            editUtenteResult.setMessage("ERRORE: L'utente non è stato modificato!");

            return editUtenteResult;
        }

        editUtenteResult.setEditUtenteResult(OperationUtenteResult.EDIT_UTENTE_RESULT.EDITED_SUCCESSFULLY);
        editUtenteResult.setMessage("L'utente è stato modificato correttamente!");

        return editUtenteResult;
    }

    @Override
    public OperationUtenteResult undo() {
        OperationUtenteResult editUtenteResult = (OperationUtenteResult) resultFactory.crea();


        boolean done = utenteDAO.update(resetUtente);

        if (!done) {
            editUtenteResult.setEditUtenteResult(OperationUtenteResult.EDIT_UTENTE_RESULT.ERROR);
            editUtenteResult.setMessage("ERRORE: L'utente non è stato modificato!");

            return editUtenteResult;
        }

        editUtenteResult.setEditUtenteResult(OperationUtenteResult.EDIT_UTENTE_RESULT.EDITED_SUCCESSFULLY);
        editUtenteResult.setMessage("L'utente è stato modificato correttamente!");

        return editUtenteResult;
    }
}
