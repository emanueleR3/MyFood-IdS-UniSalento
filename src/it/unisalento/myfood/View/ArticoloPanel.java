package it.unisalento.myfood.View;

import it.unisalento.myfood.Business.*;
import it.unisalento.myfood.View.Decorator.Icon.IconResizedDecorator;
import it.unisalento.myfood.View.Decorator.Icon.OriginalIcon;
import it.unisalento.myfood.View.Decorator.Label.*;
import it.unisalento.myfood.View.Listener.ArticoloPanelListener;
import it.unisalento.myfood.View.Listener.TextArticoloPanelFocusListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Timer;

public class ArticoloPanel extends JPanel {
    private static final String[] TIPI_DI_VALUTAZIONE = {"-", "1", "2", "3", "4", "5"}; // il "-" ci serve per controllare che il cliente abbia inserito effettivamente un numero
    private static boolean alreadyCommented;

    private static final CarrelloBusiness carrelloBusiness = CarrelloBusiness.getInstance();
    private static final InterazioneUtenteBusiness interazioneUtenteBusiness = InterazioneUtenteBusiness.getInstance();
    private static final ArticoloBusiness articoloBusiness = ArticoloBusiness.getInstance();

    private static ArrayList<ImageIcon> foto;
    private int currentIndex = 0;
    private static Timer timer = new Timer();

    private JButton addToCart = new JButton();
    private final JButton backToCatalogue = new JButton("Torna al Catalogo");
    private static JButton btnInvioCommento = new JButton("Invia commento");
    private static JButton btnEditCommento = new JButton("Modifica il commento");
    private static JButton btnRemoveCommento = new JButton("Rimuovi Commento");

    private static JPanel invioCommentoPanel = new JPanel(new FlowLayout());
    private static JPanel gridCommentiPanel = new JPanel(new GridLayout(1, 1));
    private static JPanel pnlFoto = new JPanel(new FlowLayout());

    private JLabel pezziDisponibiliArticolo = new JLabel();
    private JLabel quantitaArticolo = new JLabel();
    private JLabel prezzoArticolo = new JLabel();

    private static JTextArea textArea = new JTextArea();;
    private static String placeholderHint = "Inserisci il commento qui...";

    private static JComboBox<String> comboBoxValutazione = new JComboBox<>(TIPI_DI_VALUTAZIONE);

    private JScrollPane scrollPaneCommenti;

