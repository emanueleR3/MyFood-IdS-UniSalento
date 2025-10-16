package it.unisalento.myfood.Business;

import it.unisalento.myfood.Business.AbstractFactory.MenuFactory;
import it.unisalento.myfood.Business.AbstractFactory.ProdottoFactory;
import it.unisalento.myfood.Business.Command.Articolo.AddArticolo;
import it.unisalento.myfood.Business.Command.Articolo.EditArticolo;
import it.unisalento.myfood.Business.Command.Articolo.IArticoloOperation;
import it.unisalento.myfood.Business.Command.Articolo.RemoveArticolo;
import it.unisalento.myfood.Business.Decorator.Icon.OriginalIcon;
import it.unisalento.myfood.Business.Decorator.Icon.TableIconDecorator;
import it.unisalento.myfood.DAO.ArticoloDAO;
import it.unisalento.myfood.DAO.IngredienteDAO;
import it.unisalento.myfood.DAO.TipologiaIngredienteDAO;
import it.unisalento.myfood.DAO.TipologiaProdottoDAO;
import it.unisalento.myfood.model.*;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.Menu;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.Result.OperationArticoloResult;

import java.io.File;
import java.util.*;

public class ArticoloBusiness {


    public enum CAMPO {
        ID,
        NOME,
        DESCRIZIONE,
        PREZZO,
        DISPONIBILITA,
        SCONTO,
        TIPOLOGIA;
    }

    // COSTANTI COMPOSIZIONI
    public static final String COMPOSIZIONE_PANINO = "Panini - Bevande - Patatine - Salse - Menu";
    public static final String COMPOSIZIONE_INSALATA = "Insalate - Bevande - Patatine - Salse - Menu";
    public static final String SOLO_MENU = "Solo altri menu";


    private ArticoloDAO articoloDAO = ArticoloDAO.getInstance();
    private TipologiaProdottoDAO tipologiaProdottoDAO = TipologiaProdottoDAO.getInstance();
    private TipologiaIngredienteDAO tipologiaIngredienteDAO = TipologiaIngredienteDAO.getInstance();
    private MenuFactory menuFactory;

    private ProdottoFactory prodottoFactory;

    private IngredienteDAO ingredienteDAO = IngredienteDAO.getInstance();

    private OperationArticoloResult operationArticoloResult;
    private Stack<IArticoloOperation> undoStack = new Stack<>();
    private Stack<IArticoloOperation> redoStack = new Stack<>();
    private Integer MAX_CTRL_Z = 10;

    private ArticoloBusiness(){
        menuFactory = new MenuFactory();
        prodottoFactory = new ProdottoFactory();
    }

    private static ArticoloBusiness instance = new ArticoloBusiness();
    public static ArticoloBusiness getInstance(){return instance;}
    public Object[][] findMenu(){
        List<IArticolo> articoli = menuFactory.filterByTipologiaProdotto(null);

        Object[][] mat = new Object[articoli.size()][5];

        for(int i = 0; i < articoli.size(); i++){
            setDirectoryFotoArticolo(articoli.get(i));
            mat[i][0] = articoli.get(i).getId();

            if (!articoli.get(i).getDirectoryFoto().isEmpty()) {
                OriginalIcon originalIcon = new OriginalIcon(articoli.get(i).getDirectoryFoto().get(0));
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);
                mat[i][1] = tableIconDecorator.getImageIcon(); //per convenzione la prima va in tabella
            } else {
                OriginalIcon originalIcon = new OriginalIcon("src/immagini/icone/foto_prodotto_assente/foto_prodotto_assente.jpg");
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);
                mat[i][1] = tableIconDecorator.getImageIcon(); //per convenzione la prima va in tabella
            }

