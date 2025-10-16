package it.unisalento.myfood.DAO;

import it.unisalento.myfood.model.Azienda;

import java.util.ArrayList;
import java.util.List;

public interface IAziendaDAO {

    Azienda findByPartitaIVA(String partitaIVA);

    Azienda findById(Integer id);

    ArrayList<Azienda> findAll();

    List<Integer> loadProducedIngredienti(Integer idAzienda);

    ArrayList<Integer> loadDistributedIngredienti(Integer idAzienda);

    boolean addAzienda(String nome, String partitaIVA);

    boolean removeAzienda(Integer idAzienda);

    boolean updateAzienda(Integer idAzienda, String newName, String partitaIVA);

    Integer getLastInsertId();

}