package it.unisalento.myfood.Business.Bridge;

import it.unisalento.myfood.Business.ArticoloBusiness;
import it.unisalento.myfood.Business.MailSender;
import it.unisalento.myfood.DAO.ArticoloDAO;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Ordine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DocumentoOrdine extends Documento {

    private Ordine ordine;
    private final String oggetto = "MyFood: Ordine non pagato";
    private final String messaggioEmail = "Presentare la lista in allegato alla cassa per pagare";
    private ArticoloDAO articoloDAO = ArticoloDAO.getInstance();
    public DocumentoOrdine(PdfAPI pdfAPI, Ordine ordine) {
        super(pdfAPI);
        this.ordine = ordine;
    }

    @Override
    public void invia(String indirizzo) {

        String text = "MyFood" +
                "ordine effettuato da : " + ordine.getCliente().getCognome() + " " + ordine.getCliente().getCognome() +
                " in data " + ordine.getData();

        Iterator<Map.Entry<Integer, Integer>> iterator = ordine.getArticoli().entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry<Integer, Integer> entry = iterator.next();

            Integer idArticolo = entry.getKey();
            IArticolo articolo = articoloDAO.findById(idArticolo);
            Integer quantita = entry.getValue();
            text += articolo.getNome() + " €" + articolo.getPrezzo() + " x" + quantita;
        }

        text += "Totale: €" + ordine.getIdOrdine();

        try {
            File tempFile = File.createTempFile("myfood", ".pdf");
            System.out.println(tempFile);
            pdfAPI.createPdf(text, tempFile.getAbsolutePath());
            MailSender.getInstance().sendWithAttachment(indirizzo, oggetto, messaggioEmail, tempFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        MailSender.getInstance().sendOnlyText(indirizzo, oggetto, messaggioEmail);
    }
}
