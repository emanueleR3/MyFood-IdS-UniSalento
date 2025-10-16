package it.unisalento.myfood.View.Panels.Edit;

import it.unisalento.myfood.Business.InterazioneUtenteBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormAdd;
import it.unisalento.myfood.View.Decorator.Form.FormEdit;
import it.unisalento.myfood.View.Decorator.Form.FormRispostaDecorator;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.Edit.EditRispostaListener;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class EditRispostaPanel extends JPanel {   //per convenzione lo chiamiamo edit ma si usa sia per aggiungere che per modificare
    private Frame frame;
    private EditRispostaListener editRispostaListener;
    private static HashMap<String, Object> session;
    private TextLabel title;
    private FormEdit formEdit;
    private FormAdd formAdd;
    private InterazioneUtenteBusiness interazioneUtenteBusiness;
    private FormRispostaDecorator formRispostaDecorator;

    public EditRispostaPanel(Frame frame) {
        this.frame = frame;
        session = UtenteBusiness.getSession();
        interazioneUtenteBusiness = InterazioneUtenteBusiness.getInstance();

        setLayout(new BorderLayout());

        if(UtenteBusiness.OPERATIONS.AGGIUNGI.equals(session.get(UtenteBusiness.OPERATION))){
            title = new TextLabel("Aggiungi Risposta");
            formAdd = new FormAdd();
            formRispostaDecorator = new FormRispostaDecorator(formAdd);
        }

        else if(UtenteBusiness.OPERATIONS.MODIFICA.equals(session.get(UtenteBusiness.OPERATION))){
            title = new TextLabel("Modifica Risposta");
            formEdit = new FormEdit();
            formRispostaDecorator = new FormRispostaDecorator(formEdit);
        }

        editRispostaListener = new EditRispostaListener(formRispostaDecorator, frame);
        TitleTextLabelDecorator titleDec = new TitleTextLabelDecorator(title);

        JPanel titlePan = new JPanel(new FlowLayout());

        titlePan.add(titleDec.getLabel());

        add(titlePan, BorderLayout.NORTH);


        loadForm();
        loadPulsanti();


    }

    private void loadForm(){

        TreeMap<String, JTextField> textFields = formRispostaDecorator.getTextFields();
        JPanel fieldsPan = new JPanel(new FlowLayout());
        JPanel fieldsGrid = new JPanel(new GridLayout(textFields.size(), 2));

        Iterator<Map.Entry<String, JTextField>> iterator = textFields.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, JTextField> entry = iterator.next();
            String labelText = entry.getKey();
            String upperTextLabel = labelText.toUpperCase(Locale.ROOT);
            JTextField textField = entry.getValue();

            if(UtenteBusiness.OPERATIONS.MODIFICA.equals(session.get(UtenteBusiness.OPERATION))){   //scrive nelle label il testo dei campi da modificare
                String text = (String) interazioneUtenteBusiness.getCampoSelectedRisposta(InterazioneUtenteBusiness.CAMPO_RISPOSTA.valueOf(upperTextLabel));
                textField.setText(text);
            }

            fieldsGrid.add(new JLabel(labelText));
            JPanel pan = new JPanel(new FlowLayout());
            pan.add(textField);
            fieldsGrid.add(pan);


        }

        fieldsPan.add(fieldsGrid);
        add(fieldsPan, BorderLayout.CENTER);
    }


    private void loadPulsanti(){
        ArrayList<JButton> buttons = formRispostaDecorator.getButtons();
        JPanel buttPan = new JPanel(new FlowLayout());
        JPanel gridPan = new JPanel(new GridLayout(buttons.size(), 1));


        Iterator<JButton> iterator = buttons.iterator();

        while (iterator.hasNext()){
            JPanel pnl = new JPanel(new FlowLayout());
            JButton button = iterator.next();
            button.setPreferredSize(new Dimension(180, 25));
            button.addActionListener(editRispostaListener);
            pnl.add(button);
            gridPan.add(pnl);
        }

        buttPan.add(gridPan);
        add(buttPan, BorderLayout.EAST);

    }







}
