package it.unisalento.myfood.View.Panels.Edit;

import it.unisalento.myfood.Business.*;
import it.unisalento.myfood.View.Decorator.Form.FormAdd;
import it.unisalento.myfood.View.Decorator.Form.FormEdit;
import it.unisalento.myfood.View.Decorator.Form.FormMenuDecorator;
import it.unisalento.myfood.View.Decorator.Form.FormView;
import it.unisalento.myfood.View.Decorator.Icon.OriginalIcon;
import it.unisalento.myfood.View.Decorator.Icon.TableIconDecorator;
import it.unisalento.myfood.View.Decorator.Label.TextLabel;
import it.unisalento.myfood.View.Decorator.Label.TitleTextLabelDecorator;
import it.unisalento.myfood.View.Frame;
import it.unisalento.myfood.Listener.Edit.EditMenuListener;
import it.unisalento.myfood.View.ViewModel.ListaImmaginiTableModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

public class EditMenuPanel extends JPanel {   //per convenzione lo chiamiamo edit ma si usa sia per aggiungere che per modificare
    private Frame frame;
    private EditMenuListener editMenuListener;
    private AziendaBusiness aziendaBusiness = AziendaBusiness.getInstance();
    private static HashMap<String, Object> session;
    private TextLabel title;
    private FormEdit formEdit;
    private FormAdd formAdd;
    private FormView formView;
    private ArticoloBusiness articoloBusiness;
    private FormMenuDecorator formMenuDecorator;
    private TreeMap<String, JTextField> textFields;
    private JLabel inserisciFoto;

    private JButton btnAddArticoli;
    private JButton btnSelezionaFoto;
    private JButton btnRimuoviFoto;

    private JPanel pnlFinal;
    private JPanel pnlGridFoto;

    private Object[][] immagini;

    private ArrayList<File> fotoScelte;
    private JTable selectedImagesTable;
    private JScrollPane scrollPaneImages;

    private ArrayList<Integer> idArticoliInseriti;

    private JComboBox<String> cBoxComposizioni;

    private String composizioneAttiva;
    private String composizioneSelezionata;
    private String operationStr;


    public EditMenuPanel(Frame frame) {
        this.frame = frame;
        session = UtenteBusiness.getSession();
        editMenuListener = new EditMenuListener(this, frame);
        articoloBusiness = ArticoloBusiness.getInstance();
        pnlGridFoto = new JPanel(new GridLayout(1, 2, 10, 10));
        pnlFinal = new JPanel(new FlowLayout());

        btnRimuoviFoto = new JButton("Rimuovi foto");
        btnRimuoviFoto.addActionListener(editMenuListener);
        btnRimuoviFoto.setActionCommand(EditMenuListener.RIMUOVI_FOTO_BTN);
        btnRimuoviFoto.setPreferredSize(new Dimension(200, 25));

        operationStr = session.get(UtenteBusiness.OPERATION).toString().charAt(0) + session.get(UtenteBusiness.OPERATION).toString().toLowerCase(Locale.ROOT).substring(1);

        setLayout(new BorderLayout());

        fotoScelte = new ArrayList<>();
        idArticoliInseriti = new ArrayList<>();
        title = new TextLabel(operationStr + " Menu");

        if(UtenteBusiness.OPERATIONS.VISUALIZZA.equals(session.get(UtenteBusiness.OPERATION))){
            formView = new FormView();
            formMenuDecorator = new FormMenuDecorator(formView);

            fotoScelte = ImmagineBusiness.getInstance().getImagesPerViewArticolo();

            Object[][] articoli = articoloBusiness.getArticoliViewMenu();
            for (Object[] articolo : articoli) idArticoliInseriti.add((int) articolo[0]);

        } else if(UtenteBusiness.OPERATIONS.AGGIUNGI.equals(session.get(UtenteBusiness.OPERATION))){
            formAdd = new FormAdd();
            formMenuDecorator = new FormMenuDecorator(formAdd);
        } else if(UtenteBusiness.OPERATIONS.MODIFICA.equals(session.get(UtenteBusiness.OPERATION))){
            fotoScelte = ImmagineBusiness.getInstance().getImagesPerViewArticolo();

            Object[][] articoli = articoloBusiness.getArticoliViewMenu();
            for (Object[] articolo : articoli) idArticoliInseriti.add((int) articolo[0]);


            formEdit = new FormEdit();
            formMenuDecorator = new FormMenuDecorator(formEdit);
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

        textFields = formMenuDecorator.getTextFields();

        JPanel fieldsPan = new JPanel(new FlowLayout());
        JPanel fieldsGrid = new JPanel(new GridLayout(0, 2, 0, 10));

        Iterator<Map.Entry<String, JTextField>> iterator = textFields.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, JTextField> entry = iterator.next();
            String labelText = entry.getKey();
            String upperTextLabel = labelText.toUpperCase(Locale.ROOT);
            JTextField textField = entry.getValue();

            if(UtenteBusiness.OPERATIONS.MODIFICA.equals(session.get(UtenteBusiness.OPERATION)) || UtenteBusiness.OPERATIONS.VISUALIZZA.equals(session.get(UtenteBusiness.OPERATION))){   //scrive nelle label il testo dei campi da modificare
                if ("Sconto (0-100 %)".toUpperCase().equals(upperTextLabel)) {
                    upperTextLabel = "SCONTO";
                    int sconto = (int) (((Float) articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.SCONTO)) * 100);
                    textField.setText(String.valueOf(sconto));
                } else {
                    Object text = articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.valueOf(upperTextLabel));
                    textField.setText(String.valueOf(text));
                }
            }

