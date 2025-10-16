package it.unisalento.myfood.Listener;

import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrdinePanelListener implements ActionListener {
    Frame frame;
    public static final String BACK_TO_CATALOGUE_BTN = "BACK_TO_CATALOGUE_BTN";
    private UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();

    public OrdinePanelListener(Frame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case BACK_TO_CATALOGUE_BTN :
                if(utenteBusiness.isLoggedCliente())
                    frame.showPanel(Frame.PANEL.UTENTE_VIEW_ORDINI);
                else if (utenteBusiness.isLoggedCucina())
                    frame.showPanel(Frame.PANEL.DASHBOARD_CUCINA);
                else if(utenteBusiness.isLoggedAmministratore())
                    frame.showPanel(Frame.PANEL.GESTIONE_ORDINI);
                break;
        }
    }
}
