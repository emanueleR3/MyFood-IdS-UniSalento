package it.unisalento.myfood.View.ViewModel;

import javax.swing.table.AbstractTableModel;

public class ListaAziendeTableModel extends AbstractTableModel {

    private Object[][] aziende;
    private String[] columnNames = {"ID", "Nome", "P.IVA"};


    public ListaAziendeTableModel(Object[][] aziende) {
        this.aziende = aziende;
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
        return aziende.length;
    }

    @Override
    public int getColumnCount() {
        return 3;       // è fisso
    }

    @Override
    public Class getColumnClass(int columnIndex) {

        return Object.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        return aziende[rowIndex][columnIndex];

    }
}
