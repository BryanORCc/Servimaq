package com.example.servimaq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.pedido_lista;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Pedido_Catalogo extends BaseAdapter {

    Context c;
    LayoutInflater inflater;
    ArrayList<pedido_lista> Lista;

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

        TextView tvcodigo,tvNombre, tvApellido, tvCorreo, tvFechaActual, tvFechaEntrega, tvPModoPago, tvDni;
        Button btnborrar;

        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.pedido_lista, viewGroup, false);
        SQLConexion db = new SQLConexion();
        tvcodigo=itemView.findViewById(R.id.tvCodigo);
        tvNombre = itemView.findViewById(R.id.tvNombre);
        tvApellido = itemView.findViewById(R.id.tvApellidos);
        tvCorreo = itemView.findViewById(R.id.tvDCorreo);
        tvFechaActual = itemView.findViewById(R.id.tvFechaA);
        tvFechaEntrega = itemView.findViewById(R.id.tvFechaE);
        tvPModoPago = itemView.findViewById(R.id.tvModoP);
        tvDni = itemView.findViewById(R.id.tvDni);
        btnborrar=itemView.findViewById(R.id.btnborrar);

        tvcodigo.setText(Lista.get(i).getCodigo());
        tvNombre.setText(Lista.get(i).getNombre());
        tvApellido.setText(""+Lista.get(i).getApellidos());
        tvCorreo.setText(""+Lista.get(i).getCorreo());
        tvFechaActual.setText(""+Lista.get(i).getFechaA());
        tvFechaEntrega.setText(""+Lista.get(i).getFechaP());
        tvPModoPago.setText(""+Lista.get(i).getModo());
        tvDni.setText(""+Lista.get(i).getDni());

        btnborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SQLConexion conexion =new SQLConexion();
                    Statement st = db.ConexionDB(c).createStatement();
                    ResultSet rs = st.executeQuery("select  from T_Pedido");
                    if (!rs.next()) {
                        String MedidaLlantaId = "no hay";
                    }
                    else {

                         st.executeQuery("delete * from T_Pedido");
                    }
                }

                catch (Exception e) {
                    Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
                }


            }
        });


        return itemView;


    }

}
