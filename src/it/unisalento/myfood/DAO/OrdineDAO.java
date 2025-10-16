package it.unisalento.myfood.DAO;

import it.unisalento.myfood.DBInterface.Command.DbOperationExecutor;
import it.unisalento.myfood.DBInterface.Command.IDbOperation;
import it.unisalento.myfood.DBInterface.Command.ReadOperation;
import it.unisalento.myfood.DBInterface.Command.WriteOperation;
import it.unisalento.myfood.model.Carrello;
import it.unisalento.myfood.model.IOrdine;
import it.unisalento.myfood.model.Ordine;
import it.unisalento.myfood.model.Utente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class OrdineDAO implements IOrdineDAO{

    private static final OrdineDAO instance = new OrdineDAO();

    private OrdineDAO(){
    }

    public static OrdineDAO getInstance(){
        return instance;
    }

    @Override
    public Integer getLastInsertId(){
        int lastInsertId = 1;
        String sql = "SELECT MAX(idOrdine) as max " +
                "FROM Ordine;";
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
    public Ordine findById(Integer id){
        ResultSet rs;
        Ordine ordine;

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT * FROM Ordine WHERE idOrdine = " + id + ";";
        IDbOperation readOp = new ReadOperation(sql);
        rs = executor.executeOperation(readOp).getResultSet();

        try{
            rs.next();
            if(rs.getRow() == 1){
                ordine = new Ordine();

                HashMap<Integer, Integer> articoli = articoliPerIdOrdine(id);

                ordine.setArticoli(articoli);
                ordine.setIdOrdine(id);
                ordine.setData(rs.getTimestamp("data"));
                ordine.setTotale(rs.getFloat("importo"));
                ordine.setStato(Ordine.STATO.valueOf((rs.getString("stato"))));
                ordine.setRicorrente(rs.getInt("ricorrente") == 1);
                ordine.setIdCliente(rs.getInt("Cliente_Utente_idUtente"));

                return ordine;
             }
        }catch (SQLException e){
            System.out.println("SQL Exception " + e.getMessage());
            System.out.println("SQL State" + e.getSQLState());
            System.out.println("Vendor error " + e.getErrorCode());
        } catch (NullPointerException e){
            System.out.println("Non ho trovato l'utente con id = " + id);
        }

        return null;
    }

    @Override
    public ArrayList<Ordine> findByState(IOrdine.STATO stato) {
        ResultSet rs;
        ArrayList<Ordine> ordini = new ArrayList<>();

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT idOrdine FROM Ordine WHERE stato = '" + stato + "';";
        IDbOperation readOp = new ReadOperation(sql);
        rs = executor.executeOperation(readOp).getResultSet();

        try{
            while(rs.next()){
                ordini.add(findById(rs.getInt("idOrdine")));
            }

            return ordini;
        } catch (SQLException e){
            System.out.println("SQL Exception " + e.getMessage());
            System.out.println("SQL State" + e.getSQLState());
            System.out.println("Vendor error " + e.getErrorCode());
        }
        return null;
    }

    @Override
    public ArrayList<Ordine> findAll() {
        ResultSet rs;
        ArrayList<Ordine> ordini = new ArrayList<>();

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT idOrdine FROM Ordine";
        IDbOperation readOp = new ReadOperation(sql);
        rs = executor.executeOperation(readOp).getResultSet();

        try{
            while(rs.next()){
                ordini.add(findById(rs.getInt("idOrdine")));
            }

            return ordini;
        } catch (SQLException e){
            System.out.println("SQL Exception " + e.getMessage());
            System.out.println("SQL State" + e.getSQLState());
            System.out.println("Vendor error " + e.getErrorCode());
        }
        return null;

    }

    @Override
    public ArrayList<Ordine> findByRecurrent(Integer idCliente) {
        ResultSet rs;
        ArrayList<Ordine> ordini = new ArrayList<>();

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT idOrdine FROM Ordine WHERE Cliente_Utente_idUtente = " + idCliente + " AND ricorrente = 1 ORDER BY idOrdine DESC;";
        IDbOperation readOp = new ReadOperation(sql);
        rs = executor.executeOperation(readOp).getResultSet();

        try{
            while(rs.next()){
                ordini.add(findById(rs.getInt("idOrdine")));
            }

            return ordini;
        } catch (SQLException e){
            System.out.println("SQL Exception " + e.getMessage());
            System.out.println("SQL State" + e.getSQLState());
            System.out.println("Vendor error " + e.getErrorCode());
        }

        return null;
    }

    /**
     * @param carrello viene passato un carrello che verrà inserito tra gli ordini nel database
     * @return true se l'inserimento è avvenuto, false altrimenti
     */
    @Override
    public boolean createOrdine(Carrello carrello) {
        HashMap<Integer, Integer> articoli = carrello.getArticoli();
        Utente cliente = carrello.getCliente();

        // Prende la data dal sistema quando viene invocato il metodo
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestampLocalDateTime = Timestamp.valueOf(localDateTime);

        // Inserisce i dati in Ordine
        String sql = "INSERT INTO Ordine (Cliente_Utente_idUtente, data, importo, stato, ricorrente) " +
                "VALUES (" + cliente.getId() + ", '" + timestampLocalDateTime + "', " + carrello.getTotale() + ", '" + IOrdine.STATO.NON_PAGATO + "', 0);";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        if (rowsAffected != 1) return false;     // comunico al business che c'è un errore

        // Inserisce i dati in Ordine_has_Articolo
        Integer idOrdine = getLastInsertId();
        Iterator<Map.Entry<Integer, Integer>> iterator = carrello.getArticoli().entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();

            sql = "INSERT INTO Ordine_has_Articolo (Ordine_idOrdine, Articolo_idArticolo, quantita) " +
                    "VALUES (" + idOrdine + ", " + entry.getKey() + ", " + entry.getValue() + ");";
            executor = new DbOperationExecutor();
            writeOp = new WriteOperation(sql);
            rowsAffected += executor.executeOperation(writeOp).getRowsAffected();

        }

        // Aggiorna le disponibilità
        ArticoloDAO.getInstance().updateDisponibilitaAfterOrdine(findById(idOrdine));

        return rowsAffected == articoli.size() + 1;
    }

    @Override
    public boolean updateStatoOrdine(Ordine ordine, IOrdine.STATO nuovoStato) {
        String sql = "UPDATE Ordine SET stato = '" + nuovoStato + "' WHERE idOrdine = " + ordine.getIdOrdine() + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }

    @Override
    public ArrayList<Ordine> findByCliente(Integer idCliente) {
        ResultSet rs;
        ArrayList<Ordine> ordini = new ArrayList<>();

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT idOrdine FROM Ordine WHERE Cliente_Utente_idUtente = " + idCliente + " ORDER BY idOrdine DESC;";
        IDbOperation readOp = new ReadOperation(sql);
        rs = executor.executeOperation(readOp).getResultSet();

        try{
            while(rs.next()){
                ordini.add(findById(rs.getInt("idOrdine")));
            }

            return ordini;
        } catch (SQLException e){
            System.out.println("SQL Exception " + e.getMessage());
            System.out.println("SQL State" + e.getSQLState());
            System.out.println("Vendor error " + e.getErrorCode());
        }
        return null;
    }

    @Override
    public ArrayList<Ordine> findByClienteAndState(Integer idCliente, IOrdine.STATO stato) {
        ResultSet rs;
        ArrayList<Ordine> ordini = new ArrayList<>();

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT idOrdine FROM Ordine WHERE Cliente_Utente_idUtente = " + idCliente + " AND stato = '" + stato + "' ORDER BY idOrdine DESC;";
        IDbOperation readOp = new ReadOperation(sql);
        rs = executor.executeOperation(readOp).getResultSet();

        try{
            while(rs.next()){
                ordini.add(findById(rs.getInt("idOrdine")));
            }

            return ordini;
        } catch (SQLException e){
            System.out.println("SQL Exception " + e.getMessage());
            System.out.println("SQL State" + e.getSQLState());
            System.out.println("Vendor error " + e.getErrorCode());
        }
        return null;
    }

    @Override
    public HashMap<Integer, Integer> articoliPerIdOrdine(Integer id) {
        ResultSet rs;
        HashMap<Integer, Integer> articoli = new HashMap<>();

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT Articolo_idArticolo, quantita " +
                "FROM Ordine_has_Articolo " +
                "WHERE Ordine_idOrdine = " + id + ";";

        IDbOperation readOp = new ReadOperation(sql);
        rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                articoli.put(rs.getInt("Articolo_idArticolo"), rs.getInt("quantita"));
            }
            return articoli;
        } catch (SQLException e) {
            // Gestisce le categorie di errori
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun articolo nel database per l'ordine " + id);
        }

        return null;
    }

    @Override
    public boolean setRicorrente(Integer idOrdine, boolean ricorrente) {
        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "UPDATE Ordine " +
                "SET ricorrente = " + (ricorrente ? 1 : 0) + " WHERE idOrdine = " + idOrdine + ";";
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }

    @Override
    public boolean remove(Ordine ordine) {
        DbOperationExecutor executor;
        String sql = "DELETE FROM Ordine_has_Articolo " +
                "WHERE Ordine_idOrdine = " + ordine.getIdOrdine() + ";";

        executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        executor = new DbOperationExecutor();
        sql = "DELETE FROM Ordine " +
                "WHERE idOrdine = " + ordine.getIdOrdine() + ";";
        writeOp = new WriteOperation(sql);
        rowsAffected += executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == ordine.getArticoli().size() + 1;
    }

    @Override
    public boolean updateOrdine(Ordine o) {
        String sql = "UPDATE Ordine SET Cliente_Utente_idUtente = " + o.getIdCliente() + ", data = '" + o.getData() + "', importo = " + o.getImporto() + ", stato = '" + o.getStato() + "', ricorrente = " + (o.isRicorrente() ? 1 : 0) + " " +
                "WHERE idOrdine = " + o.getIdOrdine() + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }
}
