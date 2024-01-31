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
        TDAO.addTipologia("Salume");
        tipSal = TDAO.findTipologiaById(TDAO.getLastInsertId());
        TDAO.addTipologia("Burger");
        tipBurg = TDAO.findTipologiaById(TDAO.getLastInsertId());
    }

    @After
    public void tearDown(){
        TDAO.removeTipologia(tipSal.getId());
        TDAO.removeTipologia(tipBurg.getId());
    }

    @Test
    public void findTipologiaById() {
        TipologiaIngrediente tipologiaIngrediente = TDAO.findTipologiaById(tipBurg.getId());
        Assert.assertNotNull(tipologiaIngrediente);
        Assert.assertEquals(tipBurg.getId(), tipologiaIngrediente.getId());
        Assert.assertEquals("Burger", tipologiaIngrediente.getNome());
        System.out.println(tipologiaIngrediente);
    }

    @Test
    public void addTipologiaTest(){
        Assert.assertTrue(TDAO.addTipologia("Salsa"));
        TipologiaIngrediente tipologiaIngrediente = TDAO.findTipologiaById(TDAO.getLastInsertId());
        Assert.assertEquals("Salsa", tipologiaIngrediente.getNome());
        System.out.println(TDAO.findTipologiaById(TDAO.getLastInsertId()));
        TDAO.removeTipologia(TDAO.getLastInsertId());

    }

    @Test
    public void editTipologiaTest() {
        boolean result = TDAO.editTipologia(tipBurg.getId(), "Carni");
        Assert.assertTrue(result);
        TipologiaIngrediente newTip = TDAO.findTipologiaById(tipBurg.getId());
        Assert.assertEquals("Carni", newTip.getNome());
        System.out.println(TDAO.findTipologiaById(tipBurg.getId()));
    }

    @Test
    public void removeTipologiaTest() {
        boolean result = TDAO.removeTipologia(tipBurg.getId());
        Assert.assertTrue(result);
        System.out.println(TDAO.findTipologiaById(tipBurg.getId()));
        Assert.assertNull(TDAO.findTipologiaById(tipBurg.getId()).getId());
        ArrayList<TipologiaIngrediente> tipologie = TDAO.loadTipologia();

        for(TipologiaIngrediente t : tipologie){
            System.out.println(t);
        }

        TDAO.addTipologia("Burger");
        tipBurg = TDAO.findTipologiaById(TDAO.getLastInsertId());
    }


    @Test
    public void loadTipologiaTest() {
        ArrayList<TipologiaIngrediente> tipologie = TDAO.loadTipologia();
        Assert.assertNotNull(tipologie);
        Assert.assertFalse(tipologie.isEmpty());
        Assert.assertEquals("Burger", tipologie.get(0).getNome());
        Assert.assertEquals("Salume", tipologie.get(1).getNome());

        for(TipologiaIngrediente t : tipologie){
            System.out.println(t);
        }

    }


}
