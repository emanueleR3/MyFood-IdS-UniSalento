package it.unisalento.myfood.View;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Business.IngredienteBusiness;
import it.unisalento.myfood.Business.TipologiaIngredienteBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Label.BlackTextLabelSecondariaDecorator;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.Listener.Gestione.GestioneIngredientiProdottoFrameListener;
import it.unisalento.myfood.View.Panels.Edit.EditProdottoPanel;
import it.unisalento.myfood.View.ViewModel.ListaIngredientiTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class GestioneIngredientiProdottoFrame extends JFrame {

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int FRAME_WIDTH = 930;
    private static final int FRAME_HEIGHT = 570;

    private static JPanel panel = new JPanel();
    private static JTable leftTable;
    private static JTable rightTable;
    private static JButton addButton;
    private static JButton removeButton;
    private static JButton confirmButton;

    private BlackTextLabelSecondariaDecorator decTipologia;

    private JScrollPane leftScroll;
    private JScrollPane rightScroll;

    private EditProdottoPanel pnlEditProdotto;

    private GestioneIngredientiProdottoFrameListener listener;

    private final ArticoloBusiness articoloBusiness = ArticoloBusiness.getInstance();

    private JComboBox<String> tipComboBox;

    private String tipologiaIngredienteSelezionata;

    private Object[][] ingredientiLeftTable;
    private Object[][] ingredientiRightTable;

    public GestioneIngredientiProdottoFrame(EditProdottoPanel panel) {
        this.pnlEditProdotto = panel;

        setLayout(new FlowLayout());
        setTitle("Gestione Ingredienti nel Prodotto");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        int x = ((SCREEN_SIZE.width - getWidth()) / 2) - FRAME_WIDTH / 2;
        int y = ((SCREEN_SIZE.height - getHeight()) / 2) - FRAME_HEIGHT / 2;
        setLocation(x, y);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);

        initComponents(null);

        if(UtenteBusiness.getSession().get(UtenteBusiness.OPERATION).equals(UtenteBusiness.OPERATIONS.VISUALIZZA))
            setViewMode();

        setVisible(true);
    }

    public void initComponents(String filtroTipologia) {
        panel.removeAll();

        ListSelectionModel selectionModelLeft = new DefaultListSelectionModel();
        selectionModelLeft.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe

        ListSelectionModel selectionModelRight = new DefaultListSelectionModel();
        selectionModelRight.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe

        tipologiaIngredienteSelezionata = filtroTipologia;

        listener = new GestioneIngredientiProdottoFrameListener(this);

        if (filtroTipologia == null) {
            ingredientiLeftTable = IngredienteBusiness.getInstance().getIngredienti();
        } else {
            ingredientiLeftTable = IngredienteBusiness.getInstance().getIngredientiPerTipologia(filtroTipologia);
        }

        ListaIngredientiTableModel tableAllIngredienti = new ListaIngredientiTableModel(ingredientiLeftTable);
        leftTable = new JTable(tableAllIngredienti);
        leftTable.setRowHeight(40);
        leftTable.setSelectionModel(selectionModelLeft);

        ingredientiRightTable = IngredienteBusiness.getInstance().getIngredientiSelezionati(pnlEditProdotto.getIdIngredientiInseriti());

        ListaIngredientiTableModel tableIngredientiProdotto = new ListaIngredientiTableModel(ingredientiRightTable);
        rightTable = new JTable(tableIngredientiProdotto);
        rightTable.setRowHeight(40);
        rightTable.setSelectionModel(selectionModelRight);

        addButton = new JButton("Aggiungi al Prodotto");
        addButton.addActionListener(listener);
        addButton.setActionCommand(GestioneIngredientiProdottoFrameListener.BTN_ADD);

        removeButton = new JButton("Rimuovi dal Prodotto");
        removeButton.addActionListener(listener);
        removeButton.setActionCommand(GestioneIngredientiProdottoFrameListener.BTN_REMOVE);

        confirmButton = new JButton("Conferma e Salva");
        confirmButton.addActionListener(listener);
        confirmButton.setActionCommand(GestioneIngredientiProdottoFrameListener.BTN_CONFERMA_SALVA);

        layoutComponents();
    }

    private void layoutComponents() {
        panel.setLayout(new BorderLayout());

        loadFilters();

        leftScroll = new JScrollPane(leftTable);
        rightScroll = new JScrollPane(rightTable);

        panel.add(leftScroll, BorderLayout.WEST);
        panel.add(rightScroll, BorderLayout.EAST);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 2, 5, 5));
        southPanel.add(createButtonPanel(addButton, removeButton));
        southPanel.add(confirmButton);
        panel.add(southPanel, BorderLayout.SOUTH);

        add(panel);

        revalidate();
        repaint();
    }

    private void loadFilters() {

        JPanel pnlFiltri = new JPanel(new FlowLayout());

        TextLabel lblTipologia = new TextLabel("Tipologia ingrediente: ");
        decTipologia = new BlackTextLabelSecondariaDecorator(lblTipologia);
        JPanel pnlTipologia = new JPanel(new FlowLayout());
        pnlTipologia.add(decTipologia.getLabel());
        pnlFiltri.add(pnlTipologia);

        Object[][] tipologieIngrediente = TipologiaIngredienteBusiness.getInstance().loadTipologie();
        String[] nomiTipologieIngrediente = extractIngredientiName(tipologieIngrediente, 1);

        tipComboBox = new JComboBox<>(nomiTipologieIngrediente);

        if (tipologiaIngredienteSelezionata != null) {
            tipComboBox.setSelectedItem(tipologiaIngredienteSelezionata);
        }
        tipComboBox.addActionListener(listener);

        pnlFiltri.add(tipComboBox);

        panel.add(pnlFiltri, BorderLayout.NORTH);
    }

    private JPanel createButtonPanel(JButton button1, JButton button2) {
        JPanel pblButtons = new JPanel();
        pblButtons.setLayout(new GridLayout(2, 1, 5, 5));
        pblButtons.add(button1);
        pblButtons.add(button2);
        return pblButtons;
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


    private void setViewMode(){
     leftScroll.setVisible(false);
     tipComboBox.setVisible(false);
     decTipologia.getLabel().setVisible(false);
     addButton.setVisible(false);
     removeButton.setVisible(false);
     confirmButton.setVisible(false);
     revalidate();
     repaint();

    }

    public ArrayList<Integer> getIdIngredientiSelezionati() {
        return pnlEditProdotto.getIdIngredientiInseriti();
    }

    public String getTipologiaIngredienteSelezionata() {
        return (String) tipComboBox.getSelectedItem();
    }

    public Integer getLeftTableRowSelected(){
        if(leftTable == null || leftTable.getSelectedRow() == -1){  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un ingrediente dal catalogo degli ingredienti della tabella di sinistra!");
            return null;
        } else
            return (Integer) ingredientiLeftTable[leftTable.getSelectedRow()][0];
    }

    public Integer getRightTableRowSelected(){
        if(rightTable == null || rightTable.getSelectedRow() == -1){  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un ingrediente da quelli inseriti nella tabella di destra!");
            return null;
        } else
            return (Integer) ingredientiRightTable[rightTable.getSelectedRow()][0];
    }
}