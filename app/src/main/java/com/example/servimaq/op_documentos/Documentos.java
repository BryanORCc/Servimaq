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

import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.TemplatePDF;
import com.example.servimaq.db.items_lista;
import com.example.servimaq.menu_opciones;
import com.example.servimaq.op_catalogo.Catalogo;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

        //HABILITAR BOTON - ATRAS - EN LA BARRA DE NAVEGACION************
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //SELECCIONAR CODIGO CON PRODUCTOS DE SALIDA**************************************************
        SQLConexion db = new SQLConexion();
        try {
            Statement st = db.ConexionDB(getApplicationContext()).createStatement();
            ResultSet rs = st.executeQuery("select distinct(P.codPedido) from T_Listado L inner join T_Pedido P on L.codPedido = P.codPedido;");

            if (!rs.next()) {
                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }else {
                do {
                    listaCodigoPedido.add(rs.getString(1));
                } while (rs.next());///va agregando cada ID
            }
            rs.close();

            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,listaCodigoPedido);
            spncodPedido.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }//FIN SELECT--------------------------------------------------------------------------


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
                try {
                    Statement st = db.ConexionDB(getApplicationContext()).createStatement();
                    ResultSet rs = st.executeQuery("select LS.ItemId, LL.LlantaId, LS.Cantidad, DL.NombreMarca+' - '+V.TipoVehiculo+' - IC:'+CONVERT(varchar(15)," +
                            "DL.IndiceCarga)+' - IV:'+DL.IndiceVelocidad+' - '+DL.Construccion+' - PM:'+CONVERT(varchar(15),DL.PresionMaxima)+' - CLASI:'+DL.Clasificacion" +
                            "+' - ANC:'+CONVERT(varchar(5),ML.Ancho)+' - DIAM:'+CONVERT(varchar(5),ML.Diametro)+' - PERF:'+CONVERT(varchar(5),ML.Perfil)" +
                            "+' - MmCc:'+CONVERT(varchar(5),ML.MmCocada)+' - Compatible con '+V.MarcaVehiculo+' '+V.ModeloVehiculo as 'Descripcion', LS.Precio, LS.Total, " +
                            "P.NombresCliente+' '+P.ApellidosCliente, P.FechaActual, P.FechaEntrega, P.DNI, P.Correo from T_Listado LS inner join T_Llanta LL on LS.LlantaId = LL.LlantaId " +
                            "inner join T_DetalleLlanta DL on DL.DetalleLlantaId = LL.DetalleLlantaId inner join T_Vehiculo V on V.VehiculoId = LL.VehiculoId " +
                            "inner join T_MedidaLlanta ML on ML.MedidaLlantaId = DL.MedidaLlantaId inner join T_Pedido P on P.codPedido = LS.codPedido where LS.codPedido = '"+codPedido+"';");

                    if (!rs.next()) {
                        Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                    }else {
                        do {
                            ItemId = rs.getString(1);
                            LlantaId = rs.getString(2);
                            Cantidad = ""+rs.getInt(3);
                            Descripcion = rs.getString(4);
                            Precio = ""+rs.getDouble(5);
                            Total = ""+rs.getDouble(6);
                            setNeumaticos(ItemId,LlantaId,Cantidad, Descripcion, Precio, Total);

                            NombresCliente = rs.getString(7);
                            FechaActual = rs.getString(8);
                            FechaEntrega = rs.getString(9);
                            DNI = ""+rs.getInt(10);
                            Correo = rs.getString(11);
                        } while (rs.next());///va agregando cada ID
                    }
                    rs.close();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }//FIN SELECT--------------------------------------------------------------------------


                templatePDF.AddTituloEmpresa("SERVIMAQ S.A.C.", "GUIA DE REMISION","CODIGO DE PEDIDO: ",codPedido ,"20455986835");
                templatePDF.AddTitles("Av. Mariscal castilla 1006 - Mariano Melgar","Perú - Arequipa",FechaActual);
                templatePDF.AddDatosCliente(NombresCliente,Correo,DNI,FechaEntrega);
                /*templatePDF.AddParagraph("Hola");
                templatePDF.AddParagraph("descripcion de la guia de remision");*/
                templatePDF.CreateTable(cabecera,getNeumaticos());
                templatePDF.CloseDocument();
                rows.clear();
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