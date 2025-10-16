package it.unisalento.myfood.Listener;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Business.OrdineBusiness;
import it.unisalento.myfood.View.*;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.View.Panels.CarrelloPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CarrelloPanelListener implements ActionListener {

    public final static String BACK_TO_CATALOGUE_BTN = "ABOUT_BTN";
    public final static String ORD_BTN = "ORD_BTN";
    public final static String MOD_BTN = "MOD_BTN";

    private Frame frame;
    private OrdineBusiness ordineBusiness = OrdineBusiness.getInstance();
    private ArticoloBusiness articoloBusiness = ArticoloBusiness.getInstance();
    private CarrelloPanel carrelloPanel;


    public CarrelloPanelListener(Frame frame, CarrelloPanel carrelloPanel) {
        this.frame = frame;
        this.carrelloPanel = carrelloPanel;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case BACK_TO_CATALOGUE_BTN :
                frame.showPanel(Frame.PANEL.DASHBOARD_CLIENTE);
                break;
            case ORD_BTN:
                int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler confermare l'ordine?", "Conferma ordine", JOptionPane.OK_CANCEL_OPTION);

                if(choice == JOptionPane.OK_OPTION) {
                    Integer idOrdineCreato = ordineBusiness.create();
                    if(idOrdineCreato != null) {
                        frame.showPanel(Frame.PANEL.CARRELLO);  //fa vedere il carrello svuotato
                        if (ordineBusiness.isCreatedSuccessfully()) {
                            new PaymentFrame(idOrdineCreato);
                        } else if (ordineBusiness.isError()) {
                            JOptionPane.showMessageDialog(frame, ordineBusiness.getMessage());
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(frame, "Il carrello è vuoto!");
                    }
                }
                break;
            case MOD_BTN:
                if(carrelloPanel.getArticoloSelezionato() != null){
                    articoloBusiness.setSelectedArticolo(carrelloPanel.getArticoloSelezionato());
                    new QuantityCartFrame(carrelloPanel, frame).openQuantityFrame();
                }
                break;
        }
    }
}
