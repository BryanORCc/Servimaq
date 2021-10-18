package com.example.servimaq.fragments_registros;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista_salida_P;
import com.example.servimaq.lista_salida_producto;
import com.example.servimaq.op_catalogo.Catalogo;
import com.example.servimaq.op_catalogo.producto_catalogo;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Salida_Prod extends AppCompatActivity {


    ArrayList<items_lista_salida_P>lista =new ArrayList<>();

    lista_salida_producto lsp;

    ListView lvSalidaProducto;

    String  op_codPedido;
    TextView tvnombreyapellido,tvcorreo,tvmodopago,tvDNI,tvfechaentrega,tvdivision;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_salidaa_);

        lvSalidaProducto = findViewById(R.id.lvSalidaProducto);
        tvdivision = findViewById(R.id.tvdivision);
        tvnombreyapellido = findViewById(R.id.tvnombreyapellido);
        tvcorreo = findViewById(R.id.tvcorreo);
        tvmodopago =findViewById(R.id.tvmodopago);
        tvDNI = findViewById(R.id.tvDNI);
        tvfechaentrega = findViewById(R.id.tvfechaentrega);


        Intent recibir_codigo = getIntent();

        op_codPedido=recibir_codigo.getStringExtra("op_codPedido");


        try {
            SQLConexion db = new SQLConexion();
            Statement st = db.ConexionDB(getApplicationContext()).createStatement();

            Log.e("error",""+st);

            ResultSet rs = st.executeQuery("select codPedido,NombresCliente,ApellidosCliente,Correo,FechaEntrega,ModoPago,DNI from T_Pedido;");
           // select codPedido,NombresCliente,ApellidosCliente,P.Correo,FechaEntrega,ModoPago,DNI from T_Pedido
            Log.e("error",""+rs.getString(1));


            if (!rs.next()) {

                Toast.makeText(getApplicationContext(), "No se encontraron registros", Toast.LENGTH_SHORT).show();
            } else {
                do {
                    lista.add(new items_lista_salida_P(rs.getString(1),rs.getString(2)+rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));

                    Log.e("error",""+lista.size());


                } while (rs.next());
            }

            //rs.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


      lsp= new lista_salida_producto(Salida_Prod.this,lista);
      lvSalidaProducto.setAdapter(lsp);

    }


}








