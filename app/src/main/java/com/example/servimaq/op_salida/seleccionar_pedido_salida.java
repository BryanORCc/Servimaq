package com.example.servimaq.op_salida;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.R;
import com.example.servimaq.db.Items_Salida_set_get;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista_salida_P;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class seleccionar_pedido_salida extends AppCompatActivity {

    ListView lvitems;
    TextView tvcodPedido;
    ArrayList<Items_Salida_set_get> salida = new ArrayList<>();
    Items_Salida i_salida;
    String codPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_pedido_salida);

        //HABILITAR BOTON - ATRAS - EN LA BARRA DE NAVEGACION************
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);

        lvitems = findViewById(R.id.lvitems);
        tvcodPedido = findViewById(R.id.tvcodPedido);

        Intent recibir_codigo = getIntent();
        codPedido = recibir_codigo.getStringExtra("codPedido");

        tvcodPedido.setText(codPedido);

        //INICIAR CONEXION CON EL SERVICIO WEB - HEROKU
        AndroidNetworking.initialize(getApplicationContext());

        //--**ACTUALIZAR DATOS DE LA TABLA MEDIDA------------------------------------------------------------------------------::::
        Map<String,String> insertar = new HashMap<>();
        insertar.put("codPedido",codPedido);
        JSONObject datosJSON = new JSONObject(insertar);

        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/Salida_POST_SELECT_WHERE.php")
                .addJSONObjectBody(datosJSON)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String validarDatos = response.getString("data");
                            int contar= 1;
                            Log.e("respuesta: ",""+validarDatos);

                            if (!validarDatos.equals("[]")) {
                                JSONArray array = response.getJSONArray("data");
                                do {
                                    JSONObject object = array.getJSONObject(contar-1);
                                    salida.add(new Items_Salida_set_get(object.getString("itemid"),object.getInt("cantidad"),object.getString("precio"),
                                            object.getString("total"), object.getString("nombremarca"),object.getString("tipovehiculo"),
                                            object.getString("codpedido"), object.getString("llantaid")));
                                    contar++;
                                } while (contar <= array.length());

                                try {
                                    i_salida = new Items_Salida(seleccionar_pedido_salida.this,salida);
                                    lvitems.setAdapter(i_salida);
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });//FIN SELECT - HEROKU -----------------------------------------------------
    }
}