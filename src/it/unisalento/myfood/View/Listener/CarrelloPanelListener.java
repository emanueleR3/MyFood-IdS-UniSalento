package it.unisalento.myfood.View.Listener;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Business.MailSender;
import it.unisalento.myfood.Business.OrdineBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.*;
import it.unisalento.myfood.View.Frame;

import javax.mail.search.FromTerm;
import javax.swing.*;
import java.awt.*;
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
                    ordineBusiness.create();
                    if (ordineBusiness.isCreatedSuccessfully()) {
                        new PaymentFrame();
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
