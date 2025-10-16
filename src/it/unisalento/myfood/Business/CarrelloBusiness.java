package it.unisalento.myfood.Business;

import it.unisalento.myfood.Business.AbstractFactory.ResultFactory;
import it.unisalento.myfood.Business.Command.Carrello.*;
import it.unisalento.myfood.DAO.*;
import it.unisalento.myfood.Business.Decorator.Icon.TableIconDecorator;
import it.unisalento.myfood.Business.Decorator.Icon.OriginalIcon;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Result.OperationCarrelloResult;
import it.unisalento.myfood.model.Carrello;
import it.unisalento.myfood.model.Utente;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class CarrelloBusiness {

    private static CarrelloBusiness instance = new CarrelloBusiness();

    private static HashMap<String, Object> session = UtenteBusiness.getSession();
    private ICarrelloDAO carrelloDAO;
    private IArticoloDAO articoloDAO = ArticoloDAO.getInstance();
    private final ArticoloBusiness articoloBusiness = ArticoloBusiness.getInstance();

    private OperationCarrelloResult operationCartResult;

    private ResultFactory resultFactory;
    //private ArrayList<ICartOperation> history;

    private CarrelloBusiness() {
        carrelloDAO = null;
        resultFactory = new ResultFactory("OperationCarrello");
      //  history = new ArrayList<>();
    }

    public static CarrelloBusiness getInstance() {
        return instance;
    }

    // Invocata dalla view e viene passata una HashMap con idProdotto e quantità dei prodotti che vengono selezionati
    // Ho messo una HashMap perchè il cliente potrebbe selezionare più Articoli contemporaneamente e passarglieli all'addArticoliToCart
    public void addArticoliToCart(HashMap<Integer, Integer> articoli) {

        Integer idUtente = ((Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID)).getId();

        // Passa uno a uno l'idArticolo e la quantità a CarrelloDAO e controlla se è andato tutto bene
        // Conviene passarne uno a uno, in modo da avere più controllo su eventuali errori
        for (Map.Entry<Integer, Integer> entry : articoli.entrySet()) {
            int idArticolo = entry.getKey();
            int quantita = entry.getValue();
            ICartOperation addToCarrello = new AddToCarrello(idUtente, quantita, idArticolo);
            addToCarrello.execute();
            }

        // Aggiorna il carrello nella sessione
        putCarrelloToSession();
    }

    public void addSelectedArticoloToCarrello(int quantita) {
        Integer idUtente = ((Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID)).getId();
        IArticolo articolo = ((IArticolo) session.get(UtenteBusiness.SELECTED_OBJECT));

        ICartOperation addToCarrello = new AddToCarrello(idUtente, quantita, articolo.getId());

        operationCartResult = addToCarrello.execute();
        //history.add(addToCarrello);


        putCarrelloToSession();
    }

    public void removeSelectedArticoloFromCart() {
        carrelloDAO = CarrelloDAO.getInstance();
        IArticolo articolo = ((IArticolo) session.get(UtenteBusiness.SELECTED_OBJECT));

        Integer idUtente = ((Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID)).getId();

        ICartOperation removeFromCarrello = new RemoveFromCarrello(idUtente, articolo.getId());

        operationCartResult = removeFromCarrello.execute();
        //history.add(removeFromCarrello);


        // Aggiorna il carrello nella sessione
        putCarrelloToSession();
    }
    // Metodo per svuotare il carrello, che sia dopo un ordine effettuato o quando lo si vuole svuotare

    public void emptyCart() {

        Utente u = (Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID);

        ICartOperation emptyCart = new EmptyCarrello(u.getId());
        operationCartResult = emptyCart.execute();
        //history.add(emptyCart);


        putCarrelloToSession();

    }

    public void addArticoliFromOrdine(Integer idOdine){
        Integer idUtente = ((Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID)).getId();
        ICartOperation addArticoliFromOrdine = new AddArticoliFromOrdine(idUtente, idOdine);
        operationCartResult = addArticoliFromOrdine.execute();
        //history.add(emptyCart);

        putCarrelloToSession();
    }

    public void putCarrelloToSession() {
        carrelloDAO = CarrelloDAO.getInstance();
        Integer idUtente = ((Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID)).getId();

        Carrello carrello = carrelloDAO.loadCarrelloPerIdUtente(idUtente);
        // Usa il metodo per calcolarsi la somma del carrello

        carrello.setTotale(calculateTotal());


        // Sostituisce il carrello aggiornato alla sessione dell'utente
        session.replace(UtenteBusiness.CART, carrello);   // Non lancia exception se non esiste

        // Se non esiste aggiunge il Carrello
        session.putIfAbsent(UtenteBusiness.CART, carrello);   // Non lancia exception se esiste
    }

    public float calculateTotal() {
        // Non èuò esserci il caso in cui non esiste il carrello
        Carrello carrello = (Carrello) session.get(UtenteBusiness.CART);

        if(carrello == null){
            return 0f;
        }

        carrelloDAO = CarrelloDAO.getInstance();
        articoloDAO = ArticoloDAO.getInstance();

        float total = 0f;

        for (Map.Entry<Integer, Integer> entry : carrello.getArticoli().entrySet()) {
            int idArticolo = entry.getKey();
            int quantita = entry.getValue();

            // Il metodo getPrezzo è diverso per Prodotto e Menu, quindi automaticamente si prende lo sconto eventuale
            total += articoloDAO.findById(idArticolo).getPrezzo() * quantita;
        }


        return total;
    }

    public int getArticoloQuantityInCart(IArticolo articolo) {
        Utente utente = (Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID);
        Integer quantity;

        if (utente.getRuolo() == Utente.RUOLO.CLIENTE) {
            Carrello carrello = carrelloDAO.loadCarrelloPerIdUtente(utente.getId());
            HashMap<Integer, Integer> articoli = carrello.getArticoli();

            quantity = articoli.get(articolo.getId());

        } else return 0;

        return (quantity != null ? quantity : 0);
    }

    public Object[][] getArticoliFromCarrello(){
        Utente utente = (Utente) session.get(UtenteBusiness.LOGGED_IN_USER_ID);
        ICartOperation loadCarrello = new LoadCarrello(utente.getId());

        operationCartResult = loadCarrello.execute();


        HashMap<Integer, Integer> articoli = operationCartResult.getCarrello().getArticoli();

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

    public int getViewArticoloQuantityInCart() {
        return getArticoloQuantityInCart((IArticolo) session.get(UtenteBusiness.SELECTED_OBJECT));
    }

    public OperationCarrelloResult getOperationCartResult() {
        return operationCartResult;
    }

    public boolean isAddeddSuccessfully(){
        return OperationCarrelloResult.EDIT_CART_RESULT.ADDED_SUCCESSFULLY.equals(operationCartResult.getEditCartResult());
    }

    public boolean isRemovedSuccessfully(){
        return OperationCarrelloResult.EDIT_CART_RESULT.REMOVED_SUCCESSFULLY.equals(operationCartResult.getEditCartResult());
    }

    public boolean isEmptiedSuccessfully(){
        return OperationCarrelloResult.EDIT_CART_RESULT.EMPTIED_SUCCESSFFULLY.equals(operationCartResult.getEditCartResult());
    }
    public boolean isError(){
        return OperationCarrelloResult.EDIT_CART_RESULT.ERROR.equals(operationCartResult.getEditCartResult());
    }

    public String getMessage() {
        return operationCartResult.getMessage();
    }

}
