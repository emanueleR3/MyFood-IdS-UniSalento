package it.unisalento.myfood.View.EsempiVistiALezione;/*package it.unisalento.myfood.View.EsempiVistiALezione;

import it.unisalento.myfood.Business.ImmagineBusiness;
import it.unisalento.myfood.Business.UtenteBusiness;
import it.unisalento.myfood.View.Decorator.Form.Form.Pulsantiera.Pulsantiera;
import it.unisalento.myfood.View.Decorator.Form.Form.Pulsantiera.PulsantieraClienteDecorator;
import it.unisalento.myfood.View.Decorator.Form.Form.Pulsantiera.PulsantieraGuest;
import it.unisalento.myfood.View.ViewModel.ListaArticoliTableModel;
import it.unisalento.myfood.View.LoginPanel;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class MixedLayoutFrame extends JFrame {

    private static MixedLayoutFrame instance = new MixedLayoutFrame();

    public static MixedLayoutFrame getInstance() {
        return instance;
    }

    private JPanel north = new JPanel();
    private JPanel west = new JPanel();

    private LoginPanel loginPanel;
    private LoggedInPanel loggedInPanel;

    JButton button = new JButton("Aggiungi al carrello");

    private MixedLayoutFrame() {
        super("Quarta finestra");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());    // Gestisce il layout degli elementi presenti nella finestra

        JPanel center = new JPanel();
        JPanel south = new JPanel();

        // 3 pannelli organizzati col Border Layout
        add(north, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
        add(west, BorderLayout.WEST);

        // Layout mixati
        north.setLayout(new FlowLayout());
        center.setLayout(new GridLayout(2, 1));
        south.setLayout(new FlowLayout());

        west.setLayout(new GridLayout(10, 1));

        Pulsantiera guestPulsantiera = new PulsantieraGuest();

        for (JButton btn : guestPulsantiera.getPulsanti()) {
            west.add(btn);
        }

        //north.add(label);
        //loginPanel = LoginPanel.getInstance();
        north.add(loginPanel);

        //center.add(new JCheckBox("Opz 1"));
        //center.add(new JCheckBox("Opz 2"));

        String[][] dati = new String[3][5];
        dati[0][0] = "a";
        dati[0][1] = "b";
        dati[0][2] = "c";
        dati[0][3] = "d";
        dati[0][4] = "e";
        dati[1][0] = "f";
        dati[1][1] = "g";
        dati[1][2] = "h";
        dati[1][3] = "i";
        dati[1][4] = "j";
        dati[2][0] = "k";
        dati[2][1] = "l";
        dati[2][2] = "m";
        dati[2][3] = "n";
        dati[2][4] = "o";


        String[] nomi_colonne = new String[] {"Prima colonna", "Seconda colonna", "Terza colonna",
                "Quarta colonna", "Quinta colonna"};

        JTable table = new JTable(dati, nomi_colonne);

        center.add(new JScrollPane(table));


        // MOCK TODO: ovviamente tutti i tipi prodotti sono da cambiare
        List<Prodotto> listaProdotti = new ArrayList<>();

        Prodotto p1 = new Prodotto();
        p1.setNome("Panino 1");
       // p1.setTipologiaProdotto(Prodotto(IArticolo.TIPO_PRODOTTO.PANINI));
        p1.setDescrizione("Panino croccante bamdwodwdn");
        p1.setPrezzo(8.99f);
        // esempio per far vedere un immagine
        ArrayList<String> foto = new ArrayList<>();
        foto.add("C:\\Users\\Utente\\Documents\\MyFood_images\\avatar.png");
        p1.setDirectoryFoto(foto);

        listaProdotti.add(p1);  //TODO: deve farlo il dao

        Prodotto p2 = new Prodotto();
        p2.setNome("Coca zero");
        // p2.setTipologiaProdotto(Prodotto(IArticolo.TIPO_PRODOTTO.BEVANDE));
        p2.setDescrizione("Bevanda con zero zuccheri");
        p2.setPrezzo(3.5f);

        listaProdotti.add(p2);

        Prodotto p3 = new Prodotto();
        p3.setNome("Insalata X");
        //p3.setTipologiaProdotto(Prodotto(IArticolo.TIPO_PRODOTTO.INSALATE));
        p3.setDescrizione("Insalata fresca blablabla");
        p3.setPrezzo(7.5f);

        listaProdotti.add(p3);

        ListaArticoliTableModel tableModel = new ListaArticoliTableModel(listaProdotti);

        JTable table = new JTable(tableModel);

        table.setRowHeight(100);
        center.add(new JScrollPane(table));

        table.setRowHeight(100);
        south.add(button);
        button.addActionListener(new ActionListener() { // TODO: farlo con una classe a parte
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] prodottiSelezionati = table.getSelectedRows();
                List<Prodotto> selezionati = new ArrayList<>(); // TODO: diverso, nel nostro caso è un HashMap
                for(int i : prodottiSelezionati) {
                    System.out.println("Selezionato prodotto con indice " + i);
                    Prodotto p = listaProdotti.get(i);;

                }
                UtenteBusiness.getSession().put(UtenteBusiness.CART, selezionati);   // TODO: Cambiare totalmente. Nel nostro caso non c'è il problema del carrello sovrascritto
            }
        });

        //south.add(new JButton("Log in"));

        // TODO: toglierlo da qua, è solo per far vedere come si può selezionare le immagini
        JButton selezionaFoto = new JButton("seleziona foto");

        selezionaFoto.addActionListener(e -> ImmagineBusiness.getInstance().chooseAndAddImage(2));  // ho messo 2 per provare, come se ci fosse una cartella articolo 2

        south.add(selezionaFoto);

        // Barra dei Menu
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");

        bar.add(file);
        bar.add(edit);

        edit.add(new JMenuItem("Up"));
        edit.add(new JMenuItem("Down"));

        setJMenuBar(bar);

        setVisible(true);
    }

    public void mostraUtenteLoggato() {
        north.remove(loginPanel);
        loggedInPanel = new LoggedInPanel();
        north.add(loggedInPanel);

        repaint();
        validate();
    }

    public void aggiornaPulsantiera() {
        Utente u = (Utente) UtenteBusiness.getSession().get(UtenteBusiness.LOGGED_IN_USER);

        west.removeAll();
        // u.getRuolo().equals(Utente.RUOLO.CLIENTE)    meglio mettere a sinistra una costante che non può essere null
        if(Utente.RUOLO.CLIENTE.equals(u.getRuolo())) {
            PulsantieraGuest pulsantieraGuest = new PulsantieraGuest();
            Pulsantiera pulsantieraCliente = new PulsantieraClienteDecorator(pulsantieraGuest);

            for (JButton btn : pulsantieraCliente.getPulsanti()) {
                west.add(btn);
            }
        }
        // TODO: else if amministratore...

        repaint();
        validate();
    }
}*/