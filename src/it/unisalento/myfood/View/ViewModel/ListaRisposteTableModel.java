package it.unisalento.myfood.View.ViewModel;

import javax.swing.table.AbstractTableModel;

public class ListaRisposteTableModel extends AbstractTableModel {

    private Object[][] risposte;
    private String[] columnNames = {"ID", "Amministratore", "Data e ora", "Testo"};


    public ListaRisposteTableModel(Object[][] risposte) {
        this.risposte = risposte;
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
        return risposte.length;
    }

    @Override
    public int getColumnCount() {
        return 4;       // è fisso
    }

    @Override
    public Class getColumnClass(int columnIndex) {

        return Object.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        return risposte[rowIndex][columnIndex];

    }
}
