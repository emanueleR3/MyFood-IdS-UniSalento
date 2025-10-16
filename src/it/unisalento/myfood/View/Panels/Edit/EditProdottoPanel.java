package it.unisalento.myfood.View.Panels.Edit;

import it.unisalento.myfood.Business.*;
import it.unisalento.myfood.View.Decorator.Form.*;
import it.unisalento.myfood.View.Decorator.Icon.TableIconDecorator;
import it.unisalento.myfood.View.Decorator.Icon.OriginalIcon;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.Edit.EditProdottoListener;
import it.unisalento.myfood.View.ViewModel.ListaImmaginiTableModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class EditProdottoPanel extends JPanel {   //per convenzione lo chiamiamo edit ma si usa sia per aggiungere che per modificare
    private Frame frame;
    private EditProdottoListener editProdottoListener;
    private static HashMap<String, Object> session;
    private TextLabel title;
    private FormEdit formEdit;
    private FormAdd formAdd;
    private FormView formView;
    private ArticoloBusiness articoloBusiness;
    private FormProdottoDecorator formProdottoDecorator;
    private JButton btnAddIngredienti;
    private JButton btnSelezionaFoto;
    private JComboBox<String> cBoxTipologie;
    private ArrayList<Integer> idIngredientiInseriti;
    private TreeMap<String, JTextField> textFields;
    private JLabel insFoto;

    private ArrayList<File> fotoScelte = new ArrayList<>();
    private JTable selectedImagesTable;
    private JScrollPane scrollPaneImages;
    private JPanel pnlGridFoto;
    private JPanel pnlFinal;

    private ArrayList<JButton> buttons;
    private Integer idProdottoModifica;

    private JButton btnRimuoviFoto;

    private Object[][] immagini;
    private String operationStr;

    public EditProdottoPanel(Frame frame) {
        this.frame = frame;
        session = UtenteBusiness.getSession();
        editProdottoListener = new EditProdottoListener(this, frame);
        articoloBusiness = ArticoloBusiness.getInstance();
        pnlGridFoto = new JPanel(new GridLayout(1, 2, 10, 10));
        pnlFinal = new JPanel(new FlowLayout());

        operationStr = session.get(UtenteBusiness.OPERATION).toString().charAt(0) + session.get(UtenteBusiness.OPERATION).toString().toLowerCase(Locale.ROOT).substring(1);

        btnRimuoviFoto = new JButton("Rimuovi foto");
        btnRimuoviFoto.addActionListener(editProdottoListener);
        btnRimuoviFoto.setActionCommand(EditProdottoListener.RIMUOVI_FOTO_BTN);
        btnRimuoviFoto.setPreferredSize(new Dimension(200, 25));

        setLayout(new BorderLayout());

        idIngredientiInseriti = new ArrayList<>();

        if(UtenteBusiness.OPERATIONS.MODIFICA.equals(session.get(UtenteBusiness.OPERATION)) || UtenteBusiness.OPERATIONS.VISUALIZZA.equals(session.get(UtenteBusiness.OPERATION))){
            idProdottoModifica = (Integer) articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.ID);

            Object[][] ingredientiProdotto = IngredienteBusiness.getInstance().getIngredientiPerViewProdotto();
            for (Object[] prodotto : ingredientiProdotto) idIngredientiInseriti.add((int) prodotto[0]);

            if(ImmagineBusiness.getInstance().getImagesPerViewArticolo() != null)
                fotoScelte = ImmagineBusiness.getInstance().getImagesPerViewArticolo();
            else
                fotoScelte = new ArrayList<>();


        }

        title = new TextLabel(operationStr + " prodotto");

        if(UtenteBusiness.getSession().get(UtenteBusiness.OPERATION).equals(UtenteBusiness.OPERATIONS.MODIFICA)) {
            formEdit = new FormEdit();
            formProdottoDecorator = new FormProdottoDecorator(formEdit);
        }
        if(UtenteBusiness.getSession().get(UtenteBusiness.OPERATION).equals(UtenteBusiness.OPERATIONS.AGGIUNGI)) {
            formAdd = new FormAdd();
            formProdottoDecorator = new FormProdottoDecorator(formAdd);
        }
        if(UtenteBusiness.getSession().get(UtenteBusiness.OPERATION).equals(UtenteBusiness.OPERATIONS.VISUALIZZA)) {
            formView = new FormView();
            formProdottoDecorator = new FormProdottoDecorator(formView);
        }

        TitleTextLabelDecorator titleDec = new TitleTextLabelDecorator(title);

        JPanel titlePan = new JPanel(new FlowLayout());

        titlePan.add(titleDec.getLabel());

        add(titlePan, BorderLayout.NORTH);

        loadPulsanti();
        loadForm();
        loadSelectedImages();

        if(UtenteBusiness.OPERATIONS.VISUALIZZA.equals(session.get(UtenteBusiness.OPERATION)))
            setViewMode();

    }

    private void loadForm(){

        textFields = formProdottoDecorator.getTextFields();

        JPanel fieldsPan = new JPanel(new FlowLayout());
        JPanel fieldsGrid = new JPanel(new GridLayout(0, 2, 0, 10));    // 0 rows = senza un limite

        Iterator<Map.Entry<String, JTextField>> iterator = textFields.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, JTextField> entry = iterator.next();
            String labelText = entry.getKey();
            String upperTextLabel = labelText.toUpperCase(Locale.ROOT);
            JTextField textField = entry.getValue();

            if(UtenteBusiness.OPERATIONS.MODIFICA.equals(session.get(UtenteBusiness.OPERATION)) || UtenteBusiness.OPERATIONS.VISUALIZZA.equals(session.get(UtenteBusiness.OPERATION))){   //scrive nelle label il testo dei campi da modificare
                String text = articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.valueOf(upperTextLabel)).toString();
                textField.setText(text);
            }

            fieldsGrid.add(new JLabel(labelText));
            JPanel pan = new JPanel(new FlowLayout());
            pan.add(textField);
            fieldsGrid.add(pan);
        }

        fieldsGrid.add(new JLabel(operationStr + " ingredienti:"));
        fieldsGrid.add(btnAddIngredienti);

        fieldsGrid.add(new JLabel("Tipologia Prodotto:"));
        loadTipologie();

        if(UtenteBusiness.OPERATIONS.MODIFICA.equals(session.get(UtenteBusiness.OPERATION)))
            cBoxTipologie.setSelectedItem(articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.TIPOLOGIA));

        fieldsGrid.add(cBoxTipologie);

        insFoto = new JLabel("Inserisci foto:");
        fieldsGrid.add(insFoto);
        btnSelezionaFoto = new JButton("Seleziona Foto dal Computer");
        btnSelezionaFoto.setPreferredSize(new Dimension(180, 25));
        btnSelezionaFoto.addActionListener(editProdottoListener);
        btnSelezionaFoto.setActionCommand(EditProdottoListener.SCEGLI_FOTO_BTN);
        fieldsGrid.add(btnSelezionaFoto);

        fieldsPan.add(fieldsGrid);
        add(fieldsPan, BorderLayout.CENTER);
    }

    private void loadTipologie() {
        String[] tipologie = TipologiaProdottoBusiness.getInstance().loadNomiTipologie();
        List<String> tipologieList = new ArrayList<>(Arrays.asList(tipologie));
        tipologieList.remove("Menu");
        tipologie = tipologieList.toArray(new String[0]);
        cBoxTipologie = new JComboBox<>(tipologie);
    }

    private void loadPulsanti(){
        buttons = formProdottoDecorator.getButtons();

        btnAddIngredienti = new JButton();
        btnAddIngredienti = buttons.remove(buttons.size() - 1);
        btnAddIngredienti.setPreferredSize(new Dimension(180, 25));
        btnAddIngredienti.addActionListener(editProdottoListener);

        JPanel buttPan = new JPanel(new FlowLayout());
        JPanel gridPan = new JPanel(new GridLayout(buttons.size(), 1));

        Iterator<JButton> iterator = buttons.iterator();

        while (iterator.hasNext()){
            JPanel pnl = new JPanel(new FlowLayout());
            JButton button = iterator.next();
            button.setPreferredSize(new Dimension(180, 25));
            button.addActionListener(editProdottoListener);
            pnl.add(button);
            gridPan.add(pnl);
        }

        buttPan.add(gridPan);
        add(buttPan, BorderLayout.EAST);

    }
    
    public void loadSelectedImages() {
        pnlFinal.removeAll();
        pnlGridFoto.removeAll();

        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //per impedire che si possano selezionare più righe

        immagini = loadMatrix();
        
        ListaImmaginiTableModel listaImmagini = new ListaImmaginiTableModel(immagini);
        selectedImagesTable = new JTable(listaImmagini);
        selectedImagesTable.setRowHeight(80);
        selectedImagesTable.setSelectionModel(selectionModel);

        selectedImagesTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        selectedImagesTable.getColumnModel().getColumn(1).setPreferredWidth(300);

        scrollPaneImages = new JScrollPane(selectedImagesTable);

        pnlGridFoto.add(scrollPaneImages);

        JPanel pnlButton = new JPanel(new FlowLayout());
        pnlButton.add(btnRimuoviFoto);
        pnlGridFoto.add(pnlButton);

        pnlFinal.add(pnlGridFoto);

        add(pnlFinal, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private Object[][] loadMatrix() {
        if (fotoScelte == null || fotoScelte.isEmpty())
            return new Object[0][0];

        Object[][] immagini = new Object[fotoScelte.size()][2];

        int i = 0;
        for (File f : fotoScelte) {
            // Foto
            OriginalIcon icon = new OriginalIcon(f.getAbsolutePath());
            TableIconDecorator tableIconDecorator = new TableIconDecorator(icon);
            immagini[i][0] = tableIconDecorator.getImageIcon();

            // Nome
            immagini[i][1] = f.getName();

            i++;
        }

        return immagini;
    }

    private void setViewMode(){
        Iterator<Map.Entry<String, JTextField>> iterator = textFields.entrySet().iterator();

        while (iterator.hasNext())
            iterator.next().getValue().setEnabled(false);

        cBoxTipologie.setEnabled(false);
        btnRimuoviFoto.setVisible(false);
        btnSelezionaFoto.setVisible(false);
        insFoto.setVisible(false);
        btnAddIngredienti.setText("Visualizza ingredienti");

        
    }


    public String getTableRowSelected(){
        if(selectedImagesTable == null || selectedImagesTable.getSelectedRow() == -1){  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un'immagine");
            return null;
        } else
            return (String) immagini[selectedImagesTable.getSelectedRow()][1];
    }
    
    public String getSelectedTipologia() {
        return (String) cBoxTipologie.getSelectedItem();
    }

    public ArrayList<Integer> getIdIngredientiInseriti() {
        return idIngredientiInseriti;
    }

    public void setIdIngredientiInseriti(ArrayList<Integer> idIngredientiInseriti) {
        this.idIngredientiInseriti = idIngredientiInseriti;
    }

    public ArrayList<File> getFotoScelte() {
        return fotoScelte;
    }

    public FormProdottoDecorator getFormProdottoDecorator() {
        return formProdottoDecorator;
    }

    public Integer getIdProdottoModifica() {
        return idProdottoModifica;
    }
}
