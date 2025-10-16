package it.unisalento.myfood.View.ViewModel;

import it.unisalento.myfood.Business.UtenteBusiness;

import javax.swing.table.AbstractTableModel;

public class ListaCommentiTableModel extends AbstractTableModel {

    private Object[][] commenti;
    private String[] columnNames;


    public ListaCommentiTableModel(Object[][] commenti) {
        updateModelBasedOnLoggedUser(commenti);
    }

    private void updateModelBasedOnLoggedUser(Object[][] commenti) {
        if (UtenteBusiness.getInstance().isLoggedCliente()) {
            columnNames = new String[]{"Utente", "Data e ora", "Valutazione", "Testo"};
        } else if (UtenteBusiness.getInstance().isLoggedAmministratore()) {
            columnNames = new String[]{"ID", "Utente", "Testo", "Articolo", "ID Articolo", "Valutazione", "Data e ora"};
        }

        this.commenti = commenti;

        fireTableStructureChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;    // tutte le celle sono editabili così
    }

    @Override   // per modificare le intestazioni delle colonne
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return commenti.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Class getColumnClass(int columnIndex) {

        return Object.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (UtenteBusiness.getInstance().isLoggedAmministratore()) {
            return commenti[rowIndex][columnIndex];
        } else if (UtenteBusiness.getInstance().isLoggedCliente()) {
            return commenti[rowIndex][columnIndex + 1];
        }
        return null;
    }
}
