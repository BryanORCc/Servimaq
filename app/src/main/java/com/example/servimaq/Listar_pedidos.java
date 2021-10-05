package com.example.servimaq;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.pedido_lista;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Listar_pedidos extends AppCompatActivity {
    TextView tvcodigo,tvNombre, tvApellido, tvCorreo, tvFechaActual, tvFechaEntrega, tvPModoPago, tvDni;
    SearchView svBusquedaPedidos;
    ListView lvListaPedidos;
    ArrayList<pedido_lista> lista = new ArrayList<>();;
    Pedido_Catalogo pedi_catalogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pedidos);


        svBusquedaPedidos = findViewById(R.id.svBusquedaPedidos);

        lvListaPedidos = findViewById(R.id.lvListaPedidos);
        tvcodigo=findViewById(R.id.tvCodigo);
        tvNombre = findViewById(R.id.tvNombre);
        tvApellido = findViewById(R.id.tvApellidos);
        tvCorreo = findViewById(R.id.tvDCorreo);
        tvFechaActual = findViewById(R.id.tvFechaA);
        tvFechaEntrega = findViewById(R.id.tvFechaE);
        tvPModoPago = findViewById(R.id.tvModoP);
        tvDni = findViewById(R.id.tvDni);

        SQLConexion db = new SQLConexion();


        try {
            Statement st = db.ConexionDB(getApplicationContext()).createStatement();
            ResultSet rs = st.executeQuery("select codPedido,NombresCliente,ApellidosCliente,Correo,FechaActual,FechaEntrega,ModoPago,DNI from T_Pedido;");

            if (!rs.next()) {
                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }else {
                do {
                    lista.add(new pedido_lista(rs.getString(1),rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8)));
                } while (rs.next());///va agregando cada ID

            }
            rs.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        pedi_catalogo = new Pedido_Catalogo(getApplicationContext(),lista);
        lvListaPedidos.setAdapter(pedi_catalogo);



    }
}