package com.example.servimaq.fragments_registros;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.servimaq.menu_opciones;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class detalle_llanta extends Fragment {
    ScrollView svScroll;
    ImageView ivFotoLlanta;
    Button BtnFotoLlanta,btnRegistrar, btnCancelar;
    Spinner spinner1, spnNombreMarca, spnIndiceCarga, spnIndiceVelocidad, spnConstruccion, spnPresionMaxima, spnClasificacion;
    View vista;
    String ruta;
    TextView Tv_cargar;
    String medidas;
    ArrayList<String> opciones = new ArrayList<>();
    ArrayList<String> informacion = new ArrayList<>();
    String MedidaLlantaId="", NombreMarca, IndiceCarga, IndiceVelocidad, Construccion, PresionMaxima, Clasificacion;
    public static EditText Fecha;
    public static int dia, mes, año;

    private StorageReference mStorage;
    StorageReference FilePath;
    Uri uri;

    String[] nMarca = {"Pirelli","Bridgestone","Firestone","GoodYear","Yokohama","Michelin","Continental"};
    String[] ic = {"290","300","307","315","325","335","345","355","365","375","387","400","412","425","437","450","462","475","487","500","515","530","545","560","580","600",
            "615","630","650","670","690","710","730","750","775","800","825","850","875", "900","925","950","975","1000","1030","1060","1090","1120","1150","1180", "1215",
            "1250","1285","1320","1360","1400","1450","1500","1550","1600", "1650","1700","1750","1800","1850"};
    String[] iv = {"D = 65 km/h","E = 70 km/h","F = 80 km/h","G = 90 km/h","J = 100 km/h","K = 110 km/h","L = 120 km/h","M = 130 km/h","N = 140 km/h","P = 150 km/h",
            "Q = 160 km/h","R = 170 km/h","S = 180 km/h","T = 190 km/h","U = 200 km/h","H = 210 km/h","V = 240 km/h","ZR > 240 km/h","W = 270 km/h","Y = 300 km/h"};
    String[] constru = {"Diagonal","Solida","Radial"};
    String[] pm = {"25 psi","26 psi","27 psi","28 psi","29 psi","30 psi","31 psi","32 psi","33 psi","34 psi","35 psi","36 psi","37 psi","38 psi","39 psi","40 psi","41 psi",
            "42 psi","43 psi","44 psi","45 psi","46 psi","47 psi","48 psi","49 psi","50 psi","51 psi","52 psi","53 psi","54 psi","55 psi","56 psi","57 psi","58 psi","59 psi",
            "60 psi","61 psi","62 psi","63 psi","64 psi","65 psi","66 psi","67 psi","68 psi","69 psi","70 psi"};
    String[] clasif = {"HT = TERRENO ASFALTADO","AT = TODO TERRENO","MT = TERRENO PANTANOSO"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_detalle_llanta, container, false);

        //STORAGE FIREBASE********************************************************
        mStorage = FirebaseStorage.getInstance().getReference();

        AndroidNetworking.initialize(getContext());

        svScroll = vista.findViewById(R.id.svScroll);
        spnNombreMarca = vista.findViewById(R.id.spnNombreMarca);
        spnIndiceCarga = vista.findViewById(R.id.spnIndiceCarga);
        spnIndiceVelocidad = vista.findViewById(R.id.spnIndiceVelocidad);
        spnConstruccion = vista.findViewById(R.id.spnConstruccion);
        spnPresionMaxima = vista.findViewById(R.id.spnPresionMaxima);
        spnClasificacion = vista.findViewById(R.id.spnClasificacion);
        Fecha = vista.findViewById(R.id.EtFechaFabricacion);
        ivFotoLlanta = vista.findViewById(R.id.ivFoto_Llanta);
        btnRegistrar = vista.findViewById(R.id.btnRegistrar);
        btnCancelar = vista.findViewById(R.id.btnCancelar);
        Tv_cargar=vista.findViewById(R.id.Tv_cargar);
        spinner1=vista.findViewById(R.id.Spi_MedidaLlanta);
        BtnFotoLlanta=vista.findViewById(R.id.btnFoto_Llanta);

        //CARGAR DATO DEL SPINNER NOMBRE MARCA*************************************************************************************************************************
        ArrayAdapter adapter_nMarca = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,nMarca);
        spnNombreMarca.setAdapter(adapter_nMarca);

        spnNombreMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.parseColor("#835E5A5A"));
                ((TextView) parent.getChildAt(0)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                NombreMarca = nMarca[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //CARGAR DATO DEL SPINNER INDICE DE CARGA****************************************************************************************************************************
        ArrayAdapter adapter_ic = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, ic);
        spnIndiceCarga.setAdapter(adapter_ic);

        spnIndiceCarga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.parseColor("#835E5A5A"));
                ((TextView) parent.getChildAt(0)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                IndiceCarga = ic[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //CARGAR DATO DEL SPINNER INDICE DE VELOCIDAD*************************************************************************************************************************
        ArrayAdapter adapter_iv = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,iv);
        spnIndiceVelocidad.setAdapter(adapter_iv);

        spnIndiceVelocidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.parseColor("#835E5A5A"));
                ((TextView) parent.getChildAt(0)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                IndiceVelocidad = iv[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //CARGAR DATO DEL SPINNER CONSTRUCCION****************************************************************************************************************************
        ArrayAdapter adapter_constru = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, constru);
        spnConstruccion.setAdapter(adapter_constru);

        spnConstruccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.parseColor("#835E5A5A"));
                ((TextView) parent.getChildAt(0)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                Construccion = constru[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //CARGAR DATO DEL SPINNER PRESION MAXIMA****************************************************************************************************************************
        ArrayAdapter adapter_pm = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, pm);
        spnPresionMaxima.setAdapter(adapter_pm);

        spnPresionMaxima.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.parseColor("#835E5A5A"));
                ((TextView) parent.getChildAt(0)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                PresionMaxima = pm[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //CARGAR DATO DEL SPINNER CLASIFICACION ****************************************************************************************************************************
        ArrayAdapter adapter_clasif = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, clasif);
        spnClasificacion.setAdapter(adapter_clasif);

        spnClasificacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.parseColor("#835E5A5A"));
                ((TextView) parent.getChildAt(0)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                Clasificacion = clasif[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //CARGAR DATO DEL SPINNER XD*************************************************************************************************************************
        //EVENTO DEL HEROKU - DB SELECCION MEDIDAS************************************************************************************************************
        AndroidNetworking.initialize(getContext());
        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_MedidaLlanta_POST_SELECT_ALL.php")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String validarDatos = response.getString("data");
                            String aux_MedidaLlantaId = "";
                            int contar = 1;

                            Log.e("respuesta: ", "" + validarDatos);

                            if (!validarDatos.equals("[]")) {
                                JSONArray array = response.getJSONArray("data");
                                do {
                                    JSONObject object = array.getJSONObject(contar-1);
                                    Log.e("Recorrido: ", "aca llegue");
                                    aux_MedidaLlantaId = object.getString("medidallantaid");
                                    Log.e("id capturado::: ", "" + aux_MedidaLlantaId);
                                    opciones.add(aux_MedidaLlantaId);
                                    medidas = "→ Ancho: "+object.getInt("ancho")+" \n→ Diametro: "+object.getInt("diametro")+" \n→ Perfil: "+object.getInt("perfil")+"\n→ Milimetro-Cocada: "+object.getInt("mmcocada");
                                    informacion.add(medidas);
                                    contar++;
                                } while (contar <= array.length());


                                try {
                                    ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, opciones);
                                    spinner1.setAdapter(adapter);
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

        //SELECCION DE MEDIDA EN EL SPINNER ************************************************************************************************************
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) adapterView.getChildAt(0)).setBackgroundColor(Color.parseColor("#835E5A5A"));

                Tv_cargar.setText(informacion.get(i));
                MedidaLlantaId=opciones.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //SELECCION DE IMAGEN  **************************************************************************************************************************
        BtnFotoLlanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarImagen();
            }
        });


        //BOTON REGISTRO **************************************************************************************************************************
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fech=Fecha.getText().toString();

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

                                insertar.put("FotoLlanta",ruta);
                                insertar.put("NombreMarca",NombreMarca);
                                insertar.put("IndiceCarga",IndiceCarga);
                                insertar.put("IndiceVelocidad",IndiceVelocidad);
                                insertar.put("Construccion",Construccion);
                                insertar.put("PresionMaxima",PresionMaxima);
                                insertar.put("Clasificacion",Clasificacion);
                                insertar.put("MedidaLlantaId",MedidaLlantaId);


                                //EVENTO DEL HEROKU DB SELECCION***********************************************************************************************************
                                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_DetalleLlanta_POST_SELECT.php")
                                        .setPriority(Priority.IMMEDIATE)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                                String DetalleLlantaId="", aux_DetalleLlantaId="";
                                                int contar= 1, extraer = 0;

                                                try {
                                                    String validarDatos = response.getString("data");
                                                    int pos = 1;
                                                    Log.e("respuesta: ",""+validarDatos);
                                                    //--VALIDAR LOGIN***********************************************************************************************************
                                                    if(validarDatos.equals("[]")){
                                                        DetalleLlantaId = "DN01";
                                                    }else{
                                                        JSONArray array = response.getJSONArray("data");
                                                        do {
                                                            JSONObject object = array.getJSONObject(contar-1);
                                                            aux_DetalleLlantaId = object.getString("detallellantaid");
                                                            //Log.e("id capturado: ",""+aux_DetalleLlantaId);
                                                            extraer = Integer.parseInt(aux_DetalleLlantaId.substring(aux_DetalleLlantaId.length()-pos));

                                                            if(extraer!=contar && contar<=9 && contar>=1 && extraer!=0){
                                                                DetalleLlantaId = "DN0"+contar;
                                                                break;
                                                            }else if(extraer==0){
                                                                pos = 2;
                                                                extraer = Integer.parseInt(aux_DetalleLlantaId.substring(aux_DetalleLlantaId.length()-pos));
                                                                if(extraer!=contar && contar<=99 && contar>=10){
                                                                    DetalleLlantaId = "DN"+contar;
                                                                    break;
                                                                }
                                                            }else if(extraer!=contar && contar<=99 && contar>=10){
                                                                DetalleLlantaId = "DN"+contar;
                                                                break;
                                                            }
                                                            contar++;
                                                        } while (contar <= array.length());

                                                        if(contar<=9 && extraer!=contar){
                                                            DetalleLlantaId = "DN0"+contar;
                                                        }else if(contar>=10 && contar<=99 && extraer!=contar){
                                                            DetalleLlantaId = "DN"+contar;
                                                        }


                                                    }//FIN IF VALIDACION REGISTROS----------------------------------------------------------------

                                                    insertar.put("DetalleLlantaId",DetalleLlantaId);
                                                    insertar.put("FechaFabricacion",fech);

                                                    JSONObject datosJSON = new JSONObject(insertar);
                                                    //EVENTO DEL HEROKU DB INSERCION***********************************************************************************************************
                                                    AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_DetalleLlanta_POST_INSERT.php")
                                                            .addJSONObjectBody(datosJSON)
                                                            .setPriority(Priority.MEDIUM)
                                                            .build()
                                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {

                                                                    try {
                                                                        String validarDatos = response.getString("data");
                                                                        Log.e("respuesta insercion: ",""+validarDatos);

                                                                        //EstiloToast(getContext(),"Registro de Detalle de Neumatico Exitoso");
                                                                        Toast.makeText(getContext(),"Registro de Detalle de Neumatico Exitoso",Toast.LENGTH_SHORT).show();
                                                                        svScroll.fullScroll(ScrollView.FOCUS_UP);
                                                                        Limpiar();

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

                        Log.e("direccion sin imagen: ","++"+ruta);
                        if(ruta==null){
                            ruta = "";
                        }
                        insertar.put("FotoLlanta",ruta);
                        insertar.put("NombreMarca",NombreMarca);
                        insertar.put("IndiceCarga",IndiceCarga);
                        insertar.put("IndiceVelocidad",IndiceVelocidad);
                        insertar.put("Construccion",Construccion);
                        insertar.put("PresionMaxima",PresionMaxima);
                        insertar.put("Clasificacion",Clasificacion);
                        insertar.put("MedidaLlantaId",MedidaLlantaId);

                        //EVENTO DEL HEROKU DB SELECCION************************************************************************************************************
                        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_DetalleLlanta_POST_SELECT.php")
                                .setPriority(Priority.IMMEDIATE)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        String DetalleLlantaId="", aux_DetalleLlantaId="";
                                        int contar= 1, extraer = 0;

                                        try {
                                            String validarDatos = response.getString("data");
                                            int pos = 1;
                                            Log.e("respuesta: ",""+validarDatos);
                                            //--VALIDAR LOGIN***********************************************************************************************************
                                            if(validarDatos.equals("[]")){
                                                DetalleLlantaId = "DN01";
                                            }else{
                                                JSONArray array = response.getJSONArray("data");
                                                do {
                                                    JSONObject object = array.getJSONObject(contar-1);
                                                    aux_DetalleLlantaId = object.getString("detallellantaid");
                                                    Log.e("id capturado: ",""+aux_DetalleLlantaId);
                                                    extraer = Integer.parseInt(aux_DetalleLlantaId.substring(aux_DetalleLlantaId.length()-pos));

                                                    if(extraer!=contar && contar<=9 && contar>=1 && extraer!=0){
                                                        DetalleLlantaId = "DN0"+contar;
                                                        break;
                                                    }else if(extraer==0){
                                                        pos = 2;
                                                        extraer = Integer.parseInt(aux_DetalleLlantaId.substring(aux_DetalleLlantaId.length()-pos));
                                                        if(extraer!=contar && contar<=99 && contar>=10){
                                                            DetalleLlantaId = "DN"+contar;
                                                            break;
                                                        }
                                                    }else if(extraer!=contar && contar<=99 && contar>=10){
                                                        DetalleLlantaId = "DN"+contar;
                                                        break;
                                                    }
                                                    contar++;
                                                } while (contar <= array.length());

                                                Log.e("contar ","__ "+contar);
                                                if(contar<=9 && extraer!=contar){
                                                    DetalleLlantaId = "DN0"+contar;
                                                }else if(contar>=10 && contar<=99 && extraer!=contar){
                                                    DetalleLlantaId = "DN"+contar;
                                                }

                                            }//FIN IF VALIDACION REGISTROS----------------------------------------------------------------

                                            insertar.put("DetalleLlantaId",DetalleLlantaId);
                                            insertar.put("FechaFabricacion",fech);

                                            JSONObject datosJSON = new JSONObject(insertar);
                                            //EVENTO DEL HEROKU DB INSERCION***********************************************************************************************************
                                            AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_DetalleLlanta_POST_INSERT.php")
                                                    .addJSONObjectBody(datosJSON)
                                                    .setPriority(Priority.MEDIUM)
                                                    .build()
                                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {

                                                            try {
                                                                String validarDatos = response.getString("data");
                                                                Log.e("respuesta insercion: ",""+validarDatos);

                                                                //EstiloToast(getContext(),"Registro de Detalle de Neumatico Exitoso");
                                                                Toast.makeText(getContext(),"Registro de Detalle de Neumatico Exitoso",Toast.LENGTH_SHORT).show();
                                                                svScroll.fullScroll(ScrollView.FOCUS_UP);
                                                                Limpiar();

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

                                });//EVENTO DEL HEROKU DB SELECCION------------------------------------------------
                    }

                }else{
                    String alerta = "Debe llenar todos los campos";
                    if(MedidaLlantaId.equals("")){
                        alerta = "No tiene medidas registradas";
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

        //BOTON CANCELAR **************************************************************************************************************************
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Limpiar();
            }
        });


        //Mostrar calendario **************************************************************************************************************************
        Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        return vista;
    }

    ///BLOQUE FECHA DINAMICA******************************************************************************************************************************
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
                Fecha.setText("0"+dia+"/"+"0"+mes+"/"+año);
            }else if(dia<10 && mes>9){
                Fecha.setText("0"+dia+"/"+mes+"/"+año);
            }else if(dia>9 && mes<10){
                Fecha.setText(dia+"/"+"0"+mes+"/"+año);
            }else{
                Fecha.setText(dia+"/"+mes+"/"+año);
            }

        }
    }

    //CARGA DE IMAGEN **************************************************************************************************************************
    private void CargarImagen(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i.createChooser(i,"seleccione la aplicación"),10);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode==getActivity().RESULT_OK){
            uri = data.getData();
            FilePath = mStorage.child("fotos").child(uri.getLastPathSegment());
            ivFotoLlanta.setImageURI(uri);
        }
    }


    //LIMPIAR CAMPOS **************************************************************************************************************************
    public void Limpiar(){
        ivFotoLlanta.setImageDrawable(getResources().getDrawable(R.drawable.no_imagen));
        Fecha.setText("");;
        ruta = null;
        uri = null;
    }

    public boolean ValidarCampos(){
        return !Fecha.getText().toString().trim().isEmpty() && !MedidaLlantaId.equals("");
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