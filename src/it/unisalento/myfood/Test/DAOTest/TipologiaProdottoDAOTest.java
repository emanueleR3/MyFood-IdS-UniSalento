package it.unisalento.myfood.Test.DAOTest;

import it.unisalento.myfood.DAO.ITipologiaProdottoDAO;
import it.unisalento.myfood.DAO.TipologiaProdottoDAO;
import it.unisalento.myfood.model.TipologiaProdotto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class TipologiaProdottoDAOTest {
    private final ITipologiaProdottoDAO TDAO = TipologiaProdottoDAO.getInstance();
    private TipologiaProdotto panTip;
    private TipologiaProdotto bevTip;

    @Before
    public void setUp(){
        TDAO.addTipologia("Tipologia1");
        panTip = TDAO.findTipologiaByName("Tipologia1");
        TDAO.addTipologia("Tipologia2");
        bevTip = TDAO.findTipologiaByName("Tipologia2");

    }

    @After
    public void tearDown(){
        TDAO.removeTipologia(panTip.getId());
        TDAO.removeTipologia(bevTip.getId());
    }

    @Test
    public void loadTipologiaTest(){
        ArrayList<TipologiaProdotto> tipologie = TDAO.loadTipologia();

        Assert.assertNotNull(tipologie);
        Assert.assertEquals(2,  tipologie.size());
        Assert.assertNotNull(tipologie.get(0));
        Assert.assertEquals("Tipologia2", tipologie.get(1).getNome());
        Assert.assertNotNull(tipologie.get(1));
        Assert.assertEquals("Tipologia1", tipologie.get(0).getNome());


    }

    @Test
    public void findTipologiaByIdTest() {
        TipologiaProdotto tipologiaProdotto = TDAO.findTipologiaById(bevTip.getId());
        Assert.assertNotNull(tipologiaProdotto);
        Assert.assertEquals(bevTip.getId(), tipologiaProdotto.getId());

    }

    @Test
    public void findTipologiaByNameTest() {
        TipologiaProdotto tipologiaProdotto = TDAO.findTipologiaByName("Tipologia2");
        Assert.assertNotNull(tipologiaProdotto);
        Assert.assertEquals(bevTip.getId(), tipologiaProdotto.getId());
        
    }

    @Test
    public void addTipologiaTest() {
        Assert.assertTrue(TDAO.addTipologia("Tipologia3"));
        TipologiaProdotto tipologiaProdotto = TDAO.findTipologiaByName("Tipologia3");
        Assert.assertNotNull(tipologiaProdotto);
        Assert.assertEquals("Tipologia3", tipologiaProdotto.getNome());
        

        TDAO.removeTipologia(tipologiaProdotto.getId());
    }

    @Test
    public void editTipologiaTest() {
        TDAO.editTipologia(bevTip.getId(), "Tipologia4");
        TipologiaProdotto tipologiaProdotto = TDAO.findTipologiaById(bevTip.getId());
        Assert.assertNotNull(tipologiaProdotto);
        Assert.assertEquals("Tipologia4", tipologiaProdotto.getNome());

        TDAO.editTipologia(bevTip.getId(), "Tipologia2"); //se si fa il teardown non trova la tipologia "Tipologia2" da rimuovere
    }



    @Test
    public void removeTipologiaTest() {
        Assert.assertTrue(TDAO.removeTipologia(bevTip.getId()));
        Assert.assertNull(TDAO.findTipologiaByName("Tipologia2"));
    }

}
