package com.example.servimaq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.db.SQLConexion;
import com.itextpdf.text.pdf.codec.JBIG2SegmentReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etUsuario, etContra;
    Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.etUsuario);
        etContra = findViewById(R.id.etContra);
        btnIngresar = findViewById(R.id.btnIngresar);

        //BOTON DE INGRESO - LOGIN-------------------------------------------
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usuario = etUsuario.getText().toString(),
                        contra = etContra.getText().toString();

                if(ValidarCampos()){
                    AndroidNetworking.initialize(getApplicationContext());
                    Map<String,String> datosCuenta = new HashMap<>();
                    datosCuenta.put("Usuario",usuario);
                    datosCuenta.put("Contraseña",contra);

                    JSONObject datosJSON = new JSONObject(datosCuenta);
                    AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Cuenta_POST_ALL.php")
                            .addJSONObjectBody(datosJSON)
                            .setPriority(Priority.IMMEDIATE)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String validarDatos = response.getString("data");
                                        Log.e("respuesta: ",""+validarDatos);

                                        //--VALIDAR LOGIN------------------------------------------------------------------------------------------------
                                        if(!validarDatos.equals("[]")){
                                            Toast toast = Toast.makeText(getApplicationContext(),"Bienvenido", Toast.LENGTH_SHORT);
                                            View vista = toast.getView();
                                            vista.setBackgroundResource(R.drawable.estilo_color_x);
                                            toast.setGravity(Gravity.CENTER,0,0);
                                            TextView text = (TextView) vista.findViewById(android.R.id.message);
                                            text.setTextColor(Color.parseColor("#FFFFFF"));
                                            text.setTextSize(20);
                                            toast.show();

                                            Intent i = new Intent(MainActivity.this,menu_opciones.class);
                                            finish();
                                            overridePendingTransition(0, 0);
                                            startActivity(i);
                                            overridePendingTransition(0, 0);

                                        }else{
                                            Toast toast = Toast.makeText(getApplicationContext(),"Usuario o Contraseña incorrecto", Toast.LENGTH_SHORT);
                                            View vista = toast.getView();
                                            vista.setBackgroundResource(R.drawable.estilo_color_x);
                                            toast.setGravity(Gravity.CENTER,0,0);
                                            TextView text = (TextView) vista.findViewById(android.R.id.message);
                                            text.setTextColor(Color.parseColor("#FFFFFF"));
                                            text.setTextSize(20);
                                            toast.show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(getApplicationContext(),"Error:" + anError.getErrorDetail(),Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Debe llenar todos los campos", Toast.LENGTH_SHORT);
                    View vista = toast.getView();
                    vista.setBackgroundResource(R.drawable.estilo_color_x);
                    toast.setGravity(Gravity.CENTER,0,0);
                    TextView text = (TextView) vista.findViewById(android.R.id.message);
                    text.setTextColor(Color.parseColor("#FFFFFF"));
                    text.setTextSize(15);
                    toast.show();
                }


            }
        });
    }

    public boolean ValidarCampos(){
        return !etUsuario.getText().toString().trim().isEmpty() && !etContra.getText().toString().trim().isEmpty();
    }

}