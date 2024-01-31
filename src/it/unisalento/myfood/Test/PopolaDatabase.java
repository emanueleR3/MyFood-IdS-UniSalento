package it.unisalento.myfood.Test;

import it.unisalento.myfood.Business.InvalidFormatException;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.DAO.*;
import it.unisalento.myfood.model.*;
import it.unisalento.myfood.model.Composite.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class PopolaDatabase {

    private static final UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();
    private static final UtenteDAO utenteDAO = UtenteDAO.getInstance();
    private static final OrdineDAO ordineDAO = OrdineDAO.getInstance();
    private static final CarrelloDAO carrelloDAO = CarrelloDAO.getInstance();
    private static final InterazioneUtenteDAO interazioneUtenteDAO = InterazioneUtenteDAO.getInstance();
    private static final TipologiaIngredienteDAO tipologiaIngredienteDAO = TipologiaIngredienteDAO.getInstance();
    private static final AziendaDAO aziendaDAO = AziendaDAO.getInstance();
    private static final ArticoloDAO articoloDAO = ArticoloDAO.getInstance();
    private static final IngredienteDAO ingredienteDAO = IngredienteDAO.getInstance();

    // TABELLA UTENTE

    /**
     *
     * @return restituisce una lista di utenti inseriti (1 cliente, 1 cucina, 1 amministratore)
     */
    public static ArrayList<Utente> popolaUtente() {
        ArrayList<Utente> utentiInseriti = new ArrayList<>();

        // Creo un cliente
        try {
            utenteBusiness.signUpCliente("emanuele.romano.03@gmail.com", "Emanuele", "Romano", "3331234567", "2003-02-25", "Studente", "Via Enrico Fermi, 8");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        Utente utente = utenteDAO.findByEmail("emanuele.romano.03@gmail.com");

        utentiInseriti.add(utente);

        //Creo un utente cucina
        try {
            utenteBusiness.signUpCucina("maildicarlocracco@gmail.com", "Carlo", "Cracco", "3331112347", "1998-02-25", "Cuoco");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        utente = utenteDAO.findByEmail("maildicarlocracco@gmail.com");

        utentiInseriti.add(utente);

        //Creo un utente amministratore
        try {
            utenteBusiness.signUpAmministratore("andrea.barone@gmail.com", "Andrea", "Barone", "3337654321", "2002-11-24", "Ingegnere");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        utente = utenteDAO.findByEmail("andrea.barone@gmail.com");

        utentiInseriti.add(utente);

        return utentiInseriti;
    }

    /**
     *
     * @param utenti Gli utenti da rimuovere
     */
    public static void svuotaUtente(ArrayList<Utente> utenti) {
        for (Utente u : utenti) {
            utenteDAO.removeByEmail(u.getEmail());
        }
    }

    /**
     *
     * @return restituisce l'Utente Cliente creato
     */
    public static Utente creaCliente() {
        try {
            utenteBusiness.signUpCliente("emanuele.romano.03@gmail.com", "Emanuele", "Romano", "3331234567", "2003-02-25", "Studente", "Via Enrico Fermi, 8");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        return utenteDAO.findByEmail("emanuele.romano.03@gmail.com");
    }

    /**
     *
     * @param utente
     */
    public static void rimuoviUtente(Utente utente) {
        utenteDAO.removeByEmail(utente.getEmail());
    }

    public static Utente creaCucina() {
        try {
            utenteBusiness.signUpCucina("emanuele.romano@liceobanzi.edu.it", "Carlo", "Cracco", "3331234567", "2003-02-25", "Cuoco");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        return utenteDAO.findByEmail("emanuele.romano2@studenti.unisalento.it");
    }

    public static Utente creaAmministratore() {
        try {
            utenteBusiness.signUpAmministratore("emanuele.romano2@studenti.unisalento.it", "Emanuele", "Romano", "3331234566", "2003-02-25", "Ingegnere");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        return utenteDAO.findByEmail("emanuele.romano2@studenti.unisalento.it");
    }

    // TABELLA ORDINE

    /**
     *
     * @param carrello
     * @return restituisce un ordine di un cliente composto da un articolo con quantità passata per parametro
     */
    public static Ordine creaOrdine(Carrello carrello) {
        ordineDAO.createOrdine(carrello);

        return ordineDAO.findById(ordineDAO.getLastInsertId());
    }

    /**
     *
     * @param ordine
     */
    public static void svuotaOrdine(Ordine ordine) {
        ordineDAO.remove(ordine);
    }

    // TABELLA CARRELLO (Cliente_has_Articolo)

    /**
     *
     * @param cliente
     * @param articolo
     * @param quantita
     * @return restituisce un carrello di un cliente composto da un articolo con quantità passata per parametro
     */
    public static Carrello creaCarrello(Utente cliente, IArticolo articolo, int quantita) {
        HashMap<Integer, Integer> articoli = new HashMap<>();
        articoli.put(articolo.getId(), quantita);

        Carrello carrello = new Carrello();

        carrello.setArticoli(articoli);
        carrello.setTotale(articolo.getPrezzo() * quantita);
        carrello.setCliente(cliente);

        carrelloDAO.addArticoloToCarrello(articolo.getId(), quantita, cliente.getId());

        return carrello;
    }

    /**
     *
      * @param cliente
     */
    public static void svuotaCarrello(Utente cliente) {
        carrelloDAO.emptyCarrello(cliente.getId());
    }

    // TABELLA COMMENTO

    /**
     *
     * @param articolo
     * @param cliente
     * @return restituisce il commento creato
     */
    public static IInterazioneUtente creaCommento(IArticolo articolo, Utente cliente) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestampLocalDateTime = Timestamp.valueOf(localDateTime);
        CommentoCliente commento = new CommentoCliente();

        commento.setCliente(cliente);
        commento.setIdArticolo(articolo.getId());
        commento.setIndiceDiGradimento(IInterazioneUtente.INDICE_GRADIMENTO.CINQUE);
        commento.setTesto("Questo prodotto di prova è molto buono!");
        commento.setDataEOra(timestampLocalDateTime);

        interazioneUtenteDAO.addCommento(commento);
        commento.setIdCommento(interazioneUtenteDAO.getLastCommentoInsertId());

        return commento;
    }

    /**
     *
     * @param commento
     */
    public static void svuotaCommento(CommentoCliente commento) {
        interazioneUtenteDAO.removeCommento(commento.getId());
    }

    // TABELLA TIPOLOGIA INGREDIENTE

    public static TipologiaIngrediente creaTipologiaIngrediente(String nome) {
        tipologiaIngredienteDAO.addTipologia(nome);

        return tipologiaIngredienteDAO.findTipologiaByNome(nome);
    }

    public static void eliminaTipologiaIngrediente(TipologiaIngrediente tipologiaIngrediente) {
        tipologiaIngredienteDAO.removeTipologia(tipologiaIngrediente.getId());
    }

    // TABELLA AZIENDA

    public static Azienda createAzienda() {
        aziendaDAO.addAzienda("Ladisa S.R.L.", "05282230720");

        return aziendaDAO.findByPartitaIVA("05282230720");
    }

    public static void eliminaAzienda(Azienda azienda) {
        aziendaDAO.removeAzienda(azienda.getId());
    }

    // TABELLA Prodotto_has_Ingrediente

    public static void setIngredientiPerProdotto(Prodotto prodotto, ArrayList<Ingrediente> ingredienti) {
        for (Ingrediente i : ingredienti) {
            articoloDAO.addIngredienteToProdotto(i.getId(), prodotto.getId());
        }
    }

    public static void svuotaIngredientiPerProdotto(Prodotto prodotto) {
        ArrayList<Ingrediente> ingredienti = prodotto.getIngredienti();

        for (Ingrediente i : ingredienti) {
            articoloDAO.removeIngredienteFromProdotto(i.getId(), prodotto.getId());
        }
    }

    // TABELLA INGREDIENTE

    public static Ingrediente creaIngrediente(TipologiaIngrediente tipologiaIngrediente, String nome, Azienda produttore, ArrayList<Azienda> distributori) {
        Ingrediente ingrediente = new Ingrediente(nome, tipologiaIngrediente, produttore, distributori);

        ingredienteDAO.addIngrediente(ingrediente);

        return ingredienteDAO.findIngredienteById(ingredienteDAO.getLastInsertId());
    }

    public static void eliminaIngrediente(Ingrediente ingrediente) {
        ingredienteDAO.removeIngrediente(ingrediente.getId());
    }

    // TABELLA MENU

    public static Menu creaMenu(ArrayList<IArticolo> articoli, ArrayList<CommentoCliente> commenti) {
        Menu menu = new Menu("Menu di Test", "[...]", 80, articoli, 0.20f, commenti);

        articoloDAO.addArticolo(menu);

        return (Menu) articoloDAO.findById(articoloDAO.getLastInsertId());
    }

    public static void eliminaMenu(Menu menu) {
        articoloDAO.removeArticoloRecursive(menu);
    }
}