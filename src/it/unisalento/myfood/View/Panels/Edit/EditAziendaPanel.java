package it.unisalento.myfood.View.Panels.Edit;

import it.unisalento.myfood.Business.AziendaBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormAdd;
import it.unisalento.myfood.View.Decorator.Form.FormAziendaDecorator;
import it.unisalento.myfood.View.Decorator.Form.FormEdit;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.Edit.EditAziendaListener;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class EditAziendaPanel extends JPanel {
    private final EditAziendaListener editAziendaListener;
    private final AziendaBusiness aziendaBusiness = AziendaBusiness.getInstance();
    private static HashMap<String, Object> session;
    private TextLabel title;
    private FormEdit formEdit;
    private FormAdd formAdd;
    private FormAziendaDecorator formAziendaDecorator;

    public EditAziendaPanel(Frame frame) {
        session = UtenteBusiness.getSession();

        setLayout(new BorderLayout());

        if(UtenteBusiness.OPERATIONS.AGGIUNGI.equals(session.get(UtenteBusiness.OPERATION))){
            title = new TextLabel("Aggiungi Azienda");
            formAdd = new FormAdd();
            formAziendaDecorator = new FormAziendaDecorator(formAdd);
        } else if(UtenteBusiness.OPERATIONS.MODIFICA.equals(session.get(UtenteBusiness.OPERATION))){
            title = new TextLabel("Modifica Azienda");
            formEdit = new FormEdit();
            formAziendaDecorator = new FormAziendaDecorator(formEdit);
        }

        editAziendaListener = new EditAziendaListener(formAziendaDecorator, frame);

        TitleTextLabelDecorator titleDec = new TitleTextLabelDecorator(title);

        JPanel titlePan = new JPanel(new FlowLayout());

        titlePan.add(titleDec.getLabel());

        add(titlePan, BorderLayout.NORTH);


        loadForm();
        loadPulsanti();


    }

    private void loadForm(){

        TreeMap<String, JTextField> textFields = formAziendaDecorator.getTextFields();
        JPanel fieldsPan = new JPanel(new FlowLayout());
        JPanel fieldsGrid = new JPanel(new GridLayout(textFields.size(), 2));

        Iterator<Map.Entry<String, JTextField>> iterator = textFields.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, JTextField> entry = iterator.next();
            String labelText = entry.getKey();
            JTextField textField = entry.getValue();

            if(UtenteBusiness.OPERATIONS.MODIFICA.equals(session.get(UtenteBusiness.OPERATION))){   //scrive nelle label il testo dei campi da modificare
                String text = "";
                if (labelText.equalsIgnoreCase("Nome")) {
                    text = (String) aziendaBusiness.getCampoSelectedAzienda(AziendaBusiness.CAMPO.NOME);
                } else if (labelText.equalsIgnoreCase("Partita IVA")) {
                    text = (String) aziendaBusiness.getCampoSelectedAzienda(AziendaBusiness.CAMPO.PARTITA_IVA);
                }
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
        ArrayList<JButton> buttons = formAziendaDecorator.getButtons();
        JPanel buttPan = new JPanel(new FlowLayout());
        JPanel gridPan = new JPanel(new GridLayout(buttons.size(), 1));


        Iterator<JButton> iterator = buttons.iterator();

        while (iterator.hasNext()){
            JPanel pnl = new JPanel(new FlowLayout());
            JButton button = iterator.next();
            button.addActionListener(editAziendaListener);
            button.setPreferredSize(new Dimension(180, 25));
            pnl.add(button);
            gridPan.add(pnl);
        }

        buttPan.add(gridPan);
        add(buttPan, BorderLayout.EAST);

    }







}
