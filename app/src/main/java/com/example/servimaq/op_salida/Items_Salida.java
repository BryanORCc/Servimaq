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
import android.widget.Button;
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
    Button btnOk;

    String Cantidad = "";

    public Items_Salida(seleccionar_pedido_salida contextoSPS, ArrayList<Items_Salida_set_get> ListaS) {
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
        btnOk = itemView.findViewById(R.id.btnOk);

        int cantidad = (int) ListaS.get(i).getCantidad();

        tvItemId.setText(ListaS.get(i).getItemId());
        etCantidad.setText(String.valueOf(cantidad));
        tvPrecio.setText("" + ListaS.get(i).getPrecio());
        tvTotal.setText("" + ListaS.get(i).getTotal());
        tvNombreMarca.setText("" + ListaS.get(i).getNombreMarca());
        tvTipoVehiculo.setText("" + ListaS.get(i).getTipoVehiculo());

        String Precio = ListaS.get(i).getPrecio();
        String codPedido = ListaS.get(i).getCodPedido(),
                LlantaId = ListaS.get(i).getLlantaId();

        //INICIAR CONEXION CON EL SERVICIO WEB - HEROKU
        AndroidNetworking.initialize(contextoSPS);
        Map<String, String> insertar = new HashMap<>();

        //ACTUALIZAR CANTIDAD*************************************
        etCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int p, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int p, int i1, int i2) {

                //--**ACTUALIZAR DATOS DE LA TABLA MEDIDA------------------------------------------------------------------------------::::
                Cantidad = String.valueOf(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //CONFIRMAR CAMBIOS*****************************************
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("cantidad: ", Cantidad);
                insertar.put("Cantidad", Cantidad);
                insertar.put("Cantidad2", Cantidad);
                insertar.put("codPedido", codPedido);
                insertar.put("LlantaId", LlantaId);
                JSONObject datosJSON = new JSONObject(insertar);

                Log.e("cantidad: ", Cantidad);
                Log.e("Cantidad2: ", Cantidad);
                Log.e("codPedido: ", codPedido);
                Log.e("LlantaId: ", LlantaId);

                AndroidNetworking.post("https://whispering-sea-93962.herokuapp.com/T_Listado_POST_UPDATE.php")
                        .addJSONObjectBody(datosJSON)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String validarDatos = response.getString("data");

                                    Log.e("respuesta actualizacion: ", "" + validarDatos);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(contextoSPS, "Error:" + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                            }
                        });//FIN DEL EVENTO DE HEROKU DB - UPDATE------------------------------------------------

                //QUITAR ANIMACION DE CARGA DE VISTA*****************************
                contextoSPS.overridePendingTransition(0, 0);
                contextoSPS.overridePendingTransition(0, 0);
                itemView.getContext().startActivity(contextoSPS.getIntent());
            }
        });



        return itemView;
    }

}
