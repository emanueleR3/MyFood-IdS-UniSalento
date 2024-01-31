package it.unisalento.myfood.Business;

import it.unisalento.myfood.DAO.TipologiaProdottoDAO;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.TipologiaProdotto;

import java.util.ArrayList;
import java.util.Iterator;

public class TipologiaProdottoBusiness {

    public enum CAMPO {
        NOME,
        ID
    }

    private TipologiaProdottoDAO TPDAO = TipologiaProdottoDAO.getInstance();

    private static TipologiaProdottoBusiness instance = new TipologiaProdottoBusiness();

    public static TipologiaProdottoBusiness getInstance(){
        return instance;
    }

    private TipologiaProdottoBusiness(){
    }

    public String[] loadTipologie(){
        ArrayList<TipologiaProdotto> tipologieList = TPDAO.loadTipologia();

        String[] tipologie = new String[tipologieList.size() + 1];

        Iterator<TipologiaProdotto> iterator = tipologieList.iterator();

        int i = 0;
        while (iterator.hasNext()){
            tipologie[i] = iterator.next().getNome();
            i++;
        }
        tipologie[i] = "Menu";
        return tipologie;
    }

    public Object getCampoTipologiaProdotto(TipologiaProdotto tipologiaProdotto, CAMPO nomeCampo) {
        switch (nomeCampo) {
            case ID -> {
                return tipologiaProdotto.getId();
            }
            case NOME -> {
                return tipologiaProdotto.getNome();
            }
        }

        return null;
    }

    public Object getCampoTipologiaViewProdotto(CAMPO nomeCampo) {
        TipologiaProdotto tipologiaProdotto = ((Prodotto) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT)).getTipologiaProdotto();

        switch (nomeCampo) {
            case ID -> {
                return getCampoTipologiaProdotto(tipologiaProdotto, CAMPO.ID);
            }
            case NOME -> {
                return getCampoTipologiaProdotto(tipologiaProdotto, CAMPO.NOME);
            }
        }

        return null;
    }


}
