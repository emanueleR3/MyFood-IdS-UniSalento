package it.unisalento.myfood.DAO;

import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.Menu;
import it.unisalento.myfood.model.Ingrediente;
import it.unisalento.myfood.model.Ordine;
import it.unisalento.myfood.model.TipologiaProdotto;
import it.unisalento.myfood.model.Utente;

import java.util.List;

public interface IArticoloDAO {

    List<IArticolo> findProdottoByTipologia(TipologiaProdotto tipologiaProdotto);

    List<IArticolo> findProdottoByTipologiaContains(TipologiaProdotto tipologiaProdotto, Ingrediente ingrediente);

    List<IArticolo> findMenu();

    List<IArticolo> findMenuContains(Ingrediente ingrediente);

    IArticolo findById(Integer idArticolo);

    boolean addArticolo(IArticolo articolo);

    boolean removeArticolo(IArticolo articolo);

    boolean removeArticoloRecursive(IArticolo articolo);

    boolean editArticolo(IArticolo articolo);

    boolean addArticoloToMenu(Integer idMenu, Integer IdArticolo);

    boolean removeArticoloFromMenu(Integer idMenu, Integer idArticolo);

    boolean addIngredienteToProdotto(Integer idIngrediente, Integer idProdotto);

    boolean removeIngredienteFromProdotto(Integer idIngrediente, Integer idProdotto);

    boolean updateDisponibilitaAfterOrdine(Ordine ordine);

    int removeArticoloFromTable(Integer idArticolo, String table);

    public Integer getLastInsertId();
}
