package it.unisalento.myfood.View.Panels.Gestione;

import it.unisalento.myfood.Business.TipologiaIngredienteBusiness;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Decorator.Menu.EditMenuDecorator;
import it.unisalento.myfood.View.Decorator.Menu.UtilMenu;
import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione.PulsantieraGestione;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione.PulsantieraGestioneTipologieIngredientiDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.Gestione.GestioneTipologieIngredientiListener;
import it.unisalento.myfood.View.ViewModel.ListaTipologiaTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class GestioneTipologieIngredientiPanel extends JPanel {
    private Frame frame;
    private Object[][] tipologie;
    private JTable table;
    private TipologiaIngredienteBusiness tipologiaProdottoBusiness = TipologiaIngredienteBusiness.getInstance();
    private JPanel pnlUserLogged = new JPanel();
    private JPanel northPan;

    private JPanel centralPan;
    private ActionListener gestioneTipologieIngredientiListener;


    public GestioneTipologieIngredientiPanel(Frame frame) {
        this.frame = frame;
        northPan = new JPanel(new GridLayout(2, 1));
        pnlUserLogged.setLayout(new BoxLayout(pnlUserLogged, BoxLayout.Y_AXIS));
        JPanel eastPan = new JPanel(new FlowLayout());
        centralPan = new JPanel(new GridLayout(2, 1));
        gestioneTipologieIngredientiListener = new GestioneTipologieIngredientiListener(this, frame);



        TextLabel title = new TextLabel("Gestione Tipologie Ingredenti");
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
            item.addActionListener(gestioneTipologieIngredientiListener);
        }

        menuBar.add(menu);

        frame.setJMenuBar(menuBar);
    }

    public void setPulsanti() {
        Pulsantiera pulsantieraGestione = new PulsantieraGestione();
        PulsantieraGestioneTipologieIngredientiDecorator pulsantieraGestioneTipologieIngredientiDecorator = new PulsantieraGestioneTipologieIngredientiDecorator(pulsantieraGestione);

        ArrayList<JButton> buttons = pulsantieraGestioneTipologieIngredientiDecorator.getPulsanti();


        JButton btnTornaDashboard = buttons.remove(0);
        btnTornaDashboard.addActionListener(gestioneTipologieIngredientiListener);
        JPanel pnlBtnDashboard = new JPanel(new FlowLayout());
        pnlBtnDashboard.add(btnTornaDashboard);
        add(pnlBtnDashboard, BorderLayout.WEST);

        JPanel pnlGridButtons = new JPanel(new GridLayout(buttons.size(), 1));


        for (JButton btn : buttons) {
            JPanel row = new JPanel(new FlowLayout());
            row.add(btn);
            pnlGridButtons.add(row);
            btn.addActionListener(gestioneTipologieIngredientiListener );
        }

        JPanel pnlFlowButtons = new JPanel(new FlowLayout());
        pnlFlowButtons.add(pnlGridButtons);

        add(pnlFlowButtons, BorderLayout.EAST);

        revalidate();
        repaint();

    }


    public void loadCatalogo() {
        centralPan.removeAll();

        tipologie = tipologiaProdottoBusiness.loadTipologie();

        ListaTipologiaTableModel listaTipologiaTableModel = new ListaTipologiaTableModel(tipologie);

        table = new JTable(listaTipologiaTableModel);
        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe
        table.setSelectionModel(selectionModel);

        table.setRowHeight(25);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);


        JScrollPane scrollPane = new JScrollPane(table);
        centralPan.add(scrollPane);
        centralPan.add(new JLabel());

    }


    public Integer getRowSelected() {
        if (table == null || table.getSelectedRow() == -1) {  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona una tipologia");
            return null;
        } else
            return (Integer) tipologie[table.getSelectedRow()][0];
    }


}
