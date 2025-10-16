package it.unisalento.myfood.Business;

import it.unisalento.myfood.Business.Bridge.Documento;
import it.unisalento.myfood.Business.Bridge.DocumentoOrdine;
import it.unisalento.myfood.Business.Bridge.PdfBoxAPI;
import it.unisalento.myfood.Business.Command.Ordine.CreateOrdine;
import it.unisalento.myfood.Business.Command.Ordine.IOrdineOperation;
import it.unisalento.myfood.Business.Command.Ordine.RemoveOrdine;
import it.unisalento.myfood.Business.Command.Ordine.SetRicorrente;
import it.unisalento.myfood.Business.Decorator.Icon.OriginalIcon;
import it.unisalento.myfood.Business.Decorator.Icon.TableIconDecorator;
import it.unisalento.myfood.DAO.ArticoloDAO;
import it.unisalento.myfood.DAO.OrdineDAO;
import it.unisalento.myfood.DAO.UtenteDAO;
import it.unisalento.myfood.model.*;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.Menu;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.Result.OperationOrdineResult;

import java.sql.Timestamp;
import java.util.*;

public class OrdineBusiness {

    public enum STATO_ORDINE {
        PAGATO, IN_LAVORAZIONE, CONSEGNATO, NON_PAGATO;

    }
    private static OrdineBusiness instance = new OrdineBusiness();

    private static HashMap<String, Object> session = UtenteBusiness.getSession();   //serve la sessione per prendersi il carrello
    private final OrdineDAO ordineDAO = OrdineDAO.getInstance();
    private final CarrelloBusiness carrelloBusiness = CarrelloBusiness.getInstance();
    private final ArticoloDAO articoloDAO = ArticoloDAO.getInstance();
    private final ArticoloBusiness articoloBusiness = ArticoloBusiness.getInstance();
    private OperationOrdineResult operationOrdineResult;

    private OrdineBusiness() {
    }

    public static OrdineBusiness getInstance() {
        return instance;
    }

    public Integer create() {  //prende il carrello dalla sessione

        IOrdineOperation createOrdine = new CreateOrdine((Carrello) session.get(UtenteBusiness.CART));

        operationOrdineResult = createOrdine.execute();

        if(isCreatedSuccessfully()) {

            setSelectedOrdine(operationOrdineResult.getIdOrdine());

            // Svuota il carrello
            carrelloBusiness.emptyCart();
        }

        return operationOrdineResult.getIdOrdine();
    }

    private IOrdine.STATO convertState(STATO_ORDINE stato_ordine) {

        switch (stato_ordine) {
            case NON_PAGATO : return IOrdine.STATO.NON_PAGATO;
            case PAGATO : return IOrdine.STATO.PAGATO;
            case IN_LAVORAZIONE : return IOrdine.STATO.IN_LAVORAZIONE;
            case CONSEGNATO : return IOrdine.STATO.CONSEGNATO;
            default : return null;
        }
    }


    public int getNumberOfOrdiniByState(STATO_ORDINE stato_ordine) {
        IOrdine.STATO stato = convertState(stato_ordine);
        return OrdineDAO.getInstance().findByState(stato).size();
    }

    public Object[][] loadOrdiniCliente(){
        Utente cliente = (Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID);
        Integer idCliente = cliente.getId();

        ArrayList<Ordine> ordini = ordineDAO.findByCliente(idCliente);
        Object[][] ordiniMat = new Object[ordini.size()][5];

        Iterator<Ordine> iterator = ordini.listIterator();
        int i = 0;
        while (iterator.hasNext()){
            Ordine ordine = iterator.next();

            ordiniMat[i][0] = ordine.getIdOrdine();
            ordiniMat[i][1] = ordine.getData().toString();
            ordiniMat[i][2] = ordine.getStato().toString();
            ordiniMat[i][3] = ordine.getImporto();
            ordiniMat[i][4] = ordine.isRicorrente()? "Si" : "No";
            i++;
        }

        return ordiniMat;
    }


