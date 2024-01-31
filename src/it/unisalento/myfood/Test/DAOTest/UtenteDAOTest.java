package it.unisalento.myfood.Test.DAOTest;

import it.unisalento.myfood.Business.InvalidFormatException;
import it.unisalento.myfood.Business.Security.AbstractFactory.StrategyFactory;
import it.unisalento.myfood.Business.Security.Strategy.PasswordHashingContext;
import it.unisalento.myfood.Business.Security.Strategy.SHA512Hashing;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.DAO.IUtenteDAO;
import it.unisalento.myfood.DAO.UtenteDAO;
import it.unisalento.myfood.model.Composite.IInterazioneUtente;
import it.unisalento.myfood.model.Ordine;
import it.unisalento.myfood.model.Utente;
import org.junit.*;


import java.sql.Date;
import java.util.ArrayList;

 public class UtenteDAOTest {
     private final IUtenteDAO IDAO = UtenteDAO.getInstance();
    @Before
    public void setUp(){
        UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();

        try {
            utenteBusiness.signUpCliente("valentino@gmail.com" ,"Valentino", "Rossi", "1234567890", "1975-2-2", "Pilota", "Via Verdi 22, Milano");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        try {
            utenteBusiness.signUpAmministratore("mario@gmail.com" ,"Mario", "Bianchi", "1234567890", "1975-2-2", "Pilota");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        try {
            utenteBusiness.signUpCucina("vittorio@gmail.com" ,"Vittorio", "Veneto", "1234567890", "1975-2-2", "Pilota");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown(){
        IDAO.removeByEmail("mario@gmail.com");
        IDAO.removeByEmail("valentino@gmail.com");
        IDAO.removeByEmail("vittorio@gmail.com");
    }

    @Test
    public void getLastInsertIdTest() {
        UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();
        try {
            utenteBusiness.signUpCliente("emanuele@gmail.com" ,"Emanuele", "Romano", "1234567890", "1975-2-2", "Pilota", "Via Verdi 22, Milano");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        Utente utente = IDAO.findByEmail("emanuele@gmail.com");

        Assert.assertEquals(utente.getId(), IDAO.getLastInsertId());

        IDAO.removeByEmail("emanuele@gmail.com");
    }

    @Test
    public void addTest() {
        ArrayList<Ordine> ordini = new ArrayList<>();
        ArrayList<IInterazioneUtente> interazioni = new ArrayList<>();
        boolean result = IDAO.addUtente(new Utente("Emanuele" ,"Romano", "emanuele@gmail.com", "1234", Utente.RUOLO.CLIENTE, Date.valueOf("2003-02-02") ,"1234567890", Date.valueOf("2023-12-12"), false, false, "Via Verdi 22, Lecce", "studente", ordini, interazioni, SHA512Hashing.generateSalt()));

        Utente utente = IDAO.findByEmail("emanuele@gmail.com");
        System.out.println(utente);

        Assert.assertTrue(result);
        Assert.assertEquals(IDAO.getLastInsertId(), utente.getId());
        Assert.assertEquals("Emanuele", utente.getNome());
        Assert.assertEquals("Romano", utente.getCognome());
        Assert.assertEquals("emanuele@gmail.com", utente.getEmail());
        //TODO: Assert.assertEquals("1234", utente.getHashedPassword());
        Assert.assertEquals(Utente.RUOLO.CLIENTE, utente.getRuolo());
        Assert.assertEquals("2003-02-02", utente.getDataNascita().toString());
        Assert.assertEquals("1234567890", utente.getTelefono());
        Assert.assertFalse(utente.isCambiaPassword());
        Assert.assertFalse(utente.isDisabilitato());
        Assert.assertEquals("Via Verdi 22, Lecce", utente.getResidenza());
        Assert.assertEquals("studente", utente.getProfessione());
        //TODO: ordini, iterazioni, salt


        IDAO.removeByEmail("emanuele@gmail.com");
    }

    @Test
    public void removeByEmailTest() {
        ArrayList<Ordine> ordini = new ArrayList<>();
        ArrayList<IInterazioneUtente> interazioni = new ArrayList<>();
        IDAO.addUtente(new Utente("Emanuele" ,"Romano", "emanuele@gmail.com", "1234", Utente.RUOLO.CLIENTE, Date.valueOf("2003-02-02") ,"1234567890", Date.valueOf("2023-12-12"), false, false, "Via Verdi 22, Lecce", "studente", ordini, interazioni, SHA512Hashing.generateSalt()));

        boolean result = IDAO.removeByEmail("emanuele@gmail.com");

        Assert.assertTrue(result);
        Assert.assertNull(IDAO.findByEmail("emanuele@gmail.com"));

        for(Utente u : IDAO.findAll()){
            System.out.println(u);
        }
    }

    @Test
    public void findByIdTest() {
        Utente utente = IDAO.findById(IDAO.getLastInsertId());
        Assert.assertNotNull(utente);
        Assert.assertEquals(IDAO.getLastInsertId(), utente.getId());
        Assert.assertEquals("Vittorio", utente.getNome());
        Assert.assertEquals("Veneto", utente.getCognome());
        Assert.assertEquals("vittorio@gmail.com", utente.getEmail());
       //TODO: Assert.assertEquals("1234", utente.getHashedPassword());
        Assert.assertEquals(Utente.RUOLO.CUCINA, utente.getRuolo());
        Assert.assertEquals("1975-02-02", utente.getDataNascita().toString());
        Assert.assertEquals("1234567890", utente.getTelefono());
        Assert.assertTrue(utente.isCambiaPassword());
        Assert.assertFalse(utente.isDisabilitato());
        System.out.println(utente);
    }

    @Test
    public void findByEmailTest() {
        IUtenteDAO IDAO = UtenteDAO.getInstance();
        Utente utente = IDAO.findByEmail("valentino@gmail.com");
        Assert.assertNotNull(utente);
        Assert.assertEquals(utente.getEmail(), "valentino@gmail.com");
        Assert.assertEquals("Valentino", utente.getNome());
        Assert.assertEquals("Rossi", utente.getCognome());
        //TODO: Assert.assertEquals("1234", utente.getHashedPassword());
        Assert.assertEquals(Utente.RUOLO.CLIENTE, utente.getRuolo());
        Assert.assertEquals("1975-02-02", utente.getDataNascita().toString());
        Assert.assertEquals("1234567890", utente.getTelefono());
        Assert.assertTrue(utente.isCambiaPassword());
        Assert.assertFalse(utente.isDisabilitato());

        System.out.println(utente);
    }

    @Test
    public void getAllUtentiTest() {
        ArrayList<Utente> utenti = IDAO.findAll();
        Assert.assertNotNull(utenti);
        Assert.assertEquals(3, utenti.size());

        for(Utente u : utenti){
            Assert.assertNotNull(u.getId());
            System.out.println(u);
        }
    }

    @Test
    public void userExistsTest() {
        boolean exists = IDAO.userExists("valentino@gmail.com");
        Assert.assertTrue(exists);

        exists = IDAO.userExists("emailacaso@gmail.com");
        Assert.assertFalse(exists);

        Utente utente = IDAO.findByEmail("valentino@gmail.com");
        Assert.assertNotNull(utente);
        Assert.assertEquals(IDAO.findByEmail("valentino@gmail.com").getId(), utente.getId());
    }

    @Test
    public void passwordOkTest() {
        ArrayList<Ordine> ordini = new ArrayList<>();
        ArrayList<IInterazioneUtente> interazioni = new ArrayList<>();

        String saltHex = SHA512Hashing.generateSalt();
        String password = "123456789";

        StrategyFactory strategyFactory = new StrategyFactory();

        //TODO: spostare nel business: il dao usa il business
        PasswordHashingContext passwordHashingContext = new PasswordHashingContext(strategyFactory.getStrategy("SHA512"));
        String hashedPassword = passwordHashingContext.executeStrategy(password, saltHex);

        IDAO.addUtente(new Utente("Emanuele" ,"Romano", "emanuele@gmail.com", hashedPassword, Utente.RUOLO.CLIENTE, Date.valueOf("2003-02-02") ,"1234567890", Date.valueOf("2023-12-12"), false, false, "Via Verdi 22, Lecce", "studente", ordini, interazioni, saltHex));

        boolean passwordOk = IDAO.passwordOk("emanuele@gmail.com", password);
        Assert.assertTrue(passwordOk);

        passwordOk = IDAO.passwordOk("valentino@gmail.com", "passwordErrata");
        Assert.assertFalse(passwordOk);

        Utente utente = IDAO.findByEmail("emanuele@gmail.com");
        Assert.assertEquals(hashedPassword, utente.getHashedPassword());

        IDAO.removeByEmail("emanuele@gmail.com");
    }

    @Test
    public void isClienteTest() {
        Utente cliente = IDAO.findByEmail("valentino@gmail.com");
        boolean isCliente = IDAO.isCliente(cliente.getId());

        Assert.assertTrue(isCliente);
        Assert.assertEquals(cliente.getRuolo(), Utente.RUOLO.CLIENTE);

        cliente = IDAO.findByEmail("mario@gmail.com");
        isCliente = IDAO.isCliente(cliente.getId());

        Assert.assertFalse(isCliente);
        Assert.assertNotSame(Utente.RUOLO.CLIENTE, cliente.getRuolo());

        System.out.println(IDAO.findByEmail("mario@gmail.com"));
    }

    @Test
    public void isAmministratoreTest() {
        Utente amministratore = IDAO.findByEmail("valentino@gmail.com");
        boolean isAmministratore = IDAO.isAmministratore(amministratore.getId());
        Assert.assertFalse(isAmministratore);
        Assert.assertNotSame (Utente.RUOLO.AMMINISTRATORE, amministratore.getRuolo());

        amministratore = IDAO.findByEmail("mario@gmail.com");
        isAmministratore = IDAO.isAmministratore(amministratore.getId());
        Assert.assertTrue(isAmministratore);
        Assert.assertEquals(Utente.RUOLO.AMMINISTRATORE, amministratore.getRuolo());

    }

    @Test
    public void isCucinaTest() {
        Utente cucina = IDAO.findByEmail("valentino@gmail.com");

        boolean isCucina = IDAO.isCucina(cucina.getId());
        Assert.assertFalse(isCucina);
        Assert.assertNotSame(Utente.RUOLO.CUCINA, cucina.getRuolo());

        cucina = IDAO.findByEmail("vittorio@gmail.com");
        isCucina = IDAO.isCucina(cucina.getId());
        Assert.assertTrue(isCucina);
        Assert.assertEquals(Utente.RUOLO.CUCINA, cucina.getRuolo());
    }

    @Test
    public void isEnableTest() {
        Utente utente = IDAO.findByEmail("valentino@gmail.com");
        boolean isEnable = IDAO.isEnable("valentino@gmail.com");

        Assert.assertTrue(isEnable);
        Assert.assertFalse(utente.isDisabilitato());
    }

    @Test
    public void caricaClienteTest() {
        Utente u = IDAO.caricaCliente("valentino@gmail.com");
        Assert.assertNotNull(u);
        Assert.assertEquals("valentino@gmail.com", u.getEmail());
        Assert.assertEquals("Via Verdi 22, Milano", u.getResidenza());  //attributo esclusivo di cliente
    }

    @Test
    public void caricaAmministratoreTest() {
        Utente u = IDAO.caricaAmministratore("mario@gmail.com");
        Assert.assertNotNull(u);
        Assert.assertEquals("mario@gmail.com", u.getEmail());
        System.out.println(u);
    }

    @Test
    public void caricaCucinaTest() {
        Utente u = IDAO.caricaCucina("vittorio@gmail.com");
        Assert.assertNotNull(u);
        Assert.assertEquals("vittorio@gmail.com", u.getEmail());
        System.out.println(u);
    }

    @Test
    public void updateTest(){
        ArrayList<Ordine> ordini = new ArrayList<>();
        ArrayList<IInterazioneUtente> interazioni = new ArrayList<>();

        Utente utente = new Utente("Valentino" ,"Vaglio", "valentino@gmail.com", "1234", Utente.RUOLO.CLIENTE, Date.valueOf("1940-12-12") ,"1234567890", Date.valueOf("2022-12-12") , false, false, "Via Verdi 22, Milano", "Pilota", ordini, interazioni, SHA512Hashing.generateSalt());
        utente.setId(IDAO.findByEmail("valentino@gmail.com").getId());

        boolean result = IDAO.update(utente);
        Utente modUtente = IDAO.findByEmail("valentino@gmail.com");

        Assert.assertTrue(result);
        Assert.assertEquals("Vaglio", modUtente.getCognome());

        System.out.println(modUtente);
    }
}


