package com.example.servimaq.op_catalogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

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


        //--CARGAR DATOS A LOS SPINNERS - MEDIDA ------------------------------------------------------------------------------:::::::
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

                int x = -1;
                do{
                    x++;
                }while (datosMedida.get(x).equalsIgnoreCase(MedidaLlantaId));
                spMedidaLlantaId.setSelection(x);
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }//FIN Carga--------------------------------------------------------------------------------


        //SELECCION DE OPCION - SPINNER VEHICULO :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        spVehiculoId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);

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

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);

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

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);

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

        //**************************SELECT INFORMACION NEUMATICO EDIT TEXT---**************************************************************************
        //****************************************************************************************************
        try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
            ResultSet rs = st.executeQuery("select L.LlantaId, L.Stock, L.Precio, V.TipoVehiculo, V.MarcaVehiculo, V.ModeloVehiculo, D.NombreMarca," +
                    " D.IndiceCarga, D.IndiceVelocidad,  D.Construccion, D.PresionMaxima, D.Clasificacion, D.FechaFabricacion, M.Ancho, M.Diametro," +
                    " M.Perfil, M.MmCocada, V.VehiculoId, M.MedidaLlantaId, D.DetalleLlantaId, V.FotoVehiculo, D.FotoLlanta from T_Llanta L " +
                    "inner join T_DetalleLlanta D on L.DetalleLlantaId = D.DetalleLlantaId inner join T_Vehiculo V on L.VehiculoId = V.VehiculoId " +
                    "inner join T_MedidaLlanta M on M.MedidaLlantaId = D.MedidaLlantaId where L.LlantaId = '"+ llantaId +"';");

            if (!rs.next()) {
                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }else {
                do {
                    //CARGAR DATOS A LOS TEXT VIEW--------------------------------------
                    tvllantaId.setText(rs.getString(1));
                    tvStock.setText(""+rs.getInt(2));
                    tvPrecio.setText(""+rs.getDouble(3));
                    tvTipoVehiculo.setText(rs.getString(4));
                    tvMarcaVehiculo.setText(rs.getString(5));
                    tvModeloVehiculo.setText(rs.getString(6));
                    tvNombreMarca.setText(rs.getString(7));
                    tvIndiceCarga.setText(""+rs.getInt(8));
                    tvIndiceVelocidad.setText(rs.getString(9));
                    tvConstruccion.setText(rs.getString(10));
                    tvPresionMaxima.setText(""+rs.getInt(11));
                    tvClasificacion.setText(rs.getString(12));
                    tvFechaFabricacion.setText(rs.getString(13));
                    tvAncho.setText(""+rs.getInt(14));
                    tvDiametro.setText(""+rs.getInt(15));
                    tvPerfil.setText(""+rs.getInt(16));
                    tvMmCocada.setText(""+rs.getInt(17));

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference islandRef = storageRef.child(rs.getString(22));
                    final long ONE_MEGABYTE = 480 * 480;

                    Log.e("Contar","___: "+ rs.getString(22));

                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            ivFoto_Llanta.setImageBitmap(bitmap);
                        }
                    });
                } while (rs.next());///va agregando cada ID
            }
            rs.close();

            int x = -1;
            do{
                x++;
            }while (datosMedida.get(x).equalsIgnoreCase(MedidaLlantaId));
            spMedidaLlantaId.setSelection(x);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }//FIN SELECT-------------


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

                estadoCancelar = 1;

                OcultarTextView();
                MostrarEditText();
                btnModificar.setVisibility(View.GONE);
                btnConfirmar.setVisibility(View.VISIBLE);
                llOpcionVehiculoId.setVisibility(View.VISIBLE);
                llOpcionDetalleLlantaId.setVisibility(View.VISIBLE);
                llOpcionMedidaLlantaId.setVisibility(View.VISIBLE);

                //--SELECT INFORMACION NEUMATICO EDIT TEXT-----------------------------------------------------------------------------
                try {
                    SQLConexion conexion =new SQLConexion();
                    Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
                    ResultSet rs = st.executeQuery("select L.LlantaId, L.Stock, L.Precio, V.TipoVehiculo, V.MarcaVehiculo, V.ModeloVehiculo, D.NombreMarca," +
                            " D.IndiceCarga, D.IndiceVelocidad,  D.Construccion, D.PresionMaxima, D.Clasificacion, D.FechaFabricacion, M.Ancho, M.Diametro," +
                            " M.Perfil, M.MmCocada, V.VehiculoId, M.MedidaLlantaId, D.DetalleLlantaId, V.FotoVehiculo, D.FotoLlanta from T_Llanta L " +
                            "inner join T_DetalleLlanta D on L.DetalleLlantaId = D.DetalleLlantaId inner join T_Vehiculo V on L.VehiculoId = V.VehiculoId " +
                            "inner join T_MedidaLlanta M on M.MedidaLlantaId = D.MedidaLlantaId where L.LlantaId = '"+ llantaId +"';");

                    if (!rs.next()) {
                        Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                    }else {
                        do {
                            //CARGAR DATOS A LOS EDIT TEXT--------------------------------------
                            etStock.setText(""+rs.getInt(2));
                            etPrecio.setText(""+rs.getDouble(3));
                            etTipoVehiculo.setText(rs.getString(4));
                            etMarcaVehiculo.setText(rs.getString(5));
                            etModeloVehiculo.setText(rs.getString(6));
                            etNombreMarca.setText(rs.getString(7));
                            etIndiceCarga.setText(""+rs.getInt(8));
                            etIndiceVelocidad.setText(rs.getString(9));
                            etConstruccion.setText(rs.getString(10));
                            etPresionMaxima.setText(""+rs.getInt(11));
                            etClasificacion.setText(rs.getString(12));
                            etFechaFabricacion.setText(rs.getString(13));
                            etAncho.setText(""+rs.getInt(14));
                            etDiametro.setText(""+rs.getInt(15));
                            etPerfil.setText(""+rs.getInt(16));
                            etMmCocada.setText(""+rs.getInt(17));

                            int x = -1;
                            do{
                                x++;
                            }
                            while (!datosVehiculo.get(x).equalsIgnoreCase(rs.getString(18)));
                            spVehiculoId.setSelection(x);

                            int y = -1;
                            do{
                                y++;
                            }while (!datosMedida.get(y).equalsIgnoreCase(rs.getString(19)));
                            spMedidaLlantaId.setSelection(y);

                            int z = -1;
                            do{
                                z++;
                            }while (!datosDetalle.get(z).equalsIgnoreCase(rs.getString(20)));
                            spDetalleLlantaId.setSelection(z);

                        } while (rs.next());///va agregando cada ID
                    }
                    rs.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }//FIN SELECT-------------

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

                //--**ACTUALIZAR DATOS DE LA TABLA MEDIDA------------------------------------------------------------------------------::::
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

                    tvAncho.setText(etAncho.getText().toString());
                    tvDiametro.setText(etDiametro.getText().toString());
                    tvPerfil.setText(etPerfil.getText().toString());
                    tvMmCocada.setText(etMmCocada.getText().toString());

                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }


                //--**ACTUALIZAR DATOS DE LA TABLA VEHICULO------------------------------------------------------------------------------
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

                    tvTipoVehiculo.setText(etTipoVehiculo.getText().toString());
                    tvMarcaVehiculo.setText(etMarcaVehiculo.getText().toString());
                    tvModeloVehiculo.setText(etModeloVehiculo.getText().toString());

                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                //--**ACTUALIZAR DATOS DE LA TABLA DETALLE------------------------------------------------------------------------------
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
                    ps.setString(7,etFechaFabricacion.getText().toString());
                    ps.setString(8,MedidaLlantaId);
                    ps.setString(9,DetalleLlantaId);
                    ps.executeUpdate();
                    ps.close();

                    tvNombreMarca.setText(etNombreMarca.getText().toString());
                    tvIndiceCarga.setText(etIndiceCarga.getText().toString());
                    tvIndiceVelocidad.setText(etIndiceVelocidad.getText().toString());
                    tvConstruccion.setText(etConstruccion.getText().toString());
                    tvPresionMaxima.setText(etPresionMaxima.getText().toString());
                    tvClasificacion.setText(etClasificacion.getText().toString());
                    tvFechaFabricacion.setText(etFechaFabricacion.getText().toString());

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

                    tvPrecio.setText(etPrecio.getText().toString());
                    tvStock.setText(etStock.getText().toString());

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
            OcultarTextView();
            MostrarEditText();
            btnModificar.setVisibility(View.GONE);
            btnConfirmar.setVisibility(View.VISIBLE);
            llOpcionVehiculoId.setVisibility(View.VISIBLE);
            llOpcionDetalleLlantaId.setVisibility(View.VISIBLE);
            llOpcionMedidaLlantaId.setVisibility(View.VISIBLE);

            //--SELECT INFORMACION NEUMATICO EDIT TEXT-----------------------------------------------------------------------------
            try {
                SQLConexion conexion =new SQLConexion();
                Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
                ResultSet rs = st.executeQuery("select L.LlantaId, L.Stock, L.Precio, V.TipoVehiculo, V.MarcaVehiculo, V.ModeloVehiculo, D.NombreMarca," +
                        " D.IndiceCarga, D.IndiceVelocidad,  D.Construccion, D.PresionMaxima, D.Clasificacion, D.FechaFabricacion, M.Ancho, M.Diametro," +
                        " M.Perfil, M.MmCocada, V.VehiculoId, M.MedidaLlantaId, D.DetalleLlantaId, V.FotoVehiculo, D.FotoLlanta from T_Llanta L " +
                        "inner join T_DetalleLlanta D on L.DetalleLlantaId = D.DetalleLlantaId inner join T_Vehiculo V on L.VehiculoId = V.VehiculoId " +
                        "inner join T_MedidaLlanta M on M.MedidaLlantaId = D.MedidaLlantaId where L.LlantaId = '"+ llantaId +"';");

                if (!rs.next()) {
                    Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                }else {
                    do {
                        //CARGAR DATOS A LOS EDIT TEXT--------------------------------------
                        etStock.setText(""+rs.getInt(2));
                        etPrecio.setText(""+rs.getDouble(3));
                        etTipoVehiculo.setText(rs.getString(4));
                        etMarcaVehiculo.setText(rs.getString(5));
                        etModeloVehiculo.setText(rs.getString(6));
                        etNombreMarca.setText(rs.getString(7));
                        etIndiceCarga.setText(""+rs.getInt(8));
                        etIndiceVelocidad.setText(rs.getString(9));
                        etConstruccion.setText(rs.getString(10));
                        etPresionMaxima.setText(""+rs.getInt(11));
                        etClasificacion.setText(rs.getString(12));
                        etFechaFabricacion.setText(rs.getString(13));
                        etAncho.setText(""+rs.getInt(14));
                        etDiametro.setText(""+rs.getInt(15));
                        etPerfil.setText(""+rs.getInt(16));
                        etMmCocada.setText(""+rs.getInt(17));

                        int x = -1;
                        do{
                            x++;
                        }
                        while (!datosVehiculo.get(x).equalsIgnoreCase(rs.getString(18)));
                        spVehiculoId.setSelection(x);

                        int y = -1;
                        do{
                            y++;
                        }while (!datosMedida.get(y).equalsIgnoreCase(rs.getString(19)));
                        spMedidaLlantaId.setSelection(y);

                        int z = -1;
                        do{
                            z++;
                        }while (!datosDetalle.get(z).equalsIgnoreCase(rs.getString(20)));
                        spDetalleLlantaId.setSelection(z);


                    } while (rs.next());///va agregando cada ID
                }
                rs.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }//FIN SELECT-------------
        }

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
        text.setTextColor(Color.parseColor("#FFF1F9FA"));
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
}