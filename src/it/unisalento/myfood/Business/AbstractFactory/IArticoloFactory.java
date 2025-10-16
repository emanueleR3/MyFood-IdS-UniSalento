package it.unisalento.myfood.Business.AbstractFactory;

import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.TipologiaIngrediente;
import it.unisalento.myfood.model.TipologiaProdotto;

import java.util.List;

public interface IArticoloFactory {

    IArticolo crea();

    List<IArticolo> filterByTipologiaProdotto(TipologiaProdotto tipoProdotto);

    List<IArticolo> filterByTipologie(TipologiaProdotto tipologiaProdotto, TipologiaIngrediente tipologiaIngrediente);

    //metodo per creazione di una classe che va a prendere i prodotti/menu suggeriti (simili a quello scelto)

    //prevedere altri metodi significativi che posso essere affiancati alla creazione di un prodotto/menu (es. categoria...)
}
