package com.example.servimaq.op_pedidos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;

import com.example.servimaq.db.pedido_lista;
import com.example.servimaq.menu_opciones;
import com.example.servimaq.op_catalogo.Catalogo;


import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Listar_pedidos extends AppCompatActivity {
    TextView tvcodigo,tvNombre, tvApellido, tvCorreo, tvFechaActual, tvFechaEntrega, tvPModoPago, tvDni;
    SearchView svBusquedaPedidos;
    ListView lvListaPedidos;
    ArrayList<pedido_lista> lista = new ArrayList<>();
    Pedido_Catalogo pedi_catalogo;
    String cadena_texto_buscar = null;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            finish();
            Intent intent = new Intent(Listar_pedidos.this, menu_opciones.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pedidos);

        //BOTON "ATRAS" EN LA BARRA DE NAVEGACION****************
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //QUITAR ANIMACION DE CARGA DE VISTA*****************************
        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);

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

        if(cadena_texto_buscar==null) {
            try {
                Statement st = db.ConexionDB(getApplicationContext()).createStatement();
                ResultSet rs = st.executeQuery("select codPedido,NombresCliente,ApellidosCliente,Correo,FechaActual,FechaEntrega,ModoPago,DNI from T_Pedido;");
                if (!rs.next()) {
                    Toast.makeText(getApplicationContext(), "No se encontraron registros", Toast.LENGTH_SHORT).show();
                } else {
                    do {
                        lista.add(new pedido_lista(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8)));
                    } while (rs.next());///va agregando cada ID

                }
                rs.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            pedi_catalogo = new Pedido_Catalogo(Listar_pedidos.this, lista);
            lvListaPedidos.setAdapter(pedi_catalogo);
        }
        else
        { //Uso del buscador---------------------------------------------
            pedi_catalogo = new Pedido_Catalogo(Listar_pedidos.this, lista);
            lvListaPedidos.setAdapter(pedi_catalogo);
        }

        //BUSCAR POR INGRESO DE TEXTO
        svBusquedaPedidos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String texto_buscar) {
                cadena_texto_buscar = texto_buscar;
                return true;
            }
            @Override
            public boolean onQueryTextChange(String texto_buscar) {
                lvListaPedidos.setAdapter(null);
                lista.clear();
                cadena_texto_buscar = texto_buscar;
                Toast.makeText(getApplicationContext(),texto_buscar,Toast.LENGTH_SHORT).show();
                //--SELECT INFORMACION PEDIDO------------------------------------------------------------------------------
                try {
                    Statement st = db.ConexionDB(getApplicationContext()).createStatement();
                    ResultSet rs = st.executeQuery("select codPedido,NombresCliente,ApellidosCliente,Correo,FechaActual,FechaEntrega,ModoPago,DNI from T_Pedido"+
                            " where codPedido like '%"+cadena_texto_buscar+"%';");

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
                }//FIN SELECT-------------
                pedi_catalogo = new Pedido_Catalogo(Listar_pedidos.this, lista);
                lvListaPedidos.setAdapter(pedi_catalogo);
                return true;
            }
        });
    }
}