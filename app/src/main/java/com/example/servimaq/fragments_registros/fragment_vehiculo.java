package com.example.servimaq.fragments_registros;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class fragment_vehiculo extends Fragment {

    View vista;
    EditText etTipoVehiculo, etMarcaVehiculo, etModeloVehiculo;
    Button btnFoto, btnRegistrar, btnCancelar;
    ImageView ivFoto;

    String ruta = null;

    Bitmap bitmap;
    Uri uri = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_vehiculo() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static fragment_vehiculo newInstance(String param1, String param2) {
        fragment_vehiculo fragment = new fragment_vehiculo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_vehiculo, container, false);

        etTipoVehiculo = vista.findViewById(R.id.etTipoVehiculo);
        etMarcaVehiculo = vista.findViewById(R.id.etMarcaVehiculo);
        etModeloVehiculo = vista.findViewById(R.id.etModeloVehiculo);
        btnFoto = vista.findViewById(R.id.btnFoto);
        btnRegistrar = vista.findViewById(R.id.btnRegistrar);
        btnCancelar = vista.findViewById(R.id.btnCancelar);
        ivFoto = vista.findViewById(R.id.ivFoto);


        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarImagen();
            }
        });

        //BOTON REGISTRO -----------------------------------------------------------------------------------------
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String TipoVehiculo = etTipoVehiculo.getText().toString(),
                        MarcaVehiculo = etMarcaVehiculo.getText().toString(),
                        ModeloVehiculo = etModeloVehiculo.getText().toString(),
                        Foto="";
                if(ruta==null){
                    Foto = "";
                }else{
                    Foto = ruta;
                }

                SQLConexion db = new SQLConexion();
                db.RegistroVehiculo(getContext(),TipoVehiculo, Foto,MarcaVehiculo,ModeloVehiculo);
                Limpiar();
                etTipoVehiculo.findFocus();
            }
        });

        //BOTON CANCELAR REGISTRO ---------------------------------------------------------------------------------
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Limpiar();
            }
        });

        return vista;
    }

    private void CargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"seleccione la aplicación"),10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if( resultCode==getActivity().RESULT_OK){
            ivFoto.setImageURI(data.getData());
            Log.e("$$$$$$$$","____"+data.getData());
            ruta = data.getData().toString();
        }
    }


    //LIMPIAR CAMPOS--------------------------------------------------------------------------------------
    public void Limpiar(){
        etTipoVehiculo.setText("");
        ivFoto.setImageDrawable(getResources().getDrawable(R.drawable.no_imagen));
        etMarcaVehiculo.setText("");
        etModeloVehiculo.setText("");
        ruta = null;
    }


}