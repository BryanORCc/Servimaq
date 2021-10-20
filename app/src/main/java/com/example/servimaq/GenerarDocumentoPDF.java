package com.example.servimaq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class GenerarDocumentoPDF extends AppCompatActivity {

    private PDFView pdfVistaPrevia;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_documento_pdf);

        pdfVistaPrevia = findViewById(R.id.pdfVistaPrevia);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            file = new File(bundle.getString("ruta",""));
        }
        pdfVistaPrevia.fromFile(file).enableSwipe(true).swipeHorizontal(false).enableDoubletap(true).enableAntialiasing(true).load();

    }


    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RESULT_OK) {

        }
    }*/
}