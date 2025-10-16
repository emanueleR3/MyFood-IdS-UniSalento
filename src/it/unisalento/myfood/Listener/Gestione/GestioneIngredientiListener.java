package it.unisalento.myfood.Listener.Gestione;

import it.unisalento.myfood.Business.IngredienteBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Menu.EditMenuDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.Panels.Gestione.GestioneIngredientiPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class GestioneIngredientiListener implements ActionListener {

    private GestioneIngredientiPanel gestioneIngredientiPanel;
    private Frame frame;
    public final static String TORNA_DASHBOARD_BTN = "TORNA_DASHBOARD_BTN";
    public final static String AGGIUNGI_BTN = "AGGIUNGI_BTN";
    public final static String VEDI_BTN = "VEDI_BTN";
    public final static String MODIFICA_BTN = "MODIFICA_BTN";
    public final static String RIMUOVI_BTN = "RIMUOVI_BTN";
    private static HashMap<String, Object> session;
    private IngredienteBusiness ingredienteBusiness = IngredienteBusiness.getInstance();

    public GestioneIngredientiListener(GestioneIngredientiPanel gestioneIngredientiPanel, Frame frame) {
        this.gestioneIngredientiPanel = gestioneIngredientiPanel;
        this.frame = frame;
        session = UtenteBusiness.getSession();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case TORNA_DASHBOARD_BTN -> frame.showPanel(Frame.PANEL.DASHBOARD_ADMIN);
            case AGGIUNGI_BTN -> {
                session.put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.AGGIUNGI);
                frame.showPanel(Frame.PANEL.EDIT_INGREDIENTE);
            }
            case MODIFICA_BTN -> {
                Integer idIngrediente = gestioneIngredientiPanel.getRowSelected();

                if (idIngrediente != null) {
                    session.put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.MODIFICA);
                    ingredienteBusiness.setSelectedIngrediente(idIngrediente);
                    frame.showPanel(Frame.PANEL.EDIT_INGREDIENTE);
                }

            }

            case RIMUOVI_BTN -> {
                Integer idIngrediente = gestioneIngredientiPanel.getRowSelected();

                if(idIngrediente != null) {
                    int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler rimuovere l'ingrediente selezionato?", "Rimuovi ingrediente", JOptionPane.YES_NO_CANCEL_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                        ingredienteBusiness.removeIngrediente(idIngrediente);

                        if (ingredienteBusiness.isRemovedSuccessfully())
                            frame.showPanel(Frame.PANEL.GESTIONE_INGREDIENTI);
                    }
                }
            }

            case EditMenuDecorator.UNDO_MENU -> {
                if(ingredienteBusiness.undo()) {
                    JOptionPane.showMessageDialog(frame, ingredienteBusiness.getMessage());
                    frame.showPanel(Frame.PANEL.GESTIONE_INGREDIENTI);
                }
            }

            case EditMenuDecorator.REDO_MENU -> {
                if(ingredienteBusiness.redo()) {
                    JOptionPane.showMessageDialog(frame, ingredienteBusiness.getMessage());
                    frame.showPanel(Frame.PANEL.GESTIONE_INGREDIENTI);
                }
            }

            case VEDI_BTN -> {
                Integer idIngrediente = gestioneIngredientiPanel.getRowSelected();

                if(idIngrediente != null) {
                    UtenteBusiness.getSession().put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.VISUALIZZA);
                    ingredienteBusiness.setSelectedIngrediente(idIngrediente);
                    frame.showPanel(Frame.PANEL.EDIT_INGREDIENTE);
                }
            }
        }
    }
}
