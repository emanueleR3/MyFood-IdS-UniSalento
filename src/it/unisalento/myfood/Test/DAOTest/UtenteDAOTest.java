package it.unisalento.myfood.Test.DAOTest;

import it.unisalento.myfood.Business.InvalidFormatException;
import it.unisalento.myfood.Business.Security.AbstractFactory.StrategyFactory;
import it.unisalento.myfood.Business.Security.Strategy.PasswordHashingContext;
import it.unisalento.myfood.Business.Security.Strategy.SHA512Hashing;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.DAO.*;
import it.unisalento.myfood.model.Carrello;
import it.unisalento.myfood.model.Composite.CommentoCliente;
import it.unisalento.myfood.model.Composite.IInterazioneUtente;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.Ordine;
import it.unisalento.myfood.model.TipologiaProdotto;
import it.unisalento.myfood.model.Utente;
import org.junit.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class UtenteDAOTest {

    private final IUtenteDAO UDAO = UtenteDAO.getInstance();

    @Before
    public void setUp(){
        UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();

        try {
            utenteBusiness.signUpCliente("valentino@gmail.com" ,"Valentino", "Rossi", "1234567890", "02-02-1975", "Pilota", "Via Verdi 22, Milano", true);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        try {
            utenteBusiness.signUpAmministratore("mario@gmail.com" ,"Mario", "Bianchi", "1234567890", "02-02-1975", "Pilota", true);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        try {
            utenteBusiness.signUpCucina("vittorio@gmail.com" ,"Vittorio", "Veneto", "1234567890", "02-02-1975", "Pilota", true);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown(){
        UDAO.removeByEmail("mario@gmail.com");
        UDAO.removeByEmail("valentino@gmail.com");
        UDAO.removeByEmail("vittorio@gmail.com");
    }

    @Test
    public void getLastInsertIdTest() {
        UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();
        try {
            utenteBusiness.signUpCliente("emanuele@gmail.com" ,"Emanuele", "Romano", "1234567890", "02-02-1975", "Pilota", "Via Verdi 22, Milano", true);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        Utente utente = UDAO.findByEmail("emanuele@gmail.com");

        Assert.assertEquals(utente.getId(), UDAO.getLastInsertId());

        UDAO.removeByEmail("emanuele@gmail.com");
    }

    @Test
    public void addTest() throws InvalidFormatException {
        UtenteBusiness.getInstance().signUpCliente("emanuele@gmail.com" ,"Emanuele", "Romano", "1234567890", "02-02-1975", "Pilota", "Via Verdi 22, Milano", true);

        Utente utente = UDAO.findByEmail("emanuele@gmail.com");

        Assert.assertEquals(UDAO.getLastInsertId(), utente.getId());
        Assert.assertEquals("Emanuele", utente.getNome());
        Assert.assertEquals("Romano", utente.getCognome());
        Assert.assertEquals("emanuele@gmail.com", utente.getEmail());
        Assert.assertEquals(Utente.RUOLO.CLIENTE, utente.getRuolo());
        Assert.assertEquals("1975-02-02", utente.getDataNascita().toString());
        Assert.assertEquals("1234567890", utente.getTelefono());
        Assert.assertTrue(utente.isCambiaPassword());
        Assert.assertFalse(utente.isDisabilitato());
        Assert.assertEquals("Via Verdi 22, Milano", utente.getResidenza());
        Assert.assertEquals("Pilota", utente.getProfessione());


        UDAO.removeByEmail("emanuele@gmail.com");
    }

    @Test
    public void removeByEmailTest() {
        ArrayList<Ordine> ordini = new ArrayList<>();
        ArrayList<IInterazioneUtente> interazioni = new ArrayList<>();
        UDAO.addUtente(new Utente("Emanuele" ,"Romano", "emanuele@gmail.com", "1234", Utente.RUOLO.CLIENTE, Date.valueOf("2003-02-02") ,"1234567890", Date.valueOf("2023-12-12"), false, false, "Via Verdi 22, Lecce", "studente", ordini, interazioni, SHA512Hashing.generateSalt()));

        boolean result = UDAO.removeByEmail("emanuele@gmail.com");

        Assert.assertTrue(result);
        Assert.assertNull(UDAO.findByEmail("emanuele@gmail.com"));

    }

    @Test
    public void findByIdTest() {
        Utente utente = UDAO.findById(UDAO.getLastInsertId());
        Assert.assertNotNull(utente);
        Assert.assertEquals(UDAO.getLastInsertId(), utente.getId());
        Assert.assertEquals("Vittorio", utente.getNome());
        Assert.assertEquals("Veneto", utente.getCognome());
        Assert.assertEquals("vittorio@gmail.com", utente.getEmail());
        Assert.assertEquals(Utente.RUOLO.CUCINA, utente.getRuolo());
        Assert.assertEquals("1975-02-02", utente.getDataNascita().toString());
        Assert.assertEquals("1234567890", utente.getTelefono());
        Assert.assertTrue(utente.isCambiaPassword());
        Assert.assertFalse(utente.isDisabilitato());
    }

    @Test
    public void findByEmailTest() {
        IUtenteDAO IDAO = UtenteDAO.getInstance();
        Utente utente = IDAO.findByEmail("valentino@gmail.com");
        Assert.assertNotNull(utente);
        Assert.assertEquals(utente.getEmail(), "valentino@gmail.com");
        Assert.assertEquals("Valentino", utente.getNome());
        Assert.assertEquals("Rossi", utente.getCognome());
        Assert.assertEquals(Utente.RUOLO.CLIENTE, utente.getRuolo());
        Assert.assertEquals("1975-02-02", utente.getDataNascita().toString());
        Assert.assertEquals("1234567890", utente.getTelefono());
        Assert.assertTrue(utente.isCambiaPassword());
        Assert.assertFalse(utente.isDisabilitato());


    }

    @Test
    public void findAllTest() {
        ArrayList<Utente> utenti = UDAO.findAll();
        Assert.assertNotNull(utenti);
        Assert.assertEquals(3, utenti.size());
        Assert.assertEquals("mario@gmail.com", utenti.get(0).getEmail());
        Assert.assertEquals("valentino@gmail.com", utenti.get(1).getEmail());
        Assert.assertEquals("vittorio@gmail.com", utenti.get(2).getEmail());
    }

    @Test
    public void findAllClientiTest() {
        ArrayList<Utente> utenti = UDAO.findAllClienti();
        Assert.assertNotNull(utenti);
        Assert.assertEquals(1, utenti.size());
        Assert.assertEquals("valentino@gmail.com", utenti.get(0).getEmail());

    }


    @Test
    public void findAllCucinaTest() {
        ArrayList<Utente> utenti = UDAO.findAllCucina();
        Assert.assertNotNull(utenti);
        Assert.assertEquals(1, utenti.size());
        Assert.assertEquals("vittorio@gmail.com", utenti.get(0).getEmail());

    }

    @Test
    public void findAllAmministratoriTest() {
        ArrayList<Utente> utenti = UDAO.findAllAmministratori();
        Assert.assertNotNull(utenti);
        Assert.assertEquals(1, utenti.size());
        Assert.assertEquals("mario@gmail.com", utenti.get(0).getEmail());

    }

    @Test
    public void saltExistsTest() {
        Utente utente = UDAO.findByEmail("vittorio@gmail.com");
        Assert.assertTrue(UDAO.saltExists(utente.getSaltHex()));
        Assert.assertFalse(UDAO.saltExists("saltCheNonEsiste"));
    }

    @Test
    public void checkIfUtenteHasPurchasedArticoloTest() {
        TipologiaProdottoDAO TPDAO = TipologiaProdottoDAO.getInstance();
        ArticoloDAO ADAO = ArticoloDAO.getInstance();
        OrdineDAO ODAO = OrdineDAO.getInstance();

        // Istanzio un utente
        Utente clienteTest = UDAO.findByEmail("valentino@gmail.com");

        // Creare tipologia prodotto
        TPDAO.addTipologia("Tipologia1");
        TipologiaProdotto tipProd = TPDAO.findTipologiaByName("Tipologia1");

        // Creare un prodotto
        Prodotto prodottoTest = new Prodotto("ProdottoTest", "[...]", 4f, 80, tipProd, null, null, null);
        ADAO.addArticolo(prodottoTest);
        prodottoTest = (Prodotto) ADAO.findById(ADAO.getLastInsertId());

        // Creare un carrello con quel prodotto
        HashMap<Integer, Integer> prodottiNelCarrello = new HashMap<>();
        prodottiNelCarrello.put(prodottoTest.getId(), 1);
        Carrello carrelloTest = new Carrello(prodottiNelCarrello, clienteTest);

        // Creare un ordine
        ODAO.createOrdine(carrelloTest);
        Ordine ordineTest = ODAO.findById(ODAO.getLastInsertId());

        // Assert
        Assert.assertTrue(UDAO.checkIfUtenteHasPurchasedArticolo(clienteTest, prodottoTest));

        // Eliminare
        ODAO.remove(ordineTest);
        ADAO.removeArticolo(prodottoTest);
        TPDAO.removeTipologia(tipProd.getId());
    }

    @Test
    public void checkIfUtenteHasCommentedArticoloTest() {
        TipologiaProdottoDAO TPDAO = TipologiaProdottoDAO.getInstance();
        ArticoloDAO ADAO = ArticoloDAO.getInstance();
        InterazioneUtenteDAO IUDAO = InterazioneUtenteDAO.getInstance();

        // Istanzio un utente
        Utente clienteTest = UDAO.findByEmail("valentino@gmail.com");

        // Creare tipologia prodotto
        TPDAO.addTipologia("Tipologia1");
        TipologiaProdotto tipProd = TPDAO.findTipologiaByName("Tipologia1");

        // Creare un prodotto
        Prodotto prodottoTest = new Prodotto("ProdottoTest", "[...]", 4f, 80, tipProd, null, null, null);
        ADAO.addArticolo(prodottoTest);
        prodottoTest = (Prodotto) ADAO.findById(ADAO.getLastInsertId());

        // Creare un commento per prodottoTest
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestampLocalDateTime = Timestamp.valueOf(localDateTime);
        CommentoCliente commentoTest = new CommentoCliente(clienteTest, prodottoTest.getId(), "prova", IInterazioneUtente.INDICE_GRADIMENTO.CINQUE, timestampLocalDateTime);
        IUDAO.addCommento(commentoTest);
        commentoTest = IUDAO.findCommentoById(IUDAO.getLastCommentoInsertId());

        // Assert
        Assert.assertTrue(UDAO.checkIfUtenteHasCommentedArticolo(clienteTest, prodottoTest));

        // Eliminare
        IUDAO.removeCommento(commentoTest.getId());
        ADAO.removeArticolo(prodottoTest);
        TPDAO.removeTipologia(tipProd.getId());
    }

    @Test
    public void isFirstAccessTest() {
        Assert.assertTrue(UDAO.isFirstAccess("valentino@gmail.com"));
        Assert.assertTrue(UDAO.setCambiaPassword("valentino@gmail.com", false));
        Assert.assertFalse(UDAO.isFirstAccess("valentino@gmail.com"));
    }

    @Test
    public void setCambiaPasswordTest() {
        Assert.assertTrue(UDAO.setCambiaPassword("valentino@gmail.com", false));
        Assert.assertFalse(UDAO.isFirstAccess("valentino@gmail.com"));
        Assert.assertTrue(UDAO.setCambiaPassword("valentino@gmail.com", true));
        Assert.assertTrue(UDAO.isFirstAccess("valentino@gmail.com"));
    }

    @Test
    public void changePasswordTest() {
        Assert.assertTrue(UDAO.changePassword("valentino@gmail.com", "hashDiProva"));
        Assert.assertEquals("hashDiProva", UDAO.findByEmail("valentino@gmail.com").getHashedPassword());

    }

    @Test
    public void setDisabilitatoUtenteTest() {
        Utente utente = UDAO.findByEmail("valentino@gmail.com");
        UDAO.setDisabilitatoUtente(utente.getId(), true);


        Assert.assertFalse(UDAO.isEnable("valentino@gmail.com"));
        UDAO.setDisabilitatoUtente(utente.getId(), false);
        Assert.assertTrue(UDAO.isEnable("valentino@gmail.com"));

    }

    @Test
    public void userExistsTest() {
        boolean exists = UDAO.userExists("valentino@gmail.com");
        Assert.assertTrue(exists);

        exists = UDAO.userExists("emailacaso@gmail.com");
        Assert.assertFalse(exists);

        Utente utente = UDAO.findByEmail("valentino@gmail.com");
        Assert.assertNotNull(utente);
        Assert.assertEquals(UDAO.findByEmail("valentino@gmail.com").getId(), utente.getId());
    }

    @Test
    public void passwordOkTest() {
        ArrayList<Ordine> ordini = new ArrayList<>();
        ArrayList<IInterazioneUtente> interazioni = new ArrayList<>();

        String saltHex = SHA512Hashing.generateSalt();
        String password = "123456789";

        StrategyFactory strategyFactory = new StrategyFactory();

        PasswordHashingContext passwordHashingContext = new PasswordHashingContext(strategyFactory.getStrategy("SHA512"));
        String hashedPassword = passwordHashingContext.executeStrategy(password, saltHex);

        UDAO.addUtente(new Utente("Emanuele" ,"Romano", "emanuele@gmail.com", hashedPassword, Utente.RUOLO.CLIENTE, Date.valueOf("2003-02-02") ,"1234567890", Date.valueOf("2023-12-12"), false, false, "Via Verdi 22, Lecce", "studente", ordini, interazioni, saltHex));

        boolean passwordOk = UDAO.passwordOk("emanuele@gmail.com", hashedPassword);
        Assert.assertTrue(passwordOk);

        passwordOk = UDAO.passwordOk("valentino@gmail.com", "passwordErrata");
        Assert.assertFalse(passwordOk);

        Utente utente = UDAO.findByEmail("emanuele@gmail.com");
        Assert.assertEquals(hashedPassword, utente.getHashedPassword());

        UDAO.removeByEmail("emanuele@gmail.com");
    }

    @Test
    public void isClienteTest() {
        Utente cliente = UDAO.findByEmail("valentino@gmail.com");
        boolean isCliente = UDAO.isCliente(cliente.getId());

        Assert.assertTrue(isCliente);
        Assert.assertEquals(cliente.getRuolo(), Utente.RUOLO.CLIENTE);

        cliente = UDAO.findByEmail("mario@gmail.com");
        isCliente = UDAO.isCliente(cliente.getId());

        Assert.assertFalse(isCliente);
        Assert.assertNotSame(Utente.RUOLO.CLIENTE, cliente.getRuolo());

    }

    @Test
    public void isAmministratoreTest() {
        Utente amministratore = UDAO.findByEmail("valentino@gmail.com");
        boolean isAmministratore = UDAO.isAmministratore(amministratore.getId());
        Assert.assertFalse(isAmministratore);
        Assert.assertNotSame (Utente.RUOLO.AMMINISTRATORE, amministratore.getRuolo());

        amministratore = UDAO.findByEmail("mario@gmail.com");
        isAmministratore = UDAO.isAmministratore(amministratore.getId());
        Assert.assertTrue(isAmministratore);
        Assert.assertEquals(Utente.RUOLO.AMMINISTRATORE, amministratore.getRuolo());

    }

    @Test
    public void isCucinaTest() {
        Utente cucina = UDAO.findByEmail("valentino@gmail.com");

        boolean isCucina = UDAO.isCucina(cucina.getId());
        Assert.assertFalse(isCucina);
        Assert.assertNotSame(Utente.RUOLO.CUCINA, cucina.getRuolo());

        cucina = UDAO.findByEmail("vittorio@gmail.com");
        isCucina = UDAO.isCucina(cucina.getId());
        Assert.assertTrue(isCucina);
        Assert.assertEquals(Utente.RUOLO.CUCINA, cucina.getRuolo());
    }

    @Test
    public void isEnableTest() {
        Utente utente = UDAO.findByEmail("valentino@gmail.com");
        boolean isEnable = UDAO.isEnable("valentino@gmail.com");

        Assert.assertTrue(isEnable);
        Assert.assertFalse(utente.isDisabilitato());
    }

    @Test
    public void caricaClienteTest() {
        Utente u = UDAO.caricaCliente("valentino@gmail.com");
        Assert.assertNotNull(u);
        Assert.assertEquals("valentino@gmail.com", u.getEmail());
        Assert.assertEquals("Via Verdi 22, Milano", u.getResidenza());  //attributo esclusivo di cliente
    }

    @Test
    public void caricaAmministratoreTest() {
        Utente u = UDAO.caricaAmministratore("mario@gmail.com");
        Assert.assertNotNull(u);
        Assert.assertEquals("mario@gmail.com", u.getEmail());
    }

    @Test
    public void caricaCucinaTest() {
        Utente u = UDAO.caricaCucina("vittorio@gmail.com");
        Assert.assertNotNull(u);
        Assert.assertEquals("vittorio@gmail.com", u.getEmail());
    }

    @Test
    public void updateTest(){
        ArrayList<Ordine> ordini = new ArrayList<>();
        ArrayList<IInterazioneUtente> interazioni = new ArrayList<>();

        Utente utente = new Utente("Valentino" ,"Vaglio", "valentino@gmail.com", "1234", Utente.RUOLO.CLIENTE, Date.valueOf("1940-12-12") ,"1234567890", Date.valueOf("2022-12-12") , false, false, "Via Verdi 22, Milano", "Pilota", ordini, interazioni, SHA512Hashing.generateSalt());
        utente.setId(UDAO.findByEmail("valentino@gmail.com").getId());

        boolean result = UDAO.update(utente);
        Utente modUtente = UDAO.findByEmail("valentino@gmail.com");

        Assert.assertTrue(result);
        Assert.assertEquals("Vaglio", modUtente.getCognome());

    }
}


