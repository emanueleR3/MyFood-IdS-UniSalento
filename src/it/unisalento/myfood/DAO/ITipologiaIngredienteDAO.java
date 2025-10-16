package it.unisalento.myfood.DAO;

import it.unisalento.myfood.model.TipologiaIngrediente;

import java.util.ArrayList;

public interface ITipologiaIngredienteDAO {

    int getLastInsertId();

    boolean addTipologia(String tipologia);

    boolean editTipologia(Integer id, String newName);

    boolean removeTipologia(Integer id);

    ArrayList<TipologiaIngrediente> loadTipologia();

    TipologiaIngrediente findTipologiaById(Integer id);

    TipologiaIngrediente findTipologiaByNome(String nome);

    boolean tipologiaExists(String nome);

}
