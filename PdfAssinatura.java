import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfAssinatura {

  private static final String INPUT_FILE = "input.pdf";
  private static final String OUTPUT_FILE = INPUT_FILE.split("\\.")[0] + "Assinado.pdf";

  public static void main(String[] args) {

    String txtAssinaturaA = "Documento assinado eletronicamente por Vtec System";
    String txtAssinaturaB = "Processo número 2022.01.10.0001";
    PdfReader reader;

    try {
      // Ler entrada
      reader = new PdfReader(INPUT_FILE);
      // Tamanho da pagina
      Rectangle pageSize = reader.getPageSize(1);
      float width = pageSize.getWidth();
      float height = pageSize.getHeight();
      // Quantidade de paginas
      int numberPages = reader.getNumberOfPages();
      PdfImportedPage page;
      // Novo documento
      Document document = new Document();
      document.setMargins(0, 0, 0, 0);
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_FILE));
      document.open();
      for (int i = 1; i <= numberPages; i++) {
        // Adicionar cada página ao novo pdf
        page = writer.getImportedPage(reader, i);
        Image instance = Image.getInstance(page);
        document.add(instance);
        // Adicionar assinatura apenas na primeira página
        if (i == 1) {
          // Criar elemento da assinatura
          PdfTemplate textTemplate = writer.getDirectContent().createTemplate(pageSize.getWidth(),
              pageSize.getHeight());
          Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10);
          font.setColor(new BaseColor(0, 0, 100));
          // Adicionar texto com fonte
          Paragraph assinaturaA = new Paragraph(txtAssinaturaA, font);
          Paragraph assinaturaB = new Paragraph(txtAssinaturaB, font);
          ColumnText assinatura = new ColumnText(textTemplate);
          assinatura.setSimpleColumn(0, 0, width, height);
          assinatura.addElement(assinaturaA);
          assinatura.addElement(assinaturaB);
          assinatura.go();
          // Comprimento do texto
          float textSize = assinatura.getFilledWidth();
          float verticalCenter = (height / 2) - (textSize / 2);
          // Quantidade de linhas
          int lines = assinatura.getLinesWritten();
          Image textImg = Image.getInstance(textTemplate);
          // Rotacionar o texto
          textImg.setRotationDegrees(90);
          // Posição da assinatura
          textImg.setAbsolutePosition(width - (lines * 20), verticalCenter);
          // Adicionar assinatura no documento
          document.add(textImg);
        }
      }
      document.close();
    } catch (IOException ioError) {
      ioError.printStackTrace();
    } catch (DocumentException docError) {
      docError.printStackTrace();
    }
  }
}