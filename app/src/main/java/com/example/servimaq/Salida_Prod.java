package com.example.servimaq;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista_salida_P;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Salida_Prod extends AppCompatActivity {

    ArrayList<items_lista_salida_P>lista =new ArrayList<>();
    lista_salida_producto ilsp;
    ListView lvSalidaProducto;
    int validar = 0;

    String  op_codPedido;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida_prod);

        lvSalidaProducto = findViewById(R.id.lvSalidaProducto);

        //Intent recibir_codigo = getIntent();
        //op_codPedido=recibir_codigo.getStringExtra("op_codPedido");

        SQLConexion conexion = new SQLConexion();
        try{
            Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
            ResultSet rs = st.executeQuery("select * from T_Pedido;");
            rs.next();
            if(rs.getRow()>=1){
                validar = rs.getRow();
            }else{
                validar = 0;
            }

           // select codPedido,NombresCliente,ApellidosCliente,P.Correo,FechaEntrega,ModoPago,DNI from T_Pedido

            Log.e("valorrr --- ",""+validar);

            lista.add(new items_lista_salida_P(rs.getString(1),rs.getString(2)+ " " +rs.getString(3),rs.getString(4),
                    rs.getString(5),rs.getString(6),rs.getString(7)));
            if (rs.next()) {
                do {
                    lista.add(new items_lista_salida_P(rs.getString(1),rs.getString(2)+ " " +rs.getString(3),rs.getString(4),
                            rs.getString(5),rs.getString(6),rs.getString(7)));
                }while (rs.next());
            }
            else {
                if(validar == 0){
                    Toast.makeText(getApplicationContext(), "No se encontraron pedidos", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No se encontraron registros", Toast.LENGTH_SHORT).show();
        }

        ilsp = new lista_salida_producto(Salida_Prod.this,lista);
        lvSalidaProducto.setAdapter(ilsp);

    }


}








