package it.unisalento.myfood.Listener.Edit;

import it.unisalento.myfood.Business.AziendaBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormAdd;
import it.unisalento.myfood.View.Decorator.Form.FormAziendaDecorator;
import it.unisalento.myfood.View.Decorator.Form.FormEdit;
import it.unisalento.myfood.View.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.TreeMap;

public class EditAziendaListener implements ActionListener {
    private final Frame frame;
    private final TreeMap<String, JTextField> fields;
    private final AziendaBusiness aziendaBusiness;

    public EditAziendaListener(FormAziendaDecorator form, Frame frame) {
        this.frame = frame;
        fields = form.getTextFields();
        aziendaBusiness = AziendaBusiness.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case FormAdd.ANN_BUTT -> frame.showPanel(Frame.PANEL.GESTIONE_AZIENDE);
            case FormAdd.ADD_BUTT -> addAzienda();
            case FormEdit.MOD_BUTT -> editAzienda();
        }
    }

    private void addAzienda() {
        if (areTextFieldsNull()) {
            JOptionPane.showMessageDialog(frame, "ERRORE! Alcuni campi sono vuoti");
            return;
        }

        aziendaBusiness.addAzienda(fields.get("Nome").getText(), fields.get("Partita IVA").getText());
        JOptionPane.showMessageDialog(frame, aziendaBusiness.getMessage());

        if(aziendaBusiness.isAddedSuccessfully())
            frame.showPanel(Frame.PANEL.GESTIONE_AZIENDE);
    }

    private void editAzienda() {
        if (areTextFieldsNull()) {
            JOptionPane.showMessageDialog(frame, "ERRORE! Alcuni campi sono vuoti");
            return;
        }

        aziendaBusiness.editSelectedAzienda(fields.get("Nome").getText(), fields.get("Partita IVA").getText());
        JOptionPane.showMessageDialog(frame, aziendaBusiness.getMessage());

        if(aziendaBusiness.isEditedSuccessfully()) {
            frame.showPanel(Frame.PANEL.GESTIONE_AZIENDE);
        }
    }

    private boolean areTextFieldsNull() {
        boolean isCampoNull = fields.get("Nome").getText().isBlank();
        if (fields.get("Partita IVA").getText().isBlank()) isCampoNull = true;

        return isCampoNull;
    }
}
