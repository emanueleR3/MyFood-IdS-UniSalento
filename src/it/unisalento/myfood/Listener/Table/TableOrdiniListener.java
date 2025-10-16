package it.unisalento.myfood.Listener.Table;

import it.unisalento.myfood.Business.OrdineBusiness;
import it.unisalento.myfood.View.Panels.OrdiniClientePanel;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TableOrdiniListener implements ListSelectionListener {

    private OrdiniClientePanel ordiniClientePanel;
    private OrdineBusiness ordineBusiness = OrdineBusiness.getInstance();

    public TableOrdiniListener(OrdiniClientePanel ordiniClientePanel) {
        this.ordiniClientePanel = ordiniClientePanel;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

        if (!e.getValueIsAdjusting()) {   //se la selezione non è ancora in corso
            Integer idOrdine = ordiniClientePanel.getOrdineSelezionato();

             if(!ordineBusiness.isOrdineRicorrente(idOrdine)){
                ordiniClientePanel.setRicorrButtonText("Imposta come ricorrente");
            } else {
                ordiniClientePanel.setRicorrButtonText("Rimuovi dai ricorrenti");
            }
        }
    }
}
