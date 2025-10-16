package it.unisalento.myfood.View.Panels.Gestione;

import it.unisalento.myfood.Business.*;
import it.unisalento.myfood.View.MultilineCellRenderer;
import it.unisalento.myfood.View.Decorator.Label.BlackTextLabelSecondariaDecorator;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Decorator.Menu.EditMenuDecorator;
import it.unisalento.myfood.View.Decorator.Menu.UtilMenu;
import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione.PulsantieraGestione;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione.PulsantieraGestioneArticoliDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.DashboardListener;
import it.unisalento.myfood.Listener.Gestione.GestioneArticoliListener;
import it.unisalento.myfood.View.ViewModel.ListaArticoliTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class GestioneArticoliPanel extends JPanel{
    private Frame frame;

    private Object[][] articoli;
    private String[] tipologieProdotto;
    private Object[][] tipologieIngredienti;
    private TipologiaProdottoBusiness tipologiaProdottoBusiness = TipologiaProdottoBusiness.getInstance();
    private UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();
    private JTable table;
    private TipologiaIngredienteBusiness tipologiaIngredienteBusiness = TipologiaIngredienteBusiness.getInstance();
    private JComboBox<String> ingComboBox;
    private JComboBox<String> tipComboBox;
    private String[] nomiTipologieIngredienti;
    private GestioneArticoliListener gestioneArticoliListener;

    private JPanel pnlUserLogged = new JPanel();
    private JPanel northPan;

    private JPanel centralPan;


    public GestioneArticoliPanel(Frame frame){
        this.frame = frame;
        gestioneArticoliListener = new GestioneArticoliListener(this, frame);
        northPan = new JPanel(new GridLayout(2, 1));
        pnlUserLogged.setLayout(new BoxLayout(pnlUserLogged, BoxLayout.Y_AXIS));
        JPanel eastPan = new JPanel(new FlowLayout());


        TextLabel title = new TextLabel("Gestione Articoli");
        TitleTextLabelDecorator decTitle = new TitleTextLabelDecorator(title);
        JPanel titPan = new JPanel(new FlowLayout());
        titPan.add(decTitle.getLabel());
        northPan.add(titPan);

        setLayout(new BorderLayout());
        setMenuBar();
        setPulsanti();
        loadFilters();

        centralPan = new JPanel(new GridLayout(2, 1));

        if(tipologieProdotto != null && tipologieProdotto.length != 0) {
            loadCatalogo(tipologieProdotto[0], null);
        } else {
            loadCatalogo(null, null);
        }

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
            item.addActionListener(gestioneArticoliListener);
        }

        menuBar.add(menu);

        frame.setJMenuBar(menuBar);

    }

    public void setPulsanti() {
        Pulsantiera pulsantieraGestione = new PulsantieraGestione();
        PulsantieraGestioneArticoliDecorator pulsantieraGestioneArticoli = new PulsantieraGestioneArticoliDecorator(pulsantieraGestione);

        ArrayList<JButton> buttons = pulsantieraGestioneArticoli.getPulsanti();

        JButton btnTornaDashboard = buttons.remove(0);
        btnTornaDashboard.addActionListener(gestioneArticoliListener);
        JPanel pnlBtnDashboard = new JPanel(new FlowLayout());
        pnlBtnDashboard.add(btnTornaDashboard);
        add(pnlBtnDashboard, BorderLayout.WEST);

        JPanel pnlGridButtons = new JPanel(new GridLayout(buttons.size(), 1));


        for (JButton btn : buttons) {
            JPanel row = new JPanel(new FlowLayout());
            row.add(btn);
            pnlGridButtons.add(row);
            btn.addActionListener(gestioneArticoliListener);
        }

        JPanel pnlFlowButtons = new JPanel(new FlowLayout());
        pnlFlowButtons.add(pnlGridButtons);

        add(pnlFlowButtons, BorderLayout.EAST);

        revalidate();
        repaint();

    }


    /**
     * @param tipologiaProdotto se è null carica i menu
     * **/
    public void loadCatalogo(String tipologiaProdotto, String tipologiaIngrediente){
        centralPan.removeAll();

        ArticoloBusiness articoloBusiness = ArticoloBusiness.getInstance();

        if(tipologiaProdotto != null) {  //l'articolo in questione è un prodotto
            if(tipologiaIngrediente != null){   //filtro su ingredienti attivo
                articoli = articoloBusiness.filterByTipologie(tipologiaProdotto, tipologiaIngrediente);
            } else {      //filtro su ingredienti non attivo
                articoli = articoloBusiness.filterByTipologiaProdotto(tipologiaProdotto);  //filtra articolo per tipologia
            }

        } else {  //l'articolo in questione è un menu
            if(tipologiaIngrediente != null){      //filtro su ingredienti attivo
                articoli = articoloBusiness.filterMenuByTipologiaIngrediente(tipologiaIngrediente);
            } else{   //filtro su ingredienti non attivo
                articoli = articoloBusiness.findMenu();   //carica tutti i menu
            }
        }

        ListaArticoliTableModel listaProdottiTableModel = new ListaArticoliTableModel(articoli);

        table = new JTable(listaProdottiTableModel);
        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe
        table.setSelectionModel(selectionModel);

        table.setDefaultRenderer(Object.class, new MultilineCellRenderer());

        table.setRowHeight(100);

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);


        JScrollPane scrollPane = new JScrollPane(table);
        centralPan.add(scrollPane);
        centralPan.add(new JLabel());

       revalidate();
       repaint();
    }


    public void loadFilters(){

        JPanel pnlFiltri = new JPanel();
        pnlFiltri.setLayout(new BoxLayout(pnlFiltri, BoxLayout.Y_AXIS));

        JPanel pnlFiltroTipologia = new JPanel(new FlowLayout());

        TextLabel lblTipologia = new TextLabel("Tipologia prodotto: ");
        BlackTextLabelSecondariaDecorator decTipologia = new BlackTextLabelSecondariaDecorator(lblTipologia);
        JPanel pnlTipologia = new JPanel(new FlowLayout());
        pnlTipologia.add(decTipologia.getLabel());
        pnlFiltroTipologia.add(pnlTipologia);

        JPanel pnlFiltroIngrediente = new JPanel(new FlowLayout());

        TextLabel lblIngredienti = new TextLabel("Tipologia ingredienti: ");
        BlackTextLabelSecondariaDecorator decIngredienti = new BlackTextLabelSecondariaDecorator(lblIngredienti);
        JPanel pnlIngredienti = new JPanel(new FlowLayout());
        pnlIngredienti.add(decIngredienti.getLabel());
        pnlFiltroIngrediente.add(pnlIngredienti);


        tipologieProdotto = tipologiaProdottoBusiness.loadNomiTipologie();
        tipologieIngredienti = tipologiaIngredienteBusiness.loadTipologie();

        nomiTipologieIngredienti = extractIngredientiName(tipologieIngredienti, 1);

        ingComboBox = new JComboBox<>(nomiTipologieIngredienti);

        tipComboBox = new JComboBox<>(tipologieProdotto);
        tipComboBox.addActionListener(gestioneArticoliListener);
        tipComboBox.setActionCommand(DashboardListener.TIP_COMBOBOX);
        ingComboBox.addActionListener(gestioneArticoliListener);
        ingComboBox.setActionCommand(DashboardListener.ING_COMBOBOX);

        pnlFiltroTipologia.add(tipComboBox);
        pnlFiltroIngrediente.add(ingComboBox);

        pnlFiltri.add(pnlFiltroTipologia);
        pnlFiltri.add(pnlFiltroIngrediente);

        JPanel filtri = new JPanel(new FlowLayout());
        filtri.add(pnlFiltri);

        northPan.add(filtri);
    }


    public Integer getRowSelected(){
        if(table == null || table.getSelectedRow() == -1){  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un articolo");
            return null;
        } else
            return (Integer) articoli[table.getSelectedRow()][0];
    }

    private String[] extractIngredientiName(Object[][] mat, int columnIndex){
        String[] nomi = new String[mat.length + 1];

        int i = 0;
        nomi[i] = "-";
        Iterator<Object[]> iterator = Arrays.stream(mat).iterator();
        while (iterator.hasNext()){
            i++;
            nomi[i] = (String) iterator.next()[columnIndex];
        }

        return nomi;
    }

    public String getSelectedTipologiaProdotto(){
        if("Menu".equals(tipComboBox.getSelectedItem()))
            return null;
        return (String) tipComboBox.getSelectedItem();
    }

    public String getSelectedTipologiaIngrediente(){

        if("-".equals(ingComboBox.getSelectedItem()))
            return null;
        return (String) ingComboBox.getSelectedItem();
    }
}
