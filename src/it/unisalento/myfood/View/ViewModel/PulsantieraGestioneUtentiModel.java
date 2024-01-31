package it.unisalento.myfood.View.ViewModel;

import it.unisalento.myfood.View.Listener.GestioneUtentiListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PulsantieraGestioneUtentiModel {

    private JButton btnTornaDashboard = new JButton("Torna alla Dashboard");
    private JButton btnVediUtente = new JButton("Vedi Utente");
    private JButton btnAggiungiUtente = new JButton("Aggiungi Utente");
    private JButton btnModificaUtente = new JButton("Modifica Utente");
    private JButton btnRimuoviUtente = new JButton("Rimuovi Utente");
    private JButton btnAbilDisabilUtente = new JButton("Abilita/Disabilita Utente");

    private ArrayList<JButton> buttons = new ArrayList<>();

    public ArrayList<JButton> getButtons() {
        btnTornaDashboard.setActionCommand(GestioneUtentiListener.TORNA_DASHBOARD_BTN);
        btnVediUtente.setActionCommand(GestioneUtentiListener.VEDI_UTENTE_BTN);
        btnAggiungiUtente.setActionCommand(GestioneUtentiListener.AGGIUNGI_UTENTE_BTN);
        btnModificaUtente.setActionCommand(GestioneUtentiListener.MODIFICA_UTENTE_BTN);
        btnRimuoviUtente.setActionCommand(GestioneUtentiListener.RIMUOVI_UTENTE_BTN);
        btnAbilDisabilUtente.setActionCommand(GestioneUtentiListener.ABILITA_DISABILITA_UTENTE_BTN);

        buttons.add(btnTornaDashboard);
        buttons.add(btnVediUtente);
        buttons.add(btnAggiungiUtente);
        buttons.add(btnModificaUtente);
        buttons.add(btnRimuoviUtente);
        buttons.add(btnAbilDisabilUtente);

        for (JButton btn : buttons) {
            btn.setPreferredSize(new Dimension(180, 25));
        }

        return buttons;
    }
}
