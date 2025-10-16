package it.unisalento.myfood.Business;


import it.unisalento.myfood.Business.Command.Ingrediente.AddIngrediente;
import it.unisalento.myfood.Business.Command.Ingrediente.EditIngrediente;
import it.unisalento.myfood.Business.Command.Ingrediente.IIngredienteOperation;
import it.unisalento.myfood.Business.Command.Ingrediente.RemoveIngrediente;
import it.unisalento.myfood.DAO.*;
import it.unisalento.myfood.model.Azienda;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.Ingrediente;
import it.unisalento.myfood.model.Result.OperationIngredienteResult;


import java.util.*;

public class IngredienteBusiness {


    public static enum CAMPO{
        NOME, ID_TIPOLOGIA, ID_PRODUTTORE, ID_DISTRIBUTORI, NOME_TIPOLOGIA

    }
    private IIngredienteDAO ingredienteDAO = IngredienteDAO.getInstance();
    private ITipologiaIngredienteDAO tipologiaIngredienteDAO = TipologiaIngredienteDAO.getInstance();
    private IAziendaDAO aziendaDAO = AziendaDAO.getInstance();

    private static IngredienteBusiness instance = new IngredienteBusiness();

    public static IngredienteBusiness getInstance(){
        return instance;
    }
    private HashMap<String, Object> session = UtenteBusiness.getSession();

    private OperationIngredienteResult operationIngredienteResult;

    private Stack<IIngredienteOperation> undoStack = new Stack<>();

    private Stack<IIngredienteOperation> redoStack = new Stack<>();
    private Integer MAX_CTRL_Z = 10;


    private IngredienteBusiness(){}


    public Object[][] getIngredienti(){
        ArrayList<Ingrediente> ingredienti = ingredienteDAO.findAll();

        Object[][] mat = new Object[ingredienti.size()][4];

        ListIterator<Ingrediente> iterator = ingredienti.listIterator();

        int i = 0;
        while(iterator.hasNext()){
            Ingrediente ingrediente = iterator.next();
            mat[i][0] = ingrediente.getId();
            mat[i][1] = ingrediente.getNome();
            mat[i][2] = ingrediente.getTipologiaIngrediente().getNome();
            mat[i][3] = ingrediente.getProduttore().getNome();
            i++;
        }

        return mat;
    }

    public Object[][] getIngredientiPerTipologia(String filtroTipologia){
        ArrayList<Ingrediente> ingredienti = ingredienteDAO.findIngredientePerTipologia(filtroTipologia);

        Object[][] mat = new Object[ingredienti.size()][4];

        ListIterator<Ingrediente> iterator = ingredienti.listIterator();

        int i = 0;
        while(iterator.hasNext()){
            Ingrediente ingrediente = iterator.next();
            mat[i][0] = ingrediente.getId();
            mat[i][1] = ingrediente.getNome();
            mat[i][2] = ingrediente.getTipologiaIngrediente().getNome();
            mat[i][3] = ingrediente.getProduttore().getNome();
            i++;
        }

        return mat;
    }


    public Object[][] getIngredientiPerViewProdotto() {
        List<Ingrediente> ingredienti = getIngredientiPerProdotto((Prodotto) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT));

        Object[][] mat = new Object[ingredienti.size()][4];

        ListIterator<Ingrediente> iterator = ingredienti.listIterator();

        int i = 0;
        while(iterator.hasNext()){
            Ingrediente ingrediente = iterator.next();
            mat[i][0] = ingrediente.getId();
            mat[i][1] = ingrediente.getNome();
            mat[i][2] = ingrediente.getTipologiaIngrediente().getNome();
            mat[i][3] = ingrediente.getProduttore().getNome();
            i++;
        }

