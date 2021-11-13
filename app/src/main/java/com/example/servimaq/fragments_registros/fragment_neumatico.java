package com.example.servimaq.fragments_registros;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.menu_opciones;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class fragment_neumatico extends Fragment {

    ScrollView svScroll;
    View vista;
    EditText etprecio,etstock;
    Button btnRegistrar, btnCancelar;
    Spinner Spi_especificacion,Spi_vehiculo;
    TextView Tv_ver,Tv_ver1;
    ImageView ivFoto_neumatico, ivFoto_vehiculo;

    String vehiculo,especificacion;
    ArrayList<String> op = new ArrayList<>();
    ArrayList<String> info = new ArrayList<>();
    ArrayList<String> op1 = new ArrayList<>();
    ArrayList<String> info1 = new ArrayList<>();
    String VehiculoId="", DetalleLlantaId="";

    ArrayList<String> fotoVehiculo = new ArrayList<>();
    ArrayList<String> fotoNeumatico = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_neumatico, container, false);

        svScroll = vista.findViewById(R.id.svScroll);
        etprecio = vista.findViewById(R.id.etprecio);
        etstock = vista.findViewById(R.id.etstock);
        btnRegistrar=vista.findViewById(R.id.btnRegistrar);
        btnCancelar=vista.findViewById(R.id.btnCancelar);
        Tv_ver = vista.findViewById(R.id.Tv_ver);
        Tv_ver1 = vista.findViewById(R.id.Tv_ver1);
        ivFoto_neumatico = vista.findViewById(R.id.ivFoto_neumatico);
        ivFoto_vehiculo = vista.findViewById(R.id.ivFoto_vehiculo);

        Spi_especificacion=vista.findViewById(R.id.Spi_especificacion);
        Spi_vehiculo=vista.findViewById(R.id.Spi_vehiculo);

        //CARGAR DATO DEL SPINNER - VEHICULO *************************************************************************************************************************
        //EVENTO DEL HEROKU - DB SELECCION VEHICULO ************************************************************************************************************
        AndroidNetworking.initialize(getContext());
        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Vehiculo_POST_SELECT_ALL.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String validarDatos = response.getString("data");
                            String aux_VehiculoId = "";
                            int contar = 1;

                            Log.e("respuesta: ", "" + validarDatos);

                            if (!validarDatos.equals("[]")) {
                                JSONArray array = response.getJSONArray("data");
                                do {
                                    JSONObject object = array.getJSONObject(contar-1);
                                    Log.e("Recorrido: ", "aca llegue");
                                    aux_VehiculoId = object.getString("vehiculoid");

                                    op.add(aux_VehiculoId);
                                    vehiculo = "→ Tipo de Vehiculo: "+object.getString("tipovehiculo")+
                                            "\n→ Marca del Vehiculo: "+ object.getString("marcavehiculo") +
                                            "\n→ Modelo del Vehiculo: "+object.getString("modelovehiculo");
                                    info.add(vehiculo);
                                    fotoVehiculo.add(object.getString("fotovehiculo"));
                                    contar++;
                                } while (contar <= array.length());

                                ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, op);
                                Spi_vehiculo.setAdapter(adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(),"Error: "+anError.toString(),Toast.LENGTH_SHORT).show();
                    }
                });

        //SELECCIONAR VEHICULO ID ************************************************************************************************************
        Spi_vehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) adapterView.getChildAt(0)).setBackgroundColor(Color.parseColor("#835E5A5A"));
                ((TextView) adapterView.getChildAt(0)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                if(!fotoVehiculo.get(i).isEmpty()){
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference islandRef = storageRef.child(fotoVehiculo.get(i));
                    final long ONE_MEGABYTE = 480 * 480;

                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            ivFoto_vehiculo.setImageBitmap(bitmap);

                            Tv_ver1.setText(info.get(i));
                            VehiculoId=op.get(i);
                        }
                    });
                }else{
                    Tv_ver1.setText(info.get(i));
                    VehiculoId=op.get(i);
                    ivFoto_vehiculo.setImageDrawable(getResources().getDrawable(R.drawable.no_imagen));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        //CARGAR DATO DEL SPINNER - VEHICULO *************************************************************************************************************************
        //EVENTO DEL HEROKU - CARGAR DATO DEL SPINNER DE DETALLE DE LLANTA ************************************************************************************************************
        AndroidNetworking.initialize(getContext());
        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_DetalleLlanta_POST_SELECT_ALL.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String validarDatos = response.getString("data");
                            String aux_DetalleLlantaId = "";
                            int contar = 1;

                            Log.e("respuesta: ", "" + validarDatos);

                            if (!validarDatos.equals("[]")) {
                                JSONArray array = response.getJSONArray("data");
                                do {
                                    JSONObject object = array.getJSONObject(contar-1);
                                    Log.e("Recorrido: ", "aca llegue");
                                    aux_DetalleLlantaId = object.getString("detallellantaid");

                                    op1.add(aux_DetalleLlantaId);
                                    especificacion="→ NombreMarca: "+object.getString("nombremarca")+
                                            "\n→ IndiceCarga: "+object.getString("indicecarga")+
                                            "\n→ IndiceVelocidad:"+object.getString("indicevelocidad") +
                                            "\n→ Construccion: "+object.getString("construccion")+
                                            "\n→ PresionMaxima: "+object.getString("presionmaxima")+
                                            "\n→ Clasificacion: "+object.getString("clasificacion")+
                                            "\n→ FechaFabricacion: "+object.getString("fechafabricacion")+
                                            "\n→ MedidaLlantaId: "+object.getString("medidallantaid");
                                    info1.add(especificacion);
                                    fotoNeumatico.add(object.getString("fotollanta"));
                                    contar++;
                                } while (contar <= array.length());

                                try {
                                    ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, op1);
                                    Spi_especificacion.setAdapter(adapter);
                                }catch (Exception e){

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(),"Error: "+anError.toString(),Toast.LENGTH_SHORT).show();
                    }
                });


        //SELECCIONAR DETALLE NEUMATICO ID ************************************************************************************************************
        Spi_especificacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) adapterView.getChildAt(0)).setBackgroundColor(Color.parseColor("#835E5A5A"));
                ((TextView) adapterView.getChildAt(0)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                if(!fotoNeumatico.get(i).isEmpty()){
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference islandRef = storageRef.child(fotoNeumatico.get(i));
                    final long ONE_MEGABYTE = 480 * 480;

                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            ivFoto_neumatico.setImageBitmap(bitmap);

                            Tv_ver.setText(info1.get(i));
                            DetalleLlantaId=op1.get(i);
                        }
                    });
                }else{
                    Tv_ver.setText(info1.get(i));
                    DetalleLlantaId=op1.get(i);
                    ivFoto_neumatico.setImageDrawable(getResources().getDrawable(R.drawable.no_imagen));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        //REGISTRAR DATOS DE T_LLANTA EN HEROKU ************************************************************************************************************
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String precio = etprecio.getText().toString(),
                        stock = etstock.getText().toString();

                if(ValidarCampos()){

                    Map<String,String> insertar = new HashMap<>();

                    insertar.put("DetalleLlantaId", DetalleLlantaId);
                    insertar.put("VehiculoId", VehiculoId);

                    //EVENTO DEL HEROKU DB SELECCION************************************************************************************************************
                    AndroidNetworking.initialize(getContext());
                    AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Llanta_POST_SELECT.php")
                            .setPriority(Priority.IMMEDIATE)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    String LlantaId = "", aux_LlantaId = "";
                                    int contar = 1, extraer = 0;

                                    try {
                                        String validarDatos = response.getString("data");
                                        int pos = 1;
                                        Log.e("respuesta: ", "" + validarDatos);
                                        //--VALIDAR LOGIN********************************************************************************************************
                                        if (validarDatos.equals("[]")) {
                                            LlantaId = "Ll01";
                                        } else {
                                            JSONArray array = response.getJSONArray("data");
                                            do {
                                                JSONObject object = array.getJSONObject(contar-1);
                                                Log.e("Recorrido: ", "aca llegue");
                                                aux_LlantaId = object.getString("llantaid");
                                                Log.e("id capturado::: ", "" + aux_LlantaId);
                                                extraer = Integer.parseInt(aux_LlantaId.substring(aux_LlantaId.length() - pos));

                                                if (extraer != contar && contar <= 9 && contar >= 1) {
                                                    LlantaId = "Ll0" + contar;
                                                    break;
                                                }else if(extraer==0){
                                                    pos = 2;
                                                    extraer = Integer.parseInt(aux_LlantaId.substring(aux_LlantaId.length()-pos));
                                                    if(extraer!=contar && contar<=99 && contar>=10){
                                                        LlantaId = "Ll"+contar;
                                                        break;
                                                    }
                                                }else if(extraer!=contar && contar<=99 && contar>=10){
                                                    LlantaId = "Ll"+contar;
                                                    break;
                                                }
                                                contar++;
                                            } while (contar <= array.length());

                                            if (contar <= 9 && extraer != contar) {
                                                LlantaId = "Ll0" + contar;
                                            } else if (contar >= 10 && contar <= 99 && extraer != contar) {
                                                LlantaId = "Ll" + contar;
                                            }

                                            Log.e("id a insertar: ", "" + LlantaId);
                                        }

                                        insertar.put("LlantaId", LlantaId);
                                        insertar.put("Precio", precio);
                                        insertar.put("Stock", stock);

                                        JSONObject datosJSON = new JSONObject(insertar);

                                        //EVENTO DEL HEROKU DB INSERCION*****************************************************************************
                                        AndroidNetworking.initialize(getContext());
                                        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Llanta_POST_INSERT.php")
                                                .addJSONObjectBody(datosJSON)
                                                .setPriority(Priority.MEDIUM)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {

                                                        try {
                                                            String validarDatos = response.getString("data");
                                                            Log.e("respuesta insercion: ", "" + validarDatos);
                                                            //EstiloToast(getContext(), "Registro de Neumatico exitoso");
                                                            Toast.makeText(getContext(),"Registro de Neumatico exitoso",Toast.LENGTH_SHORT).show();
                                                            Limpiar();
                                                            etprecio.requestFocus();
                                                            svScroll.fullScroll(ScrollView.FOCUS_UP);

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
                    String alerta = "Debe llenar todos los campos";
                    if(DetalleLlantaId.equals("") && VehiculoId.equals("")){
                        alerta = "No tiene detalles de neumatico y vehiculos registrados";
                    }else if(DetalleLlantaId.equals("")){
                        alerta = "No tiene detalles de neumatico registrados";
                    }else if(VehiculoId.equals("")){
                        alerta = "No tiene vehiculos registrados";
                    }
                    Toast.makeText(getContext(),alerta,Toast.LENGTH_SHORT).show();
                    /*Toast toast = Toast.makeText(getContext(),alerta, Toast.LENGTH_SHORT);
                    View vista = toast.getView();
                    vista.setBackgroundResource(R.drawable.estilo_color_x);
                    toast.setGravity(Gravity.CENTER,0,0);
                    TextView text = (TextView) vista.findViewById(android.R.id.message);
                    text.setTextColor(Color.parseColor("#FFFFFF"));
                    text.setTextSize(15);
                    toast.show();*/
                }//FIN IF----------------------

            }
        });

        // cancelar -----------------------*-----------------------------------------------------
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Limpiar();
            }
        });

        return vista;
    }


    //LIMPIAR llanta--------------------------------------------------------------------------------------
    public void Limpiar(){
        etprecio.setText("");
        etstock.setText("");
    }

    public boolean ValidarCampos(){
        return !etprecio.getText().toString().trim().isEmpty() && !etstock.getText().toString().trim().isEmpty();
    }

    /*public void EstiloToast(Context c, String mensaje){
        Toast toast = Toast.makeText(c,mensaje, Toast.LENGTH_SHORT);
        View vista = toast.getView();
        vista.setBackgroundResource(R.drawable.estilo_color_x);
        toast.setGravity(Gravity.CENTER,0,0);
        TextView text = (TextView) vista.findViewById(android.R.id.message);
        text.setTextColor(Color.parseColor("#FFFFFF"));
        text.setTextSize(16);
        toast.show();
    }*/
}