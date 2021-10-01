package com.example.servimaq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.servimaq.db.SQLConexion;

public class MainActivity extends AppCompatActivity {

    EditText etUsuario, etContra;
    Button btnIngresar;
    View v;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.etUsuario);
        etContra = findViewById(R.id.etContra);
        btnIngresar = findViewById(R.id.btnIngresar);

        //BOTON DE INGRESO - LOGIN-------------------------------------------
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu menu = null;
                String usuario = etUsuario.getText().toString(),
                        contra = etContra.getText().toString();

                SQLConexion db = new SQLConexion();
                if(db.Validar(getBaseContext(),usuario,contra)==true){
                    Intent i = new Intent(MainActivity.this,menu_opciones.class);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                }
                
            }
        });
    }


}