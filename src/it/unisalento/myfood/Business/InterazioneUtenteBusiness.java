package it.unisalento.myfood.Business;

import it.unisalento.myfood.Business.Command.Interazione.AddRisposta;
import it.unisalento.myfood.Business.Command.Interazione.IInterazioneOperation;
import it.unisalento.myfood.Business.Command.Interazione.RemoveCommento;
import it.unisalento.myfood.Business.Command.Interazione.RemoveRisposta;
import it.unisalento.myfood.DAO.*;
import it.unisalento.myfood.model.Composite.CommentoCliente;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.IInterazioneUtente;
import it.unisalento.myfood.model.Composite.RispostaAmministratore;
import it.unisalento.myfood.model.Result.OperationInterazioneResult;
import it.unisalento.myfood.model.Utente;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Stack;

public class InterazioneUtenteBusiness {

    public enum CAMPO_COMMENTO {
        ID,
        NOME_CLIENTE,
        DATA_COMMENTO,
        VALUTAZIONE,
        TESTO
    }

    public enum CAMPO_RISPOSTA {
        TESTO,
        DATA_RISPOSTA,
        ID_COMMENTO,
        NOME_ADMIN,
        COGNOME_ADMIN
    }

    private static InterazioneUtenteBusiness instance = new InterazioneUtenteBusiness();
    private IInterazioneUtenteDAO interazioneUtenteDAO = InterazioneUtenteDAO.getInstance();
    private IArticoloDAO articoloDAO = ArticoloDAO.getInstance();
    private HashMap<String, Object> session = UtenteBusiness.getSession();
    private OperationInterazioneResult operationInterazioneResult;

    private UtenteDAO utenteDAO = UtenteDAO.getInstance();
    private Stack<IInterazioneOperation> undoStack = new Stack<>();
    private Stack<IInterazioneOperation> redoStack = new Stack<>();
    private Integer MAX_CTRL_Z = 10;

    private InterazioneUtenteBusiness(){}

    public static InterazioneUtenteBusiness getInstance(){
        return instance;
    }


    public boolean insertCommento(String testo, String valutazione) {
        Utente u = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER_ID);
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

    public ArrayList<CommentoCliente> findCommentiByArticolo(IArticolo articolo){
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
        Utente utente = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER_ID);

        CommentoCliente commento = interazioneUtenteDAO.findCommentoByClienteAndArticolo(utente, articolo);

        boolean done = interazioneUtenteDAO.updateCommento(commento, testo, indice);

        // FONDAMENTALE
        ArticoloBusiness.getInstance().reloadViewArticoloSession();

