package it.unisalento.myfood.DAO;

import it.unisalento.myfood.model.*;
import it.unisalento.myfood.model.Composite.IArticolo;

import java.util.List;

public interface IArticoloDAO {

    List<IArticolo> findProdottoByTipologiaProdotto(TipologiaProdotto tipologiaProdotto);

    List<IArticolo> findProdottoByTipologie(TipologiaProdotto tipologiaProdotto, TipologiaIngrediente tipologiaIngrediente);

    List<IArticolo> findMenu();

    List<IArticolo> findMenuContainsTipologiaIngrediente(TipologiaIngrediente tipologiaIngrediente);

    IArticolo findById(Integer idArticolo);

    boolean addArticolo(IArticolo articolo);

    boolean removeArticolo(IArticolo articolo);

    //boolean removeArticoloRecursive(IArticolo articolo);

    boolean editArticolo(IArticolo articolo);

    boolean addArticoloToMenu(Integer idMenu, Integer IdArticolo);

    boolean removeArticoloFromMenu(Integer idMenu, Integer idArticolo);

    boolean addIngredienteToProdotto(Integer idIngrediente, Integer idProdotto);

    boolean removeIngredienteFromProdotto(Integer idIngrediente, Integer idProdotto);

    boolean updateDisponibilitaAfterOrdine(Ordine ordine);

    int removeArticoloFromTable(Integer idArticolo, String table);

    Integer getLastInsertId();
}
