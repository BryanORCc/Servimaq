package com.example.servimaq.fragments_registros;

import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class fragment_vehiculo extends Fragment {

    ScrollView svScroll;
    View vista;
    EditText etTipoVehiculo, etMarcaVehiculo, etModeloVehiculo;
    Button btnFoto, btnRegistrar, btnCancelar;
    ImageView ivFoto;

    String ruta = null;

    private StorageReference mStorage;
    StorageReference FilePath;
    Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_vehiculo, container, false);

        //STORAGE FIREBASE*****************************
        mStorage = FirebaseStorage.getInstance().getReference();

        svScroll = vista.findViewById(R.id.svScroll);
        etTipoVehiculo = vista.findViewById(R.id.etTipoVehiculo);
        etMarcaVehiculo = vista.findViewById(R.id.etMarcaVehiculo);
        etModeloVehiculo = vista.findViewById(R.id.etModeloVehiculo);
        btnFoto = vista.findViewById(R.id.btnFoto);
        btnRegistrar = vista.findViewById(R.id.btnRegistrar);
        btnCancelar = vista.findViewById(R.id.btnCancelar);
        ivFoto = vista.findViewById(R.id.ivFoto);


        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarImagen();
            }
        });

        //BOTON REGISTRO ***********************************************************************************************************
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TipoVehiculo = etTipoVehiculo.getText().toString(),
                        MarcaVehiculo = etMarcaVehiculo.getText().toString(),
                        ModeloVehiculo = etModeloVehiculo.getText().toString();

                if(ValidarCampos()){

                    if(uri!=null){
                        //EVENTO DEL FIRESTORE***********************************************************************************************************
                        FilePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ruta = taskSnapshot.getMetadata().getPath();
                                Log.e("direccion: ","++"+ruta);

                                Map<String,String> insertar = new HashMap<>();

                                if(ruta==null){
                                    ruta = "";
                                }

                                insertar.put("FotoVehiculo",ruta);

                                //EVENTO DEL HEROKU DB SELECCION***********************************************************************************************************
                                AndroidNetworking.initialize(getContext());
                                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Vehiculo_POST_SELECT.php")
                                        .setPriority(Priority.IMMEDIATE)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                                String VehiculoId="", aux_VehiculoId="";
                                                int contar= 1, extraer = 0;

                                                try {
                                                    String validarDatos = response.getString("data");
                                                    int pos = 1;
                                                    Log.e("respuesta: ",""+validarDatos);
                                                    //--VALIDAR LOGIN***********************************************************************************************************
                                                    if(validarDatos.equals("[]")){
                                                        VehiculoId = "VH01";
                                                    }else{
                                                        JSONArray array = response.getJSONArray("data");
                                                        do {
                                                            JSONObject object = array.getJSONObject(contar-1);
                                                            aux_VehiculoId = object.getString("vehiculoid");
                                                            Log.e("id capturado: ",""+aux_VehiculoId);
                                                            extraer = Integer.parseInt(aux_VehiculoId.substring(aux_VehiculoId.length()-pos));

                                                            if(extraer!=contar && contar<=9 && contar>=1){
                                                                VehiculoId = "VH0"+contar;
                                                                break;
                                                            }else if(extraer==0){
                                                                pos = 2;
                                                                extraer = Integer.parseInt(aux_VehiculoId.substring(aux_VehiculoId.length()-pos));
                                                                if(extraer!=contar && contar<=99 && contar>=10){
                                                                    VehiculoId = "VH"+contar;
                                                                    break;
                                                                }
                                                            }else if(extraer!=contar && contar<=99 && contar>=10){
                                                                VehiculoId = "VH"+contar;
                                                                break;
                                                            }
                                                            contar++;
                                                        } while (contar <= array.length());

                                                        if(contar<=9 && extraer!=contar){
                                                            VehiculoId = "VH0"+contar;
                                                        }else if(contar>=10 && contar<=99 && extraer!=contar){
                                                            VehiculoId = "VH"+contar;
                                                        }

                                                    }//FIN IF VALIDACION REGISTROS----------------------------------------------------------------

                                                    insertar.put("VehiculoId",VehiculoId);
                                                    insertar.put("TipoVehiculo",TipoVehiculo);
                                                    insertar.put("MarcaVehiculo",MarcaVehiculo);
                                                    insertar.put("ModeloVehiculo",ModeloVehiculo);

                                                    JSONObject datosJSON = new JSONObject(insertar);

                                                    //EVENTO DEL HEROKU DB INSERCION***********************************************************************************************************
                                                    AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Vehiculo_POST_INSERT.php")
                                                            .addJSONObjectBody(datosJSON)
                                                            .setPriority(Priority.MEDIUM)
                                                            .build()
                                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {

                                                                    try {
                                                                        String validarDatos = response.getString("data");
                                                                        Log.e("respuesta insercion: ",""+validarDatos);

                                                                        EstiloToast(getContext(),"Registro de Vehiculo exitoso");
                                                                        Limpiar();
                                                                        svScroll.fullScroll(ScrollView.FOCUS_UP);
                                                                        etTipoVehiculo.requestFocus();


                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                }

                                                                @Override
                                                                public void onError(ANError anError) {
                                                                    Toast.makeText(getContext(),"Error:" + anError.getErrorDetail(),Toast.LENGTH_SHORT).show();
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

                                        });//FIN EVENTO DEL HEROKU DB SELECCION------------------------------------------------

                            }

                        }); //FIN FIRESTORE----------------------
                    }else{

                        Map<String,String> insertar = new HashMap<>();

                        if(ruta==null){
                            ruta = "";
                        }
                        insertar.put("FotoVehiculo",ruta);

                        //EVENTO DEL HEROKU DB SELECCION************************************************************************************************************
                        AndroidNetworking.initialize(getContext());
                        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Vehiculo_POST_SELECT.php")
                                .setPriority(Priority.IMMEDIATE)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        String VehiculoId = "", aux_VehiculoId = "";
                                        int contar = 1, extraer = 0;

                                        try {
                                            String validarDatos = response.getString("data");
                                            int pos = 1;
                                            Log.e("respuesta: ", "" + validarDatos);
                                            //--VALIDAR LOGIN********************************************************************************************************
                                            if (validarDatos.equals("[]")) {
                                                VehiculoId = "VH01";
                                            } else {
                                                JSONArray array = response.getJSONArray("data");
                                                do {
                                                    JSONObject object = array.getJSONObject(contar-1);
                                                    Log.e("Recorrido: ", "aca llegue");
                                                    aux_VehiculoId = object.getString("vehiculoid");
                                                    Log.e("id capturado::: ", "" + aux_VehiculoId);
                                                    extraer = Integer.parseInt(aux_VehiculoId.substring(aux_VehiculoId.length() - pos));

                                                    if (extraer != contar && contar <= 9 && contar >= 1) {
                                                        VehiculoId = "VH0" + contar;
                                                        break;
                                                    }else if(extraer==0){
                                                        pos = 2;
                                                        extraer = Integer.parseInt(aux_VehiculoId.substring(aux_VehiculoId.length()-pos));
                                                        if(extraer!=contar && contar<=99 && contar>=10){
                                                            VehiculoId = "VH"+contar;
                                                            break;
                                                        }
                                                    }else if(extraer!=contar && contar<=99 && contar>=10){
                                                        VehiculoId = "VH"+contar;
                                                        break;
                                                    }
                                                    contar++;
                                                } while (contar <= array.length());

                                                if (contar <= 9 && extraer != contar) {
                                                    VehiculoId = "VH0" + contar;
                                                } else if (contar >= 10 && contar <= 99 && extraer != contar) {
                                                    VehiculoId = "VH" + contar;
                                                }

                                                Log.e("id a insertar: ", "" + VehiculoId);
                                            }//FIN IF VALIDACION REGISTROS----------------------------------------------------------------

                                            insertar.put("VehiculoId", VehiculoId);
                                            insertar.put("TipoVehiculo", TipoVehiculo);
                                            insertar.put("MarcaVehiculo", MarcaVehiculo);
                                            insertar.put("ModeloVehiculo", ModeloVehiculo);

                                            JSONObject datosJSON = new JSONObject(insertar);

                                            //EVENTO DEL HEROKU DB INSERCION*****************************************************************************
                                            AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Vehiculo_POST_INSERT.php")
                                                    .addJSONObjectBody(datosJSON)
                                                    .setPriority(Priority.MEDIUM)
                                                    .build()
                                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {

                                                            try {
                                                                String validarDatos = response.getString("data");
                                                                Log.e("respuesta insercion: ", "" + validarDatos);

                                                                EstiloToast(getContext(), "Registro de Vehiculo exitoso");
                                                                Limpiar();
                                                                svScroll.fullScroll(ScrollView.FOCUS_UP);
                                                                etTipoVehiculo.requestFocus();

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
                    }

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

        //BOTON CANCELAR REGISTRO ---------------------------------------------------------------------------------
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Limpiar();
            }
        });

        return vista;
    }

    private void CargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"seleccione la aplicación"),10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if( resultCode==getActivity().RESULT_OK){
            uri = data.getData();
            FilePath = mStorage.child("fotos").child(uri.getLastPathSegment());
            ivFoto.setImageURI(uri);
        }
    }


    //LIMPIAR CAMPOS--------------------------------------------------------------------------------------
    public void Limpiar(){
        etTipoVehiculo.setText("");
        ivFoto.setImageDrawable(getResources().getDrawable(R.drawable.no_imagen));
        etMarcaVehiculo.setText("");
        etModeloVehiculo.setText("");
        ruta = null;
        uri = null;
    }

    public boolean ValidarCampos(){
        return !etTipoVehiculo.getText().toString().trim().isEmpty() && !etMarcaVehiculo.getText().toString().trim().isEmpty() && !etModeloVehiculo.getText().toString().trim().isEmpty();
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