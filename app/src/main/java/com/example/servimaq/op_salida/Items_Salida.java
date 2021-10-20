package com.example.servimaq.op_salida;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.R;
import com.example.servimaq.db.Items_Salida_set_get;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista;
import com.example.servimaq.menu_opciones;
import com.example.servimaq.op_catalogo.Catalogo;
import com.example.servimaq.seleccionar_pedido_salida;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Items_Salida extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<Items_Salida_set_get> ListaS;
    seleccionar_pedido_salida contextoSPS;
    Context context;

    TextView tvItemId;
    EditText etCantidad;
    TextView tvPrecio, tvTotal, tvNombreMarca, tvTipoVehiculo;

    //int n = 0;
    /*if(n <= ListaS.size()){
            n++;
        }*/

    //--ACTUALIZAR DATOS DE LOS ITEMS------------------------------------------------------------------------------
    SQLConexion conexion =new SQLConexion();
    SQLConexion db = new SQLConexion();
    PreparedStatement ps;

    public Items_Salida(seleccionar_pedido_salida contextoSPS, ArrayList<Items_Salida_set_get> ListaS ){
        this.contextoSPS = contextoSPS;
        this.ListaS = ListaS;
    }

    @Override
    public int getCount() {
        return ListaS.size();
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

        inflater = (LayoutInflater) contextoSPS.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.items_salida, viewGroup, false);

        tvItemId = itemView.findViewById(R.id.tvItemId);
        etCantidad = itemView.findViewById(R.id.etCantidad);
        tvPrecio = itemView.findViewById(R.id.tvPrecio);
        tvTotal = itemView.findViewById(R.id.tvTotal);
        tvNombreMarca = itemView.findViewById(R.id.tvNombreMarca);
        tvTipoVehiculo = itemView.findViewById(R.id.tvTipoVehiculo);

        int cantidad = (int) ListaS.get(i).getCantidad();

        tvItemId.setText(ListaS.get(i).getItemId());
        etCantidad.setText(String.valueOf(cantidad));
        tvPrecio.setText(""+ListaS.get(i).getPrecio());
        tvTotal.setText(""+ListaS.get(i).getTotal());
        tvNombreMarca.setText(""+ListaS.get(i).getNombreMarca());
        tvTipoVehiculo.setText(""+ListaS.get(i).getTipoVehiculo());

        double Precio = ListaS.get(i).getPrecio();
        String codPedido = ListaS.get(i).getCodPedido(),
                LlantaId = ListaS.get(i).getLlantaId();

        //ACTUALIZAR CANTIDAD*************************************
        etCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int p, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int p, int i1, int i2) {


                try {
                    ps = conexion.ConexionDB(context).prepareStatement(
                            "update T_Listado set Cantidad = ?, Total = ? * ? where codPedido = ? and LlantaId = ?;");

                    ps.setInt(1, Integer.parseInt( String.valueOf(charSequence)));
                    ps.setDouble(2,Precio);
                    ps.setInt(3,Integer.parseInt( String.valueOf(charSequence)));
                    ps.setString(4,codPedido);
                    ps.setString(5,LlantaId);

                }
                catch (Exception e) {
                    //Toast.makeText(contextoSPS,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        //CONFIRMAR CAMBIOS*****************************************
        etCantidad.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i==keyEvent.KEYCODE_ENTER){

                    try {
                        ps.executeUpdate();
                        ps.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    //QUITAR ANIMACION DE CARGA DE VISTA*****************************
                    contextoSPS.overridePendingTransition(0, 0);
                    contextoSPS.overridePendingTransition(0, 0);
                    itemView.getContext().startActivity(contextoSPS.getIntent());
                    return true;
                }
                return false;
            }
        });

        return itemView;
    }

}
