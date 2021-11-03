package com.example.servimaq.op_salida;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.R;
import com.example.servimaq.db.Items_Salida_set_get;
import com.example.servimaq.db.SQLConexion;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Items_Salida extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<Items_Salida_set_get> ListaS;
    seleccionar_pedido_salida contextoSPS;
    Context context;

    TextView tvItemId;
    EditText etCantidad;
    TextView tvPrecio, tvTotal, tvNombreMarca, tvTipoVehiculo;

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

        Map<String,String> insertar = new HashMap<>();

        //ACTUALIZAR CANTIDAD*************************************
        etCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int p, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int p, int i1, int i2) {

                //--**ACTUALIZAR DATOS DE LA TABLA MEDIDA------------------------------------------------------------------------------::::
                insertar.put("Cantidad",String.valueOf(charSequence));
                insertar.put("Precio", String.valueOf(Precio));
                insertar.put("Cantidad2",String.valueOf(charSequence));
                insertar.put("codPedido",codPedido);
                insertar.put("LlantaId",LlantaId);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        JSONObject datosJSON = new JSONObject(insertar);
        //CONFIRMAR CAMBIOS*****************************************
        etCantidad.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i==keyEvent.KEYCODE_ENTER){

                    AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Listado_POST_UPDATE.php")
                            .addJSONObjectBody(datosJSON)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        String validarDatos = response.getString("data");
                                        Log.e("respuesta actualizacion: ",""+validarDatos);
                                        itemView.getContext().startActivity(contextoSPS.getIntent());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(contextoSPS,"Error:" + anError.getErrorDetail(),Toast.LENGTH_SHORT).show();
                                }
                            });//FIN DEL EVENTO DE HEROKU DB - UPDATE------------------------------------------------

                    //QUITAR ANIMACION DE CARGA DE VISTA*****************************
                    contextoSPS.overridePendingTransition(0, 0);
                    contextoSPS.overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });

        return itemView;
    }

}
