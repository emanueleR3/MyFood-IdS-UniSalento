package it.unisalento.myfood.DAO;

import it.unisalento.myfood.DBInterface.Command.DbOperationExecutor;
import it.unisalento.myfood.DBInterface.Command.IDbOperation;
import it.unisalento.myfood.DBInterface.Command.ReadOperation;
import it.unisalento.myfood.DBInterface.Command.WriteOperation;
import it.unisalento.myfood.DBInterface.DbConnection;
import it.unisalento.myfood.DBInterface.IDbConnection;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.TipologiaProdotto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TipologiaProdottoDAO implements ITipologiaProdottoDAO {

    TipologiaProdotto tipologiaProdotto;

    private static TipologiaProdottoDAO instance = new TipologiaProdottoDAO();


    private TipologiaProdottoDAO() {
        tipologiaProdotto = null;
    }

    public static TipologiaProdottoDAO getInstance() {
        return instance;
    }


    @Override
    public TipologiaProdotto findTipologiaById(Integer idTipologia) {

        String sql = "SELECT idTipologiaProdotto, nome " +
                "FROM TipologiaProdotto " +
                "WHERE idTipologiaProdotto = " + idTipologia + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        TipologiaProdotto tipologiaProdotto = new TipologiaProdotto();
        try {
            rs.next();
            tipologiaProdotto.setId(rs.getInt("idTipologiaProdotto"));
            tipologiaProdotto.setNome(rs.getString("nome"));

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna tipologia con questo id");
        }

        return tipologiaProdotto;
    }
    @Override
    public boolean addTipologia(String tipologia) {

        tipologiaProdotto = new TipologiaProdotto();

        if(findTipologiaByName(tipologia) != null){
            System.out.println("Tipologia già esistente");
            return true;
        }

        // Per uniformare tutti i nomi con la prima lettera maiuscola e poi il resto minuscolo
        String nuovaTipologia = tipologia.substring(0,1).toUpperCase() + tipologia.substring(1).toLowerCase();

        String sql = "INSERT INTO TipologiaProdotto (nome) VALUES ('" + nuovaTipologia + "');";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();
        
        reloadTipologia();
        return rowsAffected > 0;
    }

    @Override
    public boolean editTipologia(Integer id, String newName) {

        // La tipologia non può non esistere, quindi la query sarà sicuramente giusta
        // L'id viene preso da un ArrayList che sta internamente a Java che è sempre aggiornato con id corretti

        String sql = "UPDATE TipologiaProdotto SET nome = '" + newName + "' WHERE idTipologiaProdotto = " + id + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        reloadTipologia();

        return rowsAffected > 0;
    }

    @Override
    public boolean removeTipologia(Integer id) {
        Integer rowsAffected;

        // La tipologia non può non esistere, quindi la query sarà sicuramente giusta
        // L'id viene preso da un ArrayList che sta internamente a Java che è sempre aggiornato con id corretti

        String sql = "DELETE FROM TipologiaProdotto WHERE idTipologiaProdotto = " + id + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation writeOp = new WriteOperation(sql);
        rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        reloadTipologia();

        return rowsAffected > 0;
    }

    @Override
    public ArrayList<TipologiaProdotto> loadTipologia() {

        ArrayList<TipologiaProdotto> tipologie = new ArrayList<>();

        String sql = "SELECT * FROM TipologiaProdotto;";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                tipologiaProdotto = new TipologiaProdotto();

                tipologiaProdotto.setId(rs.getInt("idTipologiaProdotto"));
                tipologiaProdotto.setNome(rs.getString("nome"));

                tipologie.add(tipologiaProdotto);
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
    public void reloadTipologia() {  //TODO:???
       // IArticolo.tipologiaProdotto.clear();
       // IArticolo.tipologiaProdotto.addAll(loadTipologia());
    }

    @Override
    public TipologiaProdotto findTipologiaByName(String nome) {
        String sql = "SELECT * " +
                "FROM TipologiaProdotto" +
                " WHERE nome = '" + nome + "';";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        TipologiaProdotto tipologiaProdotto = new TipologiaProdotto();
        try {
            if(rs.next()) {
                tipologiaProdotto.setId(rs.getInt("idTipologiaProdotto"));
                tipologiaProdotto.setNome(rs.getString("nome"));
                return tipologiaProdotto;
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna tipologia con questo nome");
        }

        return null;
    }
}
