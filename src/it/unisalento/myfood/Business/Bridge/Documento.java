package it.unisalento.myfood.Business.Bridge;

public abstract class Documento {

    protected PdfAPI pdfAPI;

    public Documento(PdfAPI pdfAPI) {
        this.pdfAPI = pdfAPI;
    }

    public abstract void invia(String indirizzo);   // astratto perchè in base all'uso che si fa, avrà una funzione diversa
}
