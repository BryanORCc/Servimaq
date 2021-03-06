package com.example.servimaq.fragments_registros;

import android.content.Intent;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;


public class fragment_vehiculo extends Fragment {

    View vista;
    EditText etTipoVehiculo, etMarcaVehiculo, etModeloVehiculo;
    Button btnFoto, btnRegistrar, btnCancelar;
    ImageView ivFoto;

    String ruta;

    private StorageReference mStorage;
    StorageReference FilePath;
    Uri uri;

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

        //STORAGE FIREBASE*****************************
        mStorage = FirebaseStorage.getInstance().getReference();

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
                        ModeloVehiculo = etModeloVehiculo.getText().toString();

                FilePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ruta = taskSnapshot.getMetadata().getPath();
                        Log.e("direccion: ","++"+ruta);

                        if(ruta==null){
                            ruta = "";
                        }

                        SQLConexion db = new SQLConexion();
                        db.RegistroVehiculo(getContext(),TipoVehiculo, ruta,MarcaVehiculo,ModeloVehiculo);
                        Limpiar();
                        etTipoVehiculo.requestFocus();
                    }
                });

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
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"seleccione la aplicaci??n"),10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if( resultCode==getActivity().RESULT_OK){
            uri = data.getData();
            FilePath = mStorage.child("fotos").child(uri.getLastPathSegment());
            ivFoto.setImageURI(uri);
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