package it.unisalento.myfood.DAO;

import it.unisalento.myfood.model.Carrello;
import it.unisalento.myfood.model.IOrdine;
import it.unisalento.myfood.model.Ordine;

import java.util.ArrayList;
import java.util.HashMap;

public interface IOrdineDAO {

    ArrayList<Ordine> findByState(IOrdine.STATO stato);

    boolean createOrdine(Carrello carrello);

    Ordine findById(Integer id);

    ArrayList<Ordine> findByRecurrent(Integer idCliente);

    ArrayList<Ordine> findByCliente(Integer idCliente);

    ArrayList<Ordine> findAll();

    ArrayList<Ordine> findByClienteAndState(Integer idCliente, IOrdine.STATO stato);

    HashMap<Integer, Integer> articoliPerIdOrdine(Integer id);

    Integer getLastInsertId();

    boolean updateStatoOrdine(Ordine ordine, IOrdine.STATO nuovoStato);

    boolean setRicorrente(Integer idOrdine, boolean ricorrente);

    boolean remove(Ordine ordine);

    boolean updateOrdine(Ordine o);
}