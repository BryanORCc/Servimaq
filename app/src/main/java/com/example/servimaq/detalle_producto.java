package com.example.servimaq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista;
import com.example.servimaq.fragments_registros.detalle_llanta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

public class detalle_producto extends AppCompatActivity {

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

    public static EditText etFechaFabricacion;
    public static int dia, mes, año;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

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
         String NombreMarca = datos.getStringExtra("NombreMarca"),
                IndiceCarga = datos.getStringExtra("IndiceCarga"),
                IndiceVelocidad = datos.getStringExtra("IndiceVelocidad"),
                Construccion = datos.getStringExtra("Construccion"),
                Clasificacion = datos.getStringExtra("Clasificacion"),
                FechaFabricacion = datos.getStringExtra("FechaFabricacion"),
                FotoLlanta = datos.getStringExtra("FotoLlanta"),
                FotoVehiculo = datos.getStringExtra("FotoVehiculo"),
                MarcaVehiculo = datos.getStringExtra("MarcaVehiculo"),
                ModeloVehiculo = datos.getStringExtra("ModeloVehiculo");
        int Ancho = datos.getIntExtra("Ancho",0),
                Diametro = datos.getIntExtra("Diametro",0),
                Perfil = datos.getIntExtra("Perfil",0),
                MmCocada = datos.getIntExtra("MmCocada",0),
                Stock = datos.getIntExtra("Stock",0),
                PresionMaxima = datos.getIntExtra("PresionMaxima",0);
        double Precio = datos.getDoubleExtra("Precio",0.0);
        String TipoVehiculo = datos.getStringExtra("TipoVehiculo");
                DetalleLlantaId = datos.getStringExtra("DetalleLlantaId");
                VehiculoId = datos.getStringExtra("VehiculoId");
                MedidaLlantaId = datos.getStringExtra("MedidaLlantaId");

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


        //MOSTRAR DETALLE EN LOS TEXT VIEWS ------------------------------------------
        tvllantaId.setText(llantaId);
        tvNombreMarca.setText(NombreMarca);
        tvIndiceCarga.setText(IndiceCarga);
        tvIndiceVelocidad.setText(IndiceVelocidad);
        tvConstruccion.setText(Construccion);
        tvClasificacion.setText(Clasificacion);
        tvFechaFabricacion.setText(FechaFabricacion);
        tvMarcaVehiculo.setText(MarcaVehiculo);
        tvModeloVehiculo.setText(ModeloVehiculo);
        tvAncho.setText(""+Ancho);
        tvDiametro.setText(""+Diametro);
        tvPerfil.setText(""+Perfil);
        tvMmCocada.setText(""+MmCocada);
        tvPresionMaxima.setText(""+PresionMaxima);
        tvStock.setText(""+Stock);
        tvPrecio.setText(""+Precio);
        tvTipoVehiculo.setText(TipoVehiculo);

        //OCULTAR ----------------------------------------
        OcultarEditText();
        btnConfirmar.setVisibility(View.GONE);
        llOpcionVehiculoId.setVisibility(View.GONE);
        llOpcionDetalleLlantaId.setVisibility(View.GONE);
        llOpcionMedidaLlantaId.setVisibility(View.GONE);

        //CAMBIAR VISTA CON EDIT TEXT----------------------------------------------------------------------
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OcultarTextView();
                MostrarEditText();
                btnModificar.setVisibility(View.GONE);
                btnConfirmar.setVisibility(View.VISIBLE);
                llOpcionVehiculoId.setVisibility(View.VISIBLE);
                llOpcionDetalleLlantaId.setVisibility(View.VISIBLE);
                llOpcionMedidaLlantaId.setVisibility(View.VISIBLE);

