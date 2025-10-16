package it.unisalento.myfood.View;

import it.unisalento.myfood.Business.AziendaBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.Listener.Gestione.GestioneDistributoriIngredienteFrameListener;
import it.unisalento.myfood.Listener.Gestione.GestioneProduttoreIngredienteFrameListener;
import it.unisalento.myfood.View.Panels.Edit.EditIngredientePanel;
import it.unisalento.myfood.View.ViewModel.ListaAziendeTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GestioneDistributoriIngredienteFrame extends JFrame {

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int FRAME_WIDTH = 930;
    private static final int FRAME_HEIGHT = 570;

    private static JPanel panel = new JPanel();
    private static JTable leftTable;
    private static JTable rightTable;
    private static JButton addButton;
    private static JButton removeButton;
    private static JButton confirmButton;
    private static JPanel pnlNorth;

    private JScrollPane leftScroll;
    private JScrollPane rightScroll;
    private AziendaBusiness aziendaBusiness = AziendaBusiness.getInstance();

    private EditIngredientePanel pnlEditIngrediente;

    private GestioneDistributoriIngredienteFrameListener listener;


    private Object[][] distributoriLeftTable;
    private Object[][] distributoriRightTable;

    public GestioneDistributoriIngredienteFrame(EditIngredientePanel panel) {
        this.pnlEditIngrediente = panel;

        setLayout(new FlowLayout());
        setTitle("Gestione distributori dell'ingrediente");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        int x = ((SCREEN_SIZE.width - getWidth()) / 2) - FRAME_WIDTH / 2;
        int y = ((SCREEN_SIZE.height - getHeight()) / 2) - FRAME_HEIGHT / 2;
        setLocation(x, y);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);

        initComponents();

        if(UtenteBusiness.getSession().get(UtenteBusiness.OPERATION).equals(UtenteBusiness.OPERATIONS.VISUALIZZA))
            setViewMode();

        setVisible(true);
    }

    public void initComponents() {
        panel.removeAll();

        ListSelectionModel selectionModelLeft = new DefaultListSelectionModel();
        selectionModelLeft.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe

        ListSelectionModel selectionModelRight = new DefaultListSelectionModel();
        selectionModelRight.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe

        listener = new GestioneDistributoriIngredienteFrameListener(this);

        pnlNorth = new JPanel(new FlowLayout());

        distributoriLeftTable = aziendaBusiness.getAziende();

        ListaAziendeTableModel allAziendeTableModel = new ListaAziendeTableModel(distributoriLeftTable);

        leftTable = new JTable(allAziendeTableModel);
        leftTable.setRowHeight(40);
        leftTable.setSelectionModel(selectionModelLeft);

        distributoriRightTable = AziendaBusiness.getInstance().getAziendeSelezionate(pnlEditIngrediente.getIdDistributoriInseriti());

        ListaAziendeTableModel tableAziendeinserite = new ListaAziendeTableModel(distributoriRightTable);
        rightTable = new JTable(tableAziendeinserite);
        rightTable.setRowHeight(40);
        rightTable.setSelectionModel(selectionModelRight);

        addButton = new JButton("Aggiungi distributore");
        addButton.addActionListener(listener);
        addButton.setActionCommand(GestioneProduttoreIngredienteFrameListener.BTN_ADD);

        removeButton = new JButton("Rimuovi distributore");
        removeButton.addActionListener(listener);
        removeButton.setActionCommand(GestioneProduttoreIngredienteFrameListener.BTN_REMOVE);

        confirmButton = new JButton("Conferma e Salva");
        confirmButton.addActionListener(listener);
        confirmButton.setActionCommand(GestioneProduttoreIngredienteFrameListener.BTN_CONFERMA_SALVA);

        layoutComponents();
    }

    private void layoutComponents() {
        panel.setLayout(new BorderLayout());

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

    private JPanel createButtonPanel(JButton button1, JButton button2) {
        JPanel pblButtons = new JPanel();
        pblButtons.setLayout(new GridLayout(2, 1, 5, 5));
        pblButtons.add(button1);
        pblButtons.add(button2);
        return pblButtons;
    }

    private void setViewMode(){


        leftScroll.setVisible(false);
        addButton.setVisible(false);
        removeButton.setVisible(false);
        confirmButton.setVisible(false);
        revalidate();
        repaint();

    }


    public ArrayList<Integer> getIdDistributoriSelezionati(){return pnlEditIngrediente.getIdDistributoriInseriti();}

    public Integer getLeftTableRowSelected(){
        if(leftTable == null || leftTable.getSelectedRow() == -1){  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un'azienda dal catalogo delle aziende della tabella di sinistra!");
            return null;
        } else
            return (Integer) distributoriLeftTable[leftTable.getSelectedRow()][0];
    }

    public Integer getRightTableRowSelected(){
        if(rightTable == null || rightTable.getSelectedRow() == -1){  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un'azienda da quelle inserite nella tabella di destra!");
            return null;
        } else
            return (Integer) distributoriRightTable[rightTable.getSelectedRow()][0];
    }
}