    public Object[][] loadOrdini(){

        ArrayList<Ordine> ordini = ordineDAO.findAll();
        Object[][] ordiniMat = new Object[ordini.size()][5];

        Iterator<Ordine> iterator = ordini.listIterator();
        int i = 0;
        while (iterator.hasNext()){

            Ordine ordine = iterator.next();
            ordiniMat[i][0] = ordine.getIdOrdine();
            ordiniMat[i][1] = ordine.getIdCliente();
            ordiniMat[i][2] = ordine.getData().toString();
            ordiniMat[i][3] = ordine.getStato().toString();
            ordiniMat[i][4] = ordine.getImporto();
            i++;
        }

        return ordiniMat;
    }

    public Object[][] loadOrdiniClienteByState(STATO_ORDINE statoOrdine){
        IOrdine.STATO stato = convertState(statoOrdine);

        Utente cliente = (Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID);
        Integer idCliente = cliente.getId();

        ArrayList<Ordine> ordini = ordineDAO.findByClienteAndState(idCliente, stato);

        Object[][] ordiniMat = new Object[ordini.size()][5];

        Iterator<Ordine> iterator = ordini.listIterator();
        int i = 0;
        while (iterator.hasNext()){
            Ordine ordine = iterator.next();

            ordiniMat[i][0] = ordine.getIdOrdine();
            ordiniMat[i][1] = ordine.getData().toString();
            ordiniMat[i][2] = ordine.getStato().toString();
            ordiniMat[i][3] = ordine.getImporto();
            ordiniMat[i][4] = ordine.isRicorrente()? "Si" : "No";
            i++;
        }

        return ordiniMat;
    }

    public Object[][] loadOrdiniByState(STATO_ORDINE statoOrdine){
        IOrdine.STATO stato = convertState(statoOrdine);


        ArrayList<Ordine> ordini = ordineDAO.findByState(stato);

        Object[][] ordiniMat = new Object[ordini.size()][5];

        Iterator<Ordine> iterator = ordini.listIterator();
        int i = 0;
        while (iterator.hasNext()){
            Ordine ordine = iterator.next();
            ordiniMat[i][0] = ordine.getIdOrdine();
            ordiniMat[i][1] = ordine.getIdCliente();
            ordiniMat[i][2] = ordine.getData().toString();
            ordiniMat[i][3] = ordine.getStato().toString();
            ordiniMat[i][4] = ordine.getImporto();
            i++;
        }

        return ordiniMat;
    }

    public Object[][] loadOrdiniRicorrentiCliente(){
        Utente cliente = (Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID);
        Integer idCliente = cliente.getId();

        ArrayList<Ordine> ordini = ordineDAO.findByRecurrent(idCliente);

        Object[][] ordiniMat = new Object[ordini.size()][5];

        Iterator<Ordine> iterator = ordini.listIterator();
        int i = 0;
        while (iterator.hasNext()){
            Ordine ordine = iterator.next();

            ordiniMat[i][0] = ordine.getIdOrdine();
            ordiniMat[i][1] = ordine.getData().toString();
            ordiniMat[i][2] = ordine.getStato().toString();
            ordiniMat[i][3] = ordine.getImporto();
            ordiniMat[i][4] = ordine.isRicorrente()? "Si" : "No";
            i++;
        }

        return ordiniMat;
    }


    public Object[][] loadOrdiniCucina() {
        // Ho in ordine prima gli ordini in lavorazione e poi quelli pagati
        ArrayList<Ordine> ordini = ordineDAO.findByState(IOrdine.STATO.IN_LAVORAZIONE);
        ordini.addAll(ordineDAO.findByState(IOrdine.STATO.PAGATO));

        Object[][] ordiniPerCucina = new Object[ordini.size()][4];

        Iterator<Ordine> iterator = ordini.listIterator();

        int i = 0;
        while (iterator.hasNext()){
        Ordine ordine = iterator.next();

            ordiniPerCucina[i][0] = ordine.getIdOrdine();
            ordiniPerCucina[i][1] = ordine.getData().toString();
            ordiniPerCucina[i][2] = ordine.getStato().toString();
            ordiniPerCucina[i][3] = getNumeroProdottiPerOrdine(ordine);
            i++;
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

    public Float getSelectedOrdineImporto(){
        Ordine ordine = (Ordine) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);

        return ordine.getImporto();
    }

    public Timestamp getSelectedOrdineData(){
        Ordine ordine = (Ordine) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);

        return ordine.getData();
    }

