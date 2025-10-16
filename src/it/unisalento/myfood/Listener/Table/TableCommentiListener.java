package it.unisalento.myfood.Listener.Table;

import it.unisalento.myfood.Business.InterazioneUtenteBusiness;
import it.unisalento.myfood.View.Panels.Gestione.GestioneCommentiPanel;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TableCommentiListener implements ListSelectionListener {

    private GestioneCommentiPanel gestioneCommentiPanel;
    private InterazioneUtenteBusiness interazioneUtenteBusiness = InterazioneUtenteBusiness.getInstance();

    public TableCommentiListener(GestioneCommentiPanel gestioneCommentiPanel) {
        this.gestioneCommentiPanel = gestioneCommentiPanel;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

        if (!e.getValueIsAdjusting()) {   //se la selezione non è ancora in corso
            gestioneCommentiPanel.showRisposte();

        }
    }
}
