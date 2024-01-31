package it.unisalento.myfood.Business;

import it.unisalento.myfood.DAO.IInterazioneUtenteDAO;
import it.unisalento.myfood.DAO.InterazioneUtenteDAO;
import it.unisalento.myfood.model.Composite.CommentoCliente;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.IInterazioneUtente;
import it.unisalento.myfood.model.Utente;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class InterazioneUtenteBusiness {

    public enum CAMPO_COMMENTO {
        NOME_CLIENTE,
        DATA_COMMENTO,
        VALUTAZIONE,
        TESTO;

    }
    private static InterazioneUtenteBusiness instance = new InterazioneUtenteBusiness();
    private IInterazioneUtenteDAO interazioneUtenteDAO = InterazioneUtenteDAO.getInstance();

    private InterazioneUtenteBusiness(){}
    public static InterazioneUtenteBusiness getInstance(){
        return instance;
    }
    public boolean insertCommento(String testo, String valutazione) {
        Utente u = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER);
        IArticolo a = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);

        // Prende la data dal sistema quando viene invocato il metodo
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestampLocalDateTime = Timestamp.valueOf(localDateTime);    // dovrebbe essere il tipo giusto per SQL

        CommentoCliente commentoCliente = new CommentoCliente();

        commentoCliente.setCliente(u);
        commentoCliente.setTesto(testo);
        commentoCliente.setIdArticolo(a.getId());

        IInterazioneUtente.INDICE_GRADIMENTO indice = null;
        switch (valutazione) {
            case "1" -> indice = IInterazioneUtente.INDICE_GRADIMENTO.UNO;
            case "2" -> indice = IInterazioneUtente.INDICE_GRADIMENTO.DUE;
            case "3" -> indice = IInterazioneUtente.INDICE_GRADIMENTO.TRE;
            case "4" -> indice = IInterazioneUtente.INDICE_GRADIMENTO.QUATTRO;
            case "5" -> indice = IInterazioneUtente.INDICE_GRADIMENTO.CINQUE;
        }
        commentoCliente.setIndiceDiGradimento(indice);
        commentoCliente.setDataEOra(timestampLocalDateTime);

        boolean done = interazioneUtenteDAO.addCommento(commentoCliente);

        // FONDAMENTALE
        ArticoloBusiness.getInstance().reloadViewArticoloSession();

        return done;
    }

    public ArrayList<CommentoCliente> findCommentiByArticolo(IArticolo articolo){  //TODO: in generale si potrebbe far ritornare a questi metodi delle stringhe piuttosto che degli oggetti del model in modo da rendere indipendente la view dal model
        return interazioneUtenteDAO.findCommentiByArticolo(articolo.getId());
    }

    public boolean selectedArticoloHasCommenti() {
        return !((IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT)).getCommenti().isEmpty();
    }

    public int getNumberOfInterazioniForSelectedArticolo() {
        ArrayList<CommentoCliente> commenti = ((IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT)).getCommenti();

        int numberOfCommenti = commenti.size();
        int numberOfRisposte = 0;

        for (CommentoCliente c : commenti) {
            if (interazioneUtenteDAO.commentoHasRisposta(c.getId())) numberOfRisposte++;
        }

        return numberOfCommenti + numberOfRisposte;
    }

    public int getNumberOfCommentiForSelectedArticolo() {
        return ((IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT)).getCommenti().size();
    }

    public boolean updateCommento(String testo, String valutazione) {
        IInterazioneUtente.INDICE_GRADIMENTO indice = null;
        switch (valutazione) {
            case "1" -> indice = IInterazioneUtente.INDICE_GRADIMENTO.UNO;
            case "2" -> indice = IInterazioneUtente.INDICE_GRADIMENTO.DUE;
            case "3" -> indice = IInterazioneUtente.INDICE_GRADIMENTO.TRE;
            case "4" -> indice = IInterazioneUtente.INDICE_GRADIMENTO.QUATTRO;
            case "5" -> indice = IInterazioneUtente.INDICE_GRADIMENTO.CINQUE;
        }

        IArticolo articolo = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        Utente utente = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER);

        CommentoCliente commento = interazioneUtenteDAO.findCommentoByClienteAndArticolo(utente, articolo);

        boolean done = interazioneUtenteDAO.updateCommento(commento, testo, indice);

        // FONDAMENTALE
        ArticoloBusiness.getInstance().reloadViewArticoloSession();

        return done;
    }

    public Object getCampoCommento(int index, CAMPO_COMMENTO campoCommento) {
        ArrayList<CommentoCliente> commenti = ((IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT)).getCommenti();
        CommentoCliente commentoCliente = commenti.get(index);

        switch (campoCommento) {
            case TESTO -> {
                return commentoCliente.getTesto();
            }
            case DATA_COMMENTO -> {
                return commentoCliente.getDataEOra();
            }
            case NOME_CLIENTE -> {
                return commentoCliente.getUtente().getNome();
            }
            case VALUTAZIONE -> {
                IInterazioneUtente.INDICE_GRADIMENTO indiceGradimento = commentoCliente.getIndiceDiGradimento();
                switch (indiceGradimento) {
                    case UNO -> {
                        return 1;
                    }
                    case DUE -> {
                        return 2;
                    }
                    case TRE -> {
                        return 3;
                    }
                    case QUATTRO -> {
                        return 4;
                    }
                    case CINQUE -> {
                        return 5;
                    }
                }
            }
        }

        return null;
    }

    public String getValutazioneCommentoViewArticolo() {
        IArticolo articolo = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        Utente utente = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER);

        CommentoCliente commento = interazioneUtenteDAO.findCommentoByClienteAndArticolo(utente, articolo);

        IInterazioneUtente.INDICE_GRADIMENTO indiceGradimento = commento.getIndiceDiGradimento();
        switch (indiceGradimento) {
            case UNO -> {
                return "1";
            }
            case DUE -> {
                return "2";
            }
            case TRE -> {
                return "3";
            }
            case QUATTRO -> {
                return "4";
            }
            case CINQUE -> {
                return "5";
            }
        }

        return null;
    }

    public String getTestoCommentoViewArticolo() {
        IArticolo articolo = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        Utente utente = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER);

        CommentoCliente commento = interazioneUtenteDAO.findCommentoByClienteAndArticolo(utente, articolo);

        return commento.getTesto();
    }

    public int removeCommento() {
        IArticolo articolo = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        Utente utente = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER);

        CommentoCliente commento = InterazioneUtenteDAO.getInstance().findCommentoByClienteAndArticolo(utente, articolo);

        Integer id;
        try {
            id = commento.getId();
        } catch (NullPointerException e) {
            return -1;
        }

        boolean done = interazioneUtenteDAO.removeCommento(id);

        // FONDAMENTALE
        ArticoloBusiness.getInstance().reloadViewArticoloSession();

        return done ? 1 : 0;
    }
}
