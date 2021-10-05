package com.example.servimaq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.servimaq.contenedores.ingreso_productos;
import com.example.servimaq.fragments_registros.Salida_Prod;

public class menu_opciones extends AppCompatActivity {

    Button btnProductos, btnCatalogo ,btnSalida_Producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_opciones);

        btnProductos = findViewById(R.id.btnProductos);
        btnCatalogo = findViewById(R.id.btnCatalogo);
        btnSalida_Producto= findViewById(R.id.btnSalida_Producto);

        //PANTALLA REGISTROS---------------------------------------------------------------------------------
        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu_opciones.this, ingreso_productos.class);
                startActivity(i);
            }
        });

        //PANTALLA CATALOGO---------------------------------------------------------------------------------
        btnCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu_opciones.this, Catalogo.class);
                startActivity(i);
            }
        });

        //  PANTALLA SALIDA DE PRODUCTO

        btnSalida_Producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu_opciones.this, Salida_Prod.class);
                startActivity(i);
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