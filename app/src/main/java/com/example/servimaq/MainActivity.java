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
        getSupportActionBar().hide();

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
                    getSupportActionBar().show();
                    Intent i = new Intent(MainActivity.this,menu_opciones.class);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                }
                
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

    //SELECCION OPCION EN MENU-----------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mnOrdenCompra:
                //presiono en item1
                return true;
            case R.id.mnEstadistica:
                //presiono en item2
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}