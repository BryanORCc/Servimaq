package com.example.servimaq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.servimaq.fragments.fragment_vehiculo;

public class ingreso_productos extends AppCompatActivity {

    Button btnVehiculo, btnMedida, btnDetalle, btnNeumatico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_productos);

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
                fragment_vehiculo frgMedida = new fragment_vehiculo();
                FragmentTransaction cambioFragment = getSupportFragmentManager().beginTransaction();
                cambioFragment.replace(R.id.frgContenedor,frgMedida);
                cambioFragment.commit();
            }
        });

        btnDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_vehiculo frgDetalle = new fragment_vehiculo();
                FragmentTransaction cambioFragment = getSupportFragmentManager().beginTransaction();
                cambioFragment.replace(R.id.frgContenedor,frgDetalle);
                cambioFragment.commit();
            }
        });

        btnNeumatico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_vehiculo frgNeumatico = new fragment_vehiculo();
                FragmentTransaction cambioFragment = getSupportFragmentManager().beginTransaction();
                cambioFragment.replace(R.id.frgContenedor,frgNeumatico);
                cambioFragment.commit();
            }
        });

    }
}