package com.example.servimaq.op_salida;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.R;
import com.example.servimaq.db.Items_Salida_set_get;
import com.example.servimaq.db.SQLConexion;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class seleccionar_pedido_salida extends AppCompatActivity {

    ListView lvitems;
    TextView tvcodPedido;
    ArrayList<Items_Salida_set_get> salida = new ArrayList<>();
    Items_Salida i_salida;
    String codPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_pedido_salida);

        //HABILITAR BOTON - ATRAS - EN LA BARRA DE NAVEGACION************
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);

        lvitems = findViewById(R.id.lvitems);
        tvcodPedido = findViewById(R.id.tvcodPedido);

        Intent recibir_codigo = getIntent();
        codPedido = recibir_codigo.getStringExtra("codPedido");

        tvcodPedido.setText(codPedido);

        SQLConexion conexion = new SQLConexion();
        try {
            Statement st = conexion.ConexionDB(getApplicationContext()).createStatement();
            ResultSet rs = st.executeQuery("select LS.ItemId, LS.Cantidad, LS.Precio, LS.Total, DL.NombreMarca, V.TipoVehiculo, LS.codPedido, LL.LlantaId from T_Listado LS " +
                    "inner join T_Llanta LL on LS.LlantaId = LL.LlantaId inner join T_DetalleLlanta DL on DL.DetalleLlantaId = LL.DetalleLlantaId " +
                    "inner join T_Vehiculo V on V.VehiculoId = LL.VehiculoId where LS.codPedido = '"+codPedido+"';");
            Log.e("CODIGO PEDIO____","::: "+codPedido);
            if (!rs.next()) {
                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }else {
                do {
                    salida.add(new Items_Salida_set_get(rs.getString(1),rs.getInt(2),rs.getDouble(3),rs.getDouble(4),rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8)));
                    Log.e("DATOS____","::: "+rs.getString(1));
                } while (rs.next());///va agregando cada ID
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }//FIN SELECT-------------
        i_salida = new Items_Salida(seleccionar_pedido_salida.this,salida);
        lvitems.setAdapter(i_salida);

    }
}