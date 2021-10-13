package com.example.servimaq.fragments_registros;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.menu_opciones;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link detalle_llanta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detalle_llanta extends Fragment {
    EditText MarcaVehiculo,IndiceCarga,IndiceVelocidad,Construccion,PresionMaxima,Clasificacion;
    ImageView ivFotoLlanta;
    Button BtnFotoLlanta,btnRegistrar, btnCancelar;
    Spinner spinner1;
    View vista;
    Uri ruta = null;
    TextView Tv_cargar;
    String medidas;
    ArrayList<String> opciones = new ArrayList<>();
    ArrayList<String> informacion = new ArrayList<>();
    String MedidaLlantaId="";
    public static EditText Fecha;
    public static int dia, mes, año;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public detalle_llanta() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment detalle_llanta.
     */
    // TODO: Rename and change types and number of parameters
    public static detalle_llanta newInstance(String param1, String param2) {
        detalle_llanta fragment = new detalle_llanta();
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
        vista = inflater.inflate(R.layout.fragment_detalle_llanta, container, false);
        MarcaVehiculo = vista.findViewById(R.id.EtNombreMarca);
        IndiceCarga = vista.findViewById(R.id.EtIndiceCarga);
        IndiceVelocidad = vista.findViewById(R.id.EtIndiceVelocidad);
        Construccion = vista.findViewById(R.id.EtConstruccion);
        PresionMaxima = vista.findViewById(R.id.EtPresionMaxima);
        Clasificacion = vista.findViewById(R.id.EtClasificacion);
        Fecha = vista.findViewById(R.id.EtFechaFabricacion);
        ivFotoLlanta = vista.findViewById(R.id.ivFoto_Llanta);
        btnRegistrar = vista.findViewById(R.id.btnRegistrar);
        btnCancelar = vista.findViewById(R.id.btnCancelar);
        Tv_cargar=vista.findViewById(R.id.Tv_cargar);
        spinner1=vista.findViewById(R.id.Spi_MedidaLlanta);
        BtnFotoLlanta=vista.findViewById(R.id.btnFoto_Llanta);


        //CARGAR DATO DEL SPINNER XD----------------------------------------------------------------
        try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(getContext()).createStatement();
            ResultSet rs = st.executeQuery("select * from T_MedidaLlanta");
            if (!rs.next()) {
                MedidaLlantaId = "";
            }
            else {
                do {

                    opciones.add(rs.getString(1));///jalando ID

                    medidas=" Ancho: "+rs.getString(2)+" \n Diametro: "+rs.getString(3)+" \n Perfil: "+rs.getString(4)+"\n Milimetro-Cocada: "+rs.getString(5);

                    informacion.add(medidas);
                } while (rs.next());///va agregando cada ID

                ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, opciones);
                spinner1.setAdapter(adapter);

            }
        }

        catch (Exception e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Tv_cargar.setText(informacion.get(i));
                MedidaLlantaId=opciones.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        BtnFotoLlanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarImagen();
            }
        });

        //BOTON REGISTRO -----------------------------------------------------------------------------------------
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  MarcaV = MarcaVehiculo.getText().toString(),
                        IndiceCarg = IndiceCarga.getText().toString(),
                        IndiceVeloci = IndiceVelocidad.getText().toString(),
                        Construcc = Construccion.getText().toString(),
                        PresionMax=PresionMaxima.getText().toString(),
                        Clasi=Clasificacion.getText().toString(),
                        fech=Fecha.getText().toString(),

                        Foto="";
                if(ruta==null){
                    Foto = "";
                }else{
                    Foto = ruta.toString();
                }


                SQLConexion db = new SQLConexion();
                db.RegistroDetalleLlanta(getContext(),MarcaV,Integer.parseInt(IndiceCarg),IndiceVeloci,Foto,Construcc,Integer.parseInt(PresionMax),Clasi,fech ,MedidaLlantaId);
                Limpiar();
            }
        });

        //BOTON CANCELAR -----------------------------------------------------------------------------------------
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Limpiar();
            }
        });

        //Mostrar calendario*************************************************************************
        Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        return vista;
    }

    ///BLOQUE FECHA DINAMICA**************************************************
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(),this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            dia = i2;
            mes = i1+1;
            año = i;
            if(dia<10 && mes<10){
                Fecha.setText("0"+dia+"/"+"0"+mes+"/"+año);
            }else if(dia<10 && mes>9){
                Fecha.setText("0"+dia+"/"+mes+"/"+año);
            }else if(dia>9 && mes<10){
                Fecha.setText(dia+"/"+"0"+mes+"/"+año);
            }else{
                Fecha.setText(dia+"/"+mes+"/"+año);
            }

        }
    }

    //CARGA DE IMAGEN--------------------------------------------------------------------------------------
    private void CargarImagen(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/");
        startActivityForResult(i.createChooser(i,"seleccione la aplicación"),10);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode==getActivity().RESULT_OK){
            ruta = data.getData();
            ivFotoLlanta.setImageURI(ruta);
        }
    }


    //LIMPIAR CAMPOS--------------------------------------------------------------------------------------
    public void Limpiar(){
        MarcaVehiculo.setText("");
        ivFotoLlanta.setImageDrawable(getResources().getDrawable(R.drawable.no_imagen));
        IndiceCarga.setText("");
        IndiceVelocidad.setText("");
        Construccion.setText("");
        PresionMaxima.setText("");
        Clasificacion.setText("");
        Fecha.setText("");;
        ruta = null;
    }

}