package it.unisalento.myfood.DAO;

import it.unisalento.myfood.DBInterface.Command.DbOperationExecutor;
import it.unisalento.myfood.DBInterface.Command.IDbOperation;
import it.unisalento.myfood.DBInterface.Command.ReadOperation;
import it.unisalento.myfood.DBInterface.Command.WriteOperation;
import it.unisalento.myfood.model.Composite.CommentoCliente;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.IInterazioneUtente;
import it.unisalento.myfood.model.Composite.RispostaAmministratore;
import it.unisalento.myfood.model.Utente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class InterazioneUtenteDAO implements IInterazioneUtenteDAO {

    private static InterazioneUtenteDAO instance = new InterazioneUtenteDAO();

    private IUtenteDAO UDAO = UtenteDAO.getInstance();  //TODO: l'alternativa è tenere solo l'id nel model e modificare mezza classe
    private InterazioneUtenteDAO(){
    }

    public static InterazioneUtenteDAO getInstance(){
        return instance;
    }

    @Override  //vale per la tabella commento
    public Integer getLastCommentoInsertId(){
        int lastInsertId = 0;

        String sql = "SELECT MAX(idCommento) as max FROM Commento;";
        IDbOperation readOp = new ReadOperation(sql);
        DbOperationExecutor executor = new DbOperationExecutor();
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            rs.next();
            lastInsertId = rs.getInt("max");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun articolo nel database");
        }

        return lastInsertId;
    }

    @Override  //vale per la tabella risposta
    public Integer getLastRispostaInsertId(){
        int lastInsertId = 0;

        String sql = "SELECT MAX(idRisposta) as max FROM Risposta;";
        IDbOperation readOp = new ReadOperation(sql);
        DbOperationExecutor executor = new DbOperationExecutor();
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            rs.next();
            lastInsertId = rs.getInt("max");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun articolo nel database");
        }

        return lastInsertId;
    }

    @Override
    public CommentoCliente findCommentoById(Integer idCommento) {
        String sql = "SELECT * FROM Commento WHERE idCommento = " + idCommento;

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        CommentoCliente commento = new CommentoCliente();

        try {

            while (rs.next()) {

                commento.setIdCommento(idCommento);
                commento.setTesto(rs.getString("testo"));
                commento.setIndiceDiGradimento(IInterazioneUtente.INDICE_GRADIMENTO.valueOf(rs.getString("indiceGradimento")));
                commento.setIdArticolo(rs.getInt("Articolo_idArticolo"));
                commento.setDataEOra(rs.getTimestamp("dataEOra"));
                commento.setCliente(UDAO.findById(rs.getInt("Cliente_Utente_idUtente")));
            }
            return commento;

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun commento con questo id");
        }

        return null;
    }

    @Override
    public ArrayList<CommentoCliente> findCommentiNotAnswered() {
        ArrayList<CommentoCliente> commenti = new ArrayList<>();
        String sql = "SELECT idCommento " +
                "FROM Commento AS C LEFT JOIN Risposta AS R ON C.idCommento = R.Commento_idCommento " +
                "WHERE idRisposta IS NULL";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while(rs.next()){
                commenti.add(findCommentoById(rs.getInt("idCommento")));
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna domanda senza risposta");
        }

        return commenti;
    }

    @Override
    public ArrayList<CommentoCliente> findLastCommenti() {
        ArrayList<CommentoCliente> commenti = new ArrayList<>();
        String sql = "SELECT idCommento FROM Commento ORDER BY dataEOra DESC LIMIT 30 ";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while(rs.next()){
                commenti.add(findCommentoById(rs.getInt("idCommento")));
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna tipologia con questo id");
        }

        return commenti;
    }

    @Override
    public ArrayList<CommentoCliente> findCommentiByDate(Timestamp dataEOra) {
        ArrayList<CommentoCliente> commenti = new ArrayList<>();
        String sql = "SELECT idCommento FROM Commento WHERE dataEOra = '" + dataEOra + "'" ;

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while(rs.next()){
                commenti.add(findCommentoById(rs.getInt("idCommento")));
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna tipologia con questo id");
        }

        return commenti;
    }

    @Override
    public ArrayList<CommentoCliente> findCommentiByArticolo(Integer idArticolo) {
        ArrayList<CommentoCliente> commenti = new ArrayList<>();
        String sql = "SELECT idCommento FROM Commento WHERE Articolo_idArticolo = " + idArticolo + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while(rs.next()){
                commenti.add(findCommentoById(rs.getInt("idCommento")));
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna tipologia con questo id");
        }

        return commenti;
    }

    @Override
    public boolean addCommento(CommentoCliente commento) {

        String sql = "INSERT INTO Commento (testo, indiceGradimento, Articolo_idArticolo, dataEOra, Cliente_Utente_idUtente) " +
                "VALUES ('" + commento.getTesto() + "',  '" + commento.getIndiceDiGradimento() + "' , " + commento.getIdArticolo() + ", '" + commento.getDataEOra() + "', " + commento.getUtente().getId() + ");";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        Integer rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }

    @Override
    public boolean addRisposta(RispostaAmministratore risposta) {

        String sql = "INSERT INTO Risposta (testo, dataEOra, Amministratore_Utente_idUtente, Commento_idCommento) " +
                "VALUES ('" + risposta.getTesto() + "', '" + risposta.getDataEOra() + "', " + risposta.getUtente().getId() + ", " + risposta.getIdCommentoRisposto() + ");";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        Integer rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }

    @Override
    public boolean removeCommento(Integer idCommento) {
        String sql = "DELETE FROM Commento WHERE idCommento = " + idCommento + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        if (removeRispostaByIdCommento(idCommento))
            rowsAffected++;

        return rowsAffected == 1;   // TODO CAMBIARE, SE CI SONO RISPOSTE NON ESCE TRUE
    }

    @Override
    public boolean removeRispostaByIdCommento(Integer idCommento) {
        String sql = "DELETE FROM Risposta WHERE Commento_idCommento = " + idCommento + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();
        return rowsAffected == 1;
    }

    @Override
    public ArrayList<IInterazioneUtente> caricaInterazioni(Utente utente) {
        DbOperationExecutor executor = new DbOperationExecutor();
        ArrayList<IInterazioneUtente> interazioni = null;

        try {
            if (utente.getRuolo().equals(Utente.RUOLO.CLIENTE)) {
                interazioni = new ArrayList<>();

                String sql = "SELECT * FROM Cliente as Cl " +
                        "INNER JOIN Commento AS Co " +
                        "ON Cl.Utente_idUtente = Co.Cliente_Utente_idUtente " +
                        "WHERE Cl.Utente_idUtente = " + utente.getId() + ";";

                IDbOperation readOp = new ReadOperation(sql);
                ResultSet rs = executor.executeOperation(readOp).getResultSet();

                while (rs.next()) {
                    CommentoCliente c = new CommentoCliente();

                    c.setCliente(utente);
                    c.setIdCommento(rs.getInt("idCommento"));
                    c.setIdArticolo(rs.getInt("Articolo_idArticolo"));
                    c.setDataEOra(rs.getTimestamp("dataEOra"));
                    c.setTesto(rs.getString("testo"));
                    c.setIndiceDiGradimento(IInterazioneUtente.INDICE_GRADIMENTO.valueOf(rs.getString("indiceGradimento")));

                    interazioni.add(c);
                }
            } else if (utente.getRuolo().equals(Utente.RUOLO.AMMINISTRATORE)) {
                interazioni = new ArrayList<>();

                String sql = "SELECT * FROM Amministratore as A " +
                        "INNER JOIN Risposta AS R " +
                        "ON A.Utente_idUtente = R.Amministratore_Utente_idUtente " +
                        "WHERE A.Utente_idUtente = " + utente.getId() + ";";

                IDbOperation readOp = new ReadOperation(sql);
                ResultSet rs = executor.executeOperation(readOp).getResultSet();

                while (rs.next()) {
                    RispostaAmministratore r = new RispostaAmministratore();

                    r.setAmministratore(utente);
                    r.setIdRiposta(rs.getInt("idRisposta"));
                    r.setIdCommentoRisposto(rs.getInt("Commento_idCommento"));
                    r.setDataEOra(rs.getTimestamp("dataEOra"));
                    r.setTesto(rs.getString("testo"));

                    interazioni.add(r);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna interazione per questo utente: " + utente);
        }

        // Ruolo: cucina
        return interazioni;
    }

    // TODO: testare
    @Override
    public boolean commentoHasRisposta(Integer idCommento) {
        DbOperationExecutor executor = new DbOperationExecutor();

        String sql = "SELECT COUNT(*) AS C FROM Risposta WHERE Commento_idCommento = " + idCommento + ";";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        boolean rispostaExists = false;

        try {
            rs.next();
            rispostaExists = rs.getInt("C") == 1;

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna tipologia con questo id");
        }

        return rispostaExists;
    }

    @Override
    public CommentoCliente findCommentoByClienteAndArticolo(Utente utente, IArticolo articolo) {
        DbOperationExecutor executor = new DbOperationExecutor();

        String sql = "SELECT idCommento FROM Commento " +
                "WHERE Cliente_Utente_idUtente = " + utente.getId() + " " +
                "AND Articolo_idArticolo = " + articolo.getId() + ";";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            CommentoCliente commentoCliente = null;
            while (rs.next()) {
                commentoCliente = findCommentoById(rs.getInt("idCommento"));
            }
            return commentoCliente;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non sono state trovate relazioni tra questo utente e questo articolo");
        }

        return null;
    }

    @Override
    public boolean updateCommento(CommentoCliente commento, String testo, IInterazioneUtente.INDICE_GRADIMENTO indice) {
        DbOperationExecutor executor = new DbOperationExecutor();

        String sql = "UPDATE Commento SET testo = '" + testo + "', indiceGradimento = '" + indice + "' " +
                "WHERE idCommento = " + commento.getId() + ";";

        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }
}