        return mat;
    }

    public void addIngrediente(String nome, String tipologia, Integer idProduttore, ArrayList<Integer> idDistributori ){
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNome(nome);
        ingrediente.setTipologiaIngrediente(tipologiaIngredienteDAO.findTipologiaByNome(tipologia));
        ingrediente.setProduttore(aziendaDAO.findById(idProduttore));

        ArrayList<Azienda> distributori = new ArrayList<>();
        Iterator<Integer> iterator = idDistributori.iterator();
        while (iterator.hasNext()){
            distributori.add(aziendaDAO.findById(iterator.next()));
        }

        ingrediente.setDistributori(distributori);

        IIngredienteOperation addIngrediente = new AddIngrediente(ingrediente);
        operationIngredienteResult = addIngrediente.execute();

        if(isAddedSuccessfully()) {
            undoStack.push(addIngrediente);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }

    public void removeIngrediente(Integer idIngrediente){
        IIngredienteOperation removeIngrediente = new RemoveIngrediente(idIngrediente);
        operationIngredienteResult = removeIngrediente.execute();

        if(isRemovedSuccessfully()) {
            undoStack.push(removeIngrediente);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }

    public void editSelectedIngrediente(String newNome, String newTipologia, Integer newIdProduttore, ArrayList<Integer> newIdDistributori){
        Ingrediente ingrediente = (Ingrediente) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        ingrediente.setNome(newNome);
        ingrediente.setTipologiaIngrediente(tipologiaIngredienteDAO.findTipologiaByNome(newTipologia));
        ingrediente.setProduttore(aziendaDAO.findById(newIdProduttore));

        ArrayList<Azienda> distributori = new ArrayList<>();
        Iterator<Integer> iterator = newIdDistributori.iterator();
        while (iterator.hasNext()){
            distributori.add(aziendaDAO.findById(iterator.next()));
        }

        ingrediente.setDistributori(distributori);

        IIngredienteOperation editIngrediente = new EditIngrediente(ingrediente);
        operationIngredienteResult = editIngrediente.execute();

        if(isEditedSuccessfully()) {
            undoStack.push(editIngrediente);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }



    public Object[][] getIngredientiSelezionati(ArrayList<Integer> idIngredientiSelezionati) {
        if (idIngredientiSelezionati.isEmpty())
            return new Object[0][0];

        ArrayList<Ingrediente> ingredienti = new ArrayList<>();
        for (Integer id : idIngredientiSelezionati) {
            ingredienti.add(ingredienteDAO.findIngredienteById(id));
        }

        Object[][] mat = new Object[ingredienti.size()][4];

        ListIterator<Ingrediente> iterator = ingredienti.listIterator();

        int i = 0;
        while(iterator.hasNext()){
            Ingrediente ingrediente = iterator.next();
            mat[i][0] = ingrediente.getId();
            mat[i][1] = ingrediente.getNome();
            mat[i][2] = ingrediente.getTipologiaIngrediente().getNome();
            mat[i][3] = ingrediente.getProduttore().getNome();
            i++;
        }

        return mat;
    }

    public List<Ingrediente> getIngredientiPerProdotto(Prodotto prodotto){
        return ingredienteDAO.getIngredientiPerProdotto(prodotto.getId());
    }

    public ArrayList<String> getNomiIngredientiViewProdotto() {
        ArrayList<Ingrediente> ingredienti = ((Prodotto) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT)).getIngredienti();
        ArrayList<String> nomiIngredienti = new ArrayList<>();

        ListIterator<Ingrediente> iterator = ingredienti.listIterator();

        while (iterator.hasNext()) {
            nomiIngredienti.add(iterator.next().getNome());
        }

        return nomiIngredienti;
    }

    public Object getCampoSelectedIngrediente(CAMPO campo){
        Ingrediente ingrediente = (Ingrediente) session.get(UtenteBusiness.SELECTED_OBJECT);

        switch (campo) {
            case NOME -> {
                return ingrediente.getNome();
            }
            case ID_TIPOLOGIA -> {
                return ingrediente.getTipologiaIngrediente().getId();
            }
            case NOME_TIPOLOGIA -> {
                return ingrediente.getTipologiaIngrediente().getNome();
            }
            case ID_PRODUTTORE -> {
                return ingrediente.getProduttore().getId();
            }
            case ID_DISTRIBUTORI -> {
                ArrayList<Integer> idDistributori = new ArrayList<>();
                Iterator<Azienda> iterator = ingrediente.getDistributori().iterator();

                while (iterator.hasNext())
                    idDistributori.add(iterator.next().getId());

                return idDistributori;
            }
        }
        System.out.println("campo non gestito");
        return null;

    }

    public boolean undo(){
        if (!undoStack.isEmpty()) {
            IIngredienteOperation operation = undoStack.pop();
            operationIngredienteResult = operation.undo();
            redoStack.push(operation);
            return true;
        }
        return false;
    }

    public boolean redo(){
        if (!redoStack.isEmpty()) {
            IIngredienteOperation operation = redoStack.pop();
            operationIngredienteResult = operation.execute();
            undoStack.push(operation);
            return true;
        }
        return false;
    }

    public void setSelectedIngrediente(Integer idIngrediente){
        Ingrediente ingrediente = ingredienteDAO.findIngredienteById(idIngrediente);
        session.put(UtenteBusiness.SELECTED_OBJECT, ingrediente);
    }

    public boolean isAddedSuccessfully(){
        return OperationIngredienteResult.EDIT_INGREDIENTE_RESULT.ADDED_SUCCESSFULLY.equals(operationIngredienteResult.getEditIngredienteResult());
    }

    public boolean isRemovedSuccessfully(){
        return OperationIngredienteResult.EDIT_INGREDIENTE_RESULT.REMOVED_SUCCESSFULLY.equals(operationIngredienteResult.getEditIngredienteResult());
    }

    public boolean isEditedSuccessfully(){
        return OperationIngredienteResult.EDIT_INGREDIENTE_RESULT.EDITED_SUCCESSFFULLY.equals(operationIngredienteResult.getEditIngredienteResult());
    }

    public boolean isError(){
        return OperationIngredienteResult.EDIT_INGREDIENTE_RESULT.ERROR.equals(operationIngredienteResult.getEditIngredienteResult());
    }

    public String getMessage(){
        return operationIngredienteResult.getMessage();
    }
}
