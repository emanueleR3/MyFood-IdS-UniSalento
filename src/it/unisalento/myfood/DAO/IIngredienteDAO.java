package it.unisalento.myfood.DAO;

import it.unisalento.myfood.model.Azienda;
import it.unisalento.myfood.model.Ingrediente;

import java.util.ArrayList;
import java.util.List;


public interface IIngredienteDAO {

    List<Ingrediente> getIngredientiPerProdotto(Integer idProdotto);

    Ingrediente findIngredienteById(Integer id);

    ArrayList<Ingrediente> findAll();

    ArrayList<Azienda> getDistributoriPerIngrediente(Integer idIngrediente);

    boolean addIngrediente(Ingrediente ingrediente);

    boolean removeIngrediente(Integer idIngrediente);

    boolean editIngrediente(Ingrediente ingrediente);

    boolean addDistributoreToIngrediente(Integer idIngrediente, Integer idAzienda);

    boolean removeDistributoreFromIngrediente(Integer idIngrediente, Integer idAzienda);

    Integer getLastInsertId();
}
