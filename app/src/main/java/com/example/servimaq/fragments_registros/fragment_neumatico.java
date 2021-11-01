package com.example.servimaq.fragments_registros;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.menu_opciones;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class fragment_neumatico extends Fragment {

    View vista;
    EditText etprecio,etstock;
    Button btnRegistrar, btnCancelar;
    Spinner Spi_especificacion,Spi_vehiculo;
    TextView Tv_ver,Tv_ver1;
    ImageView ivFoto_neumatico, ivFoto_vehiculo;

    String vehiculo,especificacion;
    ArrayList<String> op = new ArrayList<>();
    ArrayList<String> info = new ArrayList<>();
    ArrayList<String> op1 = new ArrayList<>();
    ArrayList<String> info1 = new ArrayList<>();
    String VehiculoId="";
    String DetalleLlantaId="";

    ArrayList<String> fotoVehiculo = new ArrayList<>();
    ArrayList<String> fotoNeumatico = new ArrayList<>();

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
        ivFoto_neumatico = vista.findViewById(R.id.ivFoto_neumatico);
        ivFoto_vehiculo = vista.findViewById(R.id.ivFoto_vehiculo);

        Spi_especificacion=vista.findViewById(R.id.Spi_especificacion);
        Spi_vehiculo=vista.findViewById(R.id.Spi_vehiculo);

        //CARGAR DATO DEL SPINNER - VEHICULO *************************************************************************************************************************
        //EVENTO DEL HEROKU - DB SELECCION VEHICULO ************************************************************************************************************
        AndroidNetworking.initialize(getContext());
        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Vehiculo_POST_SELECT_ALL.php")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String validarDatos = response.getString("data");
                            String aux_VehiculoId = "";
                            int contar = 1;

                            Log.e("respuesta: ", "" + validarDatos);

                            if (!validarDatos.equals("[]")) {
                                JSONArray array = response.getJSONArray("data");
                                do {
                                    JSONObject object = array.getJSONObject(contar-1);
                                    Log.e("Recorrido: ", "aca llegue");
                                    aux_VehiculoId = object.getString("vehiculoid");

                                    op.add(aux_VehiculoId);
                                    vehiculo = "→ Tipo de Vehiculo: "+object.getString("tipovehiculo")+
                                            "\n→ Marca del Vehiculo: "+ object.getString("marcavehiculo") +
                                            "\n→ Modelo del Vehiculo: "+object.getString("modelovehiculo");
                                    info.add(vehiculo);
                                    fotoVehiculo.add(object.getString("fotovehiculo"));
                                    contar++;
                                } while (contar <= array.length());

                                ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, op);
                                Spi_vehiculo.setAdapter(adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(),"Error: "+anError.toString(),Toast.LENGTH_SHORT).show();
                    }
                });


        Spi_vehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);

                if(!fotoVehiculo.get(i).isEmpty()){
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference islandRef = storageRef.child(fotoVehiculo.get(i));
                    final long ONE_MEGABYTE = 480 * 480;

                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            ivFoto_vehiculo.setImageBitmap(bitmap);

                            Tv_ver1.setText(info.get(i));
                            VehiculoId=op.get(i);
                        }
                    });
                }else{
                    Tv_ver1.setText(info.get(i));
                    VehiculoId=op.get(i);
                    ivFoto_vehiculo.setImageDrawable(getResources().getDrawable(R.drawable.no_imagen));
                }


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
                    especificacion="→ NombreMarca: "+rs.getString(2)+
                            "\n→ IndiceCarga: "+rs.getString(3)+
                            "\n→ IndiceVelocidad:"+rs.getString(4) +
                            "\n→ Construccion: "+rs.getString(6)+
                            "\n→ PresionMaxima: "+rs.getString(7)+
                            "\n→ Clasificacion: "+rs.getString(8)+
                            "\n→ FechaFabricacion: "+rs.getString(9)+
                            "\n→ MedidaLlantaId: "+rs.getString(10);

                    info1.add(especificacion);
                    fotoNeumatico.add(rs.getString(5));
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

                //CAMBIAR COLOR DE TEXTO DEL SPINNER---------------------------------------
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference islandRef = storageRef.child(fotoNeumatico.get(i));
                final long ONE_MEGABYTE = 480 * 480;

                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        ivFoto_neumatico.setImageBitmap(bitmap);
                    }
                });

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
                etprecio.requestFocus();

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