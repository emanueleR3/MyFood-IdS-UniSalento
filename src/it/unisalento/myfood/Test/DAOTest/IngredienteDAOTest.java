package it.unisalento.myfood.Test.DAOTest;

import it.unisalento.myfood.DAO.*;
import it.unisalento.myfood.model.Azienda;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.Ingrediente;
import it.unisalento.myfood.model.TipologiaIngrediente;
import it.unisalento.myfood.model.TipologiaProdotto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class IngredienteDAOTest {

    private final ITipologiaIngredienteDAO TIDAO = TipologiaIngredienteDAO.getInstance();
    private final ITipologiaProdottoDAO TPDAO = TipologiaProdottoDAO.getInstance();
    private final IIngredienteDAO IDAO = IngredienteDAO.getInstance();
    private final IAziendaDAO ADAO = AziendaDAO.getInstance();
    private final IArticoloDAO ARDAO = ArticoloDAO.getInstance();
    private TipologiaIngrediente tipSal;
    private TipologiaIngrediente tipBurg;
    private TipologiaProdotto panTip;
    private Ingrediente burgerScottona;
    private Ingrediente bacon;
    private Azienda distributoreProsciutti1;
    private Azienda distributoreProsciutti2;
    private Azienda fiorucci;
    private Prodotto panino;
    private ArrayList<Azienda> distributori;


    //Creiamo un panino con 2 ingredienti: burger e bacon, 1 tipologia prodotto: panTip e 2 tipologie ingrediente: tipSal e tipBurg
    //1 produttore: fiorucci e 2 distributori
    @Before
    public void setUp(){

        ADAO.addAzienda("CESARE FIORUCCI S.P.A.", "04731980969");
        fiorucci = ADAO.findById(ADAO.getLastInsertId());

        ADAO.addAzienda("DistributoreProsciutti1 S.P.A.", "14733432456");
        distributoreProsciutti1 = ADAO.findById(ADAO.getLastInsertId());

        ADAO.addAzienda("DistributoreProsciutti2 S.P.A.", "14733445456");
        distributoreProsciutti2 = ADAO.findById(ADAO.getLastInsertId());

        distributori = new ArrayList<>();
        distributori.add(distributoreProsciutti1);
        distributori.add(distributoreProsciutti2);

        TIDAO.addTipologia("Tipologiai1");
        tipSal = TIDAO.findTipologiaById(TIDAO.getLastInsertId());
        TIDAO.addTipologia("Tipologiai2");
        tipBurg = TIDAO.findTipologiaById(TIDAO.getLastInsertId());

        TPDAO.addTipologia("Tipologiap1");
        panTip = TPDAO.findTipologiaByName("Tipologiap1");

        IDAO.addIngrediente(new Ingrediente("Buger di scottona", tipBurg, fiorucci, distributori));
        burgerScottona = IDAO.findIngredienteById(IDAO.getLastInsertId());

        IDAO.addIngrediente(new Ingrediente("Bacon", tipSal, fiorucci, distributori));
        bacon = IDAO.findIngredienteById(IDAO.getLastInsertId());

        ArrayList<Ingrediente> ingredienti = new ArrayList<>();
        ingredienti.add(burgerScottona);
        ingredienti.add(bacon);

        panino = new Prodotto("Bacon Burger", "bacon croccante [...]", 4.60f, 80, panTip, ingredienti, null, null);

        ARDAO.addArticolo(panino);
        panino.setId(ARDAO.getLastInsertId());

    }

    @After
    public void tearDown(){
        ARDAO.removeArticolo(panino);
        TPDAO.removeTipologia(panTip.getId());
        IDAO.removeIngrediente(burgerScottona.getId());
        IDAO.removeIngrediente(bacon.getId());
        TIDAO.removeTipologia(tipSal.getId());
        TIDAO.removeTipologia(tipBurg.getId());

        ADAO.removeAzienda(fiorucci.getId());
        ADAO.removeAzienda(distributoreProsciutti1.getId());
        ADAO.removeAzienda(distributoreProsciutti2.getId());

    }

    @Test
    public void getLastInsertIdTest() {
        Assert.assertEquals("Bacon", bacon.getNome());
    }

    @Test
    public void findIngredientePerTipologiaTest() {
        List<Ingrediente> ingredienti = IDAO.findIngredientePerTipologia("Tipologiai1");

        for (Ingrediente ingrediente : ingredienti) {
            Assert.assertNotNull(ingrediente);
            Assert.assertNotNull(ingrediente.getId());
        }

        Assert.assertEquals(bacon.getId(), ingredienti.get(0).getId());
        Assert.assertEquals(bacon.getNome(), ingredienti.get(0).getNome());

    }

    @Test
    public void getIngredientiPerProdottoTest(){
        List<Ingrediente> ingredienti = IDAO.getIngredientiPerProdotto(panino.getId());
        Assert.assertNotNull(ingredienti);
        Assert.assertFalse(ingredienti.isEmpty());
        Assert.assertEquals(ingredienti.get(0).getId(), burgerScottona.getId());
        Assert.assertEquals(ingredienti.get(1).getId(), bacon.getId());

    }

    @Test
    public void findIngredienteByIdTest() {
        Ingrediente ingrediente = IDAO.findIngredienteById(burgerScottona.getId());
        Assert.assertNotNull(ingrediente);
        Assert.assertEquals(burgerScottona.getId(), ingrediente.getId());
        Assert.assertEquals("Buger di scottona", ingrediente.getNome());
        Assert.assertEquals("Tipologiai2", ingrediente.getTipologiaIngrediente().getNome());
        Assert.assertEquals(fiorucci.getId(), ingrediente.getProduttore().getId());
    }

    @Test
    public void getDistributoriPerIngredienteTest() {
        ArrayList<Azienda> distributori = IDAO.getDistributoriPerIngrediente(burgerScottona.getId());
        Assert.assertNotNull(distributori);
        Assert.assertEquals(distributoreProsciutti1.getId(), distributori.get(0).getId());
        Assert.assertEquals(distributoreProsciutti2.getId(), distributori.get(1).getId());
        Assert.assertEquals(2, distributori.size());

    }

    @Test
    public void addIngredienteTest() {
        boolean result = IDAO.addIngrediente(new Ingrediente("Buger di pollo", tipBurg, fiorucci, distributori));
        Assert.assertTrue(result);
        Ingrediente burger_pollo = IDAO.findIngredienteById(IDAO.getLastInsertId());
        Assert.assertEquals("Buger di pollo", burger_pollo.getNome());
        Assert.assertEquals("Tipologiai2", burger_pollo.getTipologiaIngrediente().getNome());
        Assert.assertEquals(fiorucci.getId(), burger_pollo.getProduttore().getId());
        Assert.assertEquals(distributoreProsciutti1.getId(), burger_pollo.getDistributori().get(0).getId());
        Assert.assertEquals(distributoreProsciutti2.getId(), burger_pollo.getDistributori().get(1).getId());

        IDAO.removeIngrediente(burger_pollo.getId());
    }

    @Test
    public void removeIngredienteTest() {
        IDAO.addIngrediente(new Ingrediente("Buger di pollo", tipBurg, fiorucci, distributori));
        Ingrediente burger_pollo = IDAO.findIngredienteById(IDAO.getLastInsertId());
        boolean result = IDAO.removeIngrediente(burger_pollo.getId());
        Assert.assertTrue(result);
        Assert.assertNull(IDAO.findIngredienteById(burger_pollo.getId()).getId());
    }

    @Test
    public void editIngredienteTest() {
        burgerScottona.setNome("Burger di manzo");
        boolean result = IDAO.editIngrediente(burgerScottona);

        Assert.assertTrue(result);
        Ingrediente ingrediente = IDAO.findIngredienteById(burgerScottona.getId());
        Assert.assertEquals( "Burger di manzo", ingrediente.getNome());
        Assert.assertEquals("Tipologiai2", ingrediente.getTipologiaIngrediente().getNome());
        Assert.assertEquals(fiorucci.getId(), ingrediente.getProduttore().getId());
        Assert.assertEquals(distributoreProsciutti1.getId(), ingrediente.getDistributori().get(0).getId());
        Assert.assertEquals(distributoreProsciutti2.getId(), ingrediente.getDistributori().get(1).getId());


    }

    @Test
    public void addDistributoreToIngredienteTest() {
        ADAO.addAzienda("DistributoreCarni S.P.A.", "14733447857");
        Azienda distributoreCarni = ADAO.findById(ADAO.getLastInsertId());
        boolean result = IDAO.addDistributoreToIngrediente(burgerScottona.getId(), distributoreCarni.getId());

        Assert.assertTrue(result);

        burgerScottona = IDAO.findIngredienteById(burgerScottona.getId());

        Assert.assertEquals(3, burgerScottona.getDistributori().size());

        Assert.assertEquals("14733447857", burgerScottona.getDistributori().get(2).getPartitaIVA());

        IDAO.removeDistributoreFromIngrediente(burgerScottona.getId(), distributoreCarni.getId());
        ADAO.removeAzienda(distributoreCarni.getId());
    }

    @Test
    public void removeDistributoreFromIngredienteTest() {
        ADAO.addAzienda("DistributoreCarni S.P.A.", "14733447857");
        Azienda distributoreCarni = ADAO.findById(ADAO.getLastInsertId());
        IDAO.addDistributoreToIngrediente(burgerScottona.getId(), distributoreCarni.getId());

        boolean result = IDAO.removeDistributoreFromIngrediente(burgerScottona.getId(), distributoreCarni.getId());

        Assert.assertTrue(result);
        Assert.assertEquals(2, burgerScottona.getDistributori().size());

        ADAO.removeAzienda(distributoreCarni.getId());

    }


    @Test
    public void findAllTest() {
        ArrayList<Ingrediente> ingredienti = IDAO.findAll();
        Assert.assertNotNull(ingredienti);
        Assert.assertFalse(ingredienti.isEmpty());
        Assert.assertEquals(burgerScottona.getId(), ingredienti.get(1).getId());
        Assert.assertEquals("Buger di scottona", ingredienti.get(1).getNome());
        Assert.assertEquals("Tipologiai2", ingredienti.get(1).getTipologiaIngrediente().getNome());
        Assert.assertEquals(fiorucci.getId(), ingredienti.get(1).getProduttore().getId());

        Assert.assertEquals(bacon.getId(), ingredienti.get(0).getId());
        Assert.assertEquals("Bacon", ingredienti.get(0).getNome());
        Assert.assertEquals("Tipologiai1", ingredienti.get(0).getTipologiaIngrediente().getNome());
        Assert.assertEquals(fiorucci.getId(), ingredienti.get(0).getProduttore().getId());


    }
}
