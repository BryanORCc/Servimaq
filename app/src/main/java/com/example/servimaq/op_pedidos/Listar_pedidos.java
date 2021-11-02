package com.example.servimaq.op_pedidos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;

import com.example.servimaq.db.items_lista;
import com.example.servimaq.db.pedido_lista;
import com.example.servimaq.menu_opciones;
import com.example.servimaq.op_catalogo.Catalogo;
import com.example.servimaq.op_catalogo.producto_catalogo;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        //CAMBIAR COLOR DE TEXTO DEL BUSCADOR-----------------------
        int id = svBusquedaPedidos.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) svBusquedaPedidos.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);


        //CARGA INICIAL DE DATOS/********************************************--------------------------------::::::::::::::::::::::::::::::::::
        SQLConexion db = new SQLConexion();

        if(cadena_texto_buscar==null) {
            AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Pedido_POST_SELECT_ALL.php")
                    .setPriority(Priority.IMMEDIATE)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String validarDatos = response.getString("data");
                                int contar = 1;

                                if (validarDatos.equals("[]")) {
                                    Toast.makeText(getApplicationContext(), "No se encontraron registros", Toast.LENGTH_SHORT).show();
                                } else {

                                    JSONArray array = response.getJSONArray("data");
                                    do {
                                        JSONObject object = array.getJSONObject(contar - 1);
                                        lista.add(new pedido_lista(object.getString("codpedido"), object.getString("nombrescliente"),
                                                object.getString("apellidoscliente"), object.getString("correo"), object.getString("fechaactual"),
                                                object.getString("fechaentrega"), object.getString("modopago"), object.getInt("dni")));
                                        contar++;
                                    } while (contar <= array.length());

                                }
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            try {
                                pedi_catalogo = new Pedido_Catalogo(Listar_pedidos.this, lista);
                                lvListaPedidos.setAdapter(pedi_catalogo);
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });//FIN SELECT - HEROKU ---------------------


        } else { //Uso del buscador---------------------------------------------
            try {
                pedi_catalogo = new Pedido_Catalogo(Listar_pedidos.this, lista);
                lvListaPedidos.setAdapter(pedi_catalogo);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
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
                cadena_texto_buscar = texto_buscar;
                Toast.makeText(getApplicationContext(),texto_buscar,Toast.LENGTH_SHORT).show();

                Map<String,String> insertar = new HashMap<>();
                insertar.put("Busqueda","%"+cadena_texto_buscar+"%");
                JSONObject datosJSON = new JSONObject(insertar);

                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/Pedidos_POST_BUSQUEDA_WHERE.php")
                        .addJSONObjectBody(datosJSON)
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                lvListaPedidos.setAdapter(null);
                                lista.clear();

                                try {
                                    String validarDatos = response.getString("data");
                                    int contar = 1;

                                    if (validarDatos.equals("[]")) {
                                        Toast.makeText(getApplicationContext(), "No se encontraron productos", Toast.LENGTH_SHORT).show();
                                    } else {

                                        JSONArray array = response.getJSONArray("data");
                                        do {
                                            JSONObject object = array.getJSONObject(contar - 1);
                                            lista.add(new pedido_lista(object.getString("codpedido"), object.getString("nombrescliente"),
                                                    object.getString("apellidoscliente"), object.getString("correo"), object.getString("fechaactual"),
                                                    object.getString("fechaentrega"), object.getString("modopago"), object.getInt("dni")));
                                            contar++;
                                        } while (contar <= array.length());

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                                try {
                                    pedi_catalogo = new Pedido_Catalogo(Listar_pedidos.this, lista);
                                    lvListaPedidos.setAdapter(pedi_catalogo);
                                }catch (Exception e){

                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(),"Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });//FIN SELECT BUSQUEDA - HEROKU ------------------------

                return true;
            }

        });//FIN OPCION BUSQUEDA -------------------------------------------
    }


}