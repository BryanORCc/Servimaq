package com.example.servimaq;

import static java.lang.String.valueOf;

import android.content.Context;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class producto_catalogo extends BaseAdapter {

    Context c;
    LayoutInflater inflater;
    ArrayList<items_lista> Lista;

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

        TextView tvMarca, tvAncho, tvDiametro, tvPerfil, tvMmCocada, tvPrecio, tvStock;

        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.producto_lista_catalogo, viewGroup, false);

        tvMarca = itemView.findViewById(R.id.tvMarca);
        tvAncho = itemView.findViewById(R.id.tvAncho);
        tvDiametro = itemView.findViewById(R.id.tvDiametro);
        tvPerfil = itemView.findViewById(R.id.tvPerfil);
        tvMmCocada = itemView.findViewById(R.id.tvMmCocada);
        tvPrecio = itemView.findViewById(R.id.tvPrecio);
        tvStock = itemView.findViewById(R.id.tvStock);


        tvMarca.setText(Lista.get(i).getMarcaNeumatico());
        tvAncho.setText(""+Lista.get(i).getAncho());
        tvDiametro.setText(""+Lista.get(i).getDiametro());
        tvPerfil.setText(""+Lista.get(i).getPerfil());
        tvMmCocada.setText(""+Lista.get(i).getMmCocada());
        tvPrecio.setText(""+Lista.get(i).getPrecio());
        tvStock.setText(""+Lista.get(i).getStock());

        return itemView;
    }
}
