package it.unisalento.myfood.Listener.Edit;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Business.ImmagineBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormView;
import it.unisalento.myfood.View.GestioneArticoliMenuFrame;
import it.unisalento.myfood.View.Panels.Edit.EditMenuPanel;
import it.unisalento.myfood.View.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

public class EditMenuListener implements ActionListener {

    public static final String SCEGLI_FOTO_BTN = "SCEGLI_FOTO_BTN";
    public static final String RIMUOVI_FOTO_BTN = "RIMUOVI_FOTO_BTN";
    public static final String ADD_ARTICOLO_TO_MENU = "ADD_ARTICOLO_TO_MENU";
    public static final String MOD_BUTT = "MOD_BUTT";
    public static final String ANN_BUTT = "ANN_BUTT";
    public static final String ADD_BUTT = "ADD_BUTT";

    private final EditMenuPanel editMenuPanel;
    private final Frame frame;

    public EditMenuListener(EditMenuPanel editMenuPanel, Frame frame) {
        this.editMenuPanel = editMenuPanel;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComboBox) {
            if (!editMenuPanel.getComposizioneScelta().equals(editMenuPanel.getComposizioneAttiva())) {
                if (!editMenuPanel.getIdArticoliInseriti().isEmpty()) {
                    int choice = JOptionPane.showConfirmDialog(frame, "Sei sicuro di modificare la composizione? Proseguendo perderai gli articoli che hai aggiunto al menu", "Cambio composizione", JOptionPane.YES_NO_CANCEL_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                        editMenuPanel.getIdArticoliInseriti().clear();
                        editMenuPanel.setComposizioneAttiva(editMenuPanel.getComposizioneScelta());

                        JOptionPane.showMessageDialog(frame, "Il menu è stato svuotato!");
                    } else {
                        editMenuPanel.getCBoxComposizioni().setSelectedItem(editMenuPanel.getComposizioneAttiva());
                    }
                }

                editMenuPanel.setComposizioneAttiva(editMenuPanel.getComposizioneScelta());
            }
        } else if (e.getSource() instanceof JButton) {
            switch (e.getActionCommand()) {
                case ANN_BUTT -> frame.showPanel(Frame.PANEL.GESTIONE_ARTICOLI);
                case ADD_ARTICOLO_TO_MENU -> {
                    new GestioneArticoliMenuFrame(editMenuPanel.getTipologieComposizioneScelta(), editMenuPanel);
                    editMenuPanel.getCBoxComposizioni().setEnabled(false);
                }
                case SCEGLI_FOTO_BTN -> {
                    scegliFoto();
                    editMenuPanel.loadSelectedImages();
                }
                case RIMUOVI_FOTO_BTN -> {
                    rimuoviFoto();
                    editMenuPanel.loadSelectedImages();
                }
                case MOD_BUTT -> modificaMenu();
                case ADD_BUTT -> aggiungiMenu();
                case FormView.EXIT_BUTT -> frame.showPanel(Frame.PANEL.GESTIONE_ARTICOLI);
            }
        }
    }

    private void scegliFoto() {
        File selectedFile = ImmagineBusiness.getInstance().chooseImage();

        if (selectedFile != null) {
            editMenuPanel.getFotoScelte().add(selectedFile);
            JOptionPane.showMessageDialog(frame, "Immagine inserita con successo!");
        }
    }

    private void rimuoviFoto() {
        String nomeFile = editMenuPanel.getTableRowSelected();

        if (nomeFile == null) return;

        ArrayList<File> immagini = editMenuPanel.getFotoScelte();
        for (int i = 0; i < immagini.size(); i++) {
            if (nomeFile.equals(immagini.get(i).getName())) {
                immagini.remove(i);
                return;
            }
        }
    }

    private void aggiungiMenu() {
        ArticoloBusiness articoloBusiness = ArticoloBusiness.getInstance();

        if (areFieldsNull()) {
            JOptionPane.showMessageDialog(frame, "ERRORE! Alcuni campi sono vuoti");
            return;
        }

        TreeMap<String, JTextField> textField = editMenuPanel.getFormMenuDecorator().getTextFields();

        float sconto;
        if (!textField.get("Sconto (0-100 %)").getText().isBlank()) {
            if (!checkFormatoSconto()) return;
            String scontoString = textField.get("Sconto (0-100 %)").getText();
            sconto = Integer.parseInt(scontoString) / 100f;
        } else {
            sconto = 0f;
        }

        String nome = textField.get("Nome").getText();
        String descrizione = textField.get("Descrizione").getText();

        articoloBusiness.addMenu(nome, descrizione, sconto, editMenuPanel.getIdArticoliInseriti(), editMenuPanel.getFotoScelte());
        JOptionPane.showMessageDialog(frame, articoloBusiness.getMessage());
        if (articoloBusiness.isAddedSuccessfully()) {
            frame.showPanel(Frame.PANEL.GESTIONE_ARTICOLI);
        }
    }

    private void modificaMenu() {
        ArticoloBusiness articoloBusiness = ArticoloBusiness.getInstance();

        if (areFieldsNull()) {
            JOptionPane.showMessageDialog(frame, "ERRORE! Alcuni campi sono vuoti");
            return;
        }

        TreeMap<String, JTextField> textField = editMenuPanel.getFormMenuDecorator().getTextFields();

        float sconto;
        if (!textField.get("Sconto (0-100 %)").getText().isBlank()) {
            if (!checkFormatoSconto()) return;
            String scontoString = textField.get("Sconto (0-100 %)").getText();
            sconto = Integer.parseInt(scontoString) / 100f;
        } else {
            sconto = 0f;
        }

        String nome = textField.get("Nome").getText();
        String descrizione = textField.get("Descrizione").getText();

        articoloBusiness.editSelectedMenu(nome, descrizione, sconto, editMenuPanel.getIdArticoliInseriti(), editMenuPanel.getFotoScelte());
        JOptionPane.showMessageDialog(frame, articoloBusiness.getMessage());
        if (articoloBusiness.isEditedSuccessfully()) {
            frame.showPanel(Frame.PANEL.GESTIONE_ARTICOLI);
        }
    }

    private boolean areFieldsNull() {
        TreeMap<String, JTextField> textField = editMenuPanel.getFormMenuDecorator().getTextFields();

        boolean isCampoNull = textField.get("Nome").getText().isBlank();
        if (textField.get("Descrizione").getText().isBlank()) isCampoNull = true;
        if (editMenuPanel.getIdArticoliInseriti().isEmpty()) isCampoNull = true;

        return isCampoNull;
    }

    private boolean checkFormatoSconto() {
        TreeMap<String, JTextField> textField = editMenuPanel.getFormMenuDecorator().getTextFields();
        String scontoString = textField.get("Sconto (0-100 %)").getText();

        if (scontoString.matches("\\d+")) {
            int sconto = Integer.parseInt(scontoString);

            if (sconto < 0 || sconto > 100) {
                JOptionPane.showMessageDialog(frame, "Lo sconto deve essere un numero compreso tra 0 e 100");
                return false;
            }

            return true;
        } else {
            JOptionPane.showMessageDialog(frame, "Lo sconto deve essere un numero intero nel range 0-100");
            return false;
        }
    }
}
