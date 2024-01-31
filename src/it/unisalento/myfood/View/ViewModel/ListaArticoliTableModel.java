package it.unisalento.myfood.View.ViewModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat;

public class ListaArticoliTableModel extends AbstractTableModel {

    private Object[][] articoli;
    private String[] columnNames = {"Copertina", "Articolo", "Descrizione", "Prezzo"};


    public ListaArticoliTableModel(Object[][] articoli) {
        this.articoli = articoli;
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
        return articoli.length;
    }

    // es. ora mostriamo solo nome descrizione prezzo
    @Override
    public int getColumnCount() {
        return 4;       // è fisso
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if(columnIndex == 0) return ImageIcon.class;

        return Object.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) { //la matrice ha 5 colonne, ne visualizziamo solo 4

        // id=0  id=1 id=2   id=3        id=4
        // ID, FOTO, NOME, DESCRIZIONE, PREZZO

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        if(columnIndex == 3)
            return "€ " + decimalFormat.format(articoli[rowIndex][4]);

        return articoli[rowIndex][columnIndex + 1];

    }
}
