package com.example.servimaq.op_catalogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista;
import com.example.servimaq.menu_opciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Catalogo extends AppCompatActivity {

    SearchView svBusqueda;
    ListView lvListaProductos;
    ArrayList<items_lista> lista = new ArrayList<>();
    producto_catalogo prod_catalogo;
    String cadena_texto_buscar = null, tipo_busqueda = "codigo", campo_busqueda = null;
    Spinner spTipoBusqueda;
    ArrayList<String> tipos = new ArrayList<>();
    boolean estado = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            finish();
            Intent intent = new Intent(Catalogo.this, menu_opciones.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        //QUITAR ANIMACION DE CARGA DE VISTA*****************************
        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);

        //HABILITAR BOTON - ATRAS - EN LA BARRA DE NAVEGACION************
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        svBusqueda = findViewById(R.id.svBusqueda);
        spTipoBusqueda = findViewById(R.id.spTipoBusqueda);
        lvListaProductos = findViewById(R.id.lvListaProductos);

        //CAMBIAR COLOR DE TEXTO DEL BUSCADOR-----------------------
        int id = svBusqueda.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) svBusqueda.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);


        //ELEGIR TIPO DE BUSQUEDA--------------------------------------------------------------------------------------------
        tipos.add(0,"Seleccionar busqueda por...");
        tipos.add("codigo");
        tipos.add("marca");


        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, tipos);
        adapter.setDropDownViewResource(R.layout.design_spinner);
        spTipoBusqueda.setAdapter(adapter);

        spTipoBusqueda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);

                tipo_busqueda = tipos.get(i);

                if(tipo_busqueda.equalsIgnoreCase("Seleccionar busqueda por...")){
                    campo_busqueda = "LlantaId";
                    svBusqueda.setQueryHint("ingresar codigo...");
                }else if(tipo_busqueda.equalsIgnoreCase("codigo")){
                    campo_busqueda = "LlantaId";
                    svBusqueda.setQueryHint("ingresar codigo...");
                }else if(tipo_busqueda.equalsIgnoreCase("marca")){
                    campo_busqueda = "NombreMarca";
                    svBusqueda.setQueryHint("ingresar marca...");
                }
                //Toast.makeText(getApplicationContext(),tipo_busqueda,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //INICIAR CONEXION A LA DB--------------------------
        SQLConexion db = new SQLConexion();



        //SELECT DE BUSQUEDA INICIAL - CATALOGO *****************************************************************************************************************************
        //CONDICION PARA MOSTRAR DATOS-------------------------------------------------------------------------------------------------------
        if(cadena_texto_buscar==null){

            AndroidNetworking.initialize(getApplicationContext());
            AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/Catalogo_POST_BUSQUEDA_INICIAL.php")
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

                                        lista.add(new items_lista(object.getString("llantaid"),object.getString("nombremarca"),object.getInt("indicecarga"),
                                                object.getString("indicevelocidad"),object.getString("construccion"),object.getString("clasificacion"),
                                                object.getString("fechafabricacion"),object.getString("fotollanta"),object.getString("fotovehiculo"),
                                                object.getString("marcavehiculo"),object.getString("modelovehiculo"),object.getInt("ancho"),
                                                object.getInt("diametro"),object.getInt("perfil"),Double.parseDouble(object.getString("mmcocada")),
                                                object.getInt("stock"),object.getString("presionmaxima"),object.getString("precio"),
                                                object.getString("tipovehiculo"),object.getString("detallellantaid"),object.getString("vehiculoid"),
                                                object.getString("medidallantaid")));
                                        contar++;
                                    } while (contar <= array.length());

                                }
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            try {
                                prod_catalogo = new producto_catalogo(Catalogo.this,lista);
                                lvListaProductos.setAdapter(prod_catalogo);
                            }catch (Exception e){

                            }

                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(getApplicationContext(),"Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

        }else{ //Uso del buscador---------------------------------------------
            try {
                prod_catalogo = new producto_catalogo(Catalogo.this,lista);
                lvListaProductos.setAdapter(prod_catalogo);
            }catch (Exception e){

            }
        }


        //EXPANDIR CUADRO DE BUSQUEDA***********************************************************
        svBusqueda.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spTipoBusqueda.setVisibility(View.GONE);
            }
        });

        //CERRAR CUADRO DE BUSQUEDA*************************************************************
        svBusqueda.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                spTipoBusqueda.setVisibility(View.VISIBLE);
                return false;
            }
        });

        //BUSCAR POR INGRESO DE TEXTO
        svBusqueda.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String texto_buscar) {
                cadena_texto_buscar = texto_buscar;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String texto_buscar) {

                cadena_texto_buscar = texto_buscar;
                Toast.makeText(getApplicationContext(),texto_buscar,Toast.LENGTH_SHORT).show();

                AndroidNetworking.initialize(getApplicationContext());
                Map<String,String> insertar = new HashMap<>();
                insertar.put("Condicion",campo_busqueda);
                insertar.put("Busqueda","%"+cadena_texto_buscar+"%");

                JSONObject datosJSON = new JSONObject(insertar);

                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/Catalogo_POST_BUSQUEDA_WHERE.php")
                        .addJSONObjectBody(datosJSON)
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                lvListaProductos.setAdapter(null);
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

                                            lista.add(new items_lista(object.getString("llantaid"),object.getString("nombremarca"),object.getInt("indicecarga"),
                                                    object.getString("indicevelocidad"),object.getString("construccion"),object.getString("clasificacion"),
                                                    object.getString("fechafabricacion"),object.getString("fotollanta"),object.getString("fotovehiculo"),
                                                    object.getString("marcavehiculo"),object.getString("modelovehiculo"),object.getInt("ancho"),
                                                    object.getInt("diametro"),object.getInt("perfil"),Double.parseDouble(object.getString("mmcocada")),
                                                    object.getInt("stock"),object.getString("presionmaxima"),object.getString("precio"),
                                                    object.getString("tipovehiculo"),object.getString("detallellantaid"),object.getString("vehiculoid"),
                                                    object.getString("medidallantaid")));
                                            contar++;
                                        } while (contar <= array.length());

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                                try {
                                    prod_catalogo = new producto_catalogo(Catalogo.this,lista);
                                    lvListaProductos.setAdapter(prod_catalogo);
                                }catch (Exception e){

                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(),"Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });


                try {
                    prod_catalogo = new producto_catalogo(Catalogo.this,lista);
                    lvListaProductos.setAdapter(prod_catalogo);
                }catch (Exception e){

                }

                return true;
            }

        });


    }
}