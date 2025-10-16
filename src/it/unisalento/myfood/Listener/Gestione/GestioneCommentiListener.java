package it.unisalento.myfood.Listener.Gestione;

import it.unisalento.myfood.Business.InterazioneUtenteBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Menu.EditMenuDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.Panels.Gestione.GestioneCommentiPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestioneCommentiListener implements ActionListener {

    private GestioneCommentiPanel gestioneCommentiPanel;
    private Frame frame;
    public final static String TORNA_DASHBOARD_BTN = "TORNA_DASHBOARD_BTN";
    public final static String AGGIUNGI_BTN = "AGGIUNGI_BTN";
    public final static String VEDI_BTN = "VEDI_BTN";
    public final static String RIMUOVI_RISPOSTA_BTN = "RIMUOVI_RISPOSTA_BTN";
    public final static String RIMUOVI_COMMENTO_BTN = "RIMUOVI_COMMENTO_BTN";
    private InterazioneUtenteBusiness interazioneUtenteBusiness;

    public GestioneCommentiListener(GestioneCommentiPanel gestioneCommentiPanel, Frame frame) {
        this.gestioneCommentiPanel = gestioneCommentiPanel;
        this.frame = frame;
        interazioneUtenteBusiness = InterazioneUtenteBusiness.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JComboBox<?>){

            gestioneCommentiPanel.loadCatalogo(gestioneCommentiPanel.getFilter());



        } else if (e.getSource() instanceof JButton) {
            switch (e.getActionCommand()) {
                case TORNA_DASHBOARD_BTN -> frame.showPanel(Frame.PANEL.DASHBOARD_ADMIN);
                case AGGIUNGI_BTN -> {
                    Integer idCommento = gestioneCommentiPanel.getSelectedCommento();

                    InterazioneUtenteBusiness.getInstance().setSelectedCommento(idCommento);

                    if (idCommento != null) {
                        UtenteBusiness.getSession().put(UtenteBusiness.OPERATION, UtenteBusiness.OPERATIONS.AGGIUNGI);
                        interazioneUtenteBusiness.setSelectedCommento(idCommento);
                        frame.showPanel(Frame.PANEL.EDIT_RIPSOSTA);
                    }
                }
                case RIMUOVI_RISPOSTA_BTN -> {
                    Integer idRisposta = gestioneCommentiPanel.getSelectedRisposta();
                    if (idRisposta != null) {
                        int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler rimuovere la riposta selezionata?", "Rimuovi riposta" , JOptionPane.YES_NO_CANCEL_OPTION );

                        if(choice == JOptionPane.YES_OPTION) {
                            interazioneUtenteBusiness.removeRiposta(idRisposta);

                            JOptionPane.showMessageDialog(frame, interazioneUtenteBusiness.getMessage());
                            if (interazioneUtenteBusiness.isRemovedSuccessfully())
                                frame.showPanel(Frame.PANEL.GESTIONE_COMMENTI);
                        }
                    }
                }
                case RIMUOVI_COMMENTO_BTN -> {
                    Integer idCommento = gestioneCommentiPanel.getSelectedCommento();

                    if(idCommento != null) {
                        int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler rimuovere il commento selezionato?", "Rimuovi commento", JOptionPane.YES_NO_CANCEL_OPTION);

                        if (choice == JOptionPane.YES_OPTION) {
                            interazioneUtenteBusiness.removeCommento(idCommento);

                            JOptionPane.showMessageDialog(frame, interazioneUtenteBusiness.getMessage());
                            if (interazioneUtenteBusiness.isRemovedSuccessfully())
                                frame.showPanel(Frame.PANEL.GESTIONE_COMMENTI);
                        }
                    }
                }

            }
        }

        else if(e.getSource() instanceof JMenuItem){
            switch (e.getActionCommand()){
                case EditMenuDecorator.UNDO_MENU -> {
                    if(interazioneUtenteBusiness.undo()) {
                        JOptionPane.showMessageDialog(frame, interazioneUtenteBusiness.getMessage());
                        frame.showPanel(Frame.PANEL.GESTIONE_COMMENTI);
                    }
                }

                case EditMenuDecorator.REDO_MENU -> {
                    if(interazioneUtenteBusiness.redo()) {
                        JOptionPane.showMessageDialog(frame, interazioneUtenteBusiness.getMessage());
                        frame.showPanel(Frame.PANEL.GESTIONE_COMMENTI);
                    }
                }
            }
        }
    }
}
