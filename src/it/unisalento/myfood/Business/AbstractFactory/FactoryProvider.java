package it.unisalento.myfood.Business.AbstractFactory;

public class FactoryProvider {

    public enum FactoryType {PRODOTTO, MENU}

    public static IArticoloFactory getFactory(FactoryType type) {
        if(type == null) {
            return null;
        }

        switch (type) {
            case PRODOTTO: return new ProdottoFactory();
            case MENU: return new MenuFactory();
            default: return null;
        }
    }
}
