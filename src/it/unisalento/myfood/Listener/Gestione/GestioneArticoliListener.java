package it.unisalento.myfood.Listener.Gestione;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.DAO.IngredienteDAO;
import it.unisalento.myfood.DAO.TipologiaIngredienteDAO;
import it.unisalento.myfood.DAO.TipologiaProdottoDAO;
import it.unisalento.myfood.View.Decorator.Menu.EditMenuDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.Panels.Gestione.GestioneArticoliPanel;
import it.unisalento.myfood.model.TipologiaProdotto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class GestioneArticoliListener implements ActionListener {

    private GestioneArticoliPanel gestioneArticoliPanel;
    private Frame frame;
    private HashMap<String, Object> session;

    private static final UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();

    private ArticoloBusiness articoloBusiness;

    public final static String TORNA_DASHBOARD_BTN = "TORNA_DASHBOARD_BTN";
    public final static String AGGIUNGI_PROD_BTN = "AGGIUNGI_PROD_BTN";
    public final static String AGGIUNGI_MENU_BTN = "AGGIUNGI_MENU_BTN";
    public final static String VEDI_BTN = "VEDI_BTN";
    public final static String MODIFICA_BTN = "MODIFICA_BTN";
    public final static String RIMUOVI_BTN = "RIMUOVI_BTN";


    public GestioneArticoliListener(GestioneArticoliPanel gestioneArticoliPanel, Frame frame) {
        this.gestioneArticoliPanel = gestioneArticoliPanel;
        this.frame = frame;
        this.session = UtenteBusiness.getSession();
        articoloBusiness = ArticoloBusiness.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() instanceof JComboBox) {
            String tipologiaProdotto = gestioneArticoliPanel.getSelectedTipologiaProdotto();
            String tipologiaIngediente = gestioneArticoliPanel.getSelectedTipologiaIngrediente();
            gestioneArticoliPanel.loadCatalogo(tipologiaProdotto, tipologiaIngediente);

        } else if (e.getSource() instanceof JButton) {
            switch (e.getActionCommand()) {
                case TORNA_DASHBOARD_BTN -> frame.showPanel(Frame.PANEL.DASHBOARD_ADMIN);
                case AGGIUNGI_PROD_BTN -> {
                    if (TipologiaProdottoDAO.getInstance().loadTipologia().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Non ci sono tipologie prodotto salvate.\nPrima inseriscine una!");
                        return;
                    }
                    if (IngredienteDAO.getInstance().findAll().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Non ci sono ingredienti salvati.\nPrima inseriscine uno!");
                        return;
                    }

                    session.put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.AGGIUNGI);
                    frame.showPanel(Frame.PANEL.EDIT_PRODOTTO);
                }
                case AGGIUNGI_MENU_BTN -> {


                    session.put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.AGGIUNGI);
                    frame.showPanel(Frame.PANEL.EDIT_MENU);
                }
                case MODIFICA_BTN -> {
                    Integer idArticolo = gestioneArticoliPanel.getRowSelected();

                    if (idArticolo != null) {
                        articoloBusiness.setSelectedArticolo(idArticolo);

                        session.put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.MODIFICA);


                        if (articoloBusiness.isProdotto(idArticolo)) {
                            frame.showPanel(Frame.PANEL.EDIT_PRODOTTO);

                        } else if (articoloBusiness.isMenu(idArticolo)) {
                            frame.showPanel(Frame.PANEL.EDIT_MENU);
                        }
                    }
                }

                case VEDI_BTN -> {
                    Integer idArticolo = gestioneArticoliPanel.getRowSelected();

                    if(idArticolo != null) {
                        articoloBusiness.setSelectedArticolo(idArticolo);
                        session.put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.VISUALIZZA);

                        if (articoloBusiness.isProdotto(idArticolo)) {
                            frame.showPanel(Frame.PANEL.EDIT_PRODOTTO);

                        } else if (articoloBusiness.isMenu(idArticolo)) {
                            frame.showPanel(Frame.PANEL.EDIT_MENU);
                        }
                    }
                }


                case RIMUOVI_BTN -> {
                    Integer idArticolo = gestioneArticoliPanel.getRowSelected();

                    if(idArticolo != null) {
                        int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler rimuovere l'articolo selezionato?", "Rimuovi articolo", JOptionPane.YES_NO_CANCEL_OPTION);

                        if (choice == JOptionPane.YES_OPTION) {
                            articoloBusiness.removeArticolo(idArticolo);

                            JOptionPane.showMessageDialog(frame, articoloBusiness.getMessage());
                            if (articoloBusiness.isRemovedSuccessfully())
                                frame.showPanel(Frame.PANEL.GESTIONE_ARTICOLI);

                        }
                    }
                }
            }
        } else if (e.getSource() instanceof JMenuItem){
            switch (e.getActionCommand()){
                case EditMenuDecorator.UNDO_MENU -> {
                    if(articoloBusiness.undo()){
                        JOptionPane.showMessageDialog(frame, articoloBusiness.getMessage());
                        frame.showPanel(Frame.PANEL.GESTIONE_ARTICOLI);
                    }
                }

                case EditMenuDecorator.REDO_MENU -> {
                    if(articoloBusiness.redo()){
                        JOptionPane.showMessageDialog(frame, articoloBusiness.getMessage());
                        frame.showPanel(Frame.PANEL.GESTIONE_ARTICOLI);
                    }
                }
            }
        }
    }
}
