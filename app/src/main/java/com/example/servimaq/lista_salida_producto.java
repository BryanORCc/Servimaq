package com.example.servimaq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.servimaq.db.items_lista;
import com.example.servimaq.db.items_lista_salida_P;

import java.util.ArrayList;

public class lista_salida_producto extends BaseAdapter {

    Context c;
    LayoutInflater inflater;
    ArrayList<items_lista_salida_P> Lista;

    public lista_salida_producto(Context c, ArrayList<items_lista_salida_P> Lista){
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


        EditText etstock;
        Button btnaceptar, btneliminar;
        TextView tvnombre, tventrega, tvmodopago,tvPrecio, tvcod;

        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.activity_salida_prod, viewGroup, false);


      //  tvcod =itemView.findViewById(R.id.tvcod);
        tvnombre =itemView.findViewById(R.id.tvnombre);
        tventrega =itemView.findViewById(R.id.tventrega);
        tvmodopago =itemView.findViewById(R.id.tvmodopago);
        tvPrecio =itemView.findViewById(R.id.tvPrecio);
        etstock = itemView.findViewById(R.id.etstock);




        tvnombre.setText(""+Lista.get(i).getnombre());
        tvmodopago.setText(""+Lista.get(i).getmodopago());
         tventrega.setText(""+Lista.get(i).getFentrega());
        tvPrecio.setText(""+Lista.get(i).getPrecio());
        etstock.setText(""+Lista.get(i).getStock());



        return itemView;
    }
}
