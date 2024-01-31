package it.unisalento.myfood.Business;

import it.unisalento.myfood.DAO.IIngredienteDAO;
import it.unisalento.myfood.DAO.IngredienteDAO;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.Ingrediente;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class IngredienteBusiness {

    IIngredienteDAO ingredienteDAO = IngredienteDAO.getInstance();
    private static IngredienteBusiness instance = new IngredienteBusiness();

    public static IngredienteBusiness getInstance(){
        return instance;
    }
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
            mat[i][3] = ingrediente.getProduttore().getNome();  //TODO: per i distributori farei un metodo apparte
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
}
