package com.example.servimaq;

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

import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.fragments_registros.detalle_llanta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

public class detalle_pedido extends AppCompatActivity {
    public String opcion="";
    boolean opcionModificar = false;
    String mensajeToast = null;
    Context c;
    EditText nombrecliente,apellidocliente,correo,dni,FechaActual;
    TextView tvNombre,tvApellido,tvCorreo,tvfechaActual,tvFechapago,modopago,tvdni;




    Button    btnConfirmarP;
    Spinner spinner1;
    ArrayList<String> opciones = new ArrayList<>();
    ArrayList<String> datosDePago = new ArrayList<>();
    Spinner codigoPedido;
    String pedidoId;


    public static EditText FechadePago;;
    public static int dia, mes, año;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);

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

        opciones.add("opcion1");
        opciones.add("opcion2");
        opciones.add("opcion3");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, opciones);
        spinner1.setAdapter(adapter);

        Intent datos = getIntent();

        pedidoId = datos.getStringExtra("codigo");


        //captura del dato del spinner////
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                opcion = spinner1.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ///carga de Datos inicial
        try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
            ResultSet rs = st.executeQuery("select * from T_Pedido where codPedido ='"+pedidoId+"';");
            if (!rs.next()) {
                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }
            else {
                do {
                    Log.e("id3",""+rs.getString(2));
                    nombrecliente.setText(rs.getString(2));
                    apellidocliente.setText(rs.getString(3));
                    correo.setText(rs.getString(4));
                    FechaActual.setText(rs.getString(5));
                    FechadePago.setText(rs.getString(6));
                    dni.setText(""+rs.getInt(8));
                } while (rs.next());

            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),"errror:",Toast.LENGTH_SHORT).show();
        }


//////////////cargando los ID en un array/////
        try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
            Log.e("id-4","-********"+st);
            ResultSet rs = st.executeQuery("select codPedido from T_Pedido");//llena con codigos de la tabla
            Log.e("id-4","-********"+rs);
            if (!rs.next()) {
                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }
            else {
                do {
                    //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                    datosDePago.add(rs.getString(1));
                    Log.e("id5",rs.getString(1));
                } while (rs.next());///va agregando cada ID
                ArrayAdapter adaptador = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, datosDePago);
                codigoPedido.setAdapter(adaptador);
                int x = -1;
                do{
                    x++;
                }while (datosDePago.get(x).equalsIgnoreCase(pedidoId));
                codigoPedido.setSelection(x);
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        //SELECCION DE OPCION - SPINNER DETALLE :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        codigoPedido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pedidoId=datosDePago.get(i);

                //--CARGAR DATOS AL SPINNER DE PEDIDOS------------------------------------------------------------------------------
                try {
                    SQLConexion conexion =new SQLConexion();
                    Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
                    ResultSet rs = st.executeQuery("select * from T_Pedido where codPedido = '"+ pedidoId +"';");
                    if (!rs.next()) {
                        Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        do {
                            nombrecliente.setText(rs.getString(2));
                            apellidocliente.setText(rs.getString(3));
                            correo.setText(rs.getString(4));
                            FechaActual.setText(rs.getString(5));
                            FechadePago.setText(rs.getString(6));
                            dni.setText(""+rs.getInt(8));
                        } while (rs.next());///
                    }
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /////////Calendario dinamico******
        FechadePago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        //CONFIRMAR CAMBIOS----------------------------------------------------------------------
        btnConfirmarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //--**ACTUALIZAR DATOS DE LA TABLA MEDIDA------------------------------------------------------------------------------::::
                try {
                    SQLConexion conexion =new SQLConexion();
                    PreparedStatement ps = conexion.ConexionDB(getApplicationContext()).prepareStatement(
                            "update T_Pedido set NombresCliente = ?, ApellidosCliente = ?, Correo = ?, FechaEntrega = ?,ModoPago = ?,DNI = ? where codPedido = ?;");

                    ps.setString(1,nombrecliente.getText().toString());
                    ps.setString(2,apellidocliente.getText().toString());
                    ps.setString(3,correo.getText().toString());
                    ps.setString(4,FechadePago.getText().toString());
                    ps.setString(5,opcion);
                    ps.setInt(6,Integer.parseInt(dni.getText().toString()));
                    ps.setString(7,pedidoId);

                    ps.executeUpdate();
                    ps.close();

                    tvNombre.setText(nombrecliente.getText().toString());
                    tvApellido.setText(apellidocliente.getText().toString());
                    tvCorreo.setText(correo.getText().toString());
                    tvfechaActual.setText(FechaActual.getText().toString());
                    tvFechapago.setText(FechadePago.getText().toString());
                    tvdni.setText(dni.getText().toString());

                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                opcionModificar = false;
                mensajeToast = "Producto Modificado";
                MostrarToast(mensajeToast);

            }
        });

    }

///////////////////////////*********
public void MostrarToast(String mensaje){
    Toast toast = Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_SHORT);
    View vista = toast.getView();
    vista.setBackgroundResource(R.drawable.estilo_color_x);
    toast.setGravity(Gravity.CENTER,0,0);
    TextView text = (TextView) vista.findViewById(android.R.id.message);
    text.setTextColor(Color.parseColor("#FFF1F9FA"));
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