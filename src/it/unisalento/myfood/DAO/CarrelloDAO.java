package it.unisalento.myfood.DAO;

import it.unisalento.myfood.DBInterface.Command.DbOperationExecutor;
import it.unisalento.myfood.DBInterface.Command.IDbOperation;
import it.unisalento.myfood.DBInterface.Command.ReadOperation;
import it.unisalento.myfood.DBInterface.Command.WriteOperation;
import it.unisalento.myfood.model.Carrello;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


public class CarrelloDAO implements ICarrelloDAO {

    private static CarrelloDAO instance = new CarrelloDAO();

    private CarrelloDAO() {
    }

    public static CarrelloDAO getInstance() {
        return instance;
    }

    /**
     * @param idUtente
     * @return ritorna il Carrello di un Utente
     */
    @Override
    public Carrello loadCarrelloPerIdUtente(Integer idUtente) {
        Carrello carrello = new Carrello();
        HashMap<Integer, Integer> articoli = new HashMap<>();

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT Articolo_idArticolo, quantita " +
                "FROM Cliente_has_Articolo " +
                "WHERE Cliente_Utente_idUtente = " + idUtente + ";";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                articoli.put(rs.getInt("Articolo_idArticolo"), rs.getInt("quantita"));
            }
            carrello.setArticoli(articoli);
            carrello.setCliente(UtenteDAO.getInstance().findById(idUtente));

            // Verrà calcolato nel business appena finisce di chiamare questo metodo
            carrello.setTotale(null);

            return carrello;
        } catch (SQLException e) {
            // Gestisce le categorie di errori
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun prodotto nel carrello dell'utente " + idUtente);
        }

        return null;
    }

    // TODO: NELLA VIEW (COME UX) MOSTRARE IL NUMERO DI UNITA' NEL CARRELLO PER OGNI PRODOTTO NEL CATALOGO IN MODO CHE CI EVITIAMO IL CONTROLLO SULLA SOMMA DEI PRODOTTI
    /**
     *
     * @param idArticolo
     * @param quantita nuova quantità, non incrementa
     * @param idUtente
     * @return ritorna true se l'aggiunta è avvenuta con successo, altrimenti ritorna falso
     */
    @Override
    public boolean addArticoloToCarrello(Integer idArticolo, Integer quantita, Integer idUtente) {
        int rowsAffected;
        String sql = "SELECT COUNT(*) AS C FROM Cliente_has_Articolo WHERE Articolo_idArticolo = " + idArticolo + " AND Cliente_Utente_idUtente = " + idUtente + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            rs.next();
            if (rs.getInt("C") == 0) {
                // C = 0 --> NON esiste quel prodotto nel Carrello e lo posso aggiungere tranquillamente

                sql = "INSERT INTO Cliente_has_Articolo (Cliente_Utente_idUtente, Articolo_idArticolo, quantita) " +
                        "VALUES (" + idUtente + ", " + idArticolo + ", " + quantita + ");";

                executor = new DbOperationExecutor();
                IDbOperation writeOp = new WriteOperation(sql);
                rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

                if (rowsAffected == 0) return false;    // comunico alla classe di business che c'è stato un problema
            } else {
                // C = 1 --> esiste già un prodotto nel carrello con quell'id

                sql = "UPDATE Cliente_has_Articolo " +
                        "SET quantita = " + quantita + " " +
                        "WHERE Cliente_Utente_idUtente = " + idUtente + " AND Articolo_idArticolo = " + idArticolo + ";";

                executor = new DbOperationExecutor();
                IDbOperation writeOp = new WriteOperation(sql);
                rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

                if (rowsAffected == 0) return false;    // comunico alla classe di business che c'è stato un problema
            }
        } catch (SQLException e) {
            // c'è stato qualche problema
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());

            // lo comunico alla classe di Business
            return false;
        } catch (NullPointerException e) {  // non dovrebbe esserci questo tipo di eccezione in questo caso perchè restituisce un numero, nel peggiore dei casi è 0
            // c'è stato qualche problema
            System.out.println("Errore nella tabella");

            // lo comunico alla classe di Business
            return false;
        }

        // comunico alla classe di Business che l'aggiunta è avvenuta
        return true;
    }

    /**
     * @param idArticolo
     * @param idUtente
     * @return ritorna true se la rimozione è avvenuta con successo, altrimenti ritorna falso
     * @implNote Non vanno fatti controlli sull'esistenza dell'idArticolo che viene passato. Sicuro esiste l'articolo nella tabella se si è potuto invocare questo metodo dal business
     */
    @Override
    public boolean removeArticoloFromCarrello(Integer idArticolo, Integer idUtente) {
        Integer rowsAffected;
        String sql = "DELETE FROM Cliente_has_Articolo " +
                "WHERE Cliente_Utente_idUtente = " + idUtente + " AND Articolo_idArticolo = " + idArticolo + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected != 0;   // != 0 --> true - = 0 --> false
    }

    @Override
    public boolean removeArticoloFromAll(Integer idArticolo) {
        Integer rowsAffected;
        DbOperationExecutor executor = new DbOperationExecutor();

        int numberOfRecords = 0;
        String sql = "SELECT COUNT(*) AS C FROM Cliente_has_Articolo WHERE Articolo_idArticolo = " + idArticolo + ";";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while (rs.next()) {
                numberOfRecords = rs.getInt("C");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun carrello nel database");
        }

        sql = "DELETE FROM Cliente_has_Articolo WHERE Articolo_idArticolo = " + idArticolo + ";";
        IDbOperation writeOp = new WriteOperation(sql);
        rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == numberOfRecords;
    }

    @Override
    public boolean emptyCarrello(Integer idUtente) {
        Integer rowsAffected;

        String sql = "DELETE FROM Cliente_has_Articolo WHERE Cliente_Utente_idUtente = " + idUtente + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected != 0;   // != 0 --> true - = 0 --> false
    }

    @Override
    public boolean setNewQuantita(Integer idUtente, Integer idArticolo, Integer newQuantita){
        Integer rowsAffected;
        if(newQuantita == 0)
            return removeArticoloFromCarrello(idArticolo, idUtente);

        String sql = "UPDATE Cliente_has_Articolo " +
                "SET quantita = " + newQuantita +
                " WHERE Cliente_Utente_idUtente = " + idUtente + " AND Articolo_idArticolo = " + idArticolo + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected != 0;   // != 0 --> true - = 0 --> false
    }
}
