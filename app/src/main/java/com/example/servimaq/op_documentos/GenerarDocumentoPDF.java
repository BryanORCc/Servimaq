package com.example.servimaq.op_documentos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;


import com.example.servimaq.R;
import com.example.servimaq.db.TemplatePDF;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;


public class GenerarDocumentoPDF extends AppCompatActivity {

    private PDFView pdfVistaPrevia;
    private File file;

    private TemplatePDF templatePDF;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            finish();
            Intent intent = new Intent(GenerarDocumentoPDF.this, Documentos.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(GenerarDocumentoPDF.this, Documentos.class);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_documento_pdf);

        pdfVistaPrevia = findViewById(R.id.pdfVistaPrevia);

        //HABILITAR BOTON - ATRAS - EN LA BARRA DE NAVEGACION************
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            file = new File(bundle.getString("ruta",""));
        }
        pdfVistaPrevia.fromFile(file).enableSwipe(true).swipeHorizontal(false).enableDoubletap(true).enableAntialiasing(true).load();

    }

}