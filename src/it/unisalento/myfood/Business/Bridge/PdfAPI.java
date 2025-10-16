package it.unisalento.myfood.Business.Bridge;

import java.util.ArrayList;

public interface PdfAPI {

    void createPdf(ArrayList<String> lines, String outfile);
}
