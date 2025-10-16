package it.unisalento.myfood.DAO;

import it.unisalento.myfood.DBInterface.Command.DbOperationExecutor;
import it.unisalento.myfood.DBInterface.Command.IDbOperation;
import it.unisalento.myfood.DBInterface.Command.ReadOperation;
import it.unisalento.myfood.DBInterface.Command.WriteOperation;
import it.unisalento.myfood.model.Azienda;
import it.unisalento.myfood.model.Ingrediente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class IngredienteDAO implements IIngredienteDAO {

    private static IngredienteDAO instance = new IngredienteDAO();


    private IngredienteDAO() {
    }

    public static IngredienteDAO getInstance() {
        return instance;
    }

    /**
     * @param idProdotto
     * viene passato un idProdotto, invece di Prodotto, in quanto serve esclusivamente l'id
     * @return restituisce l'arraylist degli ingredienti per lo specifico idProdotto
     */
    @Override
    public ArrayList<Ingrediente> getIngredientiPerProdotto(Integer idProdotto) {
        Ingrediente ingrediente;
        ArrayList<Ingrediente> ingredienti = new ArrayList<>();
        DbOperationExecutor executor = new DbOperationExecutor();

        // Controllo se ci sono ingredienti per quel prodotto
        String sql = "SELECT COUNT(*) AS C FROM Prodotto_has_Ingrediente " +
                "WHERE Prodotto_Articolo_idArticolo = " + idProdotto + ";";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                if (rs.getInt("C") == 0) return ingredienti;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun ingrediente nel database per questo prodotto");
        }

        // Sono presenti ingredienti
        sql = "SELECT * FROM Prodotto_has_Ingrediente AS PI " +
                "INNER JOIN Ingrediente AS I " +
                "ON PI.Ingrediente_idIngrediente = I.idIngrediente " +
                "WHERE PI.Prodotto_Articolo_idArticolo = " + idProdotto + ";";

        readOp = new ReadOperation(sql);
        rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                ingrediente = new Ingrediente();

                ingrediente.setId(rs.getInt("idIngrediente"));
                ingrediente.setNome(rs.getString("nome"));

                ingrediente.setTipologiaIngrediente(TipologiaIngredienteDAO.getInstance().findTipologiaById(rs.getInt("TipologiaIngrediente_idTipologiaIngrediente")));

                ingrediente.setProduttore(AziendaDAO.getInstance().findById(rs.getInt("Azienda_idProduttore")));
                ingrediente.setDistributori(getDistributoriPerIngrediente(ingrediente.getId()));

                ingredienti.add(ingrediente);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun ingrediente nel database per questo prodotto");
        }

        return ingredienti;
    }

    @Override
    public ArrayList<Azienda> getDistributoriPerIngrediente(Integer idIngrediente) {
        String sql = "SELECT Azienda_idAzienda " +
                "FROM Ingrediente_has_Distributore " +
                "WHERE Ingrediente_idIngrediente = " + idIngrediente + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        ArrayList<Azienda> distributori = new ArrayList<>();
        try {
            while (rs.next()) {
                distributori.add(AziendaDAO.getInstance().findById(rs.getInt("Azienda_idAzienda")));
            }
            return distributori;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun distributore per questo ingrediente");
        }

        return null;
    }

    @Override
    public Ingrediente findIngredienteById(Integer id) {
        Ingrediente ingrediente = new Ingrediente();

        String sql = "SELECT * FROM Ingrediente WHERE idIngrediente = " + id + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                ingrediente.setId(id);
                ingrediente.setNome(rs.getString("nome"));

                ingrediente.setTipologiaIngrediente(TipologiaIngredienteDAO.getInstance().findTipologiaById(rs.getInt("TipologiaIngrediente_idTipologiaIngrediente")));

                ingrediente.setProduttore(AziendaDAO.getInstance().findById(rs.getInt("Azienda_idProduttore")));
                ingrediente.setDistributori(getDistributoriPerIngrediente(id));
            }
            return ingrediente;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun ingrediente con questo ID (" + id + ")");
        }

        return null;
    }

    @Override
    public ArrayList<Ingrediente> findAll() {
       ArrayList<Ingrediente> ingredienti;

        String sql = "SELECT idIngrediente FROM Ingrediente;";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            ingredienti = new ArrayList<>();
            while (rs.next()) {
                Ingrediente ingrediente = findIngredienteById(rs.getInt("idIngrediente"));
                ingredienti.add(ingrediente);
            }
            return ingredienti;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun ingrediente");
        }

        return null;
    }

    @Override
    public ArrayList<Ingrediente> findIngredientePerTipologia(String filtroTipologia) {
        Integer idTipologia = TipologiaIngredienteDAO.getInstance().findTipologiaByNome(filtroTipologia).getId();

        ArrayList<Ingrediente> ingredienti;

        String sql = "SELECT idIngrediente FROM Ingrediente WHERE TipologiaIngrediente_idTipologiaIngrediente = " + idTipologia + ";";

        DbOperationExecutor executor = new DbOperationExecutor();
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            ingredienti = new ArrayList<>();
            while (rs.next()) {
                Ingrediente ingrediente = findIngredienteById(rs.getInt("idIngrediente"));
                ingredienti.add(ingrediente);
            }
            return ingredienti;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun ingrediente");
        }

        return null;
    }

    @Override
    public boolean addIngrediente(Ingrediente ingrediente) {
        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "INSERT INTO Ingrediente (nome, TipologiaIngrediente_idTipologiaIngrediente, Azienda_idProduttore) VALUES ('" + ingrediente.getNome() + "', " + ingrediente.getTipologiaIngrediente().getId() + ", " + ingrediente.getProduttore().getId() + ");";
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        ArrayList<Azienda> distributori = ingrediente.getDistributori();
        if(distributori != null){
            for(Azienda a : distributori){
                sql = "INSERT INTO Ingrediente_has_Distributore (Ingrediente_idIngrediente, Azienda_idAzienda) VALUES (" + getLastInsertId() + ", " + a.getId() + ");";

                writeOp = new WriteOperation(sql);
                rowsAffected += executor.executeOperation(writeOp).getRowsAffected();
            }
            return rowsAffected == distributori.size() + 1;
        }

        return rowsAffected == 1;
    }

    @Override
    public boolean removeIngrediente(Integer idIngrediente) {
        int rowsAffected = 0;

        // Per verificare che la rimozione sia andata a buon fine
        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT COUNT(*) AS C FROM Ingrediente_has_Distributore WHERE Ingrediente_idIngrediente = " + idIngrediente + ";";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        int numberOfRecords = 0;
        try {
            while (rs.next()) {
                numberOfRecords = rs.getInt("C");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun distributore per questo ingrediente");
        }

        if (numberOfRecords > 0) {
            // Va tolto prima nella tabella N:N se no dà problemi
            sql = "DELETE FROM Ingrediente_has_Distributore WHERE Ingrediente_idIngrediente = " + idIngrediente +";";

            IDbOperation writeOp = new WriteOperation(sql);
            rowsAffected = executor.executeOperation(writeOp).getRowsAffected();
        }

        sql = "DELETE FROM Ingrediente WHERE idIngrediente = " + idIngrediente + ";";

        IDbOperation writeOp = new WriteOperation(sql);
        rowsAffected += executor.executeOperation(writeOp).getRowsAffected();
        numberOfRecords++;

        return rowsAffected == numberOfRecords;
    }

    @Override
    public boolean editIngrediente(Ingrediente ingrediente) {
        DbOperationExecutor executor = new DbOperationExecutor();

        // Controllo se esiste l'ingrediente passato
        String sql = "SELECT COUNT(*) AS C FROM Ingrediente WHERE idIngrediente = " + ingrediente.getId() + ";";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while (rs.next()) {
                if (rs.getInt("C") == 0) {
                    System.out.println("L'ingrediente non esiste");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("L'ingrediente che si vuole modificare non esiste");
        }

        // L'ingrediente esiste e viene modificato
        sql = "UPDATE Ingrediente SET nome = '" + ingrediente.getNome() + "', TipologiaIngrediente_idTipologiaIngrediente = " + ingrediente.getTipologiaIngrediente().getId() + ", Azienda_idProduttore = " + ingrediente.getProduttore().getId() +
                " WHERE idIngrediente = " + ingrediente.getId() + ";";

        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        if(ingrediente.getDistributori() != null){
            Iterator<Azienda> iterator = ingrediente.getDistributori().iterator();
            while (iterator.hasNext())
                addDistributoreToIngrediente(ingrediente.getId(), iterator.next().getId());

        }

        return rowsAffected > 0;
    }

    @Override
    public boolean addDistributoreToIngrediente(Integer idIngrediente, Integer idAzienda) {
        DbOperationExecutor executor = new DbOperationExecutor();

        // Il distributore esiste già
        String sql = "SELECT COUNT(*) AS C FROM Ingrediente_has_Distributore " +
                "WHERE Ingrediente_idIngrediente = " + idIngrediente + " AND Azienda_idAzienda = " + idAzienda + ";";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while (rs.next()) {
                if (rs.getInt("C") == 1) {
                  //  System.out.println("Il distributore che si vuole aggiungere è già esistente");
                    return true;    // restituisce true perché comunque il distributore esiste già
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("L'ingrediente che si vuole modificare non esiste");
        }

        // Il distributore non esiste e viene aggiunto
        sql = "INSERT INTO Ingrediente_has_Distributore (Ingrediente_idIngrediente, Azienda_idAzienda) VALUES (" + idIngrediente + ", " + idAzienda + ");";
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }

    @Override
    public boolean removeDistributoreFromIngrediente(Integer idIngrediente, Integer idAzienda) {
        DbOperationExecutor executor = new DbOperationExecutor();

        // Il distributore non esiste
        String sql = "SELECT COUNT(*) AS C FROM Ingrediente_has_Distributore " +
                "WHERE Ingrediente_idIngrediente = " + idIngrediente + " AND Azienda_idAzienda = " + idAzienda + ";";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while (rs.next()) {
                if (rs.getInt("C") == 0) {
                    System.out.println("Non è presente questo distributore per questo ingrediente");
                    return true;    // restituisce true perché comunque il distributore non è presente
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non esiste alcuna relazione tra questo ingrediente e questo distributore");
        }

        // Il distributore esiste e viene rimosso
        sql = "DELETE FROM Ingrediente_has_Distributore WHERE Ingrediente_idIngrediente = " + idIngrediente + " AND Azienda_idAzienda = " + idAzienda + ";";
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }

    @Override
    public Integer getLastInsertId() {
        int lastInsertId = 1;
        String sql = "SELECT MAX(idIngrediente) as max " +
                "FROM Ingrediente;";
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
}
