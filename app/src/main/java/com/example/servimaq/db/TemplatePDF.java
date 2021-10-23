package com.example.servimaq.db;

import android.content.Context;
import android.content.Intent;

import android.os.Environment;
import android.util.Log;

import com.example.servimaq.op_documentos.GenerarDocumentoPDF;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TemplatePDF {

    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fTitulo = new Font(Font.FontFamily.TIMES_ROMAN,20,Font.BOLD);
    private Font fSubtitulo = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD);
    private Font fTexto = new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD);
    private Font fHighText = new Font(Font.FontFamily.TIMES_ROMAN,15,Font.BOLD, BaseColor.RED);

    public TemplatePDF(Context context) {
        this.context = context;
    }

    public void OpenDocument(){
        CreateFile();
        try {
            document = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
        }catch (Exception e){
            Log.e("Abrir documento",e.toString());
        }
    }

    private void CreateFile(){

        File folder = new File(Environment.getExternalStorageDirectory().toString(),"PDF");
        if(!folder.exists()){
            folder.mkdirs();
        }
        pdfFile = new File(folder, "TemplatePDF.pdf");
    }

    public void CloseDocument(){
        document.close();
    }

    public void AddMetaData(String titulo, String tema, String autor){
        document.addTitle(titulo);
        document.addSubject(tema);
        document.addAuthor(autor);
    }

    public void AddTitles(String titulo, String subtitulo, String fecha){
        try {
            paragraph = new Paragraph();
            AddChildP(new Paragraph(titulo, fTitulo));
            AddChildP(new Paragraph(subtitulo, fSubtitulo));
            AddChildP(new Paragraph("Generado: "+fecha, fHighText));
            paragraph.setSpacingAfter(30);
            document.add(paragraph);
        } catch (Exception e){
            Log.e("AddTitles",e.toString());
        }

    }

    private void AddChildP(Paragraph childParagraph){
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }

    public void AddParagraph(String texto){
        try {
        paragraph = new Paragraph(texto, fTexto);
        paragraph.setSpacingAfter(5);
        paragraph.setSpacingBefore(5);
        document.add(paragraph);
        } catch (Exception e){
            Log.e("AddParagraph",e.toString());
        }
    }

    public void CreateTable(String[] cabecera, ArrayList<String[]> clientes){
        try {
        paragraph = new Paragraph();
        paragraph.setFont(fTexto);
        PdfPTable pdfPTable = new PdfPTable(cabecera.length);
        pdfPTable.setWidthPercentage(100);
        PdfPCell pdfPCell;
        int indexC = 0;
        while (indexC < cabecera.length){
            pdfPCell = new PdfPCell(new Phrase(cabecera[indexC++],fSubtitulo));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBackgroundColor(BaseColor.BLUE);
            pdfPTable.addCell(pdfPCell);
        }

        for(int indexRow = 0; indexRow < clientes.size(); indexRow++){
            String[] row = clientes.get(indexRow);
            for(int indexColumn = 0; indexColumn < cabecera.length; indexColumn++){
                pdfPCell = new PdfPCell(new Phrase(row[indexColumn]));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setFixedHeight(40);
                pdfPTable.addCell(pdfPCell);
            }
        }

        paragraph.add(pdfPTable);
        document.add(paragraph);

        } catch (Exception e){
            Log.e("CreateTable",e.toString());
        }
    }

    public void MirarPDF(){
        Intent intent = new Intent(context, GenerarDocumentoPDF.class);
        intent.putExtra("ruta",pdfFile.getAbsolutePath());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
