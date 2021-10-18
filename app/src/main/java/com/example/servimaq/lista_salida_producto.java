package com.example.servimaq;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista_salida_P;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class lista_salida_producto extends BaseAdapter {

    Context c;
    LayoutInflater inflater;
    ArrayList<items_lista_salida_P> Lista;
    Salida_Prod contextosp;

    ArrayList<String> items_lista = new ArrayList<>();

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

        ListView lvitems;

        inflater = (LayoutInflater) contextosp.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.activity_listar_salidaa_, viewGroup, false);

       // lvitems = itemView.findViewById(R.id.lvitems);
        tvCodigo = itemView.findViewById(R.id.tvCodigo);
        tvnombreyapellido = itemView.findViewById(R.id.tvnombreyapellido);
        tvcorreo = itemView.findViewById(R.id.tvcorreo);
        tvmodopago = itemView.findViewById(R.id.tvmodopago);
        tvfechaentrega = itemView.findViewById(R.id.tvfechaentrega);
        tvDNI = itemView.findViewById(R.id.tvDNI);

        tvCodigo.setText(""+Lista.get(i).getTvCodigo());
        tvnombreyapellido.setText(""+Lista.get(i).getTvnombreyapellido());
        tvcorreo.setText(""+Lista.get(i).getTvcorreo());
        tvmodopago.setText(""+Lista.get(i).getTvmodopago());
        tvfechaentrega.setText(""+Lista.get(i).getTvfechaentrega());
        tvDNI.setText(""+Lista.get(i).getTvDNI());

        /*Intent intent = new Intent(contextosp,Salida_Prod.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/

        //****************************************************************************************************
        /*try {
            SQLConexion conexion =new SQLConexion();
            Statement st = conexion.ConexionDB(contextosp).createStatement();
            ResultSet rs = st.executeQuery("");

            if (!rs.next()) {
                Toast.makeText(contextosp,"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }else {
                do {
                    //CARGAR DATOS A LOS TEXT VIEW--------------------------------------

                } while (rs.next());///va agregando cada ID
            }
            rs.close();

        } catch (Exception e) {
            Toast.makeText(contextosp,e.getMessage(),Toast.LENGTH_SHORT).show();
        }//FIN SELECT-------------*/

        return itemView;
    }

}
