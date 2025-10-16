package it.unisalento.myfood.View.Decorator.Pulsantiera.PulsantieraGestione;

import it.unisalento.myfood.View.Decorator.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.Listener.Gestione.GestioneUtentiListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PulsantieraGestioneUtentiDecorator extends PulsantieraGestioneDecorator {

    private JButton btnVediUtente = new JButton("Vedi Utente");
    private JButton btnAggiungiUtente = new JButton("Aggiungi Utente");
    private JButton btnRimuoviUtente = new JButton("Rimuovi Utente");
    private JButton btnAbilDisabilUtente = new JButton("Abilita/Disabilita Utente");
    private JButton btnResetPassword = new JButton("Reset Password");



    public PulsantieraGestioneUtentiDecorator(Pulsantiera pulsantiera) {
        this.pulsantiera = pulsantiera;

    }

    @Override
    public ArrayList<JButton> getPulsanti() {
        pulsanti.addAll(this.pulsantiera.getPulsanti());


        btnVediUtente.setActionCommand(GestioneUtentiListener.VEDI_UTENTE_BTN);
        btnAggiungiUtente.setActionCommand(GestioneUtentiListener.AGGIUNGI_UTENTE_BTN);
        btnRimuoviUtente.setActionCommand(GestioneUtentiListener.RIMUOVI_UTENTE_BTN);
        btnAbilDisabilUtente.setActionCommand(GestioneUtentiListener.ABILITA_DISABILITA_UTENTE_BTN);
        btnResetPassword.setActionCommand(GestioneUtentiListener.RESET_PASSWORD_UTENTE_BTN);


        pulsanti.add(btnVediUtente);
        pulsanti.add(btnAggiungiUtente);
        pulsanti.add(btnRimuoviUtente);
        pulsanti.add(btnAbilDisabilUtente);
        pulsanti.add(btnResetPassword);

        for (JButton btn : pulsanti) {
            btn.setPreferredSize(new Dimension(180, 25));
        }

        return (ArrayList<JButton>) pulsanti;
    }
}
