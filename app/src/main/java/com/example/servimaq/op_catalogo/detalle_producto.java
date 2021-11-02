package com.example.servimaq.op_catalogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class detalle_producto extends AppCompatActivity {

    ImageView ivFoto_Llanta;
    TextView tvllantaId, tvNombreMarca, tvIndiceCarga, tvIndiceVelocidad, tvConstruccion, tvClasificacion, tvFechaFabricacion, tvMarcaVehiculo,
            tvModeloVehiculo, tvAncho, tvDiametro, tvPerfil, tvMmCocada, tvPresionMaxima, tvStock, tvPrecio, tvTipoVehiculo;
    EditText etNombreMarca, etIndiceCarga, etIndiceVelocidad, etConstruccion, etClasificacion, etMarcaVehiculo,
            etModeloVehiculo, etAncho, etDiametro, etPerfil, etMmCocada, etPresionMaxima, etStock, etPrecio, etTipoVehiculo;
    Button btnModificar, btnCancelar, btnConfirmar;
    LinearLayout llOpcionVehiculoId, llOpcionDetalleLlantaId, llOpcionMedidaLlantaId;
    Spinner spVehiculoId, spDetalleLlantaId, spMedidaLlantaId;
    ArrayList<String> datosVehiculo = new ArrayList<>();
    ArrayList<String> datosDetalle = new ArrayList<>();
    ArrayList<String> datosMedida = new ArrayList<>();
    String llantaId, VehiculoId, DetalleLlantaId, MedidaLlantaId;

    String mensajeToast = null;

    boolean opcionModificar = false;
    int estadoCancelar = 0;

    public static EditText etFechaFabricacion;
    public static int dia, mes, año;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //BOTONES--------------------------------------------------
        btnModificar = findViewById(R.id.btnModificar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnConfirmar = findViewById(R.id.btnConfirmar);

        //BLOQUES DE SPINNER---------------------------------------
        llOpcionVehiculoId = findViewById(R.id.llOpcionVehiculoId);
        llOpcionDetalleLlantaId = findViewById(R.id.llOpcionDetalleLlantaId);
        llOpcionMedidaLlantaId = findViewById(R.id.llOpcionMedidaLlantaId);
        spVehiculoId = findViewById(R.id.spVehiculoId);
        spDetalleLlantaId = findViewById(R.id.spDetalleLlantaId);
        spMedidaLlantaId = findViewById(R.id.spMedidaLlantaId);

        Intent datos = getIntent();

        llantaId = datos.getStringExtra("llantaId");
        DetalleLlantaId = datos.getStringExtra("DetalleLlantaId");
        VehiculoId = datos.getStringExtra("VehiculoId");
        MedidaLlantaId = datos.getStringExtra("MedidaLlantaId");

        //FOTO-----------------------------------------------
        ivFoto_Llanta = findViewById(R.id.ivFoto_Llanta);

        //TEXT VIEWS ----------------------------------------------------------------
        tvllantaId = findViewById(R.id.tvllantaId);
        tvNombreMarca = findViewById(R.id.tvNombreMarca);
        tvIndiceCarga = findViewById(R.id.tvIndiceCarga);
        tvIndiceVelocidad = findViewById(R.id.tvIndiceVelocidad);
        tvConstruccion = findViewById(R.id.tvConstruccion);
        tvClasificacion = findViewById(R.id.tvClasificacion);
        tvFechaFabricacion = findViewById(R.id.tvFechaFabricacion);
        tvMarcaVehiculo = findViewById(R.id.tvMarcaVehiculo);
        tvModeloVehiculo = findViewById(R.id.tvModeloVehiculo);
        tvAncho = findViewById(R.id.tvAncho);
        tvDiametro = findViewById(R.id.tvDiametro);
        tvPerfil = findViewById(R.id.tvPerfil);
        tvMmCocada = findViewById(R.id.tvMmCocada);
        tvPresionMaxima = findViewById(R.id.tvPresionMaxima);
        tvStock = findViewById(R.id.tvStock);
        tvPrecio = findViewById(R.id.tvPrecio);
        tvTipoVehiculo = findViewById(R.id.tvTipoVehiculo);


        //EDITS TEXT ----------------------------------------------------------------
        etNombreMarca = findViewById(R.id.etNombreMarca);
        etIndiceCarga = findViewById(R.id.etIndiceCarga);
        etIndiceVelocidad = findViewById(R.id.etIndiceVelocidad);
        etConstruccion = findViewById(R.id.etConstruccion);
        etClasificacion = findViewById(R.id.etClasificacion);
        etFechaFabricacion = findViewById(R.id.etFechaFabricacion);
        etMarcaVehiculo = findViewById(R.id.etMarcaVehiculo);
        etModeloVehiculo = findViewById(R.id.etModeloVehiculo);
        etAncho = findViewById(R.id.etAncho);
        etDiametro = findViewById(R.id.etDiametro);
        etPerfil = findViewById(R.id.etPerfil);
        etMmCocada = findViewById(R.id.etMmCocada);
        etPresionMaxima = findViewById(R.id.etPresionMaxima);
        etStock = findViewById(R.id.etStock);
        etPrecio = findViewById(R.id.etPrecio);
        etTipoVehiculo = findViewById(R.id.etTipoVehiculo);

        //INICIAR CONEXION CON EL SERVICIO WEB - HEROKU
        AndroidNetworking.initialize(getApplicationContext());

        //--CARGAR DATOS A LOS SPINNERS - VEHICULO ------------------------------------------------------------------------------
        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Vehiculo_POST_SELECT.php")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String validarDatos = response.getString("data");
                            int contar = 1;
                            Log.e("respuesta: ", "" + validarDatos);
                            //--VALIDAR LOGIN***********************************************************************************************************
                            if (validarDatos.equals("[]")) {
                                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray array = response.getJSONArray("data");
                                do {
                                    JSONObject object = array.getJSONObject(contar - 1);

                                    //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                                    datosVehiculo.add(object.getString("vehiculoid"));
                                    contar++;
                                } while (contar <= array.length());

                                //LLENAR EL SPINNER CON LOS DATOS-----------------------------------------
                                try {
                                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, datosVehiculo);
                                    spVehiculoId.setAdapter(adapter);
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });//FIN Carga--------------------------------------------------------------------------------



        //--CARGAR DATOS A LOS SPINNERS - DETALLE ------------------------------------------------------------------------------
        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_DetalleLlanta_POST_SELECT.php")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String validarDatos = response.getString("data");
                            int contar = 1;
                            Log.e("respuesta: ", "" + validarDatos);
                            //--VALIDAR LOGIN***********************************************************************************************************
                            if (validarDatos.equals("[]")) {
                                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray array = response.getJSONArray("data");
                                do {
                                    JSONObject object = array.getJSONObject(contar - 1);

                                    //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                                    datosDetalle.add(object.getString("detallellantaid"));
                                    contar++;
                                } while (contar <= array.length());

                                //LLENAR EL SPINNER CON LOS DATOS-----------------------------------------
                                try {
                                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, datosDetalle);
                                    spDetalleLlantaId.setAdapter(adapter);
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });//FIN Carga--------------------------------------------------------------------------------



        //--CARGAR DATOS A LOS SPINNERS - MEDIDA ------------------------------------------------------------------------------:::::::
        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_MedidaLlanta_POST_SELECT.php")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String validarDatos = response.getString("data");
                            int contar = 1;
                            Log.e("respuesta: ", "" + validarDatos);
                            //--VALIDAR ***********************************************************************************************************
                            if (validarDatos.equals("[]")) {
                                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray array = response.getJSONArray("data");
                                do {
                                    JSONObject object = array.getJSONObject(contar - 1);

                                    //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                                    datosMedida.add(object.getString("medidallantaid"));
                                    contar++;
                                } while (contar <= array.length());

                                //LLENAR EL SPINNER CON LOS DATOS-----------------------------------------
                                try {
                                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, datosMedida);
                                    spMedidaLlantaId.setAdapter(adapter);

                                    int x = -1;
                                    do{
                                        x++;
                                    }while (datosMedida.get(x).equalsIgnoreCase(MedidaLlantaId));
                                    spMedidaLlantaId.setSelection(x);
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });//FIN Carga--------------------------------------------------------------------------------



        //SELECCION DE OPCION - SPINNER VEHICULO :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        spVehiculoId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                VehiculoId=datosVehiculo.get(i);


                //--CARGAR DATOS AL SPINNER DE VEHICULO------------------------------------------------------------------------------
                Map<String,String> insertar = new HashMap<>();
                insertar.put("VehiculoId",VehiculoId);
                JSONObject datosJSON = new JSONObject(insertar);

                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Vehiculo_POST_SELECT_ALL_WHERE.php")
                        .addJSONObjectBody(datosJSON)
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String validarDatos = response.getString("data");
                                    int contar = 1;
                                    Log.e("respuesta: ", "" + validarDatos);

                                    //--VALIDAR ***********************************************************************************************************
                                    if (validarDatos.equals("[]")) {
                                        Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                                    } else {
                                        JSONArray array = response.getJSONArray("data");
                                        do {
                                            JSONObject object = array.getJSONObject(contar - 1);

                                            //CARGAR DATOS DE LA TABLA-----------------------------
                                            etTipoVehiculo.setText(object.getString("tipovehiculo"));
                                            etMarcaVehiculo.setText(object.getString("marcavehiculo"));
                                            etModeloVehiculo.setText(object.getString("modelovehiculo"));
                                            contar++;
                                        } while (contar <= array.length());

                                    }
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(), "Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });//FIN SELECCION DE OPCION -------------------------------------



        //SELECCION DE OPCION - SPINNER DETALLE :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        spDetalleLlantaId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                DetalleLlantaId=datosDetalle.get(i);


                //--CARGAR DATOS AL SPINNER DE DETALLE------------------------------------------------------------------------------
                Map<String,String> insertar = new HashMap<>();
                insertar.put("DetalleLlantaId",DetalleLlantaId);
                JSONObject datosJSON = new JSONObject(insertar);

                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_DetalleLlanta_POST_SELECT_ALL_WHERE.php")
                        .addJSONObjectBody(datosJSON)
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String validarDatos = response.getString("data");
                                    int contar = 1;
                                    Log.e("respuesta: ", "" + validarDatos);

                                    //--VALIDAR ***********************************************************************************************************
                                    if (validarDatos.equals("[]")) {
                                        Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                                    } else {
                                        JSONArray array = response.getJSONArray("data");
                                        do {
                                            JSONObject object = array.getJSONObject(contar - 1);

                                            //CARGAR DATOS DE LA TABLA-----------------------------
                                            etNombreMarca.setText(object.getString("nombremarca"));
                                            etIndiceCarga.setText(object.getString("indicecarga"));
                                            etIndiceVelocidad.setText(object.getString("indicevelocidad"));
                                            etConstruccion.setText(object.getString("construccion"));
                                            etPresionMaxima.setText(object.getString("presionmaxima"));
                                            etClasificacion.setText(object.getString("clasificacion"));
                                            etFechaFabricacion.setText(object.getString("fechafabricacion"));
                                            contar++;
                                        } while (contar <= array.length());

                                    }
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(), "Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //SELECCION DE OPCION - SPINNER MEDIDA :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        spMedidaLlantaId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                MedidaLlantaId=datosMedida.get(i);

                //--CARGAR DATOS AL SPINNER DE MEDIDA------------------------------------------------------------------------------
                Map<String,String> insertar = new HashMap<>();
                insertar.put("MedidaLlantaId",MedidaLlantaId);
                JSONObject datosJSON = new JSONObject(insertar);

                //--CARGAR DATOS AL SPINNER DE VEHICULO------------------------------------------------------------------------------
                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_MedidaLlanta_POST_SELECT_ALL_WHERE.php")
                        .addJSONObjectBody(datosJSON)
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String validarDatos = response.getString("data");
                                    int contar = 1;
                                    Log.e("respuesta: ", "" + validarDatos);

                                    //--VALIDAR ***********************************************************************************************************
                                    if (validarDatos.equals("[]")) {
                                        Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                                    } else {
                                        JSONArray array = response.getJSONArray("data");
                                        do {
                                            JSONObject object = array.getJSONObject(contar - 1);

                                            //CARGAR DATOS DE LA TABLA-----------------------------
                                            etAncho.setText(""+object.getInt("ancho"));
                                            etDiametro.setText(""+object.getInt("diametro"));
                                            etPerfil.setText(""+object.getInt("perfil"));
                                            etMmCocada.setText(""+object.getDouble("mmcocada"));

                                            contar++;
                                        } while (contar <= array.length());

                                    }
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(), "Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //**************************SELECT INFORMACION NEUMATICO EDIT TEXT---**************************************************************************
        //****************************************************************************************************
        Map<String,String> insertar = new HashMap<>();
        insertar.put("LlantaId",llantaId);
        JSONObject datosJSON = new JSONObject(insertar);

        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/OP_Detalle_SELECT_INNER_WHERE.php")
                .addJSONObjectBody(datosJSON)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String validarDatos = response.getString("data");
                            int contar = 1;
                            Log.e("respuesta: ", "" + validarDatos);

                            //--VALIDAR ***********************************************************************************************************
                            if (validarDatos.equals("[]")) {
                                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray array = response.getJSONArray("data");
                                do {
                                    JSONObject object = array.getJSONObject(contar - 1);

                                    //CARGAR DATOS A LOS TEXT VIEW--------------------------------------
                                    tvllantaId.setText(""+object.getString("llantaid"));
                                    tvStock.setText(""+object.getInt("stock"));
                                    tvPrecio.setText(""+object.getString("precio"));
                                    tvTipoVehiculo.setText(""+object.getString("tipovehiculo"));
                                    tvMarcaVehiculo.setText(""+object.getString("marcavehiculo"));
                                    tvModeloVehiculo.setText(""+object.getString("modelovehiculo"));
                                    tvNombreMarca.setText(""+object.getString("nombremarca"));
                                    tvIndiceCarga.setText(""+object.getInt("indicecarga"));
                                    tvIndiceVelocidad.setText(""+object.getString("indicevelocidad"));
                                    tvConstruccion.setText(""+object.getString("construccion"));
                                    tvPresionMaxima.setText(""+object.getString("presionmaxima"));
                                    tvClasificacion.setText(""+object.getString("clasificacion"));
                                    tvFechaFabricacion.setText(""+object.getString("fechafabricacion"));
                                    tvAncho.setText(""+object.getInt("ancho"));
                                    tvDiametro.setText(""+object.getInt("diametro"));
                                    tvPerfil.setText(""+object.getInt("perfil"));
                                    tvMmCocada.setText(""+object.getDouble("mmcocada"));

                                    if(object.getString("fotollanta").isEmpty()){
                                        ivFoto_Llanta.setImageDrawable(getResources().getDrawable(R.drawable.no_imagen));
                                    }else{
                                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                        StorageReference islandRef = storageRef.child(object.getString("fotollanta"));
                                        final long ONE_MEGABYTE = 480 * 480;

                                        Log.e("Contar","___: "+ object.getString("fotollanta"));

                                        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                            @Override
                                            public void onSuccess(byte[] bytes) {
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                                ivFoto_Llanta.setImageBitmap(bitmap);
                                            }
                                        });
                                    }

                                    contar++;
                                } while (contar <= array.length());
                            }
                            int x = -1;
                            do{
                                x++;
                            }while (datosMedida.get(x).equalsIgnoreCase(MedidaLlantaId));
                            spMedidaLlantaId.setSelection(x);

                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });//FIN SELECT-------------------------------------


        //OCULTAR ----------------------------------------
        OcultarEditText();
        btnConfirmar.setVisibility(View.GONE);
        llOpcionVehiculoId.setVisibility(View.GONE);
        llOpcionDetalleLlantaId.setVisibility(View.GONE);
        llOpcionMedidaLlantaId.setVisibility(View.GONE);


        //CAMBIAR VISTA CON EDIT TEXT----------------------------------------------------------------------*************************************
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                estadoCancelar = 1;

                OcultarTextView();
                MostrarEditText();
                btnModificar.setVisibility(View.GONE);
                btnConfirmar.setVisibility(View.VISIBLE);
                llOpcionVehiculoId.setVisibility(View.VISIBLE);
                llOpcionDetalleLlantaId.setVisibility(View.VISIBLE);
                llOpcionMedidaLlantaId.setVisibility(View.VISIBLE);

                //--SELECT INFORMACION NEUMATICO EDIT TEXT-----------------------------------------------------------------------------
                Map<String,String> insertar = new HashMap<>();
                insertar.put("LlantaId",llantaId);
                JSONObject datosJSON = new JSONObject(insertar);

                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/OP_Detalle_SELECT_INNER_WHERE.php")
                        .addJSONObjectBody(datosJSON)
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String validarDatos = response.getString("data");
                                    int contar = 1;
                                    Log.e("respuesta: ", "" + validarDatos);

                                    //--VALIDAR ***********************************************************************************************************
                                    if (validarDatos.equals("[]")) {
                                        Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                                    } else {
                                        JSONArray array = response.getJSONArray("data");
                                        do {
                                            JSONObject object = array.getJSONObject(contar - 1);

                                            //CARGAR DATOS A LOS EDIT TEXT--------------------------------------
                                            etStock.setText(""+object.getInt("stock"));
                                            etPrecio.setText(""+object.getString("precio"));
                                            etTipoVehiculo.setText(""+object.getString("tipovehiculo"));
                                            etMarcaVehiculo.setText(""+object.getString("marcavehiculo"));
                                            etModeloVehiculo.setText(""+object.getString("modelovehiculo"));
                                            etNombreMarca.setText(""+object.getString("nombremarca"));
                                            etIndiceCarga.setText(""+object.getInt("indicecarga"));
                                            etIndiceVelocidad.setText(""+object.getString("indicevelocidad"));
                                            etConstruccion.setText(""+object.getString("construccion"));
                                            etPresionMaxima.setText(""+object.getString("presionmaxima"));
                                            etClasificacion.setText(""+object.getString("clasificacion"));
                                            etFechaFabricacion.setText(""+object.getString("fechafabricacion"));
                                            etAncho.setText(""+object.getInt("ancho"));
                                            etDiametro.setText(""+object.getInt("diametro"));
                                            etPerfil.setText(""+object.getInt("perfil"));
                                            etMmCocada.setText(""+object.getDouble("mmcocada"));

                                            int x = -1;
                                            do{
                                                x++;
                                            }
                                            while (!datosVehiculo.get(x).equalsIgnoreCase(object.getString("vehiculoid")));
                                            spVehiculoId.setSelection(x);

                                            int y = -1;
                                            do{
                                                y++;
                                            }while (!datosMedida.get(y).equalsIgnoreCase(object.getString("medidallantaid")));
                                            spMedidaLlantaId.setSelection(y);

                                            int z = -1;
                                            do{
                                                z++;
                                            }while (!datosDetalle.get(z).equalsIgnoreCase(object.getString("detallellantaid")));
                                            spDetalleLlantaId.setSelection(z);

                                            contar++;
                                        } while (contar <= array.length());
                                    }

                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(), "Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });//FIN SELECT-------------------------------------
            }
        });//FIN BOTON MODIFICAR -------------------------------------------------


        //Mostrar calendario*************************************************************************
        etFechaFabricacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new detalle_producto.DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });



        //CONFIRMAR CAMBIOS----------------------------------------------------------------------
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //--**ACTUALIZAR DATOS DE LA TABLA MEDIDA------------------------------------------------------------------------------::::
                Map<String,String> insertar = new HashMap<>();
                insertar.put("Ancho",etAncho.getText().toString());
                insertar.put("Diametro",etDiametro.getText().toString());
                insertar.put("Perfil",etPerfil.getText().toString());
                insertar.put("MmCocada",etMmCocada.getText().toString());
                insertar.put("MedidaLlantaId",MedidaLlantaId);
                JSONObject datosJSON = new JSONObject(insertar);

                //--**ACTUALIZAR DATOS DE LA TABLA VEHICULO------------------------------------------------------------------------------::::
                Map<String,String> insertar2 = new HashMap<>();
                insertar2.put("TipoVehiculo",etTipoVehiculo.getText().toString());
                insertar2.put("MarcaVehiculo",etMarcaVehiculo.getText().toString());
                insertar2.put("ModeloVehiculo",etModeloVehiculo.getText().toString());
                insertar2.put("VehiculoId",VehiculoId);
                JSONObject datosJSON2 = new JSONObject(insertar2);

                //--**ACTUALIZAR DATOS DE LA TABLA DETALLE------------------------------------------------------------------------------::::
                Map<String,String> insertar3 = new HashMap<>();
                insertar3.put("NombreMarca",etNombreMarca.getText().toString());
                insertar3.put("IndiceCarga",etIndiceCarga.getText().toString());
                insertar3.put("IndiceVelocidad",etIndiceVelocidad.getText().toString());
                insertar3.put("Construccion",etConstruccion.getText().toString());
                insertar3.put("PresionMaxima",etPresionMaxima.getText().toString());
                insertar3.put("Clasificacion",etClasificacion.getText().toString());
                insertar3.put("FechaFabricacion",etFechaFabricacion.getText().toString());
                insertar3.put("MedidaLlantaId",MedidaLlantaId);
                insertar3.put("DetalleLlantaId",DetalleLlantaId);
                JSONObject datosJSON3 = new JSONObject(insertar3);

                //--**ACTUALIZAR DATOS DE LA TABLA NEUMATICO------------------------------------------------------------------------------::::
                Map<String,String> insertar4 = new HashMap<>();
                insertar4.put("Precio",etPrecio.getText().toString());
                insertar4.put("Stock",etStock.getText().toString());
                insertar4.put("DetalleLlantaId",DetalleLlantaId);
                insertar4.put("VehiculoId",VehiculoId);
                insertar4.put("LlantaId",llantaId);
                JSONObject datosJSON4 = new JSONObject(insertar4);

                if(ValidarCampos()){

                    //--**ACTUALIZAR DATOS DE LA TABLA MEDIDA-----------------********************************----------------------------------------::::
                    AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_MedidaLlanta_POST_UPDATE.php")
                            .addJSONObjectBody(datosJSON)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        String validarDatos = response.getString("data");
                                        Log.e("respuesta actualizacion: ",""+validarDatos);

                                        tvAncho.setText(etAncho.getText().toString());
                                        tvDiametro.setText(etDiametro.getText().toString());
                                        tvPerfil.setText(etPerfil.getText().toString());
                                        tvMmCocada.setText(etMmCocada.getText().toString());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(getApplicationContext(),"Error:" + anError.getErrorDetail(),Toast.LENGTH_SHORT).show();
                                }
                            });//FIN DEL EVENTO DE HEROKU DB - UPDATE------------------------------------------------


                    //--**ACTUALIZAR DATOS DE LA TABLA VEHICULO-----------------********************************----------------------------------------::::
                    AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Vehiculo_POST_UPDATE.php")
                            .addJSONObjectBody(datosJSON2)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        String validarDatos = response.getString("data");
                                        Log.e("respuesta actualizacion: ",""+validarDatos);

                                        tvTipoVehiculo.setText(etTipoVehiculo.getText().toString());
                                        tvMarcaVehiculo.setText(etMarcaVehiculo.getText().toString());
                                        tvModeloVehiculo.setText(etModeloVehiculo.getText().toString());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(getApplicationContext(),"Error:" + anError.getErrorDetail(),Toast.LENGTH_SHORT).show();
                                }
                            });//FIN DEL EVENTO DE HEROKU DB - UPDATE------------------------------------------------


                    //--**ACTUALIZAR DATOS DE LA TABLA DETALLE -----------------********************************----------------------------------------::::
                    AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_DetalleLlanta_POST_UPDATE.php")
                            .addJSONObjectBody(datosJSON3)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        String validarDatos = response.getString("data");
                                        Log.e("respuesta actualizacion: ",""+validarDatos);

                                        tvNombreMarca.setText(etNombreMarca.getText().toString());
                                        tvIndiceCarga.setText(etIndiceCarga.getText().toString());
                                        tvIndiceVelocidad.setText(etIndiceVelocidad.getText().toString());
                                        tvConstruccion.setText(etConstruccion.getText().toString());
                                        tvPresionMaxima.setText(etPresionMaxima.getText().toString());
                                        tvClasificacion.setText(etClasificacion.getText().toString());
                                        tvFechaFabricacion.setText(etFechaFabricacion.getText().toString());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(getApplicationContext(),"Error:" + anError.getErrorDetail(),Toast.LENGTH_SHORT).show();
                                }
                            });//FIN DEL EVENTO DE HEROKU DB - UPDATE------------------------------------------------


                    //--**ACTUALIZAR DATOS DE LA TABLA NEUMATICO -----------------********************************----------------------------------------::::
                    AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Llanta_POST_UPDATE.php")
                            .addJSONObjectBody(datosJSON4)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        String validarDatos = response.getString("data");
                                        Log.e("respuesta actualizacion: ",""+validarDatos);

                                        tvPrecio.setText(etPrecio.getText().toString());
                                        tvStock.setText(etStock.getText().toString());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(getApplicationContext(),"Error:" + anError.getErrorDetail(),Toast.LENGTH_SHORT).show();
                                }
                            });//FIN DEL EVENTO DE HEROKU DB - UPDATE------------------------------------------------


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

                OcultarEditText();
                MostrarTextView();
                btnModificar.setVisibility(View.VISIBLE);
                btnConfirmar.setVisibility(View.GONE);
                llOpcionVehiculoId.setVisibility(View.GONE);
                llOpcionDetalleLlantaId.setVisibility(View.GONE);
                llOpcionMedidaLlantaId.setVisibility(View.GONE);

                //CAMBIO DE ESTADO y Mostrar TOAST
                opcionModificar = false;
                mensajeToast = "Producto Modificado";
                MostrarToast(mensajeToast);

            }
        });


        //BOTON MODIFICAR DEL LISTADO DE NEUMATICOS -  SEGUNDA ENTRADA *************************************
        Intent opcMod = getIntent();
        opcionModificar = opcMod.getBooleanExtra("estado",false);
        if(opcionModificar==true){
            estadoCancelar = 1;
            OcultarTextView();
            MostrarEditText();
            btnModificar.setVisibility(View.GONE);
            btnConfirmar.setVisibility(View.VISIBLE);
            llOpcionVehiculoId.setVisibility(View.VISIBLE);
            llOpcionDetalleLlantaId.setVisibility(View.VISIBLE);
            llOpcionMedidaLlantaId.setVisibility(View.VISIBLE);


            //--SELECT INFORMACION NEUMATICO EDIT TEXT-----------------------------------------------------------------------------
            Map<String,String> insertar5 = new HashMap<>();
            insertar5.put("LlantaId",llantaId);
            JSONObject datosJSON5 = new JSONObject(insertar5);

            AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/OP_Detalle_SELECT_INNER_WHERE.php")
                    .addJSONObjectBody(datosJSON5)
                    .setPriority(Priority.IMMEDIATE)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String validarDatos = response.getString("data");
                                int contar = 1;
                                Log.e("respuesta: ", "" + validarDatos);

                                //--VALIDAR ***********************************************************************************************************
                                if (validarDatos.equals("[]")) {
                                    Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray array = response.getJSONArray("data");
                                    do {
                                        JSONObject object = array.getJSONObject(contar - 1);

                                        //CARGAR DATOS A LOS EDIT TEXT--------------------------------------
                                        etStock.setText(""+object.getInt("stock"));
                                        etPrecio.setText(""+object.getString("precio"));
                                        etTipoVehiculo.setText(""+object.getString("tipovehiculo"));
                                        etMarcaVehiculo.setText(""+object.getString("marcavehiculo"));
                                        etModeloVehiculo.setText(""+object.getString("modelovehiculo"));
                                        etNombreMarca.setText(""+object.getString("nombremarca"));
                                        etIndiceCarga.setText(""+object.getInt("indicecarga"));
                                        etIndiceVelocidad.setText(""+object.getString("indicevelocidad"));
                                        etConstruccion.setText(""+object.getString("construccion"));
                                        etPresionMaxima.setText(""+object.getString("presionmaxima"));
                                        etClasificacion.setText(""+object.getString("clasificacion"));
                                        etFechaFabricacion.setText(""+object.getString("fechafabricacion"));
                                        etAncho.setText(""+object.getInt("ancho"));
                                        etDiametro.setText(""+object.getInt("diametro"));
                                        etPerfil.setText(""+object.getInt("perfil"));
                                        etMmCocada.setText(""+object.getDouble("mmcocada"));

                                        int x = -1;
                                        do{
                                            x++;
                                        }
                                        while (!datosVehiculo.get(x).equalsIgnoreCase(object.getString("vehiculoid")));
                                        spVehiculoId.setSelection(x);

                                        int y = -1;
                                        do{
                                            y++;
                                        }while (!datosMedida.get(y).equalsIgnoreCase(object.getString("medidallantaid")));
                                        spMedidaLlantaId.setSelection(y);

                                        int z = -1;
                                        do{
                                            z++;
                                        }while (!datosDetalle.get(z).equalsIgnoreCase(object.getString("detallellantaid")));
                                        spDetalleLlantaId.setSelection(z);

                                        contar++;
                                    } while (contar <= array.length());
                                }

                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(getApplicationContext(), "Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });//FIN SELECT-------------------------------------
        }



        //BOTON CANCELAR  *************************************--------------------------------:::::::::::::::::::::::::::::::::::
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(estadoCancelar == 1){
                    OcultarEditText();
                    MostrarTextView();
                    btnModificar.setVisibility(View.VISIBLE);
                    btnConfirmar.setVisibility(View.GONE);
                    llOpcionVehiculoId.setVisibility(View.GONE);
                    llOpcionDetalleLlantaId.setVisibility(View.GONE);
                    llOpcionMedidaLlantaId.setVisibility(View.GONE);
                    estadoCancelar = 0;
                }else{
                    Intent intent = new Intent(detalle_producto.this,Catalogo.class);
                    startActivity(intent);

                    //QUITAR ANIMACION DE CARGA DE VISTA*****************************
                    overridePendingTransition(0, 0);
                    overridePendingTransition(0, 0);
                }

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            Intent intent = new Intent(detalle_producto.this, Catalogo.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    //**MENSAJE DE CONFIRMACION*************************************************************************************
    public void MostrarToast(String mensaje){
        Toast toast = Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_SHORT);
        View vista = toast.getView();
        vista.setBackgroundResource(R.drawable.estilo_color_x);
        toast.setGravity(Gravity.CENTER,0,0);
        TextView text = (TextView) vista.findViewById(android.R.id.message);
        text.setTextColor(Color.parseColor("#FFFFFF"));
        text.setTextSize(20);
        toast.show();
    }

    //OCULTAR EDIT TEXT DE LA PANTALLA DETALLE PRODUCTO
    public void OcultarEditText(){
        etNombreMarca.setVisibility(View.GONE);
        etIndiceCarga.setVisibility(View.GONE);
        etIndiceVelocidad.setVisibility(View.GONE);
        etConstruccion.setVisibility(View.GONE);
        etClasificacion.setVisibility(View.GONE);
        etFechaFabricacion.setVisibility(View.GONE);
        etMarcaVehiculo.setVisibility(View.GONE);
        etModeloVehiculo.setVisibility(View.GONE);
        etAncho.setVisibility(View.GONE);
        etDiametro.setVisibility(View.GONE);
        etPerfil.setVisibility(View.GONE);
        etMmCocada.setVisibility(View.GONE);
        etPresionMaxima.setVisibility(View.GONE);
        etStock.setVisibility(View.GONE);
        etPrecio.setVisibility(View.GONE);
        etTipoVehiculo.setVisibility(View.GONE);
    }

    public void OcultarTextView(){
        tvNombreMarca.setVisibility(View.GONE);
        tvIndiceCarga.setVisibility(View.GONE);
        tvIndiceVelocidad.setVisibility(View.GONE);
        tvConstruccion.setVisibility(View.GONE);
        tvClasificacion.setVisibility(View.GONE);
        tvFechaFabricacion.setVisibility(View.GONE);
        tvMarcaVehiculo.setVisibility(View.GONE);
        tvModeloVehiculo.setVisibility(View.GONE);
        tvAncho.setVisibility(View.GONE);
        tvDiametro.setVisibility(View.GONE);
        tvPerfil.setVisibility(View.GONE);
        tvMmCocada.setVisibility(View.GONE);
        tvPresionMaxima.setVisibility(View.GONE);
        tvStock.setVisibility(View.GONE);
        tvPrecio.setVisibility(View.GONE);
        tvTipoVehiculo.setVisibility(View.GONE);
    }

    public void MostrarEditText(){
        etNombreMarca.setVisibility(View.VISIBLE);
        etIndiceCarga.setVisibility(View.VISIBLE);
        etIndiceVelocidad.setVisibility(View.VISIBLE);
        etConstruccion.setVisibility(View.VISIBLE);
        etClasificacion.setVisibility(View.VISIBLE);
        etFechaFabricacion.setVisibility(View.VISIBLE);
        etMarcaVehiculo.setVisibility(View.VISIBLE);
        etModeloVehiculo.setVisibility(View.VISIBLE);
        etAncho.setVisibility(View.VISIBLE);
        etDiametro.setVisibility(View.VISIBLE);
        etPerfil.setVisibility(View.VISIBLE);
        etMmCocada.setVisibility(View.VISIBLE);
        etPresionMaxima.setVisibility(View.VISIBLE);
        etStock.setVisibility(View.VISIBLE);
        etPrecio.setVisibility(View.VISIBLE);
        etTipoVehiculo.setVisibility(View.VISIBLE);
    }

    public void MostrarTextView(){
        tvNombreMarca.setVisibility(View.VISIBLE);
        tvIndiceCarga.setVisibility(View.VISIBLE);
        tvIndiceVelocidad.setVisibility(View.VISIBLE);
        tvConstruccion.setVisibility(View.VISIBLE);
        tvClasificacion.setVisibility(View.VISIBLE);
        tvFechaFabricacion.setVisibility(View.VISIBLE);
        tvMarcaVehiculo.setVisibility(View.VISIBLE);
        tvModeloVehiculo.setVisibility(View.VISIBLE);
        tvAncho.setVisibility(View.VISIBLE);
        tvDiametro.setVisibility(View.VISIBLE);
        tvPerfil.setVisibility(View.VISIBLE);
        tvMmCocada.setVisibility(View.VISIBLE);
        tvPresionMaxima.setVisibility(View.VISIBLE);
        tvStock.setVisibility(View.VISIBLE);
        tvPrecio.setVisibility(View.VISIBLE);
        tvTipoVehiculo.setVisibility(View.VISIBLE);
    }


    ///BLOQUE FECHA DINAMICA**************************************************
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(),this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            dia = i2;
            mes = i1+1;
            año = i;
            if(dia<10 && mes<10){
                etFechaFabricacion.setText("0"+dia+"/"+"0"+mes+"/"+año);
            }else if(dia<10 && mes>9){
                etFechaFabricacion.setText("0"+dia+"/"+mes+"/"+año);
            }else if(dia>9 && mes<10){
                etFechaFabricacion.setText(dia+"/"+"0"+mes+"/"+año);
            }else{
                etFechaFabricacion.setText(dia+"/"+mes+"/"+año);
            }

        }
    }

    public boolean ValidarCampos(){
        return !etStock.getText().toString().trim().isEmpty() && !etPrecio.getText().toString().trim().isEmpty() && !etTipoVehiculo.getText().toString().trim().isEmpty() &&
                !etMarcaVehiculo.getText().toString().trim().isEmpty() && !etModeloVehiculo.getText().toString().trim().isEmpty() && !etNombreMarca.getText().toString().trim().isEmpty() &&
                !etIndiceCarga.getText().toString().trim().isEmpty() && !etIndiceVelocidad.getText().toString().trim().isEmpty() && !etConstruccion.getText().toString().trim().isEmpty() &&
                !etPresionMaxima.getText().toString().trim().isEmpty() && !etClasificacion.getText().toString().trim().isEmpty() && !etFechaFabricacion.getText().toString().trim().isEmpty() &&
                !etAncho.getText().toString().trim().isEmpty() && !etDiametro.getText().toString().trim().isEmpty() && !etPerfil.getText().toString().trim().isEmpty() &&
                !etMmCocada.getText().toString().trim().isEmpty();
    }

}