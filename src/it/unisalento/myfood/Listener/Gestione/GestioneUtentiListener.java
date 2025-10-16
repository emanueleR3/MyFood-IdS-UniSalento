package it.unisalento.myfood.Listener.Gestione;

import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Menu.EditMenuDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.Panels.Gestione.GestioneUtentiPanel;
import it.unisalento.myfood.View.ViewModel.ListaUtentiTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class GestioneUtentiListener implements ActionListener {

    private GestioneUtentiPanel gestioneUtentiPanel;
    private Frame frame;
    private HashMap<String, Object> session;

    private static final UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();

    public final static String TORNA_DASHBOARD_BTN = "TORNA_DASHBOARD_BTN";
    public final static String AGGIUNGI_UTENTE_BTN = "AGGIUNGI_UTENTE_BTN";
    public final static String VEDI_UTENTE_BTN = "VEDI_UTENTE_BTN";
    public final static String RIMUOVI_UTENTE_BTN = "RIMUOVI_UTENTE_BTN";
    public final static String ABILITA_DISABILITA_UTENTE_BTN = "ABILITA_DISABILITA_UTENTE_BTN";
    public final static String RESET_PASSWORD_UTENTE_BTN = "RESET_PASSWORD_UTENTE_BTN";

    public GestioneUtentiListener(GestioneUtentiPanel gestioneUtentiPanel, Frame frame) {
        this.gestioneUtentiPanel = gestioneUtentiPanel;
        this.frame = frame;
        this.session = UtenteBusiness.getSession();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComboBox) {
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            String tipologia = ((String) comboBox.getSelectedItem()).toUpperCase();

            gestioneUtentiPanel.updateUITableUtente(ListaUtentiTableModel.FILTRA_PER.valueOf(tipologia));
        } else if (e.getSource() instanceof JButton){
            switch (e.getActionCommand()) {
                case TORNA_DASHBOARD_BTN -> frame.showPanel(Frame.PANEL.DASHBOARD_ADMIN);
                case AGGIUNGI_UTENTE_BTN -> frame.showPanel(Frame.PANEL.SIGNUP);
                case VEDI_UTENTE_BTN -> {
                    Integer idUtente = gestioneUtentiPanel.getUtenteSelezionato();
                    if(idUtente != null) {
                        utenteBusiness.setSelectedUtente(idUtente);
                        frame.showPanel(Frame.PANEL.EDIT_UTENTE);
                    }
                }
                case RIMUOVI_UTENTE_BTN -> {
                    Integer idUtente = gestioneUtentiPanel.getUtenteSelezionato();

                    if (idUtente != null) {
                        if (utenteBusiness.isIdOfAdminLogged(idUtente)) {
                            JOptionPane.showMessageDialog(frame, "Non è possibile auto-rimuoversi!");
                        } else {
                            int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler eliminare l'utente ID:" + idUtente + "?", "Elimina Utente", JOptionPane.YES_NO_CANCEL_OPTION);

                            if (choice == JOptionPane.YES_OPTION) {
                                utenteBusiness.removeUtente(idUtente);
                                if (utenteBusiness.isRemovedSuccessfully()) {
                                    gestioneUtentiPanel.updateUITableUtente(ListaUtentiTableModel.FILTRA_PER.CLIENTI);
                                }
                                JOptionPane.showMessageDialog(frame, utenteBusiness.getMessage());
                            }
                        }
                    }
                }
                case ABILITA_DISABILITA_UTENTE_BTN -> {

                    Integer idUtente = gestioneUtentiPanel.getUtenteSelezionato();
                    if (idUtente != null) {
                        int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler abilitare/disabilitare l'utente ID:" + idUtente + "?", "Abilita/disabilita Utente", JOptionPane.YES_NO_CANCEL_OPTION);

                        if (choice == JOptionPane.YES_OPTION) {

                            utenteBusiness.setSelectedUtente(idUtente);
                            if (!utenteBusiness.isSelectedUtenteCliente()) {
                                JOptionPane.showMessageDialog(frame, "Puoi effettuare questa operazione solo su un cliente!");
                                break;
                            }
                            utenteBusiness.setSelectedUtenteDisabilitato(!utenteBusiness.isSelectedUtenteDisabilitato());
                            JOptionPane.showMessageDialog(frame, utenteBusiness.getMessage());
                            gestioneUtentiPanel.updateUITableUtente(ListaUtentiTableModel.FILTRA_PER.CLIENTI);


                        }
                    }
                    }

                case RESET_PASSWORD_UTENTE_BTN -> {
                    Integer idUtente = gestioneUtentiPanel.getUtenteSelezionato();
                    if (idUtente != null) {

                        int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler effettuare il reset della password dell'utente ID:" + idUtente + "?", "Reset password Utente", JOptionPane.YES_NO_CANCEL_OPTION);

                        if (choice == JOptionPane.YES_OPTION) {
                            utenteBusiness.setSelectedUtente(idUtente);
                            utenteBusiness.resetPasswordSelectedUtente(true);
                            JOptionPane.showMessageDialog(frame, utenteBusiness.getMessage());
                            gestioneUtentiPanel.updateUITableUtente(ListaUtentiTableModel.FILTRA_PER.CLIENTI);

                        }
                    }
                }
            }
        }   else if(e.getSource() instanceof JMenuItem){
            switch (e.getActionCommand()){
                case EditMenuDecorator.UNDO_MENU -> {
                    if(utenteBusiness.undo()) {
                        JOptionPane.showMessageDialog(frame, utenteBusiness.getMessage());
                        frame.showPanel(Frame.PANEL.GESTIONE_UTENTI);
                    }
                }

                case EditMenuDecorator.REDO_MENU -> {
                    if(utenteBusiness.redo()) {
                        JOptionPane.showMessageDialog(frame, utenteBusiness.getMessage());
                        frame.showPanel(Frame.PANEL.GESTIONE_UTENTI);
                    }
                }
            }
        }


    }
}


