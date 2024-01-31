package it.unisalento.myfood.View.ViewModel;

import javax.swing.table.AbstractTableModel;

public class ListaUtentiTableModel extends AbstractTableModel {

    public enum FILTRA_PER {
        CLIENTI, CUCINA, AMMINISTRATORI
    }

    Object[][] utenti;
    private String[] columnNames;

    public ListaUtentiTableModel(Object[][] utenti, FILTRA_PER filtraPer) {
        updateModelBasedOnFilter(utenti, filtraPer);
    }

    private void updateModelBasedOnFilter(Object[][] utenti, FILTRA_PER filtraPer) {
        switch (filtraPer) {
            case CLIENTI -> columnNames = new String[]{"ID", "Nome Cognome", "Data di nascita", "Disabilitato"};
            case CUCINA, AMMINISTRATORI -> columnNames = new String[]{"ID", "Nome Cognome", "Email", "Professione"};
        }

        this.utenti = utenti;

        fireTableStructureChanged();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return utenti.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;  // ID - NOME e COGNOME - DATA DI NASCITA - ABILITATO
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return utenti[rowIndex][columnIndex];
    }
}