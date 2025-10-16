package it.unisalento.myfood.View.Panels.Edit;

import it.unisalento.myfood.Business.IngredienteBusiness;
import it.unisalento.myfood.Business.TipologiaIngredienteBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Form.FormAdd;
import it.unisalento.myfood.View.Decorator.Form.FormEdit;
import it.unisalento.myfood.View.Decorator.Form.FormIngredienteDecorator;
import it.unisalento.myfood.View.Decorator.Form.FormView;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.Edit.EditIngredienteListener;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class EditIngredientePanel extends JPanel {   //per convenzione lo chiamiamo edit ma si usa sia per aggiungere che per modificare
    private Frame frame;
    private EditIngredienteListener editIngredienteListener;
    private IngredienteBusiness ingredienteBusiness = IngredienteBusiness.getInstance();
    private static HashMap<String, Object> session;
    private UtenteBusiness utenteBusiness;
    private TextLabel title;
    private FormEdit formEdit;
    private FormAdd formAdd;
    private FormView formView;
    private FormIngredienteDecorator formIngredienteDecorator;
    private JButton btnSelezionaProduttore;
    private JButton btnAddDistributore;
    private  JComboBox<String> cBoxTipologie;
    private Integer idProduttoreSelezionato;
    private ArrayList<Integer> idDistributoriInseriti;
    private TipologiaIngredienteBusiness tipologiaIngredienteBusiness = TipologiaIngredienteBusiness.getInstance();
    private TreeMap<String, JTextField> textFields;
    private String operationStr;
    private ArrayList<JButton> buttons;

    public EditIngredientePanel(Frame frame) {
        this.frame = frame;
        utenteBusiness = UtenteBusiness.getInstance();
        session = UtenteBusiness.getSession();

        setLayout(new BorderLayout());

        operationStr = session.get(UtenteBusiness.OPERATION).toString().charAt(0) + session.get(UtenteBusiness.OPERATION).toString().toLowerCase(Locale.ROOT).substring(1);

        title = new TextLabel(operationStr + " ingrediente");

        if(UtenteBusiness.getSession().get(UtenteBusiness.OPERATION).equals(UtenteBusiness.OPERATIONS.MODIFICA)) {
            formEdit = new FormEdit();
            formIngredienteDecorator = new FormIngredienteDecorator(formEdit);
        }
        if(UtenteBusiness.getSession().get(UtenteBusiness.OPERATION).equals(UtenteBusiness.OPERATIONS.AGGIUNGI)) {
            formAdd = new FormAdd();
            formIngredienteDecorator = new FormIngredienteDecorator(formAdd);
        }
        if(UtenteBusiness.getSession().get(UtenteBusiness.OPERATION).equals(UtenteBusiness.OPERATIONS.VISUALIZZA)) {
            formView = new FormView();
            formIngredienteDecorator = new FormIngredienteDecorator(formView);
        }

        editIngredienteListener = new EditIngredienteListener(formIngredienteDecorator, this, frame);

       idDistributoriInseriti = new ArrayList<>();

        TitleTextLabelDecorator titleDec = new TitleTextLabelDecorator(title);

        JPanel titlePan = new JPanel(new FlowLayout());

        titlePan.add(titleDec.getLabel());

        add(titlePan, BorderLayout.NORTH);

        loadPulsanti();
        loadForm();

        if(UtenteBusiness.getSession().get(UtenteBusiness.OPERATION).equals(UtenteBusiness.OPERATIONS.VISUALIZZA))
            setViewMode();

    }

    private void loadForm(){

        textFields = formIngredienteDecorator.getTextFields();
        JPanel fieldsPan = new JPanel(new FlowLayout());
        JPanel fieldsGrid = new JPanel(new GridLayout(textFields.size() + 3, 2));

        String selectedTipologia = null;
        Iterator<Map.Entry<String, JTextField>> iterator = textFields.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, JTextField> entry = iterator.next();
            String labelText = entry.getKey();
            String upperLabelText = labelText.toUpperCase(Locale.ROOT);
            JTextField textField = entry.getValue();

            if(UtenteBusiness.OPERATIONS.MODIFICA.equals(session.get(UtenteBusiness.OPERATION)) || UtenteBusiness.OPERATIONS.VISUALIZZA.equals(session.get(UtenteBusiness.OPERATION))){   //scrive nelle label il testo dei campi da modificare
                String text = (String) ingredienteBusiness.getCampoSelectedIngrediente(IngredienteBusiness.CAMPO.valueOf(upperLabelText));
                textField.setText(text);
                selectedTipologia = (String) ingredienteBusiness.getCampoSelectedIngrediente(IngredienteBusiness.CAMPO.NOME_TIPOLOGIA);
                idProduttoreSelezionato = (Integer) ingredienteBusiness.getCampoSelectedIngrediente(IngredienteBusiness.CAMPO.ID_PRODUTTORE);
                idDistributoriInseriti = (ArrayList<Integer>) ingredienteBusiness.getCampoSelectedIngrediente(IngredienteBusiness.CAMPO.ID_DISTRIBUTORI);
            }

            fieldsGrid.add(new JLabel(labelText));
            JPanel pan = new JPanel(new FlowLayout());
            pan.add(textField);
            fieldsGrid.add(pan);

        }


        fieldsGrid.add(new JLabel("Tipologia:"));
        loadTipologie(selectedTipologia);
        fieldsGrid.add(cBoxTipologie);

        fieldsGrid.add(new JLabel("Seleziona produttore"));
        fieldsGrid.add(btnSelezionaProduttore);

        fieldsGrid.add(new JLabel("Aggiungi distributore:"));
        fieldsGrid.add(btnAddDistributore);




        fieldsPan.add(fieldsGrid);
        add(fieldsPan, BorderLayout.CENTER);
    }

    private void loadTipologie(String selectedTipologia) {
        String[] tipologie = tipologiaIngredienteBusiness.loadNomiTipologie();
        List<String> tipologieList = new ArrayList<>(Arrays.asList(tipologie));
        tipologie = tipologieList.toArray(new String[0]);
        cBoxTipologie = new JComboBox<>(tipologie);

        if(selectedTipologia != null)
            cBoxTipologie.setSelectedItem(selectedTipologia);
    }


    private void loadPulsanti(){
        buttons = formIngredienteDecorator.getButtons();
        JPanel buttPan = new JPanel(new FlowLayout());
        JPanel gridPan = new JPanel(new GridLayout(buttons.size() - 2, 1));

        btnAddDistributore = buttons.remove(buttons.size() - 1);
        btnAddDistributore.addActionListener(editIngredienteListener);

        btnSelezionaProduttore = buttons.remove(buttons.size() - 1);
        btnSelezionaProduttore.addActionListener(editIngredienteListener);

        Iterator<JButton> iterator = buttons.iterator();

        while (iterator.hasNext()){
            JPanel pnl = new JPanel(new FlowLayout());
            JButton button = iterator.next();
            button.setPreferredSize(new Dimension(180, 25));
            button.addActionListener(editIngredienteListener);
            pnl.add(button);
            gridPan.add(pnl);
        }


        buttPan.add(gridPan);
        add(buttPan, BorderLayout.EAST);
    }

    private void setViewMode(){
        Iterator<Map.Entry<String, JTextField>> iterator = textFields.entrySet().iterator();

        while(iterator.hasNext())
            iterator.next().getValue().setEnabled(false);

        btnAddDistributore.setText("Visualizza distributori");
        cBoxTipologie.setEnabled(false);
        btnSelezionaProduttore.setText("Visualizza produttore");
    }

    public ArrayList<Integer> getIdDistributoriInseriti() {
        return idDistributoriInseriti;
    }

    public Integer getIdProduttoreSelezionato() {
        return idProduttoreSelezionato;
    }

    public void setIdProduttoreSelezionato(Integer idProduttoreSelezionato) {
        this.idProduttoreSelezionato = idProduttoreSelezionato;
    }

    public String getTipologiaIngredienteSelezionata(){return (String) cBoxTipologie.getSelectedItem();}

    public void setTipologiaIngredienteSelezionata(String tipologia){
        cBoxTipologie.setSelectedItem(tipologia);
    }


}
