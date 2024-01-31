package it.unisalento.myfood.DAO;

import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Utente;

import java.util.ArrayList;

public interface IUtenteDAO {

    Integer getLastInsertId();

    boolean userExists(String email);

    boolean passwordOk(String email,String password);

    boolean isCliente(Integer id);

    boolean isCucina(Integer id);

    boolean isAmministratore(Integer id);

    boolean isEnable(String email);

    Utente caricaCliente(String email);

    Utente caricaAmministratore(String email);

    Utente caricaCucina(String email);

    Utente findByEmail(String username);

    Utente findById(Integer id);

    ArrayList<Utente> findAll();

    ArrayList<Utente> findAllClienti();

    ArrayList<Utente> findAllCucina();

    ArrayList<Utente> findAllAmministratori();

    boolean addUtente(Utente utente);

    boolean update(Utente utente);

    boolean removeByEmail(String email);

    boolean saltExists(String salt);

    boolean checkIfUtenteHasPurchasedArticolo(Utente utente, IArticolo articolo);

    boolean checkIfUtenteHasCommentedArticolo(Utente utente, IArticolo articolo);

    boolean isFirstAccess(String email);

    boolean setCambiaPassword(String email, boolean b);

    boolean changePassword(String email, String hashedPassword);

    boolean setDisabilitatoUtente(Integer idUtente, boolean b);
}
