package it.unisalento.myfood.View.Panels.Gestione;


import it.unisalento.myfood.Business.InterazioneUtenteBusiness;
import it.unisalento.myfood.View.MultilineCellRenderer;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Decorator.Menu.EditMenuDecorator;
import it.unisalento.myfood.View.Decorator.Menu.UtilMenu;
import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione.PulsantieraGestione;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione.PulsantieraGestioneCommentiDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.Gestione.GestioneCommentiListener;
import it.unisalento.myfood.Listener.Table.TableCommentiListener;
import it.unisalento.myfood.View.ViewModel.ListaCommentiTableModel;
import it.unisalento.myfood.View.ViewModel.ListaRisposteTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class GestioneCommentiPanel extends JPanel {
    private Frame frame;
    private Object[][] commenti;
    private Object[][] risposte;
    private JTable commentiTable;
    private JTable risposteTable;

    private InterazioneUtenteBusiness interazioneUtenteBusiness = InterazioneUtenteBusiness.getInstance();

    private JPanel pnlUserLogged = new JPanel();
    private TableCommentiListener tableCommentiListener;
    private JPanel northPan;
    private JComboBox<FILTERS> filter;

    private JPanel centralPan;
    private ActionListener gestioneCommentiListener;


    public enum FILTERS {
        ALL,
        SENZA_RISPOSTA
    }


    public GestioneCommentiPanel(Frame frame) {
        this.frame = frame;
        northPan = new JPanel(new GridLayout(2, 1));
        pnlUserLogged.setLayout(new BoxLayout(pnlUserLogged, BoxLayout.Y_AXIS));
        JPanel eastPan = new JPanel(new FlowLayout());
        centralPan = new JPanel(new GridLayout(2, 1));
        tableCommentiListener = new TableCommentiListener(this);

        TextLabel title = new TextLabel("Gestione Commenti");
        TitleTextLabelDecorator decTitle = new TitleTextLabelDecorator(title);
        JPanel titPan = new JPanel(new FlowLayout());
        titPan.add(decTitle.getLabel());

        JPanel filterPan = new JPanel(new FlowLayout());

        filter= new JComboBox<>(FILTERS.values());

        gestioneCommentiListener = new GestioneCommentiListener(this, frame);

        filter.addActionListener(gestioneCommentiListener);

        filterPan.add(filter);

        northPan.add(titPan);

        northPan.add(filterPan);

        setLayout(new BorderLayout());
        setPulsanti();

        loadCatalogo(FILTERS.ALL);
        setMenuBar();

        add(northPan, BorderLayout.NORTH);
        add(centralPan, BorderLayout.CENTER);

        revalidate();
        repaint();

    }

    public void setMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        UtilMenu utilMenu = new UtilMenu();
        EditMenuDecorator editMenuDecorator = new EditMenuDecorator(utilMenu);

        JMenu menu = editMenuDecorator.getJMenu();

        ArrayList<JMenuItem> menuItems = editMenuDecorator.getMenuItems();

        Iterator<JMenuItem> iterator = menuItems.iterator();

        while (iterator.hasNext()){
            JMenuItem item = iterator.next();
            menu.add(item);
            item.addActionListener(gestioneCommentiListener);
        }

        menuBar.add(menu);

        frame.setJMenuBar(menuBar);
    }


    public void setPulsanti() {
        Pulsantiera pulsantieraGestione = new PulsantieraGestione();
        PulsantieraGestioneCommentiDecorator pulsantieraGestioneCommentiDecorator = new PulsantieraGestioneCommentiDecorator(pulsantieraGestione);

        ArrayList<JButton> buttons = pulsantieraGestioneCommentiDecorator.getPulsanti();

        JButton btnTornaDashboard = buttons.remove(0);
        btnTornaDashboard.addActionListener(gestioneCommentiListener);
        JPanel pnlBtnDashboard = new JPanel(new FlowLayout());
        pnlBtnDashboard.add(btnTornaDashboard);
        add(pnlBtnDashboard, BorderLayout.WEST);

        JPanel pnlGridButtons = new JPanel(new GridLayout(buttons.size(), 1));


        for (JButton btn : buttons) {
            JPanel row = new JPanel(new FlowLayout());
            row.add(btn);
            pnlGridButtons.add(row);
            btn.addActionListener(gestioneCommentiListener);
        }

        JPanel pnlFlowButtons = new JPanel(new FlowLayout());
        pnlFlowButtons.add(pnlGridButtons);

        add(pnlFlowButtons, BorderLayout.EAST);

        revalidate();
        repaint();

    }


    public void loadCatalogo(FILTERS filter) {
        centralPan.removeAll();

        switch (filter){
            case ALL ->  commenti = interazioneUtenteBusiness.getCommenti();
            case SENZA_RISPOSTA -> commenti = interazioneUtenteBusiness.getCommentiNotAnswered();
        }

        ListaCommentiTableModel listaCommentiTableModel = new ListaCommentiTableModel(commenti);

        commentiTable = new JTable(listaCommentiTableModel);

        TableCommentiListener tableCommentiListener = new TableCommentiListener(this);
        commentiTable.getSelectionModel().addListSelectionListener(tableCommentiListener);
        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe
        commentiTable.setSelectionModel(selectionModel);

        commentiTable.getSelectionModel().addListSelectionListener(tableCommentiListener);

        commentiTable.setDefaultRenderer(Object.class, new MultilineCellRenderer());

        commentiTable.setRowHeight(75);

        commentiTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        commentiTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        commentiTable.getColumnModel().getColumn(2).setPreferredWidth(310);
        commentiTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        commentiTable.getColumnModel().getColumn(4).setPreferredWidth(25);
        commentiTable.getColumnModel().getColumn(5).setPreferredWidth(75);
        commentiTable.getColumnModel().getColumn(6).setPreferredWidth(85);

        JScrollPane scrollPane = new JScrollPane(commentiTable);
        centralPan.add(scrollPane);
        centralPan.add(new JLabel());


        revalidate();
        repaint();

    }


    public Integer getSelectedCommento() {
        if (commentiTable == null || commentiTable.getSelectedRow() == -1) {  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un commento");
            return null;
        } else
            return (Integer) commenti[commentiTable.getSelectedRow()][0];
    }

    public Integer getSelectedRisposta() {
        if (risposteTable == null || risposteTable.getSelectedRow() == -1) {  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona una risposta");
            return null;
        } else
            return (Integer) risposte[risposteTable.getSelectedRow()][0];
    }


    public void showRisposte(){

        centralPan.remove(1);

        risposte = interazioneUtenteBusiness.getRipsosteCommento(getSelectedCommento());

        ListaRisposteTableModel listaRisposteTableModel = new ListaRisposteTableModel(risposte);
        risposteTable = new JTable(listaRisposteTableModel);

        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe
        risposteTable.setSelectionModel(selectionModel);

        risposteTable.setDefaultRenderer(Object.class, new MultilineCellRenderer());

        risposteTable.setRowHeight(50);

        risposteTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        risposteTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        risposteTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        risposteTable.getColumnModel().getColumn(3).setPreferredWidth(400);

        JScrollPane scrollPane = new JScrollPane(risposteTable);

        centralPan.add(scrollPane);

        revalidate();
        repaint();


    }

    public FILTERS getFilter(){
        return (FILTERS) filter.getSelectedItem();
    }


}
