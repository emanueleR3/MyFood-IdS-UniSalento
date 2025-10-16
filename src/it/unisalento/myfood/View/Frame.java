package it.unisalento.myfood.View;


import it.unisalento.myfood.View.Panels.*;
import it.unisalento.myfood.View.Panels.Edit.*;
import it.unisalento.myfood.View.Panels.Gestione.*;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    public enum PANEL{
        LOGIN, SIGNUP, DASHBOARD_CLIENTE, DASHBOARD_CUCINA, DASHBOARD_ADMIN, ARTICOLO, CHANGE_PASSWORD, CARRELLO,
        UTENTE_VIEW_ORDINI, DETT_ORDINI, GESTIONE_UTENTI, GESTIONE_ARTICOLI, GESTIONE_INGREDIENTI, GESTIONE_AZIENDE,
        GESTIONE_COMMENTI, GESTIONE_TIPOLOGIE_PRODOTTI, GESTIONE_TIPOLOGIE_INGREDIENTI, GESTIONE_ORDINI, EDIT_AZIENDA,
        EDIT_INGREDIENTE, EDIT_MENU, EDIT_PRODOTTO, EDIT_RIPSOSTA, EDIT_TIP_PROD, EDIT_TIP_ING, EDIT_UTENTE

    }

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 850;

    private LoginPanel loginPanel;
    private SignUpPanel signUpPanel;
    private ChangePasswordPanel changePasswordPanel;
    private DashboardCliente dashboardCliente;
    private DashboardCucina dashboardCucina;
    private DashboardAmministratore dashboardAmministratore;
    private GestioneUtentiPanel gestioneUtentiPanel;
    private GestioneArticoliPanel gestioneArticoliPanel;
    private GestioneIngredientiPanel gestioneIngredientiPanel;
    private ArticoloPanel articoloPanel;
    private CarrelloPanel carrelloPanel;
    private OrdiniClientePanel ordiniClientePanel;
    private OrdinePanel ordinePanel;
    private GestioneAziendePanel gestioneAziendePanel;
    private GestioneCommentiPanel gestioneCommentiPanel;
    private GestioneTipologieIngredientiPanel gestioneTipologieIngredientiPanel;
    private GestioneTipologieProdottiPanel gestioneTipologieProdottiPanel;
    private GestioneOrdiniPanel gestioneOrdiniPanel;
    private EditAziendaPanel editAziendaPanel;
    private EditIngredientePanel editIngredientePanel;
    private EditMenuPanel editMenuPanel;
    private EditProdottoPanel editProdottoPanel;
    private EditRispostaPanel editRispostaPanel;
    private EditTipologiaProdottoPanel editTipologiaProdottoPanel;
    private EditTipologiaIngredientePanel editTipologiaIngredientePanel;
    private EditUtentePanel editUtentePanel;

    public Frame() {
        super("MyFood");

        // Icona della finestra
        ImageIcon icon = new ImageIcon("src/immagini/icone/icone_app/icona_myfood.png");
        setIconImage(icon.getImage());

        // Calcola la posizione x e y per centrare il frame
        int x = ((SCREEN_SIZE.width - getWidth()) / 2) - FRAME_WIDTH / 2;
        int y = ((SCREEN_SIZE.height - getHeight()) / 2) - FRAME_HEIGHT / 2;

        // Imposta la posizione del frame
        setLocation(x, y);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginPanel = new LoginPanel(this);
        setContentPane(loginPanel);
        setVisible(true);

    }

    public void showPanel(PANEL panel){
        hideMenuBar();

        switch (panel) {
            case LOGIN :
                loginPanel.emptyFields();
                setContentPane(loginPanel);
                break;
            case SIGNUP :
                signUpPanel = new SignUpPanel(this);
                signUpPanel.emptyFields();
                setContentPane(signUpPanel);
                break;
            case DASHBOARD_CLIENTE:
                dashboardCliente = new DashboardCliente(this);
                setContentPane(dashboardCliente);
                break;
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
            case CHANGE_PASSWORD:
                changePasswordPanel = new ChangePasswordPanel(this);
                changePasswordPanel.emptyFields();
                setContentPane(changePasswordPanel);
                break;
            case CARRELLO:
                carrelloPanel = new CarrelloPanel(this);
                setContentPane(carrelloPanel);
                break;
            case UTENTE_VIEW_ORDINI:
                ordiniClientePanel = new OrdiniClientePanel(this);
                setContentPane(ordiniClientePanel);
                break;
            case DETT_ORDINI:
                ordinePanel = new OrdinePanel(this);
                setContentPane(ordinePanel);
                break;
            case GESTIONE_UTENTI:
                gestioneUtentiPanel = new GestioneUtentiPanel(this);
                setContentPane(gestioneUtentiPanel);
                break;
            case GESTIONE_ARTICOLI:
                gestioneArticoliPanel = new GestioneArticoliPanel(this);
                setContentPane(gestioneArticoliPanel);
                break;
            case GESTIONE_INGREDIENTI:
                gestioneIngredientiPanel = new GestioneIngredientiPanel(this);
                setContentPane(gestioneIngredientiPanel);
                break;
            case GESTIONE_AZIENDE:
                gestioneAziendePanel = new GestioneAziendePanel(this);
                setContentPane(gestioneAziendePanel);
                break;
            case GESTIONE_COMMENTI:
                gestioneCommentiPanel = new GestioneCommentiPanel(this);
                setContentPane(gestioneCommentiPanel);
                break;
            case GESTIONE_TIPOLOGIE_PRODOTTI:
                gestioneTipologieProdottiPanel = new GestioneTipologieProdottiPanel(this);
                setContentPane(gestioneTipologieProdottiPanel);
                break;
            case GESTIONE_TIPOLOGIE_INGREDIENTI:
                gestioneTipologieIngredientiPanel = new GestioneTipologieIngredientiPanel(this);
                setContentPane(gestioneTipologieIngredientiPanel);
                break;
            case GESTIONE_ORDINI:
                gestioneOrdiniPanel = new GestioneOrdiniPanel(this);
                setContentPane(gestioneOrdiniPanel);
                break;
            case EDIT_AZIENDA:
                editAziendaPanel = new EditAziendaPanel(this);
                setContentPane(editAziendaPanel);
                break;
            case EDIT_INGREDIENTE:
                editIngredientePanel = new EditIngredientePanel(this);
                setContentPane(editIngredientePanel);
                break;
            case EDIT_MENU:
                editMenuPanel = new EditMenuPanel(this);
                setContentPane(editMenuPanel);
                break;
            case EDIT_PRODOTTO:
                editProdottoPanel = new EditProdottoPanel(this);
                setContentPane(editProdottoPanel);
                break;
            case EDIT_RIPSOSTA:
                editRispostaPanel = new EditRispostaPanel(this);
                setContentPane(editRispostaPanel);
                break;
            case EDIT_TIP_PROD:
                editTipologiaProdottoPanel = new EditTipologiaProdottoPanel(this);
                setContentPane(editTipologiaProdottoPanel);
                break;
            case EDIT_TIP_ING:
                editTipologiaIngredientePanel = new EditTipologiaIngredientePanel(this);
                setContentPane(editTipologiaIngredientePanel);
                break;
            case EDIT_UTENTE:
                editUtentePanel = new EditUtentePanel(this);
                setContentPane(editUtentePanel);
                break;
        }

        revalidate();
        repaint();
    }

    private void hideMenuBar(){
        JMenuBar jMenuBar = this.getJMenuBar();
        if(jMenuBar != null)
            jMenuBar.setVisible(false);
    }


}
