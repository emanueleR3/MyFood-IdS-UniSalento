package it.unisalento.myfood.Listener.Gestione;

import it.unisalento.myfood.Business.TipologiaProdottoBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Menu.EditMenuDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.Panels.Gestione.GestioneTipologieProdottiPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestioneTipologieProdottiListener implements ActionListener {

    private GestioneTipologieProdottiPanel gestioneTipologieProdottiPanel;
    private Frame frame;
    public final static String TORNA_DASHBOARD_BTN = "TORNA_DASHBOARD_BTN";
    public final static String AGGIUNGI_BTN = "AGGIUNGI_BTN";
    public final static String VEDI_BTN = "VEDI_BTN";
    public final static String MODIFICA_BTN = "MODIFICA_BTN";
    public final static String RIMUOVI_BTN = "RIMUOVI_BTN";
    private TipologiaProdottoBusiness tipologiaProdottoBusiness;

    public GestioneTipologieProdottiListener(GestioneTipologieProdottiPanel gestioneTipologieProdottiPanel, Frame frame) {
        this.gestioneTipologieProdottiPanel = gestioneTipologieProdottiPanel;
        this.frame = frame;
        tipologiaProdottoBusiness = TipologiaProdottoBusiness.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case TORNA_DASHBOARD_BTN -> frame.showPanel(Frame.PANEL.DASHBOARD_ADMIN);
            case AGGIUNGI_BTN -> {
                UtenteBusiness.getSession().put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.AGGIUNGI);
                frame.showPanel(Frame.PANEL.EDIT_TIP_PROD);
            }
            case MODIFICA_BTN -> {

                Integer idTipologia = gestioneTipologieProdottiPanel.getRowSelected();

                if(idTipologia != null){
                    UtenteBusiness.getSession().put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.MODIFICA);
                    tipologiaProdottoBusiness.setSelectedTipologia(idTipologia);

                    frame.showPanel(Frame.PANEL.EDIT_TIP_PROD);
                }
            }
            case RIMUOVI_BTN -> {
                Integer idTipologia = gestioneTipologieProdottiPanel.getRowSelected();

                if (idTipologia != null) {
                    int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler rimuovere la tipologia prodotto selezionata?", "Rimuovi tipologia prodotto", JOptionPane.YES_NO_CANCEL_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                        tipologiaProdottoBusiness.removeTipologia(idTipologia);
                        JOptionPane.showMessageDialog(frame, tipologiaProdottoBusiness.getMessage());
                        if (tipologiaProdottoBusiness.isRemovedSuccessfully())
                            frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_PRODOTTI);
                    }

                }
            }


            case EditMenuDecorator.UNDO_MENU -> {
                if(tipologiaProdottoBusiness.undo()) {
                    JOptionPane.showMessageDialog(frame, tipologiaProdottoBusiness.getMessage());
                    frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_PRODOTTI);
                }
            }

            case EditMenuDecorator.REDO_MENU -> {
                if(tipologiaProdottoBusiness.redo()) {
                    JOptionPane.showMessageDialog(frame, tipologiaProdottoBusiness.getMessage());
                    frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_PRODOTTI);
                }
            }
        }
    }
}
