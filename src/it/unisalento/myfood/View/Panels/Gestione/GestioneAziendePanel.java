package it.unisalento.myfood.View.Panels.Gestione;

import it.unisalento.myfood.Business.AziendaBusiness;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Decorator.Menu.EditMenuDecorator;
import it.unisalento.myfood.View.Decorator.Menu.UtilMenu;
import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione.PulsantieraGestione;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione.PulsantieraGestioneAziendeDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.Gestione.GestioneAziendeListener;
import it.unisalento.myfood.View.ViewModel.ListaAziendeTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class GestioneAziendePanel extends JPanel {
    private Frame frame;
    private Object[][] aziende;
    private JTable table;
    private AziendaBusiness aziendaBusiness = AziendaBusiness.getInstance();

    private JPanel pnlUserLogged = new JPanel();
    private JPanel northPan;

    private JPanel centralPan;
    private ActionListener gestioneAziendeListener;


    public GestioneAziendePanel(Frame frame) {
        this.frame = frame;
        northPan = new JPanel(new GridLayout(2, 1));
        pnlUserLogged.setLayout(new BoxLayout(pnlUserLogged, BoxLayout.Y_AXIS));
        JPanel eastPan = new JPanel(new FlowLayout());
        centralPan = new JPanel(new GridLayout(2, 1));
        gestioneAziendeListener = new GestioneAziendeListener(this, frame);


        TextLabel title = new TextLabel("Gestione Aziende");
        TitleTextLabelDecorator decTitle = new TitleTextLabelDecorator(title);
        JPanel titPan = new JPanel(new FlowLayout());
        titPan.add(decTitle.getLabel());
        northPan.add(titPan);

        setLayout(new BorderLayout());
        setMenuBar();
        setPulsanti();
        loadCatalogo();


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
            item.addActionListener(gestioneAziendeListener);
        }

        menuBar.add(menu);

        frame.setJMenuBar(menuBar);
    }


    public void setPulsanti() {
        Pulsantiera pulsantieraGestione = new PulsantieraGestione();
        PulsantieraGestioneAziendeDecorator pulsantieraGestioneAziendeDecorator = new PulsantieraGestioneAziendeDecorator(pulsantieraGestione);

        ArrayList<JButton> buttons = pulsantieraGestioneAziendeDecorator.getPulsanti();



        JButton btnTornaDashboard = buttons.remove(0);
        btnTornaDashboard.addActionListener(gestioneAziendeListener);
        JPanel pnlBtnDashboard = new JPanel(new FlowLayout());
        pnlBtnDashboard.add(btnTornaDashboard);
        add(pnlBtnDashboard, BorderLayout.WEST);

        JPanel pnlGridButtons = new JPanel(new GridLayout(buttons.size(), 1));


        for (JButton btn : buttons) {
            JPanel row = new JPanel(new FlowLayout());
            row.add(btn);
            pnlGridButtons.add(row);
            btn.addActionListener(gestioneAziendeListener);
        }

        JPanel pnlFlowButtons = new JPanel(new FlowLayout());
        pnlFlowButtons.add(pnlGridButtons);

        add(pnlFlowButtons, BorderLayout.EAST);

        revalidate();
        repaint();

    }


    public void loadCatalogo() {
        centralPan.removeAll();

        aziende = aziendaBusiness.getAziende();

        ListaAziendeTableModel listaAziendeTableModel = new ListaAziendeTableModel(aziende);

        table = new JTable(listaAziendeTableModel);
        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe
        table.setSelectionModel(selectionModel);

        table.setRowHeight(25);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);


        JScrollPane scrollPane = new JScrollPane(table);
        centralPan.add(scrollPane);
        centralPan.add(new JLabel());

    }


    public Integer getRowSelected() {
        if (table == null || table.getSelectedRow() == -1) {  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un'azienda");
            return null;
        } else
            return (Integer) aziende[table.getSelectedRow()][0];
    }


}
