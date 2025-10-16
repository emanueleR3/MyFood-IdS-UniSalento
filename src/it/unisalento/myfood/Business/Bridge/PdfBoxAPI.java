package it.unisalento.myfood.Business.Bridge;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class PdfBoxAPI implements PdfAPI {

    private final static Integer UP_MARGIN = 700;
    private final static Integer LINE_OFFSET = 15;
    private final static Integer TILE_FONT_SIZE = 18;
    private final static Integer TEXT_FONT_SIZE = 12;
    private final static Integer TITLE_MARGIN = 300;
    private final static Integer TEXT_MARGIN = 100;



    @Override
    public void createPdf(ArrayList<String> lines, String outfile) {

        try (PDDocument doc = new PDDocument())
        {
            PDPage page = new PDPage();
            doc.addPage(page);

            PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            try (PDPageContentStream contents = new PDPageContentStream(doc, page))
            {
                contents.beginText();
                contents.setFont(font, TILE_FONT_SIZE);
                contents.newLineAtOffset(TITLE_MARGIN, UP_MARGIN);

                Iterator<String> iterator = lines.iterator();

                contents.newLine();
                contents.showText(iterator.next()); //stampa il titolo

                contents.newLineAtOffset(-TITLE_MARGIN + TEXT_MARGIN, -LINE_OFFSET);  //porta il testo a sinistra
                contents.setFont(font, TEXT_FONT_SIZE);
                contents.newLine();
                contents.showText(iterator.next());

                while (iterator.hasNext()){
                    contents.newLineAtOffset(0, -LINE_OFFSET);
                    contents.newLine();
                    contents.showText(iterator.next());

                }

                contents.endText();
            }

            doc.save(outfile);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
