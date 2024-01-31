package it.unisalento.myfood.Test.DAOTest;

import it.unisalento.myfood.Business.Security.Strategy.SHA512Hashing;
import it.unisalento.myfood.DAO.*;
import it.unisalento.myfood.model.Composite.CommentoCliente;
import it.unisalento.myfood.model.Composite.IInterazioneUtente;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.Composite.RispostaAmministratore;
import it.unisalento.myfood.model.TipologiaProdotto;
import it.unisalento.myfood.model.Utente;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class InterazioneUtenteDAOTest {

    private IUtenteDAO IDAO = UtenteDAO.getInstance();
    private IArticoloDAO ADAO = ArticoloDAO.getInstance();
    private IInterazioneUtenteDAO INTDAO = InterazioneUtenteDAO.getInstance();
    private ITipologiaProdottoDAO TDAO = TipologiaProdottoDAO.getInstance();
    private TipologiaProdotto panTip;
    private TipologiaProdotto bevTip;
    private Utente cliente;
    private Utente amministratore;
    private Prodotto articolo;
    private Prodotto cocaCola;
    private CommentoCliente commentoCliente1;
    private CommentoCliente commentoCliente2;
    private CommentoCliente commentoCliente3;
    private RispostaAmministratore rispostaAmministratore;




    @Before
    public void setUp() {

        IDAO.addUtente(new Utente("Valentino" ,"Rossi", "valentino@gmail.com", "1234", Utente.RUOLO.CLIENTE, Date.valueOf("1975-02-02") ,"1234567890", Date.valueOf("2023-12-12"), false, false, "Via Verdi 22, Milano", "Pilota", null, null, SHA512Hashing.generateSalt()));
        cliente = IDAO.findByEmail("valentino@gmail.com");
        IDAO.addUtente(new Utente("Mario" ,"Bianchi", "mario@gmail.com", "1234", Utente.RUOLO.AMMINISTRATORE, Date.valueOf("1975-02-02") ,"1234567890", Date.valueOf("2023-12-12") , false, false, "Via Verdi 22, Milano", "Pilota", null, null, SHA512Hashing.generateSalt()));
        amministratore = IDAO.findByEmail("mario@gmail.com");
        TDAO.addTipologia("Panini");
        panTip = TDAO.findTipologiaByName("Panini");
        TDAO.addTipologia("Bevande");
        bevTip = TDAO.findTipologiaByName("Bevande");

        articolo = new Prodotto("Bacon Burger", "bacon croccante [...]", 4.60f, 80, panTip, null, null, null );
        ADAO.addArticolo(articolo);
        articolo.setId(ADAO.getLastInsertId());


        cocaCola = new Prodotto("CocaCola", "bevanda a base di caffeina", 2.5f, 150, bevTip, null, null, null);
        ADAO.addArticolo(cocaCola);
        cocaCola.setId(ADAO.getLastInsertId());

        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestampLocalDateTime = Timestamp.valueOf(localDateTime);
        commentoCliente1 = new CommentoCliente(cliente, articolo.getId(), "Ottimo panino!!", IInterazioneUtente.INDICE_GRADIMENTO.CINQUE, timestampLocalDateTime);
        INTDAO.addCommento(commentoCliente1);
        commentoCliente1.setIdCommento(INTDAO.getLastCommentoInsertId());

        commentoCliente2 = new CommentoCliente(cliente, cocaCola.getId(), "Più ghiaccio che bevanda", IInterazioneUtente.INDICE_GRADIMENTO.DUE, Timestamp.valueOf("2024-01-08 09:01:15"));
        INTDAO.addCommento(commentoCliente2);
        commentoCliente2.setIdCommento(INTDAO.getLastCommentoInsertId());

        rispostaAmministratore = new RispostaAmministratore(commentoCliente2.getId(), "Che pretendi? è un fast food", amministratore, Timestamp.valueOf("2024-01-08 14:01:15"));

        INTDAO.addRisposta(rispostaAmministratore);

        rispostaAmministratore.setIdRiposta(INTDAO.getLastRispostaInsertId());

        commentoCliente3 = new CommentoCliente(cliente, articolo.getId(), "Il mio preferito!", IInterazioneUtente.INDICE_GRADIMENTO.CINQUE, timestampLocalDateTime);

        INTDAO.addCommento(commentoCliente3);

        commentoCliente3.setIdCommento(INTDAO.getLastCommentoInsertId());

    }

    @After
    public void tearDown(){
        INTDAO.removeRispostaByIdCommento(commentoCliente2.getId());
        INTDAO.removeCommento(commentoCliente1.getId());
        INTDAO.removeCommento(commentoCliente2.getId());

        ADAO.removeArticolo(cocaCola);
        ADAO.removeArticolo(articolo);
        TDAO.removeTipologia(panTip.getId());
        TDAO.removeTipologia(bevTip.getId());
        IDAO.removeByEmail("mario@gmail.com");
        IDAO.removeByEmail("valentino@gmail.com");
    }

    @Test
    public void getLastInsertIdTest() {
        // TODO
    }

    @Test
    public void findCommentoByIdTest(){
        CommentoCliente commentoCliente = INTDAO.findCommentoById(commentoCliente2.getId());
        Assert.assertNotNull(commentoCliente);
        Assert.assertEquals(cliente.getId(), commentoCliente.getUtente().getId());
        Assert.assertEquals("Più ghiaccio che bevanda", commentoCliente.getTesto());
        Assert.assertEquals(IInterazioneUtente.INDICE_GRADIMENTO.DUE, commentoCliente.getIndiceDiGradimento());
        Assert.assertEquals(Timestamp.valueOf("2024-01-08 09:01:15"), commentoCliente.getDataEOra());
        System.out.println(commentoCliente);
    }

    @Test
    public void findLastCommentiTest() {
        ArrayList<CommentoCliente> commenti = INTDAO.findLastCommenti();
        Assert.assertNotNull(commenti);
        Assert.assertFalse(commenti.isEmpty());
        Assert.assertNotNull(commenti.get(0).getId());
        Assert.assertTrue(commenti.get(0).getDataEOra().compareTo(commenti.get(2).getDataEOra()) > 0);//testa il fatto che i commenti siano ordinati dal più recente
        Assert.assertTrue(commenti.get(1).getDataEOra().compareTo(commenti.get(2).getDataEOra()) > 0);
        Assert.assertEquals(commentoCliente1.getId(), commenti.get(0).getId());
        Assert.assertEquals(commentoCliente3.getId(), commenti.get(1).getId());
        Assert.assertEquals(commentoCliente2.getId(), commenti.get(2).getId());
        for(CommentoCliente c : commenti){
            System.out.println(c);
        }
    }

    @Test
    public void findCommentiNotAnsweredTest() {
        ArrayList<CommentoCliente> commenti = INTDAO.findCommentiNotAnswered();
        Assert.assertNotNull(commenti);
        Assert.assertFalse(commenti.isEmpty());
        Assert.assertEquals(commenti.get(0).getId(), commentoCliente1.getId());


        for(CommentoCliente c : commenti){
            System.out.println(c);
        }
    }


    @Test
    public void findCommentiByDateTest() {

        ArrayList<CommentoCliente> commenti = INTDAO.findCommentiByDate(Timestamp.valueOf("2024-01-08 09:01:15"));
        Assert.assertNotNull(commenti);
        Assert.assertFalse(commenti.isEmpty());
        Assert.assertEquals(commenti.get(0).getId(), commentoCliente2.getId());

        for(CommentoCliente c : commenti){
            System.out.println(c);
        }
    }


    @Test
    public void findCommentiByArticoloTest() {
        ArrayList<CommentoCliente> commenti = INTDAO.findCommentiByArticolo(panTip.getId());
        Assert.assertNotNull(commenti);
        Assert.assertEquals(2, commenti.size());
        Assert.assertEquals(commentoCliente1.getId(), commenti.get(0).getId());
        Assert.assertEquals(commentoCliente3.getId(), commenti.get(1).getId());
        for(CommentoCliente c : commenti){
            System.out.println(c);
        }

    }


    @Test
    public void caricaInterazioniTest() {
        ArrayList<IInterazioneUtente> interazioniCliente = INTDAO.caricaInterazioni(cliente);
        Assert.assertNotNull(interazioniCliente);
        Assert.assertEquals(3, interazioniCliente.size());
        Assert.assertEquals(commentoCliente1.getId(), interazioniCliente.get(0).getId());
        Assert.assertEquals(commentoCliente2.getId(), interazioniCliente.get(1).getId());
        Assert.assertEquals(commentoCliente3.getId(), interazioniCliente.get(2).getId());

        for(IInterazioneUtente c : interazioniCliente){
            System.out.println(c);
        }

        ArrayList<IInterazioneUtente> interazioniAmministratore = INTDAO.caricaInterazioni(amministratore);
        Assert.assertNotNull(interazioniAmministratore);
        Assert.assertEquals(1, interazioniAmministratore.size());
        Assert.assertEquals(rispostaAmministratore.getId(), interazioniAmministratore.get(0).getId());

        for(IInterazioneUtente c : interazioniAmministratore){
            System.out.println(c);
        }


    }
    @Test
    public void addCommentoTest() {

        boolean result = INTDAO.addCommento(new CommentoCliente(cliente, articolo.getId(), "Ottimo rapporto qualità prezzo!", IInterazioneUtente.INDICE_GRADIMENTO.QUATTRO, Timestamp.valueOf("2024-01-08 14:01:15") ));
        CommentoCliente commentoCliente = INTDAO.findCommentoById(INTDAO.getLastCommentoInsertId());
        Assert.assertTrue(result);
        Assert.assertNotNull(commentoCliente);
        Assert.assertEquals(commentoCliente.getUtente().getId(), cliente.getId());
        Assert.assertEquals("Ottimo rapporto qualità prezzo!", commentoCliente.getTesto());
        Assert.assertEquals(IInterazioneUtente.INDICE_GRADIMENTO.QUATTRO, commentoCliente.getIndiceDiGradimento());
        Assert.assertEquals(Timestamp.valueOf("2024-01-08 14:01:15"), commentoCliente.getDataEOra());
        System.out.println(commentoCliente);
        INTDAO.removeCommento(commentoCliente.getId());

    }

    @Test
    public void addRispostaTest() {

        boolean result = INTDAO.addRisposta(new RispostaAmministratore(commentoCliente1.getId(), "Ti ringraziamo per la tua recensione!", amministratore, Timestamp.valueOf("2024-01-08 14:01:15")));

        ArrayList<IInterazioneUtente> risposte = INTDAO.caricaInterazioni(amministratore);

        IInterazioneUtente rispostaAmministratore2 = risposte.get(1);
        Assert.assertTrue(result);
        Assert.assertNotNull(rispostaAmministratore2);
        Assert.assertEquals(rispostaAmministratore2.getUtente().getId(), amministratore.getId());
        Assert.assertEquals("Ti ringraziamo per la tua recensione!", rispostaAmministratore2.getTesto());
        Assert.assertEquals(Timestamp.valueOf("2024-01-08 14:01:15"), rispostaAmministratore2.getDataEOra());
        System.out.println(rispostaAmministratore2);
        INTDAO.removeRispostaByIdCommento(commentoCliente1.getId());

    }


    @Test
    public void removeCommmentoTest() {
        INTDAO.addCommento(new CommentoCliente(cliente, articolo.getId(), "Ottimo rapporto qualità prezzo!", IInterazioneUtente.INDICE_GRADIMENTO.QUATTRO, Timestamp.valueOf("2024-01-08 14:01:15") ));
        CommentoCliente commentoCliente = INTDAO.findCommentoById(INTDAO.getLastCommentoInsertId());

        boolean result = INTDAO.removeCommento(commentoCliente.getId());

        Assert.assertTrue(result);
        Assert.assertNull(INTDAO.findCommentoById(commentoCliente.getId()).getId());
    }


    @Test
    public void removeRispostaByIdCommentoTest() {
        INTDAO.addRisposta(new RispostaAmministratore(commentoCliente1.getId(), "Ti ringraziamo per la tua recensione!", amministratore, Timestamp.valueOf("2024-01-08 14:01:15")));

        boolean result = INTDAO.removeRispostaByIdCommento(commentoCliente1.getId());

        ArrayList<IInterazioneUtente> risposte = INTDAO.caricaInterazioni(amministratore);

        Assert.assertTrue(result);

        Assert.assertEquals(1, risposte.size()); //abbiamo rimosso una delle due riposte dell'amministratore

    }

}
