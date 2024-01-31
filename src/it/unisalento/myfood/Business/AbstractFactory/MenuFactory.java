package it.unisalento.myfood.Business.AbstractFactory;

import it.unisalento.myfood.DAO.ArticoloDAO;
import it.unisalento.myfood.DAO.IArticoloDAO;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.Menu;
import it.unisalento.myfood.model.Ingrediente;
import it.unisalento.myfood.model.TipologiaProdotto;

import java.util.List;

public class MenuFactory implements IArticoloFactory {
    IArticoloDAO aDAO = ArticoloDAO.getInstance();

    @Override
    public IArticolo crea() {
        return new Menu();
    }

    @Override
    public List<IArticolo> filterByTipologia(TipologiaProdotto tipoProdotto) {

        return aDAO.findMenu();
    }

    @Override
    public List<IArticolo> filterByTipologiaEIngrediente(TipologiaProdotto tipologiaProdotto, Ingrediente ingrediente) {
        return aDAO.findMenuContains(ingrediente);
    }
}

