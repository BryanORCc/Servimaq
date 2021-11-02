package com.example.servimaq.op_catalogo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista;
import com.example.servimaq.op_salida.Salida_Prod;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class producto_catalogo extends BaseAdapter {

    private Catalogo c;
    LayoutInflater inflater;
    ArrayList<items_lista> Lista;
    Button btnAgregar, btnModificar, btnEliminar;
    LinearLayout btnDetalle;
    ImageView ivFoto;

    Spinner spncodPedido;
    ArrayList<String> datos_codPedido = new ArrayList<>();

    TextView tvNombresCliente, tvApellidosCliente, tvCorreo, tvFechaActual, tvFechaEntrega, tvModoPago, tvDNI;
    String op_codPedido;
    Button btnDgAgregar, btnDgCancelar;
    int posicion;

    /*int n = 0;
    if(n <= Lista.size()){
        n++;
    }*/

    public producto_catalogo(Catalogo c, ArrayList<items_lista> Lista){
        this.c = c;
        this.Lista = Lista;
    }

    @Override
    public int getCount() {
        return Lista.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TextView tvLlantaId, tvMarca, tvAncho, tvDiametro, tvPerfil, tvMmCocada, tvPrecio, tvStock;

        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.producto_lista_catalogo, viewGroup, false);

        tvLlantaId = itemView.findViewById(R.id.tvLlantaId);
        tvMarca = itemView.findViewById(R.id.tvMarca);
        tvAncho = itemView.findViewById(R.id.tvAncho);
        tvDiametro = itemView.findViewById(R.id.tvDiametro);
        tvPerfil = itemView.findViewById(R.id.tvPerfil);
        tvMmCocada = itemView.findViewById(R.id.tvMmCocada);
        tvPrecio = itemView.findViewById(R.id.tvPrecio);
        tvStock = itemView.findViewById(R.id.tvStock);

        btnAgregar = itemView.findViewById(R.id.btnAgregar);
        btnDetalle = itemView.findViewById(R.id.btnDetalle);
        btnModificar = itemView.findViewById(R.id.btnModificar);
        btnEliminar = itemView.findViewById(R.id.btnEliminar);
        ivFoto = itemView.findViewById(R.id.ivFoto);

        //CARGAR DATOS DE LISTA *******************************
        tvLlantaId.setText(Lista.get(i).getLlantaId());
        tvMarca.setText(Lista.get(i).getNombreMarca());
        tvAncho.setText(""+Lista.get(i).getAncho());
        tvDiametro.setText(""+Lista.get(i).getDiametro());
        tvPerfil.setText(""+Lista.get(i).getPerfil());
        tvMmCocada.setText(""+Lista.get(i).getMmCocada());
        tvPrecio.setText(""+Lista.get(i).getPrecio());
        tvStock.setText(""+Lista.get(i).getStock());

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = storageRef.child(Lista.get(i).getFotoVehiculo());
        final long ONE_MEGABYTE = 480 * 480;

        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                //numeros++;
                //Log.e("Contar","___: "+numeros);
                ivFoto.setImageBitmap(bitmap);
            }
        });


        //BOTON AGREGAR A LISTA ------------************************------------------------------------------------------------------------XXX
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OBTENER POSICION PARA EL DIALOG
                posicion = i;
                datos_codPedido.clear();
                createCustomDialog().show();
            }
        });

        //BOTON MODIFICAR A LISTA -----------------------------------------------------------------------------------------------------------
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String llantaId = Lista.get(i).getLlantaId(),
                        NombreMarca = Lista.get(i).getNombreMarca(),
                        IndiceVelocidad = Lista.get(i).getIndiceVelocidad(),
                        Construccion = Lista.get(i).getConstruccion(),
                        Clasificacion = Lista.get(i).getClasificacion(),
                        FechaFabricacion = Lista.get(i).getFechaFabricacion(),
                        FotoLlanta = Lista.get(i).getFotoLlanta(),
                        FotoVehiculo = Lista.get(i).getFotoVehiculo(),
                        MarcaVehiculo = Lista.get(i).getMarcaVehiculo(),
                        ModeloVehiculo = Lista.get(i).getModeloVehiculo(),
                        PresionMaxima = Lista.get(i).getPresionMaxima(),
                        Precio = Lista.get(i).getPrecio();
                int Ancho = Lista.get(i).getAncho(),
                        Diametro = Lista.get(i).getDiametro(),
                        Perfil = Lista.get(i).getPerfil(),
                        Stock = Lista.get(i).getStock(),
                        IndiceCarga = Lista.get(i).getIndiceCarga();
                double MmCocada = Lista.get(i).getMmCocada();
                String TipoVehiculo = Lista.get(i).getTipoVehiculo(),
                        DetalleLlantaId = Lista.get(i).getDetalleLlantaId(),
                        VehiculoId = Lista.get(i).getVehiculoId(),
                        MedidaLlantaId = Lista.get(i).getMedidaLlantaId();

                Intent detalle = new Intent(c, detalle_producto.class);
                //Permite abrir una nueva vista
                detalle.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                detalle.putExtra("llantaId",llantaId);
                detalle.putExtra("NombreMarca",NombreMarca);
                detalle.putExtra("IndiceCarga",IndiceCarga);
                detalle.putExtra("IndiceVelocidad",IndiceVelocidad);
                detalle.putExtra("Construccion",Construccion);
                detalle.putExtra("Clasificacion",Clasificacion);
                detalle.putExtra("FechaFabricacion",FechaFabricacion);
                detalle.putExtra("FotoLlanta",FotoLlanta);
                detalle.putExtra("FotoVehiculo",FotoVehiculo);
                detalle.putExtra("MarcaVehiculo",MarcaVehiculo);
                detalle.putExtra("ModeloVehiculo",ModeloVehiculo);
                detalle.putExtra("Ancho",Ancho);
                detalle.putExtra("Diametro",Diametro);
                detalle.putExtra("Perfil",Perfil);
                detalle.putExtra("MmCocada",MmCocada);
                detalle.putExtra("Stock",Stock);
                detalle.putExtra("PresionMaxima",PresionMaxima);
                detalle.putExtra("Precio",Precio);
                detalle.putExtra("TipoVehiculo",TipoVehiculo);
                detalle.putExtra("DetalleLlantaId",DetalleLlantaId);
                detalle.putExtra("VehiculoId",VehiculoId);
                detalle.putExtra("MedidaLlantaId",MedidaLlantaId);

                detalle.putExtra("estado",true);
                view.getContext().startActivity(detalle);
            }
        });

        //BOTON DETALLE DEL PRODUCTO SELECCIONADO ------------************************------------------------------------------------------------------------XXX
        btnDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String llantaId = Lista.get(i).getLlantaId(),
                        NombreMarca = Lista.get(i).getNombreMarca(),
                        IndiceVelocidad = Lista.get(i).getIndiceVelocidad(),
                        Construccion = Lista.get(i).getConstruccion(),
                        Clasificacion = Lista.get(i).getClasificacion(),
                        FechaFabricacion = Lista.get(i).getFechaFabricacion(),
                        FotoLlanta = Lista.get(i).getFotoLlanta(),
                        FotoVehiculo = Lista.get(i).getFotoVehiculo(),
                        MarcaVehiculo = Lista.get(i).getMarcaVehiculo(),
                        ModeloVehiculo = Lista.get(i).getModeloVehiculo(),
                        PresionMaxima = Lista.get(i).getPresionMaxima(),
                        Precio = Lista.get(i).getPrecio();
                int Ancho = Lista.get(i).getAncho(),
                        Diametro = Lista.get(i).getDiametro(),
                        Perfil = Lista.get(i).getPerfil(),
                        Stock = Lista.get(i).getStock(),
                        IndiceCarga = Lista.get(i).getIndiceCarga();
                double MmCocada = Lista.get(i).getMmCocada();
                String TipoVehiculo = Lista.get(i).getTipoVehiculo(),
                        DetalleLlantaId = Lista.get(i).getDetalleLlantaId(),
                        VehiculoId = Lista.get(i).getVehiculoId(),
                        MedidaLlantaId = Lista.get(i).getMedidaLlantaId();

                Intent detalle = new Intent(c,detalle_producto.class);

                //Permite abrir una nueva vista
                detalle.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                detalle.putExtra("llantaId",llantaId);
                detalle.putExtra("NombreMarca",NombreMarca);
                detalle.putExtra("IndiceCarga",IndiceCarga);
                detalle.putExtra("IndiceVelocidad",IndiceVelocidad);
                detalle.putExtra("Construccion",Construccion);
                detalle.putExtra("Clasificacion",Clasificacion);
                detalle.putExtra("FechaFabricacion",FechaFabricacion);
                detalle.putExtra("FotoLlanta",FotoLlanta);
                detalle.putExtra("FotoVehiculo",FotoVehiculo);
                detalle.putExtra("MarcaVehiculo",MarcaVehiculo);
                detalle.putExtra("ModeloVehiculo",ModeloVehiculo);
                detalle.putExtra("Ancho",Ancho);
                detalle.putExtra("Diametro",Diametro);
                detalle.putExtra("Perfil",Perfil);
                detalle.putExtra("MmCocada",MmCocada);
                detalle.putExtra("Stock",Stock);
                detalle.putExtra("PresionMaxima",PresionMaxima);
                detalle.putExtra("Precio",Precio);
                detalle.putExtra("TipoVehiculo",TipoVehiculo);
                detalle.putExtra("DetalleLlantaId",DetalleLlantaId);
                detalle.putExtra("VehiculoId",VehiculoId);
                detalle.putExtra("MedidaLlantaId",MedidaLlantaId);

                view.getContext().startActivity(detalle);

            }
        });


        //BOTON ELIMINAR PRODUCTO DE LA LISTA ------------************************------------------------------------------------------------------------XXX
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AndroidNetworking.initialize(c);
                Map<String,String> insertar = new HashMap<>();
                insertar.put("LlantaId",tvLlantaId.getText().toString());
                Log.e("ID a borrar: ",""+tvLlantaId.getText().toString());
                JSONObject datosJSON = new JSONObject(insertar);

                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Llanta_POST_DELETE_WHERE.php")
                        .addJSONObjectBody(datosJSON)
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String validarDatos = response.getString("data");
                                    Log.e("respuesta eliminacion: ",""+validarDatos);
                                    Toast.makeText(c.getApplicationContext(),"Producto Eliminado",Toast.LENGTH_SHORT).show();

                                    Intent detalle = new Intent(c, Catalogo.class);
                                    //Permite abrir una nueva vista
                                    detalle.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    view.getContext().startActivity(detalle);

                                } catch (JSONException e) {
                                    Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(c,"Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return itemView;
    }


    //DIALOG DE LA OPCION DE AGREGAR A LISTA DE SALIDA ------------************************------------------------------------------------------------------------XXX
    public AlertDialog createCustomDialog() {

        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        // Get the layout inflater
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.dialog_agregar, null);

        //DIALOG DE AGREGAR -- REFERENCIAS ID'S******************************
        spncodPedido = v.findViewById(R.id.spncodPedido);
        tvNombresCliente = v.findViewById(R.id.tvNombresCliente);
        tvApellidosCliente = v.findViewById(R.id.tvApellidosCliente);
        tvCorreo = v.findViewById(R.id.tvCorreo);
        tvFechaActual = v.findViewById(R.id.tvFechaActual);
        tvFechaEntrega = v.findViewById(R.id.tvFechaEntrega);
        tvModoPago = v.findViewById(R.id.tvModoPago);
        tvDNI = v.findViewById(R.id.tvDNI);

        btnDgAgregar = v.findViewById(R.id.btnDgAgregar);
        btnDgCancelar = v.findViewById(R.id.btnDgCancelar);

        //**********CARGAR DATOS AL DIALOG ***************************************************
        //--CARGAR DATOS A LOS SPINNERS - PEDIDO ------------------------------------------------------------------------------
        AndroidNetworking.initialize(c);
        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Pedido_POST_SELECT.php")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String validarDatos = response.getString("data");
                            int contar= 1;
                            Log.e("respuesta: ",""+validarDatos);

                            if (!validarDatos.equals("[]")) {
                                JSONArray array = response.getJSONArray("data");
                                do {
                                    //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                                    JSONObject object = array.getJSONObject(contar-1);
                                    datos_codPedido.add(object.getString("codpedido"));
                                    contar++;
                                } while (contar <= array.length());

                                ArrayAdapter adapter = new ArrayAdapter(v.getContext(),android.R.layout.simple_spinner_dropdown_item, datos_codPedido);
                                spncodPedido.setAdapter(adapter);

                            }else{
                                Toast.makeText(c,"No se encontraron registros",Toast.LENGTH_SHORT).show();
                                op_codPedido = null;
                            }

                        } catch (JSONException e) {
                            Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(c,"Error: "+anError.toString(),Toast.LENGTH_SHORT).show();
                    }
                });//FIN Carga--------------------------------------------------------------------------------


        //--CARGAR INICIAL DE DATOS - PEDIDO ------------------------------------------------------------------------------
        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Pedido_POST_SELECT_INICIAL.php")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String validarDatos = response.getString("data");
                            int contar= 1;
                            Log.e("respuesta: ",""+validarDatos);

                            if (!validarDatos.equals("[]")) {
                                JSONArray array = response.getJSONArray("data");
                                do {
                                    //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                                    JSONObject object = array.getJSONObject(contar-1);

                                    tvNombresCliente.setText(object.getString("nombrescliente"));
                                    tvApellidosCliente.setText(object.getString("apellidoscliente"));
                                    tvCorreo.setText(object.getString("correo"));
                                    tvFechaActual.setText(object.getString("fechaactual"));
                                    tvFechaEntrega.setText(object.getString("fechaentrega"));
                                    tvModoPago.setText(object.getString("modopago"));
                                    tvDNI.setText(object.getString("dni"));

                                    contar++;
                                } while (contar <= array.length());

                            }else{
                                Toast.makeText(c,"No se encontraron pedidos",Toast.LENGTH_SHORT).show();
                                op_codPedido = null;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(c,"Error: "+anError.toString(),Toast.LENGTH_SHORT).show();
                    }
                });//FIN Carga--------------------------------------------------------------------------------



        //OBTENER ID DE LA POSICION*************************************************************************
        spncodPedido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                op_codPedido = datos_codPedido.get(i);
                Log.e("MENSAJE","____ "+op_codPedido);

                Map<String,String> insertar = new HashMap<>();
                insertar.put("codPedido",op_codPedido);
                JSONObject datosJSON = new JSONObject(insertar);

                //CARGAR DATOS SELECCIONADOS POR EL SPINNER
                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Pedido_POST_SELECT_ALL_WHERE.php")
                        .addJSONObjectBody(datosJSON)
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String validarDatos = response.getString("data");
                                    int contar= 1;
                                    Log.e("respuesta: ",""+validarDatos);

                                    if (!validarDatos.equals("[]")) {
                                        JSONArray array = response.getJSONArray("data");
                                        do {
                                            //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                                            JSONObject object = array.getJSONObject(contar-1);

                                            tvNombresCliente.setText(object.getString("nombrescliente"));
                                            tvApellidosCliente.setText(object.getString("apellidoscliente"));
                                            tvCorreo.setText(object.getString("correo"));
                                            tvFechaActual.setText(object.getString("fechaactual"));
                                            tvFechaEntrega.setText(object.getString("fechaentrega"));
                                            tvModoPago.setText(object.getString("modopago"));
                                            tvDNI.setText(object.getString("dni"));

                                            contar++;
                                        } while (contar <= array.length());

                                    }else{
                                        Toast.makeText(c,"No se encontraron pedidos",Toast.LENGTH_SHORT).show();
                                        op_codPedido = null;
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(c,"Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });//FIN SELECT-------------
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });//FIN seleccion codigo **************************************


        //CONSTRUCCION DE LA VISTA DIALOG************************
        builder.setView(v);
        alertDialog = builder.create();


        //BOTONES DEL DIALOG*************************************************
        btnDgAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String codigo = Lista.get(posicion).getLlantaId();
                Log.e("pos:::::","__"+Lista.get(posicion).getLlantaId());

                //OBTENER PRECIO DEL ITEM AGREGADO AL LISTADO***************************
                Map<String,String> insertar = new HashMap<>();
                insertar.put("LlantaId",codigo);
                JSONObject datosJSON = new JSONObject(insertar);

                Map<String,String> insertar2 = new HashMap<>();
                insertar2.put("codPedido",op_codPedido);

                Map<String,String> insertar3 = new HashMap<>();
                insertar3.put("codPedido",op_codPedido);

                Log.e("PUT 3:: ",op_codPedido+" - "+codigo);

                //CARGAR PRECIO DE ITEM SELECCIONADO POR EL SPINNER ********************************************************************************************************************
                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Llanta_POST_SELECT_PRECIO.php")
                        .addJSONObjectBody(datosJSON)
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String validarDatos = response.getString("data");
                                    int contar= 1;

                                    int cantidad = 1;
                                    String precio = "S0.00", total = "S0.00";

                                    Log.e("respuesta: ",""+validarDatos);

                                    if (!validarDatos.equals("[]")) {
                                        JSONArray array = response.getJSONArray("data");
                                        //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                                        JSONObject object = array.getJSONObject(contar-1);
                                        precio = object.getString("precio");
                                        total = precio;


                                        insertar3.put("Cantidad",""+cantidad);
                                        insertar3.put("Precio",precio);
                                        insertar3.put("Total",total);
                                        insertar3.put("LlantaId",codigo);

                                        Log.e("PUT:: ",""+cantidad +" - "+ precio+" - "+total+" - "+codigo);


                                        //EVENTO DEL HEROKU DB SELECCION************************************************************************************************************
                                        AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Listado_POST_SELECT.php")
                                                .setPriority(Priority.IMMEDIATE)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {

                                                        String ItemId = "", aux_ItemId = "";
                                                        int contar = 1, extraer = 0;

                                                        try {
                                                            String validarDatos = response.getString("data");
                                                            int pos = 1;
                                                            Log.e("respuesta: ", "" + validarDatos);
                                                            //--VALIDAR ********************************************************************************************************
                                                            if (validarDatos.equals("[]")) {
                                                                ItemId = "SVQSAC-001";
                                                            } else {
                                                                JSONArray array = response.getJSONArray("data");
                                                                do {
                                                                    JSONObject object = array.getJSONObject(contar-1);
                                                                    Log.e("Recorrido: ", "aca llegue");
                                                                    aux_ItemId = object.getString("itemid");
                                                                    Log.e("id capturado::: ", "" + aux_ItemId);
                                                                    extraer = Integer.parseInt(aux_ItemId.substring(aux_ItemId.length() - pos));

                                                                    if (extraer != contar && contar <= 9 && contar >= 1) {
                                                                        ItemId = "SVQSAC-00" + contar;
                                                                        break;
                                                                    }else if(extraer==0){
                                                                        pos = 2;
                                                                        extraer = Integer.parseInt(aux_ItemId.substring(aux_ItemId.length()-pos));
                                                                        if(extraer!=contar && contar<=99 && contar>=10){
                                                                            ItemId = "SVQSAC-0"+contar;
                                                                            break;
                                                                        }
                                                                    }else if(extraer!=contar && contar<=99 && contar>=10){
                                                                        ItemId = "SVQSAC-0"+contar;
                                                                        break;
                                                                    }
                                                                    contar++;
                                                                } while (contar <= array.length());

                                                                if (contar <= 9 && extraer != contar) {
                                                                    ItemId = "SVQSAC-00" + contar;
                                                                } else if (contar >= 10 && contar <= 99 && extraer != contar) {
                                                                    ItemId = "SVQSAC-0" + contar;
                                                                }

                                                            }//FIN IF VALIDACION REGISTROS----------------------------------------------------------------

                                                            insertar3.put("ItemId",ItemId);

                                                            Log.e("id a insertar: ", "" + ItemId);

                                                            insertar2.put("LlantaId",codigo);
                                                            JSONObject datosJSON2 = new JSONObject(insertar2);

                                                            //VALIDACION DE REGISTROS PEDIDO y NEUMATICO EN LISTA************************************************
                                                            AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Listado_POST_SELECT_WHERE_VAL.php")
                                                                    .addJSONObjectBody(datosJSON2)
                                                                    .setPriority(Priority.IMMEDIATE)
                                                                    .build()
                                                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                                                        @Override
                                                                        public void onResponse(JSONObject response) {

                                                                            try {
                                                                                String validarDatos = response.getString("data");
                                                                                Log.e("respuesta: ", "" + validarDatos);

                                                                                //--VALIDAR ********************************************************************************************************
                                                                                if (validarDatos.equals("[]")) {

                                                                                    JSONObject datosJSON3 = new JSONObject(insertar3);
                                                                                    //EVENTO DEL HEROKU DB INSERCION*****************************************************************************
                                                                                    AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Listado_POST_INSERT.php")
                                                                                            .addJSONObjectBody(datosJSON3)
                                                                                            .setPriority(Priority.MEDIUM)
                                                                                            .build()
                                                                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                                                                @Override
                                                                                                public void onResponse(JSONObject response) {
                                                                                                    EstiloToast(c,"Agregacion exitosa");
                                                                                                    Intent enviar_codigo = new Intent(v.getContext(), Salida_Prod.class);
                                                                                                    enviar_codigo.putExtra("op_codPedido",op_codPedido);
                                                                                                }

                                                                                                @Override
                                                                                                public void onError(ANError anError) {

                                                                                                }
                                                                                            });

                                                                                }else{
                                                                                    Toast.makeText(c,"Neumatico ya registrado en la Lista de salida de productos\nPor favor, verificar en su listado",Toast.LENGTH_LONG).show();
                                                                                }

                                                                            }catch (Exception e){
                                                                                e.printStackTrace();
                                                                            }

                                                                        }

                                                                        @Override
                                                                        public void onError(ANError anError) {

                                                                        }
                                                                    });



                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }

                                                    @Override
                                                    public void onError(ANError anError) {
                                                        Toast.makeText(c,"Error:" + anError.getErrorDetail(),Toast.LENGTH_SHORT).show();
                                                    }

                                                });//EVENTO DEL HEROKU DB SELECCION------------------------------------------------


                                    }else{
                                        Toast.makeText(c,"No se encontraron registros",Toast.LENGTH_SHORT).show();
                                    }



                                } catch (JSONException e) {
                                    Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(c,"Error: "+anError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });//FIN SELECT-------------
            }
        });//FIN PRIMER SELECT--------------------------------


        btnDgCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }

    public void EstiloToast(Context c, String mensaje){
        Toast toast = Toast.makeText(c,mensaje, Toast.LENGTH_SHORT);
        View vista = toast.getView();
        vista.setBackgroundResource(R.drawable.estilo_color_x);
        toast.setGravity(Gravity.CENTER,0,0);
        TextView text = (TextView) vista.findViewById(android.R.id.message);
        text.setTextColor(Color.parseColor("#FFFFFF"));
        text.setTextSize(16);
        toast.show();
    }

}
