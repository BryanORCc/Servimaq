package com.example.servimaq.fragments_registros;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class fragment_medidas extends Fragment {

    View vista;
    EditText etAncho, etDiametro, etPerfil, etMmcocada;
    Button  btnRegistrar, btnCancelar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_medidas, container, false);

        etAncho=vista.findViewById(R.id.etAncho);
        etDiametro=vista.findViewById(R.id.etDiametro);
        etPerfil=vista.findViewById(R.id.etPerfil);
        etMmcocada=vista.findViewById(R.id.etMmcocada);
        btnRegistrar=vista.findViewById(R.id.btnRegistrar);
        btnCancelar=vista.findViewById(R.id.btnCancelar);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Ancho=etAncho.getText().toString(),
                        Diametro=etDiametro.getText().toString(),
                        Perfil=etPerfil.getText().toString(),
                        MmCocada=etMmcocada.getText().toString();

                if(ValidarCampos()){

                    Map<String,String> insertar = new HashMap<>();

                    //EVENTO DEL HEROKU DB SELECCION************************************************************************************************************
                    AndroidNetworking.initialize(getContext());
                    AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_MedidaLlanta_POST_SELECT.php")
                            .setPriority(Priority.IMMEDIATE)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    String MedidaLlantaId = "", aux_MedidaLlantaId = "";
                                    int contar = 1, extraer = 0;

                                    try {
                                        String validarDatos = response.getString("data");
                                        Log.e("respuesta: ", "" + validarDatos);
                                        //--VALIDAR LOGIN********************************************************************************************************
                                        if (validarDatos.equals("[]")) {
                                            MedidaLlantaId = "MD01";
                                        } else {
                                            JSONArray array = response.getJSONArray("data");
                                            do {
                                                JSONObject object = array.getJSONObject(contar-1);
                                                Log.e("id: ", "aca llegue");
                                                aux_MedidaLlantaId = object.getString("medidallantaid");
                                                Log.e("id::: ", "" + aux_MedidaLlantaId);
                                                extraer = Integer.parseInt(aux_MedidaLlantaId.substring(aux_MedidaLlantaId.length() - 1));

                                                if (extraer != contar && contar <= 9 && contar >= 1) {
                                                    MedidaLlantaId = "MD0" + contar;
                                                    break;
                                                } else if (extraer == 0) {
                                                    if (extraer != contar && contar <= 99 && contar >= 10) {
                                                        MedidaLlantaId = "MD" + contar;
                                                        break;
                                                    }
                                                }
                                                contar++;
                                            } while (contar <= array.length());

                                            if (contar <= 9 && extraer != contar) {
                                                MedidaLlantaId = "MD0" + contar;
                                            } else if (contar >= 10 && contar <= 99 && extraer != contar) {
                                                MedidaLlantaId = "MD" + contar;
                                            }

                                            Log.e("id: ", "" + MedidaLlantaId);
                                        }

                                        insertar.put("MedidaLlantaId", MedidaLlantaId);
                                        insertar.put("Ancho", Ancho);
                                        insertar.put("Diametro", Diametro);
                                        insertar.put("Perfil", Perfil);
                                        insertar.put("MmCocada", MmCocada);

                                        JSONObject datosJSON = new JSONObject(insertar);

                                        //EVENTO DEL HEROKU DB INSERCION*****************************************************************************
                                        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_MedidaLlanta_POST_INSERT.php")
                                                .addJSONObjectBody(datosJSON)
                                                .setPriority(Priority.MEDIUM)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {

                                                        try {
                                                            String validarDatos = response.getString("data");
                                                            Log.e("respuesta insercion: ", "" + validarDatos);
                                                            EstiloToast(getContext(), "Registro de Medida exitoso");

                                                            Limpiar();
                                                            etAncho.requestFocus();

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(ANError anError) {
                                                        Toast.makeText(getContext(), "Error:" + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });//FIN EVENTO DEL HEROKU DB INSERCION------------------------------------------------

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(getContext(),"Error:" + anError.getErrorDetail(),Toast.LENGTH_SHORT).show();
                                }

                            });//EVENTO DEL HEROKU DB SELECCION------------------------------------------------



                }else{
                    Toast toast = Toast.makeText(getContext(),"Debe llenar todos los campos", Toast.LENGTH_SHORT);
                    View vista = toast.getView();
                    vista.setBackgroundResource(R.drawable.estilo_color_x);
                    toast.setGravity(Gravity.CENTER,0,0);
                    TextView text = (TextView) vista.findViewById(android.R.id.message);
                    text.setTextColor(Color.parseColor("#FFFFFF"));
                    text.setTextSize(15);
                    toast.show();
                }//FIN IF----------------------
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Limpiar();
            }
        });

        return vista;
    }

    //LIMPIAR CAMPOS--------------------------------------------------------------------------------------
    public void Limpiar(){
        etAncho.setText("");
        etDiametro.setText("");
        etPerfil.setText("");
        etMmcocada.setText("");
    }

    public boolean ValidarCampos(){
        return !etAncho.getText().toString().trim().isEmpty() && !etDiametro.getText().toString().trim().isEmpty() && !etPerfil.getText().toString().trim().isEmpty()
                && !etMmcocada.getText().toString().trim().isEmpty();
    }

    public void EstiloToast(Context c, String mensaje){
        Toast toast = Toast.makeText(c,mensaje, Toast.LENGTH_SHORT);
        View vista = toast.getView();
        vista.setBackgroundResource(R.drawable.estilo_color_x);
        toast.setGravity(Gravity.CENTER,0,0);
        TextView text = (TextView) vista.findViewById(android.R.id.message);
        text.setTextColor(Color.parseColor("#FFFFFF"));
        text.setTextSize(16);
        toast.show();
    }
}