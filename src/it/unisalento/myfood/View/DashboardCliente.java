package it.unisalento.myfood.View;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Business.IngredienteBusiness;
import it.unisalento.myfood.Business.TipologiaProdottoBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.UsernameTextLabelDecorator;
import it.unisalento.myfood.View.Decorator.PulsantieraDashboard.*;
import it.unisalento.myfood.View.Listener.DashboardListener;
import it.unisalento.myfood.View.ViewModel.ListaArticoliTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

//E' utile usare un panel col FlowLayout per ogni elemento perchè il FlowLayout non modifica le dimensioni del Component a differenza di Grid e Border
public class DashboardCliente extends JPanel implements Dashboard {
    private Frame frame;
    private DashboardListener dashboardListener;

    private Object[][] articoli;
    private String[] tipologie;
    private Object[][] ingredienti;
    private TipologiaProdottoBusiness tipologiaProdottoBusiness = TipologiaProdottoBusiness.getInstance();
    private UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();
    private PulsantieraGuest pulsantieraGuest;
    private ArrayList<JButton> buttons;
    private JPanel catPanel;
    private JTable table;
    private IngredienteBusiness ingredienteBusiness = IngredienteBusiness.getInstance();
    private JComboBox<String> ingComboBox;
    private JComboBox<String> tipComboBox;
    private String[] nomiIngredienti;

    private static JPanel pnlUserLogged = new JPanel();

    public DashboardCliente(Frame frame){
        this.frame = frame;
        dashboardListener = new DashboardListener(this, frame);

        pnlUserLogged.setLayout(new BoxLayout(pnlUserLogged, BoxLayout.Y_AXIS));

        setLayout(new BorderLayout());
        setLoggedUser();
        setPulsantiera();
        loadFilters();

        catPanel = new JPanel(new FlowLayout());

        if(tipologie != null && tipologie.length != 0)
            loadCatalogo(tipologie[0], null);

        add(catPanel, BorderLayout.CENTER);
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

        add(pnlUserLogged, BorderLayout.WEST);

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
                row.add(btn);
                pnlGridButtons.add(row);
                btn.addActionListener(dashboardListener);
            }

            JPanel pnlButtons = new JPanel(new FlowLayout());
            pnlButtons.add(pnlGridButtons);

            add(pnlButtons, BorderLayout.EAST);

        } else if(utenteBusiness.isLoggedCliente()) {
            pulsantieraGuest = new PulsantieraGuest();
            PulsantieraDashboard pulsantieraCliente = new PulsantieraClienteDecorator(pulsantieraGuest);
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

            add(pnlButtons, BorderLayout.EAST);
        }
    }


    /**
     * @param tipologiaProdotto se è null carica i menu
     * **/
    public void loadCatalogo(String tipologiaProdotto, Integer idIngrediente){
        catPanel.removeAll();

        ArticoloBusiness articoloBusiness = ArticoloBusiness.getInstance();

        if(tipologiaProdotto != null) {  //l'articolo in questione è un prodotto
            if(idIngrediente != null){   //filtro su ingredienti attivo

                articoli = articoloBusiness.filterByTipologiaEIngrediente(tipologiaProdotto, idIngrediente);
            } else {      //filtro su ingredienti non attivo
                articoli = articoloBusiness.filterByTipologia(tipologiaProdotto);  //filtra articolo per tipologia
            }

        } else {  //l'articolo in questione è un mene
            if(idIngrediente != null){      //filtro su ingredienti attivo
                    articoli = articoloBusiness.filterMenuByIngrediente(idIngrediente);
            } else{   //filtro su ingredienti non attivo
                articoli = articoloBusiness.findMenu();   //carica tutti i menu
            }
        }

        ListaArticoliTableModel listaProdottiTableModel = new ListaArticoliTableModel(articoli);

        table = new JTable(listaProdottiTableModel);
        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe
        table.setSelectionModel(selectionModel);

        table.setRowHeight(100);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);


        JScrollPane scrollPane = new JScrollPane(table);
        catPanel.add(scrollPane);

        revalidate();
        repaint();

    }


    public void loadFilters(){
        JPanel panel = new JPanel(new FlowLayout());
        tipologie = tipologiaProdottoBusiness.loadTipologie();
        ingredienti = ingredienteBusiness.getIngredienti();

        nomiIngredienti = extractIngredientiName(ingredienti, 1);

        ingComboBox = new JComboBox<>(nomiIngredienti);

        tipComboBox = new JComboBox<>(tipologie);
        tipComboBox.addActionListener(dashboardListener);
        tipComboBox.setActionCommand(DashboardListener.TIP_COMBOBOX);
        ingComboBox.addActionListener(dashboardListener);
        ingComboBox.setActionCommand(DashboardListener.ING_COMBOBOX);
        panel.add(tipComboBox);
        panel.add(ingComboBox);
        add(panel, BorderLayout.NORTH);
    }

    public Integer getArticoloSelezionato(){
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

    public String getSelectedTipologia(){
            if("Menu".equals(tipComboBox.getSelectedItem()))
                return null;
        return (String) tipComboBox.getSelectedItem();
    }

    public Integer getSelectedIngrediente(){

        if("-".equals(ingComboBox.getSelectedItem()))
            return null;
        return (Integer) ingredienti[ingComboBox.getSelectedIndex() - 1][0];
    }
}
