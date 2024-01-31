package it.unisalento.myfood.Business;

import it.unisalento.myfood.Business.Command.Carrello.Ordine.CreateOrdine;
import it.unisalento.myfood.Business.Command.Carrello.Ordine.IOrdineOperation;
import it.unisalento.myfood.DAO.ArticoloDAO;
import it.unisalento.myfood.DAO.OrdineDAO;
import it.unisalento.myfood.model.Carrello;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.Menu;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.OperationOrdineResult;
import it.unisalento.myfood.model.IOrdine;
import it.unisalento.myfood.model.Ordine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrdineBusiness {

    public enum STATO_ORDINE {
        PAGATO, IN_LAVORAZIONE, CONSEGNATO, NON_PAGATO;

    }
    private static OrdineBusiness instance = new OrdineBusiness();
    private static HashMap<String, Object> session = UtenteBusiness.getSession();   //serve la sessione per prendersi il carrello
    private OrdineDAO ordineDAO = OrdineDAO.getInstance();
    private CarrelloBusiness carrelloBusiness = CarrelloBusiness.getInstance();

    private OperationOrdineResult operationOrdineResult;

    private OrdineBusiness() {}

    public static OrdineBusiness getInstance() {
        return instance;
    }

    public void create() {  //prende il carrello dalla sessione

        IOrdineOperation createOrdine = new CreateOrdine((Carrello) session.get(UtenteBusiness.CART));

        operationOrdineResult = createOrdine.execute();

        setSelectedOrdine(operationOrdineResult.getIdOrdine());

        // Svuota il carrello
        carrelloBusiness.emptyCart();
    }


    public int getNumberOfOrdiniByState(STATO_ORDINE stato_ordine) {
        IOrdine.STATO stato;

        switch (stato_ordine) {
            case NON_PAGATO -> stato = IOrdine.STATO.NON_PAGATO;
            case PAGATO -> stato = IOrdine.STATO.PAGATO;
            case IN_LAVORAZIONE -> stato = IOrdine.STATO.IN_LAVORAZIONE;
            case CONSEGNATO -> stato = IOrdine.STATO.CONSEGNATO;
            default -> stato = null;
        }

        return OrdineDAO.getInstance().findByState(stato).size();
    }

    public Object[][] loadOrdiniCucina() {
        // Ho in ordine prima gli ordini in lavorazione e poi quelli pagati
        ArrayList<Ordine> ordini = ordineDAO.findByState(IOrdine.STATO.IN_LAVORAZIONE);
        ordini.addAll(ordineDAO.findByState(IOrdine.STATO.PAGATO));

        Object[][] ordiniPerCucina = new Object[ordini.size()][4];

        for (int i = 0; i < ordini.size(); i++) {
            Ordine ordine = ordini.get(i);

            ordiniPerCucina[i][0] = ordine.getIdOrdine();
            ordiniPerCucina[i][1] = ordine.getData().toString();
            ordiniPerCucina[i][2] = ordine.getStato().toString();
            ordiniPerCucina[i][3] = getNumeroProdottiPerOrdine(ordine);
        }

        return ordiniPerCucina;
    }

    public int getNumeroProdottiPerOrdine(Ordine ordine) {
        ArticoloDAO articoloDAO = ArticoloDAO.getInstance();
        HashMap<Integer, Integer> articoli = ordine.getArticoli();
        int nOfProdotti = 0;

        for (Map.Entry<Integer, Integer> entry : articoli.entrySet()) {
            IArticolo articolo = articoloDAO.findById(entry.getKey());
            int quantita = entry.getValue();

            if (articolo instanceof Prodotto) {
                nOfProdotti += quantita;
            } else {
                ArrayList<IArticolo> articoliMenu1 = ((Menu) articolo).getArticoli();

                for (IArticolo a : articoliMenu1) {
                    if (a instanceof Prodotto) {
                        nOfProdotti += quantita;
                    } else {
                        ArrayList<IArticolo> articoliMenu2 = ((Menu) a).getArticoli();
                        nOfProdotti += (articoliMenu2.size() * quantita);
                    }
                }
            }
        }

        return nOfProdotti;
    }

    public void setSelectedOrdine(Integer idOrdine) {
        Ordine ordine = ordineDAO.findById(idOrdine);

        session.putIfAbsent(UtenteBusiness.SELECTED_OBJECT, ordine);

        session.replace(UtenteBusiness.SELECTED_OBJECT, ordine);
    }

    public boolean setSelectedOrdinePagato() {

        Ordine ordine = (Ordine) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        ordine.setStato(IOrdine.STATO.PAGATO);
        session.replace(UtenteBusiness.SELECTED_OBJECT, ordine);
        return ordineDAO.updateStatoOrdine(ordine, IOrdine.STATO.PAGATO);

    }

    public void setSelectedOrdineInLavorazione(Integer idOrdine) {
        Ordine ordine = ordineDAO.findById(idOrdine);
        ordineDAO.updateStatoOrdine(ordine, IOrdine.STATO.IN_LAVORAZIONE);

        ordine = ordineDAO.findById(idOrdine);

        session.replace(UtenteBusiness.SELECTED_OBJECT, ordine);
    }


    public void setSelectedOrdineCompletato(Integer idOrdine) {
        Ordine ordine = ordineDAO.findById(idOrdine);
        ordineDAO.updateStatoOrdine(ordine, IOrdine.STATO.CONSEGNATO);

        ordine = ordineDAO.findById(idOrdine);

        session.replace(UtenteBusiness.SELECTED_OBJECT, ordine);
    }

    public boolean isOrdineInLavorazione(Integer idOrdine) {
        return ordineDAO.findById(idOrdine).getStato().equals(IOrdine.STATO.IN_LAVORAZIONE);
    }

    public String getMessage(){
        return operationOrdineResult.getMessage();
    }

    public boolean isCreatedSuccessfully(){
        return OperationOrdineResult.EDIT_ORDINE_RESULT.CREATED_SUCCESSFULLY.equals(operationOrdineResult.getEditOrdineResult());
    }

    public boolean isError(){
        return OperationOrdineResult.EDIT_ORDINE_RESULT.ERROR.equals(operationOrdineResult.getEditOrdineResult());
    }
}
