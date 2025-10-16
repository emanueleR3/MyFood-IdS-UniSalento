package it.unisalento.myfood.Test.DAOTest;

import it.unisalento.myfood.DAO.ITipologiaIngredienteDAO;
import it.unisalento.myfood.DAO.TipologiaIngredienteDAO;
import it.unisalento.myfood.model.TipologiaIngrediente;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TipologiaIngredienteDAOTest {
    private final ITipologiaIngredienteDAO TDAO = TipologiaIngredienteDAO.getInstance();
    private TipologiaIngrediente tipSal;
    private TipologiaIngrediente tipBurg;

    @Before
    public void setUp(){
        TDAO.addTipologia("Tipologia1");
        tipSal = TDAO.findTipologiaById(TDAO.getLastInsertId());
        TDAO.addTipologia("Tipologia2");
        tipBurg = TDAO.findTipologiaById(TDAO.getLastInsertId());
    }

    @After
    public void tearDown(){
        TDAO.removeTipologia(tipSal.getId());
        TDAO.removeTipologia(tipBurg.getId());
    }

    @Test
    public void getLastInsertIdTest() {
        Assert.assertEquals("Tipologia2", tipBurg.getNome());
    }

    @Test
    public void findTipologiaById() {
        TipologiaIngrediente tipologiaIngrediente = TDAO.findTipologiaById(tipBurg.getId());
        Assert.assertNotNull(tipologiaIngrediente);
        Assert.assertEquals(tipBurg.getId(), tipologiaIngrediente.getId());
        Assert.assertEquals("Tipologia2", tipologiaIngrediente.getNome());

    }

    @Test
    public void addTipologiaTest(){
        Assert.assertTrue(TDAO.addTipologia("Salsa"));
        TipologiaIngrediente tipologiaIngrediente = TDAO.findTipologiaById(TDAO.getLastInsertId());
        Assert.assertEquals("Salsa", tipologiaIngrediente.getNome());

        TDAO.removeTipologia(TDAO.getLastInsertId());

    }

    @Test
    public void editTipologiaTest() {
        boolean result = TDAO.editTipologia(tipBurg.getId(), "Carni");
        Assert.assertTrue(result);
        TipologiaIngrediente newTip = TDAO.findTipologiaById(tipBurg.getId());
        Assert.assertEquals("Carni", newTip.getNome());

    }

    @Test
    public void tipologiaExistsTest() {
        Assert.assertTrue(TDAO.tipologiaExists("Tipologia1"));
        Assert.assertFalse(TDAO.tipologiaExists("Tipologia3"));
    }

    @Test
    public void findTipologiaByNomeTest() {
        TipologiaIngrediente tipologiaIngrediente = TDAO.findTipologiaByNome("Tipologia1");
        Assert.assertNotNull(tipologiaIngrediente);
        Assert.assertEquals(tipSal.getId(), tipologiaIngrediente.getId());
        Assert.assertEquals("Tipologia1", tipologiaIngrediente.getNome());
    }

    @Test
    public void removeTipologiaTest() {
        boolean result = TDAO.removeTipologia(tipBurg.getId());
        Assert.assertTrue(result);
        Assert.assertNull(TDAO.findTipologiaById(tipBurg.getId()).getId());


        TDAO.addTipologia("Tipologia2");
        tipBurg = TDAO.findTipologiaById(TDAO.getLastInsertId());
    }


    @Test
    public void loadTipologiaTest() {
        ArrayList<TipologiaIngrediente> tipologie = TDAO.loadTipologia();
        Assert.assertNotNull(tipologie);
        Assert.assertFalse(tipologie.isEmpty());
        Assert.assertEquals("Tipologia2", tipologie.get(1).getNome());
        Assert.assertEquals("Tipologia1", tipologie.get(0).getNome());


    }


}
