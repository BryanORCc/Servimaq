package com.example.servimaq.op_pedidos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.pedido_lista;
import com.example.servimaq.fragments_registros.detalle_llanta;

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

public class detalle_pedido extends AppCompatActivity {
    public String opcion="";
    boolean opcionModificar = false;
    String mensajeToast = null;
    Context c;
    EditText nombrecliente,apellidocliente,correo,dni,FechaActual;
    TextView tvNombre,tvApellido,tvCorreo,tvfechaActual,tvFechapago,modopago,tvdni;


    Button  btnConfirmarP;
    Spinner spinner1;
    ArrayList<String> opciones = new ArrayList<>();
    ArrayList<String> CodigoPed = new ArrayList<>();
    Spinner codigoPedido;
    String pedidoId, modo;


    public static EditText FechadePago;;
    public static int dia, mes, año;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);


        AndroidNetworking.initialize(getApplicationContext());

        ///////EditText*******
        nombrecliente=findViewById(R.id.EtNombreCliente);
        apellidocliente=findViewById(R.id.EtApellidoCliente);
        correo=findViewById(R.id.EtCorreo);
        FechaActual=findViewById(R.id.EtFechaActual);
        FechadePago = (EditText) findViewById(R.id.EtFechaEntrega);
        dni=findViewById(R.id.EtDni);
        ///////////////////
        btnConfirmarP=findViewById(R.id.btnConfirmarP);
        spinner1=findViewById(R.id.Spi_Modo_Pago);
        codigoPedido=findViewById(R.id.Spi_codigoPedido);

        /////////////textview******
        tvNombre=findViewById(R.id.tvNombre);
        tvApellido=findViewById(R.id.tvApellidos);
        tvCorreo=findViewById(R.id.tvDCorreo);
        tvfechaActual=findViewById(R.id.tvFechaA);
        tvFechapago=findViewById(R.id.tvFechaE);
        modopago=findViewById(R.id.tvModoP);
        tvdni=findViewById(R.id.tvDni);
        ////////////////////////////////

        ///////spinner*****

        opciones.add("Deposito");
        opciones.add("Efectivo");
        opciones.add("Plazos");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, opciones);
        spinner1.setAdapter(adapter);

        Intent datos = getIntent();
        pedidoId = datos.getStringExtra("codigo");
        modo = datos.getStringExtra("modo");

        //INICIAR EN LA POSICION SELECCIONADA EN LA EDICION----------------------
        int x = -1;
        do{
            x++;
        }while (!opciones.get(x).equalsIgnoreCase(modo));
        spinner1.setSelection(x);


        //captura del dato del spinner////
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);

                opcion = spinner1.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ///carga de Datos inicial -----------------------------------*******************-----------------------------------------------------------
        Map<String,String> insertar = new HashMap<>();
        insertar.put("codPedido",pedidoId);
        JSONObject datosJSON = new JSONObject(insertar);

        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Pedido_POST_SELECT_ALL_WHERE.php")
                .addJSONObjectBody(datosJSON)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String validarDatos = response.getString("data");
                            int contar = 1;

                            if (validarDatos.equals("[]")) {
                                Toast.makeText(getApplicationContext(), "No se encontraron pedidos", Toast.LENGTH_SHORT).show();
                            } else {

                                JSONArray array = response.getJSONArray("data");
                                do {
                                    JSONObject object = array.getJSONObject(contar - 1);

                                    //CARGAR DATOS AL EDIT TEXT---------------------------------------------------
                                    nombrecliente.setText(object.getString("nombrescliente"));
                                    apellidocliente.setText(object.getString("apellidoscliente"));
                                    correo.setText(object.getString("correo"));
                                    FechaActual.setText(object.getString("fechaactual"));
                                    FechadePago.setText(object.getString("fechaentrega"));
                                    dni.setText(""+object.getInt("dni"));

                                    contar++;
                                } while (contar <= array.length());

                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(),"Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });//FIN SELECT BUSQUEDA - HEROKU ------------------------


        //CARGA DE los ID EN EL SPINNER -------------------------------****************************--------------------------------------------------
        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Pedido_POST_SELECT.php")
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
                                    //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                                    JSONObject object = array.getJSONObject(contar-1);
                                    CodigoPed.add(object.getString("codpedido"));
                                    contar++;
                                } while (contar <= array.length());

                                try {
                                    ArrayAdapter adaptador = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, CodigoPed);
                                    codigoPedido.setAdapter(adaptador);

                                    int z = -1;
                                    do{
                                        z++;
                                    }while (!CodigoPed.get(z).equalsIgnoreCase(pedidoId));
                                    codigoPedido.setSelection(z);

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
                        Toast.makeText(c,"Error: "+anError.toString(),Toast.LENGTH_SHORT).show();
                    }
                });//FIN Carga--------------------------------------------------------------------------------



        //SELECCION DE OPCION - SPINNER DETALLE :::::::::::::::*******::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        codigoPedido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) adapterView.getChildAt(0)).setBackgroundColor(Color.parseColor("#835E5A5A"));
                ((TextView) adapterView.getChildAt(0)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                pedidoId=CodigoPed.get(i);

                //--CARGAR DATOS AL SPINNER DE PEDIDOS------------------------------------------------------------------------------
                Map<String,String> insertar = new HashMap<>();
                insertar.put("codPedido",pedidoId);
                JSONObject datosJSON = new JSONObject(insertar);

                //CARGAR DATOS SELECCIONADOS POR EL SPINNER
                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Pedido_POST_SELECT_ALL_WHERE.php")
                        .addJSONObjectBody(datosJSON)
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
                                            //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                                            JSONObject object = array.getJSONObject(contar-1);

                                            nombrecliente.setText(object.getString("nombrescliente"));
                                            apellidocliente.setText(object.getString("apellidoscliente"));
                                            correo.setText(object.getString("correo"));
                                            FechaActual.setText(object.getString("fechaactual"));
                                            FechadePago.setText(object.getString("fechaentrega"));
                                            dni.setText(object.getString("dni"));

                                            contar++;
                                        } while (contar <= array.length());

                                    }else{
                                        Toast.makeText(c,"No se encontraron pedidos",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(),"Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });//FIN SELECT----------------------------
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); //FIN SELECT DE OPCION - SPINNER ------------------------------------------*******


        /////////Calendario dinamico******
        FechadePago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });


        //CONFIRMAR CAMBIO DE DATOS ---------------------------------------********************-------------------------------
        btnConfirmarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AndroidNetworking.initialize(getApplicationContext());

                //--**ACTUALIZAR DATOS DE LA TABLA MEDIDA------------------------------------------------------------------------------::::
                Map<String,String> insertar = new HashMap<>();
                insertar.put("NombresCliente",nombrecliente.getText().toString());
                insertar.put("ApellidosCliente",apellidocliente.getText().toString());
                insertar.put("Correo",correo.getText().toString());
                insertar.put("FechaEntrega",FechadePago.getText().toString());
                insertar.put("ModoPago",opcion);
                insertar.put("DNI",dni.getText().toString());
                insertar.put("codPedido",pedidoId);
                JSONObject datosJSON = new JSONObject(insertar);


                //--**ACTUALIZAR DATOS DE LA TABLA PEDIDO------------------------------------------------------------------------------::::::::::::::::::::::::::::
                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Pedido_POST_UPDATE.php")
                        .addJSONObjectBody(datosJSON)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String validarDatos = response.getString("data");
                                    Log.e("respuesta actualizacion: ",""+validarDatos);

                                    Log.e("nombre: ",""+nombrecliente.getText().toString());
                                    /*tvNombre.setText(nombrecliente.getText().toString());
                                    tvApellido.setText(apellidocliente.getText().toString());
                                    tvCorreo.setText(correo.getText().toString());
                                    tvfechaActual.setText(FechaActual.getText().toString());
                                    tvFechapago.setText(FechadePago.getText().toString());
                                    tvdni.setText(dni.getText().toString());*/

                                    opcionModificar = false;
                                    mensajeToast = "Pedido Modificado";
                                    MostrarToast(mensajeToast);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(),"Error:" + anError.getErrorDetail(),Toast.LENGTH_SHORT).show();
                            }
                        });//FIN DEL EVENTO DE HEROKU DB - UPDATE------------------------------------------------
            }
        }); //FIN DEL BOTON CONFIRMAR ACTUALIZACION ------------------------------

    }

    ///////////////////////////*********
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
                FechadePago.setText("0"+año+"/"+"0"+mes+"/"+dia);
            }else if(dia<10 && mes>9){
                FechadePago.setText("0"+año+"/"+mes+"/"+dia);
            }else if(dia>9 && mes<10){
                FechadePago.setText(año+"/"+"0"+mes+"/"+dia);
            }else{
                FechadePago.setText(año+"/"+mes+"/"+dia);
            }

        }

    }
}