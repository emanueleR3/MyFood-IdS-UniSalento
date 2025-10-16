package it.unisalento.myfood.View.Panels;

import it.unisalento.myfood.Business.OrdineBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Label.BlackTextLabelSecondariaDecorator;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.OrdiniClienteListener;
import it.unisalento.myfood.Listener.Table.TableOrdiniListener;
import it.unisalento.myfood.View.ViewModel.ListaOrdiniTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class OrdiniClientePanel extends JPanel {

    public enum FILTERS{
        ALL,
        RICORRENTI,
        NON_PAGATI
    }

    private OrdiniClienteListener ordiniClienteListener;
    private Frame frame;
    private JComboBox<FILTERS> filters;
    private OrdineBusiness ordineBusiness = OrdineBusiness.getInstance();
    private Object[][] ordini;
    private HashMap<String, Object> session = UtenteBusiness.getSession();
    private JPanel buttPanel = new JPanel(new FlowLayout());
    private JPanel tabPanel = new JPanel(new GridLayout(2, 1));
    private JTable table;
    private JButton ricorr;


    public OrdiniClientePanel(Frame frame){
        this.frame = frame;
        ordiniClienteListener = new OrdiniClienteListener(frame, this);
        setLayout(new BorderLayout());

        TextLabel title = new TextLabel("Ordini effettuati");
        TitleTextLabelDecorator decTitle = new TitleTextLabelDecorator(title);
        JPanel titPan = new JPanel(new FlowLayout());
        titPan.add(decTitle.getLabel());

        JPanel gridTitle = new JPanel(new GridLayout(2, 1));
        gridTitle.add(titPan);

        JPanel filPan = new JPanel(new FlowLayout());
        filters = new JComboBox<>(FILTERS.values());
        filters.addActionListener(ordiniClienteListener);
        filPan.add(filters);

        gridTitle.add(filPan);

        add(gridTitle, BorderLayout.NORTH);

        filter(FILTERS.ALL);

    }

    public void filter(FILTERS filter) {
        loadPulsanti(filter);
        loadOrdini(filter);
        revalidate();
        repaint();
    }

    private void loadPulsanti(FILTERS filter){
        buttPanel.removeAll();
        JPanel gridButton = new JPanel(new GridLayout(4, 1));

        JButton back = new JButton("Torna al catalogo");
        back.setPreferredSize(new Dimension(200, 25));
        back.addActionListener(ordiniClienteListener);
        back.setActionCommand(OrdiniClienteListener.BACK_TO_CATALOGUE_BTN);
        JPanel pnlBackBtn = new JPanel(new FlowLayout());
        pnlBackBtn.add(back);
        gridButton.add(pnlBackBtn);

        JButton view = new JButton("Visualizza in dettaglio");
        view.setPreferredSize(new Dimension(200, 25));
        view.addActionListener(ordiniClienteListener);
        view.setActionCommand(OrdiniClienteListener.VIEW_DETT_ORD);
        JPanel pnlViewBtn = new JPanel(new FlowLayout());
        pnlViewBtn.add(view);
        gridButton.add(pnlViewBtn);

        ricorr = new JButton("Imposta come ricorrente");
        ricorr.setPreferredSize(new Dimension(200, 25));
        ricorr.addActionListener(ordiniClienteListener);
        ricorr.setActionCommand(OrdiniClienteListener.SET_UNSET_RICOR);
        JPanel pnlRicorBtn = new JPanel(new FlowLayout());
        pnlRicorBtn.add(ricorr);
        gridButton.add(pnlRicorBtn);

        switch (filter){
            case RICORRENTI:
                JButton loadCarr = new JButton("Importa nel carrello");
                loadCarr.setPreferredSize(new Dimension(200, 25));
                loadCarr.addActionListener(ordiniClienteListener);
                loadCarr.setActionCommand(OrdiniClienteListener.LOAD_CARR);
                JPanel pnlLoadCartBtn = new JPanel(new FlowLayout());
                pnlLoadCartBtn.add(loadCarr);
                gridButton.add(pnlLoadCartBtn);
                break;

            case NON_PAGATI:
                JButton pay = new JButton("Prosegui con l'ordine");
                pay.setPreferredSize(new Dimension(200, 25));
                pay.addActionListener(ordiniClienteListener);
                pay.setActionCommand(OrdiniClienteListener.PAY_ORD);
                JPanel pnlPayBtn = new JPanel(new FlowLayout());
                pnlPayBtn.add(pay);
                gridButton.add(pnlPayBtn);
                break;
        }

        buttPanel.add(gridButton);

        add(buttPanel, BorderLayout.EAST);

        buttPanel.revalidate();
        buttPanel.repaint();
    }

    private void loadOrdini(FILTERS filter){
        tabPanel.removeAll();

        switch (filter){
            case ALL :
               ordini = ordineBusiness.loadOrdiniCliente();
               break;
            case RICORRENTI:
                ordini = ordineBusiness.loadOrdiniRicorrentiCliente();
                break;
            case NON_PAGATI:
                ordini = ordineBusiness.loadOrdiniClienteByState(OrdineBusiness.STATO_ORDINE.NON_PAGATO);
                break;
            default:
                System.out.println("Filtro non esistente");
                break;
        }
        ListaOrdiniTableModel listaOrdiniTableModel = new ListaOrdiniTableModel(ordini);
        TableOrdiniListener tableOrdiniListener = new TableOrdiniListener(this);
        table = new JTable(listaOrdiniTableModel);

        JScrollPane scrollTable = new JScrollPane(table);

        table.getColumnModel().getColumn(0).setPreferredWidth(270);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.setRowHeight(25);

        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe
        table.setSelectionModel(selectionModel);

        table.getSelectionModel().addListSelectionListener(tableOrdiniListener);
        tabPanel.add(scrollTable);

        Integer nonPagatiNumber = ordineBusiness.getNumberOfOrdiniByState(OrdineBusiness.STATO_ORDINE.NON_PAGATO);


        JPanel pan = new JPanel(new FlowLayout());

        TextLabel textLabel = new TextLabel("Hai " + nonPagatiNumber + " ordini in sospeso");

        BlackTextLabelSecondariaDecorator decoratedTextLabel = new BlackTextLabelSecondariaDecorator(textLabel);
        pan.add(decoratedTextLabel.getLabel());

       tabPanel.add(pan);

        add(tabPanel, BorderLayout.CENTER);

        tabPanel.revalidate();
        tabPanel.repaint();

    }

    public void setRicorrButtonText(String text){
        ricorr.setText(text);
    }

    public FILTERS getFilter(){
        return (FILTERS) filters.getSelectedItem();
    }

    public Integer getOrdineSelezionato(){
        if(table == null || table.getSelectedRow() == -1) {  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un ordine");
            return null;
        } else
        return (Integer) ordini[table.getSelectedRow()][0];
    }

}
