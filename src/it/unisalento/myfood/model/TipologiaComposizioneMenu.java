package it.unisalento.myfood.model;

import java.util.ArrayList;

public class TipologiaComposizioneMenu {

    private String nomeComposizione;
    private ArrayList<TipologiaProdotto> tipologieAccettate;

    public TipologiaComposizioneMenu() {}

    public TipologiaComposizioneMenu(String nomeComposizione, ArrayList<TipologiaProdotto> tipologieAccettate) {
        this.nomeComposizione = nomeComposizione;
        this.tipologieAccettate = tipologieAccettate;
    }

    public String getNomeComposizione() {
        return nomeComposizione;
    }

    public void setNomeComposizione(String nomeComposizione) {
        this.nomeComposizione = nomeComposizione;
    }

    public ArrayList<TipologiaProdotto> getTipologieAccettate() {
        return tipologieAccettate;
    }

    public void setTipologieAccettate(ArrayList<TipologiaProdotto> tipologieAccettate) {
        this.tipologieAccettate = tipologieAccettate;
    }
}
