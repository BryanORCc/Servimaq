package com.example.servimaq.fragments_registros;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.menu_opciones;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_neumatico#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_neumatico extends Fragment {

    View vista;
    EditText etprecio,etstock;
    Button btnRegistrar, btnCancelar;
    Spinner Spi_especificacion,Spi_vehiculo;
    TextView Tv_ver,Tv_ver1;

    String vehiculo,especificacion;
    ArrayList<String> op = new ArrayList<>();
    ArrayList<String> info = new ArrayList<>();
    ArrayList<String> op1 = new ArrayList<>();
    ArrayList<String> info1 = new ArrayList<>();
    String VehiculoId="";
    String DetalleLlantaId="";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_neumatico() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static fragment_neumatico newInstance(String param1, String param2) {
        fragment_neumatico fragment = new fragment_neumatico();
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


        vista = inflater.inflate(R.layout.fragment_neumatico, container, false);

        etprecio = vista.findViewById(R.id.etprecio);
        etstock = vista.findViewById(R.id.etstock);
        btnRegistrar=vista.findViewById(R.id.btnRegistrar);
        btnCancelar=vista.findViewById(R.id.btnCancelar);
        Tv_ver = vista.findViewById(R.id.Tv_ver);
        Tv_ver1 = vista.findViewById(R.id.Tv_ver1);

        Spi_especificacion=vista.findViewById(R.id.Spi_especificacion);
        Spi_vehiculo=vista.findViewById(R.id.Spi_vehiculo);


        //CARGAR DATO DEL SPINNER DE VEHICULO ------------------------------------------------------------------
        try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(getContext()).createStatement();
            ResultSet rs = st.executeQuery("select * from T_Vehiculo");

            if (!rs.next()) {
                VehiculoId = "";
            }
            else {
                do {

                    op.add(rs.getString(1));

                    vehiculo="-TipoVehiculo: "+rs.getString(2)+
                            "\n-MarcaVehiculo: "+rs.getString(5) +
                            "\n-ModeloVehiculo: "+rs.getString(4);
                            //FotoVehiculo: "+rs.getString(3);

                    info.add(vehiculo);
                } while (rs.next());///va agregando cada ID

                ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, op);

                Spi_vehiculo.setAdapter(adapter);

            }
        }
        catch (Exception e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }


        Spi_vehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Tv_ver1.setText(info.get(i));
                VehiculoId=op.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //CARGAR DATO DEL SPINNER DE DETALLE DE LLANTA  ----------------------------------------------------------------------
        try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(getContext()).createStatement();
            ResultSet rs = st.executeQuery("select * from T_DetalleLlanta");
            if (!rs.next()) {
                DetalleLlantaId = "";
            }
            else {
                do {

                    op1.add(rs.getString(1));

                    especificacion="-NombreMarca: "+rs.getString(2)+
                            "\n-IndiceCarga: "+rs.getString(3)+
                            "\n-IndiceVelocidad:"+rs.getString(4) +
                            "\n-Construccion: "+rs.getString(6)+
                            "\n-PresionMaxima: "+rs.getString(7)+
                            "\n-Clasificacion: "+rs.getString(8)+
                            "\n-FechaFabricacion: "+rs.getString(9)+
                            "\n-MedidaLlantaId: "+rs.getString(10);
                            //FotoLlanta: "+rs.getString(5);

                    info1.add(especificacion);
                } while (rs.next());///va agregando cada ID

                ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, op1);
                Spi_especificacion.setAdapter(adapter);

            }
        }
        catch (Exception e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }


        Spi_especificacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Tv_ver.setText(info1.get(i));
                DetalleLlantaId=op1.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // registrar--------------------------------------------------------------------------
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String precio = etprecio.getText().toString(),
                        stock = etstock.getText().toString();


                SQLConexion db = new SQLConexion();
                db.RegistroLlanta(getContext(),Double.parseDouble(precio),Integer.parseInt(stock),DetalleLlantaId,VehiculoId);
                Limpiar();


            }
        });

        // cancelar -----------------------*-----------------------------------------------------
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Limpiar();
            }
        });

        return vista;
    }


    //LIMPIAR llanta--------------------------------------------------------------------------------------
    public void Limpiar(){
        etprecio.setText("");
        etstock.setText("");

    }
}