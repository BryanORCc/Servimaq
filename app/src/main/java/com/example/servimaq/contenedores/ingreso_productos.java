package com.example.servimaq.contenedores;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.servimaq.R;
import com.example.servimaq.fragments_registros.detalle_llanta;
import com.example.servimaq.fragments_registros.fragment_medidas;
import com.example.servimaq.fragments_registros.fragment_neumatico;
import com.example.servimaq.fragments_registros.fragment_vehiculo;

public class ingreso_productos extends AppCompatActivity {

    Button btnVehiculo, btnMedida, btnDetalle, btnNeumatico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_productos);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnVehiculo = findViewById(R.id.btnVehiculo);
        btnMedida = findViewById(R.id.btnMedida);
        btnDetalle = findViewById(R.id.btnDetalle);
        btnNeumatico = findViewById(R.id.btnNeumatico);

        btnVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_vehiculo frgVehiculo = new fragment_vehiculo();
                FragmentTransaction cambioFragment = getSupportFragmentManager().beginTransaction();
                cambioFragment.replace(R.id.frgContenedor,frgVehiculo);
                cambioFragment.commit();
            }
        });

        btnMedida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_medidas frgMedida = new fragment_medidas();
                FragmentTransaction cambioFragment = getSupportFragmentManager().beginTransaction();
                cambioFragment.replace(R.id.frgContenedor,frgMedida);
                cambioFragment.commit();
            }
        });

        btnDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detalle_llanta frgDetalle = new detalle_llanta();
                FragmentTransaction cambioFragment = getSupportFragmentManager().beginTransaction();
                cambioFragment.replace(R.id.frgContenedor,frgDetalle);
                cambioFragment.commit();
            }
        });

        btnNeumatico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_neumatico frgNeumatico = new fragment_neumatico();
                FragmentTransaction cambioFragment = getSupportFragmentManager().beginTransaction();
                cambioFragment.replace(R.id.frgContenedor,frgNeumatico);
                cambioFragment.commit();
            }
        });

    }
}