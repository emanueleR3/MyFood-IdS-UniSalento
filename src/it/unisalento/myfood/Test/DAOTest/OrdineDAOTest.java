package it.unisalento.myfood.Test.DAOTest;

import it.unisalento.myfood.Business.Security.Strategy.SHA512Hashing;
import it.unisalento.myfood.DAO.*;
import it.unisalento.myfood.model.*;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.IInterazioneUtente;
import it.unisalento.myfood.model.Composite.Menu;
import it.unisalento.myfood.model.Composite.Prodotto;
import org.junit.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class OrdineDAOTest {

   private Utente cliente;
   private Utente cliente1;
   private final TipologiaProdottoDAO tipologiaProdottoDAO = TipologiaProdottoDAO.getInstance();
   private final ArticoloDAO articoloDAO = ArticoloDAO.getInstance();
   private final OrdineDAO ordineDAO = OrdineDAO.getInstance();
   private final UtenteDAO utenteDAO = UtenteDAO.getInstance();
   private Prodotto panino;
   private Prodotto cocaCola;
   private Menu menu;
   private TipologiaProdotto panTip;
   private TipologiaProdotto bevTip;
   private Ordine ordine;
   private Ordine ordine1;
   private Ordine ordine2;

   @Before
   public void setUp() {
      tipologiaProdottoDAO.addTipologia("Panini");
      tipologiaProdottoDAO.addTipologia("Bevande");
      panTip = tipologiaProdottoDAO.findTipologiaByName("Panini");
      bevTip = tipologiaProdottoDAO.findTipologiaByName("Bevande");

      panino = new Prodotto("Bacon Burger", "bacon croccante [...]", 4.60f, 80, panTip, null, null, null );
      articoloDAO.addArticolo(panino);
      panino.setId(articoloDAO.getLastInsertId());

      ArrayList<IArticolo> prodotti = new ArrayList<>();
      prodotti.add(panino);

      menu = new Menu("Menu large", "gran bel menu!", 80, prodotti, 0.20f, null);
      articoloDAO.addArticolo(menu);
      menu.setId(articoloDAO.getLastInsertId());

      cocaCola = new Prodotto("CocaCola", "bevanda a base di caffeina", 2.5f, 150, bevTip, null, null, null);
      articoloDAO.addArticolo(cocaCola);
      cocaCola.setId(articoloDAO.getLastInsertId());

      HashMap<Integer, Integer> articoli = new HashMap<>();
      articoli.put(menu.getId(), 1);
      articoli.put(panino.getId(), 2);

      ArrayList<Ordine> ordini = new ArrayList<>();
      ArrayList<IInterazioneUtente> interazioni = new ArrayList<>();

      cliente = new Utente("Valentino" ,"Rossi", "valentino@gmail.com", "1234", Utente.RUOLO.CLIENTE, Date.valueOf("1975-02-02") ,"1234567890", Date.valueOf("2023-12-12"), false, false, "Via Verdi 22, Milano", "Pilota", ordini, interazioni, SHA512Hashing.generateSalt());
      utenteDAO.addUtente(cliente);
      cliente.setId(utenteDAO.getLastInsertId());

      Carrello carrello = new Carrello(articoli, cliente);
      ordineDAO.createOrdine(carrello);
      ordine = ordineDAO.findById(ordineDAO.getLastInsertId());

      ordineDAO.setRicorrente(ordine.getIdOrdine(), true);
      ordine.setRicorrente(true);

      ordini.add(ordine);
      cliente.setOrdini(ordini);

      // Creo un cliente
      cliente1 = new Utente("Mario" ,"Bianchi", "mario@gmail.com", "1234", Utente.RUOLO.CLIENTE, Date.valueOf("1975-02-02") ,"1234567890", Date.valueOf("2023-12-12"), false, false, "Via Verdi 22, Milano", "Pilota", ordini, interazioni, SHA512Hashing.generateSalt());
      utenteDAO.addUtente(cliente1);
      cliente1.setId(utenteDAO.getLastInsertId());

      // Creo un nuovo carrello
      articoli.clear();
      articoli.put(panino.getId(), 3);
      articoli.put(cocaCola.getId(), 2);

      carrello = new Carrello(articoli, cliente1);

      // Creo l'ordine
      ordineDAO.createOrdine(carrello);

      // Salvo l'ordine in locale
      ordine1 = ordineDAO.findById(ordineDAO.getLastInsertId());

      // Creo un nuovo carrello
      articoli.clear();
      articoli.put(menu.getId(), 1);

      carrello = new Carrello(articoli, cliente1);

      // Creo l'ordine
      ordineDAO.createOrdine(carrello);

      // Salvo l'ordine in locale
      ordine2 = ordineDAO.findById(ordineDAO.getLastInsertId());

      // Aggiorno lo stato a "PAGATO"
      ordineDAO.updateStatoOrdine(ordine2, IOrdine.STATO.PAGATO);
      ordine2.setStato(IOrdine.STATO.PAGATO);

      // Creo l'ordine
      ordini.clear();
      ordini.add(ordine1);
      ordini.add(ordine2);
      cliente1.setOrdini(ordini);
   }

   @After
   public void tearDown() {
      ordineDAO.remove(ordine);
      ordineDAO.remove(ordine1);
      ordineDAO.remove(ordine2);
      articoloDAO.removeArticoloRecursive(menu);
      articoloDAO.removeArticolo(cocaCola);
      tipologiaProdottoDAO.removeTipologia(panTip.getId());
      tipologiaProdottoDAO.removeTipologia(bevTip.getId());
      utenteDAO.removeByEmail("valentino@gmail.com");
      utenteDAO.removeByEmail("mario@gmail.com");
   }

   @Test
   public void getLastInsertId() {
      Assert.assertEquals(ordine2.getIdOrdine(), ordineDAO.getLastInsertId());
   }

   @Test
   public void findByIdTest() {
      Ordine ordineTest = ordineDAO.findById(ordine1.getIdOrdine());

      Assert.assertNotNull(ordineTest);

      Assert.assertEquals(ordineTest.getIdOrdine(), ordine1.getIdOrdine());
      Assert.assertEquals(ordineTest.getArticoli(), ordine1.getArticoli());
      Assert.assertEquals(ordineTest.getCliente().getId(), ordine1.getCliente().getId());
      Assert.assertEquals(ordineTest.getData(), ordine1.getData());
      Assert.assertEquals(ordineTest.getStato(), ordine1.getStato());
      Assert.assertEquals(ordineTest.getImporto(), ordine1.getImporto());
   }

   @Test
   public void findByState() {
      ArrayList<Ordine> ordini = ordineDAO.findByState(IOrdine.STATO.NON_PAGATO);

      Assert.assertNotNull(ordini);
      Assert.assertEquals(2, ordini.size());
      Assert.assertEquals(ordine.getIdOrdine(), ordini.get(0).getIdOrdine());
      Assert.assertEquals(ordine1.getIdOrdine(), ordini.get(1).getIdOrdine());

      ordineDAO.updateStatoOrdine(ordine1, IOrdine.STATO.PAGATO);
      ordine1.setStato(IOrdine.STATO.PAGATO);

      ordini = ordineDAO.findByState(IOrdine.STATO.NON_PAGATO);
      Assert.assertNotNull(ordini);
      Assert.assertEquals(1, ordini.size());
      Assert.assertEquals(ordine.getIdOrdine(), ordini.get(0).getIdOrdine());
   }

   @Test
   public void findByRecurrent() {
      ArrayList<Ordine> ordini = ordineDAO.findByRecurrent(cliente.getId());

      Assert.assertNotNull(ordini);
      Assert.assertEquals(1, ordini.size());
      Assert.assertEquals(ordine.getIdOrdine(), ordini.get(0).getIdOrdine());
   }

   @Test
   public void findByCliente() {
      // Cliente
      ArrayList<Ordine> ordini = ordineDAO.findByCliente(cliente.getId());

      Assert.assertNotNull(ordini);
      Assert.assertEquals(1, ordini.size());
      Assert.assertEquals(ordine.getIdOrdine(), ordini.get(0).getIdOrdine());

      // Cliente 1
      ordini = ordineDAO.findByCliente(cliente1.getId());

      Assert.assertNotNull(ordini);
      Assert.assertEquals(2, ordini.size());
      Assert.assertEquals(ordine1.getIdOrdine(), ordini.get(0).getIdOrdine());
      Assert.assertEquals(ordine2.getIdOrdine(), ordini.get(1).getIdOrdine());
   }

   @Test
   public void articoliPerIdOrdineTest() {
      HashMap<Integer, Integer> articoli = ordineDAO.articoliPerIdOrdine(ordine1.getIdOrdine());

      Assert.assertNotNull(articoli);
      Assert.assertEquals(2, articoli.size());
      Assert.assertEquals( 3, (int) articoli.get(panino.getId()));
      Assert.assertEquals( 2, (int) articoli.get(cocaCola.getId()));
   }

   @Test
   public void createOrdineTest() {
      ArrayList<IArticolo> prodotti = new ArrayList<>();
      prodotti.add(panino);
      prodotti.add(cocaCola);

      Menu menu = new Menu("Menu large", "gran bel menu!", 80, prodotti, 0.20f, null);
      articoloDAO.addArticolo(menu);
      menu.setId(articoloDAO.getLastInsertId());

      HashMap<Integer, Integer> articoli = new HashMap<>();
      articoli.put(panino.getId(), 2);
      articoli.put(cocaCola.getId(), 3);
      articoli.put(menu.getId(), 1);

      Carrello carrello = new Carrello(articoli, cliente);

      Assert.assertTrue(ordineDAO.createOrdine(carrello));

      Ordine ordineTest = ordineDAO.findById(ordineDAO.getLastInsertId());

      Assert.assertNotNull(ordineTest);
      Assert.assertEquals(ordineDAO.getLastInsertId(), ordineTest.getIdOrdine());
      Assert.assertEquals(3, ordineTest.getArticoli().size());
      Assert.assertEquals(2, (int) ordineTest.getArticoli().get(panino.getId()));
      Assert.assertEquals(3, (int) ordineTest.getArticoli().get(cocaCola.getId()));
      Assert.assertEquals(1, (int) ordineTest.getArticoli().get(menu.getId()));

      articoloDAO.removeArticolo(menu);
      ordineDAO.remove(ordineTest);
   }

   @Test
   public void removeTest() {
      HashMap<Integer, Integer> articoli = new HashMap<>();

      articoli.put(panino.getId(), 2);
      articoli.put(cocaCola.getId(), 3);

      Carrello carrello = new Carrello(articoli, cliente);

      ordineDAO.createOrdine(carrello);

      Ordine ordineTest = ordineDAO.findById(ordineDAO.getLastInsertId());

      Assert.assertTrue(ordineDAO.remove(ordineTest));

      Assert.assertNull(ordineDAO.findById(ordineTest.getIdOrdine()));
   }

   @Test
   public void updateStatoOrdineTest() {
      // Ordine parte da "NON PAGATO"
      boolean done = ordineDAO.updateStatoOrdine(ordine, IOrdine.STATO.IN_LAVORAZIONE);
      ordine.setStato(IOrdine.STATO.IN_LAVORAZIONE);

      Assert.assertTrue(done);
      Assert.assertEquals(ordine.getStato(), ordineDAO.findById(ordine.getIdOrdine()).getStato());
   }

   @Test
   public void setRicorrenteTest() {
      // Ordine parte da ricorrente = true
      boolean done = ordineDAO.setRicorrente(ordine.getIdOrdine(), false);
      ordine.setRicorrente(false);

      Assert.assertTrue(done);
      Assert.assertEquals(ordine.isRicorrente(), ordineDAO.findById(ordine.getIdOrdine()).isRicorrente());

      // Lo riporto a true
      done = ordineDAO.setRicorrente(ordine.getIdOrdine(), true);
      ordine.setRicorrente(true);

      Assert.assertTrue(done);
      Assert.assertEquals(ordine.isRicorrente(), ordineDAO.findById(ordine.getIdOrdine()).isRicorrente());
   }
}
