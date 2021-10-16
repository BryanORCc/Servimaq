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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class registro_cliente extends AppCompatActivity {
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
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        FechaActual.setText(date);
        ///////////////////////////////

        ///captura del dato del spinner////
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                SQLConexion conexion =new SQLConexion();
                try {
                    Statement st = conexion.ConexionDB(registro_cliente.this).createStatement();
                }
                catch (Exception e) {
                    Toast.makeText(registro_cliente.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                String  nombreC = nombrecliente.getText().toString(),
                        apellidoC = apellidocliente.getText().toString(),
                        corre = correo.getText().toString(),
                        FechaAct = FechaActual.getText().toString(),
                        FechaEntr=Fecha.getText().toString(),
                        documento=dni.getText().toString();

                SQLConexion db = new SQLConexion();
                db.RegistroPedidoCliente(registro_cliente.this,nombreC,apellidoC,corre,FechaAct,FechaEntr, opcion ,Integer.parseInt(documento));

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
            case R.id.mnOrdenCompra:
                //presiono en item1
                return true;
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
                Fecha.setText("0"+año+"-"+"0"+mes+"-"+dia);
            }else if(dia<10 && mes>9){
                Fecha.setText("0"+año+"-"+mes+"-"+dia);
            }else if(dia>9 && mes<10){
                Fecha.setText(año+"-"+"0"+mes+"-"+dia);
            }else{
                Fecha.setText(año+"-"+mes+"-"+dia);
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
}
