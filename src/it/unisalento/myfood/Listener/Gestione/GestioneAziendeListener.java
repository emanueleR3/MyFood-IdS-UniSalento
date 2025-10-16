package it.unisalento.myfood.Listener.Gestione;

import it.unisalento.myfood.Business.AziendaBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Menu.EditMenuDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.Panels.Gestione.GestioneAziendePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class GestioneAziendeListener implements ActionListener {

    public final static String TORNA_DASHBOARD_BTN = "TORNA_DASHBOARD_BTN";
    public final static String AGGIUNGI_BTN = "AGGIUNGI_BTN";
    public final static String VEDI_BTN = "VEDI_BTN";
    public final static String MODIFICA_BTN = "MODIFICA_BTN";
    public final static String RIMUOVI_BTN = "RIMUOVI_BTN";
    private static HashMap<String, Object> session;
    private AziendaBusiness aziendaBusiness;

    private GestioneAziendePanel gestioneAziendePanel;
    private Frame frame;


    public GestioneAziendeListener(GestioneAziendePanel gestioneAziendePanel, Frame frame) {
        this.gestioneAziendePanel = gestioneAziendePanel;
        this.frame = frame;
        session = UtenteBusiness.getSession();
        aziendaBusiness = AziendaBusiness.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case TORNA_DASHBOARD_BTN -> frame.showPanel(Frame.PANEL.DASHBOARD_ADMIN);
            case AGGIUNGI_BTN -> {
                session.put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.AGGIUNGI);
                frame.showPanel(Frame.PANEL.EDIT_AZIENDA);
            }
            case MODIFICA_BTN -> {
                Integer idAzienda = gestioneAziendePanel.getRowSelected();

                if (idAzienda != null) {
                    session.put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.MODIFICA);
                    aziendaBusiness.setSelectedAzienda(idAzienda);

                    frame.showPanel(Frame.PANEL.EDIT_AZIENDA);
                }

            }

            case RIMUOVI_BTN -> {
                Integer idAzienda = gestioneAziendePanel.getRowSelected();

                if (idAzienda != null) {
                    int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler rimuovere l'azienda selezionata?", "Rimuovi azienda", JOptionPane.YES_NO_CANCEL_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                        aziendaBusiness.removeAzienda(idAzienda);
                        JOptionPane.showMessageDialog(frame, aziendaBusiness.getMessage());

                        if (aziendaBusiness.isRemovedSuccessfully()) {
                            frame.showPanel(Frame.PANEL.GESTIONE_AZIENDE);
                        }
                    }
                }
            }

            case EditMenuDecorator.UNDO_MENU -> {
                if(aziendaBusiness.undo()) {
                    JOptionPane.showMessageDialog(frame, aziendaBusiness.getMessage());
                    frame.showPanel(Frame.PANEL.GESTIONE_AZIENDE);
                }
            }

            case EditMenuDecorator.REDO_MENU -> {
                if(aziendaBusiness.redo()) {
                    JOptionPane.showMessageDialog(frame, aziendaBusiness.getMessage());
                    frame.showPanel(Frame.PANEL.GESTIONE_AZIENDE);
                }
            }
        }
    }
}
