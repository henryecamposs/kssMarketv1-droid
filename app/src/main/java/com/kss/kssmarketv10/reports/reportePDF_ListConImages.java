package com.kss.kssmarketv10.reports;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.kss.kssmarketv10.kssSettings;
import com.kss.kssmarketv10.db.Productos;
import com.kss.kssutil.ToastManager;
import com.kss.kssutil.clsUtil_Files;
import com.kss.kssutil.enuToastIcons;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by KSS on 23/3/2017.
 */

public class reportePDF_ListConImages {

    private String DEST;
    private String IMG;
    private List<Productos> ProductosList;
    private String rutaMedia;
    private Context context;
    private kssSettings globalcls;

    private static final Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 26, Font.BOLDITALIC);
    private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);

    private static final Font categoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font subcategoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static final Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static final Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static final Font smallNormal = new Font(Font.FontFamily.TIMES_ROMAN, 9 );


    public enum POSITION {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT}

    public void crearReporte(Context context,
                             String ArchivoPDF,
                             String dirMedia,
                             List<Productos> ProductosList,
                             kssSettings kssSettings
    ) throws IOException, DocumentException {
        if (ProductosList != null)
            globalcls = kssSettings;
        if (ProductosList.size() > 0) {
            this.DEST = ArchivoPDF;
            this.ProductosList = ProductosList;
            rutaMedia = dirMedia;
            File file = new File(DEST);
            this.context = context;
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            createPdf(DEST);
        } else {
            ToastManager.show(context, "No existen Datos para Mostrar", enuToastIcons.WARNING, 10);
        }
    }

    class ImageEvent implements PdfPCellEvent {
        protected Image img;

        public ImageEvent(Image img) {
            this.img = img;
        }

        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
            img.scaleToFit(position.getWidth(), position.getHeight());
            img.setAbsolutePosition(position.getLeft() + (position.getWidth() - img.getScaledWidth()) / 2,
                    position.getBottom() + (position.getHeight() - img.getScaledHeight()) / 2);
            PdfContentByte canvas = canvases[PdfPTable.BACKGROUNDCANVAS];
            try {
                canvas.addImage(img);
            } catch (DocumentException ex) {
                ToastManager.show(context, "Se ha generado excepcion: " + ex.toString(), enuToastIcons.ERROR, 20);
            }
        }
    }

    class PositionEvent implements PdfPCellEvent {
        protected Phrase content;
        protected POSITION pos;

        public PositionEvent(Phrase content, POSITION pos) {
            this.content = content;
            this.pos = pos;
        }

        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.TEXTCANVAS];
            float x = 0;
            float y = 0;
            int alignment = 0;
            switch (pos) {
                case TOP_LEFT:
                    x = position.getLeft(3);
                    y = position.getTop(content.getLeading());
                    alignment = Element.ALIGN_LEFT;
                    break;
                case TOP_RIGHT:
                    x = position.getRight(3);
                    y = position.getTop(content.getLeading());
                    alignment = Element.ALIGN_RIGHT;
                    break;
                case BOTTOM_LEFT:
                    x = position.getLeft(3);
                    y = position.getBottom(3);
                    alignment = Element.ALIGN_LEFT;
                    break;
                case BOTTOM_RIGHT:
                    x = position.getRight(3);
                    y = position.getBottom(3);
                    alignment = Element.ALIGN_RIGHT;
                    break;
            }
            ColumnText.showTextAligned(canvas, alignment, content, x, y, 0);
        }
    }


    public void createPdf(String dest) throws IOException, DocumentException {
        try {
            clsUtil_Files. verifyStoragePermissions((Activity) context);

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();
            Chunk glue = new Chunk(new VerticalPositionMark());
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

            // Titulo
            Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 19, Font.BOLDITALIC);
            Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
            Chunk chunk = new Chunk(globalcls.getEmpresaNombre(), chapterFont);
            Chapter chapter = new Chapter(new Paragraph(chunk), 1);
            chapter.setNumberDepth(0);
            String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

            chapter.add(new Paragraph(globalcls.getEmpresaTelf() + " " + globalcls.getEmpresaDireccion(), paragraphFont));
            chapter.add(new Paragraph("Email: " + globalcls.getEmpresaEmailGmail(), paragraphFont));
            chapter.add(new Paragraph("Fecha: " + timeStamp  , smallNormal));
            Paragraph pListado=new Paragraph("Lista de Precios" , chapterFont);
            pListado.setAlignment(Element.ALIGN_CENTER);
            chapter.add(pListado);
            document.add(chapter);


            //items de pedido
            PdfPCell[] cells = new PdfPCell[ProductosList.size()];
            int iCell = 0;
            for (Productos producto : ProductosList) {
                String sImagen = rutaMedia + producto.getId() + ".jpg";
                File file = new File(sImagen);
                Boolean Existe = file.exists();
                if (!Existe) sImagen = rutaMedia + "logoempresa.jpg";
                cells[iCell] = new PdfPCell();
                cells[iCell].setBorder(0);
                cells[iCell].setPaddingRight(20);
                //Contenido
                Paragraph p = new Paragraph();
                p.setPaddingTop(5);
                p.setSpacingBefore(5);
                Paragraph pCodigo = new Paragraph(String.format("Código: %03d", producto.getId()), new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD));
                Paragraph pProducto = new Paragraph(producto.getProducto(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLUE));
                Paragraph pProductoDesc = new Paragraph(producto.getProducto_DescripcionLarga().toString().length()==0? producto.getProducto():producto.getProducto_DescripcionLarga(), new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC, BaseColor.BLACK));
                Paragraph pPrecio = new Paragraph("  Bs.  " + Double.toString(producto.getMontoPrecio1()), new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.RED));
                pCodigo.setAlignment(Element.ALIGN_RIGHT);
                pPrecio.setAlignment(Element.ALIGN_RIGHT);
                pProducto.setAlignment(Element.ALIGN_RIGHT);
                pProductoDesc.setAlignment(Element.ALIGN_RIGHT);
                Image img = Image.getInstance(sImagen);
                img.setPaddingTop(10);
                img.setSpacingBefore(10);
                p.add(pCodigo);
                p.add(pProducto);
                p.add(pProductoDesc);
                p.add(pPrecio );
               // p.add( chunk.NEWLINE);
                p.add(new Chunk(img, 0, 0, true));
                cells[iCell].addElement(p);
                table.addCell(cells[iCell]);
                iCell++;
            }
            document.add(table);
            document.close();
            ToastManager.show(context, "Archivo generado con Exito!", enuToastIcons.OK, 10);
        } catch (Exception ex) {
            Toast.makeText(context, "Se ha generado excepcion:" + ex.toString(), Toast.LENGTH_LONG);
        }
    }
}
//Agregar Imagen de Fondo
                /*ImageEvent imgEvent = new ImageEvent(Image.getInstance(sImagen));
                cells[iCell].setFixedHeight(70);
                cells[iCell].setCellEvent(imgEvent);
                cells[iCell].setCellEvent(new PositionEvent(
                        new Phrase(
                                String.format("Código: %03d", producto.getId()) +
                                        producto.getProducto() + "\n" +
                                        producto.getProducto_DescripcionLarga()),
                        POSITION.TOP_LEFT));*/
