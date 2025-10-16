package it.unisalento.myfood.Listener.Gestione;


import it.unisalento.myfood.Business.CarrelloBusiness;
import it.unisalento.myfood.Business.OrdineBusiness;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.Panels.Gestione.GestioneOrdiniPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestioneOrdiniListener implements ActionListener {

    public final static String TORNA_DASHBOARD_BTN = "TORNA_DASHBOARD_BTN";
    public final static String AGGIUNGI_BTN = "AGGIUNGI_BTN";
    public final static String VEDI_BTN = "VEDI_BTN";
    public final static String MODIFICA_BTN = "MODIFICA_BTN";
    public final static String RIMUOVI_BTN = "RIMUOVI_BTN";

    private GestioneOrdiniPanel gestioneOrdiniPanel;
    private CarrelloBusiness carrelloBusiness = CarrelloBusiness.getInstance();
    private OrdineBusiness ordineBusiness = OrdineBusiness.getInstance();

    private Frame frame;
    private Integer idOrdine;

    public GestioneOrdiniListener(GestioneOrdiniPanel gestioneOrdiniPanel, Frame frame) {
        this.gestioneOrdiniPanel = gestioneOrdiniPanel;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() instanceof JComboBox) {
            switch (gestioneOrdiniPanel.getFilter()) {
                case ALL:
                    gestioneOrdiniPanel.filter(GestioneOrdiniPanel.FILTERS.ALL);
                    break;
                case NON_PAGATI:
                    gestioneOrdiniPanel.filter(GestioneOrdiniPanel.FILTERS.NON_PAGATI);
                    break;
                case IN_LAVORAZIONE:
                    gestioneOrdiniPanel.filter(GestioneOrdiniPanel.FILTERS.IN_LAVORAZIONE);
                    break;
                case CONSEGNATI:
                    gestioneOrdiniPanel.filter(GestioneOrdiniPanel.FILTERS.CONSEGNATI);
                    break;
                case PAGATI:
                    gestioneOrdiniPanel.filter(GestioneOrdiniPanel.FILTERS.PAGATI);
                    break;
            }
        } else if (e.getSource() instanceof JButton) {

            switch (e.getActionCommand()) {
                case TORNA_DASHBOARD_BTN:
                    frame.showPanel(Frame.PANEL.DASHBOARD_ADMIN);
                    break;
                case VEDI_BTN:
                    idOrdine = gestioneOrdiniPanel.getOrdineSelezionato();
                    if(idOrdine != null) {
                        ordineBusiness.setSelectedOrdine(idOrdine);
                        frame.showPanel(Frame.PANEL.DETT_ORDINI);
                    }
                    break;
                case RIMUOVI_BTN:
                    idOrdine = gestioneOrdiniPanel.getOrdineSelezionato();
                    if(idOrdine != null) {
                        int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler rimuovere l'ordine selezionato?", "Rimuovi ordine", JOptionPane.YES_NO_CANCEL_OPTION);

                        if (choice == JOptionPane.YES_OPTION) {
                            ordineBusiness.removeOrdine(idOrdine);

                            JOptionPane.showMessageDialog(frame, ordineBusiness.getMessage());

                            if (ordineBusiness.isRemovedSuccessfully())
                                frame.showPanel(Frame.PANEL.GESTIONE_ORDINI);
                        }
                    }
            }
        }
    }
}
