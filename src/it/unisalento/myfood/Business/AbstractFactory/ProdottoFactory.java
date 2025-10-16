package it.unisalento.myfood.Business.AbstractFactory;

import it.unisalento.myfood.DAO.ArticoloDAO;
import it.unisalento.myfood.DAO.IArticoloDAO;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.TipologiaIngrediente;
import it.unisalento.myfood.model.TipologiaProdotto;

import java.util.List;

public class ProdottoFactory implements IArticoloFactory {
    IArticoloDAO aDAO = ArticoloDAO.getInstance();

    @Override
    public IArticolo crea() {
        return new Prodotto();
    }

    @Override
    public List<IArticolo> filterByTipologiaProdotto(TipologiaProdotto tipologiaProdotto) {
        return aDAO.findProdottoByTipologiaProdotto(tipologiaProdotto);
    }

    @Override
    public List<IArticolo> filterByTipologie(TipologiaProdotto tipologiaProdotto, TipologiaIngrediente tipologiaIngrediente) {
        return aDAO.findProdottoByTipologie(tipologiaProdotto, tipologiaIngrediente);
    }

}
