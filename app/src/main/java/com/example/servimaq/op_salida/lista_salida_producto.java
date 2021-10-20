package com.example.servimaq.op_salida;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista_salida_P;
import com.example.servimaq.db.Items_Salida_set_get;
import com.example.servimaq.seleccionar_pedido_salida;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class lista_salida_producto extends BaseAdapter {

    Context c;
    LayoutInflater inflater;
    ArrayList<items_lista_salida_P> Lista;
    Salida_Prod contextosp;

    LinearLayout llTransicion;

    public lista_salida_producto(Salida_Prod contextosp,ArrayList<items_lista_salida_P> Lista ){
        this.contextosp = contextosp;
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

        TextView tvnombreyapellido,tvcorreo,tvmodopago,tvDNI,tvfechaentrega,tvCodigo;

        inflater = (LayoutInflater) contextosp.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.activity_listar_salidaa_, viewGroup, false);


        tvCodigo = itemView.findViewById(R.id.tvCodigo);
        tvnombreyapellido = itemView.findViewById(R.id.tvnombreyapellido);
        tvcorreo = itemView.findViewById(R.id.tvcorreo);
        tvmodopago = itemView.findViewById(R.id.tvmodopago);
        tvfechaentrega = itemView.findViewById(R.id.tvfechaentrega);
        tvDNI = itemView.findViewById(R.id.tvDNI);

        llTransicion = itemView.findViewById(R.id.llTransicion);

        tvCodigo.setText(""+Lista.get(i).getTvCodigo());
        tvnombreyapellido.setText(""+Lista.get(i).getTvnombreyapellido());
        tvcorreo.setText(""+Lista.get(i).getTvcorreo());
        tvmodopago.setText(""+Lista.get(i).getTvmodopago());
        tvfechaentrega.setText(""+Lista.get(i).getTvfechaentrega());
        tvDNI.setText(""+Lista.get(i).getTvDNI());


        llTransicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codPedido = Lista.get(i).getTvCodigo();
                Intent items = new Intent(contextosp, seleccionar_pedido_salida.class);
                items.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                items.putExtra("codPedido",codPedido);
                view.getContext().startActivity(items);
            }
        });


        return itemView;
    }

}
