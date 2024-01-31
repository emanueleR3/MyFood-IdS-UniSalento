package it.unisalento.myfood.Test.DAOTest;

import it.unisalento.myfood.DAO.ITipologiaProdottoDAO;
import it.unisalento.myfood.DAO.IngredienteDAO;
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
        TDAO.addTipologia("Panini");
        panTip = TDAO.findTipologiaByName("Panini");
        TDAO.addTipologia("Bevande");
        bevTip = TDAO.findTipologiaByName("Bevande");

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
        Assert.assertEquals("Bevande", tipologie.get(0).getNome());
        Assert.assertNotNull(tipologie.get(1));
        Assert.assertEquals("Panini", tipologie.get(1).getNome());


        for(TipologiaProdotto t : tipologie){
            System.out.println(t);
        }

    }

    @Test
    public void findTipologiaByIdTest() {
        TipologiaProdotto tipologiaProdotto = TDAO.findTipologiaById(bevTip.getId());
        Assert.assertNotNull(tipologiaProdotto);
        Assert.assertEquals(bevTip.getId(), tipologiaProdotto.getId());
        System.out.println(tipologiaProdotto);

    }

    @Test
    public void findTipologiaByNameTest() {
        TipologiaProdotto tipologiaProdotto = TDAO.findTipologiaByName("Bevande");
        Assert.assertNotNull(tipologiaProdotto);
        Assert.assertEquals(bevTip.getId(), tipologiaProdotto.getId());
        System.out.println(tipologiaProdotto);
    }

    @Test
    public void addTipologiaTest() {
        Assert.assertTrue(TDAO.addTipologia("Patatine"));
        TipologiaProdotto tipologiaProdotto = TDAO.findTipologiaByName("Patatine");
        Assert.assertNotNull(tipologiaProdotto);
        Assert.assertEquals("Patatine", tipologiaProdotto.getNome());
        System.out.println(tipologiaProdotto);

        TDAO.removeTipologia(tipologiaProdotto.getId());
    }

    @Test
    public void editTipologiaTest() {
        TDAO.editTipologia(bevTip.getId(), "BevAnalcoliche");
        TipologiaProdotto tipologiaProdotto = TDAO.findTipologiaById(bevTip.getId());
        Assert.assertNotNull(tipologiaProdotto);
        Assert.assertEquals("BevAnalcoliche", tipologiaProdotto.getNome());
        System.out.println(tipologiaProdotto);

        TDAO.editTipologia(bevTip.getId(), "Bevande"); //se non si fa il teardown non trova la tipologia "Bevande" da rimuovere
    }



    @Test
    public void removeTipologiaTest() {
        Assert.assertTrue(TDAO.removeTipologia(bevTip.getId()));
        Assert.assertNull(TDAO.findTipologiaByName("Bevande"));
        TDAO.addTipologia("Bevande");
    }

}
