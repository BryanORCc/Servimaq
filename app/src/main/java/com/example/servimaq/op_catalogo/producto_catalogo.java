package com.example.servimaq.op_catalogo;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista;
import com.example.servimaq.op_catalogo.Catalogo;
import com.example.servimaq.op_catalogo.detalle_producto;

import java.sql.PreparedStatement;
import java.util.ArrayList;

public class producto_catalogo extends BaseAdapter {

    Context c;
    LayoutInflater inflater;
    ArrayList<items_lista> Lista;
    Button btnAgregar, btnModificar, btnEliminar;
    LinearLayout btnDetalle;
    ImageView ivFoto;


    public producto_catalogo(Context c, ArrayList<items_lista> Lista){
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

        tvLlantaId.setText(Lista.get(i).getLlantaId());
        tvMarca.setText(Lista.get(i).getNombreMarca());
        tvAncho.setText(""+Lista.get(i).getAncho());
        tvDiametro.setText(""+Lista.get(i).getDiametro());
        tvPerfil.setText(""+Lista.get(i).getPerfil());
        tvMmCocada.setText(""+Lista.get(i).getMmCocada());
        tvPrecio.setText(""+Lista.get(i).getPrecio());
        tvStock.setText(""+Lista.get(i).getStock());

        /*if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ActivityCompat.checkSelfPermission(c.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    ivFoto.setImageURI(Uri.parse(Lista.get(i).getFotoVehiculo()));
                }
            }
        }else{

        }*/

        //BOTON AGREGAR A LISTA ------------************************------------------------------------------------------------------------
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(c.getApplicationContext(), "Boton Agregar",Toast.LENGTH_SHORT).show();
            }
        });

        //BOTON MODIFICAR A LISTA -----------------------------------------------------------------------------------------------------------
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String llantaId = Lista.get(i).getLlantaId(),
                        NombreMarca = Lista.get(i).getNombreMarca(),
                        IndiceCarga = Lista.get(i).getIndiceCarga(),
                        IndiceVelocidad = Lista.get(i).getIndiceVelocidad(),
                        Construccion = Lista.get(i).getConstruccion(),
                        Clasificacion = Lista.get(i).getClasificacion(),
                        FechaFabricacion = Lista.get(i).getFechaFabricacion(),
                        FotoLlanta = Lista.get(i).getFotoLlanta(),
                        FotoVehiculo = Lista.get(i).getFotoVehiculo(),
                        MarcaVehiculo = Lista.get(i).getMarcaVehiculo(),
                        ModeloVehiculo = Lista.get(i).getModeloVehiculo();
                int Ancho = Lista.get(i).getAncho(),
                        Diametro = Lista.get(i).getDiametro(),
                        Perfil = Lista.get(i).getPerfil(),
                        MmCocada = Lista.get(i).getMmCocada(),
                        Stock = Lista.get(i).getStock(),
                        PresionMaxima = Lista.get(i).getPresionMaxima();
                double Precio = Lista.get(i).getPrecio();
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
                IndiceCarga = Lista.get(i).getIndiceCarga(),
                IndiceVelocidad = Lista.get(i).getIndiceVelocidad(),
                Construccion = Lista.get(i).getConstruccion(),
                Clasificacion = Lista.get(i).getClasificacion(),
                FechaFabricacion = Lista.get(i).getFechaFabricacion(),
                FotoLlanta = Lista.get(i).getFotoLlanta(),
                FotoVehiculo = Lista.get(i).getFotoVehiculo(),
                MarcaVehiculo = Lista.get(i).getMarcaVehiculo(),
                ModeloVehiculo = Lista.get(i).getModeloVehiculo();
                int Ancho = Lista.get(i).getAncho(),
                Diametro = Lista.get(i).getDiametro(),
                Perfil = Lista.get(i).getPerfil(),
                MmCocada = Lista.get(i).getMmCocada(),
                Stock = Lista.get(i).getStock(),
                PresionMaxima = Lista.get(i).getPresionMaxima();
                double Precio = Lista.get(i).getPrecio();
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

}
