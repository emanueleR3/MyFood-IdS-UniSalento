package it.unisalento.myfood.DAO;

import it.unisalento.myfood.DBInterface.Command.DbOperationExecutor;
import it.unisalento.myfood.DBInterface.Command.IDbOperation;
import it.unisalento.myfood.DBInterface.Command.ReadOperation;
import it.unisalento.myfood.DBInterface.Command.WriteOperation;
import it.unisalento.myfood.DBInterface.DbConnection;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Utente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UtenteDAO implements IUtenteDAO {
    private static UtenteDAO instance = new UtenteDAO();

    private UtenteDAO() {}

    public static UtenteDAO getInstance() {
        return instance;
    }


    @Override
    public Integer getLastInsertId(){

        int lastInsertId = 1;
        String sql = "SELECT MAX(idUtente) as max " +
                "FROM Utente;";
        IDbOperation readOp = new ReadOperation(sql);
        DbOperationExecutor executor = new DbOperationExecutor();
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {

            if(rs.next()) {
                lastInsertId = rs.getInt("max");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastInsertId;
    }

    @Override
    public boolean userExists(String email) {

        boolean userExists = false;

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT COUNT(*) AS C FROM Utente AS U WHERE U.email = '" + email + "';";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                userExists = rs.getInt("C") == 1;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun utente nel database");
        }

        return userExists;
    }

    @Override
    public boolean passwordOk(String email, String hashedPassword) {

        boolean passwordOk = false;

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT COUNT(*) AS C FROM Utente AS U WHERE U.email = '" + email + "' " +
                "AND U.password = '" + hashedPassword + "';";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                passwordOk = rs.getInt("C") == 1;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun utente nel database");
        }

        return passwordOk;
    }

    @Override
    public boolean isCliente(Integer id) {

        boolean isCliente = false;

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT COUNT(*) AS C FROM Utente AS U " +
                "INNER JOIN Cliente AS CL ON U.idUtente = CL.Utente_idUtente " +
                "WHERE U.idUtente = " + id + ";";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                isCliente = rs.getInt("C") == 1;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun utente nel database");
        }

        return isCliente;
    }

    @Override
    public boolean isAmministratore(Integer id) {
        boolean isAmministratore = false;

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT COUNT(*) AS C FROM Utente AS U " +
                "INNER JOIN Amministratore AS A ON U.idUtente = A.Utente_idUtente " +
                "WHERE U.idUtente = " + id + ";";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                isAmministratore = rs.getInt("C") == 1;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun utente nel database");
        }

        return isAmministratore;
    }

    @Override
    public boolean isCucina(Integer id) {

        boolean isCucina = false;

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT COUNT(*) AS C FROM Utente AS U " +
                "INNER JOIN Cucina AS CU ON U.idUtente = CU.Utente_idUtente " +
                "WHERE U.idUtente = " + id + ";";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                isCucina = rs.getInt("C") == 1;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun utente nel database");
        }

        return isCucina;
    }

    @Override
    public boolean isEnable(String email) {

        boolean isEnable = true;

        String sql = "SELECT disabilitato FROM Cliente AS C " +
                "INNER JOIN Utente as U " +
                "ON U.idUtente = C.Utente_idUtente " +
                "WHERE U.email = '" + email + "';";
        DbOperationExecutor executor = new DbOperationExecutor();

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while (rs.next()) {
                isEnable = rs.getInt("disabilitato") == 0;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun utente nel database");
        }

        return isEnable;
    }

    @Override
    public Utente caricaCliente(String email) {
        // Se il cliente è disabilitato non lo carica
        if (!isEnable(email)) {
            System.out.println("Il cliente è disabilitato");
            return null;
        }

        // Il cliente è abilitato
        Utente u = this.findByEmail(email);

        u.setRuolo(Utente.RUOLO.CLIENTE);
        u.setOrdini(OrdineDAO.getInstance().findByCliente(u.getId()));

        return u;
    }

    @Override
    public Utente caricaAmministratore(String email) {

        String sql = "SELECT Utente_idUtente FROM Amministratore AS A INNER JOIN Utente AS U ON A.Utente_idUtente = U.idUtente WHERE email = '" + email + "'" ;
        DbOperationExecutor executor = new DbOperationExecutor();

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        Utente u;
        try {
            rs.next();
            if(rs.getRow() == 1 ){
                u = this.findByEmail(email);
                u.setRuolo(Utente.RUOLO.AMMINISTRATORE);
                return u;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo questo amministratore nel database");
        }
        return null;
    }

    @Override
    public Utente caricaCucina(String email) {

        String sql = "SELECT Utente_idUtente FROM Cucina AS C INNER JOIN Utente AS U ON C.Utente_idUtente = U.idUtente WHERE email = '" + email + "'" ;
        DbOperationExecutor executor = new DbOperationExecutor();

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        Utente u;
        try {
            rs.next();
            if(rs.getRow() == 1) {
                u = this.findByEmail(email);
                u.setRuolo(Utente.RUOLO.CUCINA);
                return u;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo questo lavoratore nel database");
        }

        return null;
    }



    @Override
    public Utente findByEmail(String email) {

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT idUtente FROM Utente WHERE email = '" + email + "';";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            rs.next();
            if (rs.getRow() == 1) {
                return findById(rs.getInt("idUtente"));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun utente con email = " + email);
        }
        return null;
    }

    @Override
    public Utente findById(Integer id) {

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT * FROM Utente WHERE idUtente = " + id + ";";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            rs.next();
            if (rs.getRow() == 1) {
                Utente utente = new Utente();

                utente.setId(id);
                utente.setNome(rs.getString("nome"));
                utente.setCognome(rs.getString("cognome"));
                utente.setHashedPassword(rs.getString("password"));
                utente.setEmail(rs.getString("email"));
                utente.setDataRegistrazione(rs.getDate("dataRegistrazione"));
                utente.setTelefono(rs.getString("telefono"));
                utente.setDataNascita(rs.getDate("dataDiNascita"));
                utente.setProfessione(rs.getString("professione"));
                utente.setCambiaPassword(rs.getInt("cambiaPassword") == 1);

                byte[] saltBytes = rs.getBytes("salt");
                String saltHex = bytesToHex(saltBytes);
                utente.setSaltHex(saltHex);

                if(isCliente(id)){
                    sql = "SELECT * FROM Utente as U JOIN Cliente as C ON U.idUtente = C.Utente_idUtente WHERE idUtente = " + id + ";";
                    readOp = new ReadOperation(sql);
                    rs = executor.executeOperation(readOp).getResultSet();
                    rs.next();

                    utente.setDisabilitato(rs.getInt("disabilitato") == 1);
                    utente.setResidenza(rs.getString("residenza"));
                    utente.setRuolo(Utente.RUOLO.CLIENTE);
                    utente.setInterazioni(InterazioneUtenteDAO.getInstance().caricaInterazioni(utente));
                    utente.setOrdini(OrdineDAO.getInstance().findByCliente(id));
                } else if(isCucina(id)){
                    utente.setRuolo(Utente.RUOLO.CUCINA);
                    utente.setInterazioni(InterazioneUtenteDAO.getInstance().caricaInterazioni(utente));
                } else if(isAmministratore(id)){
                    utente.setRuolo(Utente.RUOLO.AMMINISTRATORE);
                    utente.setInterazioni(InterazioneUtenteDAO.getInstance().caricaInterazioni(utente));
                }

                return utente;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun utente con id = " + id);
        }

        return null;
    }

    @Override
    public ArrayList<Utente> findAll() {

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT email FROM Utente;";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        String email;
        try {
            ArrayList<Utente> utenti = new ArrayList<>();
            while (rs.next()) {
                 email = rs.getString("email");
                 utenti.add(findByEmail(email));
            }
            return utenti;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun utente nel database");
        }
        return null;
    }

    @Override
    public ArrayList<Utente> findAllClienti() {

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT Utente_idUtente FROM cliente;";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            ArrayList<Utente> utenti = new ArrayList<>();
            while (rs.next()) {
                utenti.add(findById(rs.getInt("Utente_idUtente")));
            }
            return utenti;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun cliente nel database");
        }
        return null;
    }

    @Override
    public ArrayList<Utente> findAllCucina() {

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT Utente_idUtente FROM cucina;";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            ArrayList<Utente> utenti = new ArrayList<>();
            while (rs.next()) {
                utenti.add(findById(rs.getInt("Utente_idUtente")));
            }
            return utenti;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna cucina nel database");
        }
        return null;
    }

    @Override
    public ArrayList<Utente> findAllAmministratori() {
        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT Utente_idUtente FROM amministratore;";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            ArrayList<Utente> utenti = new ArrayList<>();
            while (rs.next()) {
                utenti.add(findById(rs.getInt("Utente_idUtente")));
            }
            return utenti;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun amministratore nel database");
        }
        return null;
    }

    @Override
    public boolean saltExists(String salt) {

        String sql = "SELECT COUNT(*) AS C FROM utente WHERE salt = UNHEX('" + salt + "');";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        int numberOfRecords = 0;
        try {
            if (rs.next()) {
                numberOfRecords = rs.getInt("C");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun salt nel database");
        }

        return numberOfRecords == 1;
    }

    @Override
    public boolean addUtente(Utente utente) {

        if(utente.getRuolo() == Utente.RUOLO.GUEST){
            return false;
        }

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "INSERT INTO Utente (nome, cognome, password, email, dataRegistrazione, telefono, dataDiNascita, professione, cambiaPassword, salt) VALUES ('" + utente.getNome() + "','" + utente.getCognome() + "','" + utente.getHashedPassword() + "','" + utente.getEmail() + "', '" + utente.getDataRegistrazione().toString() + "', '" + utente.getTelefono() + "', '" + utente.getDataNascita().toString() + "', '" + utente.getProfessione() + "', " + (utente.isCambiaPassword()? 1 : 0) + ", UNHEX('" + utente.getSaltHex() + "'));";

        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

            if (utente.getRuolo() == Utente.RUOLO.CLIENTE){
                sql = "INSERT INTO Cliente (Utente_idUtente, disabilitato, residenza) VALUES (" + getLastInsertId() + ", " + (utente.isDisabilitato()? 1 : 0) + ",'" + utente.getResidenza() + "');";
                writeOp = new WriteOperation(sql);
                rowsAffected += executor.executeOperation(writeOp).getRowsAffected();

                return rowsAffected == 2;
            } else if (utente.getRuolo() == Utente.RUOLO.CUCINA){
                sql = "INSERT INTO Cucina (Utente_idUtente) VALUES (" + getLastInsertId() + ");";
                writeOp = new WriteOperation(sql);
                rowsAffected += executor.executeOperation(writeOp).getRowsAffected();

                return rowsAffected == 2;
            } else if (utente.getRuolo() == Utente.RUOLO.AMMINISTRATORE){
                sql = "INSERT INTO Amministratore (Utente_idUtente) VALUES (" + getLastInsertId() + ");";
                writeOp = new WriteOperation(sql);
                rowsAffected += executor.executeOperation(writeOp).getRowsAffected();

                return rowsAffected == 2;
            }

        return false;
    }

    @Override
    public boolean update(Utente utente) {

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "UPDATE utente " +
                "SET nome = '" + utente.getNome() + "', cognome = '" + utente.getCognome() + "', password = '" + utente.getHashedPassword() + "', email = '" + utente.getEmail() + "', dataRegistrazione = '" + utente.getDataRegistrazione().toString() + "', telefono = '" + utente.getTelefono() + "', dataDiNascita = '" + utente.getDataNascita().toString() + "', professione = '" + utente.getProfessione() + "', cambiaPassword = " + (utente.isCambiaPassword()? 1 : 0) +
                " WHERE idUtente = " + utente.getId() + ";";
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        if (utente.getRuolo() == Utente.RUOLO.CLIENTE){
            sql = "UPDATE Cliente" +
                    " SET disabilitato = " + (utente.isDisabilitato()? 1 : 0) + ", residenza = '" + utente.getResidenza() + "'" +
                    " WHERE Utente_idUtente = " + utente.getId() + ";";
            writeOp = new WriteOperation(sql);
            rowsAffected += executor.executeOperation(writeOp).getRowsAffected();
            return rowsAffected > 1;
        }

        return rowsAffected > 0;
    }



    @Override
    public boolean removeByEmail(String email) {

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql;
        IDbOperation writeOp;
        Integer id = findByEmail(email).getId();

        int rowsAffected = 0;
        if(isCliente(id)){
            CarrelloDAO.getInstance().emptyCarrello(id);

            sql = "DELETE FROM Cliente WHERE Utente_idUtente = " + id + ";";
            writeOp = new WriteOperation(sql);
            rowsAffected = executor.executeOperation(writeOp).getRowsAffected();
        } else if(isAmministratore(id)){
            // Rimuove tutte le risposte dell'amministratore
            boolean done = InterazioneUtenteDAO.getInstance().removeRispostePerAmministratore(id);
            if (!done)
                System.out.println("ERRORE! Risposte non eliminate!");

            sql = "DELETE FROM Amministratore WHERE Utente_idUtente = " + id + ";";
            writeOp = new WriteOperation(sql);
            rowsAffected = executor.executeOperation(writeOp).getRowsAffected();
        } else if(isCucina(id)){
            sql = "DELETE FROM Cucina WHERE Utente_idUtente = " + id + ";";
            writeOp = new WriteOperation(sql);
            rowsAffected = executor.executeOperation(writeOp).getRowsAffected();
        }

        sql = "DELETE FROM Utente WHERE email = '" + email + "';";
        writeOp = new WriteOperation(sql);
        rowsAffected += executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected > 1;
    }


    @Override
    public boolean checkIfUtenteHasPurchasedArticolo(Utente utente, IArticolo articolo) {

        DbOperationExecutor executor = new DbOperationExecutor();

        String sql = "SELECT COUNT(*) AS C FROM Ordine AS O " +
                "INNER JOIN Ordine_has_Articolo AS OA " +
                "ON O.idOrdine = OA.Ordine_idOrdine " +
                "WHERE O.Cliente_Utente_idUtente = " + utente.getId() + " " +
                "AND OA.Articolo_idArticolo = " + articolo.getId() + ";";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            rs.next();
            if (rs.getInt("C") >= 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non sono state trovate relazioni tra questo utente e questo articolo");
        }

        return false;
    }

    @Override
    public boolean checkIfUtenteHasCommentedArticolo(Utente utente, IArticolo articolo) {

        DbOperationExecutor executor = new DbOperationExecutor();

        String sql = "SELECT COUNT(*) AS C FROM Commento " +
                "WHERE Cliente_Utente_idUtente = " + utente.getId() + " " +
                "AND Articolo_idArticolo = " + articolo.getId() + ";";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            rs.next();
            if (rs.getInt("C") == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non sono state trovate relazioni tra questo utente e questo articolo");
        }

        return false;
    }

    @Override
    public boolean isFirstAccess(String email) {

        DbOperationExecutor executor = new DbOperationExecutor();

        String sql = "SELECT cambiaPassword FROM utente WHERE email = '" + email + "';";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            rs.next();
            if (rs.getInt("cambiaPassword") == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non esiste un utente con questa email");
        }

        return false;
    }

    @Override
    public boolean setCambiaPassword(String email, boolean b) {
        DbOperationExecutor executor = new DbOperationExecutor();

        String sql = "UPDATE utente SET cambiaPassword = " + (b? 1 : 0) + " WHERE email = '" + email + "';";

        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }

    @Override
    public boolean changePassword(String email, String hashedPassword) {
        DbOperationExecutor executor = new DbOperationExecutor();

        String sql = "UPDATE utente SET password = '" + hashedPassword + "' WHERE email = '" + email + "';";

        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }

    @Override
    public boolean setDisabilitatoUtente(Integer idUtente, boolean b) {
        DbOperationExecutor executor = new DbOperationExecutor();

        String sql = "UPDATE cliente SET disabilitato = " + (b ? 1 : 0) + " WHERE Utente_idUtente = " + idUtente + ";";

        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();


        return rowsAffected == 1;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}