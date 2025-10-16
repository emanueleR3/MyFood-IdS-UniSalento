package it.unisalento.myfood.Business.Command.Interazione;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.InterazioneUtenteDAO;
import it.unisalento.myfood.model.Composite.RispostaAmministratore;
import it.unisalento.myfood.model.Result.OperationInterazioneResult;

public class AddRisposta implements IInterazioneOperation {
    private RispostaAmministratore risposta;
    private InterazioneUtenteDAO interazioneUtenteDAO = InterazioneUtenteDAO.getInstance();
    private IResultFactory resultFactory = new ResultFactory("OperationInterazione");

    public AddRisposta(RispostaAmministratore risposta) {
        this.risposta = risposta;
    }

    @Override
    public OperationInterazioneResult execute() {
        OperationInterazioneResult editInterazioneResult = (OperationInterazioneResult) resultFactory.crea();

        boolean done  = interazioneUtenteDAO.addRisposta(risposta);
        if (!done) {
            editInterazioneResult.setEditInterazioneResult(OperationInterazioneResult.EDIT_INTERAZIONE_RESULT.ERROR);
            editInterazioneResult.setMessage("ERRORE: La risposta non è stata aggiunta!");

            return editInterazioneResult;
        }
        risposta.setIdRiposta(interazioneUtenteDAO.getLastRispostaInsertId());

        editInterazioneResult.setEditInterazioneResult(OperationInterazioneResult.EDIT_INTERAZIONE_RESULT.CREATED_SUCCESSFULLY);
        editInterazioneResult.setMessage("La risposta è stata aggiunta correttamente!");

        return editInterazioneResult;
    }

    @Override
    public OperationInterazioneResult undo() {
        OperationInterazioneResult editInterazioneResult = (OperationInterazioneResult) resultFactory.crea();

        boolean done  = interazioneUtenteDAO.removeRispostaById(risposta.getId());
        if (!done) {
            editInterazioneResult.setEditInterazioneResult(OperationInterazioneResult.EDIT_INTERAZIONE_RESULT.ERROR);
            editInterazioneResult.setMessage("ERRORE: La riposta non è stata rimossa!");

            return editInterazioneResult;
        }

        editInterazioneResult.setEditInterazioneResult(OperationInterazioneResult.EDIT_INTERAZIONE_RESULT.REMOVED_SUCCESSFULLY);
        editInterazioneResult.setMessage("La riposta è stata rimossa correttamente!");

        return editInterazioneResult;

    }
}
