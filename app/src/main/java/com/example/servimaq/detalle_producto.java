package com.example.servimaq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class detalle_producto extends AppCompatActivity {

    TextView tvllantaId, tvNombreMarca, tvIndiceCarga, tvIndiceVelocidad, tvConstruccion, tvClasificacion, tvFechaFabricacion, tvMarcaVehiculo,
            tvModeloVehiculo, tvAncho, tvDiametro, tvPerfil, tvMmCocada, tvPresionMaxima, tvStock, tvPrecio, tvTipoVehiculo;
    EditText etNombreMarca, etIndiceCarga, etIndiceVelocidad, etConstruccion, etClasificacion, etFechaFabricacion, etMarcaVehiculo,
            etModeloVehiculo, etAncho, etDiametro, etPerfil, etMmCocada, etPresionMaxima, etStock, etPrecio, etTipoVehiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        Intent datos = getIntent();

        String llantaId = datos.getStringExtra("llantaId"),
                NombreMarca = datos.getStringExtra("NombreMarca"),
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
        String TipoVehiculo = datos.getStringExtra("TipoVehiculo"),
                DetalleLlantaId = datos.getStringExtra("DetalleLlantaId"),
                VehiculoId = datos.getStringExtra("VehiculoId"),
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

        //OCULTAR ----------------------------------------
        OcultarEditText();

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
    }
}