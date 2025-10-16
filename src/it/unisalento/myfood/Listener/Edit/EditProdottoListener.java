package it.unisalento.myfood.Listener.Edit;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Business.ImmagineBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormView;
import it.unisalento.myfood.View.GestioneIngredientiProdottoFrame;
import it.unisalento.myfood.View.Panels.Edit.EditProdottoPanel;
import it.unisalento.myfood.View.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

public class EditProdottoListener implements ActionListener {
    public static final String MOD_BUTT = "MOD_BUTT";
    public static final String ADD_BUTT = "ADD_BUTT";
    public static final String ANN_BUTT = "ANN_BUTT";
    public static final String ADD_ING = "ADD_ING";
    public static final String SCEGLI_FOTO_BTN = "SCEGLI_FOTO_BTN";
    public static final String RIMUOVI_FOTO_BTN = "RIMUOVI_FOTO_BTN";

    private final EditProdottoPanel editProdottoPanel;
    private final Frame frame;

    public EditProdottoListener(EditProdottoPanel editProdottoPanel, Frame frame) {
        this.editProdottoPanel = editProdottoPanel;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ANN_BUTT -> frame.showPanel(Frame.PANEL.GESTIONE_ARTICOLI);
            case ADD_BUTT -> {
                boolean done = aggiungiProdotto();
                if (done) {
                    JOptionPane.showMessageDialog(frame, "Prodotto inserito con successo!");
                    frame.showPanel(Frame.PANEL.GESTIONE_ARTICOLI);
                } else {
                    JOptionPane.showMessageDialog(frame, "ERRORE! Prodotto non inserito");
                }
            }
            case ADD_ING -> new GestioneIngredientiProdottoFrame(editProdottoPanel);
            case MOD_BUTT -> {
                boolean done = modificaProdotto();
                if (done) {
                    JOptionPane.showMessageDialog(frame, "Articolo modificato con successo!");
                    frame.showPanel(Frame.PANEL.GESTIONE_ARTICOLI);
                } else {
                    JOptionPane.showMessageDialog(frame, "ERRORE! Articolo non modificato");
                }
            }
            case SCEGLI_FOTO_BTN -> {
                scegliFoto();
                editProdottoPanel.loadSelectedImages();
            }
            case RIMUOVI_FOTO_BTN -> {
                rimuoviFoto();
                editProdottoPanel.loadSelectedImages();
            }
            case FormView.EXIT_BUTT -> frame.showPanel(Frame.PANEL.GESTIONE_ARTICOLI);

        }
    }

    private void scegliFoto() {
        File selectedFile = ImmagineBusiness.getInstance().chooseImage();

        if (selectedFile != null) {
            editProdottoPanel.getFotoScelte().add(selectedFile);
            JOptionPane.showMessageDialog(frame, "Immagine inserita con successo!");
        }
    }

    private void rimuoviFoto() {
        String nomeFile = editProdottoPanel.getTableRowSelected();

        if (nomeFile == null) return;

        ArrayList<File> immagini = editProdottoPanel.getFotoScelte();
        for (int i = 0; i < immagini.size(); i++) {
            if (nomeFile.equals(immagini.get(i).getName())) {
                immagini.remove(i);
                return;
            }
        }
    }

    private boolean modificaProdotto() {
        if (areTextFieldNull()) {
            JOptionPane.showMessageDialog(frame, "ERRORE! Alcuni campi sono vuoti");
            return false;
        }

        if (!checkFormatoPrezzo()) return false;
        if (!checkFormatoDisponibilita()) return false;

        TreeMap<String, JTextField> textField = editProdottoPanel.getFormProdottoDecorator().getTextFields();
        String nome = textField.get("Nome").getText();
        String descrizione = textField.get("Descrizione").getText();
        Integer disponibilita = Integer.valueOf(textField.get("Disponibilita").getText());

        String prezzoString = textField.get("Prezzo").getText();
        if (prezzoString.contains(",")) prezzoString = prezzoString.replace(",", ".");
        Float prezzo = Float.valueOf(prezzoString);

        return ArticoloBusiness.getInstance().editSelectedProdotto(nome, disponibilita, descrizione, prezzo, editProdottoPanel.getIdIngredientiInseriti(), editProdottoPanel.getSelectedTipologia(), editProdottoPanel.getFotoScelte());
    }

    private boolean aggiungiProdotto() {
        if (areTextFieldNull()) {
            JOptionPane.showMessageDialog(frame, "ERRORE! Alcuni campi sono vuoti");
            return false;
        }

        if (!checkFormatoPrezzo()) return false;
        if (!checkFormatoDisponibilita()) return false;

        TreeMap<String, JTextField> textField = editProdottoPanel.getFormProdottoDecorator().getTextFields();
        String nome = textField.get("Nome").getText();
        String descrizione = textField.get("Descrizione").getText();
        Integer disponibilita = Integer.valueOf(textField.get("Disponibilita").getText());

        String prezzoString = textField.get("Prezzo").getText();
        if (prezzoString.contains(",")) prezzoString = prezzoString.replace(",", ".");
        Float prezzo = Float.valueOf(prezzoString);

        return ArticoloBusiness.getInstance().addProdotto(nome, disponibilita, descrizione, prezzo, editProdottoPanel.getIdIngredientiInseriti(), editProdottoPanel.getSelectedTipologia(), editProdottoPanel.getFotoScelte());
    }

    private boolean areTextFieldNull() {
        TreeMap<String, JTextField> textField = editProdottoPanel.getFormProdottoDecorator().getTextFields();

        boolean isCampoNull = textField.get("Disponibilita").getText().isBlank();
        if (textField.get("Prezzo").getText().isBlank()) isCampoNull = true;
        if (textField.get("Nome").getText().isBlank()) isCampoNull = true;
        if (textField.get("Descrizione").getText().isBlank()) isCampoNull = true;

        return isCampoNull;
    }

    private boolean checkFormatoPrezzo() {
        String prezzoString = editProdottoPanel.getFormProdottoDecorator().getTextFields().get("Prezzo").getText().trim();

        if (prezzoString.matches("^\\d+(\\.\\d+)?(,\\d+)?$")) {
            return true;
        } else {
            JOptionPane.showMessageDialog(frame, "Il prezzo deve essere un numero");
            return false;
        }
    }

    private boolean checkFormatoDisponibilita() {
        String disponibilitaString = editProdottoPanel.getFormProdottoDecorator().getTextFields().get("Disponibilita").getText().trim();

        if (disponibilitaString.matches("\\d+")) {
            return true;
        } else {
            JOptionPane.showMessageDialog(frame, "Il numero di pezzi disponibili deve essere un intero");
            return false;
        }
    }
}
