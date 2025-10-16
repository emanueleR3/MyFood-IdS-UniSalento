package it.unisalento.myfood.Business.Bridge;

public abstract class Documento {

    protected PdfAPI pdfAPI;

    public Documento(PdfAPI pdfAPI) {
        this.pdfAPI = pdfAPI;
    }

    public abstract void inviaListaOrdineEffettuato(String indirizzo);   // astratto perchè in base all'uso che si fa, avrà una funzione diversa

    public abstract void inviaListaPagamentoEffettuato(String indirizzo);

    public abstract void inviaListaOrdineCompletato(String indirizzo);
}
