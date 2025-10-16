package it.unisalento.myfood.Business.Bridge;

import it.unisalento.myfood.Business.MailSender;
import it.unisalento.myfood.DAO.ArticoloDAO;
import it.unisalento.myfood.DAO.UtenteDAO;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Ordine;
import it.unisalento.myfood.model.Utente;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class DocumentoOrdine extends Documento {

    private Ordine ordine;
    private final String oggettoOrdineEffettuato = "MyFood: Ordine effettuato";
    private final String messaggioEmailOrdineEffettuato = "Presentare la lista in allegato alla cassa per pagare";
    private final String oggettoOrdinePagato = "MyFood: Pagamento ricevuto!";
    private final String messaggioEmailOrdinePagato = """
            Il pagamento è stato ricevuto!
            La preghiamo di attendere la preparazione dell'ordine.
            
            Grazie per aver scelto MyFood!""";
    private final String oggettoOrdineCompletato = "MyFood: L'ordine è pronto!";
    private final ArticoloDAO articoloDAO = ArticoloDAO.getInstance();
    private final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public DocumentoOrdine(PdfAPI pdfAPI, Ordine ordine) {
        super(pdfAPI);
        this.ordine = ordine;
    }

    @Override
    public void inviaListaOrdineEffettuato(String indirizzo) {
        Utente u = UtenteDAO.getInstance().findById(ordine.getIdCliente());

        ArrayList<String> lines = new ArrayList<>();

        lines.add("MyFood");
        lines.add("");  //per lasciare dello spazio
        lines.add("Ordine n. " + ordine.getIdOrdine());
        lines.add("effettuato da " + u.getCognome() + " " + u.getNome());
        lines.add("in data " + ordine.getData());

        Iterator<Map.Entry<Integer, Integer>> iterator = ordine.getArticoli().entrySet().iterator();

        lines.add("");
        lines.add("Articoli: ");

        while(iterator.hasNext()){
            Map.Entry<Integer, Integer> entry = iterator.next();

            Integer idArticolo = entry.getKey();
            IArticolo articolo = articoloDAO.findById(idArticolo);
            Integer quantita = entry.getValue();

            lines.add(articolo.getNome() + " € " + articolo.getPrezzo() + " x" + quantita);
        }

        lines.add("");
        lines.add("Totale: € " + decimalFormat.format(ordine.getImporto()));
        lines.add("");
        lines.add("Presentare questo documento alla cassa per procedere con il pagamento");


        try {
            File tempFile = File.createTempFile("myfood", ".pdf");
            pdfAPI.createPdf(lines, tempFile.getAbsolutePath());
            MailSender.getInstance().sendWithAttachment(indirizzo, oggettoOrdineEffettuato, messaggioEmailOrdineEffettuato, tempFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void inviaListaPagamentoEffettuato(String indirizzo) {
        Utente u = UtenteDAO.getInstance().findById(ordine.getIdCliente());

        ArrayList<String> lines = new ArrayList<>();

        lines.add("MyFood");
        lines.add("");  //per lasciare dello spazio
        lines.add("Ordine n. " + ordine.getIdOrdine());
        lines.add("effettuato da " + u.getCognome() + " " + u.getNome());
        lines.add("in data " + ordine.getData());

        Iterator<Map.Entry<Integer, Integer>> iterator = ordine.getArticoli().entrySet().iterator();

        lines.add("");
        lines.add("Articoli: ");

        while(iterator.hasNext()){
            Map.Entry<Integer, Integer> entry = iterator.next();

            Integer idArticolo = entry.getKey();
            IArticolo articolo = articoloDAO.findById(idArticolo);
            Integer quantita = entry.getValue();

            lines.add(articolo.getNome() + " € " + articolo.getPrezzo() + " x" + quantita);
        }

        lines.add("");
        lines.add("Totale: € " + decimalFormat.format(ordine.getImporto()));
        lines.add("");
        lines.add("Il pagamento è stato ricevuto!");
        lines.add("La preghiamo di attendere la preparazione dell'ordine");
        lines.add("");
        lines.add("Grazie per aver scelto MyFood!");


        try {
            File tempFile = File.createTempFile("myfood", ".pdf");
            pdfAPI.createPdf(lines, tempFile.getAbsolutePath());
            MailSender.getInstance().sendWithAttachment(indirizzo, oggettoOrdinePagato, messaggioEmailOrdinePagato, tempFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void inviaListaOrdineCompletato(String indirizzo) {
        Utente u = UtenteDAO.getInstance().findById(ordine.getIdCliente());

        ArrayList<String> lines = new ArrayList<>();

        lines.add("MyFood");
        lines.add("");  //per lasciare dello spazio
        lines.add("Ordine effettuato da " + u.getCognome() + " " + u.getNome());
        lines.add("in data " + ordine.getData());

        Iterator<Map.Entry<Integer, Integer>> iterator = ordine.getArticoli().entrySet().iterator();

        lines.add("");
        lines.add("Articoli: ");

        while(iterator.hasNext()){
            Map.Entry<Integer, Integer> entry = iterator.next();

            Integer idArticolo = entry.getKey();
            IArticolo articolo = articoloDAO.findById(idArticolo);
            Integer quantita = entry.getValue();

            lines.add(articolo.getNome() + " € " + articolo.getPrezzo() + " x" + quantita);
        }

        lines.add("");
        lines.add("Totale: € " + decimalFormat.format(ordine.getImporto()));
        lines.add("");
        lines.add("La informiamo che l'ordine da lei effettuato è pronto per essere ritirato!");
        lines.add("Può essere ritirato direttamente al bancone presentando il codice dell'ordine:");
        lines.add("" + ordine.getIdOrdine());
        lines.add("");
        lines.add("Buon appetito!");

        String messaggioEmailOrdineCompletato =
                "Il suo ordine è pronto!\n" +
                "Può ritirarlo direttamente al bancone!\n" +
                "Codice Ordine: " + ordine.getIdOrdine() + "\n\n" +
                "Buon appetito!";

        try {
            File tempFile = File.createTempFile("myfood", ".pdf");
            pdfAPI.createPdf(lines, tempFile.getAbsolutePath());
            MailSender.getInstance().sendWithAttachment(indirizzo, oggettoOrdineCompletato, messaggioEmailOrdineCompletato, tempFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
