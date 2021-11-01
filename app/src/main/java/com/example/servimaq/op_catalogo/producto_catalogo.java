package com.example.servimaq.op_catalogo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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

import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista;
import com.example.servimaq.op_salida.Salida_Prod;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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


        //OBTENER URL DE LA IMAGEN
        /*storageRef.child(Lista.get(i).getFotoVehiculo()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ivFoto.setImageURI(uri);
                Log.e("Contar","___: "+Lista.get(i).getFotoVehiculo()+".jpeg");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });*/
        //Log.e("Contar","___: "+bytes);


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

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //--ACTUALIZAR DATOS DE LA TABLA NEUMATICO------------------------------------------------------------------------------
                try {
                    SQLConexion conexion =new SQLConexion();
                    PreparedStatement ps = conexion.ConexionDB(c.getApplicationContext()).prepareStatement(
                            "delete from T_Llanta where LlantaId = ?;");
                    ps.setString(1,tvLlantaId.getText().toString());
                    ps.executeUpdate();
                    ps.close();
                    Toast.makeText(c.getApplicationContext(),"Producto Eliminado",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    Toast.makeText(c.getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }


                Intent detalle = new Intent(c, Catalogo.class);
                //Permite abrir una nueva vista
                detalle.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(detalle);

            }
        });

        return itemView;
    }

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
        try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(v.getContext()).createStatement();
            ResultSet rs = st.executeQuery("select codPedido from T_Pedido;");
            if (!rs.next()) {
                Toast.makeText(c,"No se encontraron registros",Toast.LENGTH_SHORT).show();
                op_codPedido = null;
            }
            else {
                do {
                    //ARRAY LIST - INFORMACION PARA EL SPINNER-------------
                    datos_codPedido.add(rs.getString(1));
                } while (rs.next());///va agregando cada ID

                ArrayAdapter adapter = new ArrayAdapter(v.getContext(),android.R.layout.simple_spinner_dropdown_item, datos_codPedido);
                spncodPedido.setAdapter(adapter);
            }
            rs.close();
        }
        catch (Exception e) {
            Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }//FIN Carga--------------------------------------------------------------------------------

        //--CARGAR INICIAL DE DATOS - PEDIDO ------------------------------------------------------------------------------
        try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(v.getContext()).createStatement();
            ResultSet rs = st.executeQuery("select * from T_Pedido where codPedido = 'COD-001';");
            if (!rs.next()) {
                Toast.makeText(c,"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }
            else {
                tvNombresCliente.setText(rs.getString(2));
                tvApellidosCliente.setText(rs.getString(3));
                tvCorreo.setText(rs.getString(4));
                tvFechaActual.setText(rs.getString(5));
                tvFechaEntrega.setText(rs.getString(6));
                tvModoPago.setText(rs.getString(7));
                tvDNI.setText(rs.getString(8));
            }
            rs.close();
        }
        catch (Exception e) {
            Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }//FIN Carga--------------------------------------------------------------------------------


        //OBTENER ID DE LA POSICION*************************************************************************
        spncodPedido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                op_codPedido = datos_codPedido.get(i);
                Log.e("MENSAJE","____ "+op_codPedido);
                //CARGAR DATOS SELECCIONADOS POR EL SPINNER
                try {
                    SQLConexion db =new SQLConexion();
                    Statement st = db.ConexionDB(v.getContext()).createStatement();
                    ResultSet rs = st.executeQuery("select * from T_Pedido where codPedido = '"+op_codPedido +"';");
                    if (!rs.next()) {
                        Toast.makeText(v.getContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                    }else {
                        do {
                            tvNombresCliente.setText(rs.getString(2));
                            tvApellidosCliente.setText(rs.getString(3));
                            tvCorreo.setText(rs.getString(4));
                            tvFechaActual.setText(rs.getString(5));
                            tvFechaEntrega.setText(rs.getString(6));
                            tvModoPago.setText(rs.getString(7));
                            tvDNI.setText(rs.getString(8));
                        } while (rs.next());///va agregando cada pedido
                    }
                    rs.close();
                } catch (Exception e) {
                    Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }//FIN SELECT-------------
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //CONSTRUCCION DE LA VISTA DIALOG************************
        builder.setView(v);
        alertDialog = builder.create();

        //BOTONES DEL DIALOG*************************************************
        btnDgAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = 1;
                double precio = 0.0, total = 0.0; //op_codPedido
                String codigo = Lista.get(posicion).getLlantaId();
                Log.e("pos:::::","__"+Lista.get(posicion).getLlantaId());

                //OBTENER PRECIO DEL ITEM AGREGADO AL LISTADO***************************
                try {
                    SQLConexion db =new SQLConexion();
                    Statement st = db.ConexionDB(v.getContext()).createStatement();
                    ResultSet rs = st.executeQuery("select Precio from T_Llanta where LlantaId = '"+codigo+"';");
                    if (!rs.next()) {
                        Toast.makeText(c,"No se encontraron registros",Toast.LENGTH_SHORT).show();
                    }else {
                        do {
                            precio = rs.getDouble(1);
                            total = precio;
                        } while (rs.next());///va agregando cada pedido
                    }
                    rs.close();
                    db.RegistroListado(v.getContext(),cantidad,precio,total,op_codPedido,codigo);
                } catch (Exception e) {
                    Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }//FIN SELECT-------------


                Intent enviar_codigo = new Intent(v.getContext(), Salida_Prod.class);
                enviar_codigo.putExtra("op_codPedido",op_codPedido);
            }
        });

        btnDgCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }

}
