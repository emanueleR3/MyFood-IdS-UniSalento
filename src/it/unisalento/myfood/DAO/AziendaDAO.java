package it.unisalento.myfood.DAO;

import it.unisalento.myfood.DBInterface.Command.DbOperationExecutor;
import it.unisalento.myfood.DBInterface.Command.IDbOperation;
import it.unisalento.myfood.DBInterface.Command.ReadOperation;
import it.unisalento.myfood.DBInterface.Command.WriteOperation;
import it.unisalento.myfood.model.Azienda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AziendaDAO implements IAziendaDAO {

    private static AziendaDAO instance = new AziendaDAO();


    private AziendaDAO() {
    }

    public static AziendaDAO getInstance() {
        return instance;
    }

    @Override
    public Integer getLastInsertId(){
        int lastInsertId = 1;
        String sql = "SELECT MAX(idAzienda) as max " +
                "FROM Azienda;";
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
    public Azienda findByPartitaIVA(String partitaIVA) {
        Azienda azienda = null;

        String sql = "SELECT * FROM Azienda WHERE partitaIVA = '" + partitaIVA + "';";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                azienda = new Azienda();

                azienda.setId(rs.getInt("idAzienda"));
                azienda.setNome(rs.getString("nome"));
                azienda.setPartitaIVA(partitaIVA);

                azienda.setIdIngredientiProdotti(loadProducedIngredienti(azienda.getId()));
                azienda.setIdIngredientiDistribuiti(loadDistributedIngredienti(azienda.getId()));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna tipologia nel database");
        }

        return azienda;
    }

    @Override
    public Azienda findById(Integer id) {
        Azienda azienda = null;

        String sql = "SELECT * FROM Azienda WHERE idAzienda = " + id + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                azienda = new Azienda();

                azienda.setId(id);
                azienda.setNome(rs.getString("nome"));
                azienda.setPartitaIVA(rs.getString("partitaIVA"));

                azienda.setIdIngredientiProdotti(loadProducedIngredienti(id));
                azienda.setIdIngredientiDistribuiti(loadDistributedIngredienti(id));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna tipologia nel database");
        }

        return azienda;
    }

    @Override
    public ArrayList<Azienda> findAll() {
        ArrayList<Azienda> aziende = null;

        String sql = "SELECT idAzienda FROM Azienda ORDER BY idAzienda ASC;";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            aziende = new ArrayList<>();
            while (rs.next()) {
                Azienda azienda = findById(rs.getInt("idAzienda"));
                aziende.add(azienda);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessuna tipologia nel database");
        }
        return aziende;
    }

    @Override
    public ArrayList<Integer> loadProducedIngredienti(Integer idAzienda) {

        ArrayList<Integer> idProducedIngredienti = new ArrayList<>();

        String sql = "SELECT idIngrediente FROM Ingrediente AS I " +
                "INNER JOIN Azienda as A " +
                "ON I.Azienda_idProduttore = A.idAzienda " +
                "WHERE I.Azienda_idProduttore = " + idAzienda + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                idProducedIngredienti.add(rs.getInt("idIngrediente"));
            }
            return idProducedIngredienti;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun ingrediente prodotto da questa azienda");
        }

        return null;
    }

    @Override
    public ArrayList<Integer> loadDistributedIngredienti(Integer idAzienda) {
        ArrayList<Integer> idDistributedIngredienti = new ArrayList<>();

        String sql = "SELECT Ingrediente_idIngrediente FROM Ingrediente_has_Distributore WHERE Azienda_idAzienda = " + idAzienda + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {

            while (rs.next()) {
                idDistributedIngredienti.add(rs.getInt("Ingrediente_idIngrediente"));
            }
            return idDistributedIngredienti;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun ingrediente distribuito da questa azienda");
        }

        return null;
    }

    @Override
    public boolean addAzienda(String nome, String partitaIVA) {
        Integer rowsAffected;
        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "INSERT INTO Azienda (nome, partitaIVA) VALUES ('" + nome + "', '" + partitaIVA + "');";
        IDbOperation writeOp = new WriteOperation(sql);
        rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected > 0;
    }


    @Override
    public boolean removeAzienda(Integer idAzienda) {
        int rowsAffected = 0;
        int rowsToDelete = 0;
        int numberOfRecords = 0;

        // Verifico che non sia presente in Ingrediente_has_Distributore
        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT COUNT(*) AS C FROM Ingrediente_has_Distributore WHERE Azienda_idAzienda = " + idAzienda + ";";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                numberOfRecords = rs.getInt("C");
                rowsToDelete = numberOfRecords;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun ingrediente distribuito per questa azienda");
        }

        if (numberOfRecords > 0) {
            // Va tolto prima nella tabella N:N se no dà problemi
            sql = "DELETE FROM Ingrediente_has_Distributore WHERE Azienda_idAzienda = " + idAzienda +";";

            IDbOperation writeOp = new WriteOperation(sql);
            rowsAffected += executor.executeOperation(writeOp).getRowsAffected();
        }

        // Controllo che non stia in Ingrediente
        sql = "SELECT COUNT(*) AS C FROM Ingrediente WHERE Azienda_idProduttore = " + idAzienda + ";";

        readOp = new ReadOperation(sql);
        rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                numberOfRecords = rs.getInt("C");
                rowsToDelete += numberOfRecords;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun ingrediente prodotto da questa azienda");
        }

        if (numberOfRecords > 0) {
            IngredienteDAO ingredienteDAO = IngredienteDAO.getInstance();
            boolean done;

            sql = "SELECT idIngrediente FROM Ingrediente WHERE Azienda_idProduttore = " + idAzienda + ";";
            readOp = new ReadOperation(sql);
            rs = executor.executeOperation(readOp).getResultSet();

            try {
                while (rs.next()) {
                    done = ingredienteDAO.removeIngrediente(rs.getInt("idIngrediente"));
                    if (done) rowsAffected++;
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
                System.out.println("SQL State: " + e.getSQLState());
                System.out.println("Vendor Error: " + e.getErrorCode());
            } catch (NullPointerException e) {
                System.out.println("Non trovo nessun ingrediente prodotto da questa azienda");
            }
        }

        // Infine rimuovo da Azienda
        sql = "DELETE FROM Azienda WHERE idAzienda = " + idAzienda + ";";
        IDbOperation writeOp = new WriteOperation(sql);
        rowsAffected += executor.executeOperation(writeOp).getRowsAffected();
        rowsToDelete++;

        return rowsAffected == rowsToDelete;
    }

    @Override
    public boolean updateAzienda(Integer idAzienda, String newName, String partitaIVA) {
        Integer rowsAffected;
        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "UPDATE Azienda SET nome = '" + newName + "', partitaIVA = '" + partitaIVA + "' " +
                "WHERE idAzienda = " + idAzienda + ";";
        IDbOperation writeOp = new WriteOperation(sql);
        rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected > 0;
    }
}