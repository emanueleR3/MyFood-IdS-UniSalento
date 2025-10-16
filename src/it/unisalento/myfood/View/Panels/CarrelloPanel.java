package it.unisalento.myfood.View.Panels;

import it.unisalento.myfood.Business.CarrelloBusiness;
import it.unisalento.myfood.View.Decorator.Label.*;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.CarrelloPanelListener;
import it.unisalento.myfood.View.ViewModel.CarrelloTableModel;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class CarrelloPanel extends JPanel {
    private CarrelloBusiness carrelloBusiness = CarrelloBusiness.getInstance();
    private JTable table;
    private Object[][] articoli;
    private CarrelloPanelListener carrelloPanelListener;
    private Frame frame;
    private JPanel gridTot;


    public CarrelloPanel(Frame frame){
        this.frame = frame;
        setLayout(new BorderLayout());

        gridTot = new JPanel(new GridLayout(2, 1));
        JPanel titoloPanel = new JPanel(new FlowLayout());

        carrelloPanelListener = new CarrelloPanelListener(frame, this);
        TextLabel textLabel = new TextLabel("Carrello ");
        TitleTextLabelDecorator titleTextLabel = new TitleTextLabelDecorator(textLabel);

        titoloPanel.add(titleTextLabel.getLabel());

        add(titoloPanel, BorderLayout.NORTH);

        loadArticoli();

        loadPulsanti();

        JPanel totPan = new JPanel(new FlowLayout());
        totPan.add(gridTot);
        gridTot.setBackground(Color.GRAY);
        add(totPan, BorderLayout.SOUTH);
        revalidate();
        repaint();

    }


    public void loadArticoli(){
        articoli = carrelloBusiness.getArticoliFromCarrello();

        CarrelloTableModel carrelloTableModel = new CarrelloTableModel(articoli);

        table = new JTable(carrelloTableModel);

        JPanel panel = new JPanel(new GridLayout(2, 1));

        if(articoli.length > 0) {

            ListSelectionModel selectionModel = new DefaultListSelectionModel();

            table.setRowHeight(100);
            table.getColumnModel().getColumn(0).setPreferredWidth(100);   //immagine
            table.getColumnModel().getColumn(1).setPreferredWidth(100);   //nome
            table.getColumnModel().getColumn(2).setPreferredWidth(150);  //descrizione
            table.getColumnModel().getColumn(3).setPreferredWidth(50);  //prezzo
            table.getColumnModel().getColumn(4).setPreferredWidth(50);  //quantità

            selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe
            table.setSelectionModel(selectionModel);
            table.setPreferredScrollableViewportSize(table.getPreferredSize());
            JScrollPane scrollPane = new JScrollPane(table);

            JPanel tabPan = new JPanel(new GridLayout());
            tabPan.add(scrollPane);
            tabPan.setSize(scrollPane.getSize());
            panel.add(tabPan);

            gridTot = new JPanel(new GridLayout(2, 1));

            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            TextLabel totalLabel = new TextLabel("Totale: € " + decimalFormat.format(carrelloBusiness.calculateTotal()));
            WhiteTextLabelSecondariaDecorator totalLabelDec = new WhiteTextLabelSecondariaDecorator(totalLabel);
            gridTot.add(totalLabelDec.getLabel());
            panel.add(gridTot);

        } else {
            JPanel emptyCartPan = new JPanel(new FlowLayout());

            TextLabel emptyCart = new TextLabel("Il carrello è vuoto");
            BlackTextLabelSecondariaDecorator empryCartDecorated = new BlackTextLabelSecondariaDecorator(emptyCart);

            emptyCartPan.add(empryCartDecorated.getLabel());
            panel.add(emptyCartPan);
        }

        add(panel, BorderLayout.CENTER);

        revalidate();
        repaint();

    }

    private void loadPulsanti(){
        JPanel buttons = new JPanel(new FlowLayout());
        JPanel gridButton = new JPanel(new GridLayout(3, 1));

        JButton back = new JButton("Torna al catalogo");
        back.setPreferredSize(new Dimension(180, 25));
        back.addActionListener(carrelloPanelListener);
        back.setActionCommand(CarrelloPanelListener.BACK_TO_CATALOGUE_BTN);
        JPanel backPanel = new JPanel(new FlowLayout());
        backPanel.add(back);
        gridButton.add(backPanel);

        JButton ordina = new JButton("Conferma ordine");
        ordina.setPreferredSize(new Dimension(180, 25));
        ordina.addActionListener(carrelloPanelListener);
        ordina.setActionCommand(CarrelloPanelListener.ORD_BTN);
        gridTot.add(ordina);

        JButton modArticolo = new JButton("Modifica quantità");
        modArticolo.setPreferredSize(new Dimension(180, 25));
        modArticolo.addActionListener(carrelloPanelListener);
        modArticolo.setActionCommand(CarrelloPanelListener.MOD_BTN);
        JPanel modPanel = new JPanel(new FlowLayout());
        modPanel.add(modArticolo);
        gridButton.add(modPanel);

        buttons.add(gridButton);

        add(buttons, BorderLayout.EAST);

    }

    public Integer getArticoloSelezionato(){
        if(table == null || table.getSelectedRow() == -1){  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un articolo");
            return null;
        } else
            return (Integer) articoli[table.getSelectedRow()][0];
    }
}
