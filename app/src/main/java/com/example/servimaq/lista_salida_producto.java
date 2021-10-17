package com.example.servimaq;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista_salida_P;
import com.example.servimaq.fragments_registros.Salida_Prod;

import java.util.ArrayList;

public class lista_salida_producto extends BaseAdapter {

    Context c;
    LayoutInflater inflater;
    ArrayList<items_lista_salida_P> Lista;
    Salida_Prod contextosp;

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

        TextView tvnombreyapellido,tvcorreo,tvmodopago,tvDNI,tvfechaentrega,tvdivision;


        ListView lvitems;





        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.activity_salida_prod, viewGroup, false);
        SQLConexion db = new SQLConexion();

       // lvitems = itemView.findViewById(R.id.lvitems);
        tvdivision = itemView.findViewById(R.id.tvdivision);
        tvnombreyapellido = itemView.findViewById(R.id.tvnombreyapellido);
        tvcorreo = itemView.findViewById(R.id.tvcorreo);
        tvmodopago = itemView.findViewById(R.id.tvmodopago);
        tvDNI = itemView.findViewById(R.id.tvDNI);
        tvfechaentrega = itemView.findViewById(R.id.tvfechaentrega);




        tvdivision.setText(""+Lista.get(i).getTvdivision());
        tvnombreyapellido.setText(""+Lista.get(i).getTvnombreyapellido());
        tvcorreo.setText(""+Lista.get(i).getTvcorreo());
        tvmodopago.setText(""+Lista.get(i).getTvmodopago());
        tvfechaentrega.setText(""+Lista.get(i).getTvfechaentrega());
        tvDNI.setText(""+Lista.get(i).getTvDNI());

        Intent SALIDA = new Intent(c,Salida_Prod.class);
        SALIDA.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);




        return itemView;
    }

}
