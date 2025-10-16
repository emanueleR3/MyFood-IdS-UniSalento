package it.unisalento.myfood.DAO;

import it.unisalento.myfood.model.Carrello;

public interface ICarrelloDAO {

    Carrello loadCarrelloPerIdUtente(Integer idUtente);

    boolean addArticoloToCarrello(Integer idArticolo, Integer quantita, Integer idUtente);

    boolean removeArticoloFromCarrello(Integer idArticolo, Integer idUtente);

    boolean removeArticoloFromAll(Integer idArticolo);

    boolean emptyCarrello(Integer idUtente);

    boolean setNewQuantita(Integer idUtente, Integer idArticolo, Integer newQuantita);
}
