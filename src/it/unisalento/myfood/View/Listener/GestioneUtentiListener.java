package it.unisalento.myfood.View.Listener;

import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.DashboardAmministratore;
import it.unisalento.myfood.View.DashboardCliente;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.GestioneUtentiPanel;
import it.unisalento.myfood.View.ViewModel.ListaUtentiTableModel;
import it.unisalento.myfood.model.Utente;

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
    public final static String MODIFICA_UTENTE_BTN = "MODIFICA_UTENTE_BTN";
    public final static String RIMUOVI_UTENTE_BTN = "RIMUOVI_UTENTE_BTN";
    public final static String ABILITA_DISABILITA_UTENTE_BTN = "ABILITA_DISABILITA_UTENTE_BTN";

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
                case AGGIUNGI_UTENTE_BTN -> JOptionPane.showMessageDialog(frame, "Va fatta la schermata AggiungiUtentePanel");
                case VEDI_UTENTE_BTN -> JOptionPane.showMessageDialog(frame, "Va fatta la schermata UtentePanel");
                case MODIFICA_UTENTE_BTN -> JOptionPane.showMessageDialog(frame, "Va abilitata la modifica di UtentePanel");
                case RIMUOVI_UTENTE_BTN -> JOptionPane.showMessageDialog(frame, "Deve aprire una finestra e dire \"Si è sicuri di eliminare l'utente?\"");
                case ABILITA_DISABILITA_UTENTE_BTN -> {
                    if (gestioneUtentiPanel.getUtenteSelezionato() != null) {
                        utenteBusiness.setSelectedUtente(gestioneUtentiPanel.getUtenteSelezionato());
                        if (!utenteBusiness.isViewUtenteCliente()) {
                            JOptionPane.showMessageDialog(frame, "Puoi effettuare questa operazione solo su un cliente!");
                            break;
                        }
                        utenteBusiness.setViewUtenteDisabilitato(!utenteBusiness.isViewUtenteDisabilitato());
                        gestioneUtentiPanel.updateUITableUtente(ListaUtentiTableModel.FILTRA_PER.CLIENTI);
                    }
                }
            }
        }
    }
}
