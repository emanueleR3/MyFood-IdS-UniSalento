package it.unisalento.myfood.View;

import it.unisalento.myfood.Business.OrdineBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.UsernameTextLabelDecorator;
import it.unisalento.myfood.View.Decorator.PulsantieraDashboard.*;
import it.unisalento.myfood.View.Listener.DashboardListener;
import it.unisalento.myfood.View.ViewModel.ListaOrdiniTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DashboardCucina extends JPanel implements Dashboard {

    private Frame frame;
    private static DashboardListener dashboardListener;

    private Object[][] ordini;

    private PulsantieraGuest pulsantieraGuest;
    private ArrayList<JButton> buttons;

    private static final UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();

    private static JPanel pnlNorth = new JPanel(new FlowLayout());
    private static JPanel finalFlowPanel = new JPanel(new FlowLayout());
    private static JTable tableOrdini;

    private static JPanel pnlUserLogged = new JPanel();

    public DashboardCucina(Frame frame) {
        this.frame = frame;
        dashboardListener = new DashboardListener(this, frame);

        pnlUserLogged.setLayout(new BoxLayout(pnlUserLogged, BoxLayout.Y_AXIS));

        setLayout(new BorderLayout());
        setLoggedUser();
        setPulsantiera();

        finalFlowPanel.removeAll();

        finalFlowPanel.add(createUITableOrdini());
        add(finalFlowPanel, BorderLayout.CENTER);
    }

    public void setLoggedUser(){
        pnlUserLogged.removeAll();

        TextLabel user = new TextLabel("Personale: " + utenteBusiness.getLoggedInName() + " " + utenteBusiness.getLoggedInCognome());

        UsernameTextLabelDecorator usernameTextLabel = new UsernameTextLabelDecorator(user);

        pnlUserLogged.add(usernameTextLabel.getLabel());
        pnlUserLogged.add(Box.createVerticalStrut(10));

        add(pnlUserLogged, BorderLayout.WEST);
    }

    public void setPulsantiera() {
        if(utenteBusiness.isLoggedCucina()) {
            pulsantieraGuest = new PulsantieraGuest();
            PulsantieraDashboard pulsantieraCucina = new PulsantieraCucinaDecorator(pulsantieraGuest);
            buttons = (ArrayList<JButton>) pulsantieraCucina.getPulsanti();

            JButton btnLogout = buttons.remove(0);
            btnLogout.addActionListener(dashboardListener);
            pnlUserLogged.add(btnLogout);

            JPanel pnlGridButtons = new JPanel(new GridLayout(buttons.size(), 1));

            Iterator<JButton> iterator = buttons.iterator();
            while (iterator.hasNext()){
                JButton btn = iterator.next();
                JPanel row = new JPanel(new FlowLayout());
                row.add(btn);
                pnlGridButtons.add(row);
                btn.addActionListener(dashboardListener);
            }

            JPanel pnlButtons = new JPanel(new FlowLayout());
            pnlButtons.add(pnlGridButtons);

            add(pnlButtons, BorderLayout.EAST);
        }
    }

    public JPanel createUITableOrdini() {
        pnlNorth.removeAll();

        JPanel gridOrdiniPanel;
        gridOrdiniPanel = new JPanel(new GridLayout(1, 1));

        int nOrdiniPagati = OrdineBusiness.getInstance().getNumberOfOrdiniByState(OrdineBusiness.STATO_ORDINE.PAGATO);
        int nOrdiniInLavorazione = OrdineBusiness.getInstance().getNumberOfOrdiniByState(OrdineBusiness.STATO_ORDINE.IN_LAVORAZIONE);

        int nOrdiniSospesi = nOrdiniPagati + nOrdiniInLavorazione;

        pnlNorth.add(new JLabel("Ordini pagati in sospeso: " + nOrdiniPagati + " - Ordini in lavorazione: " + nOrdiniInLavorazione));

        add(pnlNorth, BorderLayout.NORTH);

        if (nOrdiniSospesi == 0) {
            JPanel panel = new JPanel(new FlowLayout());
            panel.add(new JLabel("Non ci sono ordini in sospeso!"));

            gridOrdiniPanel.add(panel);
        } else {
            // Carico la tabella

            ordini = OrdineBusiness.getInstance().loadOrdiniCucina();

            ListaOrdiniTableModel listaOrdiniTableModel = new ListaOrdiniTableModel(ordini);
            tableOrdini = new JTable(listaOrdiniTableModel);

            ListSelectionModel selectionModel = new DefaultListSelectionModel();
            selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe
            tableOrdini.setSelectionModel(selectionModel);

            tableOrdini.setRowHeight(30);
            tableOrdini.getColumnModel().getColumn(0).setPreferredWidth(150);
            tableOrdini.getColumnModel().getColumn(1).setPreferredWidth(150);
            tableOrdini.getColumnModel().getColumn(2).setPreferredWidth(150);
            tableOrdini.setPreferredScrollableViewportSize(tableOrdini.getPreferredSize());

            JScrollPane pnlOrdini = new JScrollPane(tableOrdini);

            gridOrdiniPanel.add(pnlOrdini);
        }

        return gridOrdiniPanel;
    }

    public void updateUITableOrdini() {
        finalFlowPanel.removeAll();
        finalFlowPanel.add(createUITableOrdini());

        revalidate();
        repaint();
    }

    public Integer getOrdineSelezionato(){
        if(tableOrdini == null || tableOrdini.getSelectedRow() == -1){  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un ordine");
            return null;
        } else
            return (Integer) ordini[tableOrdini.getSelectedRow()][0];
    }
}
