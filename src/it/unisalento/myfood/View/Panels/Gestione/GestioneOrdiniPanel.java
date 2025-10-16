package it.unisalento.myfood.View.Panels.Gestione;

import it.unisalento.myfood.Business.OrdineBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione.PulsantieraGestione;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione.PulsantieraGestioneOrdiniDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.Gestione.GestioneOrdiniListener;
import it.unisalento.myfood.View.ViewModel.ListaOrdiniTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GestioneOrdiniPanel extends JPanel {

    public enum FILTERS{
        ALL,
        NON_PAGATI,
        PAGATI,
        IN_LAVORAZIONE,
        CONSEGNATI
    }

    private GestioneOrdiniListener gestioneOrdiniListener;
    private Frame frame;
    private JComboBox<FILTERS> filters;
    private OrdineBusiness ordineBusiness = OrdineBusiness.getInstance();
    private Object[][] ordini;
    private HashMap<String, Object> session = UtenteBusiness.getSession();
    private JPanel buttPanel = new JPanel(new FlowLayout());
    private JPanel tabPanel = new JPanel(new GridLayout(2, 1));
    private JTable table;


    public GestioneOrdiniPanel(Frame frame){
        this.frame = frame;
        gestioneOrdiniListener = new GestioneOrdiniListener(this, frame);
        setLayout(new BorderLayout());

        TextLabel title = new TextLabel("Gestione ordini");
        TitleTextLabelDecorator decTitle = new TitleTextLabelDecorator(title);
        JPanel titPan = new JPanel(new FlowLayout());
        titPan.add(decTitle.getLabel());

        JPanel gridTitle = new JPanel(new GridLayout(2, 1));
        gridTitle.add(titPan);

        JPanel filPan = new JPanel(new FlowLayout());
        filters = new JComboBox<>(FILTERS.values());
        filters.addActionListener(gestioneOrdiniListener);
        filPan.add(filters);


        gridTitle.add(filPan);

        add(gridTitle, BorderLayout.NORTH);

        filter(FILTERS.ALL);

    }

    public void filter(FILTERS filter) {
        loadPulsanti();
        loadOrdini(filter);
        revalidate();
        repaint();
    }

    private void loadPulsanti(){
        buttPanel.removeAll();
        PulsantieraGestione pulsantiera = new PulsantieraGestione();

        GestioneOrdiniListener gestioneOrdiniListener = new GestioneOrdiniListener(this, frame);
        PulsantieraGestioneOrdiniDecorator pulsantieraGestioneOrdiniDecorator = new PulsantieraGestioneOrdiniDecorator(pulsantiera);

        ArrayList<JButton> buttons = pulsantieraGestioneOrdiniDecorator.getPulsanti();

        JButton btnTornaDashboard = buttons.remove(0);
        btnTornaDashboard.addActionListener(gestioneOrdiniListener);
        JPanel pnlBtnDashboard = new JPanel(new FlowLayout());
        pnlBtnDashboard.add(btnTornaDashboard);
        add(pnlBtnDashboard, BorderLayout.WEST);

        JPanel buttGrid = new JPanel(new GridLayout(buttons.size(), 1));

        Iterator<JButton> iterator = buttons.iterator();

        while(iterator.hasNext()){
            JPanel pnl = new JPanel(new FlowLayout());
            JButton button = iterator.next();
            button.setPreferredSize(new Dimension(180, 25));
            button.addActionListener(gestioneOrdiniListener);
            pnl.add(button);
            buttGrid.add(pnl);
        }

        buttPanel.add(buttGrid);

        add(buttPanel, BorderLayout.EAST);
    }

    private void loadOrdini(FILTERS filter){
        tabPanel.removeAll();

        switch (filter){
            case ALL :
               ordini = ordineBusiness.loadOrdini();
               break;
            case NON_PAGATI:
                ordini = ordineBusiness.loadOrdiniByState(OrdineBusiness.STATO_ORDINE.NON_PAGATO);
                break;
            case PAGATI:
                ordini = ordineBusiness.loadOrdiniByState(OrdineBusiness.STATO_ORDINE.PAGATO);
                break;
            case IN_LAVORAZIONE:
                ordini = ordineBusiness.loadOrdiniByState(OrdineBusiness.STATO_ORDINE.IN_LAVORAZIONE);
                break;
            case CONSEGNATI:
                ordini = ordineBusiness.loadOrdiniByState(OrdineBusiness.STATO_ORDINE.CONSEGNATO);
                break;
            default:
                System.out.println("Filtro non esistente");
                break;
        }
        ListaOrdiniTableModel listaOrdiniTableModel = new ListaOrdiniTableModel(ordini);

        table = new JTable(listaOrdiniTableModel);

        JScrollPane scrollTable = new JScrollPane(table);


        table.getColumnModel().getColumn(0).setPreferredWidth(270);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.setRowHeight(25);
        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe
        table.setSelectionModel(selectionModel);

        tabPanel.add(scrollTable);

        add(tabPanel, BorderLayout.CENTER);

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
