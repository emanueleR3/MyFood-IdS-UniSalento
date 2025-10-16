package it.unisalento.myfood.Business.Command.Articolo;

import it.unisalento.myfood.Business.AbstractFactory.IResultFactory;
import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.DAO.ArticoloDAO;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Result.OperationArticoloResult;

public class EditArticolo implements IArticoloOperation{
    private IArticolo articolo;
    private ArticoloDAO articoloDAO = ArticoloDAO.getInstance();
    private IResultFactory resultFactory = new ResultFactory("OperationArticolo");
    private IArticolo resetArticolo;


    public EditArticolo(IArticolo articolo) {
        this.articolo = articolo;
        resetArticolo = articoloDAO.findById(articolo.getId());
    }

    @Override
    public OperationArticoloResult execute() {
        OperationArticoloResult editArticoloResult = (OperationArticoloResult) resultFactory.crea();

        boolean done = articoloDAO.editArticolo(articolo);
        if (!done) {
            editArticoloResult.setEditArticoloResult(OperationArticoloResult.EDIT_ARTICOLO_RESULT.ERROR);
            editArticoloResult.setMessage("ERRORE: L'articolo non è stato modificato!");

            return editArticoloResult;
        }

        editArticoloResult.setEditArticoloResult(OperationArticoloResult.EDIT_ARTICOLO_RESULT.EDITED_SUCCESSFULLY);
        editArticoloResult.setMessage("L'articolo è stato modificato correttamente!");

        return editArticoloResult;
    }

    @Override
    public OperationArticoloResult undo() {
        OperationArticoloResult editArticoloResult = (OperationArticoloResult) resultFactory.crea();

        boolean done = articoloDAO.editArticolo(resetArticolo);
        if (!done) {
            editArticoloResult.setEditArticoloResult(OperationArticoloResult.EDIT_ARTICOLO_RESULT.ERROR);
            editArticoloResult.setMessage("ERRORE: L'articolo non è stato modificato!");

            return editArticoloResult;
        }

        editArticoloResult.setEditArticoloResult(OperationArticoloResult.EDIT_ARTICOLO_RESULT.EDITED_SUCCESSFULLY);
        editArticoloResult.setMessage("L'articolo è stato modificato correttamente!");

        return editArticoloResult;
    }

}