        return done;
    }

    public Object[][] getCommentiForSelectedArticolo() {
        ArrayList<CommentoCliente> commenti = interazioneUtenteDAO.findCommentiByArticolo(((IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT)).getId());

        Object[][] mat = new Object[commenti.size()][5];

        ListIterator<CommentoCliente> iterator = commenti.listIterator();

        int i = 0;
        while(iterator.hasNext()){
            CommentoCliente commentoCliente = iterator.next();

            mat[i][0] = commentoCliente.getId();
            mat[i][1] = commentoCliente.getUtente().getNome() + " " + commentoCliente.getUtente().getCognome();
            mat[i][2] = commentoCliente.getDataEOra();
            mat[i][3] = commentoCliente.getIndiceDiGradimento();
            mat[i][4] = commentoCliente.getTesto();

            i++;
        }

        return mat;
    }

    public Object[][] getCommenti(){
        ArrayList<CommentoCliente> commenti = interazioneUtenteDAO.findCommenti();

        Object[][] mat = new Object[commenti.size()][7];

        ListIterator<CommentoCliente> iterator = commenti.listIterator();

        int i = 0;
        while(iterator.hasNext()){
            CommentoCliente commentoCliente = iterator.next();
            IArticolo articolo = articoloDAO.findById(commentoCliente.getIdArticolo());

            mat[i][0] = commentoCliente.getId();
            mat[i][1] = commentoCliente.getUtente().getEmail();
            mat[i][2] = commentoCliente.getTesto();
            mat[i][3] = articolo.getNome();
            mat[i][4] = commentoCliente.getIdArticolo();
            mat[i][5] = commentoCliente.getIndiceDiGradimento();
            mat[i][6] = commentoCliente.getDataEOra();

            i++;
        }

        return mat;
    }

    public Object[][] getRipsosteCommento(Integer idCommento){
        ArrayList<RispostaAmministratore> risposte = interazioneUtenteDAO.findRisposteByIdCommento(idCommento);

        Object[][] mat = new Object[risposte.size()][4];

        ListIterator<RispostaAmministratore> iterator = risposte.listIterator();

        int i = 0;
        while(iterator.hasNext()){
            RispostaAmministratore rispostaAmministratore = iterator.next();

            mat[i][0] = rispostaAmministratore.getId();
            mat[i][1] = rispostaAmministratore.getUtente().getNome() + " " + rispostaAmministratore.getUtente().getCognome();
            mat[i][2] = rispostaAmministratore.getDataEOra();
            mat[i][3] = rispostaAmministratore.getTesto();
            i++;
        }

        return mat;
    }

    public Object[][] getCommentiNotAnswered(){
        ArrayList<CommentoCliente> commenti = interazioneUtenteDAO.findCommentiNotAnswered();

        Object[][] mat = new Object[commenti.size()][7];

        ListIterator<CommentoCliente> iterator = commenti.listIterator();

        int i = 0;
        while(iterator.hasNext()){
            CommentoCliente commentoCliente = iterator.next();
            IArticolo articolo = articoloDAO.findById(commentoCliente.getIdArticolo());

            mat[i][0] = commentoCliente.getId();
            mat[i][1] = commentoCliente.getUtente().getEmail();
            mat[i][2] = commentoCliente.getTesto();
            mat[i][3] = articolo.getNome();
            mat[i][4] = commentoCliente.getIdArticolo();
            mat[i][5] = commentoCliente.getIndiceDiGradimento();
            mat[i][6] = commentoCliente.getDataEOra();

            i++;
        }

        return mat;
    }

    public Object getCampoSelectedCommento(CAMPO_COMMENTO campoCommento) {
        CommentoCliente commentoCliente = (CommentoCliente) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);

        switch (campoCommento) {
            case ID ->{
                return commentoCliente.getId();
            }
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

    public Object getCampoCommento(int index, CAMPO_COMMENTO campoCommento) {
        CommentoCliente commentoCliente;
        if(UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT) instanceof IArticolo){
            ArrayList<CommentoCliente> commenti = ((IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT)).getCommenti();
            commentoCliente = commenti.get(index);
        } else{
            commentoCliente = (CommentoCliente) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        }


        switch (campoCommento) {
            case ID ->{
                return commentoCliente.getId();
            }
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

    public Object getCampoSelectedRisposta(CAMPO_RISPOSTA campoRisposta) {

        RispostaAmministratore rispostaAmministratore = (RispostaAmministratore) session.get(UtenteBusiness.SELECTED_OBJECT);
        Utente admin = utenteDAO.findById(rispostaAmministratore.getUtente().getId());

        switch (campoRisposta) {
            case TESTO -> {
                return rispostaAmministratore.getTesto();
            }
            case DATA_RISPOSTA -> {
                return rispostaAmministratore.getDataEOra().toString();
            }
            case ID_COMMENTO -> {
                return rispostaAmministratore.getCommentoRisposto().getId();
            }
            case NOME_ADMIN -> {
                return admin.getNome();
            }
            case COGNOME_ADMIN -> {
                return admin.getCognome();
            }

        }

        System.out.println("campo non gestito");
        return null;
    }

    public String getValutazioneCommentoViewArticolo() {
        IArticolo articolo = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        Utente utente = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER_ID);

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
        Utente utente = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER_ID);

        CommentoCliente commento = interazioneUtenteDAO.findCommentoByClienteAndArticolo(utente, articolo);

        return commento.getTesto();
    }

    public int removeSelectedArticoloLoggedUserCommento() {
        IArticolo articolo = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        Utente utente = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER_ID);

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


    public void removeCommento(Integer idCommento){

        CommentoCliente commentoCliente = interazioneUtenteDAO.findCommentoById(idCommento);
        IInterazioneOperation removeCommento = new RemoveCommento(commentoCliente);

        operationInterazioneResult = removeCommento.execute();

        if(isRemovedSuccessfully()) {
            undoStack.push(removeCommento);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }


    public void addRiposta(String testo, Integer idCommento){

        Utente amministratore = (Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID);

        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestampLocalDateTime = Timestamp.valueOf(localDateTime);    // dovrebbe essere il tipo giusto per SQL

        CommentoCliente commentoCliente = interazioneUtenteDAO.findCommentoById(idCommento);

        RispostaAmministratore rispostaAmministratore = new RispostaAmministratore(commentoCliente, testo, amministratore, timestampLocalDateTime);

        IInterazioneOperation addRisposta = new AddRisposta(rispostaAmministratore);

        operationInterazioneResult = addRisposta.execute();

        if(isCreatedSuccessfully()) {
            undoStack.push(addRisposta);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }

    public void removeRiposta(Integer idRisposta) {

        RispostaAmministratore rispostaAmministratore = interazioneUtenteDAO.findRispostaById(idRisposta);

        IInterazioneOperation removeRisposta = new RemoveRisposta(rispostaAmministratore);

        operationInterazioneResult = removeRisposta.execute();

        if(isRemovedSuccessfully()) {
            undoStack.push(removeRisposta);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }

    public boolean undo(){
        if (!undoStack.isEmpty()) {
            IInterazioneOperation operation = undoStack.pop();
            operationInterazioneResult = operation.undo();
            redoStack.push(operation);
            return true;
        }
        return false;
    }

    public boolean redo(){
        if (!redoStack.isEmpty()) {
            IInterazioneOperation operation = redoStack.pop();
            operationInterazioneResult = operation.execute();
            undoStack.push(operation);
            return true;
        }
        return false;
    }

    public void setSelectedRisposta(Integer idRisposta){
        RispostaAmministratore rispostaAmministratore = interazioneUtenteDAO.findRispostaById(idRisposta);

        UtenteBusiness.getSession().put(UtenteBusiness.SELECTED_OBJECT, rispostaAmministratore);
    }


    public void setSelectedCommento(Integer idCommento){
        CommentoCliente commentoCliente = interazioneUtenteDAO.findCommentoById(idCommento);

        UtenteBusiness.getSession().put(UtenteBusiness.SELECTED_OBJECT, commentoCliente);

    }

    public boolean commentoHasRisposta(Integer idCommento){
        return interazioneUtenteDAO.commentoHasRisposta(idCommento);
    }

    public String getMessage(){
        return operationInterazioneResult.getMessage();
    }

    public boolean isCreatedSuccessfully(){
        return OperationInterazioneResult.EDIT_INTERAZIONE_RESULT.CREATED_SUCCESSFULLY.equals(operationInterazioneResult.getEditInterazioneResult());
    }

    public boolean isRemovedSuccessfully(){
        return OperationInterazioneResult.EDIT_INTERAZIONE_RESULT.REMOVED_SUCCESSFULLY.equals(operationInterazioneResult.getEditInterazioneResult());
    }
    public boolean isError(){
        return OperationInterazioneResult.EDIT_INTERAZIONE_RESULT.ERROR.equals(operationInterazioneResult.getEditInterazioneResult());
    }


}
