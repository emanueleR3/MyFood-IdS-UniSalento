package it.unisalento.myfood.DAO;

import it.unisalento.myfood.DBInterface.Command.DbOperationExecutor;
import it.unisalento.myfood.DBInterface.Command.IDbOperation;
import it.unisalento.myfood.DBInterface.Command.ReadOperation;
import it.unisalento.myfood.DBInterface.Command.WriteOperation;
import it.unisalento.myfood.DBInterface.DbConnection;
import it.unisalento.myfood.DBInterface.IDbConnection;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.TipologiaIngrediente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TipologiaIngredienteDAO implements ITipologiaIngredienteDAO {

    private static TipologiaIngredienteDAO instance = new TipologiaIngredienteDAO();

    private TipologiaIngredienteDAO() {}

    public static TipologiaIngredienteDAO getInstance() {
        return instance;
    }

    @Override
    public int getLastInsertId(){
        int lastInsertId = 1;
        String sql = "SELECT MAX(idTipologiaIngrediente) as max " +
                "FROM TipologiaIngrediente;";
        IDbOperation readOp = new ReadOperation(sql);
        DbOperationExecutor executor = new DbOperationExecutor();
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            if (rs.next()) {
                lastInsertId = rs.getInt("max");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastInsertId;
    }

    @Override
    public TipologiaIngrediente findTipologiaByNome(String nome) {
        String sql = "SELECT * FROM TipologiaIngrediente WHERE nome = '" + nome + "';";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            TipologiaIngrediente tipologiaIngrediente = new TipologiaIngrediente();
            if (rs.next()) {
                tipologiaIngrediente.setId(rs.getInt("idTipologiaIngrediente"));
                tipologiaIngrediente.setNome(nome);

                return tipologiaIngrediente;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna tipologia nel database");
        }
        return null;
    }

    @Override
    public boolean tipologiaExists(String nome) {
        String sql = "SELECT COUNT(*) AS C FROM TipologiaIngrediente WHERE nome = '" + nome + "';";
        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        int numberOfRecords = 0;
        try {
            if (rs.next()) numberOfRecords = rs.getInt("C");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna tipologia nel database");
        }

        return numberOfRecords == 1;
    }

    @Override
    public boolean addTipologia(String tipologia) {
        int rowsAffected;

        if (tipologiaExists(tipologia)) {
            System.out.println("La tipologia esiste già");
            System.out.println(findTipologiaByNome(tipologia).getId());
        }

        // Per uniformare tutti i nomi con la prima lettera maiuscola e poi il resto minuscolo
        String nuovaTipologia = tipologia.substring(0,1).toUpperCase() + tipologia.substring(1).toLowerCase();

        String sql = "INSERT INTO TipologiaIngrediente (nome) VALUES ('" + nuovaTipologia + "');";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        reloadTipologia();

        return rowsAffected > 0;
    }

    @Override
    public boolean editTipologia(Integer id, String newName) {
        Integer rowsAffected;
        // La tipologia non può non esistere, quindi la query sarà sicuramente giusta
        // L'id viene preso da un ArrayList che sta internamente a Java che è sempre aggiornato con id corretti

        String sql = "UPDATE TipologiaIngrediente SET nome = '" + newName + "' WHERE idTipologiaIngrediente = " + id + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        reloadTipologia();
        return rowsAffected > 0;
    }

    @Override
    public boolean removeTipologia(Integer id) {
        int rowsAffected;

        // La tipologia non può non esistere, quindi la query sarà sicuramente giusta

        String sql = "DELETE FROM TipologiaIngrediente WHERE idTipologiaIngrediente = " + id + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        // TODO: cosa fa?
        reloadTipologia();

        return rowsAffected == 1;
    }

    @Override
    public ArrayList<TipologiaIngrediente> loadTipologia() {

        ArrayList<TipologiaIngrediente> tipologie = new ArrayList<>();

        String sql = "SELECT * FROM TipologiaIngrediente;";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            TipologiaIngrediente tipologiaIngrediente;
            while (rs.next()) {
                tipologiaIngrediente = new TipologiaIngrediente();

                tipologiaIngrediente.setId(rs.getInt("idTipologiaIngrediente"));
                tipologiaIngrediente.setNome(rs.getString("nome"));

                tipologie.add(tipologiaIngrediente);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna tipologia nel database");
        }

        return tipologie;
    }

    @Override
    public TipologiaIngrediente findTipologiaById(Integer id) {
        String sql = "SELECT * FROM TipologiaIngrediente WHERE idTipologiaIngrediente = " + id + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            TipologiaIngrediente tipologiaIngrediente = new TipologiaIngrediente();
            while (rs.next()) {
                tipologiaIngrediente.setId(id);
                tipologiaIngrediente.setNome(rs.getString("nome"));
            }
            return tipologiaIngrediente;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna tipologia nel database");
        }
        return null;
    }


    @Override
    public void reloadTipologia() {
       // IArticolo.tipologiaIngrediente.clear();
      //  IArticolo.tipologiaIngrediente.addAll(loadTipologia());
    }
}
