package it.unisalento.myfood.View;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Business.InterazioneUtenteBusiness;
import it.unisalento.myfood.Listener.VisualizzaCommentiFrameListener;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.ViewModel.ListaCommentiTableModel;

import javax.swing.*;
import java.awt.*;

public class VisualizzaCommentiFrame extends JFrame {

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 580;

    public VisualizzaCommentiFrame() {
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
        int numberOfCommenti = InterazioneUtenteBusiness.getInstance().getNumberOfCommentiForSelectedArticolo();

        TextLabel title = new TextLabel(numberOfCommenti + " " + (numberOfCommenti == 1 ? "commento" : "commenti") + " per " + ArticoloBusiness.getInstance().getCampoSelectedArticolo(ArticoloBusiness.CAMPO.NOME));
        TitleTextLabelDecorator titleDec = new TitleTextLabelDecorator(title);
        JPanel titlePan = new JPanel(new FlowLayout());
        titlePan.add(titleDec.getLabel());

        add(titlePan, BorderLayout.NORTH);

        // Tabella commenti
        Object[][] commenti = InterazioneUtenteBusiness.getInstance().getCommentiForSelectedArticolo();
        ListaCommentiTableModel listaCommentiTableModel = new ListaCommentiTableModel(commenti);
        JTable tableCommenti = new JTable(listaCommentiTableModel);

        tableCommenti.setDefaultRenderer(Object.class, new MultilineCellRenderer());

        tableCommenti.setRowHeight(75);

        tableCommenti.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableCommenti.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableCommenti.getColumnModel().getColumn(2).setPreferredWidth(50);
        tableCommenti.getColumnModel().getColumn(3).setPreferredWidth(400);

        JScrollPane spCommenti = new JScrollPane(tableCommenti);

        JPanel pnlCommenti = new JPanel(new FlowLayout());
        pnlCommenti.add(spCommenti);

        add(pnlCommenti, BorderLayout.CENTER);

        // Pulsante chiudi
        JButton btnClose = new JButton("Chiudi");
        btnClose.addActionListener(new VisualizzaCommentiFrameListener(this));
        btnClose.setActionCommand(VisualizzaCommentiFrameListener.CLOSE_BTN);
        btnClose.setPreferredSize(new Dimension(200, 30));

        JPanel pnlButton = new JPanel(new FlowLayout());
        pnlButton.add(btnClose);

        add(pnlButton, BorderLayout.SOUTH);
    }
}
