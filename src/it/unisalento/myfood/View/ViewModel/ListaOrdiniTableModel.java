package it.unisalento.myfood.View.ViewModel;

import it.unisalento.myfood.Business.UtenteBusiness;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class ListaOrdiniTableModel extends AbstractTableModel {

    private Object[][] ordini;
    private String[] columnNames;

    public ListaOrdiniTableModel(Object[][] ordini) {
        // Inizializza la tabella in base all'utente loggato
        updateModelBasedOnLoggedUser(ordini);
    }

    private void updateModelBasedOnLoggedUser(Object[][] ordini) {
        if (UtenteBusiness.getInstance().isLoggedCliente()) {
            columnNames = new String[]{"Data", "Stato", "Importo", "Ricorrente"};
        } else if (UtenteBusiness.getInstance().isLoggedCucina()) {
            columnNames = new String[]{"Data", "Stato", "Numero Prodotti"};
        } else if (UtenteBusiness.getInstance().isLoggedAmministratore()) {
            columnNames = new String[]{"ID cliente", "ID ordine", "Data", "Stato", "Importo"};
        }

        this.ordini = ordini;

        fireTableStructureChanged();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return ordini.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (UtenteBusiness.getInstance().isLoggedCucina())
            return ordini[rowIndex][columnIndex + 1];   // La cucina non deve vedere l'id

        return null;
    }
}
