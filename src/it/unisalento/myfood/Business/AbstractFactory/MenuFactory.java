package it.unisalento.myfood.Business.AbstractFactory;

import it.unisalento.myfood.DAO.ArticoloDAO;
import it.unisalento.myfood.DAO.IArticoloDAO;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.Menu;
import it.unisalento.myfood.model.TipologiaIngrediente;
import it.unisalento.myfood.model.TipologiaProdotto;

import java.util.List;

public class MenuFactory implements IArticoloFactory {
    IArticoloDAO aDAO = ArticoloDAO.getInstance();

    @Override
    public IArticolo crea() {
        return new Menu();
    }

    @Override
    public List<IArticolo> filterByTipologiaProdotto(TipologiaProdotto tipoProdotto) {

        return aDAO.findMenu();
    }

    //la tipologia prodotto è sempre menu
    @Override
    public List<IArticolo> filterByTipologie(TipologiaProdotto tipologiaProdotto, TipologiaIngrediente tipologiaIngrediente) {
        return aDAO.findMenuContainsTipologiaIngrediente(tipologiaIngrediente);
    }

}

