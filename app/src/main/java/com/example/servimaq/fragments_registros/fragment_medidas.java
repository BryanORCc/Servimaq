package com.example.servimaq.fragments_registros;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.menu_opciones;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_medidas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_medidas extends Fragment {

    View vista;
    EditText etAncho, etDiametro, etPerfil, etMmcocada;
    Button  btnRegistrar, btnCancelar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public fragment_medidas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_medidas.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_medidas newInstance(String param1, String param2) {
        fragment_medidas fragment = new fragment_medidas();
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
        vista = inflater.inflate(R.layout.fragment_medidas, container, false);

        etAncho=vista.findViewById(R.id.etAncho);
        etDiametro=vista.findViewById(R.id.etDiametro);
        etPerfil=vista.findViewById(R.id.etPerfil);
        etMmcocada=vista.findViewById(R.id.etMmcocada);
        btnRegistrar=vista.findViewById(R.id.btnRegistrar);
        btnCancelar=vista.findViewById(R.id.btnCancelar);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Ancho=Integer.parseInt(etAncho.getText().toString());
                int Diametro=Integer.parseInt(etDiametro.getText().toString());
                int Perfil=Integer.parseInt(etPerfil.getText().toString());
                int MmCocada=Integer.parseInt(etMmcocada.getText().toString());

                SQLConexion db = new SQLConexion();
                db.RegistroMedida(getContext(),Ancho,Diametro,Perfil,MmCocada);
                //Limpiar();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Limpiar();
            }
        });

        return vista;
    }

    //LIMPIAR CAMPOS--------------------------------------------------------------------------------------
    public void Limpiar(){
        etAncho.setText("");
        etDiametro.setText("");
        etPerfil.setText("");
        etMmcocada.setText("");
    }
}