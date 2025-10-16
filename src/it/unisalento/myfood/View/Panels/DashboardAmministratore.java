package it.unisalento.myfood.View.Panels;

import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.UsernameTextLabelDecorator;
import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraDashboard.PulsantieraAmministratoreDecorator;
import it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraDashboard.PulsantieraGuest;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.DashboardListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DashboardAmministratore extends JPanel implements IDashboard {

    private Frame frame;
    private DashboardListener dashboardListener;

    private static UtenteBusiness utenteBusiness = UtenteBusiness.getInstance();

    private static PulsantieraGuest pulsantieraGuest;
    private static ArrayList<JButton> buttons;

    private static JPanel pnlUserLogged = new JPanel();
    private static JPanel pnlPulsantiera = new JPanel(new FlowLayout());
    private static JPanel pnlButtonsFlow = new JPanel();

    public DashboardAmministratore(Frame frame) {
        this.frame = frame;
        dashboardListener = new DashboardListener(this, frame);

        pnlButtonsFlow.setLayout(new BoxLayout(pnlButtonsFlow, BoxLayout.Y_AXIS));
        pnlUserLogged.setLayout(new BoxLayout(pnlUserLogged, BoxLayout.Y_AXIS));

        setLayout(new BorderLayout());

        JPanel pnlNorth = new JPanel(new FlowLayout());
        pnlNorth.add(new JLabel("Seleziona cosa vuoi gestire!"));
        add(pnlNorth, BorderLayout.NORTH);

        setLoggedUser();
        setPulsantiera();
    }

    public void setLoggedUser() {
        pnlUserLogged.removeAll();

        TextLabel user = new TextLabel("Amministratore: " + utenteBusiness.getLoggedInName() + " " + utenteBusiness.getLoggedInCognome());

        UsernameTextLabelDecorator usernameTextLabel = new UsernameTextLabelDecorator(user);
        pnlUserLogged.add(usernameTextLabel.getLabel());
        pnlUserLogged.add(Box.createVerticalStrut(10));

        add(pnlUserLogged, BorderLayout.WEST);
    }

    public void setPulsantiera() {
        pnlPulsantiera.removeAll();
        pnlButtonsFlow.removeAll();

        pulsantieraGuest = new PulsantieraGuest();
        Pulsantiera pulsantieraAmministratore = new PulsantieraAmministratoreDecorator(pulsantieraGuest);
        buttons = (ArrayList<JButton>) pulsantieraAmministratore.getPulsanti();

        JPanel pnlGridButtons = new JPanel(new GridLayout(4, 2));

        JButton logoutButton = buttons.remove(0);
        logoutButton.addActionListener(dashboardListener);
        pnlUserLogged.add(logoutButton);
        Iterator<JButton> iterator = buttons.iterator();

        while(iterator.hasNext()){
            JButton btn = iterator.next();
            JPanel row = new JPanel(new FlowLayout());
            row.add(btn);
            pnlGridButtons.add(row);
            btn.addActionListener(dashboardListener);
        }

        pnlPulsantiera.add(pnlGridButtons);
        pnlButtonsFlow.add(pnlPulsantiera);

        add(pnlButtonsFlow, BorderLayout.CENTER);
    }


    @Override
    public Integer getRowSelected() {
        System.out.println("Non è presente una tabella in questa dashboard");
        return null;
    }
}