package it.unisalento.myfood.Test.DAOTest;

import it.unisalento.myfood.DAO.*;
import it.unisalento.myfood.Test.PopolaDatabase;
import it.unisalento.myfood.model.*;
import it.unisalento.myfood.model.Composite.*;

import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ArticoloDAOTest {

    private Menu menu;
    private Menu menu1;
    private TipologiaProdotto panTip;

    private Prodotto panino;

    private final ArticoloDAO articoloDAO = ArticoloDAO.getInstance();
    private final TipologiaProdottoDAO tipologiaProdottoDAO = TipologiaProdottoDAO.getInstance();
    private final IngredienteDAO ingredienteDAO = IngredienteDAO.getInstance();
    private final TipologiaIngredienteDAO tipologiaIngredienteDAO = TipologiaIngredienteDAO.getInstance();
    private final AziendaDAO aziendaDAO = AziendaDAO.getInstance();

    @Before
    public void setUp() {
        // Aggiunge la tipologia "panini" alle tipologie
        tipologiaProdottoDAO.addTipologia("Panini");
        panTip = tipologiaProdottoDAO.findTipologiaByName("Panini");

        // Crea una lista di articoli
        ArrayList<IArticolo> articoli = new ArrayList<>();

        // Crea un prodotto
        panino = new Prodotto("Bacon Burger", "bacon croccante [...]", 4.60f, 80, panTip, null, null, null);

        // Aggiunge il prodotto alla tabella articolo e setta l'ID
        boolean result = articoloDAO.addArticolo(panino);  //testa anche la addArticoloToMenu
        panino.setId(articoloDAO.getLastInsertId());

        // Aggiunge il prodotto alla lista di articoli
        articoli.add(panino);


        // Crea un menu costituito da un prodotto
        ArrayList<IArticolo> articoliMenu1 = new ArrayList<>();
        articoliMenu1.add(panino);
        menu1 = new Menu("sottomenu x" , "sfizioserie aggiuntive", 80, articoliMenu1, 0.20f, null);

        // Aggiunge il menu alla tabella articolo e setta l'ID
        result = articoloDAO.addArticolo(menu1);
        menu1.setId(articoloDAO.getLastInsertId());

        // Aggiunge il menu agli articoli
        articoli.add(menu1);

        // Crea un menu e inserisce gli articoli (un menu e un prodotto)
        menu = new Menu("Menu large", "gran bel menu!", 80, articoli, 0.20f, null);

        // Aggiunge il menu alla tabella articolo e setta l'ID
        result = articoloDAO.addArticolo(menu);
        menu.setId(articoloDAO.getLastInsertId());

    }

    @After
    public void tearDown() {
        boolean done = articoloDAO.removeArticoloRecursive(menu);
        Assert.assertTrue(done);

        tipologiaProdottoDAO.removeTipologia(panTip.getId());
        articoloDAO.removeArticolo(panino);
        articoloDAO.removeArticolo(menu1);
        articoloDAO.removeArticolo(menu);

    }

   @Test
    public void findProdottoByTipologiaTest() {
        // Aggiungo una tipologia fittizia e inserisco un prodotto --> Non dovrà uscire nell'arraylist finale
        tipologiaProdottoDAO.addTipologia("Bevande");
        TipologiaProdotto bevTip = tipologiaProdottoDAO.findTipologiaByName("Bevande");

        Prodotto cocaCola = new Prodotto("CocaCola", "bevanda a base di caffeina", 2.5f, 150, bevTip, null, null, null);
        articoloDAO.addArticolo(cocaCola);
        cocaCola.setId(articoloDAO.getLastInsertId());

        // Aggiungo un prodotto --> Dovrà uscire nell'arraylist finale, insieme al panino del setUp
        Prodotto hamburger = new Prodotto("Hamburger", "[...]", 4.60f, 80, panTip, null, null, null);
        articoloDAO.addArticolo(hamburger);
        hamburger.setId(articoloDAO.getLastInsertId());

        List<IArticolo> prodotti = articoloDAO.findProdottoByTipologia(tipologiaProdottoDAO.findTipologiaByName("Panini"));

        // Deve contenere solo Panino e Hamburger
        Assert.assertEquals(prodotti.size(), 2);

        Assert.assertEquals(prodotti.get(0).getId(), panino.getId());
        Assert.assertEquals(prodotti.get(1).getId(), hamburger.getId());

        // Rimuovo gli oggetti creati in questo metodo
        articoloDAO.removeArticolo(hamburger);
        articoloDAO.removeArticolo(cocaCola);
        TipologiaProdottoDAO.getInstance().removeTipologia(bevTip.getId());
    }

    @Test
    public void findProdottoByTipologiaContainsTest() {
        /*TipologiaIngrediente tipologiaSalume = tipologiaIngredienteDAO.findTipologiaById(tipologiaIngredienteDAO.getLastInsertId());

        aziendaDAO.addAzienda("CESARE FIORUCCI S.P.A.", "04731980969");
        Azienda fiorucci = aziendaDAO.findById(aziendaDAO.getLastInsertId());

        Ingrediente ingrediente = new Ingrediente("Bacon", tipologiaSalume, fiorucci, null);

        ingredienteDAO.addIngrediente(ingrediente);
        ingrediente.setId(ingredienteDAO.getLastInsertId());

        // Aggiungo al panino il nuovo ingrediente
        ArrayList<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente);
        panino.setIngredienti(ingredienti);

       List<IArticolo> prodotti =  articoloDAO.findProdottoByTipologiaContains(panTip, ingrediente);

       Assert.assertEquals(panino.getId(), prodotti.get(0).getId());

        Iterator<IArticolo> iterator = prodotti.listIterator();

        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }

        ingredienteDAO.removeIngrediente(ingrediente.getId());
        aziendaDAO.removeAzienda(fiorucci.getId());
*/


        //TODO
    }

    @Test
    public void findMenuContainsTest() {
        //TODO
    }

    @Test
    public void findMenuTest() {
        List<IArticolo> menuList = articoloDAO.findMenu();

        // Dovranno esserci due menu (menu1 e menu)
        Assert.assertEquals(menuList.size(), 2);

        Assert.assertEquals(menuList.get(0).getId(), menu1.getId());
        Assert.assertEquals(menuList.get(1).getId(), menu.getId());
    }

    @Test
    public void findByIdTest() {
        // Provo sul prodotto
        Prodotto prodottoTest = new Prodotto("ProdottoTest", "[...]", 2f, 150, panTip, null, null, null);

        articoloDAO.addArticolo(prodottoTest);
        prodottoTest.setId(articoloDAO.getLastInsertId());

        IArticolo prodotto = articoloDAO.findById(articoloDAO.getLastInsertId());

        Assert.assertEquals(prodotto.getId(), prodottoTest.getId());

        // Provo sul menu
        Menu menuTest = new Menu("MenuTest", "[...]", 80, null, 0.20f, null);

        articoloDAO.addArticolo(menuTest);
        menuTest.setId(articoloDAO.getLastInsertId());

        IArticolo menu = articoloDAO.findById(articoloDAO.getLastInsertId());

        Assert.assertEquals(menu.getId(), menuTest.getId());
        Assert.assertEquals("MenuTest", menuTest.getNome());
        Assert.assertEquals("[...]", menuTest.getDescrizione());
        Assert.assertEquals((Integer) 80, menuTest.getPezziDisponibili());
        Assert.assertEquals((Float) 0.20f, menuTest.getSconto());


        // Rimuovo il prodotto e il menu
        articoloDAO.removeArticolo(prodottoTest);
        articoloDAO.removeArticolo(menuTest);
    }

    @Test
    public void addArticoloTest() {
        Prodotto prodottoTest = new Prodotto("ProdottoTest", "[...]", 2f, 150, panTip, null, null, null);

        boolean done = articoloDAO.addArticolo(prodottoTest);
        Integer id = articoloDAO.getLastInsertId();
        prodottoTest.setId(id);

        Assert.assertTrue(done);
        Assert.assertEquals(id, prodottoTest.getId());
        Assert.assertEquals("ProdottoTest", prodottoTest.getNome());
        Assert.assertEquals("[...]", prodottoTest.getDescrizione());
        Assert.assertEquals((Float) 2f, prodottoTest.getPrezzo());
        Assert.assertEquals(panTip.getId(), prodottoTest.getTipologiaProdotto().getId());

        // Rimozione
        articoloDAO.removeArticolo(prodottoTest);
    }

    @Test
    public void removeArticoloProdottoTest() {
        Prodotto prodottoTest = new Prodotto("ProdottoTest", "[...]", 2f, 150, panTip, null, null, null);
        articoloDAO.addArticolo(prodottoTest);
        prodottoTest.setId(articoloDAO.getLastInsertId());

        ArrayList<IArticolo> articoliTest = new ArrayList<>();
        articoliTest.add(prodottoTest);

        ArrayList<CommentoCliente> commentiTest = new ArrayList<>();

        // Popolamento database
        Utente cliente = PopolaDatabase.creaCliente();
        Carrello carrello = PopolaDatabase.creaCarrello(cliente, prodottoTest, 1);
        Ordine ordine = PopolaDatabase.creaOrdine(carrello);
        CommentoCliente commento = (CommentoCliente) PopolaDatabase.creaCommento(prodottoTest, cliente);
        Menu menuTest = PopolaDatabase.creaMenu(articoliTest, commentiTest);
        Azienda azienda = PopolaDatabase.createAzienda();
        TipologiaIngrediente tipologiaIngrediente = PopolaDatabase.creaTipologiaIngrediente("Aroma");
        Ingrediente ingrediente = PopolaDatabase.creaIngrediente(tipologiaIngrediente, "Alloro", azienda, null);

        ArrayList<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente);

        PopolaDatabase.setIngredientiPerProdotto(prodottoTest, ingredienti);

        // Aggiungo in locale le informazioni aggiunte

        prodottoTest = (Prodotto) articoloDAO.findById(prodottoTest.getId());

        ArrayList<Ordine> ordini = new ArrayList<>();
        ordini.add(ordine);
        cliente.setOrdini(ordini);

        ArrayList<IInterazioneUtente> commenti = new ArrayList<>();
        commenti.add(commento);
        cliente.setInterazioni(commenti);

        // Test
        boolean done = articoloDAO.removeArticolo(prodottoTest);

        Assert.assertTrue(done);
        Assert.assertNull(articoloDAO.findById(prodottoTest.getId()));

        // Rimozione
        PopolaDatabase.svuotaOrdine(ordine);
        PopolaDatabase.svuotaCarrello(cliente);
        PopolaDatabase.svuotaCommento(commento);
        PopolaDatabase.rimuoviUtente(cliente);
        PopolaDatabase.eliminaMenu(menuTest);
        PopolaDatabase.eliminaIngrediente(ingrediente);
        PopolaDatabase.eliminaTipologiaIngrediente(tipologiaIngrediente);
        PopolaDatabase.eliminaAzienda(azienda);
    }

    @Test
    public void removeArticoloMenuTest() {
        Prodotto prodottoTest = new Prodotto("ProdottoTest", "[...]", 2f, 150, panTip, null, null, null);
        articoloDAO.addArticolo(prodottoTest);
        prodottoTest.setId(articoloDAO.getLastInsertId());

        ArrayList<IArticolo> articoliTest = new ArrayList<>();
        articoliTest.add(prodottoTest);

        ArrayList<CommentoCliente> commentiTest = new ArrayList<>();

        // Popolamento database
        Utente cliente = PopolaDatabase.creaCliente();
        Menu menuTest = PopolaDatabase.creaMenu(articoliTest, commentiTest);
        Carrello carrello = PopolaDatabase.creaCarrello(cliente, menuTest, 1);
        Ordine ordine = PopolaDatabase.creaOrdine(carrello);
        CommentoCliente commento = (CommentoCliente) PopolaDatabase.creaCommento(menuTest, cliente);
        Azienda azienda = PopolaDatabase.createAzienda();
        TipologiaIngrediente tipologiaIngrediente = PopolaDatabase.creaTipologiaIngrediente("Aroma");
        Ingrediente ingrediente = PopolaDatabase.creaIngrediente(tipologiaIngrediente, "Alloro", azienda, null);

        ArrayList<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente);

        PopolaDatabase.setIngredientiPerProdotto(prodottoTest, ingredienti);

        // Aggiungo in locale le informazioni aggiunte

        menuTest = (Menu) articoloDAO.findById(menuTest.getId());

        ArrayList<Ordine> ordini = new ArrayList<>();
        ordini.add(ordine);
        cliente.setOrdini(ordini);

        ArrayList<IInterazioneUtente> commenti = new ArrayList<>();
        commenti.add(commento);
        cliente.setInterazioni(commenti);

        // Test
        boolean done = articoloDAO.removeArticolo(menuTest);

        Assert.assertTrue(done);
        Assert.assertNull(articoloDAO.findById(menuTest.getId()));

        // Rimozione
        prodottoTest = (Prodotto) articoloDAO.findById(prodottoTest.getId());

        PopolaDatabase.svuotaOrdine(ordine);
        PopolaDatabase.svuotaCarrello(cliente);
        PopolaDatabase.svuotaCommento(commento);
        PopolaDatabase.rimuoviUtente(cliente);
        PopolaDatabase.svuotaIngredientiPerProdotto(prodottoTest);
        PopolaDatabase.eliminaIngrediente(ingrediente);
        articoloDAO.removeArticolo(prodottoTest);
        PopolaDatabase.eliminaTipologiaIngrediente(tipologiaIngrediente);
        PopolaDatabase.eliminaAzienda(azienda);
    }

    @Test
    public void removeArticoloRecursiveTest() {
        //TODO
    }

    @Test
    public void removeArticoloFromTable() {
        // TODO
    }

    @Test
    public void getLastInsertIdTest() {
        Prodotto prodottoTest = new Prodotto("ProdottoTest", "[...]", 2f, 150, panTip, null, null, null);
        articoloDAO.addArticolo(prodottoTest);
        prodottoTest.setId(articoloDAO.getLastInsertId());

        articoloDAO.removeArticolo(prodottoTest);
    }

    @Test
    public void editArticoloTest() {
        menu.setNome("menu small");
        menu.setDescrizione("menu modesto...");

        boolean done = articoloDAO.editArticolo(menu);
        Menu menuTest = (Menu) articoloDAO.findById(menu.getId());

        Assert.assertTrue(done);
        Assert.assertEquals(menuTest.getDescrizione(), menu.getDescrizione());
        Assert.assertEquals(menuTest.getNome(), menu.getNome());
    }

    @Test
    public void addArticoloToMenuTest() {
        ArrayList<IArticolo> articoliMenuTest = new ArrayList<>();
        Menu menuTest = new Menu("MenuTest" , "[...]", 80, articoliMenuTest, 0.20f, null);

        articoloDAO.addArticolo(menuTest);
        Integer idMenuTest = articoloDAO.getLastInsertId();
        menuTest.setId(idMenuTest);

        //articoliMenuTest.add(panino);
        menuTest.add(panino);   //questo metodo che andrà messo nel business, aggiorna già l'arraylist

        boolean done = articoloDAO.addArticoloToMenu(idMenuTest, panino.getId());
        ArrayList<IArticolo> articoliMenuDB = ((Menu) articoloDAO.findById(idMenuTest)).getArticoli();

        Assert.assertTrue(done);
        Assert.assertEquals(articoliMenuDB.size(), menuTest.getArticoli().size());
        Assert.assertEquals(articoliMenuDB.get(0).getId(), panino.getId());

        articoloDAO.removeArticoloRecursive(menuTest);
    }

    @Test
    public void removeArticoloFromMenuTest() {
        boolean done = articoloDAO.removeArticoloFromMenu(menu.getId(), panino.getId());
        menu = (Menu) articoloDAO.findById(menu.getId());

        // Ora menu1 dovrebbe contenere solo menu1
        Assert.assertTrue(done);

        Assert.assertEquals(menu.getArticoli().size(), 1);
        Assert.assertEquals(menu.getArticoli().get(0).getId(), menu1.getId());

        articoloDAO.removeArticolo(panino); //perché il tearDown elimina ricorsivamente il menu che ora non contiene più il panino
    }

    @Test
    public void addIngredienteToProdottoTest() {
        tipologiaIngredienteDAO.addTipologia("Salume");
        TipologiaIngrediente tipologiaSalume = tipologiaIngredienteDAO.findTipologiaById(tipologiaIngredienteDAO.getLastInsertId());

        aziendaDAO.addAzienda("CESARE FIORUCCI S.P.A.", "04731980969");
        Azienda fiorucci = aziendaDAO.findById(aziendaDAO.getLastInsertId());

        Ingrediente ingrediente = new Ingrediente("Bacon", tipologiaSalume, fiorucci, null);

        ingredienteDAO.addIngrediente(ingrediente);
        ingrediente.setId(ingredienteDAO.getLastInsertId());

        // Aggiungo al panino il nuovo ingrediente
        ArrayList<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente);
        panino.setIngredienti(ingredienti);

        boolean done = articoloDAO.addIngredienteToProdotto(ingrediente.getId(), panino.getId());
        Prodotto paninoDB = (Prodotto) articoloDAO.findById(panino.getId());

        Assert.assertTrue(done);

        Assert.assertEquals(paninoDB.getIngredienti().size(), panino.getIngredienti().size());
        Assert.assertEquals(paninoDB.getIngredienti().get(0).getId(), ingrediente.getId());

        // Rimuovo
        articoloDAO.removeIngredienteFromProdotto(ingrediente.getId(), panino.getId());  ///test di removeIngredienteFromProdotto
        ingredienteDAO.removeIngrediente(ingrediente.getId());
        tipologiaIngredienteDAO.removeTipologia(tipologiaSalume.getId());
        aziendaDAO.removeAzienda(fiorucci.getId());
    }

    @Test
    public void removeIngredienteFromProdottoTest(){
        // Aggiungo una tipologia
        tipologiaIngredienteDAO.addTipologia("Salume");
        TipologiaIngrediente tipologiaSalume = tipologiaIngredienteDAO.findTipologiaById(tipologiaIngredienteDAO.getLastInsertId());

        // Aggiungo
        aziendaDAO.addAzienda("CESARE FIORUCCI S.P.A.", "04731980969");
        Azienda fiorucci = aziendaDAO.findById(aziendaDAO.getLastInsertId());

        // Aggiungo un ingrediente
        Ingrediente ingrediente = new Ingrediente("Bacon", tipologiaSalume, fiorucci, null);
        ingredienteDAO.addIngrediente(ingrediente);
        ingrediente.setId(ingredienteDAO.getLastInsertId());

        // Aggiungo all'arraylist ingredienti il nuovo ingrediente
        ArrayList<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(ingrediente);

        // Creo e aggiungo un nuovo prodotto con un ingrediente
        Prodotto prodottoTest = new Prodotto("ProdottoTest", "[...]", 2f, 150, panTip, ingredienti, null, null);
        articoloDAO.addArticolo(prodottoTest);
        prodottoTest.setId(articoloDAO.getLastInsertId());

        // Rimuovo l'ingrediente dal prodotto
        boolean done = articoloDAO.removeIngredienteFromProdotto(ingrediente.getId(), prodottoTest.getId());
        prodottoTest = (Prodotto) articoloDAO.findById(prodottoTest.getId());

        Assert.assertTrue(done);

        Assert.assertEquals(prodottoTest.getIngredienti().size(), 0);

        // Rimuovo
        articoloDAO.removeArticolo(prodottoTest);
        ingredienteDAO.removeIngrediente(ingrediente.getId());
        tipologiaIngredienteDAO.removeTipologia(tipologiaSalume.getId());
        aziendaDAO.removeAzienda(fiorucci.getId());
    }

    @Test
    public void updateDisponibilitaAfterOrdineTest() {
        HashMap<Integer, Integer> articoliOrdine = new HashMap<>();
        Utente cliente = PopolaDatabase.creaCliente();

        // Ordine contenente due prodotti
        Prodotto prodottoTest1 = new Prodotto("TestProdotto1", "[...]", 4.60f, 100, panTip, null, null, null);
        articoloDAO.addArticolo(prodottoTest1);
        prodottoTest1.setId(articoloDAO.getLastInsertId());

        Prodotto prodottoTest2 = new Prodotto("TestProdotto2", "[...]", 4.60f, 50, panTip, null, null, null);
        articoloDAO.addArticolo(prodottoTest2);
        prodottoTest2.setId(articoloDAO.getLastInsertId());

        articoliOrdine.put(prodottoTest1.getId(), 5);
        articoliOrdine.put(prodottoTest2.getId(), 1);

        Ordine ordine = new Ordine();
        ordine.setArticoli(articoliOrdine);

        boolean done = articoloDAO.updateDisponibilitaAfterOrdine(ordine);

        prodottoTest1.setPezziDisponibili(articoloDAO.findById(prodottoTest1.getId()).getPezziDisponibili());
        prodottoTest2.setPezziDisponibili(articoloDAO.findById(prodottoTest2.getId()).getPezziDisponibili());

        Assert.assertTrue(done);
        Assert.assertEquals(95, (int) prodottoTest1.getPezziDisponibili());
        Assert.assertEquals(49, (int) prodottoTest2.getPezziDisponibili());

        // Ordine contenente un menu contenente solo due prodotti
        ArrayList<IArticolo> articoliMenu = new ArrayList<>();
        articoliMenu.add(prodottoTest1);
        articoliMenu.add(prodottoTest2);

        Menu menuTest = new Menu("TestMenu" , "[...]", 80, articoliMenu, 0.20f, null);
        articoloDAO.addArticolo(menuTest);
        menuTest.setId(articoloDAO.getLastInsertId());

        articoliOrdine.clear();
        articoliOrdine.put(menuTest.getId(), 2);

        ordine.setArticoli(articoliOrdine);

        done = articoloDAO.updateDisponibilitaAfterOrdine(ordine);

        prodottoTest1.setPezziDisponibili(articoloDAO.findById(prodottoTest1.getId()).getPezziDisponibili());
        prodottoTest2.setPezziDisponibili(articoloDAO.findById(prodottoTest2.getId()).getPezziDisponibili());

        Assert.assertTrue(done);
        Assert.assertEquals(93, (int) prodottoTest1.getPezziDisponibili());
        Assert.assertEquals(47, (int) prodottoTest2.getPezziDisponibili());

        // Ordine contenente un supermenu composto da un prodotto e da un menu di un prodotto
        articoliMenu.clear();
        articoliMenu.add(prodottoTest1);
        articoliMenu.add(prodottoTest2);

        menuTest.setArticoli(articoliMenu);

        articoliMenu.add(menuTest);

        Menu supermenuTest = new Menu("TestMenu" , "[...]", 80, articoliMenu, 0.20f, null);
        articoloDAO.addArticolo(supermenuTest);
        supermenuTest.setId(articoloDAO.getLastInsertId());

        articoliOrdine.clear();
        articoliOrdine.put(supermenuTest.getId(), 3);

        ordine.setArticoli(articoliOrdine);

        done = articoloDAO.updateDisponibilitaAfterOrdine(ordine);

        prodottoTest1.setPezziDisponibili(articoloDAO.findById(prodottoTest1.getId()).getPezziDisponibili());
        prodottoTest2.setPezziDisponibili(articoloDAO.findById(prodottoTest2.getId()).getPezziDisponibili());

        Assert.assertTrue(done);
        Assert.assertEquals(87, (int) prodottoTest1.getPezziDisponibili());
        Assert.assertEquals(41, (int) prodottoTest2.getPezziDisponibili());

        // Rimozione
        articoloDAO.removeArticolo(prodottoTest1);
        articoloDAO.removeArticolo(prodottoTest2);
        articoloDAO.removeArticolo(menuTest);
        articoloDAO.removeArticolo(supermenuTest);

        PopolaDatabase.rimuoviUtente(cliente);
    }
}