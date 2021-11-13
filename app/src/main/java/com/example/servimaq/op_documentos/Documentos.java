package com.example.servimaq.op_documentos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.TemplatePDF;
import com.example.servimaq.db.items_lista;
import com.example.servimaq.menu_opciones;
import com.example.servimaq.op_catalogo.Catalogo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Documentos extends AppCompatActivity {

    Spinner spncodPedido;
    ArrayList<String> listaCodigoPedido = new ArrayList<>();
    String codPedido;
    Button btnGenerarPdf, btnAbrirPdf;

    String NombresCliente, FechaActual, FechaEntrega, DNI, Correo;
    String ItemId, LlantaId, Cantidad, Descripcion, Precio, Total;

    private TemplatePDF templatePDF;

    private String[] cabecera = {"Item","Codigo","Cant","Descripcion","P.U.","Total"};
    ArrayList<String[]> rows = new ArrayList<>();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            finish();
            Intent intent = new Intent(Documentos.this, menu_opciones.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(Documentos.this, menu_opciones.class);
        startActivity(intent);
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentos);

        spncodPedido = findViewById(R.id.spncodPedido);
        btnGenerarPdf = findViewById(R.id.btnGenerarPdf);
        btnAbrirPdf = findViewById(R.id.btnAbrirPdf);

        //INICIALIZAR CONEXION CON EL SERVICIO WEB*************************
        AndroidNetworking.initialize(getApplicationContext());

        //HABILITAR BOTON - ATRAS - EN LA BARRA DE NAVEGACION************
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //SELECCIONAR CODIGO CON PRODUCTOS DE SALIDA**************************************************
        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/Documentos_POST_SELECT_INNER.php")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String validarDatos = response.getString("data");
                            int contar = 1;

                            if (validarDatos.equals("[]")) {
                                Toast.makeText(getApplicationContext(), "No se encontraron pedidos de salida de neumaticos", Toast.LENGTH_SHORT).show();
                                btnGenerarPdf.setClickable(false);
                                btnAbrirPdf.setClickable(false);
                            } else {
                                btnGenerarPdf.setClickable(true);
                                btnAbrirPdf.setClickable(true);
                                JSONArray array = response.getJSONArray("data");
                                do {
                                    JSONObject object = array.getJSONObject(contar - 1);
                                    listaCodigoPedido.add(object.getString("codpedido"));
                                    contar++;
                                } while (contar <= array.length());

                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        try {
                            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,listaCodigoPedido);
                            spncodPedido.setAdapter(adapter);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }

                });//FIN SELECT--------------------------------------------------------------------------



        //SELECCION DE OPCION EN SPINNER*******************************************************
        spncodPedido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.parseColor("#835E5A5A"));

                codPedido = listaCodigoPedido.get(position);
                Log.e("Codigo: ","__ "+codPedido);

                //CREAR PDF_______________________________________________________________________________________________________
                templatePDF = new TemplatePDF(getApplicationContext());
                templatePDF.OpenDocument();
                templatePDF.AddMetaData("Almacen","Salida de Neumaticos", "Servimaq S.A.C."); //-----------------


                //SELECCIONAR INFORMACION PARA EL PDF**************************************************
                Map<String,String> insertar = new HashMap<>();
                insertar.put("codPedido",codPedido);
                JSONObject datosJSON = new JSONObject(insertar);

                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/Documenos_POST_SELECT_ALL_WHERE.php")
                        .addJSONObjectBody(datosJSON)
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String validarDatos = response.getString("data");
                                    int contar = 1;

                                    if (validarDatos.equals("[]")) {
                                        Toast.makeText(getApplicationContext(), "No se encontraron productos", Toast.LENGTH_SHORT).show();
                                        btnGenerarPdf.setClickable(false);
                                        btnAbrirPdf.setClickable(false);
                                    } else {
                                        btnGenerarPdf.setClickable(true);
                                        btnAbrirPdf.setClickable(true);
                                        JSONArray array = response.getJSONArray("data");
                                        do {
                                            JSONObject object = array.getJSONObject(contar - 1);

                                            ItemId = object.getString("itemid");
                                            LlantaId = object.getString("llantaid");
                                            Cantidad = ""+object.getInt("cantidad");
                                            Descripcion = object.getString("descripcion");
                                            Precio = object.getString("precio");
                                            Total = object.getString("total");
                                            setNeumaticos(ItemId,LlantaId,Cantidad, Descripcion, Precio, Total);

                                            NombresCliente = object.getString("nya");
                                            FechaActual = object.getString("fechaactual");
                                            FechaEntrega = object.getString("fechaentrega");
                                            DNI = ""+object.getString("dni");
                                            Correo = object.getString("correo");
                                            Log.e("NOMBRE:: ",Cantidad);
                                            contar++;
                                        } while (contar <= array.length());

                                        templatePDF.AddTituloEmpresa("SERVIMAQ S.A.C.", "GUIA DE REMISION","CODIGO DE PEDIDO: ",codPedido ,"20455986835");
                                        templatePDF.AddTitles("Av. Mariscal castilla 1006 - Mariano Melgar","Perú - Arequipa",FechaActual);
                                        templatePDF.AddDatosCliente(NombresCliente,Correo,DNI,FechaEntrega);
                                        templatePDF.CreateTable(cabecera,getNeumaticos());
                                        templatePDF.CloseDocument();
                                        rows.clear();

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(),"ACA"+ e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {

                            }

                        });//FIN SELECT--------------------------------------------------------------------------
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });//FIN--------------------------------------------------------------------------


        btnGenerarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                templatePDF.MirarPDF();
            }
        });

        btnAbrirPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                templatePDF.AppPDF(Documentos.this);
            }
        });
    }

    private void setNeumaticos(String ItemId,String LlantaId,String Cantidad,String Descripcion,String Precio,String Total){
        rows.add(new String[]{ItemId,LlantaId,Cantidad,Descripcion,Precio,Total});
    }

    private ArrayList<String[]> getNeumaticos(){
        return rows;
    }

}