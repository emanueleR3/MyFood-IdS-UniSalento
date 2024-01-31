package it.unisalento.myfood.Business;

import it.unisalento.myfood.Business.AbstractFactory.MenuFactory;
import it.unisalento.myfood.Business.AbstractFactory.ProdottoFactory;
import it.unisalento.myfood.DAO.ArticoloDAO;
import it.unisalento.myfood.DAO.IngredienteDAO;
import it.unisalento.myfood.DAO.TipologiaProdottoDAO;
import it.unisalento.myfood.View.Decorator.Icon.IconResizedDecorator;
import it.unisalento.myfood.View.Decorator.Icon.OriginalIcon;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.Menu;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.Ingrediente;
import it.unisalento.myfood.model.TipologiaProdotto;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArticoloBusiness {

    public enum CAMPO {
        ID,
        NOME,
        DESCRIZIONE,
        PREZZO,
        DISPONIBILITA,
        SCONTO_MENU
    }


    private ArticoloDAO articoloDAO = ArticoloDAO.getInstance();
    private TipologiaProdottoDAO tipologiaProdottoDAO = TipologiaProdottoDAO.getInstance();
    private MenuFactory menuFactory;
    private ProdottoFactory prodottoFactory;
    private IngredienteDAO ingredienteDAO = IngredienteDAO.getInstance();

    private ArticoloBusiness(){
        menuFactory = new MenuFactory();
        prodottoFactory = new ProdottoFactory();
    }

    private static ArticoloBusiness instance = new ArticoloBusiness();

    public static ArticoloBusiness getInstance(){return instance;}

    public Object[][] findMenu(){
        List<IArticolo> articoli = menuFactory.filterByTipologia(null);

        Object[][] mat = new Object[articoli.size()][5];

        for(int i = 0; i < articoli.size(); i++){
            setDirectoryFotoArticolo(articoli.get(i));
            mat[i][0] = articoli.get(i).getId();
            mat[i][1] = new ImageIcon(articoli.get(i).getDirectoryFoto().get(0));   //per convenzione la prima va in tabella
            mat[i][2] = articoli.get(i).getNome();
            mat[i][3] = articoli.get(i).getDescrizione();
            mat[i][4] = articoli.get(i).getPrezzo();
        }

        return mat;
    }

    public Object[][] filterMenuByIngrediente(Integer  idIngrediente){
        Ingrediente ingrediente = ingredienteDAO.findIngredienteById(idIngrediente);
        List<IArticolo> articoli = menuFactory.filterByTipologiaEIngrediente(null, ingrediente);

        Object[][] mat = new Object[articoli.size()][5];

        for(int i = 0; i < articoli.size(); i++){
            setDirectoryFotoArticolo(articoli.get(i));
            mat[i][0] = articoli.get(i).getId();
            mat[i][1] = new ImageIcon(articoli.get(i).getDirectoryFoto().get(0));   //per convenzione la prima va in tabella
            mat[i][2] = articoli.get(i).getNome();
            mat[i][3] = articoli.get(i).getDescrizione();
            mat[i][4] = articoli.get(i).getPrezzo();
        }

        return mat;
    }

    public Object[][] filterByTipologia(String nomeTipologia){
        TipologiaProdotto tipologiaProdotto = tipologiaProdottoDAO.findTipologiaByName(nomeTipologia);
        List<IArticolo> articoli = prodottoFactory.filterByTipologia(tipologiaProdotto);

        Object[][] mat = new Object[articoli.size()][5];

        for(int i = 0; i < articoli.size(); i++){
            setDirectoryFotoArticolo(articoli.get(i));
            mat[i][0] = articoli.get(i).getId();

            OriginalIcon originalIcon = new OriginalIcon(articoli.get(i).getDirectoryFoto().get(0));
            IconResizedDecorator iconResizedDecorator = new IconResizedDecorator(originalIcon, 80, 80);

            mat[i][1] = iconResizedDecorator.getImageIcon(); //per convenzione la prima va in tabella
            mat[i][2] = articoli.get(i).getNome();
            mat[i][3] = articoli.get(i).getDescrizione();
            mat[i][4] = articoli.get(i).getPrezzo();
        }

        return mat;
    }

    public Object[][] filterByTipologiaEIngrediente(String nomeTipologia, Integer idIngrediente){
        TipologiaProdotto tipologiaProdotto = tipologiaProdottoDAO.findTipologiaByName(nomeTipologia);
        Ingrediente ingrediente = ingredienteDAO.findIngredienteById(idIngrediente);

        List<IArticolo> articoli = prodottoFactory.filterByTipologiaEIngrediente(tipologiaProdotto, ingrediente);

        Object[][] mat = new Object[articoli.size()][5];

        for(int i = 0; i < articoli.size(); i++){
            setDirectoryFotoArticolo(articoli.get(i));
            mat[i][0] = articoli.get(i).getId();

            OriginalIcon originalIcon = new OriginalIcon(articoli.get(i).getDirectoryFoto().get(0));
            IconResizedDecorator iconResizedDecorator = new IconResizedDecorator(originalIcon, 80, 80);

            mat[i][1] = iconResizedDecorator.getImageIcon(); //per convenzione la prima va in tabella
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
            } else {
                System.out.println("La cartella è vuota.");
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
            case SCONTO_MENU -> {
                if (articolo instanceof Menu)
                    return ((Menu) articolo).getSconto();
                else
                    return null;
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
}