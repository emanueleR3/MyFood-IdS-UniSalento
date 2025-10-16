package it.unisalento.myfood.View.Panels.Edit;

import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormClienteDecorator;
import it.unisalento.myfood.View.Decorator.Form.FormUtente;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.Edit.EditUtenteListener;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class EditUtentePanel extends JPanel {   //In questo caso si usa solo per la modifica, è usato anche dall'amministratore per visualizzare i dati
    private Frame frame;
    private EditUtenteListener editUtenteListener;
    private static HashMap<String, Object> session;
    private UtenteBusiness utenteBusiness;
    private TextLabel title;
    private FormUtente formUtente;
    private FormClienteDecorator formClienteDecorator; //in questo modo non abbiamo i radioButton per selezionare il ruolo

    public EditUtentePanel(Frame frame) {
        this.frame = frame;
        utenteBusiness = UtenteBusiness.getInstance();
        session = UtenteBusiness.getSession();
        editUtenteListener = new EditUtenteListener(this, frame);

        setLayout(new BorderLayout());

        formUtente = new FormUtente();
        formClienteDecorator = new FormClienteDecorator(formUtente);

        title = new TextLabel("Dati utente");
        TitleTextLabelDecorator titleDec = new TitleTextLabelDecorator(title);

        JPanel titlePan = new JPanel(new FlowLayout());

        titlePan.add(titleDec.getLabel());

        add(titlePan, BorderLayout.NORTH);


        loadForm();
        loadPulsanti();


    }

    private void loadForm(){

        TreeMap<String, JTextField> textFields = formClienteDecorator.getTextFields();
        JPanel fieldsPan = new JPanel(new FlowLayout());
        JPanel fieldsGrid = new JPanel(new GridLayout(textFields.size() + 1, 2));

        Iterator<Map.Entry<String, JTextField>> iterator = textFields.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, JTextField> entry = iterator.next();
            String labelText = entry.getKey();
            String upperLabelText = labelText.toUpperCase(Locale.ROOT);
            String adjustedText = upperLabelText.replaceAll("\\s", "").substring(2); //rimuove gli eventuali spazi e i numeri

            JTextField textField = entry.getValue();

            //scrive nelle label il testo dei campi
            String text = (String) utenteBusiness.getCampoSelectedUtente(UtenteBusiness.CAMPO.valueOf(adjustedText));
            textField.setText(text);

            if(utenteBusiness.isLoggedAmministratore()){
                textField.setEditable(false);  //l'amministratore al momento può solo visualizzare
            }

            fieldsGrid.add(new JLabel(labelText));
            JPanel pan = new JPanel(new FlowLayout());
            pan.add(textField);
            fieldsGrid.add(pan);

        }

        JTextField dataRegistrazione = new JTextField();
        dataRegistrazione.setText((String) utenteBusiness.getCampoSelectedUtente(UtenteBusiness.CAMPO.valueOf("DATAREGISTRAZIONE")));

        fieldsGrid.add(new JLabel("8. Data registrazione"));
        fieldsGrid.add(dataRegistrazione);

        fieldsPan.add(fieldsGrid);
        add(fieldsPan, BorderLayout.CENTER);
    }


    private void loadPulsanti(){
        ArrayList<JButton> buttons = formClienteDecorator.getButtons();
        JPanel buttPan = new JPanel(new FlowLayout());
        JPanel gridPan = new JPanel(new GridLayout(buttons.size(), 1));


        Iterator<JButton> iterator = buttons.iterator();

        while (iterator.hasNext()){
            JButton button = iterator.next();
            gridPan.add(button);
            button.addActionListener(editUtenteListener);

        }

        buttPan.add(gridPan);
        add(buttPan, BorderLayout.EAST);

    }







}
