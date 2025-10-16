package it.unisalento.myfood.View.Panels;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class CommentoPanel extends JPanel {
    private String testo;
    private int valutazione;
    private String nomeCliente;
    private Timestamp dataCommento;

    public CommentoPanel(String testo, int valutazione, String nomeCliente, Timestamp dataCommento) {
        this.testo = testo;
        this.valutazione = valutazione;
        this.nomeCliente = nomeCliente;
        this.dataCommento = dataCommento;
    }

    // NON VA BENE
    /*
    public JPanel createPanel() {
        int numberOfRow = 2;
        if (testo != null) numberOfRow = 3;

        JPanel grid = new JPanel(new GridLayout(numberOfRow, 1));

        JPanel dataPnl = new JPanel(new FlowLayout());
        dataPnl.add(new JLabel("Data commento: " + dataCommento.toString()));
        grid.add(dataPnl);

        JPanel infoCommentoPnl = new JPanel(new FlowLayout());
        infoCommentoPnl.add(new JLabel(nomeCliente + " - valutazione " + valutazione + "/5"));
        grid.add(infoCommentoPnl);

        if (numberOfRow == 3) {
            JPanel testoPnl = new JPanel(new FlowLayout());
            testoPnl.add(new JLabel(testo));
            grid.add(testoPnl);
        }

        JPanel finalPnl = new JPanel(new FlowLayout());
        finalPnl.add(grid);

        return finalPnl;
    }
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        boolean testoExists = !testo.isEmpty();
        // Disegna le tre righe del commento
        g.drawString("Data del commento: " + dataCommento.toString(), 10, 20);

        if (!testoExists) g.drawString(nomeCliente + " - valutazione " + valutazione + "/5\n", 10, 40);
        else {
            g.drawString(nomeCliente + " - valutazione " + valutazione + "/5", 10, 40);
            g.drawString(testo + "\n", 10, 60);  // il \n serve per mantenere lo spazio con il commento successivo
        }
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public int getValutazione() {
        return valutazione;
    }

    public void setValutazione(int valutazione) {
        this.valutazione = valutazione;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Timestamp getDataCommento() {
        return dataCommento;
    }

    public void setDataCommento(Timestamp dataCommento) {
        this.dataCommento = dataCommento;
    }
}
