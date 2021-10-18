package com.example.servimaq.contenedores;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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

        //BOTON "ATRAS" - MOSTRAR EN LA BARRA DE NAVEGACION
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnVehiculo = findViewById(R.id.btnVehiculo);
        btnMedida = findViewById(R.id.btnMedida);
        btnDetalle = findViewById(R.id.btnDetalle);
        btnNeumatico = findViewById(R.id.btnNeumatico);

        btnVehiculo.setBackground(getResources().getDrawable(R.drawable.fondo));

        btnVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_vehiculo frgVehiculo = new fragment_vehiculo();
                FragmentTransaction cambioFragment = getSupportFragmentManager().beginTransaction();
                cambioFragment.replace(R.id.frgContenedor,frgVehiculo);
                cambioFragment.commit();

                //TRANSICION DE COLORES
                btnVehiculo.setBackground(getResources().getDrawable(R.drawable.fondo));
                btnMedida.setBackground(getResources().getDrawable(R.drawable.estilo_transparente));
                btnDetalle.setBackground(getResources().getDrawable(R.drawable.estilo_transparente));
                btnNeumatico.setBackground(getResources().getDrawable(R.drawable.estilo_transparente));
            }
        });

        btnMedida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_medidas frgMedida = new fragment_medidas();
                FragmentTransaction cambioFragment = getSupportFragmentManager().beginTransaction();
                cambioFragment.replace(R.id.frgContenedor,frgMedida);
                cambioFragment.commit();

                //TRANSICION DE COLORES
                btnVehiculo.setBackground(getResources().getDrawable(R.drawable.estilo_transparente));
                btnMedida.setBackground(getResources().getDrawable(R.drawable.fondo));
                btnDetalle.setBackground(getResources().getDrawable(R.drawable.estilo_transparente));
                btnNeumatico.setBackground(getResources().getDrawable(R.drawable.estilo_transparente));
            }
        });

        btnDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detalle_llanta frgDetalle = new detalle_llanta();
                FragmentTransaction cambioFragment = getSupportFragmentManager().beginTransaction();
                cambioFragment.replace(R.id.frgContenedor,frgDetalle);
                cambioFragment.commit();

                //TRANSICION DE COLORES
                btnVehiculo.setBackground(getResources().getDrawable(R.drawable.estilo_transparente));
                btnMedida.setBackground(getResources().getDrawable(R.drawable.estilo_transparente));
                btnDetalle.setBackground(getResources().getDrawable(R.drawable.fondo));
                btnNeumatico.setBackground(getResources().getDrawable(R.drawable.estilo_transparente));
            }
        });

        btnNeumatico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_neumatico frgNeumatico = new fragment_neumatico();
                FragmentTransaction cambioFragment = getSupportFragmentManager().beginTransaction();
                cambioFragment.replace(R.id.frgContenedor,frgNeumatico);
                cambioFragment.commit();

                //TRANSICION DE COLORES
                btnVehiculo.setBackground(getResources().getDrawable(R.drawable.estilo_transparente));
                btnMedida.setBackground(getResources().getDrawable(R.drawable.estilo_transparente));
                btnDetalle.setBackground(getResources().getDrawable(R.drawable.estilo_transparente));
                btnNeumatico.setBackground(getResources().getDrawable(R.drawable.fondo));
            }
        });

    }
}