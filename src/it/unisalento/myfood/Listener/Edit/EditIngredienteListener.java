package it.unisalento.myfood.Listener.Edit;

import it.unisalento.myfood.Business.IngredienteBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormAdd;
import it.unisalento.myfood.View.Decorator.Form.FormEdit;
import it.unisalento.myfood.View.Decorator.Form.FormIngredienteDecorator;
import it.unisalento.myfood.View.Decorator.Form.FormView;
import it.unisalento.myfood.View.GestioneDistributoriIngredienteFrame;
import it.unisalento.myfood.View.GestioneProduttoreIngredienteFrame;
import it.unisalento.myfood.View.Panels.Edit.EditIngredientePanel;
import it.unisalento.myfood.View.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class EditIngredienteListener implements ActionListener {
    private final EditIngredientePanel editIngredientePanel;
    private final Frame frame;
    private final TreeMap<String, JTextField> fields;
    private final IngredienteBusiness ingredienteBusiness = IngredienteBusiness.getInstance();

    public EditIngredienteListener(FormIngredienteDecorator form, EditIngredientePanel editIngredientePanel, Frame frame) {
        this.editIngredientePanel = editIngredientePanel;
        this.frame = frame;
        fields = form.getTextFields();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case FormEdit.ANN_BUTT -> frame.showPanel(Frame.PANEL.GESTIONE_INGREDIENTI);
            case FormAdd.ADD_BUTT -> addIngrediente();
            case FormEdit.MOD_BUTT -> editIngrediente();
            case FormIngredienteDecorator.ADD_PROD -> new GestioneProduttoreIngredienteFrame(editIngredientePanel);
            case FormIngredienteDecorator.ADD_DISTR -> new GestioneDistributoriIngredienteFrame(editIngredientePanel);
            case FormView.EXIT_BUTT -> frame.showPanel(Frame.PANEL.GESTIONE_INGREDIENTI);
        }
    }

    private void addIngrediente() {
        if (areFieldsNull()) {
            JOptionPane.showMessageDialog(frame, "ERRORE! Uno o più campi sono vuoti");
            return;
        }

        Integer idProduttore = editIngredientePanel.getIdProduttoreSelezionato();

        ingredienteBusiness.addIngrediente(fields.get("Nome").getText(), editIngredientePanel.getTipologiaIngredienteSelezionata(), idProduttore, editIngredientePanel.getIdDistributoriInseriti());
        JOptionPane.showMessageDialog(frame, ingredienteBusiness.getMessage());
        if(ingredienteBusiness.isAddedSuccessfully())
            frame.showPanel(Frame.PANEL.GESTIONE_INGREDIENTI);
    }

    private void editIngrediente() {
        if (areFieldsNull()) {
            JOptionPane.showMessageDialog(frame, "ERRORE! Uno o più campi sono vuoti");
            return;
        }

        Integer idProduttore = editIngredientePanel.getIdProduttoreSelezionato();

        ingredienteBusiness.editSelectedIngrediente(fields.get("Nome").getText(), editIngredientePanel.getTipologiaIngredienteSelezionata(), idProduttore, editIngredientePanel.getIdDistributoriInseriti());
        JOptionPane.showMessageDialog(frame, ingredienteBusiness.getMessage());
        if(ingredienteBusiness.isEditedSuccessfully())
            frame.showPanel(Frame.PANEL.GESTIONE_INGREDIENTI);
    }

    private boolean areFieldsNull() {
        boolean isCampoNull = fields.get("Nome").getText().isBlank();
        if (editIngredientePanel.getIdProduttoreSelezionato() == null) isCampoNull = true;
        if (editIngredientePanel.getIdDistributoriInseriti().isEmpty()) isCampoNull = true;

        return isCampoNull;
    }
}
