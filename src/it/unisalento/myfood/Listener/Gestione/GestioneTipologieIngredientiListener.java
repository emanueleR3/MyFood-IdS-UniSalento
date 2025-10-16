package it.unisalento.myfood.Listener.Gestione;

import it.unisalento.myfood.Business.TipologiaIngredienteBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Menu.EditMenuDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.Panels.Gestione.GestioneTipologieIngredientiPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestioneTipologieIngredientiListener implements ActionListener {

    private GestioneTipologieIngredientiPanel gestioneTipologieIngredientiPanel;
    private Frame frame;
    public final static String TORNA_DASHBOARD_BTN = "TORNA_DASHBOARD_BTN";
    public final static String AGGIUNGI_BTN = "AGGIUNGI_BTN";
    public final static String VEDI_BTN = "VEDI_BTN";
    public final static String MODIFICA_BTN = "MODIFICA_BTN";
    public final static String RIMUOVI_BTN = "RIMUOVI_BTN";
    private TipologiaIngredienteBusiness tipologiaIngredienteBusiness;

    public GestioneTipologieIngredientiListener(GestioneTipologieIngredientiPanel gestioneTipologieIngredientiPanel, Frame frame) {
        this.gestioneTipologieIngredientiPanel = gestioneTipologieIngredientiPanel;
        this.frame = frame;
        tipologiaIngredienteBusiness = TipologiaIngredienteBusiness.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case TORNA_DASHBOARD_BTN -> frame.showPanel(Frame.PANEL.DASHBOARD_ADMIN);
            case AGGIUNGI_BTN -> {
                UtenteBusiness.getSession().put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.AGGIUNGI);
                frame.showPanel(Frame.PANEL.EDIT_TIP_ING);
            }
            case MODIFICA_BTN -> {

                Integer idTipologia = gestioneTipologieIngredientiPanel.getRowSelected();

                if(idTipologia != null){
                    UtenteBusiness.getSession().put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.MODIFICA);
                    tipologiaIngredienteBusiness.setSelectedTipologia(idTipologia);

                    frame.showPanel(Frame.PANEL.EDIT_TIP_ING);
                }

            }

            case RIMUOVI_BTN -> {
                Integer idTipologia = gestioneTipologieIngredientiPanel.getRowSelected();
                if(idTipologia != null) {
                    int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler rimuovere la tipologia selezionata?", "Rimuovi tipologia ingredienti", JOptionPane.YES_NO_CANCEL_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                        tipologiaIngredienteBusiness.removeTipologia(idTipologia);
                        JOptionPane.showMessageDialog(frame, tipologiaIngredienteBusiness.getMessage());

                        if (tipologiaIngredienteBusiness.isRemovedSuccessfully())
                            frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_INGREDIENTI);
                    }
                }
            }

            case EditMenuDecorator.UNDO_MENU -> {
                if(tipologiaIngredienteBusiness.undo()) {
                    JOptionPane.showMessageDialog(frame, tipologiaIngredienteBusiness.getMessage());
                    frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_INGREDIENTI);
                }
            }

            case EditMenuDecorator.REDO_MENU -> {
                if(tipologiaIngredienteBusiness.redo()) {
                    JOptionPane.showMessageDialog(frame, tipologiaIngredienteBusiness.getMessage());
                    frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_INGREDIENTI);
                }
            }
        }
    }
}
