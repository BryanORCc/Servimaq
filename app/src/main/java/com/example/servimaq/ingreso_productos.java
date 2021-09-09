package com.example.servimaq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.servimaq.fragments.fragment_vehiculo;

public class ingreso_productos extends AppCompatActivity {

    Button btnVehiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_productos);

        btnVehiculo = findViewById(R.id.btnVehiculo);

        btnVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_vehiculo frgVehiculo = new fragment_vehiculo();
                FragmentTransaction cambioFragment = getSupportFragmentManager().beginTransaction();
                cambioFragment.replace(R.id.frgContenedor,frgVehiculo);
                cambioFragment.commit();
            }
        });

    }
}