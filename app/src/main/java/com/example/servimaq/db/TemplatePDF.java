package com.example.servimaq.db;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

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
import com.microsoft.sqlserver.jdbc.dataclassification.Label;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TemplatePDF {

    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fEmpresa = new Font(Font.FontFamily.TIMES_ROMAN,25,Font.BOLD);
    private Font fTitulo = new Font(Font.FontFamily.TIMES_ROMAN,20,Font.BOLD);
    private Font fSubtitulo = new Font(Font.FontFamily.TIMES_ROMAN,16,Font.BOLD);
    private Font fTexto = new Font(Font.FontFamily.TIMES_ROMAN,12,Font.NORMAL);
    private Font fHighText = new Font(Font.FontFamily.TIMES_ROMAN,16,Font.BOLD, BaseColor.RED);
    private Font fCliente = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD, BaseColor.BLUE);

    public TemplatePDF(Context context) {
        this.context = context;
    }

    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED;
    }

    // Checks if a volume containing external storage is available to at least read.
    private boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED ||
                Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED_READ_ONLY;
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
        isExternalStorageWritable();
        isExternalStorageReadable();
        File folder = new File(context.getExternalFilesDir("Documentos"), "PDF");
        //File folder = new File(Environment.getExternalStorageDirectory().toString(),"PDF");
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

    public void AddTituloEmpresa(String Empresa, String titulo, String subtitulo, String CodigoPedido, String RUC){
        try {
            paragraph = new Paragraph();
            AddTituloEmpresa(new Paragraph(titulo + " - " + Empresa,fEmpresa));
            AddTituloEmpresa(new Paragraph(subtitulo + CodigoPedido, fTitulo));
            AddTituloEmpresa(new Paragraph("RUC: "+RUC, fHighText));
            paragraph.setSpacingAfter(25);
            document.add(paragraph);
        } catch (Exception e){
            Log.e("AddTituloEmpresa",e.toString());
        }

    }

    public void AddTitles(String Direccion, String Ubicacion, String FechaActual){
        try {
            paragraph = new Paragraph();
            AddChildP(new Paragraph("Direccion: "+Direccion,fSubtitulo));
            AddChildP(new Paragraph("Ubicacion: "+Ubicacion,fSubtitulo));
            AddChildP(new Paragraph("Fecha inicial del pedido: "+FechaActual,fSubtitulo));
            paragraph.setSpacingAfter(25);
            document.add(paragraph);
        } catch (Exception e){
            Log.e("AddTitles",e.toString());
        }
    }

    public void AddDatosCliente(String NombreCliente, String Correo, String DNI, String FechaEntrega){
        try {
            paragraph = new Paragraph();
            AddChildP(new Paragraph("Datos del Cliente: ",fCliente));
            AddChildP(new Paragraph("Nombres y Apellidos: "+NombreCliente,fSubtitulo));
            AddChildP(new Paragraph("Fecha limite de Entrega: "+FechaEntrega+"       DNI: "+DNI,fSubtitulo));
            AddChildP(new Paragraph("Correo: "+Correo,fSubtitulo));
            paragraph.setSpacingAfter(25);
            document.add(paragraph);
        } catch (Exception e){
            Log.e("AddTitles",e.toString());
        }
    }

    private void AddChildP(Paragraph childParagraph){
        childParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        paragraph.add(childParagraph);
    }

    private void AddTituloEmpresa(Paragraph childParagraph){
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

    public void CreateTable(String[] cabecera, ArrayList<String[]> Neumaticos){
        try {
        paragraph = new Paragraph();
        paragraph.setFont(fTexto);
        PdfPTable pdfPTable = new PdfPTable(new float[] {0.8F, 0.7F,0.6F,3,0.6F,0.8F});
        pdfPTable.setWidthPercentage(100);
        PdfPCell pdfPCell;
        int indexC = 0;
        while (indexC < cabecera.length){
            pdfPCell = new PdfPCell(new Phrase(cabecera[indexC++],fSubtitulo));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBackgroundColor(BaseColor.GRAY);
            pdfPCell.setFixedHeight(25);
            pdfPTable.addCell(pdfPCell);
        }

        for(int indexRow = 0; indexRow < Neumaticos.size(); indexRow++){
            String[] row = Neumaticos.get(indexRow);
            for(int indexColumn = 0; indexColumn < cabecera.length; indexColumn++){
                pdfPCell = new PdfPCell(new Phrase(row[indexColumn],fTexto));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setFixedHeight(55);
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

    public void AppPDF(Activity activity) {
        if(pdfFile.exists()){
            Uri contentUri = FileProvider.getUriForFile(context, "xxx.fileprovider", pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setDataAndType(contentUri,"application/pdf");
            try {
                activity.startActivity(intent);
            }catch (ActivityNotFoundException e){
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.adobe.reader")));
                Toast.makeText(activity.getApplicationContext(),"No cuentas con la aplicacion para visualizar PDF",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(activity.getApplicationContext(),"Archivo no encontrado",Toast.LENGTH_SHORT).show();
        }
    }

}