    public ArticoloPanel(Frame frame) {
        // Per salvarci se un articolo è un menu o un prodotto
        boolean articoloIsProdotto = articoloBusiness.isSelectedArticoloInstanceOfProdotto();

        setLayout(new BorderLayout());

        String nomeArticolo = (String) articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.NOME);
        String descrizioneArticolo = (String) articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.DESCRIZIONE);

        // Nome del prodotto in alto
        JPanel name = new JPanel(new FlowLayout());


        TextLabel title = new TextLabel(nomeArticolo);
        TitleTextLabelDecorator titleTextLabel = new TitleTextLabelDecorator(title);
        name.add(titleTextLabel.getLabel());
        add(name, BorderLayout.NORTH);

        // Foto a sx e nome e descrizione a dx
        JPanel gridNameDescription = new JPanel(new GridLayout(3, 1));

        JPanel nameArticolo = new JPanel(new FlowLayout());
        nameArticolo.add(new JLabel(nomeArticolo));
        gridNameDescription.add(nameArticolo);

        JPanel description = new JPanel(new FlowLayout());
        description.add(new JLabel(descrizioneArticolo));
        gridNameDescription.add(description);

        JPanel pnlPrezzo = new JPanel(new FlowLayout());
        if (articoloIsProdotto) {
            float prezzo = (Float) articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.PREZZO);
            prezzoArticolo.setText("€ " + formatCurrency(prezzo));
            pnlPrezzo.add(prezzoArticolo);
        } else {
            float sconto = (Float) articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.SCONTO_MENU);
            float prezzoMenu = (Float) articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.PREZZO);

            if (sconto > 0) {
                float prezzoOriginario = prezzoMenu / (1 - sconto);
                TextLabel prezzoScontato = new TextLabel("Menu scontato del " + sconto * 100 + "%! Tuo a soli € " + formatCurrency(prezzoMenu) + ", invece di € " + formatCurrency(prezzoOriginario));

                ScontoTextLabelDecorator scontoTextLabel = new ScontoTextLabelDecorator(prezzoScontato);

                pnlPrezzo.add(scontoTextLabel.getLabel());
            } else {
                prezzoArticolo.setText("€ " + formatCurrency(prezzoMenu));
            }
        }
        gridNameDescription.add(pnlPrezzo);

        // Foto
        ArrayList<String> directoryFoto = ArticoloBusiness.getInstance().getDirectoryFotoViewArticolo();

        this.currentIndex = 0;
        foto = new ArrayList<>();
        for (String s : directoryFoto) {
            OriginalIcon originalIcon = new OriginalIcon(s);
            IconResizedDecorator iconResizedDecorator = new IconResizedDecorator(originalIcon, 200, 200);
            foto.add(iconResizedDecorator.getImageIcon());
        }

        if (directoryFoto.size() == 1) {
            pnlFoto.removeAll();
            JLabel fotoAssente = new JLabel(foto.get(0));
            pnlFoto.add(fotoAssente);
        } else if (directoryFoto.size() > 1) {
            pnlFoto.removeAll();
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    displayNextImage();
                }
            }, 0, 3000);
        } else {
            pnlFoto.removeAll();
            JLabel fotoAssente = new JLabel(new ImageIcon("src/immagini/icone/foto_prodotto_assente.jpg"));
            pnlFoto.add(fotoAssente);
        }

        JPanel articoloInfo = new JPanel(new FlowLayout());
        articoloInfo.add(pnlFoto);
        articoloInfo.add(gridNameDescription);

        // Ingredienti e tipologie
        JPanel gridTipologiaEIngredienti = new JPanel(new GridLayout(2, 1));

        TextLabel textLabel = null;
        JPanel tipologiaProdotto = new JPanel(new FlowLayout());
        if (articoloIsProdotto) {
            textLabel = new TextLabel("Tipologia: " + (TipologiaProdottoBusiness.getInstance().getCampoTipologiaViewProdotto(TipologiaProdottoBusiness.CAMPO.NOME)));
            DettagliTextLabelDecorator dettagliTextLabel = new DettagliTextLabelDecorator(textLabel);
            tipologiaProdotto.add(dettagliTextLabel.getLabel());
        } else {
            textLabel = new TextLabel("Tipologia: Menu");
            DettagliTextLabelDecorator dettagliTextLabel = new DettagliTextLabelDecorator(textLabel);
            tipologiaProdotto.add(dettagliTextLabel.getLabel());
        }
        gridTipologiaEIngredienti.add(tipologiaProdotto);

        if (articoloIsProdotto) {
            JPanel ingredienti = new JPanel(new FlowLayout());
            textLabel = new TextLabel(loadIngredienti());
            DettagliTextLabelDecorator dettagliTextLabel = new DettagliTextLabelDecorator(textLabel);
            ingredienti.add(dettagliTextLabel.getLabel());
            gridTipologiaEIngredienti.add(ingredienti);

        } else {
            JPanel componenti = new JPanel(new FlowLayout());
            textLabel = new TextLabel(loadComponentiMenu());
            DettagliTextLabelDecorator dettagliTextLabel = new DettagliTextLabelDecorator(textLabel);
            componenti.add(dettagliTextLabel.getLabel());
            gridTipologiaEIngredienti.add(componenti);
        }

        JPanel flowTipologiaEIngredienti = new JPanel(new FlowLayout());
        flowTipologiaEIngredienti.add(gridTipologiaEIngredienti);

        // Commenti
        ActionListener articoloListener = new ArticoloPanelListener(frame, this);
        btnRemoveCommento.addActionListener(articoloListener);
        btnRemoveCommento.setActionCommand(ArticoloPanelListener.REMOVE_COMMENT_BTN);

        btnInvioCommento.addActionListener(articoloListener);
        btnInvioCommento.setActionCommand(ArticoloPanelListener.SEND_COMMENT_BTN);

        btnEditCommento.addActionListener(articoloListener);
        btnEditCommento.setActionCommand(ArticoloPanelListener.EDIT_COMMENT_BTN);

        comboBoxValutazione.addActionListener(articoloListener);

        FocusListener textListener = new TextArticoloPanelFocusListener(frame, this);
        textArea.addFocusListener(textListener);

        gridCommentiPanel = new JPanel(new GridLayout(1, 1));
        gridCommentiPanel.add(createUICommento());

        // Griglia finale
        JPanel finalGrid = new JPanel(new GridLayout(3, 1));
        finalGrid.add(articoloInfo);
        finalGrid.add(flowTipologiaEIngredienti);
        finalGrid.add(gridCommentiPanel);

        JPanel finalFlowLayout = new JPanel(new FlowLayout());
        finalFlowLayout.add(finalGrid);

        add(finalFlowLayout, BorderLayout.CENTER);

        // SOUTH
        int quantita = loadQuantitaArticolo();

        JPanel firstRowSouth = new JPanel(new FlowLayout());
        pezziDisponibiliArticolo.setText("Pezzi disponibili: " + articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.DISPONIBILITA) + "  ");
        quantitaArticolo.setText("  Hai " + quantita + " pezzi di questo articolo nel carrello");
        firstRowSouth.add(pezziDisponibiliArticolo);
        firstRowSouth.add(quantitaArticolo);


        // Pulsanti
        if (quantita == 0) {
            addToCart.setText("Aggiungi al Carrello");
        } else {
            addToCart.setText("Modifica Quantità nel Carrello");
        }

        addToCart.addActionListener(articoloListener);
        backToCatalogue.addActionListener(articoloListener);

        addToCart.setActionCommand(ArticoloPanelListener.ADD_TO_CART_BTN);
        backToCatalogue.setActionCommand(ArticoloPanelListener.BACK_TO_CATALOGUE_BTN);

        JPanel secondRowSouth = new JPanel(new FlowLayout());
        secondRowSouth.add(addToCart);
        secondRowSouth.add(backToCatalogue);

        JPanel gridSouth = new JPanel(new GridLayout(2, 1));
        gridSouth.add(firstRowSouth);
        gridSouth.add(secondRowSouth);

        JPanel flowSouth = new JPanel(new FlowLayout());
        flowSouth.add(gridSouth);

        add(flowSouth, BorderLayout.SOUTH);
    }

    private String loadIngredienti() {
        ArrayList<String> nomeIngredienti = IngredienteBusiness.getInstance().getNomiIngredientiViewProdotto();

        StringBuilder text = new StringBuilder("Ingredienti: ");

        Iterator<String> iterator = nomeIngredienti.iterator();

        if (iterator.hasNext()) {
            text.append(iterator.next());
        } else {
            text.append("non sono presenti ingredienti per questo prodotto");
        }
        while (iterator.hasNext()){
            text.append(", ").append(iterator.next());
        }

        return text.toString();
    }

    private JScrollPane loadCommenti() {
        ArrayList<CommentoPanel> commentiPanelsList = new ArrayList<>();

        int numberOfCommenti = interazioneUtenteBusiness.getNumberOfCommentiForSelectedArticolo();
        for (int i = 0; i < numberOfCommenti; i++) {
            String testoCommento = (String) interazioneUtenteBusiness.getCampoCommento(i, InterazioneUtenteBusiness.CAMPO_COMMENTO.TESTO);
            String nomeCliente = (String) interazioneUtenteBusiness.getCampoCommento(i, InterazioneUtenteBusiness.CAMPO_COMMENTO.NOME_CLIENTE);
            Timestamp dataCommento = (Timestamp) interazioneUtenteBusiness.getCampoCommento(i, InterazioneUtenteBusiness.CAMPO_COMMENTO.DATA_COMMENTO);
            int valutazione = (int) interazioneUtenteBusiness.getCampoCommento(i, InterazioneUtenteBusiness.CAMPO_COMMENTO.VALUTAZIONE);

            commentiPanelsList.add(new CommentoPanel(testoCommento, valutazione, nomeCliente, dataCommento));
        }

        // Il pannello che conterrà i commenti
        JPanel commentiPanel = new JPanel();
        commentiPanel.setLayout(new BoxLayout(commentiPanel, BoxLayout.Y_AXIS));

        // Aggiungo i pannelli commento al pannello finale che li contiene tutti
        for (CommentoPanel cp : commentiPanelsList) {
            commentiPanel.add(cp);
        }

        scrollPaneCommenti = new JScrollPane(commentiPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        return scrollPaneCommenti;
    }

    private String loadComponentiMenu(){
        ArrayList<String> nomeComponenti = articoloBusiness.getNomeComponentiViewMenu();

        Iterator<String> iterator = nomeComponenti.iterator();

        StringBuilder text = new StringBuilder("Contiene: ");

        text.append(iterator.next());
        while(iterator.hasNext()){
            text.append(", ").append(iterator.next());
        }

        return text.toString();
    }

    private int loadQuantitaArticolo() {
        return carrelloBusiness.getViewArticoloQuantityInCart();
    }

    private JPanel createUICommento() {

        // Numero di righe da mostrare
        int numberOfRows = UtenteBusiness.getInstance().isLoggedCliente() ? 4 : 2;
        JPanel gridCommentiPanel = new JPanel(new GridLayout(numberOfRows, 1));

        // Prima riga
        gridCommentiPanel.add(new JLabel("Commenti: "));
        // Seconda riga
        if (!interazioneUtenteBusiness.selectedArticoloHasCommenti()) {
            gridCommentiPanel.add(new JLabel("Non ci sono commenti per quest'articolo"));
        } else {
            gridCommentiPanel.add(loadCommenti());
        }

        if (numberOfRows == 4) {
            // Terza riga
            JPanel creaCommentoPanel = new JPanel(new FlowLayout());

            alreadyCommented = UtenteBusiness.getInstance().loggedUtenteHasCommentedArticolo();

            JScrollPane scrollPaneText;

            textArea.setRows(2);
            textArea.setColumns(40);
            textArea.setLineWrap(true);  // Imposta l'avvolgimento delle linee
            textArea.setWrapStyleWord(true);  // Imposta l'avvolgimento delle parole

            if (!alreadyCommented) {
                textArea.setText(placeholderHint);
                textArea.setForeground(Color.GRAY);
                textArea.setEnabled(true);

                scrollPaneText = new JScrollPane(textArea);
                creaCommentoPanel.add(scrollPaneText);

                creaCommentoPanel.add(new JLabel("Valutazione: "));
                comboBoxValutazione.setEnabled(true);
                creaCommentoPanel.add(comboBoxValutazione);
                creaCommentoPanel.add(new JLabel("/ 5"));
            } else {
                textArea.setText(interazioneUtenteBusiness.getTestoCommentoViewArticolo());
                textArea.setForeground(Color.BLACK);
                textArea.setEnabled(false);

                scrollPaneText = new JScrollPane(textArea);
                creaCommentoPanel.add(scrollPaneText);

                creaCommentoPanel.add(new JLabel("Valutazione: "));
                comboBoxValutazione.setEnabled(false);
                creaCommentoPanel.add(comboBoxValutazione);
                creaCommentoPanel.add(new JLabel("/ 5"));
            }

            // Quarta riga
            if (!alreadyCommented) {
                invioCommentoPanel.remove(btnEditCommento);
                invioCommentoPanel.remove(btnRemoveCommento);

                btnInvioCommento.setEnabled(true);
                invioCommentoPanel.add(btnInvioCommento);
            } else {
                invioCommentoPanel.remove(btnInvioCommento);
                invioCommentoPanel.add(btnEditCommento);
                invioCommentoPanel.add(btnRemoveCommento);
            }

            // Inserimento nella grid
            gridCommentiPanel.add(creaCommentoPanel);
            gridCommentiPanel.add(invioCommentoPanel);

            revalidate();
            repaint();
        }

        return gridCommentiPanel;
    }

    public void updateUICommento() {
        gridCommentiPanel.removeAll();
        gridCommentiPanel.add(createUICommento());
    }

    public void updateUIQuantita(int quantita) {
        pezziDisponibiliArticolo.setText("Pezzi disponibili: " + articoloBusiness.getCampoSelectedArticolo(ArticoloBusiness.CAMPO.DISPONIBILITA) + "  ");
        quantitaArticolo.setText("  Hai " + quantita + " pezzi di questo articolo nel carrello");

        if (quantita == 0) {
            addToCart.setText("Aggiungi al Carrello");
        } else {
            addToCart.setText("Modifica Quantità nel Carrello");
        }
    }

    private void displayNextImage() {
        if (foto.size() > 1) {
            // Ottieni l'immagine corrente
            ImageIcon currentImageIcon = foto.get(currentIndex);

            // Aggiungi l'immagine corrente al pannello
            JLabel imageLabel = new JLabel(currentImageIcon);
            pnlFoto.removeAll();
            pnlFoto.add(imageLabel);
            pnlFoto.revalidate();
            pnlFoto.repaint();

            // Passa all'immagine successiva
            currentIndex = (currentIndex + 1) % foto.size();
        }
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JComboBox<String> getComboBoxValutazione() {
        return comboBoxValutazione;
    }

    public JButton getBtnInvioCommento() {
        return btnInvioCommento;
    }

    public JButton getBtnRemoveCommento() {
        return btnRemoveCommento;
    }

    public JButton getBtnEditCommento() {
        return btnEditCommento;
    }

    public boolean isAlreadyCommented() {
        return alreadyCommented;
    }

    public JPanel getInvioCommentoPanel() {
        return invioCommentoPanel;
    }

    public Timer getTimer() {
        return timer;
    }

    public String getPlaceholderHint() {
        return placeholderHint;
    }

    private String formatCurrency(float price) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(price);
    }
}
