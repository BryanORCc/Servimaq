package com.example.servimaq.op_pedidos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class registro_cliente extends AppCompatActivity {

    ScrollView svScroll;
    public String opcion="";
    Context c;
    EditText nombrecliente,apellidocliente,correo,dni,FechaActual;
    Button btnRegistrar, btnCancelar;
    Spinner spinner1;
    ArrayList<String> opciones = new ArrayList<>();
    public static EditText Fecha;
    public static int dia, mes, año;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);

        //BOTON "ATRAS" EN LA BARRA DE NAVEGACION****************
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        svScroll= findViewById(R.id.svScroll);
        nombrecliente=(EditText) findViewById(R.id.EtNombreCliente);
        apellidocliente=(EditText) findViewById(R.id.EtApellidoCliente);
        correo=(EditText) findViewById(R.id.EtCorreo);
        FechaActual=(EditText) findViewById(R.id.EtFechaActual);
        dni=(EditText) findViewById(R.id.EtDni);
        btnRegistrar=(Button) findViewById(R.id.btnRegistrarCliente);
        btnCancelar=(Button) findViewById(R.id.btnCancelarCliente);
        spinner1=(Spinner) findViewById(R.id.Spi_Modo_Pago);
        Fecha = (EditText) findViewById(R.id.EtFechaEntrega);

        ///////Spinner//////
        opciones.add("Deposito");
        opciones.add("Efectivo");
        opciones.add("Plazos");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, opciones);
        spinner1.setAdapter(adapter);
       //////cargar fecha actual --------
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        FechaActual.setText(date);
        ///////////////////////////////

        ///captura del dato del spinner////
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setBackgroundColor(Color.parseColor("#835E5A5A"));
                ((TextView) parent.getChildAt(0)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                opcion = spinner1.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //BOTON REGISTRO -----------------------------------------------------------------------------------------
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  nombreC = nombrecliente.getText().toString(),
                        apellidoC = apellidocliente.getText().toString(),
                        corre = correo.getText().toString(),
                        FechaAct = FechaActual.getText().toString(),
                        FechaEntr=Fecha.getText().toString(),
                        documento=dni.getText().toString();

                if(ValidarCampos()){

                    Map<String,String> insertar = new HashMap<>();

                    //EVENTO DEL HEROKU DB SELECCION************************************************************************************************************
                    AndroidNetworking.initialize(getApplicationContext());
                    AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Pedido_POST_SELECT.php")
                            .setPriority(Priority.IMMEDIATE)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    String codPedido = "", aux_codPedido = "";
                                    int contar = 1, extraer = 0;

                                    try {
                                        String validarDatos = response.getString("data");
                                        int pos = 1;
                                        Log.e("respuesta: ", "" + validarDatos);
                                        //--VALIDAR LOGIN********************************************************************************************************
                                        if (validarDatos.equals("[]")) {
                                            codPedido = "COD-001";
                                        } else {
                                            JSONArray array = response.getJSONArray("data");
                                            do {
                                                JSONObject object = array.getJSONObject(contar-1);
                                                Log.e("Recorrido: ", "aca llegue");
                                                aux_codPedido = object.getString("codpedido");
                                                Log.e("id capturado::: ", "" + aux_codPedido);
                                                extraer = Integer.parseInt(aux_codPedido.substring(aux_codPedido.length() - pos));

                                                if (extraer != contar && contar <= 9 && contar >= 1) {
                                                    codPedido = "COD-00" + contar;
                                                    break;
                                                }else if(extraer==0){
                                                    pos = 2;
                                                    extraer = Integer.parseInt(aux_codPedido.substring(aux_codPedido.length()-pos));
                                                    if(extraer!=contar && contar<=99 && contar>=10){
                                                        codPedido = "COD-0"+contar;
                                                        break;
                                                    }
                                                }else if(extraer!=contar && contar<=99 && contar>=10){
                                                    codPedido = "COD-0"+contar;
                                                    break;
                                                }
                                                contar++;
                                            } while (contar <= array.length());

                                            if (contar <= 9 && extraer != contar) {
                                                codPedido = "COD-00" + contar;
                                            } else if (contar >= 10 && contar <= 99 && extraer != contar) {
                                                codPedido = "COD-0" + contar;
                                            }

                                            Log.e("id a insertar: ", "" + codPedido);
                                        }

                                        insertar.put("codPedido", codPedido);
                                        insertar.put("NombresCliente", nombreC);
                                        insertar.put("ApellidosCliente", apellidoC);
                                        insertar.put("Correo", corre);
                                        insertar.put("FechaActual", FechaAct);
                                        insertar.put("FechaEntrega", FechaEntr);
                                        insertar.put("ModoPago", opcion);
                                        insertar.put("DNI", documento);

                                        Log.e("opcion: ",opcion);

                                        JSONObject datosJSON = new JSONObject(insertar);

                                        //EVENTO DEL HEROKU DB INSERCION*****************************************************************************
                                        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Pedido_POST_INSERT.php")
                                                .addJSONObjectBody(datosJSON)
                                                .setPriority(Priority.MEDIUM)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {

                                                        try {
                                                            String validarDatos = response.getString("data");
                                                            Log.e("respuesta insercion: ", "" + validarDatos);
                                                            EstiloToast(getApplicationContext(), "Registro de pedido exitoso");
                                                            Limpiar();
                                                            nombrecliente.requestFocus();
                                                            svScroll.fullScroll(ScrollView.FOCUS_UP);

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(ANError anError) {
                                                        Toast.makeText(getApplicationContext(), "Error:" + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });//FIN EVENTO DEL HEROKU DB INSERCION------------------------------------------------

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(getApplicationContext(),"Error:" + anError.getErrorDetail(),Toast.LENGTH_SHORT).show();
                                }

                            });//EVENTO DEL HEROKU DB SELECCION------------------------------------------------

                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Debe llenar todos los campos", Toast.LENGTH_SHORT);
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


        //BOTON CANCELAR -----------------------------------------------------------------------------------------
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Limpiar();
            }
        });

        /////Mostrar Calendario---------------
        Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }
    //MENU------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mnSalir: //presiono en item2
                AlertDialog alertDialog = new AlertDialog.Builder(registro_cliente.this).create();
                alertDialog.setTitle("Salir de la aplicación");
                alertDialog.setMessage("Cerrar Sesión");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finishAffinity();
                                System.exit(0);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
                return true;
            case R.id.mnListaPedidos:
                //presiono en item3
                Intent intent=new Intent(this, Listar_pedidos.class);
                this.startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    //LIMPIAR CAMPOS--------------------------------------------------------------------------------------
    public void Limpiar() {
        nombrecliente.setText("");
        apellidocliente.setText("");
        correo.setText("");
        Fecha.setText("");
        dni.setText("");
    }

    public boolean ValidarCampos(){
        return !nombrecliente.getText().toString().trim().isEmpty() && !apellidocliente.getText().toString().trim().isEmpty() && !correo.getText().toString().trim().isEmpty()
                && !Fecha.getText().toString().trim().isEmpty() && !dni.getText().toString().trim().isEmpty();
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
