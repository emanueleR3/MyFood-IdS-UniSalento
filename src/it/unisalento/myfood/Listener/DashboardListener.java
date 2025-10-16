package it.unisalento.myfood.Listener;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Business.OrdineBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.*;
import it.unisalento.myfood.View.Panels.DashboardCliente;
import it.unisalento.myfood.View.Panels.DashboardCucina;
import it.unisalento.myfood.View.Panels.IDashboard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class DashboardListener implements ActionListener {
    private IDashboard panel;
    private Frame frame;
    private static final ArticoloBusiness articoloBusiness = ArticoloBusiness.getInstance();
    private HashMap<String, Object> session;

    public static final String ADD_TO_CART_BTN = "ADD_TO_CART_BTN";
    public static final String LOGOUT_BTN = "LOGOUT_BTN";
    public static final String VEDI_CARRELLO_BTN = "VEDI_CARRELLO_BTN";
    public static final String VEDI_ORDINI_CLIENTE_BTN = "VEDI_ORDINI_CLIENTE_BTN";
    public static final String VEDI_ORDINE_CUCINA_BTN = "VEDI_ORDINE_CUCINA_BTN";
    public static final String VEDI_ARTICOLO_BTN = "VEDI_ARTICOLO_BTN";
    public static final String INIZIA_LAVORAZIONE_BTN = "INIZIA_LAVORAZIONE_BTN";
    public static final String ORDINE_COMPLETATO_BTN = "ORDINE_COMPLETATO_BTN";
    public static final String GESTISCI_ORDINI_BTN = "GESTISCI_ORDINI_BTN";
    public static final String GESTISCI_COMMENTI_BTN = "GESTISCI_COMMENTI_BTN";
    public static final String GESTISCI_ARTICOLI_BTN = "GESTISCI_ARTICOLI_BTM";
    public static final String GESTISCI_INGREDIENTI_BTN = "GESTISCI_INGREDIENTI_BTN";
    public static final String GESTISCI_UTENTI_BTN = "GESTISCI_UTENTI_BTN";
    public static final String GESTISCI_AZIENDE_BTN = "GESTISCI_AZIENDE_BTN";
    public static final String GESTISCI_TIP_PRODOTTI_BTN = "GESTISCI_TIP_PRODOTTI_BTN";
    public static final String GESTISCI_TIP_INGREDIENTI_BTN = "GESTISCI_TIP_INGREDIENTI_BTN";
    public static final String TIP_COMBOBOX = "TIP_COMBOBOX";
    public static final String ING_COMBOBOX = "ING_COMBOBOX";

    public DashboardListener(IDashboard panel, Frame frame){
        this.panel = panel;
        this.frame = frame;
        session = UtenteBusiness.getSession();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComboBox) {
            String tipologiaProdotto = ((DashboardCliente) panel).getSelectedTipologiaProdotto();
            String tipologiaIngediente = ((DashboardCliente) panel).getSelectedTipologiaIngrediente();
            ((DashboardCliente) panel).loadCatalogo(tipologiaProdotto, tipologiaIngediente);

        } else if (e.getSource() instanceof JButton){
            switch (e.getActionCommand()) {
                case LOGOUT_BTN:
                    session.remove(UtenteBusiness.LOGGED_IN_USER_ID);
                    frame.showPanel(Frame.PANEL.LOGIN);
                    break;
                case VEDI_ARTICOLO_BTN:
                    if(((DashboardCliente) panel).getRowSelected() != null) {
                        articoloBusiness.setSelectedArticolo(((DashboardCliente) panel).getRowSelected());
                        frame.showPanel(Frame.PANEL.ARTICOLO);
                    }
                    break;
                case ADD_TO_CART_BTN:
                    if(((DashboardCliente) panel).getRowSelected() != null) {
                        articoloBusiness.setSelectedArticolo(((DashboardCliente) panel).getRowSelected());
                        if ((int) articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.DISPONIBILITA) == 0) {
                            JOptionPane.showMessageDialog(frame, "Disponibilità esaurita per questo articolo!");
                        } else {
                            new QuantityCartFrame().openQuantityFrame();
                        }
                    }
                    break;
                case VEDI_CARRELLO_BTN:
                    frame.showPanel(Frame.PANEL.CARRELLO);
                    break;
                case VEDI_ORDINI_CLIENTE_BTN:
                   frame.showPanel(Frame.PANEL.UTENTE_VIEW_ORDINI);
                    break;
                case VEDI_ORDINE_CUCINA_BTN:
                    if(((DashboardCucina) panel).getRowSelected() != null) {
                        OrdineBusiness.getInstance().setSelectedOrdine(((DashboardCucina) panel).getRowSelected());
                        frame.showPanel(Frame.PANEL.DETT_ORDINI);
                    }
                    break;
                case INIZIA_LAVORAZIONE_BTN:
                    if(((DashboardCucina) panel).getRowSelected() != null) {
                        OrdineBusiness.getInstance().setSelectedOrdineInLavorazione(((DashboardCucina) panel).getRowSelected());
                        ((DashboardCucina) panel).updateUITableOrdini();
                    }
                    break;
                case ORDINE_COMPLETATO_BTN:
                    if(((DashboardCucina) panel).getRowSelected() != null) {
                        if (OrdineBusiness.getInstance().isOrdineInLavorazione(((DashboardCucina) panel).getRowSelected())) {
                            OrdineBusiness.getInstance().setSelectedOrdineCompletato(((DashboardCucina) panel).getRowSelected());
                            OrdineBusiness.getInstance().sendEmailOrdineCompletato(((DashboardCucina) panel).getRowSelected());
                            ((DashboardCucina) panel).updateUITableOrdini();
                        } else {
                            JOptionPane.showMessageDialog(frame, "L'ordine non è ancora in lavorazione!");
                        }
                    }
                    break;
                case GESTISCI_UTENTI_BTN :
                    frame.showPanel(Frame.PANEL.GESTIONE_UTENTI);
                    break;
                case GESTISCI_ARTICOLI_BTN:
                    frame.showPanel(Frame.PANEL.GESTIONE_ARTICOLI);
                    break;
                case GESTISCI_INGREDIENTI_BTN:
                    frame.showPanel(Frame.PANEL.GESTIONE_INGREDIENTI);
                    break;
                case GESTISCI_AZIENDE_BTN:
                    frame.showPanel(Frame.PANEL.GESTIONE_AZIENDE);
                    break;
                case GESTISCI_COMMENTI_BTN:
                    frame.showPanel(Frame.PANEL.GESTIONE_COMMENTI);
                    break;
                case GESTISCI_TIP_PRODOTTI_BTN:
                    frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_PRODOTTI);
                    break;
                case GESTISCI_TIP_INGREDIENTI_BTN:
                    frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_INGREDIENTI);
                    break;
                case GESTISCI_ORDINI_BTN:
                    frame.showPanel(Frame.PANEL.GESTIONE_ORDINI);
                    break;
            }
        }
    }
}
