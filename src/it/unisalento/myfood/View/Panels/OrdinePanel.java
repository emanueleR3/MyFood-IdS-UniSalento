package it.unisalento.myfood.View.Panels;

import it.unisalento.myfood.Business.OrdineBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Label.BlackTextLabelSecondariaDecorator;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.OrdinePanelListener;
import it.unisalento.myfood.View.MultilineCellRenderer;
import it.unisalento.myfood.View.ViewModel.CarrelloTableModel;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;

public class OrdinePanel extends JPanel {
    private HashMap<String, Object> session;
    private OrdineBusiness ordineBusiness;
    private Frame frame;
    private OrdinePanelListener ordinePanelListener;
    private UtenteBusiness utenteBusiness;
    private Object[][] articoli;
    private JPanel gridPan;

    public OrdinePanel(Frame frame) {
        this.frame = frame;
        session = UtenteBusiness.getSession();
        ordineBusiness = OrdineBusiness.getInstance();
        ordinePanelListener = new OrdinePanelListener(frame);
        utenteBusiness = UtenteBusiness.getInstance();
        gridPan = new JPanel(new GridLayout(2, 1));

        setLayout(new BorderLayout());

        TextLabel title = new TextLabel("Dettaglio Ordine");
        TitleTextLabelDecorator decoratedTitle = new TitleTextLabelDecorator(title);
        JPanel titlePan = new JPanel(new FlowLayout());
        titlePan.add(decoratedTitle.getLabel());
        add(titlePan, BorderLayout.NORTH);

        loadArticoli();

        loadPulsanti();

        loadDettagli();

        add(gridPan, BorderLayout.CENTER);


        revalidate();
        repaint();


    }

    private void loadArticoli(){
        articoli = ordineBusiness.loadArticoliBySelectedOrdine();

        CarrelloTableModel carrelloTableModel = new CarrelloTableModel(articoli);

        JTable table = new JTable(carrelloTableModel);
        ListSelectionModel selectionModel = new DefaultListSelectionModel();

        table.setDefaultRenderer(Object.class, new MultilineCellRenderer());

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



        gridPan.add(scrollPane, BorderLayout.CENTER);

    }

    private void loadPulsanti(){

        JPanel buttonPan = new JPanel(new FlowLayout());
        JPanel buttonPanGrid = new JPanel(new GridLayout(2, 1));
        JPanel buttonClientePan = new JPanel(new BorderLayout());


        JButton back = new JButton("Torna alla lista");
        back.setPreferredSize(new Dimension(180, 25));
        back.addActionListener(ordinePanelListener);
        back.setActionCommand(OrdinePanelListener.BACK_TO_CATALOGUE_BTN);
        buttonClientePan.add(back);

        buttonPanGrid.add(buttonClientePan);
        buttonPan.add(buttonPanGrid);
        add(buttonPan, BorderLayout.EAST);

    }

    private void loadDettagli(){
        JPanel dettagliPan = new JPanel(new FlowLayout());
        JPanel dettagliGrid = new JPanel(new GridLayout(4, 1));
        TextLabel data = new TextLabel("Ordine effettuato in data: " + ordineBusiness.getSelectedOrdineData());
        BlackTextLabelSecondariaDecorator dataDec = new BlackTextLabelSecondariaDecorator(data);
        dettagliGrid.add(dataDec.getLabel());

        TextLabel stato = new TextLabel("Attualmente l'ordine è nello stato: " + ordineBusiness.getSelectedOrdineStato());
        BlackTextLabelSecondariaDecorator statoDec = new BlackTextLabelSecondariaDecorator(stato);
        dettagliGrid.add(statoDec.getLabel());

        if(utenteBusiness.isLoggedCliente()){
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            TextLabel totale = new TextLabel("Totale: € " + decimalFormat.format(ordineBusiness.getSelectedOrdineImporto()));
            BlackTextLabelSecondariaDecorator totaleDec = new BlackTextLabelSecondariaDecorator(totale);
            dettagliGrid.add(totaleDec.getLabel());

            TextLabel ricorr = new TextLabel(ordineBusiness.isOrdineRicorrente()? "L'ordine è tra i ricorrenti" : "L'ordine non è tra i ricorrenti");
            BlackTextLabelSecondariaDecorator ricorrDec = new BlackTextLabelSecondariaDecorator(ricorr);
            dettagliGrid.add(ricorrDec.getLabel());

        }

        dettagliPan.add(dettagliGrid);
        gridPan.add(dettagliPan, BorderLayout.SOUTH);

    }




}
