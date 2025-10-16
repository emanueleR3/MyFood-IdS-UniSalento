package it.unisalento.myfood.View.Panels;

import it.unisalento.myfood.Business.*;
import it.unisalento.myfood.View.MultilineCellRenderer;
import it.unisalento.myfood.View.Decorator.Label.BlackTextLabelSecondariaDecorator;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Decorator.Label.UsernameTextLabelDecorator;
import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraDashboard.*;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.DashboardListener;
import it.unisalento.myfood.View.ViewModel.ListaArticoliTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

//E' utile usare un panel col FlowLayout per ogni elemento perchè il FlowLayout non modifica le dimensioni del Component a differenza di Grid e Border
public class DashboardCliente extends JPanel implements IDashboard {
    private Frame frame;
    private DashboardListener dashboardListener;

    private Object[][] articoli;
    private String[] tipologieProdotto;
    private Object[][] tipologieIngrediente;
    private TipologiaProdottoBusiness tipologiaProdottoBusiness = TipologiaProdottoBusiness.getInstance();
    private UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();
    private PulsantieraGuest pulsantieraGuest;
    private ArrayList<JButton> buttons;
    private JTable table;
    private TipologiaIngredienteBusiness tipologiaIngredienteBusiness = TipologiaIngredienteBusiness.getInstance();
    private JComboBox<String> ingComboBox;
    private JComboBox<String> tipComboBox;
    private String[] nomiTipologieIngrediente;

    private JPanel pnlUserLogged = new JPanel();
    private JPanel northPan;
    private JPanel eastGrid;
    private JPanel centralPan;


    public DashboardCliente(Frame frame){
        this.frame = frame;
        dashboardListener = new DashboardListener(this, frame);
        northPan = new JPanel(new GridLayout(2, 1));
        pnlUserLogged.setLayout(new BoxLayout(pnlUserLogged, BoxLayout.Y_AXIS));
        JPanel eastPan = new JPanel(new FlowLayout());

        eastGrid = new JPanel(new GridLayout(2, 1));
        eastPan.add(eastGrid);
        TextLabel title = new TextLabel("Catalogo");
        TitleTextLabelDecorator decTitle = new TitleTextLabelDecorator(title);
        JPanel titPan = new JPanel(new FlowLayout());
        titPan.add(decTitle.getLabel());
        northPan.add(titPan);

        setLayout(new BorderLayout());
        setLoggedUser();
        setPulsantiera();
        loadFilters();

        centralPan = new JPanel(new GridLayout(2, 1));


        if(tipologieProdotto != null && tipologieProdotto.length != 0) {
            loadCatalogo(tipologieProdotto[0], null);
        } else {
            // Il catalogo è vuoto
            loadCatalogo(null, null);
        }

        add(northPan, BorderLayout.NORTH);
        add(eastPan, BorderLayout.EAST);
        add(centralPan, BorderLayout.CENTER);
    }


    public void setLoggedUser(){
        pnlUserLogged.removeAll();
        TextLabel user = null;

        if(utenteBusiness.isLoggedGuest())
            user = new TextLabel("Guest");
        if(utenteBusiness.isLoggedCliente())
            user = new TextLabel("Cliente: " + utenteBusiness.getLoggedInName() + " " + utenteBusiness.getLoggedInCognome());

        UsernameTextLabelDecorator usernameTextLabel = new UsernameTextLabelDecorator(user);

        //JPanel row = new JPanel(new FlowLayout());
        //row.add(usernameTextLabel.getLabel());
        pnlUserLogged.add(usernameTextLabel.getLabel());
        pnlUserLogged.add(Box.createVerticalStrut(10));

        eastGrid.add(pnlUserLogged);
    }

    public void setPulsantiera() {

        if (utenteBusiness.isLoggedGuest()) {
            pulsantieraGuest = new PulsantieraGuest();
            ArrayList<JButton> buttons = (ArrayList<JButton>) pulsantieraGuest.getPulsanti();
            JPanel pnlGridButtons = new JPanel(new GridLayout(buttons.size(), 1));

            Iterator<JButton> iterator = buttons.iterator();
            while(iterator.hasNext()){
                JButton btn = iterator.next();
                JPanel row = new JPanel(new FlowLayout());
                btn.setPreferredSize(new Dimension(180, 25));
                row.add(btn);
                pnlGridButtons.add(row);
                btn.addActionListener(dashboardListener);
            }

            JPanel pnlButtons = new JPanel(new FlowLayout());
            pnlButtons.add(pnlGridButtons);

            eastGrid.add(pnlGridButtons);

            //add(pnlButtons, BorderLayout.EAST);

        } else if(utenteBusiness.isLoggedCliente()) {
            pulsantieraGuest = new PulsantieraGuest();
            Pulsantiera pulsantieraCliente = new PulsantieraClienteDecorator(pulsantieraGuest);
            buttons = (ArrayList<JButton>) pulsantieraCliente.getPulsanti();

            JButton btnLogout = buttons.remove(0);
            btnLogout.addActionListener(dashboardListener);
            pnlUserLogged.add(btnLogout);

            JPanel pnlGridButtons = new JPanel(new GridLayout(buttons.size(), 1));

            for (JButton btn : buttons) {
                JPanel row = new JPanel(new FlowLayout());
                btn.setPreferredSize(new Dimension(180, 25));
                row.add(btn);
                pnlGridButtons.add(row);
                btn.addActionListener(dashboardListener);
            }

            JPanel pnlButtons = new JPanel(new FlowLayout());
            pnlButtons.add(pnlGridButtons);

            eastGrid.add(pnlButtons);
        }
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
        tipologieIngrediente = tipologiaIngredienteBusiness.loadTipologie();

        nomiTipologieIngrediente = extractIngredientiName(tipologieIngrediente, 1);

        ingComboBox = new JComboBox<>(nomiTipologieIngrediente);

        tipComboBox = new JComboBox<>(tipologieProdotto);
        tipComboBox.addActionListener(dashboardListener);
        tipComboBox.setActionCommand(DashboardListener.TIP_COMBOBOX);
        ingComboBox.addActionListener(dashboardListener);
        ingComboBox.setActionCommand(DashboardListener.ING_COMBOBOX);

        pnlFiltroTipologia.add(tipComboBox);
        pnlFiltroIngrediente.add(ingComboBox);

        pnlFiltri.add(pnlFiltroTipologia);
        pnlFiltri.add(pnlFiltroIngrediente);

        JPanel filtri = new JPanel(new FlowLayout());
        filtri.add(pnlFiltri);

        northPan.add(filtri);
    }


    @Override
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
