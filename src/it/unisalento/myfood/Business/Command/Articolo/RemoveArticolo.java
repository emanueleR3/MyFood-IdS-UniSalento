package it.unisalento.myfood.Business.Command.Articolo;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.Business.ImmagineBusiness;
import it.unisalento.myfood.DAO.ArticoloDAO;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Result.OperationArticoloResult;

import java.io.File;

public class RemoveArticolo implements IArticoloOperation {
    private IArticolo articolo;
    private ArticoloDAO articoloDAO = ArticoloDAO.getInstance();
    private IResultFactory resultFactory = new ResultFactory("OperationArticolo");

    public RemoveArticolo(IArticolo articolo) {
        this.articolo = articolo;
    }

    @Override
    public OperationArticoloResult execute() {
        OperationArticoloResult editArticoloResult = (OperationArticoloResult) resultFactory.crea();

        boolean done = articoloDAO.removeArticolo(articolo);
        if (!done) {
            editArticoloResult.setEditArticoloResult(OperationArticoloResult.EDIT_ARTICOLO_RESULT.ERROR);
            editArticoloResult.setMessage("ERRORE: L'articolo non è stato rimosso!");

            return editArticoloResult;
        }

        editArticoloResult.setEditArticoloResult(OperationArticoloResult.EDIT_ARTICOLO_RESULT.REMOVED_SUCCESSFULLY);
        editArticoloResult.setMessage("L'articolo è stato rimosso correttamente!");

        return editArticoloResult;
    }

    @Override
    public OperationArticoloResult undo() {
        OperationArticoloResult editArticoloResult = (OperationArticoloResult) resultFactory.crea();

        boolean done = articoloDAO.addArticolo(articolo);
        if (!done) {
            editArticoloResult.setEditArticoloResult(OperationArticoloResult.EDIT_ARTICOLO_RESULT.ERROR);
            editArticoloResult.setMessage("ERRORE: L'articolo non è stato aggiunto!");

            return editArticoloResult;
        }

        done = ImmagineBusiness.getInstance().updateFolder(articolo.getId());
        if (!done) System.out.println("ERRORE! La cartella non è stata modificata");

        articolo.setId(articoloDAO.getLastInsertId());

        editArticoloResult.setEditArticoloResult(OperationArticoloResult.EDIT_ARTICOLO_RESULT.ADDED_SUCCESSFULLY);
        editArticoloResult.setMessage("L'articolo è stato aggiunto correttamente!");

        return editArticoloResult;
    }
}
