package it.unisalento.myfood.Listener.Edit;

import it.unisalento.myfood.Business.TipologiaProdottoBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormAdd;
import it.unisalento.myfood.View.Decorator.Form.FormEdit;
import it.unisalento.myfood.View.Decorator.Form.FormTipologiaDecorator;
import it.unisalento.myfood.View.Panels.Edit.EditTipologiaProdottoPanel;
import it.unisalento.myfood.View.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class EditTipologiaProdottoListener implements ActionListener {
    private EditTipologiaProdottoPanel editTipologiaProdottoPanel;
    private Frame frame;
    private TipologiaProdottoBusiness tipologiaProdottoBusiness;
    private TreeMap<String, JTextField> fields;

    public EditTipologiaProdottoListener(FormTipologiaDecorator form, EditTipologiaProdottoPanel editTipologiaProdottoPanel, Frame frame) {
        this.editTipologiaProdottoPanel = editTipologiaProdottoPanel;
        this.frame = frame;
        tipologiaProdottoBusiness = TipologiaProdottoBusiness.getInstance();
        fields = form.getTextFields();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case FormAdd.ANN_BUTT -> frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_PRODOTTI);
            case FormAdd.ADD_BUTT -> addTipologia();
            case FormEdit.MOD_BUTT -> editTipologia();
        }
    }

    private void addTipologia() {
        if (isTextFieldNull()) {
            JOptionPane.showMessageDialog(frame, "ERRORE! Il campo di testo è vuoto");
            return;
        }

        tipologiaProdottoBusiness.addTipologia(fields.get("Nome").getText());

        JOptionPane.showMessageDialog(frame, tipologiaProdottoBusiness.getMessage());
        if(tipologiaProdottoBusiness.isAddedSuccessfully())
            frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_PRODOTTI);
    }

    private void editTipologia() {
        if (isTextFieldNull()) {
            JOptionPane.showMessageDialog(frame, "ERRORE! Il campo di testo è vuoto");
            return;
        }

        tipologiaProdottoBusiness.editSelectedTipologia(fields.get("Nome").getText());

        JOptionPane.showMessageDialog(frame, tipologiaProdottoBusiness.getMessage());

        System.out.println(tipologiaProdottoBusiness.isEditedSuccessfully());
        if(tipologiaProdottoBusiness.isEditedSuccessfully()){
            frame.showPanel(Frame.PANEL.GESTIONE_TIPOLOGIE_PRODOTTI);
        }
    }

    private boolean isTextFieldNull() {
        return fields.get("Nome").getText().isBlank();
    }
}
