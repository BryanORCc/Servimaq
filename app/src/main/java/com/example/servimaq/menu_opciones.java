package com.example.servimaq;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.servimaq.contenedores.ingreso_productos;
import com.example.servimaq.op_catalogo.Catalogo;
import com.example.servimaq.op_documentos.Documentos;
import com.example.servimaq.op_pedidos.Listar_pedidos;
import com.example.servimaq.op_pedidos.registro_cliente;
import com.example.servimaq.op_salida.Salida_Prod;

public class menu_opciones extends AppCompatActivity {

    Button btnProductos, btnCatalogo,btnRegistroCliente,btnSalida_Producto, btnDocumentos;
    ImageView mensajePersonaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_opciones);

        final VideoView videofondo = (VideoView) findViewById(R.id.mc_fondo_video);
        videofondo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videomenu));
        videofondo.start();

        videofondo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        btnProductos = findViewById(R.id.btnProductos);
        btnCatalogo = findViewById(R.id.btnCatalogo);
        btnSalida_Producto = findViewById(R.id.btnSalida_Producto);
        btnRegistroCliente = findViewById(R.id.btnRegistroCliente);
        btnDocumentos = findViewById(R.id.btnDocumentos);
        mensajePersonaje = findViewById(R.id.mensajePersonaje);

        //PANTALLA REGISTROS---------------------------------------------------------------------------------
        btnProductos.setOnClickListener(view -> {
            Intent i = new Intent(menu_opciones.this, ingreso_productos.class);
            startActivity(i);
        });

        //PANTALLA CATALOGO---------------------------------------------------------------------------------
        btnCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu_opciones.this, Catalogo.class);
                startActivity(i);
            }
        });

        //PANTALLA DE REGISTRO DE CLIENTE--------------------------------------
        btnRegistroCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu_opciones.this, registro_cliente.class);
                startActivity(i);
            }
        });

        //  PANTALLA SALIDA DE PRODUCTO--------------------------------------
        btnSalida_Producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu_opciones.this, Salida_Prod.class);
                startActivity(i);
            }
        });

        //  PANTALLA DOCUMENTOS--------------------------------------------
        btnDocumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu_opciones.this, Documentos.class);
                startActivity(i);
            }
        });

        mensajePersonaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(menu_opciones.this).create();
                alertDialog.setTitle("QUE ME TOCAS OE, CUANDO TE FALTE EL RESPETO");
                alertDialog.setMessage("Ubícate en primer lugar cuando seas estudiante, de inicial, primaria, secundaria " +
                        "\ny superior, ahí quiero verte mis respetos diría ingeniero así, que chucha eres webon??!! " +
                        "\neres del campo, un campesino, un indigente, no eres ni mierda on!!! " +
                        "\nNO ERES NI MIERDA, NO ERES NADA!!, piensa pe chato piensa, Estudia sonso!!");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ponerse a Estudiar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Fuera CT...",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                AlertDialog alertDialog2 = new AlertDialog.Builder(menu_opciones.this).create();
                                LayoutInflater factory = LayoutInflater.from(menu_opciones.this);
                                final View view = factory.inflate(R.layout.mensaje_conciencia, null);
                                alertDialog2.setView(view);
                                alertDialog2.setButton(AlertDialog.BUTTON_POSITIVE, "PI PI PI...",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                alertDialog2.show();
                            }
                        });
                alertDialog.show();
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
            case R.id.mnSalir: //presiono en item2
                AlertDialog alertDialog = new AlertDialog.Builder(menu_opciones.this).create();
                alertDialog.setTitle("Salir de la aplicación");
                alertDialog.setMessage("Cerrar Sesión");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finishAffinity();
                                System.exit(0);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
                return true;
            case R.id.mnListaPedidos:
                //presiono en item3
                Intent intent=new Intent(this, Listar_pedidos.class);
                this.startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}