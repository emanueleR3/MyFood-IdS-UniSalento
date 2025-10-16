package it.unisalento.myfood.View;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class MultilineCellRenderer extends JTextArea implements TableCellRenderer {
    public MultilineCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true); // Per garantire che lo sfondo della cella venga disegnato correttamente
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        // Imposta il testo della JTextArea sulla base del valore della cella
        setText((value == null) ? "" : value.toString());

        // Imposta il colore di sfondo sulla base dello stato della cella
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }

        return this;
    }
}
