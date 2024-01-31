package it.unisalento.myfood.View;


import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    public enum PANEL{
        LOGIN, SIGNUP, DASHBOARD_CLIENTE, DASHBOARD_CUCINA, DASHBOARD_ADMIN, ARTICOLO, CHANGE_PASSWORD, CARRELLO, ORDINE, GESTIONE_UTENTI
    }

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 850;

    private LoginPanel loginPanel = new LoginPanel(this);
    private SignUpPanel signUpPanel = new SignUpPanel(this);
    private ChangePasswordPanel changePasswordPanel = new ChangePasswordPanel(this);
    private DashboardCliente dashboardCliente;
    private DashboardCucina dashboardCucina;
    private DashboardAmministratore dashboardAmministratore;
    private GestioneUtentiPanel gestioneUtentiPanel;
    private ArticoloPanel articoloPanel;
    private CarrelloPanel carrelloPanel;

    public Frame() {
        super("MyFood");

        // Calcola la posizione x e y per centrare il frame
        int x = ((SCREEN_SIZE.width - getWidth()) / 2) - FRAME_WIDTH / 2;
        int y = ((SCREEN_SIZE.height - getHeight()) / 2) - FRAME_HEIGHT / 2;

        // Imposta la posizione del frame
        setLocation(x, y);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(loginPanel);
        setVisible(true);

    }

    public void showPanel(PANEL panel){
        switch (panel) {
            case LOGIN :
                loginPanel.emptyFields();
                setContentPane(loginPanel);
                break;
            case SIGNUP :
                signUpPanel.emptyFields();
                setContentPane(signUpPanel);
                break;
            case DASHBOARD_CLIENTE:
                dashboardCliente = new DashboardCliente(this);
                setContentPane(dashboardCliente);
                break;
             //TODO: è così perchè la dashboard ha bisogno di un utente in sessione per essere istanziata, da risolvere HINT: scrivere un metodo aggiorna() in dashboard e chiamarlo qui
            case DASHBOARD_CUCINA:
                dashboardCucina = new DashboardCucina(this);
                setContentPane(dashboardCucina);
                break;
            case DASHBOARD_ADMIN:
                dashboardAmministratore = new DashboardAmministratore(this);
                setContentPane(dashboardAmministratore);
                break;
            case ARTICOLO :
                articoloPanel = new ArticoloPanel(this);
                setContentPane(articoloPanel);
                break;
             //TODO: stessa cosa di bashboard
            case CHANGE_PASSWORD:
                changePasswordPanel.emptyFields();
                setContentPane(changePasswordPanel);
                break;
            case CARRELLO:
                carrelloPanel = new CarrelloPanel(this);
                setContentPane(carrelloPanel);
                break;
            case ORDINE:
                JOptionPane.showMessageDialog(null, "Il pannello deve essere ancora creato");
                break;//TODO
            case GESTIONE_UTENTI:
                gestioneUtentiPanel = new GestioneUtentiPanel(this);
                setContentPane(gestioneUtentiPanel);
                break;
        }

        revalidate();
        repaint();
    }


}
