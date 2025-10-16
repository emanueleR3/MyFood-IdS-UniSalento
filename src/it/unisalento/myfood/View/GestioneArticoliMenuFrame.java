package it.unisalento.myfood.View;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Label.BlackTextLabelSecondariaDecorator;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.Listener.Gestione.GestioneArticoliMenuFrameListener;
import it.unisalento.myfood.Listener.Gestione.WindowListenerFrame;
import it.unisalento.myfood.View.Panels.Edit.EditMenuPanel;
import it.unisalento.myfood.View.ViewModel.ListaArticoliTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GestioneArticoliMenuFrame extends JFrame {

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int FRAME_WIDTH = 930;
    private static final int FRAME_HEIGHT = 605;

    private static JPanel panel = new JPanel();
    private static JTable leftTable;
    private static JTable rightTable;
    private static JButton addButton;
    private static JButton removeButton;
    private static JButton confirmButton;
    private EditMenuPanel pnlEditMenu;
    private JScrollPane leftScroll;
    private JScrollPane rightScroll;

    private Integer idMenuSelezionato;

    private GestioneArticoliMenuFrameListener listener;

    private String[] tipologieProdotto;
    private JComboBox<String> tipComboBox;

    private Object[][] articoliCatalogo;
    private Object[][] articoliMenu;

    private BlackTextLabelSecondariaDecorator decTipologia;
    private BlackTextLabelSecondariaDecorator decComposizione;

    private ArticoloBusiness articoloBusiness = ArticoloBusiness.getInstance();

    private ArrayList<String> tipologieAccettate;
    private String composizioneScelta;

    private String tipologiaProdottoSelezionata;

    private static WindowListenerFrame windowListener;

    public GestioneArticoliMenuFrame(ArrayList<String> tipologieAccettate, EditMenuPanel pnlEditMenu) {
        this.pnlEditMenu = pnlEditMenu;
        this.tipologieAccettate = tipologieAccettate;

        if (UtenteBusiness.OPERATIONS.MODIFICA.equals(UtenteBusiness.getSession().get(UtenteBusiness.OPERATION))) {
            idMenuSelezionato = (Integer) articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.ID);
        }

        composizioneScelta = pnlEditMenu.getComposizioneScelta();

        windowListener = new WindowListenerFrame(this);
        addWindowListener(windowListener);

        setLayout(new FlowLayout());
        setTitle("Gestione Articoli nel Menu");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        int x = ((SCREEN_SIZE.width - getWidth()) / 2) - FRAME_WIDTH / 2;
        int y = ((SCREEN_SIZE.height - getHeight()) / 2) - FRAME_HEIGHT / 2;
        setLocation(x, y);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);   // per permettere al WindowListener di poter comandare sulla finestra
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);

        initComponents(tipologieAccettate.get(0));

        if(UtenteBusiness.getSession().get(UtenteBusiness.OPERATION).equals(UtenteBusiness.OPERATIONS.VISUALIZZA))
            setViewMode();

        setVisible(true);
    }

    public void initComponents(String filtroTipologia) {
        panel.removeAll();

        tipologiaProdottoSelezionata = filtroTipologia;

        listener = new GestioneArticoliMenuFrameListener(this);

        ListSelectionModel selectionModelLeft = new DefaultListSelectionModel();
        selectionModelLeft.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe

        ListSelectionModel selectionModelRight = new DefaultListSelectionModel();
        selectionModelRight.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe

        loadCatalogo(filtroTipologia);

        ListaArticoliTableModel catalogo = new ListaArticoliTableModel(articoliCatalogo);
        leftTable = new JTable(catalogo);
        leftTable.setSelectionModel(selectionModelLeft);
        leftTable.setRowHeight(80);

        articoliMenu = articoloBusiness.getArticoliSelezionati(pnlEditMenu.getIdArticoliInseriti());

        ListaArticoliTableModel menu = new ListaArticoliTableModel(articoliMenu);
        rightTable = new JTable(menu);
        rightTable.setSelectionModel(selectionModelRight);
        rightTable.setRowHeight(80);

        addButton = new JButton("Aggiungi al Menu");
        addButton.addActionListener(listener);
        addButton.setActionCommand(GestioneArticoliMenuFrameListener.BTN_ADD);

        removeButton = new JButton("Rimuovi dal Menu");
        removeButton.addActionListener(listener);
        removeButton.setActionCommand(GestioneArticoliMenuFrameListener.BTN_REMOVE);

        confirmButton = new JButton("Conferma e Salva");
        confirmButton.addActionListener(listener);
        confirmButton.setActionCommand(GestioneArticoliMenuFrameListener.BTN_CONFERMA_SALVA);

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
        JPanel pnlInfoFrame = new JPanel();
        pnlInfoFrame.setLayout(new BoxLayout(pnlInfoFrame, BoxLayout.Y_AXIS));

        JPanel pnlFiltri = new JPanel(new FlowLayout());

        TextLabel lblTipologia = new TextLabel("Tipologia prodotto: ");
        decTipologia = new BlackTextLabelSecondariaDecorator(lblTipologia);
        JPanel pnlTipologia = new JPanel(new FlowLayout());
        pnlTipologia.add(decTipologia.getLabel());
        pnlFiltri.add(pnlTipologia);

        tipologieProdotto = tipologieAccettate.toArray(new String[0]);

        tipComboBox = new JComboBox<>(tipologieProdotto);

        if (tipologiaProdottoSelezionata != null) {
            tipComboBox.setSelectedItem(tipologiaProdottoSelezionata);
        }
        tipComboBox.addActionListener(listener);

        pnlFiltri.add(tipComboBox);

        pnlInfoFrame.add(pnlFiltri);

        TextLabel lblComposizione = new TextLabel("Composizione scelta: " + composizioneScelta);
        decComposizione = new BlackTextLabelSecondariaDecorator(lblComposizione);
        JPanel pnlComposizione = new JPanel(new FlowLayout());
        pnlComposizione.add(decComposizione.getLabel());
        pnlInfoFrame.add(pnlComposizione);

        JPanel pnlFinalInfo = new JPanel(new FlowLayout());
        pnlFinalInfo.add(pnlInfoFrame);

        panel.add(pnlFinalInfo, BorderLayout.NORTH);
    }

    private JPanel createButtonPanel(JButton button1, JButton button2) {
        JPanel pblButtons = new JPanel();
        pblButtons.setLayout(new GridLayout(2, 1, 5, 5));
        pblButtons.add(button1);
        pblButtons.add(button2);
        return pblButtons;
    }

    private void setViewMode(){
        addButton.setVisible(false);
        removeButton.setVisible(false);
        confirmButton.setVisible(false);
        leftScroll.setVisible(false);
        tipComboBox.setVisible(false);
        decTipologia.getLabel().setVisible(false);

    }

    public void loadCatalogo(String tipologiaProdotto) {
        if ("Menu".equals(tipologiaProdotto)) {
            articoliCatalogo = articoloBusiness.findMenu();
        } else {
            articoliCatalogo = articoloBusiness.filterByTipologiaProdotto(tipologiaProdotto);
        }
    }

    public String getTipologiaProdottoSelezionata() {
        return (String) tipComboBox.getSelectedItem();
    }

    public Integer getLeftTableRowSelected(){
        if(leftTable == null || leftTable.getSelectedRow() == -1){  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un articolo dal catalogo degli articoli della tabella di sinistra!");
            return null;
        } else
            return (Integer) articoliCatalogo[leftTable.getSelectedRow()][0];
    }

    public Integer getRightTableRowSelected(){
        if(rightTable == null || rightTable.getSelectedRow() == -1){  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un articolo da quelli inseriti nella tabella di destra!");
            return null;
        } else
            return (Integer) articoliMenu[rightTable.getSelectedRow()][0];
    }

    public ArrayList<Integer> getIdArticoliSelezionati() {
        return pnlEditMenu.getIdArticoliInseriti();
    }

    public void setEnableComboBoxComposizioni(boolean enable) {
        pnlEditMenu.getCBoxComposizioni().setEnabled(enable);
    }

    public String getComposizioneScelta() {
        return composizioneScelta;
    }

    public Integer getIdMenuSelezionato() {
        return idMenuSelezionato;
    }
}