package com.example.servimaq.op_pedidos;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.pedido_lista;
import com.example.servimaq.op_catalogo.Catalogo;
import com.example.servimaq.op_pedidos.Listar_pedidos;
import com.example.servimaq.op_pedidos.detalle_pedido;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pedido_Catalogo extends BaseAdapter {

    Context c;
    LayoutInflater inflater;
    ArrayList<pedido_lista> Lista;
    TextView tvcodigo,tvNombre, tvApellido, tvCorreo, tvFechaActual, tvFechaEntrega, tvPModoPago, tvDni;
    Button btnborrar,btneditar;
    public Pedido_Catalogo(Context c, ArrayList<pedido_lista> Lista){
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
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.pedido_lista, viewGroup, false);

        AndroidNetworking.initialize(c);

        tvcodigo=itemView.findViewById(R.id.tvCodigo);
        tvNombre = itemView.findViewById(R.id.tvNombre);
        tvApellido = itemView.findViewById(R.id.tvApellidos);
        tvCorreo = itemView.findViewById(R.id.tvDCorreo);
        tvFechaActual = itemView.findViewById(R.id.tvFechaA);
        tvFechaEntrega = itemView.findViewById(R.id.tvFechaE);
        tvPModoPago = itemView.findViewById(R.id.tvModoP);
        tvDni = itemView.findViewById(R.id.tvDni);
        btnborrar=itemView.findViewById(R.id.btnborrarPedido);
        btneditar=itemView.findViewById(R.id.btneditarPedido);

        ////// listar pedidos*******
        tvcodigo.setText(Lista.get(i).getCodigo());
        tvNombre.setText(Lista.get(i).getNombre());
        tvApellido.setText(""+Lista.get(i).getApellidos());
        tvCorreo.setText(""+Lista.get(i).getCorreo());
        tvFechaActual.setText(""+Lista.get(i).getFechaA());
        tvFechaEntrega.setText(""+Lista.get(i).getFechaP());
        tvPModoPago.setText(""+Lista.get(i).getModo());
        tvDni.setText(""+Lista.get(i).getDni());


        //BOTON MODIFICAR A LISTA -----------------------------------------------------------------------------------------------------------
        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PedidoId = Lista.get(i).getCodigo(),
                        NombreP = Lista.get(i).getNombre(),
                        ApellidoP = Lista.get(i).getApellidos(),
                        Correo = Lista.get(i).getCorreo(),
                        fechaA = Lista.get(i).getFechaA(),
                        fechaP = Lista.get(i).getFechaP();
                int  dni = Lista.get(i).getDni();

                Intent detalle = new Intent(c, detalle_pedido.class);
                detalle.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                detalle.putExtra("codigo",PedidoId);
                detalle.putExtra("nombre",NombreP);
                detalle.putExtra("apellidos",ApellidoP);
                detalle.putExtra("correo",Correo);
                detalle.putExtra("fecha de actual",fechaA);
                detalle.putExtra("fecha de pago",fechaP);
                detalle.putExtra("Dni",dni);
                detalle.putExtra("modo", Lista.get(i).getModo());
                view.getContext().startActivity(detalle);
            }
        });


       //ELIMINAR PEDIDO -----------**************************************-------------------------::::::::::::::::::::::::::::
        btnborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,String> insertar = new HashMap<>();
                insertar.put("codPedido",tvcodigo.getText().toString());
                JSONObject datosJSON = new JSONObject(insertar);

                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Pedido_POST_DELETE_WHERE.php")
                        .addJSONObjectBody(datosJSON)
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String validarDatos = response.getString("data");
                                    Log.e("respuesta eliminacion: ",""+validarDatos);
                                    Toast.makeText(c.getApplicationContext(),"Pedido Eliminado",Toast.LENGTH_SHORT).show();

                                    Intent detalle = new Intent(c, Listar_pedidos.class);
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
        });//FIN BOTON BORRAR ----------------------------*****************************-------------:::::::::::::::::::

        return itemView;
    }

}
