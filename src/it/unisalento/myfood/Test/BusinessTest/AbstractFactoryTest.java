package it.unisalento.myfood.Test.BusinessTest;

import it.unisalento.myfood.Business.AbstractFactory.IArticoloFactory;
import it.unisalento.myfood.Business.AbstractFactory.FactoryProvider;
import it.unisalento.myfood.Business.AbstractFactory.MenuFactory;
import it.unisalento.myfood.Business.AbstractFactory.ProdottoFactory;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.Menu;
import it.unisalento.myfood.model.Composite.Prodotto;
import org.junit.Assert;
import org.junit.Test;

public class AbstractFactoryTest {

    @Test
    public void creaProdottoTest() {
        FactoryProvider.FactoryType type = FactoryProvider.FactoryType.PRODOTTO;
        IArticoloFactory factory = FactoryProvider.getFactory(type);
        Assert.assertTrue(factory instanceof ProdottoFactory);

        IArticolo articolo = factory.crea();
        Assert.assertTrue(articolo instanceof Prodotto);
    }

    @Test
    public void creaMenuTest() {
        FactoryProvider.FactoryType type = FactoryProvider.FactoryType.MENU;
        IArticoloFactory factory = FactoryProvider.getFactory(type);
        Assert.assertTrue(factory instanceof MenuFactory);

        IArticolo articolo = factory.crea();
        Assert.assertTrue(articolo instanceof Menu);
    }
}
