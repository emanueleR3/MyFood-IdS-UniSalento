package it.unisalento.myfood.Listener;


import it.unisalento.myfood.Business.CarrelloBusiness;
import it.unisalento.myfood.Business.OrdineBusiness;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.Panels.OrdiniClientePanel;
import it.unisalento.myfood.View.PaymentFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrdiniClienteListener implements ActionListener {

    public static final String BACK_TO_CATALOGUE_BTN = "BACK_TO_CATALOGUE_BTN";
    public static final String VIEW_DETT_ORD = "VIEW_DETT_ORD";
    public static final String LOAD_CARR = "LOAD_CARR";
    public static final String PAY_ORD = "PAY_ORD";
    public static final String SET_UNSET_RICOR = "SET_UNSET_RICOR";

    private OrdiniClientePanel ordiniClientePanel;
    private CarrelloBusiness carrelloBusiness = CarrelloBusiness.getInstance();
    private OrdineBusiness ordineBusiness = OrdineBusiness.getInstance();

    private Frame frame;
    private Integer idOrdine;

    public OrdiniClienteListener(Frame frame, OrdiniClientePanel ordiniClientePanel) {
        this.frame = frame;
        this.ordiniClientePanel = ordiniClientePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() instanceof JComboBox) {
            switch (ordiniClientePanel.getFilter()) {
                case ALL:
                    ordiniClientePanel.filter(OrdiniClientePanel.FILTERS.ALL);
                    break;
                case RICORRENTI:
                    ordiniClientePanel.filter(OrdiniClientePanel.FILTERS.RICORRENTI);
                    break;
                case NON_PAGATI:
                    ordiniClientePanel.filter(OrdiniClientePanel.FILTERS.NON_PAGATI);
                    break;
            }
        } else if (e.getSource() instanceof JButton) {

            switch (e.getActionCommand()) {
                case BACK_TO_CATALOGUE_BTN:
                    frame.showPanel(Frame.PANEL.DASHBOARD_CLIENTE);
                    break;
                case VIEW_DETT_ORD:
                    idOrdine = ordiniClientePanel.getOrdineSelezionato();
                    if(idOrdine != null) {
                        ordineBusiness.setSelectedOrdine(idOrdine);
                        frame.showPanel(Frame.PANEL.DETT_ORDINI);
                    }
                    break;
                case PAY_ORD:
                    idOrdine = ordiniClientePanel.getOrdineSelezionato();
                    if (idOrdine != null) {
                        ordineBusiness.setSelectedOrdine(idOrdine);
                        new PaymentFrame(idOrdine);
                        frame.showPanel(Frame.PANEL.UTENTE_VIEW_ORDINI);
                    }
                    break;
                case LOAD_CARR:
                    idOrdine = ordiniClientePanel.getOrdineSelezionato();
                    if (idOrdine != null) {

                        int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler sovrascrivere il carrello ?", "Attenzione", JOptionPane.OK_CANCEL_OPTION);

                        if (choice == JOptionPane.OK_OPTION) {
                            carrelloBusiness.emptyCart();
                            carrelloBusiness.addArticoliFromOrdine(idOrdine);
                            JOptionPane.showMessageDialog(frame, carrelloBusiness.getMessage());

                            if (carrelloBusiness.isAddeddSuccessfully()) {
                                frame.showPanel(Frame.PANEL.CARRELLO);
                            }
                        }
                    }
                        break;
                    case SET_UNSET_RICOR:
                        idOrdine = ordiniClientePanel.getOrdineSelezionato();
                        if(idOrdine != null) {
                            ordineBusiness.setOrdineRicorrente(idOrdine, !ordineBusiness.isOrdineRicorrente(idOrdine));
                            if (ordineBusiness.isEditedSuccessfully()) {
                                frame.showPanel(Frame.PANEL.UTENTE_VIEW_ORDINI);
                                JOptionPane.showMessageDialog(frame, ordineBusiness.getMessage());
                            }
                        }
                        break;

            }
        }
    }
}
