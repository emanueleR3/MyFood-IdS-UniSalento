package it.unisalento.myfood.DAO;

import it.unisalento.myfood.DBInterface.Command.DbOperationExecutor;
import it.unisalento.myfood.DBInterface.Command.IDbOperation;
import it.unisalento.myfood.DBInterface.Command.ReadOperation;
import it.unisalento.myfood.DBInterface.Command.WriteOperation;
import it.unisalento.myfood.model.Composite.CommentoCliente;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.Menu;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.Ingrediente;
import it.unisalento.myfood.model.Ordine;
import it.unisalento.myfood.model.TipologiaProdotto;
import it.unisalento.myfood.model.Utente;
import org.apache.pdfbox.pdfparser.XrefTrailerResolver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticoloDAO implements IArticoloDAO {

    private static ArticoloDAO instance = new ArticoloDAO();

    private ArticoloDAO() {

    }

    public static ArticoloDAO getInstance() {
        return instance;
    }

    @Override
    public Integer getLastInsertId(){
        int lastInsertId = 0;

        String sql = "SELECT MAX(idArticolo) as max FROM Articolo;";
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
    public List<IArticolo> findProdottoByTipologia(TipologiaProdotto tipologiaProdotto) {

        ArrayList<IArticolo> prodotti = new ArrayList<>();

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT * FROM Articolo AS A " +
                "INNER JOIN PRODOTTO AS P ON A.idArticolo = P.Articolo_idArticolo " +
                "WHERE P.Tipologia_idTipologia = " + tipologiaProdotto.getId() + ";";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                Prodotto p = new Prodotto();
                p.setTipologiaProdotto(tipologiaProdotto);
                p.setId(rs.getInt("idArticolo"));
                p.setNome(rs.getString("nome"));
                p.setPrezzo(rs.getFloat("prezzo"));
                p.setPezziDisponibili(rs.getInt("disponibilita"));
                p.setDescrizione(rs.getString("descrizione"));
                p.setIngredienti(IngredienteDAO.getInstance().getIngredientiPerProdotto(p.getId()));
                p.setCommenti(InterazioneUtenteDAO.getInstance().findCommentiByArticolo(p.getId()));

                prodotti.add(p);
            }
        } catch (SQLException e) {
            // Gestisce le categorie di errori
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun prodotto nel database");
        }

        return prodotti;
    }


    @Override
    public List<IArticolo> findProdottoByTipologiaContains(TipologiaProdotto tipologiaProdotto, Ingrediente ingrediente) {

        ArrayList<IArticolo> prodotti = new ArrayList<>();

        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "SELECT A.idArticolo FROM (Articolo AS A " +
                "INNER JOIN PRODOTTO AS P ON A.idArticolo = P.Articolo_idArticolo) JOIN Prodotto_has_Ingrediente AS PI ON p.Articolo_idArticolo = PI.Prodotto_Articolo_idArticolo " +
                "WHERE P.Tipologia_idTipologia = " + tipologiaProdotto.getId() + " AND " + " Ingrediente_idIngrediente = " + ingrediente.getId() + ";";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                Prodotto p = (Prodotto) findById(rs.getInt("A.idArticolo"));

                prodotti.add(p);
            }
        } catch (SQLException e) {
            // Gestisce le categorie di errori
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun prodotto nel database");
        }

        return prodotti;
    }

    @Override
    public List<IArticolo> findMenuContains(Ingrediente ingrediente) {
        DbOperationExecutor executor = new DbOperationExecutor();

        // Lista di tutti i Menu presenti
        List<IArticolo> menus = new ArrayList<>();

        String sql =
                "SELECT Articolo_idArticolo " +
                "FROM Menu AS M2 " +
                "WHERE Articolo_idArticolo IN (" +
                        //cerca nei prodotti del menu
                        "SELECT MA.Menu_Articolo_idArticolo " +
                        "FROM (Menu_has_Articolo AS MA JOIN Articolo AS A ON MA.Articolo_idArticolo = A.idArticolo) JOIN Prodotto_has_Ingrediente AS PI ON A.idArticolo = PI.Prodotto_Articolo_idArticolo " +
                        "WHERE PI.Ingrediente_idIngrediente = 1 ) OR Articolo_idArticolo IN (" +
                        //cerca nei prodotti dei sottomenu
                        "SELECT MA2.Menu_Articolo_idArticolo " +
                        "FROM ((Menu_has_Articolo AS MA2 JOIN Menu AS M ON MA2.Articolo_idArticolo = M.Articolo_idArticolo) JOIN Menu_has_Articolo AS MA3 ON M.Articolo_idArticolo = MA3.Menu_Articolo_idArticolo) JOIN Prodotto_has_Ingrediente AS PI2 ON MA3.Articolo_idArticolo = PI2.Prodotto_Articolo_idArticolo " +
                        "WHERE MA2.Menu_Articolo_idArticolo = M2.Articolo_idArticolo AND PI2.Ingrediente_idIngrediente = " + ingrediente.getId() + ");";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                // Singolo menu
                Menu menu = (Menu) findById(rs.getInt("Articolo_idArticolo"));
                sql = "SELECT Articolo_idArticolo FROM Menu_has_Articolo " +
                        "WHERE Menu_Articolo_idArticolo = " + menu.getId() + ";";

                IDbOperation readOpArticoli = new ReadOperation(sql);
                ResultSet rsArticoli = executor.executeOperation(readOpArticoli).getResultSet();

                while (rsArticoli.next()) {
                    menu.add(findById(rsArticoli.getInt("Articolo_idArticolo")));
                }

                menus.add(menu);
            }
        } catch (SQLException e) {
            // Gestisce le categorie di errori
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun prodotto nel database");
        }

        return menus;
    }


    @Override
    public List<IArticolo> findMenu() {
        DbOperationExecutor executor = new DbOperationExecutor();

        // Lista di tutti i Menu presenti
        List<IArticolo> menus = new ArrayList<>();

        String sql = "SELECT * FROM Articolo AS A " +
                "INNER JOIN Menu AS M " +
                "ON A.idArticolo = M.Articolo_idArticolo;";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            while (rs.next()) {
                // Singolo menu
                Menu menu = new Menu();

                menu.setId(rs.getInt("idArticolo"));
                menu.setNome(rs.getString("nome"));
                menu.setDescrizione(rs.getString("descrizione"));
                menu.setSconto(rs.getFloat("sconto"));
                menu.setCommenti(InterazioneUtenteDAO.getInstance().findCommentiByArticolo(menu.getId()));

                sql = "SELECT Articolo_idArticolo FROM Menu_has_Articolo " +
                        "WHERE Menu_Articolo_idArticolo = " + menu.getId() + ";";

                IDbOperation readOpArticoli = new ReadOperation(sql);
                ResultSet rsArticoli = executor.executeOperation(readOpArticoli).getResultSet();

                while (rsArticoli.next()) {
                    menu.add(findById(rsArticoli.getInt("Articolo_idArticolo")));
                }

                menus.add(menu);
            }
        } catch (SQLException e) {
            // Gestisce le categorie di errori
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun prodotto nel database");
        }

        return menus;
    }

    @Override
    public IArticolo findById(Integer idArticolo) {

        DbOperationExecutor executor = new DbOperationExecutor();

        String sql = "SELECT COUNT(*) AS C FROM Prodotto AS P WHERE P.Articolo_idArticolo = " + idArticolo + ";";

        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();

        try {
            rs.next();
            if (rs.getInt("C") == 1) {
                // Se C=1 --> è un prodotto
                sql = "SELECT * FROM Articolo AS A " +
                        "INNER JOIN Prodotto AS P " +
                        "ON A.idArticolo = P.Articolo_idArticolo " +
                        "WHERE A.idArticolo = " + idArticolo + ";";

                readOp = new ReadOperation(sql);
                rs = executor.executeOperation(readOp).getResultSet();
                rs.next();
                Prodotto p = new Prodotto();

                p.setId(idArticolo);
                p.setNome(rs.getString("nome"));
                p.setTipologiaProdotto(TipologiaProdottoDAO.getInstance().findTipologiaById(rs.getInt("Tipologia_idTipologia")));
                p.setPrezzo(rs.getFloat("prezzo"));
                p.setPezziDisponibili(rs.getInt("disponibilita"));
                p.setDescrizione(rs.getString("descrizione"));
                p.setIngredienti(IngredienteDAO.getInstance().getIngredientiPerProdotto(idArticolo));
                p.setCommenti(InterazioneUtenteDAO.getInstance().findCommentiByArticolo(idArticolo));

                return p;
            } else {
                // Se C=0 --> è un menu o un super-menu
                sql = "SELECT * FROM Articolo AS A " +
                        "INNER JOIN Menu AS M " +
                        "ON A.idArticolo = M.Articolo_idArticolo " +
                        "WHERE A.idArticolo = " + idArticolo + ";";

                readOp = new ReadOperation(sql);
                rs = executor.executeOperation(readOp).getResultSet();
                rs.next();
                Menu menu = new Menu();

                menu.setId(idArticolo);
                menu.setNome(rs.getString("nome"));
                menu.setDescrizione(rs.getString("descrizione"));
                menu.setSconto(rs.getFloat("sconto"));
                menu.setCommenti(InterazioneUtenteDAO.getInstance().findCommentiByArticolo(idArticolo));

                // Controllo se gli id associati al menu sono articoli o un altro menu
                sql = "SELECT Articolo_idArticolo FROM Menu_has_Articolo AS MA " +
                        "WHERE MA.Menu_Articolo_idArticolo = " + idArticolo + ";";

                IDbOperation readOpMenu = new ReadOperation(sql);
                ResultSet rsMenu = executor.executeOperation(readOpMenu).getResultSet();

                // Carico ricorsivamente i prodotti o i menu nel menu
                while (rsMenu.next()) {
                    menu.add(findById(rsMenu.getInt("Articolo_idArticolo")));
                }

                return menu;
            }
        } catch (SQLException e) {
            // Gestisce le categorie di errori
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non trovo nessun prodotto nel database");
        }

        return null;
    }

    @Override
    public boolean addArticolo(IArticolo articolo) {
        DbOperationExecutor executor = new DbOperationExecutor();
        String sql = "INSERT INTO Articolo (nome, descrizione) VALUES ('" + articolo.getNome() + "', '" + articolo.getDescrizione() + "');";
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        if (articolo instanceof Prodotto) {
            sql = "INSERT INTO Prodotto (Articolo_idArticolo, prezzo, disponibilita, Tipologia_idTipologia) VALUES (" + getLastInsertId() + ", '" + articolo.getPrezzo() + "' , " + articolo.getPezziDisponibili() + " ," + ((Prodotto) articolo).getTipologiaProdotto().getId() + ");";
            writeOp = new WriteOperation(sql);
            rowsAffected += executor.executeOperation(writeOp).getRowsAffected();

            // Carico gli ingredienti
            ArrayList<Ingrediente> ingredienti = ((Prodotto) articolo).getIngredienti();
            if (ingredienti != null && !ingredienti.isEmpty()) {
                for (Ingrediente i : ingredienti) {
                    boolean done = addIngredienteToProdotto(i.getId(), getLastInsertId());
                    if (done) rowsAffected++;
                }
            }

            return rowsAffected == 2 + (ingredienti != null ? ingredienti.size() : 0);
        } else if (articolo instanceof Menu) {
            sql = "INSERT INTO Menu (Articolo_idArticolo, sconto) VALUES (" + getLastInsertId() + ", " + ((Menu) articolo).getSconto() + ");";
            writeOp = new WriteOperation(sql);
            rowsAffected += executor.executeOperation(writeOp).getRowsAffected();

            ArrayList<IArticolo> articoli = ((Menu) articolo).getArticoli();
            if(articoli != null && !articoli.isEmpty()) {
                boolean done;
                for (IArticolo a : articoli) {
                    done = addArticoloToMenu(getLastInsertId(), a.getId());
                    if (done) {
                        rowsAffected++;
                    }
                    else {
                        System.out.println("Il riempimento del menu non è andato a buon fine");
                        return false;
                    }
                }
                return rowsAffected == 2 + ((Menu) articolo).getArticoli().size();
            }
        }
        return false;
    }

    @Override
    public boolean addArticoloToMenu(Integer idMenu, Integer idArticolo) {
        DbOperationExecutor executor = new DbOperationExecutor();

        // L'articolo esiste già nel menu
        String sql = "SELECT COUNT(*) AS C FROM Menu_has_Articolo " +
                "WHERE Menu_Articolo_idArticolo = " + idMenu + " AND Articolo_idArticolo = " + idArticolo + ";";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while (rs.next()) {
                if (rs.getInt("C") == 1) {
                    System.out.println("L'articolo è già presente nel menu");
                    return true;    // Restituisce true perché comunque l'articolo è presente
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non esiste alcuna relazione tra questo articolo e questo menu");
        }

        // L'articolo non è presente nel menu e lo aggiungo
        sql = "INSERT INTO Menu_has_Articolo (Menu_Articolo_idArticolo, Articolo_idArticolo) VALUES (" + idMenu + ", " + idArticolo + ");";

        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }

    @Override
    public boolean removeArticoloFromMenu(Integer idMenu, Integer idArticolo) {
        DbOperationExecutor executor = new DbOperationExecutor();

        // L'articolo non esiste nel menu
        String sql = "SELECT COUNT(*) AS C FROM Menu_has_Articolo " +
                "WHERE Menu_Articolo_idArticolo = " + idMenu + " AND Articolo_idArticolo = " + idArticolo + ";";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while (rs.next()) {
                if (rs.getInt("C") == 0) {
                    System.out.println("L'articolo che si vuole rimuovere non è presente nel menu");
                    return true;    // Restituisce true perché comunque l'articolo non è presente
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Non esiste alcuna relazione tra questo articolo e questo menu");
        }

        // L'articolo esiste nel menu e viene rimosso
        sql = "DELETE FROM Menu_has_Articolo WHERE Menu_Articolo_idArticolo = " + idMenu + " AND Articolo_idArticolo = " + idArticolo + ";";
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }

    @Override
    public boolean editArticolo(IArticolo articolo) {
        DbOperationExecutor executor = new DbOperationExecutor();

        // Controllo se esiste l'articolo che si vuole modificare
        String sql = "SELECT COUNT(*) AS C FROM Articolo WHERE idArticolo = " + articolo.getId() + ";";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while (rs.next()) {
                if (rs.getInt("C") == 0) {
                    System.out.println("L'articolo non esiste");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("L'articolo che si vuole modificare non esiste");
        }

        // L'articolo esiste e lo modifico
        sql = "UPDATE Articolo SET nome = '" + articolo.getNome() + "', descrizione = '" + articolo.getDescrizione() + "' WHERE idArticolo = " + articolo.getId() + ";";

        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }

    /**
     * @param articolo Viene passato un qualunque articolo
     * @return Restituisce true se la rimozione è andata a buon fine, false se l'articolo non è stato rimosso
     * @implNote Per rimuovere l'articolo nella tabella "Articolo" del database, inizia a rimuovere l'articolo dalle tabelle senza vincoli di integrità referenziale, fino ad arrivare alla tabella principale
     */
    @Override
    public boolean removeArticolo(IArticolo articolo) {
        DbOperationExecutor executor = new DbOperationExecutor();

        // L'articolo non esiste
        String sql = "SELECT COUNT(*) AS C FROM Articolo WHERE idArticolo = " + articolo.getId() + ";";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while (rs.next()) {
                if (rs.getInt("C") == 0) {
                    System.out.println("L'articolo non esiste");
                    return true;    // Restituisce true perché comunque l'articolo non esiste
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("L'articolo che si vuole eliminare non esiste");
        }

        int numberOfRecords = 0;    // Per confrontare con il numero di record effettivi da eliminare
        int rowsAffected = 0;

        // Rimozione da Menu_has_Articolo
        sql = "SELECT COUNT(*) AS C FROM Menu_has_Articolo " +
                "WHERE Menu_Articolo_idArticolo = " + articolo.getId() + " OR Articolo_idArticolo = " + articolo.getId() + ";";
        readOp = new ReadOperation(sql);
        rs = executor.executeOperation(readOp).getResultSet();
        try {
            while (rs.next()) {
                numberOfRecords += rs.getInt("C");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("L'articolo che si vuole eliminare non esiste in Menu_has_Articolo");
        }

        if (numberOfRecords > 0) {
            rowsAffected += removeArticoloFromTable(articolo.getId(), "Menu_has_Articolo");
        }

        // Rimozione da Prodotto o Menu (in base all'istanza)
        if (articolo instanceof Prodotto) {

            if(((Prodotto) articolo).getIngredienti() != null && !((Prodotto) articolo).getIngredienti().isEmpty()) {

                sql = "SELECT COUNT(*) AS C FROM Prodotto_has_Ingrediente " +
                        "WHERE Prodotto_Articolo_idArticolo = " + articolo.getId() + ";";

                readOp = new ReadOperation(sql);
                rs = executor.executeOperation(readOp).getResultSet();
                try {
                    while (rs.next()) {
                        numberOfRecords += rs.getInt("C");
                    }
                } catch (SQLException e) {
                    System.out.println("SQL Exception: " + e.getMessage());
                    System.out.println("SQL State: " + e.getSQLState());
                    System.out.println("Vendor Error: " + e.getErrorCode());
                } catch (NullPointerException e) {
                    System.out.println("L'articolo che si vuole eliminare non esiste in Prodotto_has_Ingrediente");
                }

                // Rimozione da Prodotto_has_Ingrediente
                rowsAffected += removeArticoloFromTable(articolo.getId(), "Prodotto_has_Ingrediente");
            }

            // Rimozione da Prodotto
            rowsAffected += removeArticoloFromTable(articolo.getId(), "prodotto");
            numberOfRecords++;

        } else if (articolo instanceof Menu) {

            // Rimozione da Menu
            rowsAffected += removeArticoloFromTable(articolo.getId(), "menu");
            numberOfRecords++;
        }

        // Rimozione da commento
        InterazioneUtenteDAO interazioneUtenteDAO = InterazioneUtenteDAO.getInstance();
        ArrayList<CommentoCliente> commenti = InterazioneUtenteDAO.getInstance().findCommentiByArticolo(articolo.getId());
        numberOfRecords += commenti.size();

        boolean done;
        for (CommentoCliente c : commenti) {
            done = interazioneUtenteDAO.removeCommento(c.getId());
            if (done) {
                rowsAffected++;
            } else {
                System.out.println("Il commento non è stato cancellato");
            }
        }

        // Rimozione da Cliente_has_Articolo (Carrello)
        done = CarrelloDAO.getInstance().removeArticoloFromAll(articolo.getId());
        if (!done) System.out.println("I carrelli non sono stati svuotati");

        // Rimozione da Ordine_has_Articolo
        sql = "SELECT COUNT(*) AS C FROM Ordine_has_Articolo WHERE Articolo_idArticolo = " + articolo.getId() + ";";
        readOp = new ReadOperation(sql);
        rs = executor.executeOperation(readOp).getResultSet();
        try {
            while (rs.next()) {
                numberOfRecords += rs.getInt("C");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("L'articolo che si vuole eliminare non esiste in Ordine_has_Articolo");
        }

        rowsAffected += removeArticoloFromTable(articolo.getId(), "Ordine_has_Articolo");

        // Rimozione da Articolo
        rowsAffected += removeArticoloFromTable(articolo.getId(), "articolo");
        numberOfRecords++;

        return rowsAffected == numberOfRecords;
    }

    /**
     * @return ritorna true se la rimozione è avvenuta con successo, altrimenti ritorna falso
     * @implNote rimuove l'articolo il cui id passato e tutti gli eventuali articoli in esso contenuti (caso menu e supermenu)
     */
    @Override
    public boolean removeArticoloRecursive(IArticolo articolo) {
        if(articolo instanceof Prodotto)
           return removeArticolo(articolo);

        if (((Menu) articolo).getArticoli() != null && !((Menu) articolo).getArticoli().isEmpty()) {  //per gestire il caso menu senza prodotti
            for (IArticolo art : ((Menu) articolo).getArticoli()) {
                removeArticoloFromMenu(articolo.getId(), art.getId());
                removeArticoloRecursive(art);
            }

        }
        return removeArticolo(articolo);
    }

    @Override
    public boolean addIngredienteToProdotto(Integer idIngrediente, Integer idProdotto) {
        DbOperationExecutor executor = new DbOperationExecutor();

        // L'ingrediente esiste già
        String sql = "SELECT COUNT(*) AS C FROM Prodotto_has_Ingrediente " +
                "WHERE Prodotto_Articolo_idArticolo = " + idProdotto + " AND Ingrediente_idIngrediente = " + idIngrediente + ";";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while (rs.next()) {
                if (rs.getInt("C") == 1) {
                    System.out.println("L'ingrediente che si vuole aggiungere è già esistente");
                    return true;    // restituisce true perché comunque l'ingrediente esiste già
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("L'ingrediente che si vuole aggiungere non esiste");
        }

        // L'ingrediente non esisteva nel prodotto e viene aggiunto
        sql = "INSERT INTO Prodotto_has_ingrediente (Prodotto_Articolo_idArticolo, Ingrediente_idIngrediente) VALUES (" + idProdotto + ", " + idIngrediente + ");";
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }

    @Override
    public boolean removeIngredienteFromProdotto(Integer idIngrediente, Integer idProdotto) {
        DbOperationExecutor executor = new DbOperationExecutor();

        // L'ingrediente non è presente nel prodotto
        String sql = "SELECT COUNT(*) AS C FROM Prodotto_has_Ingrediente " +
                "WHERE Prodotto_Articolo_idArticolo = " + idProdotto + " AND Ingrediente_idIngrediente = " + idIngrediente + ";";
        IDbOperation readOp = new ReadOperation(sql);
        ResultSet rs = executor.executeOperation(readOp).getResultSet();
        try {
            while (rs.next()) {
                if (rs.getInt("C") == 0) {
                    System.out.println("L'ingrediente che si vuole rimuovere non è presente nel prodotto");
                    return true;    // restituisce true perché comunque l'ingrediente non c'è
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("L'ingrediente che si vuole aggiungere non esiste");
        }

        // L'ingrediente è presente nel prodotto e viene eliminato
        sql = "DELETE FROM Prodotto_has_Ingrediente WHERE Prodotto_Articolo_idArticolo = " + idProdotto + " AND Ingrediente_idIngrediente = " + idIngrediente + ";";
        IDbOperation writeOp = new WriteOperation(sql);
        int rowsAffected = executor.executeOperation(writeOp).getRowsAffected();

        return rowsAffected == 1;
    }

    @Override
    public boolean updateDisponibilitaAfterOrdine(Ordine ordine) {
        HashMap<Integer, Integer> listaProdotti = ordine.getArticoli();

        DbOperationExecutor executor = new DbOperationExecutor();

        int numberOfItems = 0;
        int rowsAffected = 0;

        for (Map.Entry<Integer, Integer> entry : listaProdotti.entrySet()) {
            IArticolo articolo = findById(entry.getKey());

            if (articolo instanceof Prodotto) {
                numberOfItems++;

                String sql = "UPDATE Prodotto SET disponibilita = disponibilita - " + entry.getValue() + " WHERE Articolo_idArticolo = " + articolo.getId() + ";";
                IDbOperation writeOp = new WriteOperation(sql);
                rowsAffected += executor.executeOperation(writeOp).getRowsAffected();

            } else if (articolo instanceof Menu) {
                ArrayList<IArticolo> articoliMenu = ((Menu) articolo).getArticoli();

                for (IArticolo a : articoliMenu) {
                    if (a instanceof Prodotto) {
                        numberOfItems++;

                        String sql = "UPDATE Prodotto SET disponibilita = disponibilita - " + entry.getValue() + " WHERE Articolo_idArticolo = " + a.getId() + ";";
                        IDbOperation writeOp = new WriteOperation(sql);
                        rowsAffected += executor.executeOperation(writeOp).getRowsAffected();

                    } else if (a instanceof Menu) {
                        ArrayList<IArticolo> articoliSupermenu = ((Menu) a).getArticoli();

                        for (IArticolo p : articoliSupermenu) {
                            numberOfItems++;

                            String sql = "UPDATE Prodotto SET disponibilita = disponibilita - " + entry.getValue() + " WHERE Articolo_idArticolo = " + p.getId() + ";";
                            IDbOperation writeOp = new WriteOperation(sql);
                            rowsAffected += executor.executeOperation(writeOp).getRowsAffected();
                        }
                    }
                }
            }
        }

        return numberOfItems == rowsAffected;
    }

    /**
     * @param idArticolo ID dell'Articolo da eliminare
     * @param table Nome della tabella da cui cancellare l'articolo
     * @return restituisce il numero di righe modificate rowsAffected
     */
    @Override
    public int removeArticoloFromTable(Integer idArticolo, String table) {
        String sql;
        IDbOperation writeOp;
        DbOperationExecutor executor = new DbOperationExecutor();

        table = table.toLowerCase();
        switch (table) {
            case ("articolo"): {
                sql = "DELETE FROM Articolo WHERE idArticolo = " + idArticolo + ";";
                writeOp = new WriteOperation(sql);
                return executor.executeOperation(writeOp).getRowsAffected();
            }
            case ("prodotto"): {
                sql = "DELETE FROM Prodotto WHERE Articolo_idArticolo = " + idArticolo;
                writeOp = new WriteOperation(sql);
                return executor.executeOperation(writeOp).getRowsAffected();
            }
            case ("menu"): {
                sql = "DELETE FROM Menu WHERE Articolo_idArticolo = " + idArticolo;
                writeOp = new WriteOperation(sql);
                return executor.executeOperation(writeOp).getRowsAffected();
            }
            case ("menu_has_articolo"): {
                sql = "DELETE FROM Menu_has_Articolo " +
                        "WHERE Menu_Articolo_idArticolo = " + idArticolo + " OR Articolo_idArticolo = " + idArticolo + ";";
                writeOp = new WriteOperation(sql);
                return executor.executeOperation(writeOp).getRowsAffected();
            }
            case ("ordine_has_articolo"): {
                sql = "DELETE FROM Ordine_has_Articolo WHERE Articolo_idArticolo = " + idArticolo + ";";
                writeOp = new WriteOperation(sql);
                return executor.executeOperation(writeOp).getRowsAffected();
            }
            case ("prodotto_has_ingrediente"): {
                sql = "DELETE FROM Prodotto_has_Ingrediente " +
                        "WHERE Prodotto_Articolo_idArticolo = " + idArticolo + ";";
                writeOp = new WriteOperation(sql);
                return executor.executeOperation(writeOp).getRowsAffected();
            }
            default: return 0;
        }
    }
}
