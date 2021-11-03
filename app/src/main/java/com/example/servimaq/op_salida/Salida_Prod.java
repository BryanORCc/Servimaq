package com.example.servimaq.op_salida;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista_salida_P;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Salida_Prod extends AppCompatActivity {

    ArrayList<items_lista_salida_P>lista =new ArrayList<>();
    lista_salida_producto ilsp;
    ListView lvSalidaProducto;
    int validar = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida_prod);

        lvSalidaProducto = findViewById(R.id.lvSalidaProducto);

        AndroidNetworking.initialize(getApplicationContext());

        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Pedido_POST_SELECT_ALL.php")
                .setPriority(Priority.IMMEDIATE)
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
                                    lista.add(new items_lista_salida_P(object.getString("codpedido"),object.getString("nombrescliente")+ " " +object.getString("apellidoscliente"),
                                            object.getString("correo"),object.getString("fechaentrega"),object.getString("modopago"),object.getString("dni")));
                                    contar++;
                                } while (contar <= array.length());

                                try {
                                    ilsp = new lista_salida_producto(Salida_Prod.this,lista);
                                    lvSalidaProducto.setAdapter(ilsp);
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
                });

    }


}








