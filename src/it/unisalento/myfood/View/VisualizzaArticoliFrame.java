package it.unisalento.myfood.View;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Listener.VisualizzaArticoliFrameListener;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.ViewModel.ListaArticoliTableModel;

import javax.swing.*;
import java.awt.*;

public class VisualizzaArticoliFrame extends JFrame {

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 580;

    public VisualizzaArticoliFrame() {
        setTitle("Visualizza commenti");

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        int x = ((SCREEN_SIZE.width - getWidth()) / 2) - FRAME_WIDTH / 2;
        int y = ((SCREEN_SIZE.height - getHeight()) / 2) - FRAME_HEIGHT / 2;
        setLocation(x, y);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        // Titolo
        TextLabel title = new TextLabel("Articoli contenuti nel menu \"" + ArticoloBusiness.getInstance().getCampoSelectedArticolo(ArticoloBusiness.CAMPO.NOME) + "\"");
        TitleTextLabelDecorator titleDec = new TitleTextLabelDecorator(title);
        JPanel titlePan = new JPanel(new FlowLayout());
        titlePan.add(titleDec.getLabel());

        add(titlePan, BorderLayout.NORTH);

        // Tabella commenti
        Object[][] articoli = ArticoloBusiness.getInstance().getArticoliViewMenu();
        ListaArticoliTableModel listaArticoliTableModel = new ListaArticoliTableModel(articoli);
        JTable tableArticoli = new JTable(listaArticoliTableModel);

        tableArticoli.setDefaultRenderer(Object.class, new MultilineCellRenderer());

        tableArticoli.setRowHeight(100);

        tableArticoli.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableArticoli.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableArticoli.getColumnModel().getColumn(2).setPreferredWidth(300);
        tableArticoli.getColumnModel().getColumn(3).setPreferredWidth(100);


        JScrollPane spArticoli = new JScrollPane(tableArticoli);

        JPanel pnlArticoli = new JPanel(new FlowLayout());
        pnlArticoli.add(spArticoli);

        add(pnlArticoli, BorderLayout.CENTER);

        // Pulsante chiudi
        JButton btnClose = new JButton("Chiudi");
        btnClose.addActionListener(new VisualizzaArticoliFrameListener(this));
        btnClose.setActionCommand(VisualizzaArticoliFrameListener.CLOSE_BTN);
        btnClose.setPreferredSize(new Dimension(200, 30));

        JPanel pnlButton = new JPanel(new FlowLayout());
        pnlButton.add(btnClose);

        add(pnlButton, BorderLayout.SOUTH);
    }
}
