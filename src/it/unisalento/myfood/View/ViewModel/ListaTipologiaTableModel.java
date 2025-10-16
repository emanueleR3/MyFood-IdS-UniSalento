package it.unisalento.myfood.View.ViewModel;

import javax.swing.table.AbstractTableModel;

public class ListaTipologiaTableModel extends AbstractTableModel {

    private Object[][] ingredienti;
    private String[] columnNames = {"ID", "Tipologia"};


    public ListaTipologiaTableModel(Object[][] ingredienti) {
        this.ingredienti = ingredienti;
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
        return ingredienti.length;
    }

    @Override
    public int getColumnCount() {
        return 2;       // è fisso
    }

    @Override
    public Class getColumnClass(int columnIndex) {

        return Object.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        return ingredienti[rowIndex][columnIndex];

    }
}
