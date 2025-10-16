package it.unisalento.myfood.View.Panels.Gestione;

import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Menu.EditMenuDecorator;
import it.unisalento.myfood.View.Decorator.Menu.UtilMenu;
import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione.PulsantieraGestione;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.Gestione.GestioneUtentiListener;
import it.unisalento.myfood.View.ViewModel.ListaUtentiTableModel;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione.PulsantieraGestioneUtentiDecorator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class GestioneUtentiPanel extends JPanel {

    private Frame frame;

    private static JTable tableUtenti;
    private Object[][] utenti;

    private static JPanel pnlListaUtenti;

    private static JComboBox<String> filtro;

    private static ListaUtentiTableModel listaUtentiTableModel;
    private ActionListener gestioneUtentiListener;

    public GestioneUtentiPanel(Frame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        gestioneUtentiListener = new GestioneUtentiListener(this, frame);

        String[] filtri = {"Clienti", "Cucina", "Amministratori"};
        filtro = new JComboBox<>(filtri);
        filtro.addActionListener(gestioneUtentiListener);
        JPanel pnlFiltri = new JPanel(new FlowLayout());
        pnlFiltri.add(filtro);
        add(pnlFiltri, BorderLayout.NORTH);

        pnlListaUtenti = new JPanel(new GridLayout(2, 1));

        setPulsanti();
        setMenuBar();
        pnlListaUtenti.add(createUITableUtenti(ListaUtentiTableModel.FILTRA_PER.valueOf(filtri[0].toUpperCase())));

        pnlListaUtenti.add(new JLabel());
        add(pnlListaUtenti, BorderLayout.CENTER);
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
            item.addActionListener(gestioneUtentiListener);
        }

        menuBar.add(menu);

        frame.setJMenuBar(menuBar);
    }

    public void setPulsanti() {
        Pulsantiera pulsantieraGestione = new PulsantieraGestione();
        PulsantieraGestioneUtentiDecorator pulsantieraGestioneUtenti = new PulsantieraGestioneUtentiDecorator(pulsantieraGestione);

        ArrayList<JButton> buttons = pulsantieraGestioneUtenti.getPulsanti();


        JButton btnTornaDashboard = buttons.remove(0);
        btnTornaDashboard.addActionListener(gestioneUtentiListener);
        JPanel pnlBtnDashboard = new JPanel(new FlowLayout());
        pnlBtnDashboard.add(btnTornaDashboard);
        add(pnlBtnDashboard, BorderLayout.WEST);

        JPanel pnlGridButtons = new JPanel(new GridLayout(buttons.size(), 1));

        for (JButton btn : buttons) {
            JPanel row = new JPanel(new FlowLayout());
            row.add(btn);
            pnlGridButtons.add(row);
            btn.addActionListener(gestioneUtentiListener);
        }

        JPanel pnlFlowButtons = new JPanel(new FlowLayout());
        pnlFlowButtons.add(pnlGridButtons);

        add(pnlFlowButtons, BorderLayout.EAST);
    }

    public JScrollPane createUITableUtenti(ListaUtentiTableModel.FILTRA_PER filtraPer) {
        loadTable(filtraPer);

        tableUtenti = new JTable(listaUtentiTableModel);

        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe
        tableUtenti.setSelectionModel(selectionModel);

        tableUtenti.setRowHeight(25);
        tableUtenti.getColumnModel().getColumn(0).setPreferredWidth(55);
        tableUtenti.getColumnModel().getColumn(1).setPreferredWidth(131);
        tableUtenti.getColumnModel().getColumn(2).setPreferredWidth(131);
        tableUtenti.getColumnModel().getColumn(3).setPreferredWidth(131);
        tableUtenti.setPreferredScrollableViewportSize(tableUtenti.getPreferredSize());

        return new JScrollPane(tableUtenti);
    }

    public void updateUITableUtente(ListaUtentiTableModel.FILTRA_PER filtraPer) {
        pnlListaUtenti.removeAll();

        pnlListaUtenti.add(createUITableUtenti(filtraPer));

        revalidate();
        repaint();
    }

    public void loadTable(ListaUtentiTableModel.FILTRA_PER filtraPer) {
        switch (filtraPer) {
            case CLIENTI -> utenti = UtenteBusiness.getInstance().loadClientiTable();
            case CUCINA -> utenti = UtenteBusiness.getInstance().loadCucinaTable();
            case AMMINISTRATORI -> utenti = UtenteBusiness.getInstance().loadAmministratoriTable();
        }

        listaUtentiTableModel = new ListaUtentiTableModel(utenti, filtraPer);
    }

    public Integer getUtenteSelezionato(){
        if(tableUtenti == null || tableUtenti.getSelectedRow() == -1){  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un utente");
            return null;
        } else
            return (Integer) utenti[tableUtenti.getSelectedRow()][0];
    }
}
