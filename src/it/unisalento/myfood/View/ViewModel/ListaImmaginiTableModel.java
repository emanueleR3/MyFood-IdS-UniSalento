package it.unisalento.myfood.View.ViewModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat;

public class ListaImmaginiTableModel extends AbstractTableModel {

    private Object[][] immagini;
    private final String[] columnNames = {"Foto", "Nome"};

    public ListaImmaginiTableModel(Object[][] immagini) {
        this.immagini = immagini;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if(columnIndex == 0) return ImageIcon.class;

        return Object.class;
    }

    @Override
    public int getRowCount() {
        return immagini.length;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return immagini[rowIndex][columnIndex];
    }
}
