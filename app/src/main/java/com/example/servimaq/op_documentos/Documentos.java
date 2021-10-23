package com.example.servimaq.op_documentos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.example.servimaq.R;
import com.example.servimaq.db.TemplatePDF;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Documentos extends AppCompatActivity {

    Spinner spncodPedido;
    Button btnGenerarPdf;

    private TemplatePDF templatePDF;

    private String[] cabecera = {"Codigo","Cantidad","Descripcion","Precio Unitario","Total"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentos);

        spncodPedido = findViewById(R.id.spncodPedido);
        btnGenerarPdf = findViewById(R.id.btnGenerarPdf);

        /*if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            return;
        }*/

        templatePDF = new TemplatePDF(getApplicationContext());
        templatePDF.OpenDocument();
        templatePDF.AddMetaData("Almacen","Salida de Neumaticos", "Servimaq S.A.C.");
        templatePDF.AddTitles("GUIA DE REMISION", "Clientes", Calendar.getInstance().getTime().toString());
        templatePDF.AddParagraph("Hola");
        templatePDF.AddParagraph("descripcion de la guia de remision");
        templatePDF.CreateTable(cabecera,getClientes());
        templatePDF.CloseDocument();

        btnGenerarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                templatePDF.MirarPDF();
            }
        });

    }

    private ArrayList<String[]> getClientes(){
        ArrayList<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"1","10","Descripcion de los neumaticos","200.0","2000.0"});
        return rows;
    }

    /*public void Generar(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent,1);
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RESULT_OK){
            templatePDF = new TemplatePDF(getApplicationContext());
            templatePDF.OpenDocument();
            templatePDF.AddMetaData("Almacen","Salida de Neumaticos", "Servimaq S.A.C.");
            templatePDF.AddTitles("GUIA DE REMISION", "Clientes", Calendar.getInstance().getTime().toString());
            templatePDF.AddParagraph("Hola");
            templatePDF.AddParagraph("descripcion de la guia de remision");
            templatePDF.CreateTable(cabecera,getClientes());
            templatePDF.CloseDocument();
        }
    }*/


}