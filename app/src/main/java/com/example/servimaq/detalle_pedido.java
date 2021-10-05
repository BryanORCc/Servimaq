package com.example.servimaq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.db.SQLConexion;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class detalle_pedido extends AppCompatActivity {
    Context c;
    EditText nombrecliente,apellidocliente,correo,dni,FechaActual,FechadePago;
    Button  btnModificarP, btnCancelarP, btnConfirmarP;
    Spinner spinner1;
    ArrayList<String> opciones = new ArrayList<>();
    ArrayList<String> datosDePago = new ArrayList<>();
    Spinner codigoPedido;
    String pedidoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);


        nombrecliente=findViewById(R.id.EtNombreCliente);
        apellidocliente=findViewById(R.id.EtApellidoCliente);
        correo=findViewById(R.id.EtCorreo);
        FechaActual=findViewById(R.id.EtFechaActual);
        FechadePago=findViewById(R.id.EtFechaEntrega);
        dni=findViewById(R.id.EtDni);
        btnModificarP=findViewById(R.id.btnModificarP);
        btnCancelarP=findViewById(R.id.btnCancelarP);
        btnConfirmarP=findViewById(R.id.btnConfirmarP);
        spinner1=findViewById(R.id.Spi_Modo_Pago);
        codigoPedido=findViewById(R.id.Spi_codigoPedido);

        opciones.add("opcion1");
        opciones.add("opcion2");
        opciones.add("opcion3");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, opciones);
        spinner1.setAdapter(adapter);

        Intent datos = getIntent();

        pedidoId = datos.getStringExtra("codigo");
        Log.e("id",pedidoId+"cual");

        ///carga de Datos inicial
        try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
            Log.e("id1","-********"+st);
            ResultSet rs = st.executeQuery("select * from T_Pedido where codPedido ='"+pedidoId+"';");
            Log.e("id2","-********"+rs);
            if (!rs.next()) {
                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }
            else {
                do {
                    Log.e("id3",""+rs.getString(2));
                    nombrecliente.setText(rs.getString(2));
                    apellidocliente.setText(rs.getString(3));
                    correo.setText(rs.getString(4));
                    FechaActual.setText(rs.getString(5));
                    FechadePago.setText(rs.getString(6));
                    dni.setText(""+rs.getInt(8));
                } while (rs.next());

            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),"errror:",Toast.LENGTH_SHORT).show();
        }


//////////////cargando los ID en un array/////
        try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
            Log.e("id-4","-********"+st);
            ResultSet rs = st.executeQuery("select codPedido from T_Pedido");//llena con codigos de la tabla
            Log.e("id-4","-********"+rs);
            if (!rs.next()) {
                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }
            else {
                do {
                    //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                    datosDePago.add(rs.getString(1));
                    Log.e("id5",rs.getString(1));
                } while (rs.next());///va agregando cada ID
                ArrayAdapter adaptador = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, datosDePago);
                codigoPedido.setAdapter(adaptador);
                int x = -1;
                do{
                    x++;
                }while (datosDePago.get(x).equalsIgnoreCase(pedidoId));
                codigoPedido.setSelection(x);
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        //SELECCION DE OPCION - SPINNER DETALLE :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        codigoPedido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pedidoId=datosDePago.get(i);

                //--CARGAR DATOS AL SPINNER DE PEDIDOS------------------------------------------------------------------------------
                try {
                    SQLConexion conexion =new SQLConexion();
                    Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
                    ResultSet rs = st.executeQuery("select * from T_Pedido where codPedido = '"+ pedidoId +"';");
                    if (!rs.next()) {
                        Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        do {
                            nombrecliente.setText(rs.getString(2));
                            apellidocliente.setText(rs.getString(3));
                            correo.setText(rs.getString(4));
                            FechaActual.setText(rs.getString(5));
                            FechadePago.setText(rs.getString(6));
                            dni.setText(""+rs.getInt(8));
                        } while (rs.next());///
                    }
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}