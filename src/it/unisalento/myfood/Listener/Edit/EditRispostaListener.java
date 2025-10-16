package it.unisalento.myfood.Listener.Edit;

import it.unisalento.myfood.Business.InterazioneUtenteBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormAdd;
import it.unisalento.myfood.View.Decorator.Form.FormRispostaDecorator;
import it.unisalento.myfood.View.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class EditRispostaListener implements ActionListener {
    private Frame frame;
    private final InterazioneUtenteBusiness interazioneUtenteBusiness = InterazioneUtenteBusiness.getInstance();
    private TreeMap<String, JTextField> fields;

    public EditRispostaListener(FormRispostaDecorator form, Frame frame) {
        this.frame = frame;
        this.fields = form.getTextFields();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case (FormAdd.ADD_BUTT) -> addRisposta();
            case (FormAdd.ANN_BUTT) -> frame.showPanel(Frame.PANEL.GESTIONE_COMMENTI);
        }
    }

    private void addRisposta() {
        if (isTextFieldNull()) {
            JOptionPane.showMessageDialog(frame, "ERRORE! Il campo di testo è vuoto.");
            return;
        }

        String testo = fields.get("Testo").getText();

        interazioneUtenteBusiness.addRiposta(testo, (Integer) interazioneUtenteBusiness.getCampoSelectedCommento(InterazioneUtenteBusiness.CAMPO_COMMENTO.ID));
        JOptionPane.showMessageDialog(frame, interazioneUtenteBusiness.getMessage());
        if(interazioneUtenteBusiness.isCreatedSuccessfully()){
            frame.showPanel(Frame.PANEL.GESTIONE_COMMENTI);
        }
    }

    private boolean isTextFieldNull() {
        return fields.get("Testo").getText().isBlank();
    }
}
