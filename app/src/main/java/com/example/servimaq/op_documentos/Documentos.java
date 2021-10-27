package com.example.servimaq.op_documentos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.servimaq.R;
import com.example.servimaq.db.TemplatePDF;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Documentos extends AppCompatActivity {

    Spinner spncodPedido;
    Button btnGenerarPdf;

    private TemplatePDF templatePDF;

    private String[] cabecera = {"Item","Codigo","Cant","Descripcion","P.U.","Total"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentos);

        spncodPedido = findViewById(R.id.spncodPedido);
        btnGenerarPdf = findViewById(R.id.btnGenerarPdf);

        templatePDF = new TemplatePDF(getApplicationContext());
        templatePDF.OpenDocument();
        templatePDF.AddMetaData("Almacen","Salida de Neumaticos", "Servimaq S.A.C.");
        templatePDF.AddTituloEmpresa("SERVIMAQ S.A.C.", "GUIA DE REMISION","CODIGO DE PEDIDO: ","COD-001" ,"20455986835");
        templatePDF.AddTitles("Av. Mariscal castilla 1006 - Mariano Melgar","Perú - Arequipa","12/12/2021");
        templatePDF.AddDatosCliente("Juan PErez","juan_perez@gmail.com","15/10/2022");
        /*templatePDF.AddParagraph("Hola");
        templatePDF.AddParagraph("descripcion de la guia de remision");*/
        templatePDF.CreateTable(cabecera,getNeumaticos());
        templatePDF.CloseDocument();

        btnGenerarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                templatePDF.MirarPDF();
            }
        });

    }

    private ArrayList<String[]> getNeumaticos(){
        ArrayList<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"SQsdds","1","10","Descripcion de los neumaticos","200.0","2000.0"});
        return rows;
    }

}