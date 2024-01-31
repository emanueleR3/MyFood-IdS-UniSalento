package it.unisalento.myfood.Test.DAOTest;

import it.unisalento.myfood.DAO.*;
import it.unisalento.myfood.model.Azienda;
import it.unisalento.myfood.model.Ingrediente;
import it.unisalento.myfood.model.TipologiaIngrediente;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class AziendaDAOTest {
    private IAziendaDAO ADAO = AziendaDAO.getInstance();
    private IIngredienteDAO IDAO = IngredienteDAO.getInstance();
    private ITipologiaIngredienteDAO TDAO = TipologiaIngredienteDAO.getInstance();
    private Azienda fiorucci;
    private Azienda distributoreProsciutti1;
    private Ingrediente ingrediente1;
    private TipologiaIngrediente tipSal;
    private Ingrediente ingrediente2;

    @Before
    public void setUp(){
        TDAO.addTipologia("Salume");

        tipSal = TDAO.findTipologiaById(TDAO.getLastInsertId());

        ADAO.addAzienda("CESARE FIORUCCI S.P.A.", "04731980969");
        fiorucci = ADAO.findById(ADAO.getLastInsertId());

        ADAO.addAzienda("DistributoreProsciutti1 S.P.A.", "14733432456");
        distributoreProsciutti1 = ADAO.findById(ADAO.getLastInsertId());

        ArrayList<Azienda> distributori = new ArrayList<>();

        distributori.add(distributoreProsciutti1);


        ingrediente1 = new Ingrediente("Beacon", tipSal, fiorucci, distributori);

        IDAO.addIngrediente(ingrediente1);

        ingrediente1.setId(IDAO.getLastInsertId());

        ingrediente2 = new Ingrediente("Salame", tipSal, fiorucci, distributori);

        IDAO.addIngrediente(ingrediente2);

        ingrediente2.setId(IDAO.getLastInsertId());



    }

    @After
    public void tearDown(){
        ADAO.removeAzienda(fiorucci.getId());
        ADAO.removeAzienda(distributoreProsciutti1.getId());
        IDAO.removeIngrediente(ingrediente1.getId());
        IDAO.removeIngrediente(ingrediente2.getId());
        TDAO.removeTipologia(tipSal.getId());


    }

    @Test
    public void findByPartitaIvaTest(){
        Azienda azienda = ADAO.findByPartitaIVA("04731980969");
        Assert.assertNotNull(azienda);
        System.out.println(azienda);
        Assert.assertEquals("04731980969", azienda.getPartitaIVA());
        Assert.assertEquals("CESARE FIORUCCI S.P.A.", azienda.getNome());
        Assert.assertEquals(ingrediente1.getId(), azienda.getIdIngredientiProdotti().get(0));
        Assert.assertEquals(ingrediente2.getId(), azienda.getIdIngredientiProdotti().get(1));
        Assert.assertFalse(azienda.getIdIngredientiDistribuiti().size() > 0);

    }


    @Test
    public void findByIdTest() {
        Azienda azienda = ADAO.findById(distributoreProsciutti1.getId());
        Assert.assertNotNull(azienda);
        Assert.assertEquals("14733432456", azienda.getPartitaIVA());
        Assert.assertEquals(ADAO.getLastInsertId(), azienda.getId());
        Assert.assertEquals(ingrediente1.getId(), azienda.getIdIngredientiDistribuiti().get(0));
        Assert.assertEquals(ingrediente2.getId(), azienda.getIdIngredientiDistribuiti().get(1));


        System.out.println(azienda);
    }

    @Test
    public void loadProducedIngredientiTest() {
        List<Integer> ingredienti = ADAO.loadProducedIngredienti(fiorucci.getId());
        Assert.assertNotNull(ingredienti);
        Assert.assertTrue(ingredienti.size() > 0);
        Assert.assertEquals(ingrediente1.getId(), ingredienti.get(0));
        Assert.assertEquals(ingrediente2.getId(), ingredienti.get(1));

        for(Integer i : ingredienti){
            System.out.println(IDAO.findIngredienteById(i));
        }

    }

    @Test
    public void loadDistributedIngredientiTest() {
        List<Integer> ingredienti = ADAO.loadDistributedIngredienti(distributoreProsciutti1.getId());
        Assert.assertNotNull(ingredienti);
        Assert.assertTrue(ingredienti.size() > 0);
        Assert.assertEquals(ingrediente1.getId(), ingredienti.get(0));
        Assert.assertEquals(ingrediente2.getId(), ingredienti.get(1));


        for(Integer i : ingredienti){
            System.out.println(IDAO.findIngredienteById(i));
        }

    }

    @Test
    public void addAziendaTest() {
        Assert.assertTrue(ADAO.addAzienda("DistributoreProsciutti2 S.P.A.", "28733432423"));
        Azienda distributoreProsciutti2 = ADAO.findById(ADAO.getLastInsertId());
        Assert.assertNotNull(distributoreProsciutti2);
        Assert.assertEquals("28733432423", distributoreProsciutti2.getPartitaIVA());
        Assert.assertEquals("DistributoreProsciutti2 S.P.A.", distributoreProsciutti2.getNome());
        System.out.println(distributoreProsciutti2);
        ADAO.removeAzienda(distributoreProsciutti2.getId());

    }

    @Test
    public void removeAziendaTest() {

        Assert.assertTrue(ADAO.addAzienda("DistributoreProsciutti2 S.P.A.", "28733432423"));

        Azienda distributoreProsciutti2 = ADAO.findById(ADAO.getLastInsertId());

        Assert.assertTrue(ADAO.removeAzienda(distributoreProsciutti2.getId()));

        Assert.assertNull(ADAO.findById(distributoreProsciutti2.getId()));


    }

}