            fieldsGrid.add(new JLabel(labelText));
            JPanel pan = new JPanel(new FlowLayout());
            pan.add(textField);
            fieldsGrid.add(pan);
        }

        fieldsGrid.add(new JLabel("Menu composto da:"));
        loadComposizioni();

        if (UtenteBusiness.OPERATIONS.MODIFICA.equals(session.get(UtenteBusiness.OPERATION))) {
            composizioneAttiva = articoloBusiness.getComposizioneViewMenu();
            cBoxComposizioni.setSelectedItem(composizioneAttiva);
        } else {
            composizioneAttiva = (String) cBoxComposizioni.getSelectedItem();
        }
        cBoxComposizioni.addActionListener(editMenuListener);

        fieldsGrid.add(cBoxComposizioni);

        fieldsGrid.add(new JLabel("Inserisci/Modifica Articoli:"));
        fieldsGrid.add(btnAddArticoli);

        inserisciFoto = new JLabel("Inserisci foto:");
        fieldsGrid.add(inserisciFoto);
        btnSelezionaFoto = new JButton("Seleziona Foto dal Computer");
        btnSelezionaFoto.setPreferredSize(new Dimension(180, 25));
        btnSelezionaFoto.addActionListener(editMenuListener);
        btnSelezionaFoto.setActionCommand(EditMenuListener.SCEGLI_FOTO_BTN);
        fieldsGrid.add(btnSelezionaFoto);

        fieldsPan.add(fieldsGrid);
        add(fieldsPan, BorderLayout.CENTER);
    }


    private void loadPulsanti(){
        ArrayList<JButton> buttons = formMenuDecorator.getButtons();

        btnAddArticoli = new JButton();
        btnAddArticoli = buttons.remove(buttons.size() - 1);
        btnAddArticoli.setPreferredSize(new Dimension(180, 25));
        btnAddArticoli.addActionListener(editMenuListener);

        JPanel buttPan = new JPanel(new FlowLayout());
        JPanel gridPan = new JPanel(new GridLayout(buttons.size(), 1));
        Iterator<JButton> iterator = buttons.iterator();

        while (iterator.hasNext()){
            JPanel pnl = new JPanel(new FlowLayout());
            JButton button = iterator.next();
            button.setPreferredSize(new Dimension(180, 25));
            button.addActionListener(editMenuListener);
            pnl.add(button);
            gridPan.add(pnl);
        }

        buttPan.add(gridPan);
        add(buttPan, BorderLayout.EAST);

    }

    private void loadComposizioni() {
        HashMap<String, ArrayList<String>> listaComposizioni = articoloBusiness.loadComposizioniMenu();
        String[] composizioni = new String[listaComposizioni.size()];

        Iterator<Map.Entry<String, ArrayList<String>>> iterator = listaComposizioni.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<String>> entry = iterator.next();
            composizioni[i] = entry.getKey();
            i++;
        }

        cBoxComposizioni = new JComboBox<>(composizioni);
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
        if (fotoScelte.isEmpty())
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

    public ArrayList<Integer> getIdArticoliInseriti() {
        return idArticoliInseriti;
    }

    public ArrayList<File> getFotoScelte() {
        return fotoScelte;
    }

    public String getTableRowSelected(){
        if(selectedImagesTable == null || selectedImagesTable.getSelectedRow() == -1){  //se nessuna riga è selezionata
            JOptionPane.showMessageDialog(null, "Seleziona un'immagine");
            return null;
        } else
            return (String) immagini[selectedImagesTable.getSelectedRow()][1];
    }

    private void setViewMode(){
        Iterator<Map.Entry<String, JTextField>> iterator = textFields.entrySet().iterator();

        while (iterator.hasNext())
            iterator.next().getValue().setEnabled(false);

        cBoxComposizioni.setEnabled(false);
        btnRimuoviFoto.setVisible(false);
        btnSelezionaFoto.setVisible(false);
        btnSelezionaFoto.setVisible(false);
        btnRimuoviFoto.setVisible(false);
        btnAddArticoli.setText("Visualizza articoli");
        inserisciFoto.setVisible(false);

        cBoxComposizioni.setEnabled(false);


    }


    public ArrayList<String> getTipologieComposizioneScelta() {
        return articoloBusiness.getTipologiePerComposizione(getComposizioneScelta());
    }

    public String getComposizioneScelta() {
        return (String) cBoxComposizioni.getSelectedItem();
    }

    public void setComposizioneAttiva(String composizioneAttiva) {
        this.composizioneAttiva = composizioneAttiva;
    }

    public String getComposizioneAttiva() {
        return composizioneAttiva;
    }

    public JComboBox<String> getCBoxComposizioni() {
        return cBoxComposizioni;
    }

    public FormMenuDecorator getFormMenuDecorator() {
        return formMenuDecorator;
    }
}
