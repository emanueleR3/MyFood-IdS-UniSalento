package it.unisalento.myfood.DAO;

import it.unisalento.myfood.model.Composite.CommentoCliente;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.IInterazioneUtente;
import it.unisalento.myfood.model.Composite.RispostaAmministratore;
import it.unisalento.myfood.model.Utente;


import java.sql.Timestamp;
import java.util.ArrayList;


public interface IInterazioneUtenteDAO {

    CommentoCliente findCommentoById(Integer idCommento);

    ArrayList<CommentoCliente> findLastCommenti();

    ArrayList<CommentoCliente> findCommentiNotAnswered();

    ArrayList<CommentoCliente> findCommentiByDate(Timestamp dataEOra);

    ArrayList<CommentoCliente> findCommentiByArticolo(Integer idArticolo);

    ArrayList<IInterazioneUtente> caricaInterazioni(Utente utente);

    boolean removeCommento(Integer idCommento);

    boolean addCommento(CommentoCliente commento);

    boolean removeRispostaByIdCommento(Integer idCommento);

    Integer getLastCommentoInsertId();

    Integer getLastRispostaInsertId();

    boolean addRisposta(RispostaAmministratore risposta);

    boolean commentoHasRisposta(Integer idCommento);

    CommentoCliente findCommentoByClienteAndArticolo(Utente utente, IArticolo articolo);

    boolean updateCommento(CommentoCliente commento, String testo, IInterazioneUtente.INDICE_GRADIMENTO indice);
}