    public String getSelectedOrdineStato(){
        Ordine ordine = (Ordine) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);

        return ordine.getStato().toString();
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

    public boolean isOrdineRicorrente(){
        Ordine ordine = (Ordine) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        return ordine.isRicorrente();
    }

    public boolean isOrdineRicorrente(Integer idOrdine){
        Ordine ordine = ordineDAO.findById(idOrdine);
        return ordine.isRicorrente();
    }


    public void setOrdineRicorrente(Integer idOrdine, boolean stato){
        IOrdineOperation setRicorrente = new SetRicorrente(idOrdine, stato);
        operationOrdineResult = setRicorrente.execute();
        //history.add(setRicorrente)

    }

    public Object[][] loadArticoliBySelectedOrdine(){
        Ordine ordine = (Ordine) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT); //prende l'ordine dalla sessione


        HashMap<Integer, Integer> articoli = ordine.getArticoli();

        Iterator<Map.Entry<Integer, Integer>> iterator = articoli.entrySet().iterator();
                      //idArticolo    quantità

        Object[][] mat = new Object[articoli.size()][6];
        int i = 0;
        while(iterator.hasNext()){

            Map.Entry<Integer, Integer> entry = iterator.next();
            IArticolo articolo = articoloDAO.findById(entry.getKey());
            mat[i][0] = articolo.getId();

            articoloBusiness.setDirectoryFotoArticolo(articolo);

            if (articolo.getDirectoryFoto().isEmpty()) {
                OriginalIcon originalIcon = new OriginalIcon("src/immagini/icone/foto_prodotto_assente/foto_prodotto_assente.jpg");
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);

                mat[i][1] = tableIconDecorator.getImageIcon(); //per convenzione la prima va in tabella
            } else {
                OriginalIcon originalIcon = new OriginalIcon(articolo.getDirectoryFoto().get(0)); //per convenzione la prima va in tabella
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);

                mat[i][1] = tableIconDecorator.getImageIcon();
            }

            mat[i][2] = articolo.getNome();
            mat[i][3] = articolo.getDescrizione();
            mat[i][4] = articolo.getPrezzo();
            mat[i][5] = entry.getValue(); //quantità
            i++;
        }
        return mat;


    }

    public void removeOrdine(Integer idOrdine){

        Ordine ordine = ordineDAO.findById(idOrdine);
        IOrdineOperation removeOrdine = new RemoveOrdine(ordine);
        operationOrdineResult = removeOrdine.execute();

    }

    public String getMessage(){
        return operationOrdineResult.getMessage();
    }

    public boolean isCreatedSuccessfully(){
        return OperationOrdineResult.EDIT_ORDINE_RESULT.CREATED_SUCCESSFULLY.equals(operationOrdineResult.getEditOrdineResult());
    }


    public boolean isEditedSuccessfully(){
        return OperationOrdineResult.EDIT_ORDINE_RESULT.EDITED_SUCCESSFULLY.equals(operationOrdineResult.getEditOrdineResult());
    }

    public boolean isRemovedSuccessfully(){
        return OperationOrdineResult.EDIT_ORDINE_RESULT.REMOVED_SUCCESSFULLY.equals(operationOrdineResult.getEditOrdineResult());
    }



    public boolean isError(){
        return OperationOrdineResult.EDIT_ORDINE_RESULT.ERROR.equals(operationOrdineResult.getEditOrdineResult());
    }

    public void sendEmailOrdineCompletato(Integer idOrdine) {
        Ordine o = ordineDAO.findById(idOrdine);

        Documento listaPagamento = new DocumentoOrdine(new PdfBoxAPI(), o);
        listaPagamento.inviaListaOrdineCompletato(UtenteDAO.getInstance().findById(o.getIdCliente()).getEmail());
    }

    public void sendEmailOrdinePagato(Integer idOrdine) {
        Ordine o = ordineDAO.findById(idOrdine);

        Documento listaPagamento = new DocumentoOrdine(new PdfBoxAPI(), o);
        listaPagamento.inviaListaPagamentoEffettuato(UtenteDAO.getInstance().findById(o.getIdCliente()).getEmail());
    }
}