            mat[i][2] = articoli.get(i).getNome();
            mat[i][3] = articoli.get(i).getDescrizione();
            mat[i][4] = articoli.get(i).getPrezzo();
        }

        return mat;
    }

    public Object[][] getArticoliViewMenu() {
        Menu menu = (Menu) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);

        ArrayList<IArticolo> articoli = menu.getArticoli();

        Object[][] mat = new Object[articoli.size()][5];

        for (int i = 0; i < articoli.size(); i++) {
            setDirectoryFotoArticolo(articoli.get(i));
            mat[i][0] = articoli.get(i).getId();

            if (!articoli.get(i).getDirectoryFoto().isEmpty()) {
                OriginalIcon originalIcon = new OriginalIcon(articoli.get(i).getDirectoryFoto().get(0));
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);
                mat[i][1] = tableIconDecorator.getImageIcon(); //per convenzione la prima va in tabella
            } else {
                OriginalIcon originalIcon = new OriginalIcon("src/immagini/icone/foto_prodotto_assente/foto_prodotto_assente.jpg");
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);
                mat[i][1] = tableIconDecorator.getImageIcon(); //per convenzione la prima va in tabella
            }

            mat[i][2] = articoli.get(i).getNome();
            mat[i][3] = articoli.get(i).getDescrizione();
            mat[i][4] = articoli.get(i).getPrezzo();
        }

        return mat;
    }
    public Object[][] filterMenuByTipologiaIngrediente(String  tipologiaIngredienteName){

        TipologiaIngrediente tipologiaIngrediente = tipologiaIngredienteDAO.findTipologiaByNome(tipologiaIngredienteName);
        List<IArticolo> articoli = menuFactory.filterByTipologie(null, tipologiaIngrediente);

        Object[][] mat = new Object[articoli.size()][5];

        for(int i = 0; i < articoli.size(); i++){
            setDirectoryFotoArticolo(articoli.get(i));
            mat[i][0] = articoli.get(i).getId();

            if (!articoli.get(i).getDirectoryFoto().isEmpty()) {
                OriginalIcon originalIcon = new OriginalIcon(articoli.get(i).getDirectoryFoto().get(0));
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);
                mat[i][1] = tableIconDecorator.getImageIcon(); //per convenzione la prima va in tabella
            } else {
                OriginalIcon originalIcon = new OriginalIcon("src/immagini/icone/foto_prodotto_assente/foto_prodotto_assente.jpg");
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);
                mat[i][1] = tableIconDecorator.getImageIcon(); //per convenzione la prima va in tabella
            }

            mat[i][2] = articoli.get(i).getNome();
            mat[i][3] = articoli.get(i).getDescrizione();
            mat[i][4] = articoli.get(i).getPrezzo();
        }

        return mat;
    }

    public Object[][] getArticoliSelezionati(ArrayList<Integer> idArticoliInseriti) {
        ArrayList<IArticolo> articoli = new ArrayList<>();

        for (Integer id : idArticoliInseriti) {
            articoli.add(articoloDAO.findById(id));
        }

        Object[][] mat = new Object[articoli.size()][5];

        for(int i = 0; i < articoli.size(); i++){
            setDirectoryFotoArticolo(articoli.get(i));
            mat[i][0] = articoli.get(i).getId();

            if (!articoli.get(i).getDirectoryFoto().isEmpty()) {
                OriginalIcon originalIcon = new OriginalIcon(articoli.get(i).getDirectoryFoto().get(0));
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);
                mat[i][1] = tableIconDecorator.getImageIcon(); //per convenzione la prima va in tabella
            } else {
                OriginalIcon originalIcon = new OriginalIcon("src/immagini/icone/foto_prodotto_assente/foto_prodotto_assente.jpg");
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);
                mat[i][1] = tableIconDecorator.getImageIcon(); //per convenzione la prima va in tabella
            }

            mat[i][2] = articoli.get(i).getNome();
            mat[i][3] = articoli.get(i).getDescrizione();
            mat[i][4] = articoli.get(i).getPrezzo();
        }

        return mat;
    }

    public Object[][] filterByTipologiaProdotto(String nomeTipologia){
        TipologiaProdotto tipologiaProdotto = tipologiaProdottoDAO.findTipologiaByName(nomeTipologia);
        List<IArticolo> articoli = prodottoFactory.filterByTipologiaProdotto(tipologiaProdotto);

        Object[][] mat = new Object[articoli.size()][5];

        for(int i = 0; i < articoli.size(); i++){
            setDirectoryFotoArticolo(articoli.get(i));
            mat[i][0] = articoli.get(i).getId();

            if (!articoli.get(i).getDirectoryFoto().isEmpty()) {
                OriginalIcon originalIcon = new OriginalIcon(articoli.get(i).getDirectoryFoto().get(0));
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);
                mat[i][1] = tableIconDecorator.getImageIcon(); //per convenzione la prima va in tabella
            } else {
                OriginalIcon originalIcon = new OriginalIcon("src/immagini/icone/foto_prodotto_assente/foto_prodotto_assente.jpg");
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);
                mat[i][1] = tableIconDecorator.getImageIcon(); //per convenzione la prima va in tabella
            }

            mat[i][2] = articoli.get(i).getNome();
            mat[i][3] = articoli.get(i).getDescrizione();
            mat[i][4] = articoli.get(i).getPrezzo();
        }

        return mat;
    }

    public Object[][] filterByTipologie(String tipologiaProdottoName, String tipologiaIngredienteName){
        TipologiaProdotto tipologiaProdotto = tipologiaProdottoDAO.findTipologiaByName(tipologiaProdottoName);
        TipologiaIngrediente tipologiaIngrediente = tipologiaIngredienteDAO.findTipologiaByNome(tipologiaIngredienteName);

        List<IArticolo> articoli = prodottoFactory.filterByTipologie(tipologiaProdotto, tipologiaIngrediente);

        Object[][] mat = new Object[articoli.size()][5];

        for(int i = 0; i < articoli.size(); i++){
            setDirectoryFotoArticolo(articoli.get(i));
            mat[i][0] = articoli.get(i).getId();

            if (!articoli.get(i).getDirectoryFoto().isEmpty()) {
                OriginalIcon originalIcon = new OriginalIcon(articoli.get(i).getDirectoryFoto().get(0));
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);
                mat[i][1] = tableIconDecorator.getImageIcon(); //per convenzione la prima va in tabella
            } else {
                OriginalIcon originalIcon = new OriginalIcon("src/immagini/icone/foto_prodotto_assente/foto_prodotto_assente.jpg");
                TableIconDecorator tableIconDecorator = new TableIconDecorator(originalIcon);
                mat[i][1] = tableIconDecorator.getImageIcon(); //per convenzione la prima va in tabella
            }

            mat[i][2] = articoli.get(i).getNome();
            mat[i][3] = articoli.get(i).getDescrizione();
            mat[i][4] = articoli.get(i).getPrezzo();
        }

        return mat;
    }


    public void reloadViewArticoloSession() {
        IArticolo articolo = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        articolo.setCommenti(ArticoloDAO.getInstance().findById(articolo.getId()).getCommenti());

        UtenteBusiness.getSession().replace(UtenteBusiness.SELECTED_OBJECT, articolo);
    }

    public ArrayList<String> getDirectoryFotoViewArticolo() {
        IArticolo articolo = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);
        setDirectoryFotoArticolo(articolo);

        UtenteBusiness.getSession().replace(UtenteBusiness.SELECTED_OBJECT, articolo);

        return articolo.getDirectoryFoto();
    }


    public void setDirectoryFotoArticolo(IArticolo articolo) {
        ArrayList<String> directoryFoto = new ArrayList<>();

        String percorsoCartella = "src/immagini/articoli/" + articolo.getId();  // Specifica il percorso della cartella
        //String percorsoCartella = "src/immagini/articoli/" + 3;  // Specifica il percorso della cartella

        // Crea un oggetto File con il percorso della cartella
        File cartella = new File(percorsoCartella);
        if (!cartella.exists() || !cartella.isDirectory()) {
            percorsoCartella = "src/immagini/icone/foto_prodotto_assente";
            cartella = new File(percorsoCartella);
        }
        // Verifica se la cartella esiste e contiene almeno un file o una sottocartella
        if (cartella.exists() && cartella.isDirectory()) {
            File[] contenuto = cartella.listFiles();

            if (contenuto != null && contenuto.length > 0) {
                for (File f : contenuto) {
                    directoryFoto.add(percorsoCartella + "/" + f.getName());
                }
            }
        } else {
            System.out.println("La cartella non esiste o non è una cartella valida.");
        }

        articolo.setDirectoryFoto(directoryFoto);
    }

    public Object getCampoSelectedArticolo(CAMPO nomeCampo) {
        IArticolo articolo = (IArticolo) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);

        switch (nomeCampo) {
            case NOME -> {
                return articolo.getNome();
            }
            case DESCRIZIONE -> {
                return articolo.getDescrizione();
            }
            case ID -> {
                return articolo.getId();
            }
            case PREZZO -> {
                return articolo.getPrezzo();
            }
            case DISPONIBILITA -> {
                if (articolo instanceof Prodotto)
                    return articolo.getPezziDisponibili();
                if (articolo instanceof Menu)
                    return getPezziDisponibiliForMenu((Menu) articolo);
            }
            case SCONTO -> {
                if (articolo instanceof Menu)
                    return ((Menu) articolo).getSconto();
                else
                    return null;
            }
            case TIPOLOGIA -> {
                if (articolo instanceof Menu)
                    return "Menu";
                else
                    return ((Prodotto) articolo).getTipologiaProdotto().getNome();
            }
        }

        return null;
    }

    public ArrayList<String> getNomeComponentiViewMenu() {
        ArrayList<IArticolo> componenti = ((Menu) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT)).getArticoli();
        ArrayList<String> nomiComponenti = new ArrayList<>();

        for (IArticolo a : componenti) {
            nomiComponenti.add(a.getNome());
        }

        return nomiComponenti;
    }

    public boolean isSelectedArticoloInstanceOfProdotto() {
        return UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT) instanceof Prodotto;
    }

    public boolean isSelectedArticoloInstanceOfMenu() {
        return UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT) instanceof Menu;
    }

    public void setSelectedArticolo(Integer id){
        IArticolo articolo = articoloDAO.findById(id);

        UtenteBusiness.getSession().putIfAbsent(UtenteBusiness.SELECTED_OBJECT, articolo);

        UtenteBusiness.getSession().replace(UtenteBusiness.SELECTED_OBJECT, articolo);
    }

    public int getPezziDisponibiliForMenu(Menu menu) {
        ArrayList<IArticolo> articoli = menu.getArticoli();
        ArrayList<Integer> quantita = new ArrayList<>();

        for (IArticolo a : articoli) {
            if (a instanceof Prodotto) {
                quantita.add(a.getPezziDisponibili());
            } else {
                quantita.add(getPezziDisponibiliForMenu((Menu) a));
            }
        }

        int min = quantita.get(0);
        for (Integer i : quantita) {
            min = Math.min(i, min);
        }
        return min;
    }

    public ArrayList<String> getTipologiePerComposizione(String selectedItem) {
        ArrayList<String> tipologieAccettate = new ArrayList<>();

        switch (selectedItem) {
            case COMPOSIZIONE_INSALATA -> {
                tipologieAccettate.add("Insalate");
                tipologieAccettate.add("Bevande");
                tipologieAccettate.add("Patatine");
                tipologieAccettate.add("Salse");
                tipologieAccettate.add("Menu");

                return tipologieAccettate;
            }
            case COMPOSIZIONE_PANINO -> {
                tipologieAccettate.add("Panini");
                tipologieAccettate.add("Bevande");
                tipologieAccettate.add("Patatine");
                tipologieAccettate.add("Salse");
                tipologieAccettate.add("Menu");

                return tipologieAccettate;
            }
            case SOLO_MENU -> {
                tipologieAccettate.add("Menu");

                return tipologieAccettate;
            }
            default -> {
                return null;
            }
        }
    }

    public boolean isComposizioneValida(String composizioneScelta, ArrayList<Integer> articoliInseriti) {
        boolean isCorrect = false;

        switch (composizioneScelta) {
            case COMPOSIZIONE_INSALATA -> {
                boolean almenoUnInsalata = false;
                boolean almenoUnaBevanda = false;

                for (Integer id : articoliInseriti) {
                    IArticolo articolo = articoloDAO.findById(id);

                    if (articolo instanceof Prodotto) {
                        if ("Insalate".equalsIgnoreCase(((Prodotto) articolo).getTipologiaProdotto().getNome())) almenoUnInsalata = true;
                        if ("Bevande".equalsIgnoreCase(((Prodotto) articolo).getTipologiaProdotto().getNome())) almenoUnaBevanda = true;
                    }
                }

                isCorrect = almenoUnInsalata && almenoUnaBevanda;
            }
            case COMPOSIZIONE_PANINO -> {
                boolean almenoUnPanino = false;
                boolean almenoUnaBevanda = false;

                for (Integer id : articoliInseriti) {
                    IArticolo articolo = articoloDAO.findById(id);

                    if (articolo instanceof Prodotto) {
                        if ("Panini".equalsIgnoreCase(((Prodotto) articolo).getTipologiaProdotto().getNome())) almenoUnPanino = true;
                        if ("Bevande".equalsIgnoreCase(((Prodotto) articolo).getTipologiaProdotto().getNome())) almenoUnaBevanda = true;
                    }
                }

                isCorrect = almenoUnPanino && almenoUnaBevanda;
            }
            case SOLO_MENU -> {
                boolean almenoDueMenu = false;
                int numberOfMenu = 0;

                for (Integer id : articoliInseriti) {
                    IArticolo articolo = articoloDAO.findById(id);

                    if (articolo instanceof Menu) {
                        numberOfMenu++;
                        if (numberOfMenu > 1) almenoDueMenu = true;
                    }
                }

                isCorrect = almenoDueMenu;
            }
        }

        return isCorrect;
    }

    public HashMap<String, ArrayList<String>> loadComposizioniMenu() {
        HashMap<String, ArrayList<String>> composizioni = new HashMap<>();

        // Questo andrebbe fatto col database per renderlo scalabile al 100%
        ArrayList<TipologiaComposizioneMenu> composizioniMenu = new ArrayList<>();

        ArrayList<TipologiaProdotto> tipologieAccettate = new ArrayList<>();
        tipologieAccettate.add(tipologiaProdottoDAO.findTipologiaByName("Panini"));
        tipologieAccettate.add(tipologiaProdottoDAO.findTipologiaByName("Bevande"));
        tipologieAccettate.add(tipologiaProdottoDAO.findTipologiaByName("Patatine"));
        tipologieAccettate.add(tipologiaProdottoDAO.findTipologiaByName("Salse"));

        TipologiaComposizioneMenu tipologiaComposizione1 = new TipologiaComposizioneMenu(COMPOSIZIONE_PANINO, tipologieAccettate);

        tipologieAccettate.clear();
        tipologieAccettate.add(tipologiaProdottoDAO.findTipologiaByName("Insalate"));
        tipologieAccettate.add(tipologiaProdottoDAO.findTipologiaByName("Bevande"));
        tipologieAccettate.add(tipologiaProdottoDAO.findTipologiaByName("Patatine"));
        tipologieAccettate.add(tipologiaProdottoDAO.findTipologiaByName("Salse"));

        TipologiaComposizioneMenu tipologiaComposizione2 = new TipologiaComposizioneMenu(COMPOSIZIONE_INSALATA, tipologieAccettate);

        TipologiaComposizioneMenu tipologiaComposizione3 = new TipologiaComposizioneMenu(SOLO_MENU, new ArrayList<>());

        composizioniMenu.add(tipologiaComposizione1);
        composizioniMenu.add(tipologiaComposizione2);
        composizioniMenu.add(tipologiaComposizione3);

        for (TipologiaComposizioneMenu tcm : composizioniMenu) {
            ArrayList<String> nomiTipologie = new ArrayList<>();
            for (TipologiaProdotto tp : tcm.getTipologieAccettate()) {
                nomiTipologie.add(tp.getNome());
            }
            composizioni.put(tcm.getNomeComposizione(), nomiTipologie);
        }

        return composizioni;
    }

    public String getComposizioneViewMenu() {
        Menu menu = (Menu) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);

        ArrayList<IArticolo> articoli = menu.getArticoli();

        boolean paninoInMenu = false;
        boolean almenoUnMenuInMenu = false;
        boolean almenoUnProdottoInMenu = false;
        for (IArticolo a : articoli) {
            if (a instanceof Prodotto) {
                almenoUnProdottoInMenu = true;
                if ("Panini".equalsIgnoreCase(((Prodotto) a).getTipologiaProdotto().getNome())) {
                    paninoInMenu = true;
                }
            } else {
                almenoUnMenuInMenu = true;
            }
        }

        if (almenoUnMenuInMenu && !almenoUnProdottoInMenu) return SOLO_MENU;
        else if (almenoUnProdottoInMenu) return paninoInMenu ? COMPOSIZIONE_PANINO : COMPOSIZIONE_INSALATA;

        // Caso in cui non ci siano nè prodotti nè menu, ma è impossibile
        return null;
    }

    public boolean addProdotto(String nome, Integer pezziDisponibili, String descrizione, Float prezzo, ArrayList<Integer> idIngredienti, String tipologia, ArrayList<File> fotoScelte){
        boolean done = false;

        Prodotto prodotto = (Prodotto) prodottoFactory.crea();

        prodotto.setNome(nome);
        prodotto.setPezziDisponibili(pezziDisponibili);
        prodotto.setDescrizione(descrizione);
        prodotto.setPrezzo(prezzo);

        ArrayList<Ingrediente> ingredienti = new ArrayList<>();
        Iterator<Integer> iterator = idIngredienti.iterator();

        while (iterator.hasNext()){
            ingredienti.add(ingredienteDAO.findIngredienteById(iterator.next()));
        }

        prodotto.setIngredienti(ingredienti);

        prodotto.setTipologiaProdotto(tipologiaProdottoDAO.findTipologiaByName(tipologia));

        IArticoloOperation addProdotto = new AddArticolo(prodotto);
        operationArticoloResult = addProdotto.execute();

        if(isAddedSuccessfully()) {
            done = true;

            // Crea la nuova cartella e inserisce i file
            ImmagineBusiness.getInstance().createFolderAndSaveImages(fotoScelte);

            undoStack.push(addProdotto);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

        return done;
    }

    public void addMenu(String nome, String descrizione, Float sconto, ArrayList<Integer> idArticoli, ArrayList<File> fotoScelte){

        Menu menu = (Menu) menuFactory.crea();
        menu.setNome(nome);
        menu.setDescrizione(descrizione);
        menu.setSconto(sconto);

        ArrayList<IArticolo> articoli = new ArrayList<>();

        Iterator<Integer> iterator = idArticoli.iterator();

        while (iterator.hasNext()){
            articoli.add(articoloDAO.findById(iterator.next()));
        }

        menu.setArticoli(articoli);

        IArticoloOperation addMenu = new AddArticolo(menu);
        operationArticoloResult = addMenu.execute();


        if(isAddedSuccessfully()) {

            ImmagineBusiness.getInstance().createFolderAndSaveImages(fotoScelte);

            undoStack.push(addMenu);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }
    }

    public void editSelectedMenu(String nome, String newDescrizione, Float newSconto, ArrayList<Integer> newIdArticoli, ArrayList<File> newFotoScelte){
        Menu menu = (Menu) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT); //in modo da mantenere l'id
        menu.setNome(nome);
        menu.setDescrizione(newDescrizione);
        menu.setSconto(newSconto);

        ArrayList<IArticolo> articoli = new ArrayList<>();

        Iterator<Integer> iterator = newIdArticoli.iterator();

        while (iterator.hasNext()){
            articoli.add(articoloDAO.findById(iterator.next()));
        }

        menu.setArticoli(articoli);

        IArticoloOperation editProdotto = new EditArticolo(menu);
        operationArticoloResult = editProdotto.execute();

        if(isEditedSuccessfully()) {
            ImmagineBusiness.getInstance().saveNewImages(newFotoScelte, menu.getId());

            undoStack.push(editProdotto);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }
    }

    public boolean editSelectedProdotto(String newNome, Integer newPezziDisponibili, String newDescrizione, Float newPrezzo, ArrayList<Integer> newIdIngredienti, String newTipologia, ArrayList<File> newFotoScelte){
        boolean done = false;

        Prodotto prodotto = (Prodotto) UtenteBusiness.getSession().get(UtenteBusiness.SELECTED_OBJECT);

        prodotto.setNome(newNome);
        prodotto.setPezziDisponibili(newPezziDisponibili);
        prodotto.setDescrizione(newDescrizione);
        prodotto.setPrezzo(newPrezzo);

        ArrayList<Ingrediente> ingredienti = new ArrayList<>();
        Iterator<Integer> iterator = newIdIngredienti.iterator();

        while (iterator.hasNext()){
            ingredienti.add(ingredienteDAO.findIngredienteById(iterator.next()));
        }

        prodotto.setIngredienti(ingredienti);

        prodotto.setTipologiaProdotto(tipologiaProdottoDAO.findTipologiaByName(newTipologia));

        IArticoloOperation editProdotto = new EditArticolo(prodotto);
        operationArticoloResult = editProdotto.execute();

        if(isEditedSuccessfully()) {
            done = true;

            ImmagineBusiness.getInstance().saveNewImages(newFotoScelte, prodotto.getId());

            undoStack.push(editProdotto);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

        return done;
    }

    public void removeArticolo(Integer idArticolo){

        IArticolo articolo = articoloDAO.findById(idArticolo);

        IArticoloOperation removeArticolo = new RemoveArticolo(articolo);
        operationArticoloResult = removeArticolo.execute();

        if(isRemovedSuccessfully()) {
            undoStack.push(removeArticolo);
            if (undoStack.size() > MAX_CTRL_Z) {
                undoStack.remove(0);
            }
            redoStack.clear();
        }

    }

    public boolean undo(){
        if (!undoStack.isEmpty()) {
            IArticoloOperation operation = undoStack.pop();
            operationArticoloResult = operation.undo();
            redoStack.push(operation);
            return true;
        }
        return false;
    }

    public boolean redo(){
        if (!redoStack.isEmpty()) {
            IArticoloOperation operation = redoStack.pop();
            operationArticoloResult = operation.execute();
            undoStack.push(operation);
            return true;
        }
        return false;
    }

    public boolean isAddedSuccessfully(){
        return OperationArticoloResult.EDIT_ARTICOLO_RESULT.ADDED_SUCCESSFULLY.equals(operationArticoloResult.getEditArticoloResult());
    }

    public boolean isRemovedSuccessfully(){
        return OperationArticoloResult.EDIT_ARTICOLO_RESULT.REMOVED_SUCCESSFULLY.equals(operationArticoloResult.getEditArticoloResult());
    }

    public boolean isEditedSuccessfully(){
        return OperationArticoloResult.EDIT_ARTICOLO_RESULT.EDITED_SUCCESSFULLY.equals(operationArticoloResult.getEditArticoloResult());
    }


    public boolean isProdotto(Integer idArticolo){
        return articoloDAO.findById(idArticolo) instanceof Prodotto;
    }

    public boolean isMenu(Integer idArticolo){
        return articoloDAO.findById(idArticolo) instanceof Menu;
    }

    public boolean isSupermenu(Integer idArticolo) {
        IArticolo articolo = articoloDAO.findById(idArticolo);

        if (articolo instanceof Menu) {
            return ((Menu) articolo).getArticoli().stream().anyMatch(obj -> obj instanceof Menu);
        } else {
            return false;
        }
    }

    public String getMessage(){
        return operationArticoloResult.getMessage();
    }
}