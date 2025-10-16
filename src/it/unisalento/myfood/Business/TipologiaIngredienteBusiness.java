package it.unisalento.myfood.Business;

import it.unisalento.myfood.Business.Command.TipologiaIngrediente.AddTipologiaIngrediente;
import it.unisalento.myfood.Business.Command.TipologiaIngrediente.EditTipologiaIngrediente;
import it.unisalento.myfood.Business.Command.TipologiaIngrediente.ITipoloagiaIngredienteOperation;
import it.unisalento.myfood.Business.Command.TipologiaIngrediente.RemoveTipologiaIngrediente;
import it.unisalento.myfood.DAO.TipologiaIngredienteDAO;
import it.unisalento.myfood.model.Result.OperationTipologiaIngredienteResult;
import it.unisalento.myfood.model.TipologiaIngrediente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

public class TipologiaIngredienteBusiness {
    public enum CAMPO {
        NOME
    }

    private TipologiaIngredienteDAO TPDAO = TipologiaIngredienteDAO.getInstance();

    private static TipologiaIngredienteBusiness instance = new TipologiaIngredienteBusiness();
    private OperationTipologiaIngredienteResult operationTipologiaIngredienteResult;
    private HashMap<String, Object> session;
    private Stack<ITipoloagiaIngredienteOperation> undoStack = new Stack<>();
    private Stack<ITipoloagiaIngredienteOperation> redoStack = new Stack<>();
    private Integer MAX_CTRL_Z = 10;


    public static TipologiaIngredienteBusiness getInstance(){
        return instance;
    }

    private TipologiaIngredienteBusiness(){
        session = UtenteBusiness.getSession();
    }


    public String[] loadNomiTipologie(){
        ArrayList<TipologiaIngrediente> tipologieList = TPDAO.loadTipologia();

        String[] tipologie = new String[tipologieList.size()];

        Iterator<TipologiaIngrediente> iterator = tipologieList.iterator();

        int i = 0;
        while (iterator.hasNext()){
            tipologie[i] = iterator.next().getNome();
            i++;
        }
        return tipologie;
    }

    public Object[][] loadTipologie(){
        ArrayList<TipologiaIngrediente> tipologieList = TPDAO.loadTipologia();

        if (tipologieList.isEmpty()) return new Object[0][0];

        Object[][] tipologie = new Object[tipologieList.size()][2];

        Iterator<TipologiaIngrediente> iterator = tipologieList.iterator();

        int i = 0;
        while (iterator.hasNext()){
            TipologiaIngrediente tipologiaIngrediente = iterator.next();
            tipologie[i][0] = tipologiaIngrediente.getId();
            tipologie[i][1] = tipologiaIngrediente.getNome();
            i++;
        }
        return tipologie;
    }

    public Object getCampoSelectedTipologia(CAMPO campo){
        TipologiaIngrediente tipologiaIngrediente = (TipologiaIngrediente) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);

        switch (campo){
            case NOME -> {
                return tipologiaIngrediente.getNome();
            }
        }

        System.out.println("campo non gestito");
        return null;

    }

    public void addTipologia(String nome){
        ITipoloagiaIngredienteOperation addTipologia = new AddTipologiaIngrediente(nome);

        operationTipologiaIngredienteResult = addTipologia.execute();

        if(isAddedSuccessfully()) {
            undoStack.push(addTipologia);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }

    public void removeTipologia(Integer id){

        TipologiaIngrediente tipologiaIngrediente = TPDAO.findTipologiaById(id);


        ITipoloagiaIngredienteOperation removeTipologia = new RemoveTipologiaIngrediente(tipologiaIngrediente.getNome());

        operationTipologiaIngredienteResult = removeTipologia.execute();

        if(isRemovedSuccessfully()) {
            undoStack.push(removeTipologia);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }

    public void editSelectedTipologia(String newName){
        TipologiaIngrediente tipologiaIngrediente = (TipologiaIngrediente) session.get(UtenteBusiness.SELECTED_OBJECT);

        ITipoloagiaIngredienteOperation editTipologia = new EditTipologiaIngrediente(tipologiaIngrediente, newName);

        operationTipologiaIngredienteResult = editTipologia.execute();

        if(isEditSuccessfully()) {
            undoStack.push(editTipologia);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }
    }

    public boolean undo(){
        if (!undoStack.isEmpty()) {
            ITipoloagiaIngredienteOperation operation = undoStack.pop();
            operationTipologiaIngredienteResult = operation.undo();
            redoStack.push(operation);
            return true;
        }
        return false;
    }

    public boolean redo(){
        if (!redoStack.isEmpty()) {
            ITipoloagiaIngredienteOperation operation = redoStack.pop();
            operationTipologiaIngredienteResult = operation.execute();
            undoStack.push(operation);
            return true;
        }
        return false;
    }


    public void setSelectedTipologia(Integer idTipologia){
        TipologiaIngrediente tipologiaIngrediente = TPDAO.findTipologiaById(idTipologia);

        UtenteBusiness.getSession().put(UtenteBusiness.SELECTED_OBJECT, tipologiaIngrediente);
    }

    public boolean isAddedSuccessfully(){
        return OperationTipologiaIngredienteResult.EDIT_TIPOLOGIA_INGREDIENTE_RESULT.ADDED_SUCCESSFULLY.equals(operationTipologiaIngredienteResult.getEditTipologiaIngredienteResult());
    }


    public boolean isRemovedSuccessfully(){
        return OperationTipologiaIngredienteResult.EDIT_TIPOLOGIA_INGREDIENTE_RESULT.REMOVED_SUCCESSFULLY.equals(operationTipologiaIngredienteResult.getEditTipologiaIngredienteResult());
    }

    public boolean isEditSuccessfully(){
        return OperationTipologiaIngredienteResult.EDIT_TIPOLOGIA_INGREDIENTE_RESULT.EDITED_SUCCESSFULLY.equals(operationTipologiaIngredienteResult.getEditTipologiaIngredienteResult());
    }

    public boolean isError(){
        return OperationTipologiaIngredienteResult.EDIT_TIPOLOGIA_INGREDIENTE_RESULT.ERROR.equals(operationTipologiaIngredienteResult.getEditTipologiaIngredienteResult());
    }

    public String getMessage(){
        return operationTipologiaIngredienteResult.getMessage();
    }

}
