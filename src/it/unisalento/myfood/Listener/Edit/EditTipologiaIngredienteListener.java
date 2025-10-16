package it.unisalento.myfood.Listener.Edit;

import it.unisalento.myfood.Business.TipologiaIngredienteBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormAdd;
import it.unisalento.myfood.View.Decorator.Form.FormEdit;
import it.unisalento.myfood.View.Decorator.Form.FormTipologiaDecorator;
import it.unisalento.myfood.View.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class EditTipologiaIngredienteListener implements ActionListener {
    private Frame frame;
    private TipologiaIngredienteBusiness tipologiaIngredienteBusiness = TipologiaIngredienteBusiness.getInstance();
    private TreeMap<String, JTextField> fields;

    public EditTipologiaIngredienteListener(FormTipologiaDecorator form, Frame frame) {
        this.frame = frame;
        fields = form.getTextFields();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case (FormAdd.ANN_BUTT) -> frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_INGREDIENTI);
            case (FormAdd.ADD_BUTT) -> addTipologia();
            case (FormEdit.MOD_BUTT) -> editTipologia();
        }
    }

    private void addTipologia() {
        if (isTextFieldNull()) {
            JOptionPane.showMessageDialog(frame, "ERRORE! Il campo di testo è vuoto");
            return;
        }

        tipologiaIngredienteBusiness.addTipologia(fields.get("Nome").getText());
        JOptionPane.showMessageDialog(frame, tipologiaIngredienteBusiness.getMessage());
        if(tipologiaIngredienteBusiness.isAddedSuccessfully())
            frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_INGREDIENTI);
    }

    private void editTipologia() {
        if (isTextFieldNull()) {
            JOptionPane.showMessageDialog(frame, "ERRORE! Il campo di testo è vuoto");
            return;
        }

        tipologiaIngredienteBusiness.editSelectedTipologia(fields.get("Nome").getText());
        JOptionPane.showMessageDialog(frame, tipologiaIngredienteBusiness.getMessage());
        if(tipologiaIngredienteBusiness.isEditSuccessfully()){
            frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_INGREDIENTI);
        }
    }

    private boolean isTextFieldNull() {
        return fields.get("Nome").getText().isBlank();
    }
}
