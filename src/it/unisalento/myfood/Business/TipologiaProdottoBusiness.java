package it.unisalento.myfood.Business;

import it.unisalento.myfood.Business.Command.TipologiaProdotto.AddTipologiaProdotto;
import it.unisalento.myfood.Business.Command.TipologiaProdotto.EditTipologiaProdotto;
import it.unisalento.myfood.Business.Command.TipologiaProdotto.ITipoloagiaProdottoOperation;
import it.unisalento.myfood.Business.Command.TipologiaProdotto.RemoveTipologiaProdotto;
import it.unisalento.myfood.DAO.TipologiaProdottoDAO;
import it.unisalento.myfood.model.Composite.Prodotto;

import it.unisalento.myfood.model.Result.OperationTipologiaProdottoResult;
import it.unisalento.myfood.model.TipologiaProdotto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

public class TipologiaProdottoBusiness {

    public enum CAMPO {
        NOME,
        ID
    }

    private TipologiaProdottoDAO TPDAO = TipologiaProdottoDAO.getInstance();

    private static TipologiaProdottoBusiness instance = new TipologiaProdottoBusiness();

    private OperationTipologiaProdottoResult operationTipologiaProdottoResult;
    private Stack<ITipoloagiaProdottoOperation> undoStack = new Stack<>();
    private Stack<ITipoloagiaProdottoOperation> redoStack = new Stack<>();
    private Integer MAX_CTRL_Z = 10;

    private HashMap<String, Object> session;

    public static TipologiaProdottoBusiness getInstance(){
        return instance;
    }

    private TipologiaProdottoBusiness(){
        session = UtenteBusiness.getSession();
    }

    public String[] loadNomiTipologie(){
        ArrayList<TipologiaProdotto> tipologieList = TPDAO.loadTipologia();

        if (tipologieList.isEmpty()) return new String[0];

        String[] tipologie = new String[tipologieList.size() + 1];

        Iterator<TipologiaProdotto> iterator = tipologieList.iterator();

        int i = 0;
        while (iterator.hasNext()){
            tipologie[i] = iterator.next().getNome();
            i++;
        }
        tipologie[i] = "Menu";
        return tipologie;
    }

    public Object[][] loadTipologie(){
        ArrayList<TipologiaProdotto> tipologieList = TPDAO.loadTipologia();

        Object[][] tipologie = new Object[tipologieList.size()][2];

        Iterator<TipologiaProdotto> iterator = tipologieList.iterator();

        int i = 0;
        while (iterator.hasNext()){
            TipologiaProdotto tipologiaProdotto = iterator.next();
            tipologie[i][0] = tipologiaProdotto.getId();
            tipologie[i][1] = tipologiaProdotto.getNome();
            i++;
        }
        return tipologie;
    }

    public Object getCampoTipologiaProdotto(TipologiaProdotto tipologiaProdotto, CAMPO nomeCampo) {
        switch (nomeCampo) {
            case ID -> {
                return tipologiaProdotto.getId();
            }
            case NOME -> {
                return tipologiaProdotto.getNome();
            }
        }

        return null;
    }

    public Object getCampoTipologiaSelectedProdotto(CAMPO nomeCampo) {
        TipologiaProdotto tipologiaProdotto = ((Prodotto) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT)).getTipologiaProdotto();

        switch (nomeCampo) {
            case ID -> {
                return getCampoTipologiaProdotto(tipologiaProdotto, CAMPO.ID);
            }
            case NOME -> {
                return getCampoTipologiaProdotto(tipologiaProdotto, CAMPO.NOME);
            }
        }

        return null;
    }

    public Object getCampoSelectedTipologia(CAMPO nomeCampo) {
        TipologiaProdotto tipologiaProdotto = (TipologiaProdotto) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);

        switch (nomeCampo) {
            case ID -> {
                return getCampoTipologiaProdotto(tipologiaProdotto, CAMPO.ID);
            }
            case NOME -> {
                return getCampoTipologiaProdotto(tipologiaProdotto, CAMPO.NOME);
            }
        }

        return null;
    }

    public void addTipologia(String nome){
        ITipoloagiaProdottoOperation addTipologia = new AddTipologiaProdotto(nome);

        operationTipologiaProdottoResult = addTipologia.execute();

        if(isAddedSuccessfully()) {
            undoStack.push(addTipologia);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }

    public void removeTipologia(Integer id){

        ITipoloagiaProdottoOperation removeTipologia = new RemoveTipologiaProdotto(id);

        operationTipologiaProdottoResult = removeTipologia.execute();

        if(isRemovedSuccessfully()) {
            undoStack.push(removeTipologia);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }

    public void editSelectedTipologia(String newName){
        TipologiaProdotto tipologiaProdotto = (TipologiaProdotto) session.get(UtenteBusiness.SELECTED_OBJECT);

        ITipoloagiaProdottoOperation editTipologia = new EditTipologiaProdotto(tipologiaProdotto, newName);

        operationTipologiaProdottoResult = editTipologia.execute();

        if(isEditedSuccessfully()) {
            undoStack.push(editTipologia);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }

    public boolean undo(){
        if (!undoStack.isEmpty()) {
            ITipoloagiaProdottoOperation operation = undoStack.pop();
            operationTipologiaProdottoResult = operation.undo();
            redoStack.push(operation);
            return true;
        }
        return false;
    }

    public boolean redo(){
        if (!redoStack.isEmpty()) {
            ITipoloagiaProdottoOperation operation = redoStack.pop();
            operationTipologiaProdottoResult = operation.execute();
            undoStack.push(operation);
            return true;
        }
        return false;
    }


    public void setSelectedTipologia(Integer idTipologia){
        TipologiaProdotto tipologiaProdotto = TPDAO.findTipologiaById(idTipologia);

        UtenteBusiness.getSession().put(UtenteBusiness.SELECTED_OBJECT, tipologiaProdotto);
    }

    public boolean isAddedSuccessfully(){
        return OperationTipologiaProdottoResult.EDIT_TIPOLOGIA_PRODOTTO_RESULT.ADDED_SUCCESSFULLY.equals(operationTipologiaProdottoResult.getEditTipologiaProdottoResult());
    }

    public boolean isRemovedSuccessfully(){
        return OperationTipologiaProdottoResult.EDIT_TIPOLOGIA_PRODOTTO_RESULT.REMOVED_SUCCESSFULLY.equals(operationTipologiaProdottoResult.getEditTipologiaProdottoResult());
    }

    public boolean isEditedSuccessfully(){
        return OperationTipologiaProdottoResult.EDIT_TIPOLOGIA_PRODOTTO_RESULT.EDITED_SUCCESSFULLY.equals(operationTipologiaProdottoResult.getEditTipologiaProdottoResult());
    }

    public boolean isError(){
        return OperationTipologiaProdottoResult.EDIT_TIPOLOGIA_PRODOTTO_RESULT.ERROR.equals(operationTipologiaProdottoResult.getEditTipologiaProdottoResult());
    }


    public String getMessage(){
        return operationTipologiaProdottoResult.getMessage();
    }




}
