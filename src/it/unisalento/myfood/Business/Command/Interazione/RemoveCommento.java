package it.unisalento.myfood.Business.Command.Interazione;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.InterazioneUtenteDAO;
import it.unisalento.myfood.model.Composite.CommentoCliente;
import it.unisalento.myfood.model.Result.OperationInterazioneResult;

public class RemoveCommento implements IInterazioneOperation {
    private CommentoCliente commento;
    private InterazioneUtenteDAO interazioneUtenteDAO = InterazioneUtenteDAO.getInstance();
    private IResultFactory resultFactory = new ResultFactory("OperationInterazione");

    public RemoveCommento(CommentoCliente commento) {
        this.commento = commento;
    }

    @Override
    public OperationInterazioneResult execute() {
        OperationInterazioneResult editInterazioneResult = (OperationInterazioneResult) resultFactory.crea();

        boolean done  = interazioneUtenteDAO.removeCommento(commento.getId());
        if (!done) {
            editInterazioneResult.setEditInterazioneResult(OperationInterazioneResult.EDIT_INTERAZIONE_RESULT.ERROR);
            editInterazioneResult.setMessage("ERRORE: Il commento non è stato rimosso!");

            return editInterazioneResult;
        }

        editInterazioneResult.setEditInterazioneResult(OperationInterazioneResult.EDIT_INTERAZIONE_RESULT.REMOVED_SUCCESSFULLY);
        editInterazioneResult.setMessage("Il commento è stato rimosso correttamente!");

        return editInterazioneResult;

    }

    @Override
    public OperationInterazioneResult undo() {
            OperationInterazioneResult editInterazioneResult = (OperationInterazioneResult) resultFactory.crea();

            boolean done  = interazioneUtenteDAO.addCommento(commento);
            if (!done) {
                editInterazioneResult.setEditInterazioneResult(OperationInterazioneResult.EDIT_INTERAZIONE_RESULT.ERROR);
                editInterazioneResult.setMessage("ERRORE: Il commento non è stato aggiunto!");

                return editInterazioneResult;
            }
            commento.setIdCommento(interazioneUtenteDAO.getLastCommentoInsertId());

            editInterazioneResult.setEditInterazioneResult(OperationInterazioneResult.EDIT_INTERAZIONE_RESULT.CREATED_SUCCESSFULLY);
            editInterazioneResult.setMessage("Il commento è stato aggiunto correttamente!");

            return editInterazioneResult;
    }


}
