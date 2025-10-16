package it.unisalento.myfood.DAO;

import it.unisalento.myfood.model.TipologiaProdotto;

import java.util.ArrayList;

public interface ITipologiaProdottoDAO {

    boolean addTipologia(String tipologia);

    boolean editTipologia(Integer id, String newName);

    boolean removeTipologia(Integer id);

    ArrayList<TipologiaProdotto> loadTipologia();

    TipologiaProdotto findTipologiaById(Integer idTipologia);

    TipologiaProdotto findTipologiaByName(String nome);
}