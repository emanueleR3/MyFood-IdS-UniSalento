package it.unisalento.myfood.View;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Listener.QuantityCartListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class QuantityCartFrame {

    private JPanel panel;

    private final JFrame frameQuantita = new JFrame("Quantità");
    private final JPanel panelQuantita = new JPanel(new BorderLayout());
    private static SpinnerModel modelloQuantita = new SpinnerNumberModel();
    private static JSpinner spinnerQuantita = new JSpinner();
    private final JButton btnAggiungi = new JButton("Aggiungi");
    private final JButton btnRimuovi = new JButton("Rimuovi Tutto");
    private Frame frame;

    public QuantityCartFrame(JPanel panel, Frame frame) {
        this.panel = panel;
        this.frame = frame;


    }

    public QuantityCartFrame() {
        this.panel = null;
    }

    public void openQuantityFrame() {
        frameQuantita.setSize(300, 150);
        frameQuantita.setResizable(false);
        frameQuantita.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Aggiungi il campo per la quantità
        int max = (int) ArticoloBusiness.getInstance().getCampoSelectedArticolo(ArticoloBusiness.CAMPO.DISPONIBILITA);
        modelloQuantita = new SpinnerNumberModel(1, 1, max, 1);
        spinnerQuantita = new JSpinner(modelloQuantita);
        panelQuantita.add(spinnerQuantita, BorderLayout.CENTER);

        // Aggiungi il pulsante "Aggiungi"
        ActionListener quantityCartListener = new QuantityCartListener(frame, frameQuantita, panel);

        btnAggiungi.addActionListener(quantityCartListener);
        btnRimuovi.addActionListener(quantityCartListener);

        btnAggiungi.setActionCommand(QuantityCartListener.ADD_BTN);
        btnRimuovi.setActionCommand(QuantityCartListener.REMOVE_BTN);

        JPanel grid = new JPanel(new GridLayout(2, 1));
        grid.add(btnAggiungi);
        grid.add(btnRimuovi);

        panelQuantita.add(grid, BorderLayout.EAST);

        frameQuantita.add(panelQuantita);
        frameQuantita.setLocationRelativeTo(null);
        frameQuantita.setVisible(true);
    }

    public static JSpinner getSpinnerQuantita() {
        return spinnerQuantita;
    }


}
