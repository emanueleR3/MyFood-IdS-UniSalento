package it.unisalento.myfood.Business;

import it.unisalento.myfood.Business.Command.Azienda.AddAzienda;
import it.unisalento.myfood.Business.Command.Azienda.EditAzienda;
import it.unisalento.myfood.Business.Command.Azienda.IAziendaOperation;
import it.unisalento.myfood.Business.Command.Azienda.RemoveAzienda;
import it.unisalento.myfood.DAO.AziendaDAO;
import it.unisalento.myfood.model.Azienda;
import it.unisalento.myfood.model.Result.OperationAziendaResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Stack;

public class AziendaBusiness {

    public enum CAMPO{
        NOME, PARTITA_IVA
    }


    private static AziendaBusiness instance = new AziendaBusiness();

    private OperationAziendaResult operationAziendaResult;

    private static HashMap<String, Object> session = UtenteBusiness.getSession();
    private Stack<IAziendaOperation> undoStack = new Stack<>();
    private Stack<IAziendaOperation> redoStack = new Stack<>();
    private Integer MAX_CTRL_Z = 10;

    private AziendaDAO aziendaDAO = AziendaDAO.getInstance();

    private AziendaBusiness() {

    }
    public static AziendaBusiness getInstance(){
        return instance;
    }

    public Object[][] getAziende(){
        ArrayList<Azienda> aziende = aziendaDAO.findAll();

        Object[][] mat = new Object[aziende.size()][3];

        ListIterator<Azienda> iterator = aziende.listIterator();

        int i = 0;
        while(iterator.hasNext()){
            Azienda azienda = iterator.next();
            mat[i][0] = azienda.getId();
            mat[i][1] = azienda.getNome();
            mat[i][2] = azienda.getPartitaIVA();
            i++;
        }

        return mat;
    }

    public Object getCampoSelectedAzienda(CAMPO campo){
        Azienda azienda = (Azienda) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);

        switch (campo){
            case NOME -> {
                return azienda.getNome();
            }
            case PARTITA_IVA -> {
                return azienda.getPartitaIVA();
            }
        }

        System.out.println("campo non gestito");
        return null;

    }

    public Object[][] getAziendeSelezionate(ArrayList<Integer> idAziendeSelezionate) {
        if (idAziendeSelezionate.isEmpty())
            return new Object[0][0];

        ArrayList<Azienda> aziende = new ArrayList<>();
        for (Integer id : idAziendeSelezionate) {
            aziende.add(aziendaDAO.findById(id));
        }

        Object[][] mat = new Object[aziende.size()][3];

        ListIterator<Azienda> iterator = aziende.listIterator();

        int i = 0;
        while(iterator.hasNext()){
            Azienda azienda = iterator.next();
            mat[i][0] = azienda.getId();
            mat[i][1] = azienda.getNome();
            mat[i][2] = azienda.getPartitaIVA();
            i++;
        }

        return mat;
    }
    public void addAzienda(String nome, String pIva){

        IAziendaOperation addAzienda = new AddAzienda(nome, pIva);
        operationAziendaResult = addAzienda.execute();

        if(isAddedSuccessfully()) {
            undoStack.push(addAzienda);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }

    public void removeAzienda(Integer idAzienda){

        IAziendaOperation removeAzienda = new RemoveAzienda(idAzienda);
        operationAziendaResult = removeAzienda.execute();

        if(isRemovedSuccessfully()) {
            undoStack.push(removeAzienda);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }

    public void editSelectedAzienda(String newNome, String newPIva){
        Azienda azienda = (Azienda) session.get(UtenteBusiness.SELECTED_OBJECT);
        azienda.setNome(newNome);
        azienda.setPartitaIVA(newPIva);
        IAziendaOperation editAzienda = new EditAzienda(azienda);
        operationAziendaResult = editAzienda.execute();

        if(isEditedSuccessfully()) {
            undoStack.push(editAzienda);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }

    public boolean undo(){
        if (!undoStack.isEmpty()) {
            IAziendaOperation operation = undoStack.pop();
            operationAziendaResult = operation.undo();
            redoStack.push(operation);
            return true;
        }
        return false;
    }

    public boolean redo(){
        if (!redoStack.isEmpty()) {
            IAziendaOperation operation = redoStack.pop();
            operationAziendaResult = operation.execute();
            undoStack.push(operation);
            return true;
        }
        return false;
    }

    public void setSelectedAzienda(Integer idAzienda){
        Azienda azienda = aziendaDAO.findById(idAzienda);
        session.put(UtenteBusiness.SELECTED_OBJECT, azienda);
    }

    public boolean isAddedSuccessfully(){
        return OperationAziendaResult.EDIT_AZIENDA_RESULT.ADDED_SUCCESSFULLY.equals(operationAziendaResult.getEditAziendaResult());
    }


    public boolean isRemovedSuccessfully(){
        return OperationAziendaResult.EDIT_AZIENDA_RESULT.REMOVED_SUCCESSFULLY.equals(operationAziendaResult.getEditAziendaResult());
    }

    public boolean isEditedSuccessfully(){
        return OperationAziendaResult.EDIT_AZIENDA_RESULT.EDITED_SUCCESSFULLY.equals(operationAziendaResult.getEditAziendaResult());
    }

    public boolean isError(){
        return OperationAziendaResult.EDIT_AZIENDA_RESULT.ERROR.equals(operationAziendaResult.getEditAziendaResult());
    }

    public String getMessage(){
        return operationAziendaResult.getMessage();
    }

}
