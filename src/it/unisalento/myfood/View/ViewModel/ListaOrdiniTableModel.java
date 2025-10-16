package it.unisalento.myfood.View.ViewModel;

import it.unisalento.myfood.Business.UtenteBusiness;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat;

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
            columnNames = new String[]{"ID ordine", "ID cliente", "Data", "Stato", "Importo"};
        }

        this.ordini = ordini;

        fireTableStructureChanged();
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return Object.class;
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

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        if(UtenteBusiness.getInstance().isLoggedCliente()) {
            if (columnIndex == 2)
                return "€ " + decimalFormat.format(ordini[rowIndex][3]);
        }

        if(UtenteBusiness.getInstance().isLoggedAmministratore()) {
            if (columnIndex == 4)
                return "€ " + decimalFormat.format(ordini[rowIndex][4]);
            else
                return ordini[rowIndex][columnIndex];
        }

        if (UtenteBusiness.getInstance().isLoggedCucina() || UtenteBusiness.getInstance().isLoggedCliente())
            return ordini[rowIndex][columnIndex + 1];   // La cucina e il cliente non devono vedere l'id

        return null;
    }
}
