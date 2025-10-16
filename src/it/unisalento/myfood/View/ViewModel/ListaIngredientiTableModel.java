package it.unisalento.myfood.View.ViewModel;

import javax.swing.table.AbstractTableModel;

public class ListaIngredientiTableModel extends AbstractTableModel {

    private Object[][] ingredienti;
    private String[] columnNames = {"ID", "Nome", "Tipologia", "Produttore"};


    public ListaIngredientiTableModel(Object[][] ingredienti) {
        this.ingredienti = ingredienti;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;    // tutte le celle sono editabili così
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        //TODO

       /* switch (columnIndex) {
            case 3: p.setPrezzo(Float.valueOf(aValue.toString()));  //mettere try - catch NumberFormatException
                                                                    // mettere fino alla seconda decimale
        }*/
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
        return 4;       // è fisso
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
