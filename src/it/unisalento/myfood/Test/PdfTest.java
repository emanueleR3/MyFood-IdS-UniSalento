package it.unisalento.myfood.Test;

import it.unisalento.myfood.Business.Bridge.DocumentoOrdine;
import it.unisalento.myfood.Business.Bridge.PdfAPI;
import it.unisalento.myfood.Business.Bridge.PdfBoxAPI;
import it.unisalento.myfood.model.Composite.IArticolo;
import it.unisalento.myfood.model.Composite.Prodotto;
import it.unisalento.myfood.model.Ordine;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PdfTest {

    @Test
    public void documentoOrdinetest() {
        PdfAPI pdfAPI = new PdfBoxAPI();

        Prodotto p = new Prodotto();
        p.setNome("Panino");
        p.setPrezzo(12.55f);

        List<IArticolo> articoli = new ArrayList<>();
        articoli.add(p);
    }
}