                //CARGAR DATOS A LOS EDIT TEXT--------------------------------------
                etStock.setText(""+Stock);
                etPrecio.setText(""+Precio);
                etTipoVehiculo.setText(TipoVehiculo);
                etMarcaVehiculo.setText(MarcaVehiculo);
                etModeloVehiculo.setText(ModeloVehiculo);
                etNombreMarca.setText(NombreMarca);
                etIndiceCarga.setText(IndiceCarga);
                etIndiceVelocidad.setText(IndiceVelocidad);
                etConstruccion.setText(Construccion);
                etPresionMaxima.setText(""+PresionMaxima);
                etClasificacion.setText(Clasificacion);
                etFechaFabricacion.setText(FechaFabricacion);
                etAncho.setText(""+Ancho);
                etDiametro.setText(""+Diametro);
                etPerfil.setText(""+Perfil);
                etMmCocada.setText(""+MmCocada);


            }
        });

        //--CARGAR DATOS A LOS SPINNERS - VEHICULO ------------------------------------------------------------------------------
        try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
            ResultSet rs = st.executeQuery("select VehiculoId from T_Vehiculo");

            if (!rs.next()) {
                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }
            else {
                do {
                    //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                    datosVehiculo.add(rs.getString(1));
                } while (rs.next());///va agregando cada ID

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, datosVehiculo);
                spVehiculoId.setAdapter(adapter);
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }//FIN Carga--------------------------------------------------------------------------------


        //--CARGAR DATOS A LOS SPINNERS - DETALLE ------------------------------------------------------------------------------
        try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
            ResultSet rs = st.executeQuery("select DetalleLlantaId from T_DetalleLlanta");

            if (!rs.next()) {
                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }
            else {
                do {
                    //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                    datosDetalle.add(rs.getString(1));
                } while (rs.next());///va agregando cada ID
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, datosDetalle);
                spDetalleLlantaId.setAdapter(adapter);
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }//FIN Carga--------------------------------------------------------------------------------


        //--CARGAR DATOS A LOS SPINNERS - MEDIDA ------------------------------------------------------------------------------
        try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
            ResultSet rs = st.executeQuery("select MedidaLlantaId from T_MedidaLlanta");

            if (!rs.next()) {
                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }
            else {
                do {
                    //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                    datosMedida.add(rs.getString(1));
                } while (rs.next());///va agregando cada ID
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, datosMedida);
                spMedidaLlantaId.setAdapter(adapter);
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }//FIN Carga--------------------------------------------------------------------------------


        //SELECCION DE OPCION - SPINNER VEHICULO :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        spVehiculoId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                VehiculoId=datosVehiculo.get(i);

                //--CARGAR DATOS AL SPINNER DE VEHICULO------------------------------------------------------------------------------
                try {
                    SQLConexion conexion =new SQLConexion();
                    Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
                    ResultSet rs = st.executeQuery("select * from T_Vehiculo where VehiculoId = '"+ VehiculoId +"';");

                    if (!rs.next()) {
                        Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        do {
                            //CARGAR DATOS DE LA TABLA-----------------------------
                            etTipoVehiculo.setText(rs.getString(2));
                            etMarcaVehiculo.setText(rs.getString(4));
                            etModeloVehiculo.setText(rs.getString(5));
                        } while (rs.next());///va agregando cada ID
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


        //SELECCION DE OPCION - SPINNER DETALLE :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        spDetalleLlantaId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DetalleLlantaId=datosDetalle.get(i);

                //--CARGAR DATOS AL SPINNER DE VEHICULO------------------------------------------------------------------------------
                try {
                    SQLConexion conexion =new SQLConexion();
                    Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
                    ResultSet rs = st.executeQuery("select * from T_DetalleLlanta where DetalleLlantaId = '"+ DetalleLlantaId +"';");

                    if (!rs.next()) {
                        Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        do {
                            //CARGAR DATOS DE LA TABLA-----------------------------
                            etNombreMarca.setText(rs.getString(2));
                            etIndiceCarga.setText(""+rs.getInt(3));
                            etIndiceVelocidad.setText(rs.getString(4));
                            etConstruccion.setText(rs.getString(6));
                            etPresionMaxima.setText(""+rs.getInt(7));
                            etClasificacion.setText(rs.getString(8));
                            etFechaFabricacion.setText(rs.getString(9));

                        } while (rs.next());///va agregando cada ID
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


        //SELECCION DE OPCION - SPINNER MEDIDA :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        spMedidaLlantaId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MedidaLlantaId=datosMedida.get(i);

                //--CARGAR DATOS AL SPINNER DE VEHICULO------------------------------------------------------------------------------
                try {
                    SQLConexion conexion =new SQLConexion();
                    Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
                    ResultSet rs = st.executeQuery("select * from T_MedidaLlanta where MedidaLlantaId = '"+ MedidaLlantaId +"';");

                    if (!rs.next()) {
                        Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        do {
                            //CARGAR DATOS DE LA TABLA-----------------------------
                            etAncho.setText(""+rs.getInt(2));
                            etDiametro.setText(""+rs.getInt(3));
                            etPerfil.setText(""+rs.getInt(4));
                            etMmCocada.setText(""+rs.getInt(5));

                        } while (rs.next());///va agregando cada ID
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

                //--ACTUALIZAR DATOS DE LA TABLA MEDIDA------------------------------------------------------------------------------
                try {
                    SQLConexion conexion =new SQLConexion();
                    PreparedStatement ps = conexion.ConexionDB(getApplicationContext()).prepareStatement(
                            "update T_MedidaLlanta set Ancho = ?, Diametro = ?, Perfil = ?, MmCocada = ? where MedidaLlantaId = ?;");

                    ps.setInt(1,Integer.parseInt(etAncho.getText().toString()));
                    ps.setInt(2,Integer.parseInt(etDiametro.getText().toString()));
                    ps.setInt(3,Integer.parseInt(etPerfil.getText().toString()));
                    ps.setInt(4,Integer.parseInt(etMmCocada.getText().toString()));
                    ps.setString(5,MedidaLlantaId);
                    ps.executeUpdate();
                    ps.close();
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }


                //--ACTUALIZAR DATOS DE LA TABLA VEHICULO------------------------------------------------------------------------------
                try {
                    SQLConexion conexion =new SQLConexion();
                    PreparedStatement ps = conexion.ConexionDB(getApplicationContext()).prepareStatement(
                            "update T_Vehiculo set TipoVehiculo = ?, MarcaVehiculo = ?, ModeloVehiculo = ? where VehiculoId = ?;");
                    ps.setString(1,etTipoVehiculo.getText().toString());
                    ps.setString(2,etMarcaVehiculo.getText().toString());
                    ps.setString(3,etModeloVehiculo.getText().toString());
                    ps.setString(4,VehiculoId);
                    ps.executeUpdate();
                    ps.close();
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                //--ACTUALIZAR DATOS DE LA TABLA DETALLE------------------------------------------------------------------------------
                try {
                    SQLConexion conexion =new SQLConexion();
                    PreparedStatement ps = conexion.ConexionDB(getApplicationContext()).prepareStatement(
                            "update T_DetalleLlanta set NombreMarca = ?, IndiceCarga = ?, IndiceVelocidad = ?, Construccion = ?, PresionMaxima = ?," +
                                    " Clasificacion = ?, FechaFabricacion = ?, MedidaLlantaId = ? where DetalleLlantaId = ?;");
                    ps.setString(1,etNombreMarca.getText().toString());
                    ps.setInt(2,Integer.parseInt(etIndiceCarga.getText().toString()));
                    ps.setString(3,etIndiceVelocidad.getText().toString());
                    ps.setString(4,etConstruccion.getText().toString());
                    ps.setInt(5,Integer.parseInt(etPresionMaxima.getText().toString()));
                    ps.setString(6,etClasificacion.getText().toString());
                    ps.setString(7,tvFechaFabricacion.getText().toString());
                    ps.setString(8,MedidaLlantaId);
                    ps.setString(9,DetalleLlantaId);
                    ps.executeUpdate();
                    ps.close();
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                //--ACTUALIZAR DATOS DE LA TABLA NEUMATICO------------------------------------------------------------------------------
                try {
                    SQLConexion conexion =new SQLConexion();
                    PreparedStatement ps = conexion.ConexionDB(getApplicationContext()).prepareStatement(
                            "update T_Llanta set Precio = ?, Stock = ?, DetalleLlantaId = ?, VehiculoId = ? where LlantaId = ?;");
                    ps.setDouble(1,Double.parseDouble(etPrecio.getText().toString()));
                    ps.setInt(2,Integer.parseInt(etStock.getText().toString()));
                    ps.setString(3,DetalleLlantaId);
                    ps.setString(4,VehiculoId);
                    ps.setString(5,llantaId);
                    ps.executeUpdate();
                    ps.close();
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                OcultarEditText();
                MostrarTextView();
                btnModificar.setVisibility(View.VISIBLE);
                btnConfirmar.setVisibility(View.GONE);
                llOpcionVehiculoId.setVisibility(View.GONE);
                llOpcionDetalleLlantaId.setVisibility(View.GONE);
                llOpcionMedidaLlantaId.setVisibility(View.GONE);

            }
        });

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
}