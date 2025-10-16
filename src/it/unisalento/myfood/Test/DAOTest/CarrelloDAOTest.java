package it.unisalento.myfood.Test.DAOTest;

import it.unisalento.myfood.Business.Security.Strategy.SHA512Hashing;
import it.unisalento.myfood.DAO.*;
import it.unisalento.myfood.model.Carrello;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.IInterazioneUtente;
import it.unisalento.myfood.model.Composite.Menu;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.Ordine;
import it.unisalento.myfood.model.TipologiaProdotto;
import it.unisalento.myfood.model.Utente;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;

public class CarrelloDAOTest {
    private Utente cliente;
    private Utente cliente1;
    private final ITipologiaProdottoDAO TDAO = TipologiaProdottoDAO.getInstance();
    private final IArticoloDAO articoloDAO = ArticoloDAO.getInstance();
    private final IUtenteDAO UDAO = UtenteDAO.getInstance();
    private final ICarrelloDAO CDAO = CarrelloDAO.getInstance();
    private Prodotto panino;
    private Prodotto cocaCola;
    private Menu menu;
    private TipologiaProdotto panTip;
    private TipologiaProdotto bevTip;

    @Before
    public void setUp(){
        TDAO.addTipologia("Tipologia1");
        TDAO.addTipologia("Tipologia2");
        panTip = TDAO.findTipologiaByName("Tipologia1");
        bevTip = TDAO.findTipologiaByName("Tipologia2");
        panino = new Prodotto("Bacon Burger", "bacon croccante [...]", 4.60f, 80, panTip, null, null, null );

        ArrayList<IArticolo> prodotti = new ArrayList<>();
        articoloDAO.addArticolo(panino);
        panino.setId(articoloDAO.getLastInsertId());
        prodotti.add(panino);

        menu = new Menu("Menu large", "gran bel menu!", 80, prodotti, 0.20f, null);

        cocaCola = new Prodotto("CocaCola", "bevanda a base di caffeina", 2.5f, 150, bevTip, null, null, null);

        articoloDAO.addArticolo(cocaCola);
        cocaCola.setId(articoloDAO.getLastInsertId());

        articoloDAO.addArticolo(menu);

        menu.setId(articoloDAO.getLastInsertId());

        ArrayList<Ordine> ordini = new ArrayList<>();
        ArrayList<IInterazioneUtente> interazioni = new ArrayList<>();

        cliente = new Utente("Valentino" ,"Rossi", "valentino@gmail.com", "1234", Utente.RUOLO.CLIENTE, Date.valueOf("1975-02-02") ,"1234567890", Date.valueOf("2023-12-12"), false, false, "Via Verdi 22, Milano", "Pilota", ordini, interazioni, SHA512Hashing.generateSalt());

        UDAO.addUtente(cliente);
        cliente.setId(UDAO.getLastInsertId());

        CDAO.addArticoloToCarrello(menu.getId(), 1, cliente.getId());
        CDAO.addArticoloToCarrello(cocaCola.getId(), 2, cliente.getId());

        cliente1 = new Utente("Mario" ,"Bianchi", "mario@gmail.com", "1234", Utente.RUOLO.CLIENTE, Date.valueOf("1975-02-02") ,"1234567890", Date.valueOf("2023-12-12"), false, false, "Via Verdi 22, Milano", "Pilota", ordini, interazioni, SHA512Hashing.generateSalt());

        UDAO.addUtente(cliente1);
        cliente1.setId(UDAO.getLastInsertId());

        CDAO.addArticoloToCarrello(panino.getId(), 2, cliente1.getId());
        CDAO.addArticoloToCarrello(cocaCola.getId(), 1, cliente1.getId());

    }

    @After
    public void tearDown(){
        CDAO.emptyCarrello(cliente.getId());
        CDAO.emptyCarrello(cliente1.getId());
        articoloDAO.removeArticolo(panino);
        articoloDAO.removeArticolo(menu);
        articoloDAO.removeArticolo(cocaCola);
        TDAO.removeTipologia(panTip.getId());
        TDAO.removeTipologia(bevTip.getId());
        UDAO.removeByEmail("valentino@gmail.com");
        UDAO.removeByEmail("mario@gmail.com");
    }


    @Test
    public void loadCarrelloPerIdUtenteTest() {
        Carrello carrello = CDAO.loadCarrelloPerIdUtente(cliente1.getId());
        Assert.assertNotNull(carrello);
        Assert.assertNotNull(carrello.getArticoli());
        Assert.assertEquals(2, (int) carrello.getArticoli().get(panino.getId()));
        Assert.assertEquals(1, (int) carrello.getArticoli().get(cocaCola.getId()));
        Assert.assertEquals(carrello.getArticoli().size(), 2);



    }

    @Test
    public void addArticoloToCarrelloTest() {
        boolean result = CDAO.addArticoloToCarrello(panino.getId(), 3, cliente1.getId());
        Assert.assertTrue(result);
        Carrello carrello = CDAO.loadCarrelloPerIdUtente(cliente1.getId());
        Assert.assertEquals(carrello.getArticoli().get(panino.getId()), (Integer) 3);

    }

    @Test
    public void removeArticoloFromCarrelloTest() {
        boolean result = CDAO.removeArticoloFromCarrello(cocaCola.getId(), cliente.getId());
        Assert.assertTrue(result);
        Carrello carrello = CDAO.loadCarrelloPerIdUtente(cliente.getId());
        Assert.assertNull(carrello.getArticoli().get(cocaCola.getId()));
    }

    @Test
    public void setNewQuantitaTest() {
        boolean result = CDAO.setNewQuantita(cliente.getId(), cocaCola.getId(), 3);
        Assert.assertTrue(result);
        Carrello carrello = CDAO.loadCarrelloPerIdUtente(cliente.getId());
        Assert.assertEquals(carrello.getArticoli().get(cocaCola.getId()), (Integer) 3);
    }

    @Test
    public void removeArticoloFromAllTest() {
        boolean result = CDAO.removeArticoloFromAll(cocaCola.getId());
        Assert.assertTrue(result);
        Carrello carrello = CDAO.loadCarrelloPerIdUtente(cliente.getId());
        Carrello carrello1 = CDAO.loadCarrelloPerIdUtente(cliente1.getId());
        Assert.assertNull(carrello.getArticoli().get(cocaCola.getId()));
        Assert.assertNull(carrello1.getArticoli().get(cocaCola.getId()));
    }

    @Test
    public void emptyCarrelloTest() {
        boolean result = CDAO.emptyCarrello(cliente.getId());
        Assert.assertTrue(result);
        Carrello carrello = CDAO.loadCarrelloPerIdUtente(cliente.getId());
        Assert.assertEquals(carrello.getArticoli().size(), 0);
    }
}
